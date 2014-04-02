/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * 
 */
public class Connector {

    private String driverClass = "com.mysql.jdbc.Driver";;
    private static String url;
    private static String user;
    private static String pwd;

    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt;

    /** Creates a new instance of Connector */
    public Connector() {
	setUrl("jdbc:mysql://localhost:3306/linux");
	setUser("root");
	setPwd("123456");
	conn = getNewConnection();
    }
    
    public Connector(String url, String user, String pwd) {
	Connector.setUrl(url);
	Connector.setUser(user);
	Connector.setPwd(pwd);
	conn = getNewConnection();
    }

    public Connection getNewConnection() {
	try {
	    Class.forName(driverClass);
	    conn = DriverManager.getConnection(getUrl(), getUser(), getPwd());
	} catch (ClassNotFoundException ex) {
	    System.out.println("cannot load db drivers!");
	    System.exit(1);
	} catch (SQLException ex) {
	    System.out
		    .println("================database connection failed, database may not start================");
	    ex.printStackTrace();
	    System.exit(1);
	}
	return conn;
    }

    public Statement getNewStatement() {
	try {
	    if(conn == null)
		conn = getNewConnection();
	    //stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	    stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	    //stmt.setFetchSize(Integer.MIN_VALUE);
	} catch (SQLException e) {
	    e.printStackTrace();
	    System.exit(1);
	}

	return stmt;
    }

    public PreparedStatement getNewPreparedStatement(String str) {
	try {
	    //pstmt = conn.prepareStatement(str, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		pstmt = conn.prepareStatement(str);
		//pstmt.setFetchSize(Integer.MIN_VALUE);  
	   // pstmt.setFetchDirection(ResultSet.FETCH_REVERSE);
	} catch (SQLException e) {
	    e.printStackTrace();
	    System.exit(1);
	}

	return pstmt;
    }

    public void close() {
	if (stmt != null) {
	    try {
		stmt.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	    stmt = null;
	}

	if (pstmt != null) {
	    try {
		pstmt.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	    pstmt = null;
	}

	if (conn != null) {
	    try {
		conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	    conn = null;
	}
    }

    public static void main(String[] args) throws Exception {
	Connector c = new Connector();
	Statement stmt = c.getNewStatement();
	stmt.executeUpdate("create  table if not exists test(id int primary key)");
	stmt.executeUpdate("insert into test values(12)"); 
	c.close();
	
	System.out.println(12);
    }

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		Connector.url = url;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		Connector.user = user;
	}

	public static String getPwd() {
		return pwd;
	}

	public static void setPwd(String pwd) {
		Connector.pwd = pwd;
	}
}
