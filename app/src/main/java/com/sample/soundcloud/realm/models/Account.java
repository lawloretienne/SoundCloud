package com.sample.soundcloud.realm.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by etiennelawlor on 4/18/15.
 */
public class Account extends RealmObject {

    // region Member Variables
    private UserProfile userProfile;
    private RealmList<Track> tracks;
    // endregion

    // region Getters
    public UserProfile getUserProfile() {
        return userProfile;
    }

    public RealmList<Track> getTracks() {
        return tracks;
    }
    // endregion

    // region Setters
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void setTracks(RealmList<Track> tracks) {
        this.tracks = tracks;
    }
    // endregion
}
