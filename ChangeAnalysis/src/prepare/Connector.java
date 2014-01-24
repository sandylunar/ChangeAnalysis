package prepare;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * 
 */
public class Connector {

    private String driverClass;
    private String url;
    private String user;
    private String pwd;

    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt;

    /** Creates a new instance of Connector */
    public Connector() {
	driverClass = Property.driverClass;
	url = "jdbc:mysql://localhost:3306/api_activemq";
	user = Property.user;
	pwd = Property.pwd;
	conn = getNewConnection();
    }

    public Connector(String url, String user, String pwd) {
	this.url = url;
	this.user = user;
	this.pwd = pwd;
    }

    public Connection getNewConnection() {
	try {
	    Class.forName(driverClass);
	    conn = DriverManager.getConnection(url, user, pwd);
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
	    stmt = conn.createStatement();
	} catch (SQLException e) {
	    e.printStackTrace();
	    System.exit(1);
	}

	return stmt;
    }

    public PreparedStatement getNewPreparedStatement(String str) {
	try {
	    pstmt = conn.prepareStatement(str);
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
    }
}
