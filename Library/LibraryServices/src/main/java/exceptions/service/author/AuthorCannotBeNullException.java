package exceptions.service.author;

import exceptions.ServiceException;

public class AuthorCannotBeNullException extends ServiceException {

	private static final long serialVersionUID = -7569345035659990660L;

	@Override
	public String getCode() {
		String code = "author_cannot_be_null";
		return code;
	}
	
	public AuthorCannotBeNullException() {
		super("Author cannot be null");
	}

	@Override
	public String classNameThrown() {
		String clazzName = "AuthorService";
		return clazzName;
	}

}
