/**
 * 
 */
package com.brot.admin.action;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.apache.commons.httpclient.HttpException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.brot.admin.helper.ManageVideosHelper;
import com.brot.admin.model.ManageVideoModel;
import com.brot.admin.model.SubCategoryModel;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author viddasar
 * 
 */
public class ManageVideosAction extends BrotCommonAction implements
		Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Collection<ManageVideoModel> manageVideosDetails = new ArrayList<ManageVideoModel>();
	Collection<String[]> subjectList = new ArrayList<String[]>();
	private Collection<ManageVideoModel> subjectVideosDetails = new ArrayList<ManageVideoModel>();
	private Collection<SubCategoryModel> subCategoriesDetails = new ArrayList<SubCategoryModel>();
	private String selectedSubject = new String();
	ManageVideosHelper manageVideosHelper = new ManageVideosHelper();
	private String videoJson;
	private String updateJson;
	private Integer videoId;
	private String saveSubjectSubCategoryJson;
	private Integer subject_id;
	private Integer category_id;
	
	private Integer subCategoryId;
	private String subCategoryName;
	private String description;
	private Integer populatVideoRating;
	private String mentorName;
	private String videoLink;
	private Blob image;
	private Integer subject_sub_category_id;

	/*public String listVideoData() {

		manageVideosDetails = manageVideosHelper.getAllVideos();

		return ActionSupport.SUCCESS;
	}*/

	public String fetchSubjects() {

		try {
			subjectList = manageVideosHelper.fetchSubjects();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String getVideoDetails() {
		try {
			if(null != selectedSubject && !"".equalsIgnoreCase(selectedSubject)){
				subjectVideosDetails = manageVideosHelper.getVideoDetails(new Integer(selectedSubject));	
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ActionSupport.SUCCESS;
	}

	public String createVideo() {
		List<ManageVideoModel> newVideoDetails = new ArrayList<ManageVideoModel>();

		try {
			ObjectMapper mapper = new ObjectMapper();
			newVideoDetails = mapper.readValue(videoJson,
					new TypeReference<List<ManageVideoModel>>() {
					});
			manageVideosHelper.createVideo(newVideoDetails);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ActionSupport.SUCCESS;
	}

	public String updateVideoDetails(){
		if(null != updateJson && !"".equalsIgnoreCase(updateJson)){
			try {
				manageVideosHelper.updateVideoDetails(updateJson);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		return ActionSupport.SUCCESS;
	}
	
	public String deleteVideo(){
		if(null != videoId ){
			try {
				manageVideosHelper.deleteVideo(videoId);
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return ActionSupport.SUCCESS;
	}
	
	public String getSubCategoryData() {
		try {
			if(null != subject_id && null != category_id){
				subCategoriesDetails = manageVideosHelper.getSubCategoryData(subject_id, category_id);	
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ActionSupport.SUCCESS;
	}
		
	public String saveSubCategory() {
		SubCategoryModel subCategoryModel = new SubCategoryModel();

		try {
			ObjectMapper mapper = new ObjectMapper();
			
			subCategoryModel = mapper.readValue(saveSubjectSubCategoryJson,SubCategoryModel.class);
			manageVideosHelper.saveSubCategory(subCategoryModel);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ActionSupport.SUCCESS;
	}
	public String updateSubCategory() {
		SubCategoryModel subCategoryModel = new SubCategoryModel();

		try {
			ObjectMapper mapper = new ObjectMapper();
			subCategoryModel = mapper.readValue(updateJson,SubCategoryModel.class);
			manageVideosHelper.updateSubCategory(subCategoryModel);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ActionSupport.SUCCESS;
	}
	public String deleteSubCategory() {
		
		
		if(null != subject_sub_category_id ){
			try {
				manageVideosHelper.deleteSubCategory(subject_sub_category_id);
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return ActionSupport.SUCCESS;
	}
	@Override
	public void prepare() throws Exception {
		super.prepare();

	}

	/**
	 * @return the manageVideosDetails
	 */
	public Collection<ManageVideoModel> getManageVideosDetails() {
		return manageVideosDetails;
	}

	/**
	 * @param manageVideosDetails
	 *            the manageVideosDetails to set
	 */
	public void setManageVideosDetails(
			Collection<ManageVideoModel> manageVideosDetails) {
		this.manageVideosDetails = manageVideosDetails;
	}

	/**
	 * @return the subjectList
	 */
	public Collection<String[]> getSubjectList() {
		return subjectList;
	}

	/**
	 * @param subjectList
	 *            the subjectList to set
	 */
	public void setSubjectList(Collection<String[]> subjectList) {
		this.subjectList = subjectList;
	}

	/**
	 * @return the selectedSubject
	 */
	public String getSelectedSubject() {
		return selectedSubject;
	}

	/**
	 * @param selectedSubject
	 *            the selectedSubject to set
	 */
	public void setSelectedSubject(String selectedSubject) {
		this.selectedSubject = selectedSubject;
	}

	/**
	 * @return the subjectVideosDetails
	 */
	public Collection<ManageVideoModel> getSubjectVideosDetails() {
		return subjectVideosDetails;
	}

	/**
	 * @param subjectVideosDetails
	 *            the subjectVideosDetails to set
	 */
	public void setSubjectVideosDetails(
			Collection<ManageVideoModel> subjectVideosDetails) {
		this.subjectVideosDetails = subjectVideosDetails;
	}

	/**
	 * @return the videoJson
	 */
	public String getVideoJson() {
		return videoJson;
	}

	/**
	 * @param videoJson
	 *            the videoJson to set
	 */
	public void setVideoJson(String videoJson) {
		this.videoJson = videoJson;
	}

	/**
	 * @return the updateJson
	 */
	public String getUpdateJson() {
		return updateJson;
	}

	/**
	 * @param updateJson the updateJson to set
	 */
	public void setUpdateJson(String updateJson) {
		this.updateJson = updateJson;
	}

	/**
	 * @return the videoId
	 */
	public Integer getVideoId() {
		return videoId;
	}

	/**
	 * @param videoId the videoId to set
	 */
	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}

	public Integer getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(Integer subject_id) {
		this.subject_id = subject_id;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public Collection<SubCategoryModel> getSubCategoriesDetails() {
		return subCategoriesDetails;
	}

	public void setSubCategoriesDetails(
			Collection<SubCategoryModel> subCategoriesDetails) {
		this.subCategoriesDetails = subCategoriesDetails;
	}

	/**
	 * @return the subCategoryId
	 */
	public Integer getSubCategoryId() {
		return subCategoryId;
	}

	/**
	 * @param subCategoryId the subCategoryId to set
	 */
	public void setSubCategoryId(Integer subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	/**
	 * @return the subCategoryName
	 */
	public String getSubCategoryName() {
		return subCategoryName;
	}

	/**
	 * @param subCategoryName the subCategoryName to set
	 */
	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the populatVideoRating
	 */
	public Integer getPopulatVideoRating() {
		return populatVideoRating;
	}

	/**
	 * @param populatVideoRating the populatVideoRating to set
	 */
	public void setPopulatVideoRating(Integer populatVideoRating) {
		this.populatVideoRating = populatVideoRating;
	}

	/**
	 * @return the mentorName
	 */
	public String getMentorName() {
		return mentorName;
	}

	/**
	 * @param mentorName the mentorName to set
	 */
	public void setMentorName(String mentorName) {
		this.mentorName = mentorName;
	}

	/**
	 * @return the videoLink
	 */
	public String getVideoLink() {
		return videoLink;
	}

	/**
	 * @param videoLink the videoLink to set
	 */
	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	/**
	 * @return the image
	 */
	public Blob getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Blob image) {
		this.image = image;
	}

	/**
	 * @return the subject_sub_category_id
	 */
	public Integer getSubject_sub_category_id() {
		return subject_sub_category_id;
	}

	/**
	 * @param subject_sub_category_id the subject_sub_category_id to set
	 */
	public void setSubject_sub_category_id(Integer subject_sub_category_id) {
		this.subject_sub_category_id = subject_sub_category_id;
	}

	/**
	 * @return the saveSubjectSubCategoryJson
	 */
	public String getSaveSubjectSubCategoryJson() {
		return saveSubjectSubCategoryJson;
	}

	/**
	 * @param saveSubjectSubCategoryJson the saveSubjectSubCategoryJson to set
	 */
	public void setSaveSubjectSubCategoryJson(String saveSubjectSubCategoryJson) {
		this.saveSubjectSubCategoryJson = saveSubjectSubCategoryJson;
	}

}
