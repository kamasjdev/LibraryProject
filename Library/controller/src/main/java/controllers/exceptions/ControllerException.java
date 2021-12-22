package controllers.exceptions;

public abstract class ControllerException extends RuntimeException {
	private static final long serialVersionUID = 3540236613592156510L;

	public abstract String getCode();
	public abstract String classNameThrown();
	
	protected ControllerException(String message) {
        super(message);
    }
	
	protected ControllerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
