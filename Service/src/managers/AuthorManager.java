package managers;

import java.util.List;
import java.util.stream.Collectors;

import entities.Author;
import entities.Book;
import services.AuthorService;
import services.BookService;

public class AuthorManager {
	private final AuthorService authorService;
	private final BookService bookService;
	
	public AuthorManager(AuthorService authorService, BookService bookService) {
		this.authorService = authorService;
		this.bookService = bookService;
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

		author.person.firstName = firstName;
		author.person.lastName = lastName;
		authorService.update(author);
	}
	
	public void getAuthorDetails(Integer id) {
		Author author = authorService.getById(id);
		
		if(author == null) {
			System.out.println("Author not found");
			return;
		}
		
		List<Integer> bookList = author.books.stream().map(b->b.bookId).collect(Collectors.toList());
		List<Book> books = bookService.getEntities().stream().filter(b->bookList.stream().anyMatch(bl->bl.equals(b.id))).collect(Collectors.toList());
		System.out.println("Author: ");
		System.out.println(author);
		System.out.println("Author's books: ");
		books.forEach(b->System.out.println(b));
	}
	
	public void deleteAuthor(Integer id) {
		Author author = authorService.getById(id);
		
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
}
