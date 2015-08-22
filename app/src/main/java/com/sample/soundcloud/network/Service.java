package com.sample.soundcloud.network;


import com.sample.soundcloud.network.models.Track;
import com.sample.soundcloud.network.models.UserProfile;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import rx.Observable;

public interface Service {

    @Headers("Accept: application/json")
    @GET("/users/{username}.json")
    Observable<UserProfile> getUserProfile(@Path("username") String username);

    @Headers("Accept: application/json")
    @GET("/users/{username}.json")
    UserProfile getUserProfileSynchronous(@Path("username") String username);

    @Headers("Accept: application/json")
    @GET("/users/{username}/favorites.json")
    Observable<List<Track>> getFavoriteTracks(@Path("username") String username);

    @Headers("Accept: application/json")
    @GET("/users/{username}/favorites.json")
    List<Track> getFavoriteTracksSynchronous(@Path("username") String username);

    @GET("/tracks/{trackId}/stream")
    void getStreamInfo(@Path("trackId") long trackId, Callback<Response> cb);

}