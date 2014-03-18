package android;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import jxl.Cell;
import jxl.Sheet;
import database.Connector;

/**
 * 第三步 读取演化矩阵，计算预测因子的值 读入source的sheet2
 * 
 * @author Administrator
 * 
 */
public class CalculatePredictFactorsForSQL {

	private static final boolean p_debug = true;
	/**
	 * @param args
	 */

	static int BEGIN; // V1的列
	static String changeHistoryTable;
	static int DATASET; // dataset的列号

	static int END; // Vmax的列
	static int FISRTMODEL;
	static int LASTMODEL;
	static int MODULE;
	static String[] modules;
	static int OFFSET; // 从V1+offset+1开始，计算model，所以一共会产生Vmax-V1-offset个model
	static int rows;
	// static final int[] numberOfUC =
	// {186,189,195,221,276,310,303,339,360,394,394,426,452,461};//V0-Vmax的用例数
	static int UCNAME;

	private static int getModuleIndex(String module) {
		for (int i = 0; i < modules.length; i++) {
			if (module.equalsIgnoreCase(modules[i]))
				return i;
		}

		System.out.println("怎么找不到模块呢？" + module);
		return -1;
	}

	Connector c;
	String[] columns;
	HashMap<Integer, Double> distance = new HashMap<Integer, Double>();
	HashMap<Integer, Double> freq = new HashMap<Integer, Double>();
	HashMap<Integer, Double> fv = new HashMap<Integer, Double>();
	boolean isFirst;
	int lastChange;
	HashMap<Integer, Double> lastDistance = new HashMap<Integer, Double>();
	HashMap<Integer, Double> life = new HashMap<Integer, Double>(); // UC存在的版本编号
	HashMap<Integer, Double> occurence = new HashMap<Integer, Double>();

	int packageLevel;
	ResultSet rs;

	HashMap<Integer, Double> seq = new HashMap<Integer, Double>();
	Statement statement;
	HashMap<Integer, Double> topicContain;
	HashMap<Integer, String> topicMaps;
	HashMap<Integer, Double> topicSimilarity;
	HashMap<String, ArrayList<Integer>> volalitySinglePack;

	int[][][] volatility;

	private void actualChange(int currColumn, Integer id) throws SQLException {
		if (currColumn < END) {
			String query = "select * from " + changeHistoryTable + " where id="
					+ id;
			rs = statement.executeQuery(query);
			int value = 0;
			if (rs.next())
				value = rs.getInt(currColumn + 1);
			rs.close();

			// 当新版本中为修改的状态时，才为1
			if (value == 1)
				fv.put(id, 1.0);
			else
				fv.put(id, 0.0);
		} else {
			fv.put(id, 0.0);
		}
	}

	void closeForMysql() throws SQLException {
		c.close();
	}

	/**
	 * establish empty tables in mysql for predict factors
	 * 
	 * @param tagSize
	 * @throws SQLException
	 */
	private void createTables(int tagSize) throws SQLException {
		String createTableSQL = null;
		columns = new String[] { "frequency", "distance", "lifecycle",
				"sequence", "occurrence", "recency", "pakvolality_single",
				"pakvolality_cum", "evolve_topic", "actual_change", "logistic",
				"predict_change" };

		for (int i = 1; i < tagSize ; i++) {
			statement.executeUpdate("drop table if exists " + i + "_" + (i + 1));
			createTableSQL = "create table if not exists " + i + "_" + (i + 1)
					+ "(id int primary key";
			for (String col : columns) {
				createTableSQL += ", " + col + " double(20,4) default 0";
			}
			createTableSQL += ")";

			// System.out.println("Mysql: " + createTableSQL);
			
			statement.executeUpdate(createTableSQL);

		}
	}

	private void freqAndDistance(Integer id, int currColumn)
			throws SQLException {
		String query = "select * from " + changeHistoryTable + " where id="
				+ id;
		rs = statement.executeQuery(query);
		if (rs.next()) {
			int value;
			double sumDis = 0;
			double frequence = 0;
			double d = 0;
			for (int prev = BEGIN; prev <= currColumn; prev++) {
				value = rs.getInt(prev);
				// 计算freq, occur
				// Cell中的值为1或者2时，Freq++， Occur++
				if (value == 1) {// ||grid. getContents().equals("2")
					frequence++;
					sumDis += currColumn - prev+1;
				}
			}
			freq.put(id, frequence);
			if(frequence!=0){
				d = sumDis/((frequence+1)*(currColumn-BEGIN+1));
			}
			else{ 
				double l = life.get(id);
				d = l/((frequence+1)*(currColumn-BEGIN+1));
				}
			
			distance.put(id, d);
			
			if(p_debug&&d==0){
				System.out.println(id);
				System.out.println();
			}
				
			
		}rs.close();
	}

	private Double getModuleVolatility(String moduleWrite, int currColumn) {
		if (moduleWrite == null)
			return new Double(0);
		int total = volatility[currColumn - BEGIN][getModuleIndex(moduleWrite)][0];
		double changes = volatility[currColumn - BEGIN][getModuleIndex(moduleWrite)][1];
		if (total == 0)
			return new Double(0);
		return new Double(changes / total);
	}

	@SuppressWarnings("unused")
	private Double getModuleVolatilityAVG(String moduleWrite, int currColumn) {
		double sum = 0;

		for (int i = BEGIN; i <= currColumn; i++) {
			sum += getModuleVolatility(moduleWrite, i).doubleValue();
		}

		return new Double(sum / (currColumn - BEGIN + 1));
	}

	/**
	 * Statistic the category of different packages.
	 * 
	 * @param changeTableName
	 * 
	 * @param i
	 *            Package Level, 0 represent the first-level dirs.
	 * @return
	 * @throws SQLException
	 */
	private String[] getPackageList(String changeTableName, int level)
			throws SQLException {
		HashSet<String> packageList = new HashSet<String>();
		HashSet<String> typeList = new HashSet<String>();
		String selectSQL = "select name from " + changeTableName;

		ResultSet rs = statement.executeQuery(selectSQL);
		int count = 0;
		while (rs.next()) {
			count++;
			String name = rs.getString("name");
			String type;
			int dotLoc = name.lastIndexOf('.');
			if (dotLoc == -1)
				type = name;
			else
				type = name.substring(dotLoc);
			String[] tokens = name.split("/");

			typeList.add(type);
			if (tokens.length == 1)
				continue;

			if (level > tokens.length - 2) {
				System.err.println("Package Level out of the boundary: "
						+ level + " > " + (tokens.length - 2));
				return null;
			}

			packageList.add(tokens[level]);

		}
		rs.close();
		rows = count;
		System.out.println("MySQL: scan " + count + ", find "
				+ packageList.toString());
		// System.out.println("File types: " + typeList.toString());
		return packageList.toArray(new String[] {});
	}

	private String getPackageName(Integer fileID) throws SQLException {
		String name = getFileName(fileID);
		if (name.isEmpty())
			return null;
		String[] tokens = name.split("/");

		if (packageLevel <= tokens.length - 2) {
			return tokens[packageLevel];
		}
		return null;
	}

	private String getFileName(Integer fileID) throws SQLException {
		String querySQL = "select name from " + changeHistoryTable
				+ " where id=" + fileID;
		rs = statement.executeQuery(querySQL);
		String name = "";
		if (rs.next())
			name = rs.getString("name");
		rs.close();
		return name;
	}

	void initFactors() {
		freq = new HashMap<Integer, Double>();
		distance = new HashMap<Integer, Double>();
		occurence = new HashMap<Integer, Double>();
		life = new HashMap<Integer, Double>(); // UC存在的版本编号
		lastDistance = new HashMap<Integer, Double>();
		fv = new HashMap<Integer, Double>();
		seq = new HashMap<Integer, Double>();
	}

	void initForMysql(String changeTableName, int packageLevel)
			throws SQLException {
		c = new Connector();
		statement = c.getNewStatement();
		BEGIN = 3; // V1的列
		END = 31; // Vmax的列
		OFFSET = 0; // 从V1+offset+1开始，计算model，所以一共会产生Vmax-V1-offset个model

		// static final int[] numberOfUC =
		// {186,189,195,221,276,310,303,339,360,394,394,426,452,461};//V0-Vmax的用例数
		//UCNAME = 2;
		this.packageLevel = packageLevel;
		modules = getPackageList(changeTableName, packageLevel);
		volatility = new int[END - BEGIN + 1][modules.length][2];
	}

	/*
	 * 判断这一行是否是后期新增的需求
	 */
	private boolean isFutureAddedFile(int currColumn, int fileID)
			throws SQLException {
		for (int t = currColumn + 1; t <= END; t++) {
			String query = "select * from " + changeHistoryTable + " where id="
					+ fileID;
			rs = statement.executeQuery(query);
			int value = 0;
			if (rs.next()){
				value = rs.getInt(t);
			}rs.close();
			if (value == 2)
				return true;
		}
		return false;
	}

	/*
	 * 判断这一行是否为删除的需求
	 */
	private boolean isHistoricDeletedFile(int currColumn, int fileID)
			throws SQLException {
		for (int t = BEGIN; t <= currColumn; t++) {
			String query = "select * from " + changeHistoryTable + " where id="
					+ fileID;
			rs = statement.executeQuery(query);
			int value = 0;
			if (rs.next()){
				value = rs.getInt(t);
				}rs.close();

			if (value == -1)
				return true;
		}
		return false;
	}

	private void lifecycle(int currColumn, int fileID) throws SQLException {
		String query = "select * from " + changeHistoryTable + " where id="
				+ fileID;
		rs = statement.executeQuery(query);

		if (rs.next()) {
			for (int t = BEGIN; t <= currColumn; t++) {
				int value = rs.getInt(t);
				if (value == 2) {
					life.put(fileID, currColumn - t + 1.0);
					isFirst = false;
				}
			}
		}

		if (isFirst)
			life.put(fileID, currColumn - BEGIN + 2.0); // 第一个版本中新增的
	}

	@SuppressWarnings("unused")
	private void normalize(int currColumn, Integer id) {
		if (freq.get(id) != null) {
			Double b = freq.get(id);
			freq.put(id, b / (currColumn - 1));
		}
	}

	private void occurrence(Integer id) {
		if (freq.get(id) != null) {
			double value = freq.get(id) * life.get(id);
			if(value!=0)
				occurence.put(id, distance.get(id) / value);
			else
				occurence.put(id,0.0);
		}
	}

	public void readAndCalculateFromMySQL(String changeTableName, int tagSize,
			int packageLevel) throws SQLException {
		changeHistoryTable = changeTableName;
		initForMysql(changeTableName, packageLevel);
		createTables(tagSize);

		boolean isDel = false;
		boolean isAdd = false;

		//TODO start from 3/BEGIN to 31,finish 5
		for (int currColumn = 3; currColumn <= 31; currColumn++) {  
			System.out.println("Working on for column: " + currColumn);
			initFactors();
			LinkedList<Integer> fileIDs = new LinkedList<Integer>(); // store
																		// the
																		// file
																		// IDs
																		// existing
																		// in
																		// the
																		// current
																		// column

			System.out
					.println("Working on Lifecycle, Sequency, Recency, Frequency, Distance, Occurrence, and actual_changes");
			
			String query = "select id from "+changeTableName;
			Statement statementIDs = c.getNewStatement();
			ResultSet rsIDs = statementIDs.executeQuery(query);
			while(rsIDs.next()){
				int fileID = rsIDs.getInt("id");
				
				if (fileID % 1000 == 0)
					System.out.println(" " + fileID);
				isFirst = true;
				lastChange = BEGIN;

				isDel = isHistoricDeletedFile(currColumn, fileID);
				isAdd = isFutureAddedFile(currColumn, fileID);

				if (!isDel && !isAdd) {
					fileIDs.add(new Integer(fileID));

					// 计算当前版本每个用例的寿命 life
					
					lifecycle(currColumn, fileID);

					// 计算当前版本的连续变更的次数
					seqAndRecency(currColumn, fileID);
					freqAndDistance(fileID, currColumn);
					
					//normalize(currColumn, fileID);
					occurrence(fileID);
					actualChange(currColumn, fileID);
					readVolatility(new Integer(fileID), currColumn);
				}
				
			}
			rsIDs.close();
			statementIDs.close();
			
			System.out.println("Working on single and average PackageVolality");

			// 输出
			String currTableName = (currColumn - BEGIN + 1) + "_"
					+ (currColumn - BEGIN + 2);
			System.out.println("MySQL: Writing Factors to TABLE: "
					+ currTableName);

			double value;
			
			for (Integer j : fileIDs) {
				String insertSQL=null;
				
				 insertSQL = "insert into "
						+ currTableName
						+ " (id,frequency,distance,lifecycle,sequence,occurrence,recency,pakvolality_single,pakvolality_cum,evolve_topic,actual_change,logistic,predict_change) values (?";

				for (int i = 0; i < columns.length; i++) {
					insertSQL += ",?";
				}

				insertSQL += ")";

				if (j % 1000 == 0)
					System.out.println(j+": "+insertSQL);

				String moduleWrite = getPackageName(j);

				PreparedStatement preStmt = c
						.getNewPreparedStatement(insertSQL);
				preStmt.setInt(1, j.intValue());
				// preStmt.setString(2, name);
				value = freq.get(j) == null ? 0.0 : freq.get(j);
				preStmt.setDouble(2, value);

				value = distance.get(j) == null ? 0.0 : distance.get(j);
				preStmt.setDouble(3, value);

				value = life.get(j) == null ? 0.0 : life.get(j);
				preStmt.setDouble(4, value);

				value = seq.get(j) == null ? 0.0 : seq.get(j);
				preStmt.setDouble(5, value);

				value = occurence.get(j) == null ? 0.0 : occurence.get(j);
				preStmt.setDouble(6, value);

				value = lastDistance.get(j) == null ? 0.0 : lastDistance.get(j);
				preStmt.setDouble(7, value);

				value = getModuleVolatility(moduleWrite, currColumn);
				preStmt.setDouble(8, value);

				//value = getModuleVolatilityAVG(moduleWrite, currColumn);
				value = getModuleVolatilityAVGFromPrev(j,currColumn,moduleWrite);
				preStmt.setDouble(9, value);
				preStmt.setDouble(10, 0.0);

				value = fv.get(j) == null ? 0 : fv.get(j);
				preStmt.setDouble(11, value);
				preStmt.setDouble(12, 0.0);
				preStmt.setDouble(13, 0.0);

				try{
					preStmt.executeUpdate();
				}catch(SQLException e){
					continue;
				}
				preStmt.close();

				//System.out.println("MySQL: " + preStmt.toString());
			}

			freq.clear();
			distance.clear();
			fv.clear();
			life.clear();
		}

		System.out.println("Done...");
		closeForMysql();

	}

	/**
	 * Bug
	 * @param j
	 * @param currColumn
	 * @param moduleWrite
	 * @return
	 * @throws SQLException
	 */
	private double getModuleVolatilityAVGFromPrev(Integer j, int currColumn,String moduleWrite) throws SQLException {
		double single = getModuleVolatility(moduleWrite, currColumn);
		if(currColumn==BEGIN)
			return single;
		
		String prev = (currColumn - BEGIN ) + "_"
		+ (currColumn - BEGIN + 1);
		double value = 0.0;
		rs = statement.executeQuery("select id from "+changeHistoryTable+" where name like \'"+moduleWrite+"%\'");
		ArrayList<Integer> ids = new ArrayList<Integer>();
		while(rs.next()){
			int id = rs.getInt("id");
			ids.add(id);
		}rs.close();
		
		for(Integer id: ids){
			rs = statement.executeQuery("select pakvolality_cum from "+prev +" where id="+id);
			if(rs.next()){
				value = rs.getDouble("pakvolality_cum");
				rs.close();
				break;
			}
			rs.close();
		}
		return (value*(currColumn-BEGIN)+single)/(currColumn-BEGIN+1);
	}

	void readVolatility(Sheet srcSheet) {
		Cell grid;

		boolean isDel = false;
		boolean isAdd = false;
		String cont;

		// 读取每一行，计算当前列的模块挥发度
		for (int row = 1; row < srcSheet.getRows(); row++) {

			for (int moduleIndex = BEGIN; moduleIndex <= END; moduleIndex++) {
				isDel = false;
				isAdd = false;

				// 去掉删除的和新增的

				// 判断这一行是否为删除的需求
				for (int t = BEGIN; t <= moduleIndex; t++) {
					cont = srcSheet.getCell(t, row).getContents();
					if (cont.equals("-1")) {
						isDel = true;
						break;
					}
				}

				// 判断这一行是否是后期新增的需求
				for (int t = moduleIndex + 1; t <= END; t++) {
					cont = srcSheet.getCell(t, row).getContents();
					if (cont.equals("2")) {
						isAdd = true;
						break;
					}
				}

				if ((!isDel) && (!isAdd)) {

					grid = srcSheet.getCell(0, row);
					cont = grid.getContents();

					// 查找module的index
					int moduleLocation = getModuleIndex(cont);

					// 总数上+1

					cont = srcSheet.getCell(moduleIndex, row).getContents();
					int tmp = volatility[moduleIndex - BEGIN][moduleLocation][0];
					volatility[moduleIndex - BEGIN][moduleLocation][0] = tmp + 1;

					if (cont.equals("1") || cont.equals("2")) {
						tmp = volatility[moduleIndex - BEGIN][moduleLocation][1];
						volatility[moduleIndex - BEGIN][moduleLocation][1] = tmp + 1;
					}
				}
			}
		}

		for (int i = 0; i < END - BEGIN; i++) {
			System.out.print("Version = " + i);

			for (int k = 0; k < modules.length; k++) {
				System.out.print(" ModuleIndex = " + k + " Total = "
						+ volatility[i][k][0] + " Changes = "
						+ volatility[i][k][1] + "\n");
			}

		}
	}

	private void readVolatility(Integer fileID, int currColumn)
			throws SQLException {
		String pakname;

		// 读取每一行，计算当前列的模块挥发度
		pakname = getPackageName(fileID);

		// 查找module的index
		if (pakname == null || pakname.isEmpty())
			return;

		int moduleLocation = getModuleIndex(pakname);

		// 总数上+1
		int value = 0;
		String query = "select * from " + changeHistoryTable + " where id="
				+ fileID;
		rs = statement.executeQuery(query);
		if (rs.next()) {
			value = rs.getInt(currColumn);
		}rs.close();

		int tmp = volatility[currColumn - BEGIN][moduleLocation][0];
		volatility[currColumn - BEGIN][moduleLocation][0] = tmp + 1;

		if (value == 1 ) {
			tmp = volatility[currColumn - BEGIN][moduleLocation][1];
			volatility[currColumn - BEGIN][moduleLocation][1] = tmp + 1;
		}

	}

	private void seqAndRecency(int currColumn, int fileID) throws SQLException {
		int sequence = 0;
		int last = 0;
		int added = BEGIN-1;

		String query = "select * from " + changeHistoryTable + " where id="
				+ fileID;
		rs = statement.executeQuery(query);
		if (rs.next()) {
			int value;
			for (int t = BEGIN; t <= currColumn; t++) {
				value = rs.getInt(t);
				if (value == 1) {
					if (last == t - 1)
						sequence++;
					else
						sequence = 1;

					last = t;
					lastChange = t;
				}
				else if(value ==2){
					added = t;
				}
			}
		}rs.close();

		if (currColumn == 2)
			seq.put(fileID, 0.0);
		else
			seq.put(fileID, sequence+0.0);
			//seq.put(fileID, sequence / (currColumn - 2.0));
		
		if(last == 0){//never change after added
			lastDistance.put(fileID, currColumn - added + 1.0);
		}else
			lastDistance.put(fileID, currColumn - lastChange + 1.0);

	}

	public void updateMetrics(String changeTableName, int tagSize,
			int packageLevel) throws SQLException {

		changeHistoryTable = changeTableName;
		initForMysql(changeTableName, packageLevel);

		boolean isDel = false;
		boolean isAdd = false;

		//TODO start from 3/BEGIN to 31,finish 5
		for (int currColumn = 3; currColumn <= 31; currColumn++) {  
			System.out.println("Working on for column: " + currColumn);
			initFactors();
			LinkedList<Integer> fileIDs = new LinkedList<Integer>(); // store
																		// the
																		// file
																		// IDs
																		// existing
																		// in
																		// the
																		// current
																		// column

			System.out
					.println("Working on Lifecycle, Sequency, Recency, Frequency, Distance, Occurrence, and actual_changes");
			
			String query = "select id from "+changeTableName+";";
			Statement statementIDs = c.getNewStatement();
			ResultSet rsIDs = statementIDs.executeQuery(query);
			while(rsIDs.next()){
				
				int fileID = rsIDs.getInt("id");
				
				if (fileID % 1000 == 0)
					System.out.println(" " + fileID);
				isFirst = true;
				lastChange = BEGIN;

				isDel = isHistoricDeletedFile(currColumn, fileID);
				isAdd = isFutureAddedFile(currColumn, fileID);

				if (!isDel && !isAdd) {
					fileIDs.add(new Integer(fileID));
					

					// 计算当前版本每个用例的寿命 life
					//TODO
					lifecycle(currColumn, fileID);

					// 计算当前版本的连续变更的次数
					//seqAndRecency(currColumn, fileID);
					freqAndDistance(fileID, currColumn);
					
					////normalize(currColumn, fileID);
					//occurrence(fileID);
					//actualChange(currColumn, fileID);
					//readVolatility(new Integer(fileID), currColumn);
				}
				
			}
			rsIDs.close();
			statementIDs.close();
			
			// 输出
			String currTableName = (currColumn - BEGIN + 1) + "_"
					+ (currColumn - BEGIN + 2);
			System.out.println("MySQL: Writing Factors to TABLE: "
					+ currTableName);

			double value,freqV,distanceV;
			for(Integer j : fileIDs){
				value = lastDistance.get(j) == null ? 0.0 : lastDistance.get(j);
				freqV = freq.get(j) == null ? 0.0 : freq.get(j);
				distanceV = distance.get(j) == null ? 0.0 : distance.get(j);
				//value = life.get(j) == null ? 0.0 : life.get(j);
				//String updateSQL = "update "+currTableName+" set recency = "+value+", frequency = "+freqV+", distance = "+distanceV+" where id = "+j;
				String updateSQL = "update "+currTableName+" set distance = "+distanceV+" where id = "+j;
				statement.execute(updateSQL);
				System.out.println(updateSQL);
			}
			

			freq.clear();
			distance.clear();
			fv.clear();
			life.clear();
		}

		System.out.println("Done...");
		closeForMysql();
	}
}
