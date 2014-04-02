package linux;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.Connector;

public class PrepareRawData {
	
	ArrayList<String> tags = new ArrayList<String>();
	Connector connector = new Connector();
	Statement statement = connector.getNewStatement();
	String commitTableName;
	String[] commitTableColumns = {"sha", "evolution_ID","author","author_date","committer","commit_date","title","summary","status","detail_change"};

    private static boolean debug = true;

	public ArrayList<String> readTags(String tagPath) throws IOException {
	BufferedReader reader = new BufferedReader(new FileReader(tagPath));
	tags = new ArrayList<String>();
	String line = "";
	while ((line = reader.readLine()) != null) {
	    tags.add(line);
	}
	reader.close();
	return tags;
    }
	
	

    public ArrayList<String> getTags() {
		return tags;
	}



	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}



	public ArrayList<String> bubbleSort() {
		int size = tags.size();
    	int[] orders = new int[size];
    	
    	for(int i = 0; i<size;i++){
    		orders[i] = i;
    	}
		
		for (int i = 0; i < size; i++)
        {
            for (int j = i+1; j < size; j++)
            {
                if (compare(tags,orders[i],orders[j]))
                {
                    int temp = orders[i];
                    orders[i] = orders[j];
                    orders[j] = temp;
                }
            }
        }
		
		if(debug ){
			System.out.println("Sorted the ids: "+orders);
		}
		
		ArrayList<String> sortedtags = new ArrayList<String>();
		for(int index = 0; index<orders.length;index++){
			String tmp = tags.get(orders[index]);
			sortedtags.add(tmp);
		}
		
		
		if(debug ){
			System.out.println("Sorted the tags: "+sortedtags);
		}
		
		
		return sortedtags;
	}

	/**
	 * if(tags[i]>tags[j]), return 1)
	 * @param tags
	 * @param i
	 * @param j
	 * @return
	 */
    private static boolean compare(ArrayList<String> tags, int i, int j) {
		String[] ti = breakTags(tags.get(i));
		String[] tj = breakTags(tags.get(j));
		
		int comp = ti.length<tj.length?ti.length:tj.length;
		
		for(int m = 0; m < comp; m++){
			if(ti[m].equals(tj[m]))
				continue;
			if(compare(ti[m],tj[m]))
				return true;
			else
				return false;
		}
		
		return false;
	}

    /**
     * if(s1>s2), return 1)
     * @param s1
     * @param s2
     * @return
     */
	private static boolean compare(String s1, String s2) {
		if(isDigital(s1)&&isDigital(s2)){
			boolean b = Integer.valueOf(s1)>Integer.valueOf(s2)?true:false;
			return b ;
		}
		else if(!isDigital(s1)&&!isDigital(s2)){
			int f1 = findFirstDigital(s1);
			int f2 = findFirstDigital(s2);
			if(f1 == -1 || f2 == -1){
				//System.err.println("Error on "+s1+", "+s2);
				if(s1.compareToIgnoreCase(s2)>0)
					return true;
				else 
					return false;
			}
			String subv1 = s1.substring(f1);
			String subv2 = s2.substring(f2);
			
			if(!isDigital(subv1)||!isDigital(subv2))
				System.err.println("Error on "+s1+": "+subv1+", "+s2+": "+subv2);
			
			return Integer.valueOf(subv1)>Integer.valueOf(subv2)?true:false;
			
		}
		
		else{
			if(!isDigital(s1))
				return false;
			else
				return true;
		}
	}

	private static boolean isDigital(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true; 
	}
	

	private static String[] breakTags(String vi) {
		int firstdigital = findFirstDigital(vi);
		String[] sub = vi.substring(firstdigital).split("\\.|-|_");
		for(int i = 0; i<sub.length;i++){
			sub[i] = sub[i].trim();
		}
		return sub;
	}

	private static int findFirstDigital(String str) {
		Pattern pattern = Pattern.compile("[0-9]");
		Matcher matcher = pattern.matcher(str); 
		if(matcher.find()){
			return str.indexOf(matcher.group());
		}
		return -1;
	}

	public static void generateBat(String batPath, String targetDir,
	    String execDir, ArrayList<String> tags) throws IOException {
	StringBuffer buffer = new StringBuffer();
	File file = new File(batPath);
	if (file.exists())
	    file.delete();
	file.createNewFile();
	BufferedWriter output = new BufferedWriter(new FileWriter(file));

	buffer.append("md " + targetDir + "\n");
	buffer.append("cd " + execDir + "\n");

	int size = tags.size();

	for (int i = 0; i < size - 1; i++) {
	    String cmd = "git diff --name-status " + tags.get(i) + " "
		    + tags.get(i + 1) + " > " + targetDir + "\\"
		    + Integer.toString(i + 1) + "-" + Integer.toString(i + 2)
		    + ".txt\n";
	    buffer.append(cmd);
	}
	buffer.append("pause");
	output.write(buffer.toString());
	output.close();
    }

    public static void dumpRawTxtToMySQL(String tablename, String targetDir,
	    ArrayList<String> tags) throws SQLException, IOException { 

	String createTableSQL = getCreateTableSQL(tablename, tags.size());

	System.out.println("Create Table: " + createTableSQL);

	Connector c = new Connector();
	Statement stmt = c.getNewStatement();
	//stmt.executeUpdate("drop table if exists " + tablename);
	stmt.executeUpdate(createTableSQL);

	// 逐个读取txt文件
	for (int i = 0; i < tags.size() - 1; i++) {
	    String filePath = targetDir + "\\" + Integer.toString(i + 1) + "-"
		    + Integer.toString(i + 2) + ".txt";
	    BufferedReader reader = new BufferedReader(new FileReader(filePath));
	    System.out.println("Dumping "+filePath +" into Mysql");
	    String line = "";
	    while ((line = reader.readLine()) != null) {
		String[] tokens = line.split("\t");
		
		//写到mysql中
		String insertSQL = "insert into "+tablename+" (name,"+Integer.toString(i + 1) + "_"
		    + Integer.toString(i + 2)+") values(?,?)";
		

		String changeFlag = tokens[0];
		int changeInt = 0;
		
		if(changeFlag.equalsIgnoreCase("M"))
		    changeInt = 1;
		else if(changeFlag.equalsIgnoreCase("D"))
		    changeInt = -1;
		else if(changeFlag.equalsIgnoreCase("A"))
		    changeInt = 2;
		
		//如果不是第一次，先查询再写
		int count = 0;
		    String selectSQL = "select * from "+tablename +" where name='"+tokens[1]+"'";
		    ResultSet rs = stmt.executeQuery(selectSQL);
		    if(rs.next()){
			count++;
			rs.updateInt(Integer.toString(i + 1) + "_"
				    + Integer.toString(i + 2), changeInt);
			rs.updateRow();
			rs.close();
			System.out.println("MySQL: update "+tokens[1]+", "+Integer.toString(i + 1) + "_"
				    + Integer.toString(i + 2)+", "+changeInt);
		    }
		
		if(count==0){
			PreparedStatement preStmt = c.getNewPreparedStatement(insertSQL);
			preStmt.setString(1, tokens[1]);
			preStmt.setInt(2, changeInt);
			preStmt.executeUpdate();
			System.out.println("MySQL: "+insertSQL+" - "+tokens[1]+", "+changeInt);
		}
	    }
	    reader.close();
	}
	c.close();
    }

    private static String getCreateTableSQL(String tablename, int size) {
	String createTableSQL = "create table if not exists " + tablename
		+ "(id int primary key AUTO_INCREMENT, name varchar(1000)";

	for (int i = 0; i < size - 1; i++) {
	    createTableSQL += ", " + Integer.toString(i + 1) + "_"
		    + Integer.toString(i + 2) + " int(4) not null default 0";
	}
	createTableSQL += ")";
	return createTableSQL;
    }

    public static void dumpTagToMySQL(String tablename, ArrayList<String> tags)
	    throws SQLException {

	String createTableSQL = "create table if not exists " + tablename
		+ "(id int primary key AUTO_INCREMENT, name varchar(255))";

	String insertTagSQL = "insert into " + tablename + " (name) values(?)";

	Connector c = new Connector();
	Statement stmt = c.getNewStatement();
	//stmt.executeUpdate("drop table if exists " + tablename);
	stmt.executeUpdate(createTableSQL);
	System.out.println("MySQL: " + createTableSQL);

	PreparedStatement preStmt = c.getNewPreparedStatement(insertTagSQL);

	for (int i = 0; i < tags.size(); i++) {
	    preStmt.setString(1, tags.get(i));
	    preStmt.executeUpdate();
	    System.out.println("MySQL: " + insertTagSQL + " - " + tags.get(i));
	}

	c.close();
    }

	public static void alterTableColumnDataset(int size) throws SQLException {
		Connector c = new Connector();
		Statement stmt = c.getNewStatement();
		for(int i = 1; i < size-1; i++){
			String tablename = i+"_"+(i+1);
			//String alter = "alter table "+tablename+" drop column dataset";
			//System.out.println(i+": "+alter);
			//stmt.executeUpdate(alter);
			
			String alter = "alter table "+tablename+" add dataset int(5) default "+i;
			System.out.println(i+": "+alter);
			stmt.executeUpdate(alter);
			
		}
		c.close();
	}

	public static void assembleAllForCle(String tablename, int size) throws SQLException {
		Connector c = new Connector();
		Statement stmt = c.getNewStatement();
		
		for(int i = 1; i < size-1; i++){
			//create table newsheet  select * from sheet1 union all select * from sheet2
			String subtable,query;
			subtable = i+"_"+(i+1);
			if(i==1){
				String drop = "drop table if exists "+tablename;
				stmt.executeUpdate(drop);
				System.out.println(i+": "+drop);
				query = "create table if not exists "+tablename+" select * from "+subtable;
			}
			else{
				query = "insert into "+tablename+" select * from "+subtable;
			}
			System.out.println(i+": "+query);
			stmt.executeUpdate(query);
		}
		c.close();
	}

	public void generateSortedTags(String sortedTagPath, ArrayList<String> printTags) throws IOException {
		File file = new File(sortedTagPath); 
		if (file.exists())
		    file.delete();
		file.createNewFile();
		
		PrintWriter pw = new PrintWriter(new FileWriter(file));  
		for(String tag:printTags){
			pw.println(tag);
		}
		
		pw.close();
		
	}

	public void generateBatForLinux(String batPath, String targetDir,
			String execDir, int part) throws IOException {

		int size = tags.size();
		int partition = Math.abs(size/part);
		
		for(int i = 1; i <=part; i++){
			File file = new File(batPath+"_"+i+".sh");
			if (file.exists())
			    file.delete();
			file.createNewFile();
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			StringBuffer buffer = new StringBuffer();
			
			buffer.append("#!/bin/bash\n");
			buffer.append("mkdir " + targetDir + "\n"); //md for windows, mkdir for linux
			buffer.append("cd " + execDir + "\n");
			
			if(i==1){
				buffer.append("git log --pretty=fuller -p --stat " + tags.get(0) + " "
					     + " > " + targetDir + "/0_1.txt\n");
			}
			
			int upper = 0;
			
			if(i==part)
				upper = size;
			else
				upper = partition*i;
			
			for (int j = partition*(i-1); j < upper-1; j++) {
			    String cmd = "git log --pretty=fuller -p --stat " + tags.get(j) + " "
				    + tags.get(j + 1) + " > " + targetDir + "/"
				    + Integer.toString(j + 1) + "-" + Integer.toString(j + 2)
				    + ".txt\n";
			    buffer.append(cmd);
			    buffer.append("echo writing to: "+targetDir + "/"
					    + Integer.toString(j + 1) + "-" + Integer.toString(j + 2)
					    + ".txt\n");
			}
			buffer.append("pause");
			output.write(buffer.toString());
			output.close();
		}
		}



	public void dumpCommitsToMysql(String targetDir, int numTags,
			String commitTable) throws IOException {

		BufferedReader buffer;
		Commit commit;
		String prefix;
		int linenumber = 5155277;//1568000; 1565169
		commitTableName = commitTable;
		//TODO
		for (int i = 0; i < 1; i++) { //<numTags
			//File f = new File(targetDir + i + "_" + i + 1 + ".txt");
			File f = new File("G:\\Sharespace\\experiment\\112-113.txt");
			
			if (!f.exists())
				continue;

			buffer = new BufferedReader(new InputStreamReader(
					new FileInputStream(f)));
			commit = new Commit();
			commit.setEvolutionID("112-113");
			//commit.setEvolutionID(i + "_" + i + 1 );
			
			int len = 0;
			String line = "";
			line = buffer.readLine();

			if(linenumber>0){ //skip lines
				while(line!=null&&len<linenumber){
					if(linenumber==++len)
						System.out.println("line "+linenumber+": "+line);
					line = buffer.readLine();
				}
			}
			while (line != null) {
				prefix = "commit ";
				while(!isCommitStart(line)){
					line = buffer.readLine();
					len++;
				}
				commit.setSha(line.substring(prefix.length()));
				try {
					if(isExists(commit)){
						System.out.println("Line "+len+": Existed: "+commit.getSha());
						line = buffer.readLine();
						len++;
						continue;
						}
				} catch (SQLException e) {
					System.err.println("Wrong when parsing line-3: " + len
							+ "  "+line+" -- file: " + f.getPath());
					e.printStackTrace();
				}
				
				prefix = "Author:";
				line = buffer.readLine();
				len++;
				while(!line.startsWith(prefix)){
					line = buffer.readLine();
					len++;
				}
				commit.setAuthor(line.substring(prefix.length()).trim());

				prefix = "AuthorDate:";
				line = buffer.readLine();
				len++;
				while(!line.startsWith(prefix)){
					line = buffer.readLine();
					len++;
				}
				commit.setAuthorDate(line.substring(prefix.length()).trim());

				prefix = "Commit:";
				line = buffer.readLine();
				len++;
				while(!line.startsWith(prefix)){
					line = buffer.readLine();
					len++;
				}
				commit.setCommitter(line.substring(prefix.length()).trim());

				prefix = "CommitDate:";
				line = buffer.readLine();
				len++;
				while(!line.startsWith(prefix)){
					line = buffer.readLine();
					len++;
				}
				commit.setCommitDate(line.substring(prefix.length()).trim());

				line = buffer.readLine();
				len++;
				if (!line.isEmpty()&&!line.equals("---")) { // check format 1
					System.err.println("Wrong 1 when parsing line: " + len
							+ "  "+line+" -- file: " + f.getPath());
					break;
				}
				
				if(!line.equals("---")){
					line = buffer.readLine();
					len++;
					
					while(line==null || line.isEmpty()){
						line = buffer.readLine();
						len++;
					}
					commit.appendTitle(line.trim());
					

					line = buffer.readLine();
					len++;
					while(!line.equals("---")&&!line.trim().isEmpty()){
						commit.appendTitle(line);
						line = buffer.readLine();
						len++;
					}
					
					while(line==null || line.trim().isEmpty()){
						line = buffer.readLine();
						len++;
					}
					
					while (!line.equals("---")&&!isCommitStart(line)) {
						commit.appendSummary(line);
						line = buffer.readLine();
						len++;
					}
				}

				

				if(isCommitStart(line)) //如果沒有summary
					continue;
				
				line = buffer.readLine();
				len++;
				
				while(line!=null&&!line.trim().isEmpty()){ //status
					commit.appendStatus(line);
					line = buffer.readLine();
					len++;
				}
				
				while(line==null || line.isEmpty()){
					line = buffer.readLine();
					len++;
				}
				
				if(isCommitStart(line)) //如果沒有summary
					continue;
				
				while (!isCommitStart(line)) {
					commit.appendDetailChanges(line);
					line = buffer.readLine();
					len++;
					if(len%5000==0){
						System.out.println("line: "+len+", String.lenght = "+commit.getDetailChanges().length()+", content = "+line);
					}
				}

				if (!isCommitStart(line) && !commit.isEmpity()) {
					System.err.println("Wrong 2 when parsing line: " + len
							+ "  "+line+" -- file: " + f.getPath());
					break;
				} 
				
				try {
					submitCommit(commit,len);
				} catch (SQLException e) {
					System.err.println("Wrong 4 when parsing line: " + len
							+ "  "+line+" -- file: " + f.getPath());
					e.printStackTrace();
				}
				commit.flush();
				
			}
			buffer.close();

			System.out.println("len = " + len);
		}
	}
	
	



	private void submitCommit(Commit commit,int len) throws SQLException {
		

		
		String insertSQL = getInsertSQL("commitColumns");
		
		//if (len % 100 == 0){
			System.out.println(len+": "+insertSQL);
			System.out.println(commit);
		//	}
		
		PreparedStatement preStmt = connector.getNewPreparedStatement(insertSQL);
		preStmt.setString(1,commit.getSha());
		preStmt.setString(2, commit.getEvolutionID());
		preStmt.setString(3,commit.getAuthor());
		preStmt.setString(4,commit.getAuthorDate());
		preStmt.setString(5,commit.getCommitter());
		preStmt.setString(6,commit.getCommitDate());
		preStmt.setString(7, commit.getTitle());
		preStmt.setString(8, commit.getSummary());
		preStmt.setString(9, commit.getStatus());
		preStmt.setString(10, commit.getDetailChanges());
		
		System.out.println(preStmt);
		preStmt.executeUpdate();
		preStmt.close();
	}
	
	private boolean isExists(Commit commit) throws SQLException {
		String select = "select * from "+commitTableName+" where sha=\'"+commit.getSha()+"\'";
		ResultSet rs = statement.executeQuery(select);
		if(rs.next())
			return true;
		return false;
	}



	private String getInsertSQL(String type){
		String insertSQL = "";
		if(type.equalsIgnoreCase("commitColumns")){
			insertSQL = "insert into "+commitTableName+" ("+commitTableColumns[0];
			
			for(int i = 1; i<commitTableColumns.length;i++){
				insertSQL+=", "+commitTableColumns[i];
			}
			
			insertSQL+=") values (?";
			
			for(int i = 1; i<commitTableColumns.length;i++){
				insertSQL+=",?";
			}
			insertSQL+=")";
		}
		
		return  insertSQL;
	}



	public boolean isCommitStart(String nextline) {
		if(!nextline.startsWith("commit "))
			return false;
		String sub = nextline.substring(7);
		if(sub==null||sub.isEmpty())
			return false;
			
		for(int i = 0; i<sub.length();i++){
			char ch = sub.charAt(i);
			if(!Character.isLetterOrDigit(ch)){
				return false;
			}
		}
		return true;
	}
/*		
		String pattern = "[commit\\s]{1}[a-zA-Z0-9]+$";
		Pattern pt = Pattern.compile(pattern);
		Matcher mc = pt.matcher(nextline);
		if(mc.find())
			return true;
		return false;
	}*/



	public void createTable(String tableName) throws SQLException {
		String createSQL = "";
		if (tableName.equalsIgnoreCase("commits_in_all")) {
			createSQL = "create table if not exists "
					+ tableName
					+ "(id int primary key AUTO_INCREMENT, sha varchar(50), evolution_ID varchar(20), author varchar(100), author_date varchar(50), committer varchar(100), commit_date varchar(50), title text, summary text, status text, detail_change MEDIUMTEXT)";
		}
		statement.executeUpdate(createSQL);
		if(debug)
			System.out.println(createSQL);
	}



	public void close() {
		connector.close();
	}
	
	
}
