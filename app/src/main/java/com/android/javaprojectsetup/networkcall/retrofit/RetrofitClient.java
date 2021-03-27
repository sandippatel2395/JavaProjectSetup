package com.android.javaprojectsetup.networkcall.retrofit;

import android.content.Context;

import com.android.javaprojectsetup.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    private static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder().addHeader("Cache-Control", "no-cache");
            request = builder.build();
            return chain.proceed(request);
        }
    };

    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(180, TimeUnit.SECONDS)
            //.addInterceptor(new ChuckInterceptor(Globals.getContext()))
            .addInterceptor(interceptor)
            .readTimeout(180, TimeUnit.SECONDS)
            .cache(null)
            .build();

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(SelfSigningClientBuilder.getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.api_base_url,context.getString(R.string.base_url)))
                    .client(SelfSigningClientBuilder.getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void cancelAllRequest() {
        //client.dispatcher().cancelAll();
        SelfSigningClientBuilder.getUnsafeOkHttpClient().dispatcher().cancelAll();
    }


}
