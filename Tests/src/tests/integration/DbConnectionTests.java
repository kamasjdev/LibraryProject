package tests.integration;

import java.sql.Connection;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import connection.db.DbConnectionImpl;

public class DbConnectionTests {
	private String connectionString = "jdbc:mysql://localhost:3306/library?user=root&password=";
	private String dbPackage = "com.mysql.cj.jdbc.Driver";	
	
	@Test
	public void given_valid_parameters_should_connect_with_db() {
		DbConnectionImpl dbConnection = new DbConnectionImpl(connectionString, dbPackage);
				
		Connection connection = dbConnection.getConnection();
				
		assertThat(connection).isNotNull();
	}
}
