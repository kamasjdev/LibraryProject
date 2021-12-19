package exceptions;

public class ExceptionResponse {
	private String code;
	private String reason;
	private String clazzThrownName;
	
	public ExceptionResponse(String reason, String code, String clazzThrownName) {
		this.reason = reason;
		this.code = code;
		this.clazzThrownName = clazzThrownName;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getReason() {
		return reason;
	}
	
	public String getClazzThrownName() {
		return clazzThrownName;
	}
	
	@Override
	public String toString() {
		StringBuilder desciption = new StringBuilder("{").append("\"code\": \"");
		desciption.append(code).append("\", ");
		desciption.append("\"reason\": \"").append(reason).append("\", ");
		desciption.append("\"classThrown\": \"").append(clazzThrownName);
		desciption.append("\"}");
		return desciption.toString();
	}
}
