package com.siblinks.ws.response;

public abstract class Response {

    private String status;
    private String request_data_type;
    private String request_data_method;
    private Object request_data_result;
    private String count;
    private Object rows;

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Response() {
    }

    public Response(String status, Object message) {
        super();

        this.status = status;
        this.request_data_result = message;

    }

    public Response(Object message) {
        super();
        this.rows = message;
    }

    public Response(String status, String request_data_type, String request_data_method, Object message) {
        super();

        this.status = status;
        this.request_data_type = request_data_type;
        this.request_data_method = request_data_method;
        this.request_data_result = message;

    }

    public Response(String status, String request_data_type, String request_data_method, Object message, String count) {
        super();

        this.status = status;
        this.request_data_type = request_data_type;
        this.request_data_method = request_data_method;
        this.request_data_result = message;
        this.count = count;

    }

    public Response(String status, Object message, String count) {
        super();

        this.status = status;
        this.request_data_result = message;
        // this.count=count;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequest_data_type() {
        return request_data_type;
    }

    public void setRequest_data_type(String request_data_type) {
        this.request_data_type = request_data_type;
    }

    public String getRequest_data_method() {
        return request_data_method;
    }

    public void setRequest_data_method(String request_data_method) {
        this.request_data_method = request_data_method;
    }

    public Object getRequest_data_result() {
        return request_data_result;
    }

    public void setRequest_data_result(Object request_data_result) {
        this.request_data_result = request_data_result;
    }
}
