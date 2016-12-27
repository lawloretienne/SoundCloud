package com.sample.soundcloud.network.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.sample.soundcloud.realm.models.RealmTrack;

/**
 * Created by etiennelawlor on 3/21/15.
 */

public final class Track {

//  Sample JSON
//    {
//        kind: "track",
//                id: 196156839,
//            created_at: "2015/03/16 16:02:37 +0000",
//            user_id: 2709976,
//            duration: 267518,
//            commentable: true,
//            state: "finished",
//            original_content_size: 47176824,
//            last_modified: "2015/04/13 04:34:29 +0000",
//            sharing: "public",
//            tag_list: "Tiesto KSHMR Vassy",
//            permalink: "tiesto-kshmr-feat-vassy-secretsoriginal-mix",
//            streamable: true,
//            embeddable_by: "all",
//            downloadable: false,
//            purchase_url: "http://btprt.dj/1BJ70Uz",
//            label_id: null,
//            purchase_title: "Buy on Beatport",
//            genre: "Musical Freedom",
//            title: "Tiesto & KSHMR feat. VASSY - Secrets(Original Mix)[OUT NOW]",
//            description: "Available now on Beatport via Musical Freedom, pick up "Secrets" from Tiësto & KSHMR featuring VASSY. Purchase LinK: http://btprt.dj/1BJ70Uz Connect with Tiësto: www.tiesto.com www.facebook.com/tiesto www.twitter.com/tiesto www.instagram.com/tiesto www.soundcloud.com/tiesto Connect with KSHMR: www.facebook.com/KSHMRmusic www.twitter.com/kshmrmusic www.instagram.com/KSHMR Connect with VASSY: www.facebook.com/vassy www.twitter.com/vassy www.instagram.com/vassy",
//            label_name: "Musical Freedom",
//            release: null,
//            track_type: null,
//            key_signature: null,
//            isrc: null,
//            video_url: null,
//            bpm: null,
//            release_year: null,
//            release_month: null,
//            release_day: null,
//            original_format: "wav",
//            license: "all-rights-reserved",
//            uri: "https://api.soundcloud.com/tracks/196156839",
//            user: {},
//        permalink_url: "http://soundcloud.com/musical-freedom/tiesto-kshmr-feat-vassy-secretsoriginal-mix",
//                artwork_url: "https://i1.sndcdn.com/artworks-000110214611-yz333i-large.jpg",
//            waveform_url: "https://w1.sndcdn.com/N4yZzDb2idA2_m.png",
//            stream_url: "https://api.soundcloud.com/tracks/196156839/stream",
//            playback_count: 2176835,
//            download_count: 0,
//            favoritingsCount: 75038,
//            comment_count: 943,
//            attachments_uri: "https://api.soundcloud.com/tracks/196156839/attachments",
//            policy: "ALLOW"
//    }

    // region Fields
    @SerializedName("kind")
    private String kind;
    @SerializedName("id")
    private long id;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("user_id")
    private long userId;
    @SerializedName("duration")
    private long duration;
    @SerializedName("commentable")
    private boolean commentable;
    @SerializedName("state")
    private String state;
    @SerializedName("original_content_size")
    private long originalContentSize;
    @SerializedName("last_modified")
    private String lastModified;
    @SerializedName("sharing")
    private String sharing;
    @SerializedName("tag_list")
    private String tagList;
    @SerializedName("permalink")
    private String permalink;
    @SerializedName("streamable")
    private boolean streamable;
    @SerializedName("embeddable_by")
    private String embeddableBy;
    @SerializedName("downloadable")
    private boolean downloadable;
    @SerializedName("purchase_url")
    private String purchaseUrl;
    @SerializedName("label_id")
    private String labelId;
    @SerializedName("purchase_title")
    private String purchaseTitle;
    @SerializedName("genre")
    private String genre;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("label_name")
    private String labelName;
    @SerializedName("release")
    private String release;
    @SerializedName("track_type")
    private String trackType;
    @SerializedName("key_signature")
    private String keySignature;
    @SerializedName("isrc")
    private String isrc;
    @SerializedName("video_url")
    private String videoUrl;
    @SerializedName("bpm")
    private String bpm;
    @SerializedName("release_year")
    private String releaseYear;
    @SerializedName("release_month")
    private String releaseMonth;
    @SerializedName("release_day")
    private String releaseDay;
    @SerializedName("original_format")
    private String originalFormat;
    @SerializedName("license")
    private String license;
    @SerializedName("uri")
    private String uri;
    @SerializedName("user")
    private UserProfile user;
    @SerializedName("permalink_url")
    private String permalinkUrl;
    @SerializedName("artwork_url")
    private String artworkUrl;
    @SerializedName("waveform_url")
    private String waveformUrl;
    @SerializedName("stream_url")
    private String streamUrl;
    @SerializedName("playback_count")
    private int playbackCount;
    @SerializedName("download_count")
    private int downloadCount;
    @SerializedName("favoritingsCount")
    private int favoritingsCount;
    @SerializedName("comment_count")
    private int commentCount;
    @SerializedName("attachments_uri")
    private String attachmentsUri;
    @SerializedName("policy")
    private String policy;
    // endregion

    // region Getters

    public String getKind() {
        return kind;
    }

    public long getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public long getUserId() {
        return userId;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isCommentable() {
        return commentable;
    }

    public String getState() {
        return state;
    }

    public long getOriginalContentSize() {
        return originalContentSize;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getSharing() {
        return sharing;
    }

    public String getTagList() {
        return tagList;
    }

    public String getPermalink() {
        return permalink;
    }

    public boolean isStreamable() {
        return streamable;
    }

    public String getEmbeddableBy() {
        return embeddableBy;
    }

    public boolean isDownloadable() {
        return downloadable;
    }

    public String getPurchaseUrl() {
        return purchaseUrl;
    }

    public String getLabelId() {
        return labelId;
    }

    public String getPurchaseTitle() {
        return purchaseTitle;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLabelName() {
        return labelName;
    }

    public String getRelease() {
        return release;
    }

    public String getTrackType() {
        return trackType;
    }

    public String getKeySignature() {
        return keySignature;
    }

    public String getIsrc() {
        return isrc;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getBpm() {
        return bpm;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getReleaseMonth() {
        return releaseMonth;
    }

    public String getReleaseDay() {
        return releaseDay;
    }

    public String getOriginalFormat() {
        return originalFormat;
    }

    public String getLicense() {
        return license;
    }

    public String getUri() {
        return uri;
    }

    public UserProfile getUser() {
        return user;
    }

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public String getWaveformUrl() {
        return waveformUrl;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public int getPlaybackCount() {
        return playbackCount;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public int getFavoritingsCount() {
        return favoritingsCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public String getAttachmentsUri() {
        return attachmentsUri;
    }

    public String getPolicy() {
        return policy;
    }
    //endregion

    // region Setters

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setCommentable(boolean commentable) {
        this.commentable = commentable;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setOriginalContentSize(long originalContentSize) {
        this.originalContentSize = originalContentSize;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public void setSharing(String sharing) {
        this.sharing = sharing;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public void setStreamable(boolean streamable) {
        this.streamable = streamable;
    }

    public void setEmbeddableBy(String embeddableBy) {
        this.embeddableBy = embeddableBy;
    }

    public void setDownloadable(boolean downloadable) {
        this.downloadable = downloadable;
    }

    public void setPurchaseUrl(String purchaseUrl) {
        this.purchaseUrl = purchaseUrl;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public void setPurchaseTitle(String purchaseTitle) {
        this.purchaseTitle = purchaseTitle;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public void setTrackType(String trackType) {
        this.trackType = trackType;
    }

    public void setKeySignature(String keySignature) {
        this.keySignature = keySignature;
    }

    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setBpm(String bpm) {
        this.bpm = bpm;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setReleaseMonth(String releaseMonth) {
        this.releaseMonth = releaseMonth;
    }

    public void setReleaseDay(String releaseDay) {
        this.releaseDay = releaseDay;
    }

    public void setOriginalFormat(String originalFormat) {
        this.originalFormat = originalFormat;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
    }

    public void setWaveformUrl(String waveformUrl) {
        this.waveformUrl = waveformUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public void setPlaybackCount(int playbackCount) {
        this.playbackCount = playbackCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public void setFavoritingsCount(int favoritingsCount) {
        this.favoritingsCount = favoritingsCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void setAttachmentsUri(String attachmentsUri) {
        this.attachmentsUri = attachmentsUri;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }
    // endregion

    @Override
    public int hashCode() {
        return (int)(getId() * getCreatedAt().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RealmTrack)) {
            return false;
        }

        return this.getId() == ((RealmTrack) obj).getId()
                && this.getCreatedAt().equals(((RealmTrack) obj).getCreatedAt());
    }
}