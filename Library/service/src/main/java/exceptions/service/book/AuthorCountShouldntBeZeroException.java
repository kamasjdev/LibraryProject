package exceptions.service.book;

import exceptions.ServiceException;

public class AuthorCountShouldntBeZeroException extends ServiceException  {

	private static final long serialVersionUID = 359751819839862582L;

	@Override
	public String getCode() {
		String code = "author_count_shouldnt_be_zero";
		return code;
	}
	
	public AuthorCountShouldntBeZeroException() {
		super("Before add book please first add some authors");
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookService";
		return clazzName;
	}
}
