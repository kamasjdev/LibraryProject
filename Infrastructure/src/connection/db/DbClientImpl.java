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
			
			while(result.next()){
			    ResultSetMetaData metaData = result.getMetaData();
			    int cols = metaData.getColumnCount();
			    List<Map<String, Object>> objectFields = new ArrayList<Map<String, Object>>();
			    
			    for(int i=0; i<cols; i++){
			    	Map<String, Object> field = new HashMap<String, Object>();
			    	field.put(metaData.getColumnName(i+1), result.getObject(i+1));
			    	objectFields.add(field);
			    }
			    
			    records.add(objectFields);
			}
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
			
			result = prepareStatement.executeQuery();
			
			while(result.next()){
				ResultSetMetaData metaData = result.getMetaData();
			    int cols = metaData.getColumnCount();
			    List<Map<String, Object>> objectFields = new ArrayList<Map<String, Object>>();
			    
			    for(int i=0; i<cols; i++){
			    	Map<String, Object> field = new HashMap<String, Object>();
			    	field.put(metaData.getColumnName(i+1), result.getObject(i+1));
			    	objectFields.add(field);
			    }
			    
			    records.add(objectFields);
			}
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
			int paramNumber = 1;
			
			for (String paramName : params.keySet()) {
				Object paramValue = params.get(paramName);
				if (paramValue != null) {
			        if (paramValue instanceof Date) {
			        	prepareStatement.setDate(paramNumber, (Date) paramValue);
			        } else if (paramValue instanceof Integer) {
			        	prepareStatement.setInt(paramNumber, (Integer) paramValue);
			        } else {
			        	prepareStatement.setString(paramNumber, paramValue.toString());
			        }
			    }
		        paramNumber ++;
			}
			
			result = prepareStatement.executeQuery();
			
			while(result.next()){
				ResultSetMetaData metaData = result.getMetaData();
			    int cols = metaData.getColumnCount();
			    List<Map<String, Object>> objectFields = new ArrayList<Map<String, Object>>();
			    
			    for(int i=0; i<cols; i++){
			    	Map<String, Object> field = new HashMap<String, Object>();
			    	field.put(metaData.getColumnName(i+1), result.getObject(i+1));
			    	objectFields.add(field);
			    }
			    
			    records.add(objectFields);
			}
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
			
			prepareStatement.executeUpdate();
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
			
			prepareStatement.executeUpdate();
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
			
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DbQueryIssueException(query, e);
		} finally {
		    try { if (rs != null) rs.close(); } catch (Exception e) {};
		    try { if (prepareStatement != null) prepareStatement.close(); } catch (Exception e) {};
		    try { if (connection != null) connection.close(); } catch (Exception e) {};
		}
	}
}
