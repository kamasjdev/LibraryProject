package tests.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.List;

import org.junit.Test;

import entities.BookCustomer;
import exceptions.service.bookcustomer.BookCustomerNotFoundException;
import exceptions.service.bookcustomer.InvalidBookCustomerBookIdException;
import exceptions.service.bookcustomer.InvalidBookCustomerCustomerIdException;
import services.BookCustomerService;

public class BookCustomerServiceTests {
	private BookCustomerService bookCustomerService;
	
	public BookCustomerServiceTests() {
		bookCustomerService = new BookCustomerService();
	}
	
	@Test
	public void given_valid_parameters_should_add_book_customer() {
		Integer bookId = 1;
		Integer customerId = 1;
		BookCustomer bookCustomer = BookCustomer.create(bookId, customerId);
		Integer expectedId = 1;
		
		Integer id = bookCustomerService.add(bookCustomer);
		
		assertThat(id).isEqualTo(expectedId);
	}
	
	@Test
	public void given_invalid_customer_id_when_add_should_throw_an_exception() {
		Integer bookId = 1;
		Integer customerId = 0;
		BookCustomer bookCustomer = BookCustomer.create(bookId, customerId);
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
		BookCustomer bookCustomer = BookCustomer.create(bookId, customerId);
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
		BookCustomer bookCustomer = BookCustomer.create(bookId, customerId);
		Integer id = bookCustomerService.add(bookCustomer);
		
		BookCustomer bookCustomerAdded = bookCustomerService.getById(id);
		bookCustomerAdded.customerId = customerAfterUpdate;
		bookCustomerService.update(bookCustomerAdded);
		BookCustomer bookCustomerUpdated = bookCustomerService.getById(id);
				
		assertThat(bookCustomerUpdated.customerId).isEqualTo(customerAfterUpdate);
	}
	
	@Test
	public void given_invalid_customer_id_when_update_should_throw_an_exception() {
		Integer bookId = 1;
		Integer customerId = 1;
		Integer customerIdAfterUpdate = 0;
		BookCustomer bookCustomer = BookCustomer.create(bookId, customerId);
		Integer id = bookCustomerService.add(bookCustomer);
		InvalidBookCustomerCustomerIdException expectedException = new InvalidBookCustomerCustomerIdException(id, customerIdAfterUpdate);

		BookCustomer bookCustomerAdded = bookCustomerService.getById(id);
		bookCustomerAdded.customerId = customerIdAfterUpdate;
		InvalidBookCustomerCustomerIdException thrown = (InvalidBookCustomerCustomerIdException) catchThrowable(() -> bookCustomerService.update(bookCustomerAdded));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookCustomerId).isEqualTo(id);
		assertThat(thrown.customerId).isEqualTo(customerIdAfterUpdate);	
	}
	
	@Test
	public void given_invalid_book_id_when_update_should_throw_an_exception() {
		Integer bookId = 1;
		Integer customerId = 1;
		Integer bookIdAfterUpdate = 0;
		BookCustomer bookCustomer = BookCustomer.create(bookId, customerId);
		Integer id = bookCustomerService.add(bookCustomer);
		InvalidBookCustomerBookIdException expectedException = new InvalidBookCustomerBookIdException(bookCustomer.id, bookIdAfterUpdate);
		
		BookCustomer bookCustomerAdded = bookCustomerService.getById(id);
		bookCustomerAdded.bookId = bookIdAfterUpdate;
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
		Integer id = bookCustomerService.add(bookCustomer);
		Integer expectedSize = 0;
		
		bookCustomerService.delete(id);
		List<BookCustomer> bookCustomers = bookCustomerService.getEntities();
		
		assertThat(bookCustomers.size()).isEqualTo(expectedSize);
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
	
	@Test
	public void given_valid_parameters_should_return_book_customer_by_predicate() {
		Integer bookId = 1;
		Integer bookId2 = 2;
		Integer customerId = 1;
		Integer customerId2 = 2;
		BookCustomer bookCustomer = BookCustomer.create(bookId, customerId);
		BookCustomer bookCustomer2 = BookCustomer.create(bookId, customerId2);
		BookCustomer bookCustomer3 = BookCustomer.create(bookId2, customerId);
		BookCustomer bookCustomer4 = BookCustomer.create(bookId2, customerId2);
		bookCustomerService.add(bookCustomer);
		bookCustomerService.add(bookCustomer2);
		bookCustomerService.add(bookCustomer3);
		bookCustomerService.add(bookCustomer4);
		
		BookCustomer bookCustomer5 = bookCustomerService.getBookCustomer(b->b.bookId.equals(bookId) && b.customerId.equals(customerId2));
		
		assertThat(bookCustomer5).isNotNull();
		assertThat(bookCustomer5).isEqualTo(bookCustomer2);
	}
	
	@Test
	public void given_invalid_book_id_and_customer_id_shouldnt_return_book_customer_by_predicate() {
		Integer bookId = 1;
		Integer customerId = 1;
		BookCustomerNotFoundException expectedException = new BookCustomerNotFoundException(null);
		
		BookCustomerNotFoundException thrown = (BookCustomerNotFoundException) catchThrowable(() -> bookCustomerService.getBookCustomer(b->b.bookId.equals(bookId) && b.customerId.equals(customerId)));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookCustomerId).isNull();
	}
}
