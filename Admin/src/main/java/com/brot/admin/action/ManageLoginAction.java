package com.brot.admin.action;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.brot.admin.helper.ManageLoginActionHelper;
import com.opensymphony.xwork2.Preparable;


public class ManageLoginAction extends BrotCommonAction implements Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ManageLoginActionHelper actionHelper = new ManageLoginActionHelper();
	/*
	 * private DataSource dataSource; private Connection connection; private
	 * Statement statement;
	 */
	private String username;
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// all struts logic here
	public String execute() throws HttpException, IOException {
		String status = "SUCCESS";
			status = actionHelper.validateUser(username, password);
			if(status == "true")
				status = "SUCCESS";
			else
				status = "FAILURE";
		return status;
	}

	@Override
	public void prepare() throws Exception {
		super.prepare();
		/*
		 * DBUtil dbUtil = new
		 * DBUtil("jdbc:mysql://10.65.63.240:3306/adminmodule",
		 * "com.mysql.jdbc.Driver", "root", "admin1234"); try { connection =
		 * dbUtil.getConnection(dbUtil); } catch (SQLException e) {
		 * e.printStackTrace(); }
		 */
	}
}