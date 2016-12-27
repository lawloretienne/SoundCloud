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
    private long id;
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
    private boolean online;
    @SerializedName("track_count")
    private int trackCount;
    @SerializedName("playlist_count")
    private int playlistCount;
    @SerializedName("plan")
    private String plan;
    @SerializedName("public_favorites_count")
    private int publicFavoritesCount;
    @SerializedName("followers_count")
    private int followersCount;
    @SerializedName("followings_count")
    private int followingsCount;
//    @SerializedName("subscriptions")
//    private List<Subscription> subscriptions;

    // endregion

    // region Getters

    public long getId() {
        return id;
    }

    public String getKind() {
        return kind;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getUsername() {
        return username;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getUri() {
        return uri;
    }

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getCountry() {
        return country;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return city;
    }

    public String getDiscogsName() {
        return discogsName;
    }

    public String getMyspaceName() {
        return myspaceName;
    }

    public String getWebsite() {
        return website;
    }

    public String getWebsiteTitle() {
        return websiteTitle;
    }

    public boolean isOnline() {
        return online;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public int getPlaylistCount() {
        return playlistCount;
    }

    public String getPlan() {
        return plan;
    }

    public int getPublicFavoritesCount() {
        return publicFavoritesCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingsCount() {
        return followingsCount;
    }

//    public List<Subscription> getSubscriptions() {
//        return subscriptions;
//    }
    // endregion

    // region Setters

    public void setId(long id) {
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

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public void setPlaylistCount(int playlistCount) {
        this.playlistCount = playlistCount;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setPublicFavoritesCount(int publicFavoritesCount) {
        this.publicFavoritesCount = publicFavoritesCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setFollowingsCount(int followingsCount) {
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