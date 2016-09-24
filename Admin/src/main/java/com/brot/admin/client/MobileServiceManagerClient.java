package com.brot.admin.client;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brot.admin.exception.ServiceConnectionException;

public class MobileServiceManagerClient<T> {
    private static final Logger LOG = LoggerFactory.getLogger(MobileServiceManagerClient.class);
    public static final String DEFAULT_DATE_PATTERN = "yyyy/MM/dd HH:mm:ss:SSSS";
    private static MobileServiceManagerClient _instance = null;
    private static ExecutorService executor = Executors.newFixedThreadPool(5);

    private MobileServiceManagerClient() {
        LOG.debug("initialized...");
    }

    public synchronized static MobileServiceManagerClient getInstance() {
        if (_instance == null) {
            _instance = new MobileServiceManagerClient();
        }
        return _instance;
    }

    @SuppressWarnings("unchecked")
    public List<T> getConfigDetails(final Class<T> clazz, final String env, final String configName,
            final Map<String, String> queryParams) throws HttpException, IOException, JSONException {

        StringBuffer request = new StringBuffer();
        try {
            request.append(env);
            if (configName == null || configName.equals("")) {
                throw new Exception("config name can't be null");
            }

            request.append(configName);
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                request.append("&" + entry.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LOG.debug(request.toString());
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(request.toString());
        // client.startSession(url);
        int code = client.executeMethod(method);
        String jsonStringResponse = method.getResponseBodyAsString();
        return getObjectAsResponse(jsonStringResponse, clazz);

    }

    public String write(final Map<String, String> queryParams, final String endpointURL) throws ServiceConnectionException {
        try {
            /*
             * StringBuffer serviceURL = fixURL(endpointURL, "write")
             * .append("en=").append(entityName);
             */
            HttpMethod method = null;
            method = createPostMethod(endpointURL, queryParams);
            String jsonString = invoke(method);
            return jsonString;
        } catch (Exception e) {
            LOG.error("Exception in excecuting write:", e);
        }
        return null;
    }

    /**
     * This method create http request by POST method
     * 
     * @param baseURL
     *            the url request call service
     * @param parameters
     *            these parameter request
     * @return http method
     */
    private HttpMethod createPostMethod(final String baseURL, final Map<String, String> parameters) {

        PostMethod method = new PostMethod(baseURL);

        if (parameters != null) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                method.addParameter(entry.getKey(), entry.getValue());
            }
        }
        return method;
    }

    private String invoke(final HttpMethod method) throws ServiceConnectionException {
        try {

            int code = HttpConnectionManager.getHttpClient().executeMethod(method);

            if (code != HttpStatus.SC_OK && code != HttpStatus.SC_UNAUTHORIZED) {
                throw new ServiceConnectionException("Service invocation returned with HttpStatus :" + code);
            }

            return method.getResponseBodyAsString();

        } catch (HttpException e) {
            e.printStackTrace();
            throw new ServiceConnectionException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceConnectionException(e);
        } finally {
            method.releaseConnection();
        }
    }

    public String callServiceAPI(final String method, final String endpointURL,
            final Map<String, String> queryParams) {
        HttpMethod httpMethod = null;
        String strReponseDate = "";

        if (method == null ||
            "".equals(method) ||
            endpointURL == null ||
            "".equals(endpointURL)) {
            return "";
        }

        try {
            if (method.equals("POST")) {
                httpMethod = new PostMethod(endpointURL);
            } else if (method.equals("GET")) {
                httpMethod = createGetMethod(endpointURL, queryParams);
            }
            strReponseDate = invoke(httpMethod);

        } catch (ServiceConnectionException e) {
            LOG.error("call service error:", e);
            e.printStackTrace();
        }
        return strReponseDate;
    }

    /**
     * This method create http request by GET method
     * 
     * @param baseURL
     *            the url request call service
     * @param parameters
     *            these parameter request
     * @return http method
     */
    private HttpMethod createGetMethod(final String baseURL,
            final Map<String, String> parameters) {
        StringBuffer url = new StringBuffer(baseURL);
        if (parameters != null) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                url.append("&" + entry.toString());
            }
        }
        HttpMethod method = new GetMethod(baseURL);
        return method;
    }

    public static StringBuffer fixURL(final String endpointUrl, final String serviceName) {
        StringBuffer url = new StringBuffer(endpointUrl);

        if (!endpointUrl.trim().endsWith("?")) {
            url.append(serviceName);
            url.append("?");
        }
        return url;
    }

    public List dataService(final Class<T> clazz, final String entityName, final Map<String, String> queryParams,
            final String endpointURL) throws HttpException, IOException {
        String response = null;
        List dsResultList = new ArrayList();

        StringBuffer serviceURL = fixURL(endpointURL, "process");
        serviceURL.append("en=").append(entityName);
        System.out.println("Hoai Nguyen: " + serviceURL);
        // queryParams.put("en", entityName);
        List<Future<String>> flist = new LinkedList<Future<String>>();
        CallableDataService cqd = null;
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();
            Set<Callable<String>> callables = new HashSet<Callable<String>>();
            Callable<String> callable1 = new CallableDataService(serviceURL.toString(), queryParams);
            callables.add(callable1);
            long startService = System.currentTimeMillis();

            List<Future<String>> futures = null;
            try {
                futures = executorService.invokeAll(callables);

            } catch (InterruptedException ie) {
                LOG.error("InterruptedException " + ie.getMessage());
                ie.printStackTrace();
            }

            executorService.shutdown();

            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException ie) {
                LOG.equals("InterruptedException " + ie.getMessage());
                ie.printStackTrace();
            }
            for (int i = 0; i < ((futures != null) ? futures.size() : 0); i++) {
                Future<String> future = futures.get(i);
                try {
                    response = future.get();
                    if (response != null && response.length() > 0) {

                        if (entityName.contains("READ") && clazz != null) {
                            /*
                             * dsResultList = getObjectAsResponse(response,
                             * clazz);
                             */

                            dsResultList.add(response);
                        } else if (entityName.contains("READ") && null == clazz) {
                            /*
                             * DISPResultSet rs = new JSONResultSet(response);
                             * dsResultList.add(rs);
                             */

                            dsResultList.add(response);
                        } else {
                            dsResultList.add(response);
                            /*
                             * DISPResultSet rs = new JSONResultSet(response);
                             * StringBuffer resBuf = new StringBuffer();
                             * resBuf.append(rs.getColumns()[0].getName());
                             * resBuf.append(":");
                             * resBuf.append(rs.getRows()[0].getItem(0));
                             * LOG.debug("Return status : " +
                             * resBuf.toString());
                             * dsResultList.add(resBuf.toString());
                             */
                        }
                    } else {
                        // SimpleResultSet drs = new SimpleResultSet("EMPTY",
                        // new Column[0], new Row[0], 0,0);
                        dsResultList = null;
                    }

                } catch (InterruptedException ie) {
                    LOG.error("InterruptedException ::::: " + ie.getMessage());
                } catch (ExecutionException ee) {
                    LOG.error("ExecutionException ::::: " + ee.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // executor.shutdown();
        }
        return dsResultList;
    }

    private List<T> getObjectAsResponse(final String reponse, final Class clazz) {

        // DISPResultSet rs;
        List<T> list = new ArrayList<T>();
        /*
         * 
         * try { rs = new JSONResultSet(reponse); while (rs.hasNextRow()) {
         * list.add((T) rs.getNextRow(clazz)); } rs.close(); } catch
         * (JSONException e) { e.printStackTrace(); }
         */

        // LOG.debug("Response object list: " +list);
        return list;
    }

    private class ResponseObject {
        private String configName;

        private String reponse;

        public ResponseObject(final String configName, final String reponse) {
            super();
            this.configName = configName;
            this.reponse = reponse;
        }

        /**
         * @return the configName
         */
        public String getConfigName() {
            return configName;
        }

        /**
         * @param configName
         *            the configName to set
         */
        public void setConfigName(final String configName) {
            this.configName = configName;
        }

        /**
         * @return the reponse
         */
        public String getReponse() {
            return reponse;
        }

        /**
         * @param reponse
         *            the reponse to set
         */
        public void setReponse(final String reponse) {
            this.reponse = reponse;
        }

    }

    private class ExecuteCallable implements Callable<ResponseObject> {
        private final String request;
        private final String configName;

        public ExecuteCallable(final String configName, final String request) {
            this.request = request;
            this.configName = configName;

        }

        @Override
        public ResponseObject call() {
            ResponseObject response = null;
            try {
                HttpClient client = new HttpClient();
                HttpMethod method = new GetMethod(request);
                int code = client.executeMethod(method);
                // response = method.getResponseBodyAsString();
                response = new ResponseObject(configName, method.getResponseBodyAsString());
                // LOG.debug("Response:" + response);
            } catch (Exception se) {
                LOG.debug("Exception :: request Query in MultiThread ");
                se.printStackTrace();

            }
            return response;
        }
    }

    private class CallableDataService implements Callable<String> {
        private final String env;
        private final Map<String, String> queryParams;
        String jsonStringResponse;

        public CallableDataService(final String env, final Map queryParams) {
            this.env = env;
            this.queryParams = queryParams;

        }

        @Override
        public String call() {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                System.out.println("~~~~~~Request URL for Data Service : " + env);
                HttpClient client = new HttpClient();
                PostMethod method = new PostMethod(env);
                method.addParameter("REQ_ACCEPTED", "true");
                if (queryParams != null) {
                    System.out.println("queryParams size = " + queryParams.size());
                    Iterator it = queryParams.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        /*
                         * if(entry.getKey().toString().contains("st") ||
                         * entry.getKey().toString().contains("et"))
                         * method.addParameter(entry.getKey().toString(),
                         * dateToDefaultString(xmlGregorianCalendarToTimestamp(
                         * longToXMLGregorianCalendar
                         * (Long.parseLong(entry.getValue().toString())))));
                         * else {
                         */
                        if (null != entry.getValue()) {
                            method.addParameter(entry.getKey().toString(), entry.getValue().toString());
                        } else {
                            method.addParameter(entry.getKey().toString(), "");
                            /* } */
                        }

                    }
                }
                int code = client.executeMethod(method);
                jsonStringResponse = method.getResponseBodyAsString();
                System.out.println("jsonStringResponse === " + jsonStringResponse);
                LOG.debug("DataService JSON Response:" + jsonStringResponse);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonStringResponse;
        }
    }

    public static XMLGregorianCalendar longToXMLGregorianCalendar(final long millis) throws DatatypeConfigurationException {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTimeInMillis(millis);

        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
    }

    public static Timestamp xmlGregorianCalendarToTimestamp(final XMLGregorianCalendar c) {
        if (c == null) {
            return null;
        }
        Timestamp ts = new Timestamp(c.toGregorianCalendar().getTime().getTime());
        return ts;
    }

    public static String dateToDefaultString(final Date d) {
        return dateToString(d, DEFAULT_DATE_PATTERN);
    }

    public static String dateToString(final Date d, final String pattern) {
        if (d == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(d);
    }

}
