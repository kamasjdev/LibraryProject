package tests.integration;

import connection.db.DbClientImpl;
import connection.db.DbConnectionImpl;
import interfaces.DbClient;
import interfaces.DbConnection;

public abstract class BaseTest {
	protected DbClient dbClient;
	protected DbConnection dbConnection;
	private String connectionString = "jdbc:mysql://localhost:3306/library?user=root&password=";
	private String dbPackage = "com.mysql.cj.jdbc.Driver";	
	
	public BaseTest() {
		dbConnection = new DbConnectionImpl(connectionString, dbPackage);
		dbClient = new DbClientImpl(dbConnection);
	}
}
