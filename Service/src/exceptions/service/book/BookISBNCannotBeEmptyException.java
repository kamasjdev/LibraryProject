package exceptions.service.book;

import exceptions.ServiceException;

public class BookISBNCannotBeEmptyException extends ServiceException  {
	private static final long serialVersionUID = 5968148655166420370L;
	
	public Integer bookId;
	
	@Override
	public String getCode() {
		String code = "book_isbn_cannot_be_empty";
		return code;
	}
	
	public BookISBNCannotBeEmptyException(Integer bookId) {
		super(String.format("ISBN cannot be empty for book with id: '%1$s'", bookId));
		this.bookId = bookId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookService";
		return clazzName;
	}
}
