package exceptions;

public abstract class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 8192429850629906119L;
	
	public abstract String getCode();
	public abstract String classNameThrown();
	
	protected ServiceException(String message) {
        super(message);
    }
}
