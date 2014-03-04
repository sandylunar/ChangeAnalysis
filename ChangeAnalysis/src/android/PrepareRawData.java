package android;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.Connector;

public class PrepareRawData {

    public static ArrayList<String> readTags(String tagPath) throws IOException {
	ArrayList<String> tags = new ArrayList<String>();
	BufferedReader reader = new BufferedReader(new FileReader(tagPath));
	String line = "";
	while ((line = reader.readLine()) != null) {
	    tags.add(line);
	}
	reader.close();
	return tags;
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

	public static void addColumnDataset(int size) throws SQLException {
		Connector c = new Connector();
		Statement stmt = c.getNewStatement();
		for(int i = 1; i < size-1; i++){
			String tablename = i+"_"+(i+1);
			String alter = "alter table "+tablename+" drop column dataset";
			System.out.println(i+": "+alter);
			stmt.executeUpdate(alter);
			
			alter = "alter table "+tablename+" add dataset int(5) default "+i;
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
	
}
