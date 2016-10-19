package com.siblinks.ws.util;

//Include the Dropbox SDK.
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxEntry;

public class DropBoxDownloading {
    public static void main(final String[] args) throws IOException, DbxException {
        // Get your app key and secret from the Dropbox developers website.
        final String APP_KEY = "hbtsh7f2ezeanan";
        final String APP_SECRET = "ryexe8g1ydcyfw9";

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

        // Have the user sign in and authorize your app.
        String authorizeUrl = webAuth.start();
        System.out.println("1. Go to: " + authorizeUrl);
        System.out.println("2. Click \"Allow\" (you might have to log in first)");
        System.out.println("3. Copy the authorization code.");
        String code = "";
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            if (bufferedReader != null) {
                code = bufferedReader.readLine();
            }
        } catch (Exception io) {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        // This will fail if the user enters an invalid authorization code.
        DbxAuthFinish authFinish = webAuth.finish(code);
        String accessToken = authFinish.getAccessToken();

        DbxClientV1 client = new DbxClientV1(config, accessToken);

        System.out.println("Linked account: " + client.getAccountInfo().displayName);

        // getChildRecords(client,"/Approved Videos by Leaders/Math/Calculus");
        getChildRecords(client, "/Biology Team");
    }

    // getChildRecords(client,"/Physics Team/Thaman");

    private static void getChildRecords(final DbxClientV1 client, final String path) throws DbxException, IOException {
        DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
        for (DbxEntry child : listing.children) {

            if (child.isFolder()) {

                getChildRecords(client, child.path);

            } else if (child.isFile()) {

                String[] str = child.path.split("/");

                try {
                    ByteArrayOutputStream outputStream = null;
                    InputStream inputStream = null;
                    try {
                        outputStream = new ByteArrayOutputStream();
                        client.getFile(child.path, null, outputStream);

                        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                        try {
                            UploadVideo upload = new UploadVideo();// Algebra 2
                            upload.upload(child.name, inputStream, "Biology", str[2], str[3]);
                            // upload.upload(child.name,inputStream,"Physics","Thaman",str[3]);
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } finally {
                        // System.out.println("===closing==");
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }

                    }

                } finally {

                }

            }

        }
    }
}
