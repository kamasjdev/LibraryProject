package interfaces;

import dto.BookDto;

public interface BookService extends BaseService<BookDto> {
	BookDto getDetails(Integer bookId);
	void borrowBook(Integer id, Integer customerId);
	void returnBook(Integer id, Integer customerId);
	void deleteBookWithBookAuthors(int bookId);
}