package tests.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;

import org.junit.Test;

import entities.Author;

public class AuthorTests {
	
	@Test
	public void given_different_objects_with_same_id_should_return_equals() {
		Author lem = Author.create("Stanis³aw", "Lem");
		lem.id = 1;
		Author tolkien = Author.create("John Ronald Reuel", "Tolkien");
		tolkien.id = 1;
		
		boolean equals = lem.equals(tolkien);
		
		assertThat(equals).isEqualTo(true);
	}
	
	@Test
	public void given_different_objects_with_same_id_shouldnt_add_two_objects_to_hash_set() {
		Author lem = Author.create("Stanis³aw", "Lem");
		lem.id = 1;
		Author tolkien = Author.create("John Ronald Reuel", "Tolkien");
		tolkien.id = 1;
		Author lewis = Author.create("Carroll", "Lewis");
		lewis.id = 2;
		int expectedSize = 2;
		
		HashSet<Author> authors = new HashSet<Author>();
		authors.add(tolkien);
		authors.add(lem);
		authors.add(lewis);
		
		assertThat(authors).isNotNull();
		assertThat(authors.size()).isEqualTo(expectedSize);
		Author author = authors.stream().findFirst().get();
		assertThat(author).isEqualTo(tolkien);
	}
}
