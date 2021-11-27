package exceptions.connection.db;

import exceptions.InfrastructureException;

public class CannotFindDriverException extends InfrastructureException {
	private static final long serialVersionUID = -8824953291343255561L;

	public final String driverClass;
	
	public CannotFindDriverException(String driverClass, Throwable throwable) {
		super(String.format("Cannot find driver %1$s", driverClass), throwable);
		this.driverClass = driverClass;
	}
	
	@Override
	public String getCode() {
		String code = "cannot_find_driver"; 
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "DbConnection";
		return className;
	}

}
