package com.sample.soundcloud.realm.models;

import android.text.TextUtils;

import io.realm.RealmObject;

/**
 * Created by etiennelawlor on 4/18/15.
 */
public class Track extends RealmObject {

    // region Member Variables
    private long id;
    private String createdAt;
    private long duration;
    private String title;
    private UserProfile user;
    private String artworkUrl;
    private String streamUrl;
    private int playbackCount;
    // endregion

    // region Getters
    public long getId() {
        return id;
    }

    public String getCreatedAt() {
        if (TextUtils.isEmpty(createdAt))
            return "";
        else
            return createdAt;
    }

    public long getDuration() {
        return duration;
    }

    public String getTitle() {
        if (TextUtils.isEmpty(title))
            return "";
        else
            return title;
    }

    public UserProfile getUser() {
        return user;
    }

    public String getArtworkUrl() {
        if (TextUtils.isEmpty(artworkUrl))
            return "";
        else
            return artworkUrl;
    }

    public String getStreamUrl() {
        if (TextUtils.isEmpty(streamUrl))
            return "";
        else
            return streamUrl;
    }

    public int getPlaybackCount() {
        return playbackCount;
    }

    // endregion

    // region Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public void setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public void setPlaybackCount(int playbackCount) {
        this.playbackCount = playbackCount;
    }

    // endregion
}
