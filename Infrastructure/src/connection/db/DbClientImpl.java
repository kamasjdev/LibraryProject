package connection.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.connection.db.DbQueryIssueException;
import interfaces.DbClient;
import interfaces.DbConnection;

public class DbClientImpl implements DbClient {
	private DbConnection dbConnection;
	
	public DbClientImpl(DbConnection dbConnection) {
		this.dbConnection = dbConnection;
	}
	
	@Override
	public List<List<Map<String, Object>>> executeQuery(String query) {
		Connection connection = dbConnection.getConnection();
		Statement statement = null;
		ResultSet result = null;
		List<List<Map<String, Object>>> records = new ArrayList<List<Map<String,Object>>>();
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			addDataToList(records, result);
		} catch (SQLException e) {
			throw new DbQueryIssueException(query, e);
		} finally {
		    try { if (result != null) result.close(); } catch (Exception e) {};
		    try { if (statement != null) statement.close(); } catch (Exception e) {};
		    try { if (connection != null) connection.close(); } catch (Exception e) {};
		}
		
		return records;
	}
	
	@Override
	public List<List<Map<String, Object>>> executeQuery(String query, Object... params) {
		Connection connection = dbConnection.getConnection();
		ResultSet result = null;
		PreparedStatement prepareStatement = null;
		List<List<Map<String, Object>>> records = new ArrayList<List<Map<String,Object>>>();
		
		try {
			prepareStatement = connection.prepareStatement(query);
			executeQuery(prepareStatement, params);
			result = prepareStatement.executeQuery();
			addDataToList(records, result);
		} catch (SQLException e) {
			throw new DbQueryIssueException(query, e);
		} finally {
		    try { if (result != null) result.close(); } catch (Exception e) {};
		    try { if (prepareStatement != null) prepareStatement.close(); } catch (Exception e) {};
		    try { if (connection != null) connection.close(); } catch (Exception e) {};
		}
		
		return records;
	}
	
	@Override
	public List<List<Map<String, Object>>> executeQuery(String query, Map<String, Object> params) {
		Connection connection = dbConnection.getConnection();
		ResultSet result = null;
		boolean first = true;
		PreparedStatement prepareStatement = null;
		
		for (String paramName : params.keySet()) {
		    Object paramValue = params.get(paramName);
		    if (paramValue != null) {
		        if (first){
		        	query += " where " + paramName + "=?";
		            first = false;
		        } else {
		        	query += " and " + paramName + "=?";
		        }
		    }
		}
		
		List<List<Map<String, Object>>> records = new ArrayList<List<Map<String,Object>>>();
		
		try {
			prepareStatement = connection.prepareStatement(query);
			executeQuery(prepareStatement, params);
			result = prepareStatement.executeQuery();
			addDataToList(records, result);
		} catch (SQLException e) {
			throw new DbQueryIssueException(query, e);
		} finally {
		    try { if (result != null) result.close(); } catch (Exception e) {};
		    try { if (prepareStatement != null) prepareStatement.close(); } catch (Exception e) {};
		    try { if (connection != null) connection.close(); } catch (Exception e) {};
		}
		
		return records;
	}

	@Override
	public Integer insert(String query, Object... params) {
		Connection connection = dbConnection.getConnection();
		Integer id = null;
		ResultSet rs = null;
		PreparedStatement prepareStatement = null;
		
		try {
			prepareStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			executeUpdate(prepareStatement, params);
			rs = prepareStatement.getGeneratedKeys();
			rs.beforeFirst();
			
			while( rs.next() ) {  
				id = rs.getInt(1);  
			}
		} catch (SQLException e) {
			throw new DbQueryIssueException(query, e);
		} finally {
		    try { if (rs != null) rs.close(); } catch (Exception e) {};
		    try { if (prepareStatement != null) prepareStatement.close(); } catch (Exception e) {};
		    try { if (connection != null) connection.close(); } catch (Exception e) {};
		}
		
		return id;
	}
	
	private Date utilDateToSQL(java.util.Date date) {
		Date sqlDate = new Date(date.getTime());
		return sqlDate;
	}

	@Override
	public void update(String query, Object... params) {
		Connection connection = dbConnection.getConnection();
		ResultSet rs = null;
		PreparedStatement prepareStatement = null;
		
		try {
			prepareStatement = connection.prepareStatement(query);
			executeUpdate(prepareStatement, params);
		} catch (SQLException e) {
			throw new DbQueryIssueException(query, e);
		} finally {
		    try { if (rs != null) rs.close(); } catch (Exception e) {};
		    try { if (prepareStatement != null) prepareStatement.close(); } catch (Exception e) {};
		    try { if (connection != null) connection.close(); } catch (Exception e) {};
		}
	}

	@Override
	public void delete(String query, Object... params) {
		Connection connection = dbConnection.getConnection();
		ResultSet rs = null;
		PreparedStatement prepareStatement = null;
		
		try {
			prepareStatement = connection.prepareStatement(query);
			executeUpdate(prepareStatement, params);
		} catch (SQLException e) {
			throw new DbQueryIssueException(query, e);
		} finally {
		    try { if (rs != null) rs.close(); } catch (Exception e) {};
		    try { if (prepareStatement != null) prepareStatement.close(); } catch (Exception e) {};
		    try { if (connection != null) connection.close(); } catch (Exception e) {};
		}
	}
	
	 /**
     * Executes the SQL statement in this {@code PreparedStatement} object,
     * which must be an SQL Data Manipulation Language (DML) statement, such as {@code INSERT}, {@code UPDATE} or
     * {@code DELETE}; or an SQL statement that returns nothing,
     * such as a DDL statement.
     */
	private void executeUpdate(PreparedStatement prepareStatement, Object... params) throws SQLException {
		setParams(prepareStatement, params);
		prepareStatement.executeUpdate();
	}
	
	/**
     * Executes the SQL query in this {@code PreparedStatement} object
     * used for {@code SELECT}
     */
	private void executeQuery(PreparedStatement prepareStatement, Object... params) throws SQLException {
		setParams(prepareStatement, params);
		prepareStatement.executeQuery();
	}
	
	private void setParams(PreparedStatement prepareStatement, Object... params) throws SQLException {
		int paramNumber = 1;
		
		for (Object paramValue : params) {
			if (paramValue != null) {
		        if (paramValue instanceof java.util.Date) {
		        	prepareStatement.setDate(paramNumber, utilDateToSQL((java.util.Date)paramValue));
		        } else if (paramValue instanceof Integer) {
		        	prepareStatement.setInt(paramNumber, (Integer) paramValue);
		        } else {
		        	prepareStatement.setString(paramNumber, paramValue.toString());
		        }
		    }
	        paramNumber ++;
		}
	}
	
	private void addDataToList(List<List<Map<String, Object>>> records, ResultSet result) throws SQLException {
		while(result.next()){
			ResultSetMetaData metaData = result.getMetaData();
		    int cols = metaData.getColumnCount();
		    List<Map<String, Object>> objectFields = new ArrayList<Map<String, Object>>();
		    
		    for(int i=0; i<cols; i++){
		    	Map<String, Object> field = new HashMap<String, Object>();
		    	String tableName = metaData.getTableName(i+1);
		    	String columnName = metaData.getColumnName(i+1);
		    	field.put(tableName + "." + columnName, result.getObject(i+1));
		    	objectFields.add(field);
		    }
		    
		    records.add(objectFields);
		}
	}
}
