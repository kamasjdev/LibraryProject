package interfaces;

import dto.CustomerDto;

public interface CustomerService extends BaseService<CustomerDto> {
	CustomerDto getDetails(Integer customerId);
	boolean canBorrow(Integer customerId);		
}