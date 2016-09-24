package com.siblinks.ws.response;

public class SimpleResponse extends Response {
	

    public SimpleResponse(final String status, final Object msg) {
        super(status, msg);
    }

    public SimpleResponse(final String status, final Object msg, final String count) {
        super(status, msg, count);
    }

    public SimpleResponse(final String status, final String request_data_type, final String request_data_method, final Object message) {
        super(status, request_data_type, request_data_method, message);
    }

    public SimpleResponse(final String status, final String request_data_type, final String request_data_method, final Object message, final String count) {
        super(status, request_data_type, request_data_method, message, count);
    }

    /**
     * This method reponse rows attribute for Admin using JQGrid
     * 
     * @param message
     */
    public SimpleResponse(final Object message) {
        super(message);
    }
}
