package com.mobile.brot.Notification.Helper;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyByEmail {

	private final Logger LOG = LoggerFactory.getLogger(NotifyByEmail.class);
	private final long serialVersionUID = 1L;
	private Map<String, String> templateData;
	private String templateName;
	private final String SEPARATORS = ",; ";
	private static final String SMTP_HOST_NAME = "smtp.mail.yahoo.com";
    private static final int SMTP_HOST_PORT = 587;//465,587,25
    private static final String SMTP_AUTH_USER = "raja_seelam@yahoo.com";
    private static final String SMTP_AUTH_PWD  = "Sekhar03";
	
	private static String ALERT_TEMPLATE;
	private static Map<String, StringBuffer> templates = new HashMap<String, StringBuffer>();
	
	public NotifyByEmail(NotificationInfo info) {
		this.templateData = info.getTemplateMap();
		this.templateName = templateData.get("templateName").toString();
		this.ALERT_TEMPLATE = templateName;
	}
	
	
	/*
	 * Will send email with the data sent to this method.
	 */
	public void sendMail() throws Exception {

		String host = templateData.get("Host").toString(); 
		String from = templateData.get("From").toString(); 
		String to = templateData.get("To").toString();
		String cc = templateData.get("Cc").toString(); 
		String bcc = templateData.get("Bcc").toString(); 
		String subject = templateData.get("subject").toString(); 
		StringBuffer messageBuffer = getTemplate(ALERT_TEMPLATE);;
		replace(messageBuffer, templateData);
		String text = messageBuffer.toString(); 
		String domainName = templateData.get("DomainName").toString();
		System.out.println("Domain Name : " + domainName);
		
		//First check basic things. to vector should not be null
		if (from == null
				|| (to == null))
			throw new Exception("Not all required information is available");
		try {
			// Get system properties
			Properties props = System.getProperties();
			// Setup mail server
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", SMTP_HOST_NAME);
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.port", "587");
	        
			// Get session
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
				}
			  });
			
			System.out.println("Session  : " + session);
			
			// Define message
			Message message = new MimeMessage(session);
			// Set the from address
			if (from.indexOf('@') < 0)
				from += "@" + domainName;
			message.setFrom(new InternetAddress(from));
			// Set the to address. There can be multiple to addresses
			Address addrTo[] = parseString(to,domainName);
			Address addrCc[] = parseString(cc,domainName);
			Address addrBcc[] = parseString(bcc,domainName);
			if (addrTo != null && addrTo.length > 0)
				message.setRecipients(javax.mail.Message.RecipientType.TO, addrTo);
			if (addrCc != null && addrCc.length > 0)
				message.setRecipients(javax.mail.Message.RecipientType.CC, addrCc);
			if (addrBcc != null && addrBcc.length > 0)
				message.setRecipients(javax.mail.Message.RecipientType.BCC, addrBcc);
			// Set the subject
			message.setSubject(subject);
			//------------------

			// We must use a MIME message for slick HTML based messages
			Multipart mp = new MimeMultipart();

			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent(text, "text/html");
			mp.addBodyPart(mbp1);

			/*Transport transport = session.getTransport();
			transport.connect
	          (SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);*/
			message.setContent(mp);
			// Send message
			Transport.send(message);

		} catch (AddressException e) {
			LOG.error("Address Exception : "+e);
			throw new Exception(e.getMessage());
		} catch (MessagingException e) {
			LOG.error("MessagingException : "+e);
			throw new Exception(e.toString());
		}catch (Exception e){
			LOG.error("Unknown Exception : "+e);
		}
	}
	
	/*
	 * parse string and convert the addresses to RFC822 Internet Address format.
	 * @param str address string to be parsed @return array of Addresses
	 */
	private Address[] parseString(String str, String domainName) throws AddressException {
		if (str == null || str.equals("")) // should not happen...but hey who
			// knows ;)
			return null;
		ArrayList addrList = new ArrayList();
		StringTokenizer strToken = new StringTokenizer(str, SEPARATORS);
		while (strToken.hasMoreTokens()) {
			String mailId = strToken.nextToken();
			if (mailId.indexOf('@') < 0) { // if domain not specified add
				// cisco.com
				mailId += "@" + domainName;
			}
			InternetAddress inetAddr = new InternetAddress(mailId);
			addrList.add(inetAddr);
		}
		if (addrList.size() > 0) {
			Address addrArray[] = new Address[addrList.size()];
			addrList.toArray((Address[]) addrArray);
			return addrArray;
		} else
			return null; // returning null here removes the field from the
		// Message Header
	}
	
	/*
	 * Replace logic
	 */
	private static void replace(StringBuffer buffer, Map<String, String> map){
		Iterator itr = map.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<String, String> entry = (Map.Entry) itr.next();
			String oldValue = (String)entry.getKey();
			String newValue = (String)entry.getValue();
			if(buffer != null && oldValue != null && newValue != null){
				int startIndex = buffer.indexOf(oldValue);
				while(startIndex > -1){
					int endIndex = startIndex + oldValue.length();
					buffer.replace(startIndex, endIndex, newValue);
					startIndex = buffer.indexOf(oldValue, endIndex);
				}
			}
		}
    }
	
	
	
	/*
	 * Will return template text as StringBuffer
	 */
	private StringBuffer getTemplate(String resourcePath){
		StringBuffer buffer = new StringBuffer();
		if(templates.containsKey(resourcePath)){
			StringBuffer template = (StringBuffer)templates.get(resourcePath);
			buffer.append(template.toString());
		} else {
			synchronized(NotifyByEmail.class) {
				InputStream is = null;
				BufferedInputStream bis = null;
				try{
					StringBuffer template = new StringBuffer();
					is = NotifyByEmail.class.getClassLoader().getResourceAsStream(resourcePath);
					bis = new BufferedInputStream(is);
					byte[] bytes = new byte[bis.available()];
					bis.read(bytes);
					template.append(new String(bytes));
					templates.put(resourcePath, template);
					buffer.append(template.toString());
				}catch(Exception ex){
					LOG.error("Error Loading Mail template: " + resourcePath, ex);
				}finally{
					try{
						if(bis != null)
							bis.close();
						if(is != null)
							is.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		return buffer;
	}
	
}
