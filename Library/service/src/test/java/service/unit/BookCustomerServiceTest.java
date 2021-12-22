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

import dto.BookCustomerDto;
import entities.BookCustomer;
import exceptions.service.bookcustomer.BookCustomerNotFoundException;
import exceptions.service.bookcustomer.InvalidBookCustomerBookIdException;
import exceptions.service.bookcustomer.InvalidBookCustomerCustomerIdException;
import interfaces.BookCustomerService;
import repository.BookCustomerRepository;
import services.BookCustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookCustomerServiceTest {

	private BookCustomerService bookCustomerService;
	private BookCustomerRepository bookCustomerRepository;
	
	@Before 
	public void init() {
		bookCustomerRepository = Mockito.mock(BookCustomerRepository.class);
		bookCustomerService = new BookCustomerServiceImpl(bookCustomerRepository);
    }
	
	@Test
	public void given_valid_parameters_should_add_book_customer() {
		BookCustomerDto bookCustomer = createBookCustomer();
		
		bookCustomerService.add(bookCustomer);
		
		verify(bookCustomerRepository).add(any(BookCustomer.class));	
	}
	
	@Test
	public void given_invalid_customer_id_when_add_should_throw_an_exception() {
		Integer bookId = 1;
		Integer customerId = 0;
		BookCustomerDto bookCustomer = createBookCustomer(bookId, customerId);
		InvalidBookCustomerCustomerIdException expectedException = new InvalidBookCustomerCustomerIdException(bookCustomer.id, customerId);
		
		InvalidBookCustomerCustomerIdException thrown = (InvalidBookCustomerCustomerIdException) catchThrowable(() -> bookCustomerService.add(bookCustomer));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookCustomerId).isNull();
		assertThat(thrown.customerId).isEqualTo(customerId);	
	}
	
	@Test
	public void given_invalid_book_id_when_add_should_throw_an_exception() {
		Integer bookId = 0;
		Integer customerId = 1;
		BookCustomerDto bookCustomer = createBookCustomer(bookId, customerId);
		InvalidBookCustomerBookIdException expectedException = new InvalidBookCustomerBookIdException(bookCustomer.id, bookId);
		
		InvalidBookCustomerBookIdException thrown = (InvalidBookCustomerBookIdException) catchThrowable(() -> bookCustomerService.add(bookCustomer));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookCustomerId).isNull();
		assertThat(thrown.bookId).isEqualTo(bookId);	
	}
	
	@Test
	public void given_valid_parameters_should_update_book_customer() {
		Integer bookId = 1;
		Integer customerId = 1;
		Integer customerAfterUpdate = 2;
		Integer id = 1;
		BookCustomer bookCustomer = BookCustomer.create(bookId, customerId);
		bookCustomer.id = id;
		BookCustomerDto bookCustomerDto = createBookCustomer(bookId, customerAfterUpdate);
		bookCustomerDto.id = id;
		doReturn(bookCustomer).when(bookCustomerRepository).get(id);
		
		bookCustomerService.update(bookCustomerDto);
				
		verify(bookCustomerRepository, times(1)).update(bookCustomer);
	}
	
	@Test
	public void given_invalid_customer_id_when_update_should_throw_an_exception() {
		Integer bookId = 1;
		Integer customerIdAfterUpdate = 0;
		Integer id = 1;
		BookCustomerDto bookCustomerDto = createBookCustomer(bookId, customerIdAfterUpdate);
		bookCustomerDto.id = id;
		InvalidBookCustomerCustomerIdException expectedException = new InvalidBookCustomerCustomerIdException(id, customerIdAfterUpdate);

		InvalidBookCustomerCustomerIdException thrown = (InvalidBookCustomerCustomerIdException) catchThrowable(() -> bookCustomerService.update(bookCustomerDto));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookCustomerId).isEqualTo(id);
		assertThat(thrown.customerId).isEqualTo(customerIdAfterUpdate);	
	}
	
	@Test
	public void given_invalid_book_id_when_update_should_throw_an_exception() {
		Integer customerId = 1;
		Integer bookIdAfterUpdate = 0;
		Integer id = 1;
		BookCustomerDto bookCustomer = createBookCustomer(bookIdAfterUpdate, customerId);
		bookCustomer.id = id;
		InvalidBookCustomerBookIdException expectedException = new InvalidBookCustomerBookIdException(bookCustomer.id, bookIdAfterUpdate);
		
		InvalidBookCustomerBookIdException thrown = (InvalidBookCustomerBookIdException) catchThrowable(() -> bookCustomerService.add(bookCustomer));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookCustomerId).isEqualTo(id);
		assertThat(thrown.bookId).isEqualTo(bookIdAfterUpdate);	
	}
	
	@Test
	public void given_valid_parameters_should_delete_book_customer() {
		Integer bookId = 1;
		Integer customerId = 1;
		BookCustomer bookCustomer = BookCustomer.create(bookId, customerId);
		Integer id = 1;
		bookCustomer.id = id;
		doReturn(bookCustomer).when(bookCustomerRepository).get(id);
		
		bookCustomerService.delete(id);
		
		verify(bookCustomerRepository, times(1)).delete(any(Integer.class));
	}
	
	@Test
	public void given_invalid_id_when_delete_should_throw_an_exception() {
		Integer bookCustomerId = 1;
		BookCustomerNotFoundException expectedException = new BookCustomerNotFoundException(bookCustomerId);
		
		BookCustomerNotFoundException thrown = (BookCustomerNotFoundException) catchThrowable(() -> bookCustomerService.delete(bookCustomerId));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookCustomerId).isEqualTo(bookCustomerId);
	}
	
	private BookCustomerDto createBookCustomer() {
		BookCustomerDto bookCustomerDto = new BookCustomerDto();
		bookCustomerDto.bookId = 1;
		bookCustomerDto.customerId = 1;
		return bookCustomerDto;
	}
	
	private BookCustomerDto createBookCustomer(Integer bookId, Integer customerId) {
		BookCustomerDto bookCustomerDto = new BookCustomerDto();
		bookCustomerDto.bookId = bookId;
		bookCustomerDto.customerId = customerId;
		return bookCustomerDto;
	}
}
