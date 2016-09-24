/**
 *
 */
package com.brot.admin.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;

import com.brot.admin.helper.ManageSubjectsHelper;
import com.brot.admin.model.ManageSubjectModel;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author viddasar
 *
 */
public class ManageSubjectsAction extends HomeAction implements Preparable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Collection<ManageSubjectModel> manageSubjectsDetails = new ArrayList<ManageSubjectModel>();
	private String subjectJson;
	public String listSubjectsData() {

		try {
			ManageSubjectsHelper manageSubjectsHelper = new ManageSubjectsHelper();
			this.manageSubjectsDetails = manageSubjectsHelper.getAllSubjects();
		} catch (HttpException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ActionSupport.SUCCESS;
	}

	public String createSubject() {
		List<ManageSubjectModel> newSubjectDetails = new ArrayList<ManageSubjectModel>();
		ManageSubjectsHelper manageSubjectsHelper = new ManageSubjectsHelper();
		try {
			ObjectMapper mapper = new ObjectMapper();
			newSubjectDetails = mapper.readValue(subjectJson, new TypeReference<List<ManageSubjectModel>>(){});


			manageSubjectsHelper.createSubjects(newSubjectDetails);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ActionSupport.SUCCESS;
	}

	/**
	 * @return
	 */
	public String deleteSubject() {
		List<ManageSubjectModel> newSubjectDetails = new ArrayList<ManageSubjectModel>();
		ManageSubjectsHelper manageSubjectsHelper = new ManageSubjectsHelper();
		try {
			ObjectMapper mapper = new ObjectMapper();
			newSubjectDetails = mapper.readValue(subjectJson, new TypeReference<List<ManageSubjectModel>>(){});


			manageSubjectsHelper.deleteSubjects(newSubjectDetails);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ActionSupport.SUCCESS;
	}


	/**
	 * @return the manageSubjectsDetails
	 */
	public Collection<ManageSubjectModel> getManageSubjectsDetails() {
		return manageSubjectsDetails;
	}

	/**
	 * @param manageSubjectsDetails
	 *            the manageSubjectsDetails to set
	 */
	public void setManageSubjectsDetails(
			final Collection<ManageSubjectModel> manageSubjectsDetails) {
		this.manageSubjectsDetails = manageSubjectsDetails;
	}

	@Override
	public void prepare() throws Exception {
		super.prepare();

	}


	/**
	 * @return the subjectJson
	 */
	public String getSubjectJson() {
		return subjectJson;
	}

	/**
	 * @param subjectJson the subjectJson to set
	 */
	public void setSubjectJson(final String subjectJson) {
		this.subjectJson = subjectJson;
	}

}
