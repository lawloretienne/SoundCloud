package com.sample.soundcloud.network.models;

import java.util.List;

import io.realm.RealmList;

/**
 * Created by etiennelawlor on 4/17/15.
 */
public class Account {

    // region Member Variables
    private UserProfile mUserProfile;
    private List<Track> mTracks;
    // endregion

    // region Constructors
    public Account(UserProfile userProfile, List<Track> tracks) {
        mUserProfile = userProfile;
        mTracks = tracks;
    }
    // endregion

    // region Getters
    public UserProfile getUserProfile() {
        return mUserProfile;
    }

    public List<Track> getTracks() {
        return mTracks;
    }
    // endregion

    // region Setters
    public void setUserProfile(UserProfile userProfile) {
        this.mUserProfile = userProfile;
    }

    public void setTracks(List<Track> tracks) {
        this.mTracks = tracks;
    }
    // endregion

    @Override
    public int hashCode() {
        return this.getUserProfile().hashCode() *
                this.getTracks().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof com.sample.soundcloud.realm.models.Account)) {
            return false;
        }

        RealmList<com.sample.soundcloud.realm.models.Track> realmTracks = ((com.sample.soundcloud.realm.models.Account) obj).getTracks();

        if (realmTracks.size() != this.getTracks().size()) {
            return false;
        }

        int trackCount = this.getTracks().size();
        for (int i = 0; i < trackCount; i++) {
            Track a = getTracks().get(i);
            com.sample.soundcloud.realm.models.Track b = realmTracks.get(i);

            if (!(a.equals(b))) {
                return false;
            }
        }

        if (!(this.getUserProfile().equals(((com.sample.soundcloud.realm.models.Account) obj).getUserProfile()))) {
            return false;
        }

        return true;
    }
}
