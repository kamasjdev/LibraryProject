package services;

import java.util.List;
import java.util.function.Predicate;

import abstracts.AbstractBaseService;
import entities.BookCustomer;
import exceptions.service.bookcustomer.BookCustomerNotFoundException;
import exceptions.service.bookcustomer.InvalidBookCustomerCustomerIdException;
import exceptions.service.bookcustomer.InvalidBookCustomerBookIdException;
import exceptions.service.bookcustomer.BookCustomerCannotBeNullException;

public class BookCustomerService extends AbstractBaseService<BookCustomer> {

	@Override
	public BookCustomer GetById(Integer id) {
		BookCustomer bookCustomer = objects.stream().filter(o->o.id.equals(id)).findFirst().orElse(null);
		return bookCustomer;
	}

	@Override
	public List<BookCustomer> GetEntities() {
		List<BookCustomer> bookCustomer = objects;
		return bookCustomer;
	}

	@Override
	public void Update(BookCustomer entity) {
		validateBookCustomer(entity);
		
		BookCustomer bookCustomer = GetById(entity.id);
		
		if(bookCustomer == null) {
			throw new BookCustomerNotFoundException(entity.id);
		}
		
		bookCustomer.customerId = entity.customerId;
		bookCustomer.bookId = entity.bookId;
	}

	@Override
	public Integer Add(BookCustomer entity) {
		validateBookCustomer(entity);
		
		Integer id = GetLastId();
		entity.id = id;
		objects.add(entity);
		
		return id;
	}

	@Override
	public void Delete(Integer id) {
		BookCustomer bookCustomer = GetById(id);
		
		if(bookCustomer == null) {
			throw new BookCustomerNotFoundException(id);
		}
		
		objects.remove(bookCustomer);	
	}
	
	public BookCustomer GetBookCustomer(Predicate<BookCustomer> predicate) {
		BookCustomer bookCustomer = objects.stream().filter(predicate).findFirst().orElse(null);
		
		if(bookCustomer == null) {
			throw new BookCustomerNotFoundException(null);
		}
		
		return bookCustomer;
	}
	
	private void validateBookCustomer(BookCustomer bookCustomer) {
		if(bookCustomer == null) {
			throw new BookCustomerCannotBeNullException();
		}
		
		if(bookCustomer.customerId == null){
			throw new InvalidBookCustomerCustomerIdException(bookCustomer.id, bookCustomer.customerId);
		}
		
		if(bookCustomer.customerId < 1) {
			throw new InvalidBookCustomerCustomerIdException(bookCustomer.id, bookCustomer.customerId);
		}
		
		if(bookCustomer.bookId == null){
			throw new InvalidBookCustomerBookIdException(bookCustomer.id, bookCustomer.bookId);
		}
		
		if(bookCustomer.bookId < 1) {
			throw new InvalidBookCustomerBookIdException(bookCustomer.id, bookCustomer.bookId);
		}
	}
	
}
