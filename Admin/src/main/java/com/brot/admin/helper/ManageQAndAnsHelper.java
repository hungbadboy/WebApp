/**
 * 
 */
package com.brot.admin.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;

import com.brot.admin.client.MobileServiceManagerClient;
import com.brot.admin.model.ManageQAndAnsModel;
import com.brot.admin.resource.AppConfigManager;

/**
 * @author viddasar
 * 
 */
public class ManageQAndAnsHelper {

	public List<ManageQAndAnsModel> getQAndAnsDetails()
			throws HttpException, IOException {

		List<ManageQAndAnsModel> manageUsersList = new ArrayList<ManageQAndAnsModel>();
		Map queryParams = new HashMap();
		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		String outputJson = null;
		JSONArray subjectsJsonArray = new JSONArray();
		JSONObject jsonSubjectObject = null;

		List response = MobileServiceManagerClient.getInstance().dataService(
				null, "MANAGEQANDANS_DATA_READ", queryParams, endPointUrl);
		System.out.println("response === " + response);
		if (null != response) {
			for (Object object : response) {
				outputJson = (String) object;
			}

		}

		JSONObject json1 = new JSONObject(outputJson);
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
				new TypeReference<List<ManageQAndAnsModel>>() {
				});

		return manageUsersList;

	}

	public Collection<ManageQAndAnsModel> updateManageQAndAnsAction(
			Collection<ManageQAndAnsModel> brotQAndAnsDetails)
			throws HttpException, IOException {

		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		Map queryParams = new HashMap();

		if (null != brotQAndAnsDetails && !brotQAndAnsDetails.isEmpty()
				&& brotQAndAnsDetails.size() > 0) {
			String queryName = "";
			String action = "";
			for (ManageQAndAnsModel manageQAndAnsModel : brotQAndAnsDetails) {
				action = manageQAndAnsModel.getAction();
				queryParams.put("user_id", manageQAndAnsModel.getUser_id());
				
				if("overRule".equals(action)){
					// TBD
					//queryParams.put("user_id", manageQAndAnsModel.getUser_id());
					//queryName = "MANAGE_QANDAND_OVERRULE_UPDATE";
				} else if("remove".equals(action) || "hide".equals(action) ){
					queryParams.put("user_id", manageQAndAnsModel.getUser_id());
					queryParams.put("question_id", manageQAndAnsModel.getQuestion_id());
					queryParams.put("active", ((manageQAndAnsModel.getActive().trim()).equals("Y")?"N":"Y"));
					queryName = "MANAGE_QANDAND_REMOVE_HIDE_UPDATE";
				} else if("warning".equals(action)){
					queryParams.put("user_id", manageQAndAnsModel.getUser_id());
					queryParams.put("question_id", manageQAndAnsModel.getQuestion_id());
					queryParams.put("admin_warning", manageQAndAnsModel.getAdmin_warning());
					queryName = "MANAGE_QANDAND_WARNING_UPDATE";
				} else if("disableUser".equals(action)){
					queryParams.put("user_id", manageQAndAnsModel.getUser_id());
					queryParams.put("question_id", manageQAndAnsModel.getQuestion_id());
					queryParams.put("active", ((manageQAndAnsModel.getUser_status()).equals("Y")?"N":"Y"));
					queryName = "MANAGE_QANDAND_DISABLE_USER_UPDATE";
				} 
				
				MobileServiceManagerClient.getInstance().dataService(null,
						queryName, queryParams, endPointUrl);
				MobileServiceManagerClient.getInstance().dataService(null, "SUBJECT_DATA_INSERT", queryParams, endPointUrl);
			}
		}

		return null;
	}

}
