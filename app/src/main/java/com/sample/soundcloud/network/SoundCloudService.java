package com.sample.soundcloud.network;


import com.sample.soundcloud.network.models.Track;
import com.sample.soundcloud.network.models.UserProfile;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

public interface SoundCloudService {

    String BASE_URL = "http://api.soundcloud.com";

    @Headers("Accept: application/json")
    @GET("/users/{username}.json")
    Observable<UserProfile> getUserProfile(@Path("username") String username);

    @Headers("Accept: application/json")
    @GET("/users/{username}/favorites.json")
    Observable<List<Track>> getFavoriteTracks(@Path("username") String username);

    @GET("/tracks/{trackId}/stream")
    Call<ResponseBody> getStreamInfo(@Path("trackId") long trackId);

}