package com.siblinks.ws.response;

public class ErrorResponse extends Response{

	public ErrorResponse(String msg){
		//super.setType("ErrorResponse");
		super.setStatus(String.valueOf(Status.ERROR));
		super.setRequest_data_result(msg);
	}
}
