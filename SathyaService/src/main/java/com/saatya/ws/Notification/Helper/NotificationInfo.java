package com.saatya.ws.Notification.Helper;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="NotificationInfo")
public class NotificationInfo  implements Serializable {
		
		
		private static final long serialVersionUID = 1683140299513210898L;
	    private Map<String, String> templateMap;		
		private long userId;
		
		public long getUserId() {
			return userId;
		}
		public void setUserId(long userId) {
			this.userId = userId;
		}
		/**
		 * @return the templateMap
		 */
		public Map<String, String> getTemplateMap() {
			return templateMap;
		}
		/**
		 * @param templateMap the templateMap to set
		 */
		public void setTemplateMap(Map<String, String> templateMap) {
			this.templateMap = templateMap;
		}
		/**
		 * @param alertUnit the alertUnit to set
		 */
		
}
