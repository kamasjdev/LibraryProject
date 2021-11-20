package exceptions.baseservice;

import exceptions.ServiceException;

public class CannotSerializeObjectsException extends ServiceException  {
	private static final long serialVersionUID = -413340467578515768L;
	
	@SuppressWarnings("rawtypes")
	public Class clazz;
	
	public CannotSerializeObjectsException(@SuppressWarnings("rawtypes") Class clazz) {
		super(String.format("Cannot serialize objects of class %1$s", clazz.getName()));
		this.clazz = clazz;
	}
	
	@Override
	public String getCode() {
		String code = "cannot_serialize_objects";
		return code;
	}

	@Override
	public String classNameThrown() {
		String classNameThrown = "AbstractBaseService";
		return classNameThrown;
	}	
}
