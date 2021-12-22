package controllers.exceptions;

public class AuthorsCannotBeEmptyOrNullException extends ControllerException {
	private static final long serialVersionUID = 4298896118347418274L;

	public AuthorsCannotBeEmptyOrNullException() {
		super("Authors cannot be empty or null");
	}
	
	@Override
	public String getCode() {
		String code = "authors_cannot_be_empty_or_null";
		return code;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "AuthorContoller";
		return clazzName;
	}
}
