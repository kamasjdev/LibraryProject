package exceptions.service.bookauthor;

import exceptions.ServiceException;

public class InvalidBookAuthorAuthorIdException extends ServiceException {
	private static final long serialVersionUID = -6120651497193673133L;
	
	public Integer bookAuthorId;
	public Integer authorId; 
		
	@Override
	public String getCode() {
		String code = "invalid_book_author_author_id";
		return code;
	}
	
	public InvalidBookAuthorAuthorIdException(Integer bookAuthorId, Integer authorId) {
		super(String.format("Invalid author id '%1$s' for book author with id '%$2s'", authorId, bookAuthorId));
		this.bookAuthorId = bookAuthorId;
		this.authorId = authorId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookAuthorService";
		return clazzName;
	}
}
