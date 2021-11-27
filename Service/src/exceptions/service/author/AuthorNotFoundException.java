package exceptions.service.author;

import exceptions.ServiceException;

public class AuthorNotFoundException extends ServiceException {
	private static final long serialVersionUID = -2134698316771795855L;
	public final Integer authorId;
	
	@Override
	public String getCode() {
		String code = "author_not_found";
		return code;
	}
	
	public AuthorNotFoundException(Integer authorId) {
		super(String.format("Author with id: '%1$s' not found", authorId));
		this.authorId = authorId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "AuthorService";
		return clazzName;
	}
}
