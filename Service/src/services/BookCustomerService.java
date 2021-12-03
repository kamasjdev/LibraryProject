package services;

import java.util.List;

import entities.BookCustomer;
import exceptions.service.bookcustomer.BookCustomerNotFoundException;
import exceptions.service.bookcustomer.InvalidBookCustomerCustomerIdException;
import interfaces.BaseService;
import interfaces.BookCustomerRepository;
import exceptions.service.bookcustomer.InvalidBookCustomerBookIdException;
import exceptions.service.bookcustomer.BookCustomerCannotBeNullException;

public class BookCustomerService implements BaseService<BookCustomer> {
	private final BookCustomerRepository bookCustomerRepository;
	
	public BookCustomerService(BookCustomerRepository bookCustomerRepository) {
		this.bookCustomerRepository = bookCustomerRepository;
	}
	
	@Override
	public BookCustomer getById(Integer id) {
		BookCustomer bookCustomer = bookCustomerRepository.get(id); 
		return bookCustomer;
	}

	@Override
	public List<BookCustomer> getEntities() {
		List<BookCustomer> bookCustomer = bookCustomerRepository.getAll();
		return bookCustomer;
	}

	@Override
	public void update(BookCustomer entity) {
		validateBookCustomer(entity);
		
		BookCustomer bookCustomer = getById(entity.id);
		
		if(bookCustomer == null) {
			throw new BookCustomerNotFoundException(entity.id);
		}
		
		bookCustomer.customerId = entity.customerId;
		bookCustomer.bookId = entity.bookId;
		bookCustomerRepository.update(bookCustomer);
	}

	@Override
	public Integer add(BookCustomer entity) {
		validateBookCustomer(entity);
		
		Integer id = bookCustomerRepository.add(entity);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		BookCustomer bookCustomer = getById(id);
		
		if(bookCustomer == null) {
			throw new BookCustomerNotFoundException(id);
		}
		
		bookCustomerRepository.delete(bookCustomer);	
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

	public BookCustomer getBookCustomerByBookIdAndCustomerId(Integer bookId, Integer customerId) {
		BookCustomer bookCustomer = bookCustomerRepository.getBookCustomerByBookIdAndCustomerId(bookId, customerId);
		return bookCustomer;
	}
}
