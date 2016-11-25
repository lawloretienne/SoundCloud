package com.sample.soundcloud.utilities;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by etiennelawlor on 6/1/16.
 */

public class NetworkLogUtility {

    public static void logFailure(Call call, Throwable throwable){
        if(call != null){
            if (call.isCanceled())
                Timber.e("Request was cancelled");

//            Request request = call.request();
//            if(request != null){
//                HttpUrl httpUrl = request.url();
//                if(httpUrl != null){
//                    Timber.e(String.format("logFailure() : %s : failed", httpUrl));
//                }
//            }
        }

        if(throwable != null){
            Throwable cause = throwable.getCause();
//            String message = throwable.getMessage();

            if (cause != null) {
                Timber.e(String.format("logFailure() : cause.toString() : %s", cause.toString()));
            }

//            if (!TextUtils.isEmpty(message)) {
//                Timber.e(String.format("logFailure() : message : %s", message));
//            }

//            throwable.printStackTrace();
        }
    }

    public static void logFailedResponse(Call call, Response response){

//        if(call != null){
//            Request request = call.request();
//            if(request != null){
//                HttpUrl httpUrl = request.url();
//                if(httpUrl != null){
//                    Timber.e(String.format("logFailedResponse() : %s : failed", httpUrl.toString()));
//                }
//            }
//        }

//        if(response != null){
//            int responseCode = response.code();
//            Timber.e(String.format("logFailedResponse() : response_code : %d", responseCode));

//            okhttp3.Response rawResponse = response.raw();
//            if (rawResponse != null) {
//                String message = rawResponse.message();
//                int code = rawResponse.code();
//                Timber.e(String.format("logFailedResponse() : message : %s : status_code : %d", message, code));
//            }

//            ResponseBody responseBody = response.errorBody();
//            if(responseBody != null){
//                BufferedSource bufferedSource = responseBody.source();
//                if(bufferedSource != null){
//                    Timber.e(String.format("logFailedResponse() : error_message : %s ", bufferedSource.toString()));
//                }
//            }
//        }
    }

//    public static void logStatusCode(Response response){
//        if(response != null) {
//            int code = response.code();
//
//            Timber.e(String.format("logStatusCode() : code : %d", code));
//
//            if (code >= 200 && code < 300) {
//                Timber.d("logStatusCode() : Success");
//            } else if (code == 401) {
//                Timber.e("logStatusCode() : Unauthenticated");
//            } else if (code >= 400 && code < 500) {
//                Timber.e("logStatusCode() : Client Error");
//            } else if (code >= 500 && code < 600) {
//                Timber.e("logStatusCode() : Server Error");
//            } else {
//                Timber.e("logStatusCode() : Unexpected Error");
//            }
//        }
//    }
}
