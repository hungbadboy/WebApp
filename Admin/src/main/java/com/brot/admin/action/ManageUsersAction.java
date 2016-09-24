/**
 * 
 */
package com.brot.admin.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.brot.admin.helper.ManageUsersHelper;
import com.brot.admin.model.ManageUserModel;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author viddasar
 *
 */
public class ManageUsersAction extends BrotCommonAction  implements Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Collection<ManageUserModel> manageUserDetails = new ArrayList<ManageUserModel>();
	private Collection<ManageUserModel> brotUserDetails = new ArrayList<ManageUserModel>();
	
	private String userJson;
	
	
	public Collection<ManageUserModel> getBrotUserDetails() {
		return brotUserDetails;
	}

	public void setBrotUserDetails(Collection<ManageUserModel> brotUserDetails) {
		this.brotUserDetails = brotUserDetails;
	}

	ManageUsersHelper manageUsersHelper = new ManageUsersHelper();
	Collection<String[]> userTypeList = new ArrayList<String[]>();
	private String selectedUserType = new String();
	
	public String listUsersData() {
		ManageUsersHelper manageUsersHelper = new ManageUsersHelper();
		
		return ActionSupport.SUCCESS;
	}

	public String fetchUserTypes(){
	
		
		userTypeList = manageUsersHelper.fetchUserTypes();
		
		return ActionSupport.SUCCESS;
	}
	
	public String updateMentorApprovedFlag(){
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			brotUserDetails = mapper.readValue(userJson, new TypeReference<List<ManageUserModel>>(){});
			
			manageUsersHelper.updateMentorApprovedFlag(brotUserDetails);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ActionSupport.SUCCESS;
	}
	
	
	public String getUserDetails(){
		try {
			
			brotUserDetails = manageUsersHelper.getUsersInfo(selectedUserType.trim().length() == 0?"S":selectedUserType);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ActionSupport.SUCCESS;
	}
	
	
	/**
	 * @return the manageUserDetails
	 */
	public Collection<ManageUserModel> getManageUserDetails() {
		return manageUserDetails;
	}

	/**
	 * @param manageUserDetails the manageUserDetails to set
	 */
	public void setManageUserDetails(Collection<ManageUserModel> manageUserDetails) {
		this.manageUserDetails = manageUserDetails;
	}

	/**
	 * @return the userTypeList
	 */
	public Collection<String[]> getUserTypeList() {
		return userTypeList;
	}

	/**
	 * @param userTypeList the userTypeList to set
	 */
	public void setUserTypeList(Collection<String[]> userTypeList) {
		this.userTypeList = userTypeList;
	}

	/**
	 * @return the selectedUserType
	 */
	public String getSelectedUserType() {
		return selectedUserType;
	}

	/**
	 * @param selectedUserType the selectedUserType to set
	 */
	public void setSelectedUserType(String selectedUserType) {
		this.selectedUserType = selectedUserType;
	}

	@Override
	public void prepare() throws Exception {
		super.prepare();
		
	}

	public String getUserJson() {
		return userJson;
	}

	public void setUserJson(String userJson) {
		this.userJson = userJson;
	}


}
