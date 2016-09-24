package com.mobile.brot.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.transform.stream.StreamResult;

import com.mobile.brot.domain.NotificationInfo;

public class NotificationEmailClient {

	private static String endPointUrl;
	private static NotificationEmailClient instance;
	
	
	private NotificationEmailClient(){}
	
	public static NotificationEmailClient getInstance(String url) {
		endPointUrl = url;
        if (instance==null)
                 instance = new NotificationEmailClient();
        return instance;
	}
	
	
	/*
	 * Method to be invoked to send Email notification
	 */
	public static String sendEmail(NotificationInfo info){
		StringBuilder sb = new StringBuilder();
		
		try{
			URL url = new URL(endPointUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/xml");
			JAXBContext context = JAXBContext.newInstance(NotificationInfo.class);
			String strReq = marshall(context, info) ;
			OutputStream os = conn.getOutputStream();
			os.write(strReq.getBytes());
			os.flush();
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
			    sb.append(line);
			}
			rd.close();
			conn.disconnect();
			System.out.println("Notification Email Status  :" + sb.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	/*
	 * For marshaling the object being sent to the service	
	 */
	private static String marshall(JAXBContext context, Object source) throws JAXBException {
		if (source == null) {
			
			throw new NullPointerException("NullPointerException Request is null");
		}
		String retString = "";
	    StringWriter sos = new StringWriter();
	    Marshaller marshaller = context.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
	    
	    ValidationEventCollector veh = new ValidationEventCollector();
	    marshaller.marshal(source, new StreamResult(sos));
	    retString = sos.toString();
	    if (veh.hasEvents()) {
	    	throw new JAXBException("ValidationEventCollector errors: "+ veh.getEvents().toString());
		}
	
	    return retString;
	}
	
	
	public static void main(String args[]){
		String endPointUrl =  "http://localhost:8095/SaatyaNotification/rest/notify/sendEMail";
		//String endPointUrl =  "http://vm-disp-02:8085/DISPMonitoring-1.0/monitor/notify/sendEMail";
		NotificationEmailClient dnc = NotificationEmailClientFactory.getNotifEmailClient(endPointUrl);
		NotificationInfo info = new NotificationInfo();
		//GREEN, "#00ff00"		RED, "#ff0000" 	YELLOW, "#ffff00" 
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("###USER_ID###", "112");		
	    map.put("subject", "SAATYA User Activation");
	    map.put("Host", "");
	    map.put("From", "raja_seelam@yahoo.com");
	    map.put("To", "balaraju.kagidala@yahoo.com");
	    map.put("DomainName", "gmail.com");
	    map.put("Cc", "");
	    map.put("Bcc", "");
	    map.put("templateName", "MAIL_Notify_1.template");
	    info.setTemplateMap(map);	   
		sendEmail(info);
	}
	
}
