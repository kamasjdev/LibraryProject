package connection.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.CannotCreateInstanceException;
import exceptions.connection.db.CannotFindDriverException;
import exceptions.connection.db.ConnectionIssueException;
import interfaces.DbConnection;

public class DbConnectionImpl implements DbConnection {
    private Connection conn;
    private String url;
    private String dbDriverPath;
    
    public DbConnectionImpl(String url, String dbDriverPath) {
    	this.url = url;
    	this.dbDriverPath = dbDriverPath;
	}
    
	@SuppressWarnings("deprecation")
	public Connection getConnection() {
		try {
			  conn = DriverManager.getConnection(url);
			  Class.forName(dbDriverPath).newInstance();
		 } catch (SQLException e) {
			  throw new ConnectionIssueException(dbDriverPath, e);
		 } catch (InstantiationException e) {
			  throw new CannotCreateInstanceException(conn.getClass(), e);
		 } catch (IllegalAccessException e) {
			  throw new CannotCreateInstanceException(conn.getClass(), e);
		 } catch (ClassNotFoundException e) {
			  throw new CannotFindDriverException(dbDriverPath, e);
		 }
  	
		return conn;
    }
}
