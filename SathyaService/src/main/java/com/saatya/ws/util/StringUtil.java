package com.saatya.ws.util;

import org.springframework.http.HttpHeaders;



public class StringUtil extends  org.apache.commons.lang3.StringUtils {

	public static final String NULL = "null";

	public static boolean isNull(String val) {
		return isBlank(val) ? true : val.equalsIgnoreCase(NULL);
	}
	
	public static boolean isNull(Object val) {
		
		if (val == null) return true;
		
		if (val instanceof String) {
			return isNull((String)val);
		} else {
			return isNull(String.valueOf(val));
		}
	}
	
	public static HttpHeaders addAccessControllAllowOrigin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return headers;
    }

}
