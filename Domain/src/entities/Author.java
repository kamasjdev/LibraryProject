package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashSet;

import common.BaseEntity;
import common.Person;

public class Author extends BaseEntity {
	public Author() {
		person = new Person();
		books = new HashSet<BookAuthor>();
	}
	
	@JsonProperty(value = "person")
	public Person person;
	
	@JsonDeserialize(as = HashSet.class)
	@JsonSerialize(as = HashSet.class)
	public HashSet<BookAuthor> books;
	
	public static Author create(String firstName, String lastName) {
		Author author = new Author();
		author.person.firstName = firstName;
		author.person.lastName = lastName;
		
		return author;
	}
	
	public static Author create(String firstName, String lastName, HashSet<BookAuthor> books) {
		Author author = new Author();
		author.person.firstName = firstName;
		author.person.lastName = lastName;
		
		if(!books.isEmpty()) {
			author.books = books;
		}
		
		return author;
	}
	
	@Override
	public String toString() {
		String description = String.format("%1$s. %2$s %3$s", id, person.firstName, person.lastName);
		return description;
	}
}
