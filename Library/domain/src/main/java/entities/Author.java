package entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.Table;

import common.BaseEntity;
import common.Person;

@Entity
@Table(name = "authors")
public class Author extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -1970173707060981571L;

	public Author() {
		person = new Person();
		books = new HashSet<BookAuthor>();
	}
	
	@Embedded
	@AttributeOverrides({
	  @AttributeOverride( name = "firstName", column = @Column(name = "first_name")),
	  @AttributeOverride( name = "lastName", column = @Column(name = "last_name"))
	})
	public Person person;
	
	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
	public Set<BookAuthor> books;
	
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
}
