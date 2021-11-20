package run;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import entities.Author;
import entities.Book;
import entities.Customer;
import entities.MenuAction;
import exceptions.ExceptionToResponseMapperImpl;
import interfaces.ActionService;
import managers.AuthorManager;
import managers.BookManager;
import managers.CustomerManager;
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
		currentDirectory = System.getProperty("user.dir");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss.SSS");
		Date now = new Date();
	    String strDate = sdf.format(now);
		
		try {
			fileHandler = new FileHandler(currentDirectory + "_logs_" + strDate + ".log");
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		LOGGER.addHandler(fileHandler);
		SimpleFormatter formatter = new SimpleFormatter();
		fileHandler.setFormatter(formatter);
		
		Scanner scanner = new Scanner(System.in);
		actionService = new ActionServiceImpl(scanner);
		BookService bookService = new BookService();
		BookAuthorService bookAuthorService = new BookAuthorService();
		AuthorService authorService = new AuthorService();
		BillService billService = new BillService();
		CustomerService customerService = new CustomerService();
		MenuActionService menuActionService = new MenuActionService();
		BookCustomerService bookCustomerService = new BookCustomerService();
		BookManager bookManager = new BookManager(bookService, bookAuthorService, authorService, customerService, bookCustomerService, billService, actionService);
		CustomerManager customerManager = new CustomerManager(customerService, billService, bookService, actionService);
		AuthorManager authorManager = new AuthorManager(authorService, bookService, actionService);
		ExceptionToResponseMapperImpl exceptionToResponseMapper = new ExceptionToResponseMapperImpl();
		
		System.out.println("Welcome in Library app!");
		
		while(true) {
			System.out.println("To close application enter -1. Menu list:");
			List<MenuAction> menus = menuActionService.GetMenuActionByMenuName("Main");
			menus.forEach(m -> System.out.println(m.id + ". " + m.name));
			
			try {
				Integer key = actionService.inputLine(Integer.class);
				
				if(key == -1) {
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
						
						authorManager.AddAuthor();
						break;
					case 2:
						System.out.println("Add Book to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						List<Author> authors = authorManager.GetAll();
						
						if(authors.isEmpty()) {
							System.out.println("First add some authors then add new book");
							break;
						}
						
						List<Author> chosenAuthors = new ArrayList<Author>();
						boolean getNext = true;
						int part = 0;
						while(getNext) {
							PageableResult<Author> authorsDivided = GetPartListOfObjects(authors, part);
							Result result = TryGetObjectsFromPageableResult(authorsDivided, part);
							
							if(result == null) {
								break;
							}
							
							for(Integer id : result.ids) {
								Author author = authorService.GetById(id);
								
								if(author != null) {
									chosenAuthors.add(author);
								}
							}
							
							if(result.stop) {
								getNext = false;
							}
							
							part = result.nextPage;
						}
						
						bookManager.AddBook(chosenAuthors);
						break;
					case 3:
						System.out.println("Add Customer to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						customerManager.AddCustomer();
						break;
					case 4:
						System.out.println("View Authors to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						authors = authorManager.GetAll();
						getNext = true;
						part = 0;
						while(getNext) {
							PageableResult<Author> authorsDivided = GetPartListOfObjects(authors, part);
							Result result = ShowPageableResult(authorsDivided, part);
														
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
						
						Integer authorId = actionService.inputLine(Integer.class);
						authorManager.GetAutorDetails(authorId);
						break;
					case 6:
						System.out.println("View Books to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						List<Book> books = bookManager.GetAll();
						getNext = true;
						part = 0;
						while(getNext) {
							PageableResult<Book> authorsDivided = GetPartListOfObjects(books, part);
							Result result = ShowPageableResult(authorsDivided, part);
														
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
						
						Integer bookId = actionService.inputLine(Integer.class);
						bookManager.GetBookDetails(bookId);
						break;
					case 8:
						System.out.println("View Customers to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						List<Customer> customers = customerManager.GetAll();
						getNext = true;
						part = 0;
						while(getNext) {
							PageableResult<Customer> authorsDivided = GetPartListOfObjects(customers, part);
							Result result = ShowPageableResult(authorsDivided, part);
														
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
												
						Integer customerId = actionService.inputLine(Integer.class);
						customerManager.GetCustomerDetails(customerId);
						break;
					case 10:
						System.out.println("Edit author to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter authorId");
						authorId = actionService.inputLine(Integer.class);
						authorManager.DeleteAuthor(authorId);
						break;
					case 11:
						System.out.println("Edit book to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter bookId");
						bookId = actionService.inputLine(Integer.class);
						bookManager.EditBook(bookId);
						break;
					case 12:
						System.out.println("Edit customer to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter customerId");
						customerId = actionService.inputLine(Integer.class);
						customerManager.EditCustomer(customerId);
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
						bookManager.BorrowBook(bookId, customerId);
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
						bookManager.ReturnBook(bookId, customerId);
						break;
					case 15:
						System.out.println("Delete author to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter authorId");
						authorId = actionService.inputLine(Integer.class);
						authorManager.DeleteAuthor(authorId);
						break;
					case 16:
						System.out.println("Delete book to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter bookId");
						bookId = actionService.inputLine(Integer.class);
						bookManager.DeleteBook(bookId);
						break;
					case 17:
						System.out.println("Delete customer to back press -1");
						value = actionService.inputLine(String.class);
						
						if(value.equals("-1")) {
							break;
						}
						
						System.out.println("Enter customerId");
						customerId = actionService.inputLine(Integer.class);
						customerManager.DeleteCustomer(customerId);
						break;
					default:
						System.out.println("Entered invalid key");
						break;
						
				}
				
			} catch(Exception exception) {
				String description = exceptionToResponseMapper.Map(exception).toString(); 
				System.out.println(description);
				LOGGER.log(Level.SEVERE, exception.toString(), exception);
			}
		}
		
		scanner.close();
	}
	
	private static <T> PageableResult<T> GetPartListOfObjects(List<T> objects, int part){
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
	
	private static <T> Result TryGetObjectsFromPageableResult(PageableResult<T> pageableResult, int page) {
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
			System.out.print("Enter id");
			Integer authorId = actionService.inputLine(Integer.class);
			chosenValues.add(authorId);
		}
		
		Result result = Result.Create(chosenValues, page, nextPage, returnValue);
		return result;
	}
	
	private static <T> Result ShowPageableResult(PageableResult<T> pageableResult, int page) {
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
