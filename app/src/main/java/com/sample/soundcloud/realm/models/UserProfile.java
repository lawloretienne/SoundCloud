package com.sample.soundcloud.realm.models;

import android.text.TextUtils;

import io.realm.RealmObject;

/**
 * Created by etiennelawlor on 4/18/15.
 */
public class UserProfile extends RealmObject {

    //region Member Variables
    private String username;
    private String avatarUrl;
    private String country;
    private String city;
    private int trackCount;
    private int playlistCount;
    private int followersCount;
    //endregion

    //region Getters

    public String getUsername() {
        if (TextUtils.isEmpty(username))
            return "";
        else
            return username;
    }

    public String getAvatarUrl() {
        if (TextUtils.isEmpty(avatarUrl))
            return "";
        else
            return avatarUrl;
    }

    public String getCountry() {
        if (TextUtils.isEmpty(country))
            return "";
        else
            return country;
    }

    public String getCity() {
        if (TextUtils.isEmpty(city))
            return "";
        else
            return city;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public int getPlaylistCount() {
        return playlistCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }
    //endregion

    // region Setters

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public void setPlaylistCount(int playlistCount) {
        this.playlistCount = playlistCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    // endregion
}
