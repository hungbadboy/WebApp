/**
 * 
 */
package com.brot.admin.util;

/**
 * @author viddasar
 *
 */
public class CommonUtil {
	public static StringBuffer fixURL(String endpointUrl, String serviceName) {
		StringBuffer url = new StringBuffer(endpointUrl);

		if (!endpointUrl.trim().endsWith("?")) {
			url.append(serviceName);
			url.append("?");
		}
		return url;
	}
}
