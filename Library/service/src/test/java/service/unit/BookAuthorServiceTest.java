package service.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dto.BookAuthorDto;
import entities.BookAuthor;
import exceptions.service.bookauthor.BookAuthorNotFoundException;
import exceptions.service.bookauthor.InvalidBookAuthorAuthorIdException;
import exceptions.service.bookauthor.InvalidBookAuthorBookIdException;
import interfaces.BookAuthorService;
import repository.BookAuthorRepository;
import services.BookAuthorServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookAuthorServiceTest {

	private BookAuthorService bookAuthorService;
	private BookAuthorRepository bookAuthorRepository;
	
	@Before 
	public void init() {
		bookAuthorRepository = Mockito.mock(BookAuthorRepository.class);
		bookAuthorService = new BookAuthorServiceImpl(bookAuthorRepository);
    }
	 
	@Test
	public void given_valid_parameters_should_add_book_author() {
		BookAuthorDto bookAuthor = createBookAuthor();
		
		bookAuthorService.add(bookAuthor);
		
		verify(bookAuthorRepository).add(any(BookAuthor.class));		
	}
		
	@Test
	public void given_invalid_author_id_when_add_should_throw_an_exception() {
		Integer bookId = 1;
		Integer authorId = 0;
		BookAuthorDto bookAuthor = createBookAuthor(bookId, authorId);
		InvalidBookAuthorAuthorIdException expectedException = new InvalidBookAuthorAuthorIdException(bookAuthor.id, authorId);
		
		InvalidBookAuthorAuthorIdException thrown = (InvalidBookAuthorAuthorIdException) catchThrowable(() -> bookAuthorService.add(bookAuthor));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookAuthorId).isNull();
		assertThat(thrown.authorId).isEqualTo(authorId);	
	}
	
	@Test
	public void given_invalid_book_id_when_add_should_throw_an_exception() {
		Integer bookId = 0;
		Integer authorId = 1;
		BookAuthorDto bookAuthor = createBookAuthor(bookId, authorId);
		InvalidBookAuthorBookIdException expectedException = new InvalidBookAuthorBookIdException(bookAuthor.id, bookId);
		
		InvalidBookAuthorBookIdException thrown = (InvalidBookAuthorBookIdException) catchThrowable(() -> bookAuthorService.add(bookAuthor));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookAuthorId).isNull();
		assertThat(thrown.bookId).isEqualTo(bookId);	
	}
	
	@Test
	public void given_valid_parameters_should_update_book_author() {
		Integer bookId = 1;
		Integer authorId = 1;
		Integer bookIdAfterUpdate = 2;
		BookAuthor bookAuthor = BookAuthor.create(bookId, authorId);
		bookAuthor.id = 1;
		BookAuthorDto bookAuthorDto = createBookAuthor(bookIdAfterUpdate, authorId);
		bookAuthorDto.id = 1;
		doReturn(bookAuthor).when(bookAuthorRepository).get(any(Integer.class));
		
		bookAuthorService.update(bookAuthorDto);
		
		verify(bookAuthorRepository, times(1)).update(bookAuthor);
	}
	
	@Test
	public void given_invalid_book_id_when_update_should_throw_an_exception() {
		Integer bookId = 1;
		Integer invalidAuthorId = 0;
		Integer id = 1;
		BookAuthorDto bookAuthorDto = createBookAuthor(bookId, invalidAuthorId);
		bookAuthorDto.id = id;
		InvalidBookAuthorAuthorIdException expectedException = new InvalidBookAuthorAuthorIdException(id, invalidAuthorId);
		
		InvalidBookAuthorAuthorIdException thrown = (InvalidBookAuthorAuthorIdException) catchThrowable(() -> bookAuthorService.update(bookAuthorDto));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookAuthorId).isEqualTo(id);
		assertThat(thrown.authorId).isEqualTo(invalidAuthorId);		
	}
	
	@Test
	public void given_invalid_author_id_when_update_should_throw_an_exception() {
		Integer bookId = 1;
		Integer invalidBookId = 0;
		Integer id = 1;
		BookAuthorDto bookAuthorDto = createBookAuthor(invalidBookId, bookId);
		bookAuthorDto.id = id;
		InvalidBookAuthorBookIdException expectedException = new InvalidBookAuthorBookIdException(id, invalidBookId);

		InvalidBookAuthorBookIdException thrown = (InvalidBookAuthorBookIdException) catchThrowable(() -> bookAuthorService.update(bookAuthorDto));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookAuthorId).isEqualTo(id);
		assertThat(thrown.bookId).isEqualTo(invalidBookId);
	}
	
	@Test
	public void given_valid_parameters_should_delete_book_author() {
		Integer bookId = 1;
		Integer authorId = 1;
		Integer id = 1;
		BookAuthor bookAuthor = BookAuthor.create(bookId, authorId);
		bookAuthor.id = 1;
		doReturn(bookAuthor).when(bookAuthorRepository).get(any(Integer.class));

		bookAuthorService.delete(id);
		
		verify(bookAuthorRepository, times(1)).delete(any(Integer.class));
	}
	
	@Test
	public void given_invalid_book_author_id_when_delete_should_throw_an_exception() {
		Integer bookAuthorId = 1;
		BookAuthorNotFoundException expectedException = new BookAuthorNotFoundException(bookAuthorId);
			
		BookAuthorNotFoundException thrown = (BookAuthorNotFoundException) catchThrowable(() -> bookAuthorService.delete(bookAuthorId));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookAuthorId).isEqualTo(bookAuthorId);		
	}
	
	private BookAuthorDto createBookAuthor() {
		BookAuthorDto bookAuthorDto = new BookAuthorDto();
		bookAuthorDto.authorId = 1;
		bookAuthorDto.bookId = 1;
		return bookAuthorDto;
	}
	
	private BookAuthorDto createBookAuthor(Integer bookId, Integer authorId) {
		BookAuthorDto bookAuthorDto = new BookAuthorDto();
		bookAuthorDto.authorId = authorId;
		bookAuthorDto.bookId = bookId;
		return bookAuthorDto;
	}
}
