/**
 * 
 */
package com.brot.admin.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.brot.admin.client.MobileServiceManagerClient;
import com.brot.admin.exception.ServiceConnectionException;
import com.brot.admin.resource.AppConfigManager;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author viddasar
 *
 */
public class UserRegistrationAction extends ActionSupport {

	private String firstName;
	private String lastName;
	private String email;
	private String userType;
	private String active;
	private String password;
	
	@SuppressWarnings("unchecked")
	public String createUserAccount() {
		String response = "";
		try {
			Map queryParams = new HashMap();

			queryParams.put("firstname", firstName);
			queryParams.put("lastname", lastName);
			queryParams.put("username", email);
			queryParams.put("password", password);

			String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
			System.out.println("inside doFilter.....------------------------" + endPointUrl);

			endPointUrl = endPointUrl + "user/adminRegisterUser";
			System.out.println("queryParams===" + queryParams);
			MobileServiceManagerClient _instance = null;
			_instance = MobileServiceManagerClient.getInstance();

			try {
				response = _instance.write(queryParams, endPointUrl);
				System.out.println("response====================" + response);

			} catch (ServiceConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("=========execute====================");
		String status = "SUCCESS";

		Map<String, String> map = new HashMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();

		try {

			map = mapper.readValue(response, new TypeReference<HashMap<String, String>>() {
			});

			System.out.println(map);

		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		String resStatus = map.get("status");
		HttpServletRequest request = ServletActionContext.getRequest();
		if ("true".equals(resStatus)) {
			status = "SUCCESS";
			request.setAttribute("SUCCESS", map.get("request_data_result"));
		} else {
			status = "FAILURE";

			request.setAttribute("ERROR", map.get("request_data_result"));
		}

		return status;

	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(final String userType) {
		this.userType = userType;
	}

	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(final String active) {
		this.active = active;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(final String password) {
		this.password = password;
	}
	
	/* public void validate()
	   {
	      if (null == firstName || firstName.trim().equals(""))
	      {
	         addFieldError("firstName","The name is required");
	      }
	    
	   }*/
}
