package com.brot.admin.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.brot.admin.client.MobileServiceManagerClient;
import com.brot.admin.exception.ServiceConnectionException;
import com.brot.admin.model.UserInfoModel;
import com.brot.admin.resource.AppConfigManager;
import com.brot.admin.util.ApplicationConstants;
import com.brot.admin.util.Parameters;
import com.opensymphony.xwork2.ActionSupport;

public class LoginUserAction extends BrotCommonAction {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /*
     * private DataSource dataSource; private Connection connection; private
     * Statement statement;
     */
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    // all struts logic here
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String execute() {

        Map queryParams = new HashMap();

        queryParams.put(Parameters.USER_NAME, username);
        queryParams.put(Parameters.PASSWORD, password);

        String endPointUrl = AppConfigManager.getConfigVal(Parameters.ROOT_URL_API);
        endPointUrl = endPointUrl + ApplicationConstants.RequestMappingAPI.LOGIN;
        MobileServiceManagerClient _instance = MobileServiceManagerClient.getInstance();
        String status = ActionSupport.ERROR;
        try {
            String response = _instance.write(queryParams, endPointUrl);
            JSONObject jsonObject = new JSONObject(response);
            boolean isStatus = jsonObject.getBoolean(Parameters.STATUS);
            if (isStatus) {

                ObjectMapper mapper = new ObjectMapper();
                UserInfoModel userInfo = mapper.readValue(response, UserInfoModel.class);
                status = ActionSupport.SUCCESS;
                // Save info to model
                (ServletActionContext.getRequest().getSession()).setAttribute(
                    Parameters.ROOT_URL_API,
                    AppConfigManager.getConfigVal(Parameters.ROOT_URL_API));
                (ServletActionContext.getRequest().getSession()).setAttribute(Parameters.USER_INFO, userInfo);
            } else {
                status = ActionSupport.ERROR;
                HttpServletRequest request = ServletActionContext.getRequest();
                request.setAttribute("ERROR", jsonObject.getString(Parameters.REQUEST_DATA_RESULT));
            }

        } catch (ServiceConnectionException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }

        return status;
    }

    public String execute1() {
        System.out.println("=========execute111====================");
        String status = "SUCCESS";
        if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {

            status = "SUCCESS";
        }

        return status;
    }

    public String registerNewUser() {
        String status = "SUCCESS";
        System.out.println("status==" + status);
        return status;
    }

    public String createUserAccount() {
        return ActionSupport.SUCCESS;
    }

    @Override
    public void prepare() throws Exception {
    }

}