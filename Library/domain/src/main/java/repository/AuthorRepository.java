package repository;

import entities.Author;

public interface AuthorRepository extends Repository<Author> {
	Author getAuthorDetails(int authorId);
}
