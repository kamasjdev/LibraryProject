package services;

import java.util.ArrayList;
import java.util.List;

import dto.BookCustomerDto;
import entities.BookCustomer;
import exceptions.service.bookcustomer.BookCustomerNotFoundException;
import exceptions.service.bookcustomer.InvalidBookCustomerCustomerIdException;
import helpers.services.mappings.Mapper;
import repository.BookCustomerRepository;
import interfaces.BookCustomerService;
import exceptions.service.bookcustomer.InvalidBookCustomerBookIdException;
import exceptions.service.bookcustomer.BookCustomerCannotBeNullException;

public class BookCustomerServiceImpl implements BookCustomerService {
	private final BookCustomerRepository bookCustomerRepository;
	
	public BookCustomerServiceImpl(BookCustomerRepository bookCustomerRepository) {
		this.bookCustomerRepository = bookCustomerRepository;
	}
	
	@Override
	public BookCustomerDto getById(Integer id) {
		BookCustomer bookCustomer = bookCustomerRepository.get(id); 
		
		if(bookCustomer == null) {
			return null;
		}
		
		BookCustomerDto bookCustomerDto = Mapper.mapToBookCustomerDto(bookCustomer);
		return bookCustomerDto;
	}

	@Override
	public List<BookCustomerDto> getAll() {
		List<BookCustomer> bookCustomers = bookCustomerRepository.getAll();
		List<BookCustomerDto> bookCustomersDto = new ArrayList<BookCustomerDto>();
		
		for(BookCustomer bookCustomer : bookCustomers) {
			BookCustomerDto bookCustomerDto = Mapper.mapToBookCustomerDto(bookCustomer);
			bookCustomersDto.add(bookCustomerDto);
		}
		
		return bookCustomersDto;
	}

	@Override
	public void update(BookCustomerDto dto) {
		validateBookCustomer(dto);
		
		BookCustomerDto bookCustomerDto = getById(dto.id);
		
		if(bookCustomerDto == null) {
			throw new BookCustomerNotFoundException(dto.id);
		}
		
		bookCustomerDto.customerId = dto.customerId;
		bookCustomerDto.bookId = dto.bookId;
		BookCustomer bookCustomer = Mapper.mapToBookCustomer(bookCustomerDto);
		bookCustomerRepository.update(bookCustomer);
	}

	@Override
	public Integer add(BookCustomerDto dto) {
		validateBookCustomer(dto);
		BookCustomer bookCustomer = BookCustomer.create(dto.bookId, dto.customerId);
		
		Integer id = bookCustomerRepository.add(bookCustomer);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		BookCustomerDto bookCustomerDto = getById(id);
		
		if(bookCustomerDto == null) {
			throw new BookCustomerNotFoundException(id);
		}
		
		bookCustomerRepository.delete(id);	
	}
	
	private void validateBookCustomer(BookCustomerDto bookCustomerDto) {
		if(bookCustomerDto == null) {
			throw new BookCustomerCannotBeNullException();
		}
		
		if(bookCustomerDto.customerId == null){
			throw new InvalidBookCustomerCustomerIdException(bookCustomerDto.id, bookCustomerDto.customerId);
		}
		
		if(bookCustomerDto.customerId < 1) {
			throw new InvalidBookCustomerCustomerIdException(bookCustomerDto.id, bookCustomerDto.customerId);
		}
		
		if(bookCustomerDto.bookId == null){
			throw new InvalidBookCustomerBookIdException(bookCustomerDto.id, bookCustomerDto.bookId);
		}
		
		if(bookCustomerDto.bookId < 1) {
			throw new InvalidBookCustomerBookIdException(bookCustomerDto.id, bookCustomerDto.bookId);
		}
	}

	public BookCustomerDto getBookCustomerByBookIdAndCustomerId(Integer bookId, Integer customerId) {
		BookCustomer bookCustomer = bookCustomerRepository.getBookCustomerByBookIdAndCustomerId(bookId, customerId);
		BookCustomerDto bookCustomerDto = Mapper.mapToBookCustomerDto(bookCustomer);
		return bookCustomerDto;
	}
}