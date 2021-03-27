package com.android.javaprojectsetup.networkcall.retrofit;

import android.content.Context;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.android.javaprojectsetup.R;
import com.android.javaprojectsetup.constant.AppConstant;
import com.android.javaprojectsetup.networkcall.ConnectionDetector;
import com.android.javaprojectsetup.networkcall.ResponseListener;
import com.android.javaprojectsetup.utility.ProgressUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PutRequestWithHeader {
    private APIService mApiService;
    private Context context;
    private String requestUrl;
    private boolean isLoaderRequired, isNoInternetDialog = true;
    private boolean isFromSubscription;
    private ResponseListener listener;
    private ProgressUtil progressUtil;
    private RequestBody requestBody;
    private JSONObject postData;
    private ProgressBar pb = null;
    private KProgressHUD dialog = null;
    private String token = "Bearer ";

    public PutRequestWithHeader(Context context, String token, JSONObject postData, String requestUrl, ProgressBar pb, boolean isLoaderRequired,
                                boolean isNoInternetDialog, boolean isFromSubscription, ResponseListener listener) {
        this.context = context;
        this.token += token;
        this.postData = postData;
        this.requestUrl = requestUrl;
        this.pb = pb;
        this.isLoaderRequired = isLoaderRequired;
        this.isNoInternetDialog = isNoInternetDialog;
        this.isFromSubscription = isFromSubscription;
        this.listener = listener;
        init();
    }

    public PutRequestWithHeader(Context context, String token, JSONObject postData, String requestUrl, ProgressBar pb, boolean isLoaderRequired,
                                boolean isNoInternetDialog, ResponseListener listener) {
        this.context = context;
        this.token += token;
        this.postData = postData;
        this.requestUrl = requestUrl;
        this.pb = pb;
        this.isLoaderRequired = isLoaderRequired;
        this.isNoInternetDialog = isNoInternetDialog;
        this.listener = listener;
        init();
    }

    private void init() {
        progressUtil = ProgressUtil.getInstance();
        // convert json object in to request body
        requestBody = RequestBody.create(MediaType.parse(AppConstant.HI_MediaType), (postData).toString());
        // get retrofit client for making api call
        mApiService = RetrofitClient.getClient(context).create(APIService.class);
    }

    public void execute() {
        if (!ConnectionDetector.getInstance().internetCheck(context, isNoInternetDialog)) {
            if (listener != null)
                listener.onNoInternet(context.getString(R.string.msg_server_error));
            return;
        }

        if (isLoaderRequired && pb == null) {
            dialog = progressUtil.initProgressBar(context);
        }

        if (mApiService != null) {
            progressUtil.showDialog(dialog, pb, isLoaderRequired);

            Call<ResponseBody> bodyCall = mApiService.PutRequestWithHeader(token, requestUrl, requestBody);

            String request = "URL => " + bodyCall.request().url().toString() + "\n" +
                    "headers => " + bodyCall.request().headers().toString() + "\n" +
                    "postDate => " + postData.toString();

            Logger.e(request);

            bodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    try {
                        progressUtil.hideDialog(dialog, pb);
                        if (listener != null) {
                            if (response.isSuccessful() && response.body() != null) {
                                String data = response.body().string();
                                Logger.e(data);
                                listener.onSucceedToPostCall(data);
                            } else if (response.code() == 401 && response.errorBody() != null) {
                                ResponseBody responseBody = response.errorBody();
                                String errData = responseBody.string();
                                Logger.e(errData);
                                listener.onFailedToPostCall(response.code(), "");
                            } else {
                                Logger.e(response.message());
                                listener.onFailedToPostCall(response.code(), context.getString(R.string.msg_server_error));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Logger.e(Objects.requireNonNull(e.getMessage()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    progressUtil.hideDialog(dialog, pb);
                    Logger.e(t.getMessage());
                    if (listener != null)
                        listener.onFailedToPostCall(400, context.getString(R.string.error_msg_connection_timeout));
                }
            });
        }
    }

    public void cancelRequest() {
        mApiService.RequestWithPostData(requestUrl, requestBody).cancel();
    }
}
