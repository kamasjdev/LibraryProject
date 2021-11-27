package exceptions.service.bookauthor;

import exceptions.ServiceException;

public class BookAuthorNotFoundException extends ServiceException {
	private static final long serialVersionUID = 8101781989008455938L;
	
	public final Integer bookAuthorId;
	
	@Override
	public String getCode() {
		String code = "book_author_not_found";
		return code;
	}

	public BookAuthorNotFoundException(Integer bookAuthorId) {
		super(String.format("Book Author with id: '%1$s' not found", bookAuthorId));
		this.bookAuthorId = bookAuthorId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookAuthorService";
		return clazzName;
	}
}
