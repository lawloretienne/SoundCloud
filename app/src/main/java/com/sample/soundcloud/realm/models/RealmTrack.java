package com.sample.soundcloud.realm.models;

import android.text.TextUtils;

import java.text.NumberFormat;
import java.util.Locale;

import io.realm.RealmObject;

/**
 * Created by etiennelawlor on 4/18/15.
 */
public class RealmTrack extends RealmObject {

    // region Member Variables
    private long id;
    private String createdAt;
    private long duration;
    private String title;
    private RealmUserProfile user;
    private String artworkUrl;
    private String streamUrl;
    private int playbackCount;
    // endregion

    // region Getters
    public long getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public long getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

    public RealmUserProfile getUser() {
        return user;
    }

    public String getArtworkUrl() {
        String formattedArtworkUrl =  "";
        if(!TextUtils.isEmpty(artworkUrl)){
            formattedArtworkUrl = artworkUrl;
            formattedArtworkUrl = formattedArtworkUrl.replace("large.jpg", "t500x500.jpg");
        }
        return formattedArtworkUrl;
    }

    public String getStreamUrl() {
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

    public void setUser(RealmUserProfile user) {
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
