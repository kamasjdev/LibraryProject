package interfaces;

import java.util.List;

import entities.BookAuthor;

public interface BookAuthorRepository extends Repository<BookAuthor> {

	List<BookAuthor> getByAuthorId(Integer authorId);

	List<BookAuthor> getByBookId(Integer bookId);
}
