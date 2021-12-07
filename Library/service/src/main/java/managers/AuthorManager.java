package managers;

import java.util.List;

import dto.AuthorDto;
import services.AuthorService;

public class AuthorManager {
	private final AuthorService authorService;
	
	public AuthorManager(AuthorService authorService) {
		this.authorService = authorService;
	}
	
	public Integer addAuthor(String firstName, String lastName) {
		AuthorDto author = new AuthorDto();
		author.firstName = firstName;
		author.lastName = lastName;
		Integer id = authorService.add(author);
		return id;
	}
	
	public void editAuthor(Integer authorId, String firstName, String lastName) {
		AuthorDto author = authorService.getById(authorId);
		if(author == null) {
			System.out.println(String.format("Not found any author with id: %$1s", authorId));
			return;
		}

		if(!firstName.isBlank()) {
			author.firstName = firstName;
		}
		
		if(!lastName.isBlank()) {
			author.lastName = lastName;
		}
		authorService.update(author);
	}
	
	public void getAuthorDetails(Integer id) {
		AuthorDto author = authorService.getDetails(id);
		
		if(author == null) {
			System.out.println("Author not found");
			return;
		}
		
		System.out.println("Author: ");
		System.out.println(author);
	}
	
	public void deleteAuthor(Integer id) {
		AuthorDto author = authorService.getDetails(id);
		
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
	
	public List<AuthorDto> getAll() {
		List<AuthorDto> authors = authorService.getEntities();
		return authors;
	}

	public int getCount() {
		int count = authorService.getCount();
		return count;
	}
}
