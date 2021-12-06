package managers;

import java.util.List;

import entities.Author;
import services.AuthorService;

public class AuthorManager {
	private final AuthorService authorService;
	
	public AuthorManager(AuthorService authorService) {
		this.authorService = authorService;
	}
	
	public Integer addAuthor(String firstName, String lastName) {
		Author author = Author.create(firstName, lastName);
		Integer id = authorService.add(author);
		return id;
	}
	
	public void editAuthor(Integer authorId, String firstName, String lastName) {
		Author author = authorService.getById(authorId);
		if(author == null) {
			System.out.println(String.format("Not found any author with id: %$1s", authorId));
			return;
		}

		if(!firstName.isBlank()) {
			author.person.firstName = firstName;
		}
		
		if(!lastName.isBlank()) {
			author.person.lastName = lastName;
		}
		authorService.update(author);
	}
	
	public void getAuthorDetails(Integer id) {
		Author author = authorService.getDetails(id);
		
		if(author == null) {
			System.out.println("Author not found");
			return;
		}
		
		System.out.println("Author: ");
		System.out.println(author);
	}
	
	public void deleteAuthor(Integer id) {
		Author author = authorService.getDetails(id);
		
		if(author == null) {
			System.out.println("Author not found");
			return;
		}
		
		if(!author.books.isEmpty()) {
			System.out.println("Cannot delete author with books, first delete books then delete author. See author details");
			return;
		}
		
		authorService.delete(id);
	}
	
	public List<Author> getAll() {
		List<Author> authors = authorService.getEntities();
		return authors;
	}

	public int getCount() {
		int count = authorService.getCount();
		return count;
	}
}
