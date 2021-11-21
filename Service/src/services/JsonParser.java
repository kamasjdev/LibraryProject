package services;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import exceptions.jsonparser.CannotDeserializeObjectsException;
import exceptions.jsonparser.CannotSerializeObjectsException;

public class JsonParser {
	public static <T> T deserializeObject(Class<T> clazz, String jsonString) {		
		if(jsonString.isEmpty()) {
			return null;
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		T object;
		try {
			object = objectMapper.readValue(jsonString, new TypeReference<T>() {});
		} catch (JsonProcessingException e) {
			throw new CannotDeserializeObjectsException(clazz, e);
		}
		
		return object; 
	}
	
	public static <T> List<T> deserializeObjects(Class<T> clazz, String jsonString){
		if(jsonString.isEmpty()) {
			return null;
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<T> listObject;
		try {
			TypeFactory typeFactory = TypeFactory.defaultInstance();
			listObject = objectMapper.readValue(jsonString, typeFactory.constructCollectionType(ArrayList.class,clazz));
		} catch (JsonProcessingException e) {
			throw new CannotDeserializeObjectsException(clazz, e);
		}
		
		return listObject; 
	}
	
	public static <T> String serializeObject(Class<T> clazz, T obj) {
		return getJsonString(clazz, obj);
	}
	
	public static <T> String serializeObjects(Class<T> clazz, List<T> objects) {
		return getJsonString(clazz, objects);
	}
	
	private static String getJsonString(@SuppressWarnings("rawtypes") Class clazz, Object obj){
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString;
		try {
			jsonString = objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new CannotSerializeObjectsException(clazz, e);
		}
		return jsonString;
	}
}
