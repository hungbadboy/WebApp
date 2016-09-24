/**
 * 
 */
package com.brot.admin.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;

import com.brot.admin.client.MobileServiceManagerClient;
import com.brot.admin.model.ManageUserModel;
import com.brot.admin.resource.AppConfigManager;

/**
 * @author viddasar
 * 
 */
public class ManageUsersHelper {

	Collection<String[]> userTypeList = new ArrayList<String[]>();

	public List<ManageUserModel> getUsersInfo(String userType)
			throws HttpException, IOException {

		List<ManageUserModel> manageUsersList = new ArrayList<ManageUserModel>();
		Map queryParams = new HashMap();
		queryParams.put("usertype", userType);
		System.out.println("usertype " + userType);
		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		String outputJson = null;
		JSONArray subjectsJsonArray = new JSONArray();
		JSONObject jsonSubjectObject = null;

		List response = MobileServiceManagerClient.getInstance().dataService(
				null, "USERTYPE_DATA_READ", queryParams, endPointUrl);
		System.out.println("response === " + response);
		if (null != response) {
			for (Object object : response) {
				outputJson = (String) object;
			}

		}

		JSONObject json1 = new JSONObject(outputJson);
		System.out.println("json1 === " + json1.toString());
		JSONArray jsonRowsArray = json1.getJSONArray("RS.ROWS");

		JSONArray jsonColumnsArray = json1.getJSONArray("RS.COLUMNS");

		for (int i = 0; i < jsonRowsArray.length(); i++) {

			Object jsonRow = jsonRowsArray.get(i);
			jsonSubjectObject = new JSONObject();
			if (jsonRow instanceof JSONArray) {
				JSONArray jsonRowArray = (JSONArray) jsonRow;
				for (int j = 0; j < jsonRowArray.length(); j++) {

					JSONObject jsonObject = jsonColumnsArray.getJSONObject(j);

					jsonSubjectObject.put(jsonObject.get("name").toString()
							.toLowerCase(), jsonRowArray.get(j));

				}

			}
			subjectsJsonArray.put(jsonSubjectObject);
		}

		ObjectMapper mapper = new ObjectMapper();
		manageUsersList = mapper.readValue(subjectsJsonArray.toString(),
				new TypeReference<List<ManageUserModel>>() {
				});

		return manageUsersList;

	}

	public List<String[]> fetchUserTypes() {
		/*
		 * List<String[]> userTypeList = new ArrayList<String[]>();
		 * userTypeList.add(new String[]{"S","Student"}); userTypeList.add(new
		 * String[]{"M","Mentor"});
		 */
		List<String[]> userTypeList = new ArrayList<String[]>();
		JSONArray array = new JSONArray();
		array.put(new JSONObject().putOpt("S", "Student"));
		array.put(new JSONObject().putOpt("M", "Mentor"));

		System.out.println("JSON Array : " + array.toString());

		String val = null;
		String key = null;

		for (int j = 0; j < array.length(); j++) {
			JSONObject jsonObject = array.getJSONObject(j);
			Iterator<String> keys = jsonObject.keys();

			while (keys.hasNext()) {
				key = keys.next();
				try {
					val = (String) jsonObject.get(key);
					userTypeList.add(new String[] { key, val });

				} catch (Exception e) {
				}

			}

			// subjectsList.add(new
			// String[]{array.getJSONObject(j).get("S").toString(),array.getJSONObject(j).get("subject_name").toString()});
		}

		return userTypeList;
	}

	public Collection<ManageUserModel> updateMentorApprovedFlag(
			Collection<ManageUserModel> brotUserDetails)
			throws HttpException, IOException {

		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		Map queryParams = new HashMap();

		if (null != brotUserDetails && !brotUserDetails.isEmpty()
				&& brotUserDetails.size() > 0) {
			for (ManageUserModel manageUserModel : brotUserDetails) {

				queryParams.put("user_id", manageUserModel.getUser_id());
				if (manageUserModel.getMentor_approved_flag().equals("true"))
					queryParams.put("mentor_approval_flag", "Y");
				else
					queryParams.put("mentor_approval_flag", "N");

				MobileServiceManagerClient.getInstance().dataService(null,
						"USER_APPROVED_DATA_UPDATE", queryParams, endPointUrl);
			}
		}

		return null;
	}

}
