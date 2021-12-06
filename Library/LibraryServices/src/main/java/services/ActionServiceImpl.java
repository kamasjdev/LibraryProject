package services;

import java.math.BigDecimal;
import java.util.Scanner;

import interfaces.ActionService;

public class ActionServiceImpl implements ActionService {
	private final Scanner scanner;
	
	public ActionServiceImpl(Scanner scanner) {
		this.scanner = scanner;
	}
	
	@Override
	public <T> T inputLine(Class<T> clazz) {
		
		T obj = null;
		try {
	        if(clazz == Integer.class) {
	            obj = clazz.cast(Integer.valueOf(scanner.nextInt()));
	            scanner.nextLine(); // problem with integer cannot read whole line
	        }
	        if(clazz == String.class) { 
        		obj = clazz.cast(scanner.nextLine());
	        }
	        if(clazz == Double.class) {
	            obj = clazz.cast(Double.valueOf(scanner.nextDouble()));
	        }
	        if(clazz == Float.class) {
	            obj = clazz.cast(Float.valueOf(scanner.nextFloat()));
	        }
	        if(clazz == BigDecimal.class) {
	        	obj = clazz.cast(scanner.nextBigDecimal()); 
	        }
	    } catch (Exception e) {
	    	if(clazz == Integer.class)
	    		scanner.next(); // prevent infinite loop
	    	throw e;
	    } finally {
	    	scanner.reset();
	    }
		return obj;
	}
}
