package run;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import connection.db.DbClientImpl;
import connection.db.DbConnectionImpl;
import entities.Author;
import entities.Bill;
import entities.Book;
import entities.BookAuthor;
import entities.BookCustomer;
import entities.Customer;
import entities.MenuAction;
import exceptions.ExceptionToResponseMapperImpl;
import helpers.manager.customer.UpdateCustomer;
import interfaces.ActionService;
import interfaces.AuthorRepository;
import interfaces.BillRepository;
import interfaces.BookAuthorRepository;
import interfaces.BookCustomerRepository;
import interfaces.BookRepository;
import interfaces.CustomerRepository;
import interfaces.DbClient;
import interfaces.DbConnection;
import interfaces.MapEntity;
import managers.AuthorManager;
import managers.BookManager;
import managers.CustomerManager;
import mappings.AuthorMapping;
import mappings.BillMapping;
import mappings.BookAuthorMapping;
import mappings.BookCustomerMapping;
import mappings.BookMapping;
import mappings.CustomerMapping;
import repository.AuthorRepositoryImpl;
import repository.BillRepositoryImpl;
import repository.BookAuthorRepositoryImpl;
import repository.BookCustomerRepositoryImpl;
import repository.BookRepositoryImpl;
import repository.CustomerRepositoryImpl;
import services.ActionServiceImpl;
import services.AuthorService;
import services.BillService;
import services.BookAuthorService;
import services.BookCustomerService;
import services.BookService;
import services.CustomerService;
import services.MenuActionService;

public class Program {
	private static final Logger LOGGER = Logger.getLogger( Program.class.getName() );
	private static ActionService actionService = null;
	private static FileHandler fileHandler;
	private static String currentDirectory;
	
	public static void main(String[] args) {
		currentDirectory = System.getProperty("user.dir") + File.separator;
		System.out.println(currentDirectory);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss.SSS");
		Date now = new Date();
	    String strDate = sdf.format(now);
		
		try {
			fileHandler = new FileHandler(currentDirectory + "logs_" + strDate + ".log");
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		LOGGER.addHandler(fileHandler);
		SimpleFormatter formatter = new SimpleFormatter();
		fileHandler.setFormatter(formatter);
		
		Scanner scanner = new Scanner(System.in);
		actionService = new ActionServiceImpl(scanner);
		
		String connectionString = "jdbc:mysql://localhost:3306/library?user=root&password=";
		String dbPackage = "com.mysql.cj.jdbc.Driver";
		DbConnection dbConnection = new DbConnectionImpl(connectionString, dbPackage); 
		DbClient dbClient = new DbClientImpl(dbConnection);
		
		MapEntity<Book> bookMapping = new BookMapping();
		MapEntity<Bill> billMapping = new BillMapping();
		MapEntity<BookAuthor> bookAuthorMapping = new BookAuthorMapping();
		MapEntity<Author> authorMapping = new AuthorMapping();
		MapEntity<BookCustomer> bookCustomerMapping = new BookCustomerMapping();
		MapEntity<Customer> customerMapping = new CustomerMapping();
		
		BookRepository bookRepository = new BookRepositoryImpl(dbClient, bookMapping, bookAuthorMapping, authorMapping, bookCustomerMapping, customerMapping);
		BillRepository billRepository = new BillRepositoryImpl(dbClient, billMapping);
		BookAuthorRepository bookAuthorRepository = new BookAuthorRepositoryImpl(dbClient, bookAuthorMapping, bookMapping, authorMapping);
		BookCustomerRepository bookCustomerRepository = new BookCustomerRepositoryImpl(dbClient, bookCustomerMapping, bookMapping, customerMapping);
		AuthorRepository authorRepository = new AuthorRepositoryImpl(dbClient, authorMapping, bookAuthorMapping, bookMapping);
		CustomerRepository customerRepository = new CustomerRepositoryImpl(dbClient, customerMapping);
		
		AuthorService authorService = new AuthorService(authorRepository);
		BookService bookService = new BookService(bookRepository);
		BillService billService = new BillService(billRepository);
		BookAuthorService bookAuthorService = new BookAuthorService(bookAuthorRepository);
		BookCustomerService bookCustomerService = new BookCustomerService(bookCustomerRepository);
		CustomerService customerService = new CustomerService(customerRepository);
		
		UpdateCustomer updateCustomer = new UpdateCustomer();
		
		MenuActionService menuActionService = new MenuActionService();		
		BookManager bookManager = new BookManager(bookService, bookAuthorService, authorService, customerService, bookCustomerService, billService, updateCustomer);
		CustomerManager customerManager = new CustomerManager(customerService, billService, bookService);
		AuthorManager authorManager = new AuthorManager(authorService, bookService);
		ExceptionToResponseMapperImpl exceptionToResponseMapper = new ExceptionToResponseMapperImpl();
		
		System.out.println("Welcome in Library app!");
		
		while(true) {
			System.out.println("To close application enter -1. Menu list:");
			List<MenuAction> menus = menuActionService.getMenuActionByMenuName("Main");
			menus.forEach(m -> System.out.println(m.id + ". " + m.name));
			
			try {
				Integer key = actionService.inputLine(Integer.class);
				
				if(key.equals(-1)) {
					System.out.println("Closing application");
					break;
				}
				
				switch(key) {
					case 1:
						System.out.println("Add Author to back press -1");
						String value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Please enter first name for author");
						String firstName = actionService.inputLine(String.class);	
						System.out.println("Please enter last name for author");
						String lastName = actionService.inputLine(String.class);
						
						authorManager.addAuthor(firstName, lastName);
						break;
					case 2:
						System.out.println("Add Book to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						List<Author> authors = authorManager.getAll();
						
						if(authors.isEmpty()) {
							System.out.println("First add some authors then add new book");
							break;
						}
						
						System.out.println("Please enter name for book");
						String name = actionService.inputLine(String.class);
						System.out.println("Please enter ISBN");
						String ISBN = actionService.inputLine(String.class);
						System.out.println(String.format("Please enter cost for book %1$s", name));
						BigDecimal cost = actionService.inputLine(BigDecimal.class);
						
						List<Author> chosenAuthors = new ArrayList<Author>();
						boolean getNext = true;
						int part = 0;
						while(getNext) {
							PageableResult<Author> authorsDivided = getPartListOfObjects(authors, part);
							Result result = tryGetObjectsFromPageableResult(authorsDivided, part);
							
							if(result == null) {
								break;
							}
							
							for(Integer id : result.ids) {
								Author author = authorService.getById(id);
								
								if(author != null) {
									chosenAuthors.add(author);
								}
							}
							
							if(result.stop) {
								getNext = false;
							}
							
							part = result.nextPage;
						}
						
						bookManager.addBook(name, ISBN, cost, chosenAuthors);
						break;
					case 3:
						System.out.println("Add Customer to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Please enter first name for customer");
						firstName = actionService.inputLine(String.class);
						System.out.println("Please enter last name for customer");
						lastName = actionService.inputLine(String.class);
						
						customerManager.addCustomer(firstName, lastName);
						break;
					case 4:
						System.out.println("View Authors to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						authors = authorManager.getAll();
						getNext = true;
						part = 0;
						while(getNext) {
							PageableResult<Author> authorsDivided = getPartListOfObjects(authors, part);
							
							if(authorsDivided == null) {
								System.out.println("There is no authors");
								break;
							}
							
							Result result = showPageableResult(authorsDivided, part);
														
							if(result.stop) {
								getNext = false;
							}
							
							part = result.nextPage;
						}
						break;
					case 5:
						System.out.println("View Author to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter authorId\n");
						Integer authorId = actionService.inputLine(Integer.class);
						authorManager.getAuthorDetails(authorId);
						break;
					case 6:
						System.out.println("View Books to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						List<Book> books = bookManager.getAll();
						getNext = true;
						part = 0;
						while(getNext) {
							PageableResult<Book> booksDivided = getPartListOfObjects(books, part);
							
							if(booksDivided == null) {
								System.out.println("There is no books");
								break;
							}
							
							Result result = showPageableResult(booksDivided, part);
														
							if(result.stop) {
								getNext = false;
							}
							
							part = result.nextPage;
						}
						break;
					case 7:
						System.out.println("View Book to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter bookId\n");
						Integer bookId = actionService.inputLine(Integer.class);
						bookManager.getBookDetails(bookId);
						break;
					case 8:
						System.out.println("View Customers to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						List<Customer> customers = customerManager.getAll();
						getNext = true;
						part = 0;
						while(getNext) {
							PageableResult<Customer> customersDivided = getPartListOfObjects(customers, part);
							
							if(customersDivided == null) {
								System.out.println("There is no customers");
								break;
							}
							
							Result result = showPageableResult(customersDivided, part);
														
							if(result.stop) {
								getNext = false;
							}
							
							part = result.nextPage;
						}
						break;
					case 9:
						System.out.println("View Customer to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter customerId\n");
						Integer customerId = actionService.inputLine(Integer.class);
						customerManager.getCustomerDetails(customerId);
						break;
					case 10:
						System.out.println("Edit author to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter authorId\n");
						authorId = actionService.inputLine(Integer.class);
						
						Author author = authorService.getById(authorId);
						if(author == null) {
							System.out.println(String.format("Not found any author with id: %$1s", authorId));
							break;
						}
						
						System.out.println("Please enter first name for author");
						firstName = actionService.inputLine(String.class);
						System.out.println("Please enter last name for author");
						lastName = actionService.inputLine(String.class);
					
						authorManager.editAuthor(authorId, firstName, lastName);
						break;
					case 11:
						authors = authorService.getEntities();

						if(authors.isEmpty()) {
							System.out.println("Before add book please first add some authors");
							break;
						}
						
						System.out.println("Edit book to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter bookId");
						bookId = actionService.inputLine(Integer.class);
						
						Book book = bookService.getById(bookId);
						
						if(book == null) {
							System.out.println("Book not found");
							break;
						}
						
						System.out.println("Do you want modify authors? Yes(Y) No(N)");
						String input = actionService.inputLine(String.class);
						
						List<Integer> authorIds = new ArrayList<Integer>();
						if(input.equals("Y")) {
							Integer intValue = -1;
							System.out.println("You are changing authors select authors that should be authors of this book");
							while(true) {
								System.out.println("Enter author id, 0 accept changes");
								intValue = actionService.inputLine(Integer.class);
								
								if(intValue < 0) {
									System.out.println("Entered invalid id");
									break;
								}
								
								if(intValue.equals(0)) {
									break;
								}
								
								authorIds.add(intValue);
							}
						}
						
						System.out.println("Enter book name, if dont need to change leave empty");
						String bookName = actionService.inputLine(String.class);
						
						System.out.println("Enter new ISBN, if dont need to change leave empty");
						ISBN = actionService.inputLine(String.class); 
												
						System.out.println("Enter book cost, if dont need to change put -1");
						BigDecimal bookCost = actionService.inputLine(BigDecimal.class);
						
						bookManager.editBook(bookId, bookName, ISBN, bookCost, authorIds);
						break;
					case 12:
						System.out.println("Edit customer to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter customerId\n");
						customerId = actionService.inputLine(Integer.class);
						
						Customer customer = customerService.getById(customerId);
						
						if(customer == null) {
							System.out.println("Customer not found");
							break;
						}
						
						System.out.println("Enter first name, if dont need to change leave empty");
						firstName = actionService.inputLine(String.class);
						
						System.out.println("Enter last name, if dont need to change leave empty");
						lastName = actionService.inputLine(String.class);
						
						System.out.println("Enter limit for borrow, if dont need to change put -1");
						Integer limit = actionService.inputLine(Integer.class);
														
						System.out.println("Set allow borrow book for customer, Y to yes, N to no");
						String borrow = actionService.inputLine(String.class);
						
						customerManager.editCustomer(customerId, firstName, lastName, limit, borrow);
						break;
					case 13:
						System.out.println("Borrow Book to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter bookId");
						bookId = actionService.inputLine(Integer.class);
						System.out.println("Enter customerId");
						customerId = actionService.inputLine(Integer.class);						
						bookManager.borrowBook(bookId, customerId);
						break;
					case 14:
						System.out.println("Return book to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter bookId");
						bookId = actionService.inputLine(Integer.class);
						System.out.println("Enter customerId");
						customerId = actionService.inputLine(Integer.class);
						bookManager.returnBook(bookId, customerId);
						break;
					case 15:
						System.out.println("Delete author to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter authorId");
						authorId = actionService.inputLine(Integer.class);
						authorManager.deleteAuthor(authorId);
						break;
					case 16:
						System.out.println("Delete book to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter bookId");
						bookId = actionService.inputLine(Integer.class);
						bookManager.deleteBook(bookId);
						break;
					case 17:
						System.out.println("Delete customer to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter customerId");
						customerId = actionService.inputLine(Integer.class);
						customerManager.deleteCustomer(customerId);
						break;
					default:
						System.out.println("Entered invalid key");
						break;
						
				}
				
			} catch(Exception exception) {
				String description = exceptionToResponseMapper.map(exception).toString(); 
				System.out.println(description);
				LOGGER.log(Level.SEVERE, exception.toString(), exception);
			}
		}
		
		scanner.close();
		
	}
	
	private static <T> PageableResult<T> getPartListOfObjects(List<T> objects, int part){
		if(objects.isEmpty()) {
			return null;
		}
		
		List<List<T>> objectsDivided = chopped(objects, 9);
		int size = objectsDivided.size();
		
		if(part == -1) {
			part = size - 1;
		}
		
		if(part > size) {
			part = size - 1;
		}
		
		if(part == size) {
			part = size - 1;
		}
		
		List<T> objectsFiletered = objectsDivided.get(part);
		PageableResult<T> pageableResult = PageableResult.Create(objectsFiletered, part, size);
		return pageableResult;
	}
	
	private static <T> List<List<T>> chopped(List<T> list, final int L) {
	    List<List<T>> parts = new ArrayList<List<T>>();
	    final int N = list.size();
	    for (int i = 0; i < N; i += L) {
	        parts.add(new ArrayList<T>(
	            list.subList(i, Math.min(N, i + L)))
	        );
	    }
	    return parts;
	}
	
	private static <T> Result tryGetObjectsFromPageableResult(PageableResult<T> pageableResult, int page) {
		System.out.println("Press '+' to load next 9 results, press '-' to load previous 9 results");
		System.out.println("To undo this action press '-1', to enter id press ';', to accept entered values press '.'. Select record:");
		System.out.println(pageableResult);
		boolean returnValue = false;
		List<Integer> chosenValues = new ArrayList<Integer>();
		String inLine = actionService.inputLine(String.class);
		int nextPage = page;
		boolean enterId = false;
		boolean quitMethod = false;
		
		if(inLine.getClass() == String.class) {
			String line = (String) inLine;
			switch(line) {
				case "-":
					nextPage = page - 1;
					break;
				case "+":
					nextPage = page + 1;
					break;
				case ";":
					enterId = true;
					break;
				case ".":
					returnValue = true;
					break;
				case "-1":
					quitMethod= true;
					break;
				default:
					break;
			}
		}
		
		if(quitMethod) {
			return null;
		}
		
		if(enterId) {
			System.out.print("Enter id\n");
			Integer authorId = actionService.inputLine(Integer.class);
			chosenValues.add(authorId);
		}
		
		Result result = Result.Create(chosenValues, page, nextPage, returnValue);
		return result;
	}
	
	private static <T> Result showPageableResult(PageableResult<T> pageableResult, int page) {
		System.out.println("Press '+' to load next 9 results, press '-' to load previous 9 results");
		System.out.println("To undo this action press '-1'");
		System.out.println(pageableResult);
		boolean returnValue = false;
		String inLine = actionService.inputLine(String.class);
		int nextPage = page;
		
		if(inLine.getClass() == String.class) {
			String line = (String) inLine;
			switch(line) {
				case "-":
					nextPage = page - 1;
					break;
				case "+":
					nextPage = page + 1;
					break;
				case "-1":
					returnValue = true;
					break;
				default:
					break;
			}
		}
		
		Result result = Result.Create(null, page, nextPage, returnValue);
		return result;
	}
}
