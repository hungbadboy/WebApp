package com.siblinks.ws.Notification.Helper;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

@SuppressWarnings("deprecation")
@Component("notifyByEmail")
public class NotifyByEmail {

    private final Logger LOG = LoggerFactory.getLogger(NotifyByEmail.class);

    private JavaMailSender mailSender;

    private VelocityEngine velocityEngine;

    public NotifyByEmail() {
    };

    /**
     * @return the mailSender
     */
    public JavaMailSender getMailSender() {
        return mailSender;
    }

    /**
     * @param mailSender
     *            the mailSender to set
     */
    public void setMailSender(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * @return the velocityEngine
     */
    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    /**
     * @param velocityEngine
     *            the velocityEngine to set
     */
    public void setVelocityEngine(final VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    private Map<String, String> templateData;
    // private String templateName;
    private final String SEPARATORS = ",; ";
    private static String ALERT_TEMPLATE;
    private static Map<String, StringBuffer> templates = new HashMap<String, StringBuffer>();

    public NotifyByEmail(final NotificationInfo info) {
        this.templateData = info.getTemplateMap();
        // this.templateName = templateData.get("templateName").toString();
        // this.ALERT_TEMPLATE = templateName;
    }

    /*
     * Will send email with the data sent to this method.
     */
    public String sendMail() throws Exception {
        LOG.info("Sleeping now...");
        Thread.sleep(10L);

        LOG.info("Sending email...");
        String status = "SUCCESS";
        templateData.get("Host").toString();
        String to = templateData.get("To").toString();
        String cc = templateData.get("Cc").toString();
        String bcc = templateData.get("Bcc").toString();
        String subject = templateData.get("subject").toString();
        StringBuffer messageBuffer = getTemplate(ALERT_TEMPLATE);
        ;
        replace(messageBuffer, templateData);
        String text = messageBuffer.toString();
        String domainName = templateData.get("DomainName").toString();
        System.out.println("Domain Name : " + domainName);

        // First check basic things. to vector should not be null
        // if (from == null || (to == null)) {
        // throw new Exception("Not all required information is available");
        // }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // Use the true flag to indicate you need a multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            LOG.info("Sending email...");
            // Set the to address. There can be multiple to addresses
            String[] addrTo = parseString(to, domainName);
            String[] addrCc = parseString(cc, domainName);
            String[] addrBcc = parseString(bcc, domainName);
            if (addrTo != null && addrTo.length > 0) {
                helper.setTo(addrTo);
            }
            if (addrCc != null && addrCc.length > 0) {
                helper.setCc(addrCc);
            }
            if (addrBcc != null && addrBcc.length > 0) {
                helper.setBcc(addrBcc);
            }

            // Set the subject
            helper.setSubject(subject);

            // Set text html
            helper.setText(text, true);

            // Send message
            mailSender.send(message);

            LOG.info("Email Sent!");

        } catch (AddressException e) {
            LOG.error("Address Exception : " + e);
            status = "FAIL";
            throw new Exception(e.getMessage());
        } catch (MessagingException e) {
            LOG.error("MessagingException : " + e);
            status = "FAIL";
            throw new Exception(e.toString());
        } catch (Exception e) {
            LOG.error("Unknown Exception : " + e);
            status = "FAIL";
        }

        return status;
    }

    /**
     * This method send email by template
     * 
     * @param fromEmail
     *            Default configuration application
     * @param toEmail
     * @param addrCc
     * @param addrBcc
     * @param strSubjectEmail
     * @param templateFile
     * @param model
     * @throws MessagingException
     */
    public void sendHmtlTemplateEmail(final String fromEmail, final String toEmail, final String addrCc, final String addrBcc,
            final String strSubjectEmail, final String templateFile, final Map model) throws MessagingException {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(final MimeMessage mimeMessage) throws MessagingException {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                if (fromEmail != null && !"".equals(fromEmail)) {
                    message.setFrom(fromEmail);
                }
                if (addrCc != null && !"".equals(addrCc)) {
                    message.setCc(addrCc);
                }
                if (addrBcc != null && !"".equals(addrBcc)) {
                    message.setBcc(addrBcc);
                }
                message.setTo(toEmail);
                message.setSubject(strSubjectEmail);
                String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateFile, model);
                // LOG.info(text);
                message.setText(text, true);

            }
        };
        this.mailSender.send(preparator);
    }

    /*
     * parse string and convert the addresses to RFC822 Internet Address format.
     * 
     * @param str address string to be parsed @return array of Addresses
     */
    private String[] parseString(final String str, final String domainName) throws AddressException {
        if (str == null || str.equals("")) {
            return null;
        }
        StringTokenizer strToken = new StringTokenizer(str, SEPARATORS);
        String[] strTo = new String[strToken.countTokens()];
        int i = 0;
        while (strToken.hasMoreTokens()) {
            String mailId = strToken.nextToken();
            if (mailId.indexOf('@') < 0) { // if domain not specified add
                // cisco.com
                mailId += "@" + domainName;
            }
            strTo[i] = mailId;
            i++;
        }
        return strTo;

    }

    /*
     * Replace logic
     */
    private static void replace(final StringBuffer buffer, final Map<String, String> map) {
        Iterator itr = map.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) itr.next();
            String oldValue = entry.getKey();
            String newValue = entry.getValue();
            if (buffer != null && oldValue != null && newValue != null) {
                int startIndex = buffer.indexOf(oldValue);
                while (startIndex > -1) {
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
    private StringBuffer getTemplate(final String resourcePath) {
        StringBuffer buffer = new StringBuffer();
        if (templates.containsKey(resourcePath)) {
            StringBuffer template = templates.get(resourcePath);
            buffer.append(template.toString());
        } else {
            synchronized (NotifyByEmail.class) {
                InputStream is = null;
                BufferedInputStream bis = null;
                try {
                    StringBuffer template = new StringBuffer();
                    is = NotifyByEmail.class.getClassLoader().getResourceAsStream(resourcePath);
                    bis = new BufferedInputStream(is);
                    byte[] bytes = new byte[bis.available()];
                    bis.read(bytes);
                    template.append(new String(bytes));
                    templates.put(resourcePath, template);
                    buffer.append(template.toString());
                } catch (Exception ex) {
                    LOG.error("Error Loading Mail template: " + resourcePath, ex);
                } finally {
                    try {
                        if (bis != null) {
                            bis.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                    } catch (Exception e) {
                        LOG.error(" " + e);
                    }
                }
            }
        }
        return buffer;
    }
}
