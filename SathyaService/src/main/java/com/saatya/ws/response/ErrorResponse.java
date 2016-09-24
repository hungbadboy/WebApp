package com.saatya.ws.response;

public class ErrorResponse extends Response{

	public ErrorResponse(String msg){
		//super.setType("ErrorResponse");
		super.setStatus(String.valueOf(Status.ERROR));
		super.setResponse(msg);
	}
}
