package com.sample.soundcloud.network.interceptors;

import android.content.Context;

import com.sample.soundcloud.R;
import com.sample.soundcloud.SoundcloudApplication;
import com.sample.soundcloud.utilities.NetworkUtility;
import com.sample.soundcloud.utilities.RequestUtility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by etiennelawlor on 12/5/16.
 */

public class AuthorizedNetworkInterceptor implements Interceptor {

    // region Member Variables
    private Context context;
    // endregion

    // region Constructors
    public AuthorizedNetworkInterceptor(Context context) {
        this.context = context;
    }
    // endregion

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (chain != null) {
            Request originalRequest = chain.request();

            // Add Cache Control only for GET methods
            if (originalRequest.method().equals("GET")) {
                Request modifiedRequest;

                if (NetworkUtility.isNetworkAvailable(SoundcloudApplication.getInstance())) {
                    int maxAge = 60;
                    Map<String, String> headersMap = new HashMap<>();
                    headersMap.put("Cache-Control", "public, max-age=" + maxAge);
                    modifiedRequest = RequestUtility.updateHeaders(originalRequest, headersMap);

//                                CacheControl cacheControl = new CacheControl.Builder()
//                                        .maxAge(1, TimeUnit.MINUTES)
//                                        .noCache()
//                                        .build();
//                                request.newBuilder()
//                                        .cacheControl(cacheControl)
//                                        .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 7; // 1 week
                    Map<String, String> headersMap = new HashMap<>();
                    headersMap.put("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
                    modifiedRequest = RequestUtility.updateHeaders(originalRequest, headersMap);

//                                CacheControl cacheControl = new CacheControl.Builder()
//                                        .onlyIfCached()
//                                        .maxStale(7, TimeUnit.DAYS)
//                                        .build();
//                                request.newBuilder()
//                                        .cacheControl(cacheControl)
//                                        .build();
                }

                Map<String, String> queryParamsMap = new HashMap<>();
                queryParamsMap.put("client_id", context.getString(R.string.client_id));
                modifiedRequest = RequestUtility.addQueryParams(modifiedRequest, queryParamsMap);

                return chain.proceed(modifiedRequest);
            } else {
                Map<String, String> queryParamsMap = new HashMap<>();
                queryParamsMap.put("client_id", context.getString(R.string.client_id));
                Request modifiedRequest = RequestUtility.addQueryParams(originalRequest, queryParamsMap);

                return chain.proceed(modifiedRequest);
            }
        }

        return null;
    }
}
