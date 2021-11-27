package exceptions.connection.db;

import exceptions.InfrastructureException;

public class ConnectionIssueException extends InfrastructureException {
	private static final long serialVersionUID = -8055843058427554809L;

	public final String dbDriverPath;
	
	public ConnectionIssueException(String dbDriverPath, Throwable throwable) {
		super(String.format("Cannot connect with database with driver %1$s", dbDriverPath), throwable);
		this.dbDriverPath = dbDriverPath; 
	}
	
	@Override
	public String getCode() {
		String code = "connection_issue";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "DbConnection";
		return className;
	}

}
