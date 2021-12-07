package exceptions.repository.bookcustomer;

import exceptions.InfrastructureException;

public class BookCustomerIdCannotBeNullException  extends InfrastructureException {
	private static final long serialVersionUID = -9065850122753978355L;

	public BookCustomerIdCannotBeNullException() {
		super("Book customer id cannot be null");
	}

	@Override
	public String getCode() {
		String code = "book_customer_id_cannot_be_null";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "BookCustomerRepository";
		return className;
	}
}
