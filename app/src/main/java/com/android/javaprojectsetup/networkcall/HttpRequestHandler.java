package com.android.javaprojectsetup.networkcall;

import com.android.javaprojectsetup.constant.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpRequestHandler {
    private static HttpRequestHandler mInstance = null;
    public String TAG = getClass().getName();

    public static HttpRequestHandler getInstance() {
        if (mInstance == null) {
            mInstance = new HttpRequestHandler();
        }
        return mInstance;
    }

    // login request params
    public JSONObject getLoginUserJson(String etUsername, String etPassword, String deviceToken) {
        JSONObject params = new JSONObject();
        try {
            params.put(AppConstant.HI_Username, etUsername);
            params.put(AppConstant.HI_Password, etPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }
}
