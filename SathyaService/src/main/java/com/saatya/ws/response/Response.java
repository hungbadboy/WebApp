package com.saatya.ws.response;


public abstract class Response {

	
	private String status;
	private Object response;
	private String count;
	
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}
	public Response(){}
	public Response(String status, Object message) {
		super();
		
		this.status = status;
		this.response = message;
		
	}
	
	public Response(String status, Object message,String count) {
		super();
		
		this.status = status;
		this.response = message;
		this.count=count;
		
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
	
}
