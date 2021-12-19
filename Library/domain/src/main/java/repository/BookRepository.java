package repository;

import entities.Book;

public interface BookRepository extends Repository<Book> {
	Book getBookDetails(int bookId);
	Book getBookWithoutAuthors(int bookId);
	public void deleteBookWithBookAuthors(int bookId);
}
