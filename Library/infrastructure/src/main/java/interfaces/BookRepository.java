package interfaces;

import entities.Book;

public interface BookRepository extends Repository<Book> {
	Book getBookDetails(Integer id);
	int getCount();
	Book getBookWithoutAuthors(Integer id);
}
