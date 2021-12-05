package exceptions;

public abstract class InfrastructureException extends RuntimeException {
	private static final long serialVersionUID = -8582027289995416757L;
	
	public abstract String getCode();
	public abstract String classNameThrown();
	
	protected InfrastructureException(String message) {
        super(message);
    }
	
	protected InfrastructureException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
