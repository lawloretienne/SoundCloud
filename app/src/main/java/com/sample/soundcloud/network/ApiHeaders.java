package com.sample.soundcloud.network;

import retrofit.RequestInterceptor;

/**
 * Created by etiennelawlor on 5/7/15.
 */
public class ApiHeaders implements RequestInterceptor {

    // region Constructors
    public ApiHeaders(){
    }
    // endregion

    @Override
    public void intercept(RequestFacade request) {
        request.addQueryParam("client_id", "309011f9713d22ace9b976909ed34a80");
    }
}
