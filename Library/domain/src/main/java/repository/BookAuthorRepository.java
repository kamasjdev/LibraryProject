package repository;

import java.util.List;

import entities.BookAuthor;

public interface BookAuthorRepository extends Repository<BookAuthor> {
	List<BookAuthor> getByAuthorId(int authorId);
	List<BookAuthor> getByBookId(int bookId);
}
