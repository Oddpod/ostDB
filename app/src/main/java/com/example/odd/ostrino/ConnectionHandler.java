package com.example.odd.ostrino;

/**
 * Created by Odd on 25.02.2017.
 */

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnectionHandler {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    public static MediaType JSON;
    OkHttpClient client;

    // constructor
    public ConnectionHandler() {
        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");
    }

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
