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

import com.brot.admin.helper.ManageQAndAnsHelper;
import com.brot.admin.model.ManageQAndAnsModel;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author viddasar
 *
 */
public class ManageQAndAnsAction extends BrotCommonAction  implements Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Collection<ManageQAndAnsModel> brotQAndAnsDetails = new ArrayList<ManageQAndAnsModel>();
	
	private String qandandJson;
	ManageQAndAnsHelper manageQAndAnsHelper = new ManageQAndAnsHelper();
	
	
	public String updateManageQAndAnsAction(){
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			brotQAndAnsDetails = mapper.readValue(qandandJson, new TypeReference<List<ManageQAndAnsModel>>(){});
			
			manageQAndAnsHelper.updateManageQAndAnsAction(brotQAndAnsDetails);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ActionSupport.SUCCESS;
	}
	
	
	public String getQAndAnsDetails(){
			
			/*ManageQAndAnsModel andAnsModel = new ManageQAndAnsModel();
			andAnsModel.setUser_id(100);
			andAnsModel.setQuestion_ans_txt("What is the Java");
			andAnsModel.setCreation_date(new Date());
			andAnsModel.setUsername("SAM");
			andAnsModel.setAnswers("Java is the Programming Language");
			
			brotQAndAnsDetails.add(andAnsModel);*/
			try {
				brotQAndAnsDetails = manageQAndAnsHelper.getQAndAnsDetails();
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		return ActionSupport.SUCCESS;
	}
	
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		
	}



	public Collection<ManageQAndAnsModel> getBrotQAndAnsDetails() {
		return brotQAndAnsDetails;
	}


	public void setBrotQAndAnsDetails(
			Collection<ManageQAndAnsModel> brotQAndAnsDetails) {
		this.brotQAndAnsDetails = brotQAndAnsDetails;
	}


	public String getQandandJson() {
		return qandandJson;
	}


	public void setQandandJson(String qandandJson) {
		this.qandandJson = qandandJson;
	}

}
