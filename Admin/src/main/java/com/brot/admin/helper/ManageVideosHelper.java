/**
 * 
 */
package com.brot.admin.helper;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;

import com.brot.admin.client.MobileServiceManagerClient;
import com.brot.admin.model.ManageSubjectModel;
import com.brot.admin.model.ManageVideoModel;
import com.brot.admin.model.SubCategoryModel;
import com.brot.admin.resource.AppConfigManager;

/**
 * @author viddasar
 *
 */
public class ManageVideosHelper {	

	
	
	/*public List<ManageVideoModel> getAllVideos() {
		
		List<ManageVideoModel> manageVideoList = new ArrayList<ManageVideoModel>();
		
		 System.out.println(" getAllVideos --------------");
		 ManageVideoModel manageVideoModel = new ManageVideoModel();
		 manageVideoModel.setVideo_name("Sitepoint");
		 manageVideoModel.setVideo_link("http://sitepoint.com");
		 manageVideoModel.setDescription("Site Point");
		 manageVideoModel.setActive("Active");
		 
		 manageVideoList.add(manageVideoModel);
		 
		 ManageVideoModel manageVideoModel1 = new ManageVideoModel();
		 manageVideoModel1.setVideo_name("Flippa");
		 manageVideoModel1.setVideo_link("http://flippa.com");
		 manageVideoModel1.setDescription("Marketplace");
		 manageVideoModel1.setActive("Active");
		 
		 manageVideoList.add(manageVideoModel1);
		 
		return manageVideoList;
	}*/
	
	public List<String[]> fetchSubjects() throws HttpException, IOException{
		Map queryParams = new HashMap();
		List<String[]> subjectsList = new ArrayList<String[]>();
		String subjectListJson = null;
		JSONArray subjectsJsonArray = new JSONArray();
		JSONObject jsonSubjectObject = null;
		
		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		List response = MobileServiceManagerClient.getInstance().dataService(null, "SUBJECTS_LIST_DATA_READ", queryParams, endPointUrl);
		if(null != response && !response.isEmpty()){
			for (Object object : response) {
				subjectListJson = (String)object;
			}
		}
		JSONObject json1 = new JSONObject(subjectListJson);
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
				/*	subjectsList.add(new String[]{jsonObject.get("name").toString()
							.toLowerCase(), jsonRowArray.get(j).toString()});*/
				}

			}
			subjectsJsonArray.put(jsonSubjectObject);
		}
		for(int j = 0; j < subjectsJsonArray.length(); j++){
			JSONObject jsonObject = subjectsJsonArray.getJSONObject(j);
			subjectsList.add(new String[]{subjectsJsonArray.getJSONObject(j).get("subject_id").toString(),subjectsJsonArray.getJSONObject(j).get("subject_name").toString()});
		}
		
		return subjectsList;
	}
	
	public List<ManageVideoModel> getVideoDetails(Integer subjectId) throws IOException {
		List<ManageVideoModel> manageVideoList = new ArrayList<ManageVideoModel>();
		Map queryParams = new HashMap();
		String outputJson = null;
		JSONArray videosJsonArray = new JSONArray();
		JSONObject jsonVideoObject = null;
		if(null != subjectId){
			String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
			queryParams.put("subject_id", subjectId.intValue());
			List response = MobileServiceManagerClient.getInstance().dataService(null, "VIDEO_SUBJECT_MAPPING_DATA_READ", queryParams, endPointUrl);
			
			if(null != response){
				for (Object object : response) {
					outputJson = (String)object;
				}
			
			
			JSONObject json1 = new JSONObject(outputJson);
			
			JSONArray jsonRowsArray= json1.getJSONArray("RS.ROWS");
			
			JSONArray jsonColumnsArray = json1.getJSONArray("RS.COLUMNS");
			
			for (int i = 0; i < jsonRowsArray.length(); i++) {

				Object jsonRow = jsonRowsArray.get(i);
				jsonVideoObject = new JSONObject();
				if (jsonRow instanceof JSONArray) {
					JSONArray jsonRowArray = (JSONArray) jsonRow;
					for (int j = 0; j < jsonRowArray.length(); j++) {
						
						JSONObject jsonObject = jsonColumnsArray.getJSONObject(j);
						
						jsonVideoObject.put(jsonObject.get("name").toString()
								.toLowerCase(), jsonRowArray.get(j));
					
					}

				}
				videosJsonArray.put(jsonVideoObject);
			}
			ObjectMapper mapper = new ObjectMapper();
			manageVideoList = mapper.readValue(videosJsonArray.toString(), new TypeReference<List<ManageVideoModel>>(){});
			}
		}
		return manageVideoList;
	}
	
	public void createVideo(List<ManageVideoModel> videoDetails) throws HttpException, IOException {
		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		Map queryParams = new HashMap();
		String active = null;
		if(null != videoDetails && !videoDetails.isEmpty())
		for (ManageVideoModel manageVideoModel : videoDetails) {
			queryParams.put("subject_id", manageVideoModel.getSubject_id());
			queryParams.put("subject_category_name", manageVideoModel.getSubject_category_name());
			queryParams.put("description", manageVideoModel.getDescription());
			if(null != manageVideoModel.getActive()){
				if(manageVideoModel.getActive().equalsIgnoreCase("1")){
					active = "Y";
				}else{
					active = "N";
				}
			}
			queryParams.put("active", active);
			queryParams.put("createdBy", 1);
			queryParams.put("lastUpdatedBy", 1);
		
			MobileServiceManagerClient.getInstance().dataService(null, "VIDEO_SUBJECT_MAPPING_DATA_INSERT", queryParams, endPointUrl);
			 
		}
	}
	
	public void updateVideoDetails(String updateVideoData) throws JsonParseException, JsonMappingException, IOException{
		Map queryParams = new HashMap();
		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		ObjectMapper mapper = new ObjectMapper();
		ManageVideoModel manageVideoModel = mapper.readValue(updateVideoData.toString(), ManageVideoModel.class);
		
		queryParams.put("subject_category_id", manageVideoModel.getSubject_category_id());
		queryParams.put("subject_category_name", manageVideoModel.getSubject_category_name());
		//queryParams.put("video_link", manageVideoModel.getVideo_link());
		queryParams.put("description", manageVideoModel.getDescription());
		queryParams.put("active", manageVideoModel.getActive());
		List response = MobileServiceManagerClient.getInstance().dataService(null, "VIDEO_SUBJECT_MAPPING_DATA_UPDATE", queryParams, endPointUrl);
	}
	
	public void deleteVideo(Integer subject_category_id) throws HttpException, IOException{
		Map queryParams = new HashMap();
		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		queryParams.put("subject_category_id", subject_category_id.intValue());
		List response = MobileServiceManagerClient.getInstance().dataService(null, "VIDEO_SUBJECT_MAPPING_DATA_DELETE", queryParams, endPointUrl);
	}

	
	public List<SubCategoryModel> getSubCategoryData(Integer subject_id,
			Integer category_id) throws IOException {
		List<SubCategoryModel> subCategoriesList = new ArrayList<SubCategoryModel>();
		Map queryParams = new HashMap();
		String outputJson = null;
		JSONArray subCategoriesJsonArray = new JSONArray();
		JSONObject jsonsubCategoriesObject = null;
		if(null != subject_id && null != category_id){
			String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
			queryParams.put("subjectid", subject_id.intValue());
			queryParams.put("categoryid", category_id.intValue());
			List response = MobileServiceManagerClient.getInstance().dataService(null, "VIDEO_SUBCATAGERY_READ", queryParams, endPointUrl);
			
			if(null != response){
				for (Object object : response) {
					outputJson = (String)object;
				}
			
			
			JSONObject json1 = new JSONObject(outputJson);
			
			JSONArray jsonRowsArray= json1.getJSONArray("RS.ROWS");
			
			JSONArray jsonColumnsArray = json1.getJSONArray("RS.COLUMNS");
			
			for (int i = 0; i < jsonRowsArray.length(); i++) {

				Object jsonRow = jsonRowsArray.get(i);
				jsonsubCategoriesObject = new JSONObject();
				if (jsonRow instanceof JSONArray) {
					JSONArray jsonRowArray = (JSONArray) jsonRow;
					for (int j = 0; j < jsonRowArray.length(); j++) {
						
						JSONObject jsonObject = jsonColumnsArray.getJSONObject(j);
						
						jsonsubCategoriesObject.put(jsonObject.get("name").toString()
								.toLowerCase(), jsonRowArray.get(j));
					
					}

				}
				subCategoriesJsonArray.put(jsonsubCategoriesObject);
			}
			ObjectMapper mapper = new ObjectMapper();
			subCategoriesList = mapper.readValue(subCategoriesJsonArray.toString(), new TypeReference<List<SubCategoryModel>>(){});
			}
		}
		return subCategoriesList;
	}

	public void saveSubCategory(SubCategoryModel subCategoryModel) throws HttpException, IOException {
		Map queryParams = new HashMap();
		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		if(null != subCategoryModel )
				queryParams.put("subject_category_id", subCategoryModel.getSubject_category_id());
				queryParams.put("subject_sub_category_name", subCategoryModel.getSubject_sub_category_name());
				queryParams.put("description", subCategoryModel.getDescription());
				queryParams.put("popular_video", subCategoryModel.getPopular_video());
				queryParams.put("video_link", subCategoryModel.getVideo_link());
				queryParams.put("image", null);
			
				MobileServiceManagerClient.getInstance().dataService(null, "SUBJECT_SUB_CATEGORY_INSERT", queryParams, endPointUrl);
		
	}
	public void updateSubCategory(SubCategoryModel subCategoryModel) throws HttpException, IOException {
		Map queryParams = new HashMap();
		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		if(null != subCategoryModel )
				queryParams.put("subject_sub_category_id", subCategoryModel.getSubject_sub_category_id());
				queryParams.put("subject_sub_category_name", subCategoryModel.getSubject_sub_category_name());
				queryParams.put("description", subCategoryModel.getDescription());
				queryParams.put("popular_video", subCategoryModel.getPopular_video());
				queryParams.put("video_link", subCategoryModel.getVideo_link());
				queryParams.put("image", null);
			
				MobileServiceManagerClient.getInstance().dataService(null, "SUBJECT_SUB_CATEGORY_UPDATE", queryParams, endPointUrl);
		
	}

	/**
	 * 
	 */
	public void deleteSubCategory(Integer subject_sub_category_id) throws HttpException, IOException {
		Map queryParams = new HashMap();
		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		queryParams.put("subject_sub_category_id", subject_sub_category_id.intValue());
		List response = MobileServiceManagerClient.getInstance().dataService(null, "SUBJECT_SUB_CATEGORY_DELETE", queryParams, endPointUrl);
		
	}
	
	
}
