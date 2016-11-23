package com.sample.soundcloud.models;

import com.sample.soundcloud.network.models.Track;
import com.sample.soundcloud.network.models.UserProfile;
import com.sample.soundcloud.realm.models.RealmAccount;
import com.sample.soundcloud.realm.models.RealmTrack;

import java.util.List;

import io.realm.RealmList;

/**
 * Created by etiennelawlor on 4/17/15.
 */
public class Account {

    // region Member Variables
    private UserProfile userProfile;
    private List<Track> tracks;
    // endregion

    // region Constructors
    public Account(UserProfile userProfile, List<Track> tracks) {
        this.userProfile = userProfile;
        this.tracks = tracks;
    }
    // endregion

    // region Getters
    public UserProfile getUserProfile() {
        return userProfile;
    }

    public List<Track> getTracks() {
        return tracks;
    }
    // endregion

    // region Setters
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
    // endregion

    @Override
    public int hashCode() {
        return this.getUserProfile().hashCode() *
                this.getTracks().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RealmAccount)) {
            return false;
        }

        RealmList<RealmTrack> realmTracks = ((RealmAccount) obj).getTracks();

        if (realmTracks.size() != this.getTracks().size()) {
            return false;
        }

        int trackCount = this.getTracks().size();
        for (int i = 0; i < trackCount; i++) {
            Track a = getTracks().get(i);
            RealmTrack b = realmTracks.get(i);

            if (!(a.equals(b))) {
                return false;
            }
        }

        if (!(this.getUserProfile().equals(((RealmAccount) obj).getUserProfile()))) {
            return false;
        }

        return true;
    }
}
