package com.brot.admin.client;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public class HttpConnectionManager {
	
	private static Object latch = new Object();

	private static HttpClient httpClient = null;
	
	protected static volatile long coc = 0;
	protected static volatile long cic = 0;

	public static HttpClient getHttpClient() {
		if (httpClient == null) {
			synchronized (latch) {
				if (httpClient == null) {
					httpClient = new HttpClient(new SaatyaMultiThreadedConnectionManager());
				}
			}
		}
		coc++;
		return httpClient;
	}
}

class SaatyaMultiThreadedConnectionManager extends MultiThreadedHttpConnectionManager {
	
	public SaatyaMultiThreadedConnectionManager() {
		super();
	}
	
	public void releaseConnection(HttpConnection conn) {
		HttpConnectionManager.cic++;
		super.releaseConnection(conn);
	}
}
