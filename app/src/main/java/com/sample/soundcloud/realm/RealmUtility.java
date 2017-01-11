package com.sample.soundcloud.realm;

import com.sample.soundcloud.models.Account;
import com.sample.soundcloud.network.models.Track;
import com.sample.soundcloud.network.models.UserProfile;
import com.sample.soundcloud.realm.models.RealmAccount;
import com.sample.soundcloud.realm.models.RealmTrack;
import com.sample.soundcloud.realm.models.RealmUserProfile;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by etiennelawlor on 11/22/15.
 */
public class RealmUtility {

    public static RealmAccount getCachedAccount(){
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<RealmAccount> realmResults
                    = realm.where(RealmAccount.class).findAll();
            if(realmResults != null && realmResults.isValid() && realmResults.size() > 0){
                return realmResults.get(0);
            } else {
                return null;
            }
        } finally {
            realm.close();
        }
    }

    public static boolean isAccountCached() {
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<RealmAccount> realmResults
                    = realm.where(RealmAccount.class).findAll();
            if (realmResults != null && realmResults.size() > 0) {
                return true;
            } else {
                return false;
            }
        } finally {
            realm.close();
        }
    }

    public static void persistAccount(Account account) {
        Realm realm = Realm.getDefaultInstance();
        try {
            UserProfile userProfile = account.getUserProfile();
            List<Track> tracks = account.getTracks();

            realm.beginTransaction();

            realm.delete(RealmAccount.class);

            RealmAccount realmAccount =
                    realm.createObject(RealmAccount.class);

            RealmUserProfile realmUserProfile =
                    realm.createObject(RealmUserProfile.class);

            realmUserProfile.setAvatarUrl(userProfile.getAvatarUrl());
            realmUserProfile.setCity(userProfile.getCity());
            realmUserProfile.setCountry(userProfile.getCountry());
            realmUserProfile.setFollowersCount(userProfile.getFollowersCount());
            realmUserProfile.setPlaylistCount(userProfile.getPlaylistCount());
            realmUserProfile.setTrackCount(userProfile.getTrackCount());
            realmUserProfile.setUsername(userProfile.getUsername());

            realmAccount.setUserProfile(realmUserProfile);

            RealmList<RealmTrack> realmTracks = new RealmList<>();
            for (Track track : tracks) {
                RealmTrack realmTrack
                        = realm.createObject(RealmTrack.class);
                realmTrack.setArtworkUrl(track.getArtworkUrl());
                realmTrack.setStreamUrl(track.getStreamUrl());
                realmTrack.setDuration(track.getDuration());
                realmTrack.setId(track.getId());
                realmTrack.setCreatedAt(track.getCreatedAt());
                realmTrack.setPlaybackCount(track.getPlaybackCount());
                realmTrack.setTitle(track.getTitle());

                RealmUserProfile realmUser
                        = realm.createObject(RealmUserProfile.class);
                realmUser.setUsername(track.getUser().getUsername());
                realmTrack.setUser(realmUser);

                realmTracks.add(realmTrack);
            }

            realmAccount.setTracks(realmTracks);

            realm.copyToRealm(realmAccount);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }
}
