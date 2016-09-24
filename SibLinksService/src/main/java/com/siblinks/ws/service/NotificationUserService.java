package com.siblinks.ws.service;

import org.springframework.http.ResponseEntity;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;

public interface NotificationUserService {

	public ResponseEntity<Response> getNotificationNotReaded(RequestData request);

	public ResponseEntity<Response> updateStatusNotification(RequestData request);

	public ResponseEntity<Response> getAllNotification(RequestData request);

	public ResponseEntity<Response> updateStatusAllNotification(RequestData request);

	public ResponseEntity<Response> getNotificationReaded(RequestData request);
}
