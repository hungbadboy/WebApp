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
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author hungpd
 * @version 1.0
 */
@Component("notificationFireBase")
public class FireBaseNotification {
    @Autowired
    Environment env;

    public String sendMessage(final String toTokenId, final String title, final String body, final String icon,
            final String priority) {
        try {

            // FirebaseOptions options = new FirebaseOptions.Builder()
            // .setServiceAccount(new
            // FileInputStream("path/to/serviceAccountCredentials.json"))
            // .setDatabaseUrl("https://databaseName.firebaseio.com/")
            // .build();
            // FirebaseApp.initializeApp(options);

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
            post.setHeader("Content-type", "application/json");
            post.setHeader("Authorization", "key=" + env.getProperty("firebase.server.key"));

            JSONObject message = new JSONObject();
            message.put("to",toTokenId);
            message.put("priority", priority);

            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", body);
            message.put("notification", notification);

            post.setEntity(new StringEntity(message.toString(), "UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(content, "UTF-8"));
            String line=null;
            String lines = "";
            while ((line = br.readLine()) != null) {
                lines += line;
            }
            br.close();
            return lines;

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
