package com.saatya.ws.response;


public class SimpleResponse extends Response{

	
	public SimpleResponse(String status, Object msg) {
		super( status, msg);
	}
	
	public SimpleResponse(String status, Object msg,String count) {
		super( status, msg,count);
	}
			
}
