package com.android.javaprojectsetup.networkcall;

public interface ResponseListener {
    void onSucceedToPostCall(String response);

    void onFailedToPostCall(int statusCode, String msg);

    void onNoInternet(String msg);

    default void onLicenseExpire(String response) {
    }

}
