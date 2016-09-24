/**
 *
 */
package com.brot.admin.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brot.admin.client.MobileServiceManagerClient;
import com.brot.admin.resource.AppConfigManager;
import com.brot.admin.util.Parameters;

/**
 * @author viddasar
 *
 */
public class ManageLoginActionHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ManageLoginActionHelper.class);

	public String validateUser(final String userName, final String password)
			throws HttpException, IOException {

		String endPointUrl = AppConfigManager.getConfigVal("dataServiceURL");
		Map queryParams = new HashMap();
        queryParams.put(Parameters.EMAIL_ID, userName);
        queryParams.put(Parameters.PASSWORD, password);

        List response = MobileServiceManagerClient.getInstance().dataService(
            null,
            "MANAGEUSER_DATA_VALIDATE_READ",
            queryParams,
            endPointUrl);
        LOG.info("response === " + response);
		if (null != response) {
			return "true";
		}

		return "false";

	}
}
