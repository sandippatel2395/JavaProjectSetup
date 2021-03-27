package com.android.javaprojectsetup.networkcall.retrofit;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface APIService {

    @Headers("Content-Type:application/json")
    @POST
    Call<ResponseBody> RequestWithPostData(@Url String url, @Body RequestBody postData);

    @Headers("Content-Type:application/json")
    @POST
    Call<ResponseBody> RequestWithHeader(@Header("Authorization") String token, @Url String url, @Body RequestBody postData);

    @Headers("Content-Type:application/json")
    @PUT
    Call<ResponseBody> PutRequestWithHeader(@Header("Authorization") String token, @Url String url, @Body RequestBody postData);

    @Headers("Content-Type:application/json")
    @DELETE
    Call<ResponseBody> DeleteRequestWithHeader(@Header("Authorization") String token, @Url String url);

    @Headers("Content-Type:application/json")//"Cache-Control: no-cache"
    @GET
    Call<ResponseBody> GetRequestWithHeader(@Header("Authorization") String token, @Url String url);

}
