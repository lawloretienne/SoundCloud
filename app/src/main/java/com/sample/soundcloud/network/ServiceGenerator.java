package com.sample.soundcloud.network;

import android.text.TextUtils;

import com.sample.soundcloud.BuildConfig;
import com.sample.soundcloud.SoundcloudApplication;
import com.sample.soundcloud.utilities.NetworkUtility;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by etiennelawlor on 6/14/15.
 */
public class ServiceGenerator {

    // region Constants
    private static final int DISK_CACHE_SIZE = 10 * 1024 * 1024; // 10MB
    // endregion

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder();

    // No need to instantiate this class.
    private ServiceGenerator() {
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .cache(getCache())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();

                        // Add Cache Control only for GET methods
                        if (request.method().equals("GET")) {
                            if (NetworkUtility.isNetworkAvailable(SoundcloudApplication.getInstance())) {
                                int maxAge = 60;
                                request = request.newBuilder().header("Cache-Control", "public, max-age=" + maxAge).build();

//                                CacheControl cacheControl = new CacheControl.Builder()
//                                        .maxAge(1, TimeUnit.MINUTES)
//                                        .noCache()
//                                        .build();
//                                request.newBuilder()
//                                        .cacheControl(cacheControl)
//                                        .build();
                            } else {
                                int maxStale = 60 * 60 * 24 * 7; // 1 week
                                request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).build();

//                                CacheControl cacheControl = new CacheControl.Builder()
//                                        .onlyIfCached()
//                                        .maxStale(7, TimeUnit.DAYS)
//                                        .build();
//                                request.newBuilder()
//                                        .cacheControl(cacheControl)
//                                        .build();
                            }
                        }

                        return chain.proceed(request);
                    }
                })
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        if (chain != null) {
                            Request originalRequest = chain.request();

                            HttpUrl originalHttpUrl = originalRequest.url();

                            HttpUrl url = originalHttpUrl.newBuilder()
                                    .addQueryParameter("client_id", "309011f9713d22ace9b976909ed34a80")
                                    .build();

                            // Request customization: add request headers
                            Request.Builder requestBuilder = originalRequest.newBuilder()
                                    .url(url);

                            Request request = requestBuilder.build();
                            return chain.proceed(request);

//                    if (!TextUtils.isEmpty(clientId) && !TextUtils.isEmpty(clientSecret)) {
//                        // concatenate username and password with colon for authentication
//                        final String credentials = clientId + ":" + clientSecret;
//
//                        String authorization = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//
//                        Request modifiedRequest = originalRequest.newBuilder()
//                                .header("Authorization", authorization)
//                                .header("Accept", "application/json")
//                                .build();
//                        Timber.d("Authorization : "+ authorization);
//
//                        return chain.proceed(modifiedRequest);
//                    } else {
//                        return chain.proceed(originalRequest);
//                    }

//                            String sessionId = ClvbPrefs.getSessionId(ClvbApplication.getInstance());
//
//                            if (!TextUtils.isEmpty(sessionId)) {
//
//                                Request modifiedRequest = originalRequest.newBuilder()
//                                        .header("Cookie", String.format("sessionid=%s", sessionId))
////                                        .header("Accept", "application/json")
//                                        .build();
//                                Headers headers = modifiedRequest.headers();
//                                Timber.d("Headers -> %s", headers.toString());
//                                return chain.proceed(modifiedRequest);
//                            } else {
//                                return chain.proceed(originalRequest);
//                            }

//                            return chain.proceed(originalRequest);
                        }

                        return null;
                    }
                })
                .addInterceptor(getHttpLoggingInterceptor()) // Add only for debugging purposes
                .build();

        retrofitBuilder.client(okHttpClient);
        retrofitBuilder.baseUrl(baseUrl);
        retrofitBuilder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl, final String sessionId) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .cache(getCache())
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        if (chain != null) {
                            Request originalRequest = chain.request();

//                            String sessionId = ClvbPrefs.getSessionId(ClvbApplication.getInstance());

                            if (!TextUtils.isEmpty(sessionId)) {

                                Request modifiedRequest = originalRequest.newBuilder()
                                        .header("Cookie", String.format("sessionid=%s", sessionId))
//                                        .header("Accept", "application/json")
                                        .build();
                                Headers headers = modifiedRequest.headers();
                                Timber.d("Headers -> %s", headers.toString());
                                return chain.proceed(modifiedRequest);
                            } else {
                                return chain.proceed(originalRequest);
                            }
                        }

                        return null;
                    }
                })
                .addInterceptor(getHttpLoggingInterceptor())
                .build();

        retrofitBuilder.client(okHttpClient);
        retrofitBuilder.baseUrl(baseUrl);
        retrofitBuilder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(serviceClass);
    }

//    public static <S> S createService(Class<S> serviceClass, String baseUrl, final String clientId, final String clientSecret) {
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .cache(getCache())
//                .addNetworkInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        if (chain != null) {
//                            Request originalRequest = chain.request();
//
//                            if (!TextUtils.isEmpty(clientId) && !TextUtils.isEmpty(clientSecret)) {
//                                // concatenate username and password with colon for authentication
//                                final String credentials = clientId + ":" + clientSecret;
//
//                                String authorization = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//
//                                Request modifiedRequest = originalRequest.newBuilder()
//                                        .header("Authorization", authorization)
//                                        .header("Accept", "application/json")
//                                        .build();
//                                Timber.d("Authorization : "+ authorization);
//
//                                return chain.proceed(modifiedRequest);
//                            } else {
//                                return chain.proceed(originalRequest);
//                            }
//                        }
//
//                        return null;
//                    }
//                })
//                .addInterceptor(getHttpLoggingInterceptor())
//                .build();
//
//        retrofitBuilder.client(okHttpClient);
//        retrofitBuilder.callFactory(okHttpClient);
//        retrofitBuilder.baseUrl(baseUrl);
//        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = retrofitBuilder.build();
//        return retrofit.create(serviceClass);
//    }


//    public static <S> S createService(Class<S> serviceClass, String baseUrl, final AccessToken accessToken) {
//        OkHttpClient okHttpClient = getClient();
//        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
//        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
//        okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
//
//        okHttpClient.networkInterceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                if(chain != null){
//                    Request originalRequest = chain.request();
//
//                    if (accessToken != null) {
//                        String authorization = accessToken.getTokenType() + " " + accessToken.getAccessToken();
//
//                        Request modifiedRequest = originalRequest.newBuilder()
//                                .header("Authorization", authorization)
//                                .header("Accept", "application/vnd.vimeo.*+json; version=3.2")
//                                .build();
//                        Timber.d("Authorization : "+ authorization);
//
//                        return chain.proceed(modifiedRequest);
//                    } else {
//                        return chain.proceed(originalRequest);
//                    }
//                }
//
//                return null;
//            }
//        });
//
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//        if (BuildConfig.DEBUG) {
//            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        } else {
//            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
//        }        okHttpClient.interceptors().add(httpLoggingInterceptor); // Add only for debugging purposes
//
//        sRetrofitBuilder.client(okHttpClient);
//        sRetrofitBuilder.baseUrl(baseUrl);
//        sRetrofitBuilder.addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = sRetrofitBuilder.build();
//        return retrofit.create(serviceClass);
//    }

    private static Cache getCache() {

        Cache cache = null;
        // Install an HTTP cache in the application cache directory.
        try {
            File cacheDir = new File(SoundcloudApplication.getCacheDirectory(), "http");
            cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        } catch (Exception e) {
            Timber.e(e, "Unable to install disk cache.");
        }
        return cache;
    }

    private static HttpLoggingInterceptor getHttpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return httpLoggingInterceptor;
    }

    private static class LoggingInterceptor implements Interceptor {
        @Override public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Timber.i(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Timber.i(String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
}

