/*
 * Copyright (c) 2016-2017, Tinhvan Outsourcing JSC. All rights reserved.
 *
 * No permission to use, copy, modify and distribute this software
 * and its documentation for any purpose is granted.
 * This software is provided under applicable license agreement only.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.siblinks.ws.Notification.Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.siblinks.ws.util.Parameters;
import com.siblinks.ws.util.SibConstants;

/**
 * @author hungpd
 * @version 1.0
 */
@Component("notificationFireBase")
public class FireBaseNotification {
    @Autowired
    Environment env;

    public String sendMessage(final String toTokenId, final String title, final String dataType, final String dataId,
            final String content, final String icon, final String priority) {
        InputStreamReader in = null;
        BufferedReader br = null;
        String lines = "";
        try {

            // FirebaseOptions options = new FirebaseOptions.Builder()
            // .setServiceAccount(new
            // FileInputStream("path/to/serviceAccountCredentials.json"))
            // .setDatabaseUrl("https://databaseName.firebaseio.com/")
            // .build();
            // FirebaseApp.initializeApp(options);

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(SibConstants.URL_SEND_NOTIFICATION_FIREBASE);
            post.setHeader("Content-type", "application/json");
            post.setHeader("Authorization", "key=" + env.getProperty("firebase.server.key"));

            JSONObject message = new JSONObject();
            message.put(Parameters.TO, toTokenId);
            message.put(Parameters.PRIORITY, priority);

            JSONObject notification = new JSONObject();
            notification.put(Parameters.TITLE, title);
            notification.put(Parameters.BODY, content);
            // click action
            JSONObject clickAction = new JSONObject();
            clickAction.put(Parameters.DATA_ID, dataId);
            clickAction.put(Parameters.DATA_TYPE, dataType);
            notification.put(Parameters.CLICK_ACTION, clickAction);
            //
            message.put(Parameters.NOTIFICATION, notification);

            post.setEntity(new StringEntity(message.toString(), "UTF-8"));
            HttpResponse response = client.execute(post);
            in = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
            br = new BufferedReader(in);
            String line = null;
            while ((line = br.readLine()) != null) {
                lines += line;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                // Do nothing
            }
        }
        return lines;
    }
}
