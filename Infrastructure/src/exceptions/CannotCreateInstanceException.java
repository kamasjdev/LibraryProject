package exceptions;

public class CannotCreateInstanceException extends InfrastructureException {
	private static final long serialVersionUID = -655329367199038022L;

	public final Class<?> clazz;
	
	public CannotCreateInstanceException(Class<?> clazz, Throwable throwable) {
		super(String.format("Cannot create instance of object %1$s", clazz), throwable);
		this.clazz = clazz;
	}
	
	@Override
	public String getCode() {
		String code = "cannot_create_instance";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "DbConnection";
		return className;
	}
	
	
}
