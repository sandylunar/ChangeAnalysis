package database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecoveryAndUpdate {
	
	PrintWriter pw;;  

	private static final boolean p_debug = true;
	
	List<String> latestFilenames = null;
	
	HashMap<String, Integer> packageCount = new HashMap<String, Integer>();
	
	int stablefiles = 0;
	
	 String[] includeTypes = null;

	private int level;

	public static void recoveryFromTheLastTable(String targetTableName, String outputTableName) throws SQLException {

		Connector c = new Connector();
		ResultSet rs;
		Statement statement = c.getNewStatement();
		PreparedStatement preStmt = null;
		
		String createTableSQL = "create table if not exists "
				+ outputTableName
				+ "(id int primary key, frequency double(20,4), distance double(20,4),lifecycle double(20,4),sequence double(20,4), recency double(20,4), predict_change double(20,4))";
		
		statement.executeUpdate(createTableSQL);

		String query = "select * from "+targetTableName;
		rs = statement.executeQuery(query);
		while(rs.next()){
			int id = rs.getInt("id");
			double freq = rs.getDouble("frequency");
			double newFreq = Math.round(freq*33);
			double dist = rs.getDouble("distance");
			double newDist = dist/newFreq;
			double life = rs.getDouble("lifecycle");
			double seq = rs.getDouble("sequence");
			double newSeq = Math.round(seq*32);
			
			double recency = rs.getDouble("recency");
			double predict_change = rs.getDouble("predict_change");
			
			String insertSQL = "insert into "+ outputTableName+" (id,frequency,distance,lifecycle,sequence,recency,predict_change) values (?,?,?,?,?,?,?)";
			preStmt = c
					.getNewPreparedStatement(insertSQL);
			preStmt.setInt(1, id);
			preStmt.setDouble(2, newFreq);
			preStmt.setDouble(3,newDist);
			preStmt.setDouble(4, life);
			preStmt.setDouble(5, newSeq);
			preStmt.setDouble(6, recency);
			preStmt.setDouble(7, predict_change);
			preStmt.executeUpdate();
		}
		rs.close();
		preStmt.close();
		c.close();
		
		
		
	}

	public static void filterDataToNewDB(String changeTableName, String[] includeTypes) throws SQLException {
		
		Connector c = new Connector();
		Statement statement = c.getNewStatement();
		String selectSQL = "select * from " + changeTableName;

		ResultSet rs = statement.executeQuery(selectSQL);
		while (rs.next()) {
			String filename = rs.getString("name");
			if(!includeTypes(filename,includeTypes)){
				rs.deleteRow();
				if(p_debug){
					System.out.println("Delete "+rs.getInt("id")+", "+filename);
				}
			}
		}
	}

	private static boolean includeTypes(String filename, String[] includeTypes) {
		int dotLoc = filename.lastIndexOf('.');
		String type = null;
		if(dotLoc<0){
			int splashLoc = filename.lastIndexOf('/');
			if(splashLoc<0)
				return false;
			type = filename.substring(dotLoc+1);
		}
		else
			type = filename.substring(dotLoc);
		
		for(String s : includeTypes){
			if(s.equalsIgnoreCase(type))
				return true;
		}
		
		return false;
	}

	public static void filterDataFromDBTwo(String changeTableName, int numTags,
			String[] includeTypes) throws SQLException {
		Connector c = new Connector();
		Statement statement = c.getNewStatement();

		String[] filteredTableNames = new String[numTags];
		filteredTableNames[0] = changeTableName;
		for (int i = 1; i < numTags; i++) {
			filteredTableNames[i] = Integer.toString(i) + "_"
					+ Integer.toString(i + 1);
		}

		for (String tableName : filteredTableNames) {
			if (p_debug)
				System.out.println("Filtering Table: " + tableName);

			String selectSQL = "select * from " + tableName;

			ResultSet rs = statement.executeQuery(selectSQL);
			while (rs.next()) {
				String filename = null;
				int id = rs.getInt("id");
				if (!tableName.equalsIgnoreCase(changeTableName)) {

					Statement statement2 = c.getNewStatement();
					ResultSet rs2 = statement2.executeQuery("select name from "
							+ changeTableName + " where id = " + id);
					if (rs2.next())
						filename = rs2.getString("name");
					else { // 如果没有找到说明是已经排除的，直接删了
						rs.deleteRow();
						if (p_debug && id%1000==0) {
							 System.out.println(tableName+": Delete "+id);
						}
						rs2.close();
						statement2.close();
						continue;
					}
					rs2.close();
					statement2.close();
				} else
					filename = rs.getString("name");

				if (filename == null)
					System.out.println(tableName + " id:" + id);
				if (!includeTypes(filename, includeTypes)) {
					rs.deleteRow();
					if (p_debug) {
						 System.out.println("Delete "+rs.getInt("id")+", "+filename);
					}
				}
			}
		}
	}

	public static void generateAllChanges(String tablename, String sourcetable) throws SQLException {
		
		
		String drop = "drop table if exists "+tablename;
		String query = "create table if not exists "+tablename+" select * from "+sourcetable+" where actual_change = 1";
		
		Connector c = new Connector();
		Statement stmt = c.getNewStatement();
		stmt.executeUpdate(drop);
		stmt.executeUpdate(query);
		if(p_debug){
			System.out.println(query);
		}
		c.close();
	}

	public void scanFile(String root) {
	      File dir = new File(root); 
	        File[] files = dir.listFiles(); 
	        
	        if (files == null) 
	            return ; 
	        for (int i = 0; i < files.length; i++) { 
	            if (files[i].isDirectory()) { 
	            	scanFile(files[i].getAbsolutePath()); 
	            } else { 
	                String strFileName = files[i].getAbsolutePath();
	                
	                if(includeTypes(strFileName,includeTypes)){
	                	String trimName = strFileName.substring(40); //TODO(22)
	                	if(!latestFilenames.contains(trimName)){
	                		pw.println("Stable file: "+trimName);
	                		stablefiles++;
	                		countPackage(trimName,level);
	                	}
	                }
	            } 
	        } 
	}
	
	private void countPackage(String name, int level) {
		String[] tokens = name.split("\\\\");//TODO"/"

		if (tokens.length == 1)
			return;

		if (level > tokens.length - 2) {
			System.err.println("Package Level out of the boundary: "
					+ level + " > " + (tokens.length - 2));
			return ;
		}
		Integer num = packageCount.get(tokens[level]);
		if(num == null || num == 0){
			packageCount.put(tokens[level], 1);
		}
		else{
			packageCount.put(tokens[level], num+1);
		}
	}

	public void scanFile(String root, String tablename,
			String[] includeTypes2, int level) throws SQLException, IOException {
		this.level = level;
		
			pw = new PrintWriter(new FileWriter(new File(System.getProperty("user.dir")+"\\output\\stable-files.txt")));
			latestFilenames = RecoveryAndUpdate.getLatestFileNames(tablename);
			includeTypes = includeTypes2;
			scanFile(root);
			pw.println(packageCount);
			pw.println("Stable files = "+stablefiles+" , latest file size = "+latestFilenames.size());
			pw.close();
		
	}

	private static List<String> getLatestFileNames(String tablename) throws SQLException {
		ArrayList<String> latestFilenames = new ArrayList<String>();
		Connector c = new Connector();
		Statement st1 = c.getNewStatement();
		Statement st2 = c.getNewStatement();
		
		ResultSet rs1 = st1.executeQuery("select id from "+tablename);
		while(rs1.next()){
			int id = rs1.getInt("id");
			ResultSet rs2 = st2.executeQuery("select name from change_history where id = "+id);
			if(rs2.next()){
				String f = rs2.getString("name");
				latestFilenames.add(f);
			}
			else{
				System.out.println("Wrong, cannot find name in change_history where id = "+id);
			}
				
		}
		return latestFilenames;
	}

	public void scanStableFiles(String output,String tablename, String pak) throws IOException, SQLException {
		PrintWriter pwt = new PrintWriter(new FileWriter(new File(output)));
		Connector c = new Connector();
		Statement st = c.getNewStatement();
		Statement st2 = c.getNewStatement();
		ResultSet rs,rs2;
		
		for(int i =2; i<30;i++){
			int count = 0;
			String query = "select id from "+tablename +" where frequency=0 and lifecycle="+(31-i);
			
			rs = st.executeQuery(query);
			while(rs.next()){
				count++;
				int id  = rs.getInt(1);
				rs2 = st2.executeQuery("select name from change_history where id="+id);
				if(rs2.next()){
					String name = rs2.getString(1);
					if(!pak.isEmpty())
						if(!name.startsWith(pak+"/"))
							continue;
					countPackage(name.substring(pak.length()+1), 0) ;
					}
				}
			
			pwt.println("VID="+i+": "+count);
			pwt.println(packageCount);
			System.out.println(packageCount);
			if(p_debug)
				System.out.println("VID="+i+": "+count);
		}
		c.close();
		pwt.close();
	}

	public void scanFreqFiles(String output, String tablename, int level, String pak) throws IOException, SQLException {
		PrintWriter pwt = new PrintWriter(new FileWriter(new File(output)));
		Connector c = new Connector();
		Statement st = c.getNewStatement();
		ResultSet rs1,rs2;
		String name="";
		rs1 = st.executeQuery("select id from "+tablename+" where frequency>3");
		while(rs1.next()){
			int id = rs1.getInt(1);
			Statement st2 = c.getNewStatement();
			rs2 = st2.executeQuery("select name from change_history where id="+id);
			if(rs2.next()){
				name = rs2.getString(1);
				if(!pak.isEmpty())
					if(!name.startsWith(pak+"/"))
						continue;
			}
			countPackage(name.substring(pak.length()+1),level);
		}
		pwt.println(packageCount);
		System.out.println(packageCount);
		pwt.close();
		c.close();
	}
}
