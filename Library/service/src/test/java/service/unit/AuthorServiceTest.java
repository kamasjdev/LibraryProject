package service.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dto.AuthorDto;
import entities.Author;
import exceptions.service.author.AuthorCannotBeNullException;
import exceptions.service.author.AuthorFirstNameCannotBeEmptyException;
import exceptions.service.author.AuthorLastNameCannotBeEmptyException;
import exceptions.service.author.AuthorNotFoundException;
import helpers.services.mappings.Mapper;
import interfaces.AuthorService;
import repository.AuthorRepository;
import services.AuthorServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
	
	private AuthorService authorService;
	private AuthorRepository authorRepository;
	
	 @Before 
	 public void initMocks() {
		 authorRepository = Mockito.mock(AuthorRepository.class);
		 authorService = new AuthorServiceImpl(authorRepository);
     }
		
	@Test
	public void given_valid_author_should_add() {
		AuthorDto authorDto = createAuthor();
		Integer expectedId = 1;
		doReturn(expectedId).when(authorRepository).add(any(Author.class));
		
		Integer id = authorService.add(authorDto);
		
		assertThat(id).isGreaterThan(0);
	}
	
	@Test
	public void given_invalid_id_should_return_null() {
		Integer id = null;
		
		AuthorDto auth = authorService.getById(id);
		
		assertThat(auth).isEqualTo(null);
	}
	
	@Test
	public void given_invalid_author_when_add_should_throw_an_exception() {
		AuthorDto author = null;
		AuthorCannotBeNullException expectedException = new AuthorCannotBeNullException();
		
		Throwable thrown = catchThrowable(() -> authorService.add(author));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
					.hasMessage(expectedException.getMessage());
	}
	
	@Test
	public void given_invalid_author_first_name_when_add_should_throw_an_exception() {
		AuthorDto author = new AuthorDto();
		AuthorFirstNameCannotBeEmptyException expectedException = new AuthorFirstNameCannotBeEmptyException(author.id);
		
		Throwable thrown = catchThrowable(() -> authorService.add(author));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
					.hasMessage(expectedException.getMessage());
	}
	
	@Test
	public void given_invalid_author_last_name_when_add_should_throw_an_exception() {
		AuthorDto author = new AuthorDto();
		author.firstName = "Test";
		AuthorLastNameCannotBeEmptyException expectedException = new AuthorLastNameCannotBeEmptyException(author.id);
		
		Throwable thrown = catchThrowable(() -> authorService.add(author));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
					.hasMessage(expectedException.getMessage());
		assertThat(((AuthorLastNameCannotBeEmptyException) thrown).authorId).isEqualTo(author.id);
	}
	
	@Test
	public void given_valid_author_should_update_author() {
		AuthorDto author = createAuthor();
		author.id = 1;
		doReturn(Mapper.mapToAuthor(author)).when(authorRepository).get(author.id);
		
		authorService.update(author);
		
		verify(authorRepository, times(1)).update(any(Author.class));
	}
	
	@Test
	public void given_invalid_first_name_when_update_should_throw_an_exception() {
		AuthorDto author = createAuthor();
		String firstName = null;
		author.firstName = firstName;
		AuthorFirstNameCannotBeEmptyException expectedException = new AuthorFirstNameCannotBeEmptyException(author.id);

		Throwable thrown = catchThrowable(() -> authorService.update(author));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
			.hasMessage(expectedException.getMessage());
		assertThat(((AuthorFirstNameCannotBeEmptyException) thrown).authorId).isEqualTo(author.id);
	}

	@Test
	public void given_invalid_last_name_when_update_should_throw_an_exception() {
		AuthorDto author = createAuthor();
		String lastName = null;
		author.lastName = lastName;
		AuthorLastNameCannotBeEmptyException expectedException = new AuthorLastNameCannotBeEmptyException(author.id);
		
		Throwable thrown = catchThrowable(() -> authorService.update(author));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
			.hasMessage(expectedException.getMessage());
		assertThat(((AuthorLastNameCannotBeEmptyException) thrown).authorId).isEqualTo(author.id);
	}
	
	@Test
	public void given_invalid_id_when_delete_should_throw_an_exception() {
		Integer id = 1;
		AuthorNotFoundException expectedException = new AuthorNotFoundException(id);
		
		Throwable thrown = catchThrowable(() -> authorService.delete(id));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
			.hasMessage(expectedException.getMessage());
		assertThat(((AuthorNotFoundException) thrown).authorId).isEqualTo(id);
	}
	
	@Test
	public void given_valid_id_should_delete_author() {
		Integer id = 1;
		Author author = getAuthors().stream().filter(a -> a.id.equals(id)).findFirst().orElse(null);
		doReturn(author).when(authorRepository).get(id);
		
		authorService.delete(id);
		
		verify(authorRepository, times(1)).delete(id);
	}
	
	private AuthorDto createAuthor() {
		AuthorDto authorDto = new AuthorDto();
		authorDto.firstName = "Andrew";
		authorDto.lastName = "McAfrey";
		return authorDto;		
	}
	
	private List<Author> getAuthors() {
		List<Author> authors = new ArrayList<Author>();
		Author author1 = Author.create("A", "B");
		author1.id = 1;
		authors.add(author1);
		Author author2 = Author.create("C", "D");
		author2.id = 2;
		authors.add(author2);
		Author author3 = Author.create("E", "F");
		author3.id = 3;
		authors.add(author3);
		return authors;
	}
}
