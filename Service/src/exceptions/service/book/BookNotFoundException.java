package exceptions.service.book;

import exceptions.ServiceException;

public class BookNotFoundException extends ServiceException {
	private static final long serialVersionUID = 715328790763359401L;
	
	public Integer bookId;
	
	@Override
	public String getCode() {
		String code = "book_not_found";
		return code;
	}

	public BookNotFoundException(Integer bookId) {
		super(String.format("Book with id: '%1$s' not found", bookId));
		this.bookId = bookId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookService";
		return clazzName;
	}
}
