package exceptions.service.book;

import exceptions.ServiceException;

public class BookNameCannotBeEmptyException extends ServiceException  {
	private static final long serialVersionUID = -7291985588698717107L;

	public final Integer bookId;
	
	@Override
	public String getCode() {
		String code = "book_name_cannot_be_empty";
		return code;
	}
	
	public BookNameCannotBeEmptyException(Integer bookId) {
		super(String.format("Name cannot be empty for book with id: '%1$s' not found", bookId));
		this.bookId = bookId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookService";
		return clazzName;
	}
}
