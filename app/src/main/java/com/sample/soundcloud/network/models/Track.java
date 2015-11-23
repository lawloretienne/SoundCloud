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
    private Long id;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("user_id")
    private Long userId;
    @SerializedName("duration")
    private Long duration;
    @SerializedName("commentable")
    private Boolean commentable;
    @SerializedName("state")
    private String state;
    @SerializedName("original_content_size")
    private Long originalContentSize;
    @SerializedName("last_modified")
    private String lastModified;
    @SerializedName("sharing")
    private String sharing;
    @SerializedName("tag_list")
    private String tagList;
    @SerializedName("permalink")
    private String permalink;
    @SerializedName("streamable")
    private Boolean streamable;
    @SerializedName("embeddable_by")
    private String embeddableBy;
    @SerializedName("downloadable")
    private Boolean downloadable;
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
    private Integer playbackCount;
    @SerializedName("download_count")
    private Integer downloadCount;
    @SerializedName("favoritingsCount")
    private Integer favoritingsCount;
    @SerializedName("comment_count")
    private Integer commentCount;
    @SerializedName("attachments_uri")
    private String attachmentsUri;
    @SerializedName("policy")
    private String policy;
    // endregion

    // region Getters

    public String getKind() {
        if (TextUtils.isEmpty(kind))
            return "";
        else
            return kind;
    }

    public Long getId() {
        if (id == null)
            return -1L;
        else
            return id;
    }

    public String getCreatedAt() {
        if (TextUtils.isEmpty(createdAt))
            return "";
        else
            return createdAt;
    }

    public Long getUserId() {
        if (userId == null)
            return -1L;
        else
            return userId;
    }

    public Long getDuration() {
        if (duration == null)
            return -1L;
        else
            return duration;
    }

    public Boolean isCommentable() {
        if (commentable == null)
            return false;
        else
            return commentable;
    }

    public String getState() {
        if (TextUtils.isEmpty(state))
            return "";
        else
            return state;
    }

    public Long getOriginalContentSize() {
        if (originalContentSize == null)
            return -1L;
        else
            return originalContentSize;
    }

    public String getLastModified() {
        if (TextUtils.isEmpty(lastModified))
            return "";
        else
            return lastModified;
    }

    public String getSharing() {
        if (TextUtils.isEmpty(sharing))
            return "";
        else
            return sharing;
    }

    public String getTagList() {
        if (TextUtils.isEmpty(tagList))
            return "";
        else
            return tagList;
    }

    public String getPermalink() {
        if (TextUtils.isEmpty(permalink))
            return "";
        else
            return permalink;
    }

    public Boolean isStreamable() {
        if (streamable == null)
            return false;
        else
            return streamable;
    }

    public String getEmbeddableBy() {
        if (TextUtils.isEmpty(embeddableBy))
            return "";
        else
            return embeddableBy;
    }

    public Boolean isDownloadable() {
        if (downloadable == null)
            return false;
        else
            return downloadable;
    }

    public String getPurchaseUrl() {
        if (TextUtils.isEmpty(purchaseUrl))
            return "";
        else
            return purchaseUrl;
    }

    public String getLabelId() {
        if (TextUtils.isEmpty(labelId))
            return "";
        else
            return labelId;
    }

    public String getPurchaseTitle() {
        if (TextUtils.isEmpty(purchaseTitle))
            return "";
        else
            return purchaseTitle;
    }

    public String getGenre() {
        if (TextUtils.isEmpty(genre))
            return "";
        else
            return genre;
    }

    public String getTitle() {
        if (TextUtils.isEmpty(title))
            return "";
        else
            return title;
    }

    public String getDescription() {
        if (TextUtils.isEmpty(description))
            return "";
        else
            return description;
    }

    public String getLabelName() {
        if (TextUtils.isEmpty(labelName))
            return "";
        else
            return labelName;
    }

    public String getRelease() {
        if (TextUtils.isEmpty(release))
            return "";
        else
            return release;
    }

    public String getTrackType() {
        if (TextUtils.isEmpty(trackType))
            return "";
        else
            return trackType;
    }

    public String getKeySignature() {
        if (TextUtils.isEmpty(keySignature))
            return "";
        else
            return keySignature;
    }

    public String getIsrc() {
        if (TextUtils.isEmpty(isrc))
            return "";
        else
            return isrc;
    }

    public String getVideoUrl() {
        if (TextUtils.isEmpty(videoUrl))
            return "";
        else
            return videoUrl;
    }

    public String getBpm() {
        if (TextUtils.isEmpty(bpm))
            return "";
        else
            return bpm;
    }

    public String getReleaseYear() {
        if (TextUtils.isEmpty(releaseYear))
            return "";
        else
            return releaseYear;
    }

    public String getReleaseMonth() {
        if (TextUtils.isEmpty(releaseMonth))
            return "";
        else
            return releaseMonth;
    }

    public String getReleaseDay() {
        if (TextUtils.isEmpty(releaseDay))
            return "";
        else
            return releaseDay;
    }

    public String getOriginalFormat() {
        if (TextUtils.isEmpty(originalFormat))
            return "";
        else
            return originalFormat;
    }

    public String getLicense() {
        if (TextUtils.isEmpty(license))
            return "";
        else
            return license;
    }

    public String getUri() {
        if (TextUtils.isEmpty(uri))
            return "";
        else
            return uri;
    }

    public UserProfile getUser() {
        return user;
    }

    public String getPermalinkUrl() {
        if (TextUtils.isEmpty(permalinkUrl))
            return "";
        else
            return permalinkUrl;
    }

    public String getArtworkUrl() {
        if (TextUtils.isEmpty(artworkUrl))
            return "";
        else
            return artworkUrl;
    }

    public String getWaveformUrl() {
        if (TextUtils.isEmpty(waveformUrl))
            return "";
        else
            return waveformUrl;
    }

    public String getStreamUrl() {
        if (TextUtils.isEmpty(streamUrl))
            return "";
        else
            return streamUrl;
    }

    public Integer getPlaybackCount() {
        if (playbackCount == null)
            return -1;
        else
            return playbackCount;
    }

    public Integer getDownloadCount() {
        if (downloadCount == null)
            return -1;
        else
            return downloadCount;
    }

    public Integer getFavoritingsCount() {
        if (favoritingsCount == null)
            return -1;
        else
            return favoritingsCount;
    }

    public Integer getCommentCount() {
        if (commentCount == null)
            return -1;
        else
            return commentCount;
    }

    public String getAttachmentsUri() {
        if (TextUtils.isEmpty(attachmentsUri))
            return "";
        else
            return attachmentsUri;
    }

    public String getPolicy() {
        if (TextUtils.isEmpty(policy))
            return "";
        else
            return policy;
    }
    //endregion

    // region Setters

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void setCommentable(Boolean commentable) {
        this.commentable = commentable;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setOriginalContentSize(Long originalContentSize) {
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

    public void setStreamable(Boolean streamable) {
        this.streamable = streamable;
    }

    public void setEmbeddableBy(String embeddableBy) {
        this.embeddableBy = embeddableBy;
    }

    public void setDownloadable(Boolean downloadable) {
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

    public void setPlaybackCount(Integer playbackCount) {
        this.playbackCount = playbackCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public void setFavoritingsCount(Integer favoritingsCount) {
        this.favoritingsCount = favoritingsCount;
    }

    public void setCommentCount(Integer commentCount) {
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
        return this.getId().intValue() *
                this.getCreatedAt().hashCode();
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