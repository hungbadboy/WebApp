/**
 *
 */
package com.brot.admin.action;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.brot.admin.exception.ServiceConnectionException;
import com.brot.admin.helper.AdminManagementHelper;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Hoai Nguyen
 *
 */
public class ManageMenuAction extends BrotCommonAction {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     * @return
     */
    public String getAllMenuData() {
        AdminManagementHelper helper = new AdminManagementHelper();
        try {
            helper.getAllMenuData();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceConnectionException e) {
            e.printStackTrace();
        }
        return ActionSupport.SUCCESS;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.brot.admin.action.BrotCommonAction#prepare()
     */
    @Override
    public void prepare() throws Exception {
        super.prepare();
    }
}
