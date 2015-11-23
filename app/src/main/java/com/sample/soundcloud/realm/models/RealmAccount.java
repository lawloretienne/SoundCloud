package com.sample.soundcloud.realm.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by etiennelawlor on 4/18/15.
 */
public class RealmAccount extends RealmObject {

    // region Member Variables
    private RealmUserProfile userProfile;
    private RealmList<RealmTrack> tracks;
    // endregion

    // region Getters
    public RealmUserProfile getUserProfile() {
        return userProfile;
    }

    public RealmList<RealmTrack> getTracks() {
        return tracks;
    }
    // endregion

    // region Setters
    public void setUserProfile(RealmUserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void setTracks(RealmList<RealmTrack> tracks) {
        this.tracks = tracks;
    }
    // endregion
}
