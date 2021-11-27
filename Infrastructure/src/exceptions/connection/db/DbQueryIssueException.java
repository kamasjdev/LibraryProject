package exceptions.connection.db;

import exceptions.InfrastructureException;

public class DbQueryIssueException extends InfrastructureException {

	private static final long serialVersionUID = 9194387804136336976L;

	public DbQueryIssueException(String query, Throwable throwable) {
		super(String.format("There was an issue with query %1$s", query), throwable);
	}
	
	@Override
	public String getCode() {
		String code = "db_query_issue";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "DbClient"; 
		return className;
	}
	
	
}
