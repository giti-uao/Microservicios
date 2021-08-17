package api.endpoints.springbootactuator.models;

public class responseModel {
	private String message;
	private int code;
	public responseModel() {
		super();
	}
	public responseModel(String message, int code, String status) {
		super();
		this.message = message;
		this.code = code;
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private String status;
}
