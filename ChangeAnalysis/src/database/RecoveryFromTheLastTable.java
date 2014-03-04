package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RecoveryFromTheLastTable {
	
	

	public static void recovery(String targetTableName, String outputTableName) throws SQLException {

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

}
