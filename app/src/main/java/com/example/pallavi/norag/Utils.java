package com.example.pallavi.norag;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
public class Utils {
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override public Response intercept(Chain chain) throws IOException {
            Log.d("MYTAG", "intercepted");
            Response originalResponse = chain.proceed(chain.request());
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    };

    public static OkHttpClient getClient(Context context) {
        return new OkHttpClient.Builder().addInterceptor(
                REWRITE_CACHE_CONTROL_INTERCEPTOR
        ).build();
    }


}
