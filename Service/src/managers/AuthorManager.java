package managers;

import java.util.List;
import java.util.stream.Collectors;

import entities.Author;
import entities.Book;
import interfaces.ActionService;
import services.AuthorService;
import services.BookService;

public class AuthorManager {
	private AuthorService authorService;
	private ActionService actionService;
	private BookService bookService;
	
	public AuthorManager(AuthorService authorService, BookService bookService, ActionService actionService) {
		this.authorService = authorService;
		this.actionService = actionService;
		this.bookService = bookService;
	}
	
	public Integer AddAuthor() {
		System.out.println("Please enter first name for author");
		String firstName = actionService.inputLine(String.class);	
		System.out.println("Please enter last name for author");
		String lastName = actionService.inputLine(String.class);
		Author author = Author.Create(firstName, lastName);
		Integer id = authorService.Add(author);
		return id;
	}
	
	public void EditAuthor(Integer authorId) {
		Author author = authorService.GetById(authorId);
		if(author == null) {
			System.out.println(String.format("Not found any author with id: %$1s", authorId));
			return;
		}
		
		System.out.println("Please enter first name for author");
		String firstName = actionService.inputLine(String.class);
		author.person.firstName = firstName;
		System.out.println("Please enter last name for author");
		String lastName = actionService.inputLine(String.class);
		author.person.lastName = lastName;
		authorService.Update(author);
	}
	
	public void GetAutorDetails(Integer id) {
		Author author = authorService.GetById(id);
		
		if(author == null) {
			System.out.println("Author not found");
			return;
		}
		
		List<Integer> bookList = author.books.stream().map(b->b.bookId).collect(Collectors.toList());
		List<Book> books = bookService.GetEntities().stream().filter(b->bookList.stream().anyMatch(bl->bl.equals(b.id))).collect(Collectors.toList());
		System.out.println("Author: ");
		System.out.println(author);
		System.out.println("Author's books: ");
		books.forEach(b->System.out.println(b));
	}
	
	public void DeleteAuthor(Integer id) {
		Author author = authorService.GetById(id);
		
		if(author == null) {
			System.out.println("Author not found");
			return;
		}
		
		if(!author.books.isEmpty()) {
			System.out.println("Cannot delete author with books, first delete books then delete author. See author details");
			return;
		}
		
		authorService.Delete(id);
	}
	
	public List<Author> GetAll() {
		List<Author> authors = authorService.GetEntities();
		return authors;
	}
}
