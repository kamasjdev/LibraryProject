package interfaces;

import entities.Author;

public interface AuthorRepository extends Repository<Author> {
	Author getAuthorDetails(Integer id);
}
