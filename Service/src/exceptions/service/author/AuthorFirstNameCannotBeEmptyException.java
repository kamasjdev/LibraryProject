package exceptions.service.author;

import exceptions.ServiceException;

public class AuthorFirstNameCannotBeEmptyException extends ServiceException {

	private static final long serialVersionUID = -979638893792737476L;
	public Integer authorId;
	
	@Override
	public String getCode() {
		String code = "author_first_name_cannot_be_empty";
		return code;
	}

	public AuthorFirstNameCannotBeEmptyException(Integer authorId) {
		super(String.format("Author first name with id: '%1$s' cannot be empty", authorId));
		this.authorId = authorId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "AuthorService";
		return clazzName;
	}
}
