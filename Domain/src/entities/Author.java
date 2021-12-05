package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashSet;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.Table;

import common.BaseEntity;
import common.Person;

@Entity
@Table(name = "authors")
public class Author extends BaseEntity {
	public Author() {
		person = new Person();
		books = new HashSet<BookAuthor>();
	}
	
	@JsonProperty(value = "person")
	@Embedded
	@AttributeOverrides({
	  @AttributeOverride( name = "firstName", column = @Column(name = "first_name")),
	  @AttributeOverride( name = "lastName", column = @Column(name = "last_name"))
	})
	public Person person;
	
	@JsonDeserialize(as = HashSet.class)
	@JsonSerialize(as = HashSet.class)
	@OneToMany(mappedBy = "authors")
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
		StringBuilder description = new StringBuilder().append(id).append(". ").append(person.firstName)
				.append(" ").append(person.lastName);
		
		if(!books.isEmpty()) {
			description.append("\nAuthor of books:\n");
			
			for(BookAuthor bookAuthor : books) {
				description.append(bookAuthor);
				description.append("\n");
			}
		}
		
		return description.toString();
	}
}
