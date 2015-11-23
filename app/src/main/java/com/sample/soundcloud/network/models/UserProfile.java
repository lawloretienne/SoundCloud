package com.sample.soundcloud.network.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.sample.soundcloud.realm.models.RealmUserProfile;

import timber.log.Timber;

/**
 * Created by etiennelawlor on 3/21/15.
 */

public final class UserProfile {

//  Sample JSON
//    {
//        id: 856062,
//                kind: "user",
//            permalink: "skrillex",
//            username: "Skrillex",
//            last_modified: "2015/04/08 18:32:29 +0000",
//            uri: "https://api.soundcloud.com/users/856062",
//            permalink_url: "http://soundcloud.com/skrillex",
//            avatar_url: "https://i1.sndcdn.com/avatars-000138955484-r3yxsu-large.jpg",
//            country: "United States",
//            first_name: "Sonny",
//            last_name: "Moore",
//            full_name: "Sonny Moore",
//            description: "27 year old producer who likes to make all kinds of music",
//            city: "Los Angeles",
//            discogs_name: null,
//            myspace_name: "skrillex",
//            website: "http://skrillex.com/",
//            website_title: "",
//            online: false,
//            trackCount: 82,
//            playlist_count: 13,
//            plan: "Pro Plus",
//            public_favorites_count: 2,
//            followers_count: 5278816,
//            followings_count: 90,
//            subscriptions: [
//        {
//            product: {
//                id: "creator-pro-unlimited",
//                        name: "Pro Unlimited"
//            }
//        }
//        ]
//    }

    // region Fields
    @SerializedName("id")
    private Long id;
    @SerializedName("kind")
    private String kind;
    @SerializedName("permalink")
    private String permalink;
    @SerializedName("username")
    private String username;
    @SerializedName("last_modified")
    private String lastModified;
    @SerializedName("uri")
    private String uri;
    @SerializedName("permalink_url")
    private String permalinkUrl;
    @SerializedName("avatar_url")
    private String avatarUrl;
    @SerializedName("country")
    private String country;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("description")
    private String description;
    @SerializedName("city")
    private String city;
    @SerializedName("discogs_name")
    private String discogsName;
    @SerializedName("myspace_name")
    private String myspaceName;
    @SerializedName("website")
    private String website;
    @SerializedName("website_title")
    private String websiteTitle;
    @SerializedName("online")
    private Boolean online;
    @SerializedName("track_count")
    private Integer trackCount;
    @SerializedName("playlist_count")
    private Integer playlistCount;
    @SerializedName("plan")
    private String plan;
    @SerializedName("public_favorites_count")
    private Integer publicFavoritesCount;
    @SerializedName("followers_count")
    private Integer followersCount;
    @SerializedName("followings_count")
    private Integer followingsCount;
//    @SerializedName("subscriptions")
//    private List<Subscription> subscriptions;

    // endregion

    // region Getters

    public Long getId() {
        if (id == null)
            return -1L;
        else
            return id;
    }

    public String getKind() {
        if (TextUtils.isEmpty(kind))
            return "";
        else
            return kind;
    }

    public String getPermalink() {
        if (TextUtils.isEmpty(permalink))
            return "";
        else
            return permalink;
    }

    public String getUsername() {
        if (TextUtils.isEmpty(username))
            return "";
        else
            return username;
    }

    public String getLastModified() {
        if (TextUtils.isEmpty(lastModified))
            return "";
        else
            return lastModified;
    }

    public String getUri() {
        if (TextUtils.isEmpty(uri))
            return "";
        else
            return uri;
    }

    public String getPermalinkUrl() {
        if (TextUtils.isEmpty(permalinkUrl))
            return "";
        else
            return permalinkUrl;
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

    public String getFirstName() {
        if (TextUtils.isEmpty(firstName))
            return "";
        else
            return firstName;
    }

    public String getLastName() {
        if (TextUtils.isEmpty(lastName))
            return "";
        else
            return lastName;
    }

    public String getFullName() {
        if (TextUtils.isEmpty(fullName))
            return "";
        else
            return fullName;
    }

    public String getDescription() {
        if (TextUtils.isEmpty(description))
            return "";
        else
            return description;
    }

    public String getCity() {
        if (TextUtils.isEmpty(city))
            return "";
        else
            return city;
    }

    public String getDiscogsName() {
        if (TextUtils.isEmpty(discogsName))
            return "";
        else
            return discogsName;
    }

    public String getMyspaceName() {
        if (TextUtils.isEmpty(myspaceName))
            return "";
        else
            return myspaceName;
    }

    public String getWebsite() {
        if (TextUtils.isEmpty(website))
            return "";
        else
            return website;
    }

    public String getWebsiteTitle() {
        if (TextUtils.isEmpty(websiteTitle))
            return "";
        else
            return websiteTitle;
    }

    public Boolean isOnline() {
        if (online == null)
            return false;
        else
            return online;
    }

    public Integer getTrackCount() {
        if (trackCount == null)
            return -1;
        else
            return trackCount;
    }

    public Integer getPlaylistCount() {
        if (playlistCount == null)
            return -1;
        else
            return playlistCount;
    }

    public String getPlan() {
        if (TextUtils.isEmpty(plan))
            return "";
        else
            return plan;
    }

    public Integer getPublicFavoritesCount() {
        if (publicFavoritesCount == null)
            return -1;
        else
            return publicFavoritesCount;
    }

    public Integer getFollowersCount() {
        if (followersCount == null)
            return -1;
        else
            return followersCount;
    }

    public Integer getFollowingsCount() {
        if (followingsCount == null)
            return -1;
        else
            return followingsCount;
    }

//    public List<Subscription> getSubscriptions() {
//        return subscriptions;
//    }
    // endregion

    // region Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDiscogsName(String discogsName) {
        this.discogsName = discogsName;
    }

    public void setMyspaceName(String myspaceName) {
        this.myspaceName = myspaceName;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setWebsiteTitle(String websiteTitle) {
        this.websiteTitle = websiteTitle;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public void setTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
    }

    public void setPlaylistCount(Integer playlistCount) {
        this.playlistCount = playlistCount;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setPublicFavoritesCount(Integer publicFavoritesCount) {
        this.publicFavoritesCount = publicFavoritesCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public void setFollowingsCount(Integer followingsCount) {
        this.followingsCount = followingsCount;
    }

//    public void setSubscriptions(List<Subscription> subscriptions) {
//        this.subscriptions = subscriptions;
//    }

    // endregion

    @Override
    public int hashCode() {
        return this.getUsername().hashCode() *
                this.getAvatarUrl().hashCode() *
                this.getCountry().hashCode() *
                this.getCity().hashCode() *
                this.getTrackCount() *
                this.getPlaylistCount() *
                this.getFollowersCount();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RealmUserProfile)) {
            return false;
        }

        Timber.d("Soundcloud : equals() : this.getFollowersCount() - " + this.getFollowersCount());
        Timber.d("Soundcloud : equals() : ((com.sample.soundcloud.realm.models.UserProfile)obj).getFollowersCount() - "
                + ((RealmUserProfile) obj).getFollowersCount());

        return this.getUsername().equals(((RealmUserProfile) obj).getUsername())
                && this.getAvatarUrl().equals(((RealmUserProfile) obj).getAvatarUrl())
                && this.getCountry().equals(((RealmUserProfile) obj).getCountry())
                && this.getCity().equals(((RealmUserProfile) obj).getCity())
                && this.getTrackCount() == ((RealmUserProfile) obj).getTrackCount()
                && this.getPlaylistCount() == ((RealmUserProfile) obj).getPlaylistCount()
                && this.getFollowersCount() == ((RealmUserProfile) obj).getFollowersCount();
    }
}