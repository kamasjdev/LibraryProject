package exceptions.service.author;

import exceptions.ServiceException;

public class AuthorLastNameCannotBeEmptyException extends ServiceException {
	private static final long serialVersionUID = -668575294711376912L;
	public final Integer authorId;

	@Override
	public String getCode() {
		String code = "author_last_name_cannot_be_empty";
		return code;
	}

	public AuthorLastNameCannotBeEmptyException(Integer authorId) {
		super(String.format("Author last name with id: '%1$s' cannot be empty", authorId));
		this.authorId = authorId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "AuthorService";
		return clazzName;
	}
}
