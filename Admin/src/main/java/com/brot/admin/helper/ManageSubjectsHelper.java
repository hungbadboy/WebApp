/**
 * 
 */
package com.brot.admin.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brot.admin.client.MobileServiceManagerClient;
import com.brot.admin.model.ManageSubjectModel;
import com.brot.admin.resource.AppConfigManager;

/**
 * @author viddasar
 *
 */
public class ManageSubjectsHelper {


	
	public List<ManageSubjectModel> getAllSubjects() throws HttpException, IOException, JSONException {
		Map queryParams = new HashMap();
		List<ManageSubjectModel> manageSubjectList = new ArrayList<ManageSubjectModel>();
		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		String outputJson = null;
		JSONArray subjectsJsonArray = new JSONArray();
		JSONObject jsonSubjectObject = null;
		List response = MobileServiceManagerClient.getInstance().dataService(null, "SUBJECTS_DATA_READ", queryParams, endPointUrl);
		System.out.println("response === " + response);
		if(null != response){
			for (Object object : response) {
				outputJson = (String)object;
			}
			
		}
		
		JSONObject json1 = new JSONObject(outputJson);
		System.out.println("json1 === " + json1.toString());
		JSONArray jsonRowsArray= json1.getJSONArray("RS.ROWS");
		
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
		manageSubjectList = mapper.readValue(subjectsJsonArray.toString(), new TypeReference<List<ManageSubjectModel>>(){});
		
		return manageSubjectList;
	}
	
	public void createSubjects(final List<ManageSubjectModel> subjectDetails) throws HttpException, IOException {
		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		Map queryParams = new HashMap();
		String active = null;
		if(null != subjectDetails && !subjectDetails.isEmpty() && subjectDetails.size() > 0){
			for (ManageSubjectModel manageSubjectModel : subjectDetails) {
				
				queryParams.put("subjectname", manageSubjectModel.getSubject_name());
				queryParams.put("description", manageSubjectModel.getDescription());
				queryParams.put("creationdate", manageSubjectModel.getCreation_date());
				if(null != manageSubjectModel.getActive()){
					if(manageSubjectModel.getActive().equalsIgnoreCase("1")){
						active = "Y";
					}else{
						active = "N";
					}
				}
				queryParams.put("active", active);
				queryParams.put("createdBy", 1);
				queryParams.put("lastUpdatedBy", 1);
			
				MobileServiceManagerClient.getInstance().dataService(null, "SUBJECT_DATA_INSERT", queryParams, endPointUrl);
			}
		}
		
		
	}
	
	/**
	 * @param subjectDetails
	 * @throws HttpException
	 * @throws IOException
	 */
	public void deleteSubjects(final List<ManageSubjectModel> subjectDetails) throws HttpException, IOException {
		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		Map queryParams = new HashMap();
		String active = null;
		if(null != subjectDetails && !subjectDetails.isEmpty() && subjectDetails.size() > 0){
			for (ManageSubjectModel manageSubjectModel : subjectDetails) {
				
				queryParams.put("subject_id", manageSubjectModel.getSubject_id());
				MobileServiceManagerClient.getInstance().dataService(null, "SUBJECT_DATA_DELETE", queryParams, endPointUrl);
			}
		}
		
		
	}

}
