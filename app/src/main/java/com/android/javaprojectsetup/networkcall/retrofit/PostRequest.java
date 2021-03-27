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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRequest {

    private APIService mApiService;
    private Context context;
    private JSONObject postData;
    private String requestUrl;
    private boolean isLoaderRequired, isNoInternetDialog = true;

    private ResponseListener listener;
    private ProgressUtil progressUtil;
    private KProgressHUD dialog = null;

    public PostRequest(Context context, JSONObject postData, String requestUrl, boolean isLoaderRequired,
                       boolean isNoInternetDialog, ResponseListener listener) {
        this.context = context;
        this.postData = postData;
        this.requestUrl = requestUrl;
        this.isLoaderRequired = isLoaderRequired;
        this.isNoInternetDialog = isNoInternetDialog;
        this.listener = listener;
        progressUtil = ProgressUtil.getInstance();

        // get retrofit client for making api call
        mApiService = RetrofitClient.getClient(context).create(APIService.class);
    }

    private RequestBody prepareRequestBody() {
        return RequestBody.create(MediaType.parse(AppConstant.HI_MediaType), (postData).toString());
    }

    public void execute() {
        if (!ConnectionDetector.getInstance().internetCheck(context, isNoInternetDialog)) {
            if (listener != null)
                listener.onNoInternet(context.getString(R.string.msg_server_error));
            return;
        }

        if (isLoaderRequired) {
            dialog = progressUtil.initProgressBar(context);
        }

        if (mApiService != null) {
            progressUtil.showDialog(dialog, new ProgressBar(context), isLoaderRequired);

            Call<ResponseBody> bodyCall = mApiService.RequestWithPostData(requestUrl, prepareRequestBody());

            String request = "URL => " + bodyCall.request().url().toString() + "\n" +
                    "headers => " + bodyCall.request().headers().toString() + "\n" +
                    "postDate => " + postData.toString();

            Logger.e(request);

            bodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    try {
                        progressUtil.hideDialog(dialog, new ProgressBar(context));
                        if (listener != null) {
                            if (response.isSuccessful() && response.body() != null) {
                                String data = response.body().string();
                                Logger.e(data);
                                listener.onSucceedToPostCall(data);
                            } else if (response.code() == 401 && response.errorBody() != null) {
                                ResponseBody responseBody = response.errorBody();
                                String errData = responseBody.string();
                                listener.onFailedToPostCall(response.code(), "");
                                Logger.e(errData);
                            } else {
                                Logger.e(response.message());
                                listener.onFailedToPostCall(response.code(), context.getString(R.string.msg_server_error));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Logger.e(e.getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    progressUtil.hideDialog(dialog, new ProgressBar(context));
                    Logger.e(t.getMessage());
                    if (listener != null)
                        listener.onFailedToPostCall(400, context.getString(R.string.error_msg_connection_timeout));
                }
            });
        }
    }

    public void cancelRequest() {
        mApiService.RequestWithPostData(requestUrl, prepareRequestBody()).cancel();
    }

}
