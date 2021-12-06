package exceptions.service.bookauthor;

import exceptions.ServiceException;

public class InvalidBookAuthorBookIdException extends ServiceException {
	private static final long serialVersionUID = -3879827398653643509L;
	
	public final Integer bookAuthorId; 
	public final Integer bookId;
	
	@Override
	public String getCode() {
		String code = "invalid_book_author_author_id";
		return code;
	}
	
	public InvalidBookAuthorBookIdException(Integer bookAuthorId, Integer bookId) {
		super(String.format("Invalid book id '%1$s' for book author with id '%2$s'", bookId, bookAuthorId));
		this.bookAuthorId = bookAuthorId;
		this.bookId = bookId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookAuthorService";
		return clazzName;
	}
}
