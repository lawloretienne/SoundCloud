package com.sample.soundcloud.realm;

import android.content.Context;

import com.sample.soundcloud.SoundcloudApplication;
import com.sample.soundcloud.models.Account;
import com.sample.soundcloud.network.models.Track;
import com.sample.soundcloud.network.models.UserProfile;
import com.sample.soundcloud.realm.models.RealmAccount;
import com.sample.soundcloud.realm.models.RealmTrack;
import com.sample.soundcloud.realm.models.RealmUserProfile;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by etiennelawlor on 11/22/15.
 */
public class RealmUtility {

    private static Realm mRealm;

    public static RealmConfiguration getRealmConfiguration(Context context) {
//        return new RealmConfiguration.Builder(context)
//                .name("loop.realm")
//                .schemaVersion(1)
//                .build();

        return new RealmConfiguration.Builder(context).build();
    }

    public static RealmAccount getCachedAccount(){
        Context context = SoundcloudApplication.getInstance().getApplicationContext();
        try{
            mRealm = Realm.getInstance(context);
        } catch (RealmMigrationNeededException e) {
            Realm.deleteRealm(getRealmConfiguration(context));
            mRealm = Realm.getInstance(context);
        }

        RealmResults<RealmAccount> realmResults
                = mRealm.where(RealmAccount.class).findAll();

        mRealm.close();

        if(realmResults != null && realmResults.size() > 0){
            return realmResults.get(0);
        } else {
            return null;
        }
    }

    public static boolean isAccountCached() {
        Context context = SoundcloudApplication.getInstance().getApplicationContext();
        try{
            mRealm = Realm.getInstance(context);
        } catch (RealmMigrationNeededException e) {
            Realm.deleteRealm(getRealmConfiguration(context));
            mRealm = Realm.getInstance(context);
        }
        
        RealmResults<RealmAccount> realmResults
                = mRealm.where(RealmAccount.class).findAll();

        mRealm.close();

        if (realmResults != null && realmResults.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void persistAccount(Account account) {
        Context context = SoundcloudApplication.getInstance().getApplicationContext();
        try{
            mRealm = Realm.getInstance(context);
        } catch (RealmMigrationNeededException e) {
            Realm.deleteRealm(getRealmConfiguration(context));
            mRealm = Realm.getInstance(context);
        }

        UserProfile userProfile = account.getUserProfile();
        List<Track> tracks = account.getTracks();

        mRealm.beginTransaction();

        mRealm.clear(RealmAccount.class);

        RealmAccount realmAccount =
                mRealm.createObject(RealmAccount.class);

        RealmUserProfile realmUserProfile =
                mRealm.createObject(RealmUserProfile.class);

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
                    = mRealm.createObject(RealmTrack.class);
            realmTrack.setArtworkUrl(track.getArtworkUrl());
            realmTrack.setStreamUrl(track.getStreamUrl());
            realmTrack.setDuration(track.getDuration());
            realmTrack.setId(track.getId());
            realmTrack.setCreatedAt(track.getCreatedAt());
            realmTrack.setPlaybackCount(track.getPlaybackCount());
            realmTrack.setTitle(track.getTitle());

            RealmUserProfile realmUser
                    = mRealm.createObject(RealmUserProfile.class);
            realmUser.setUsername(track.getUser().getUsername());
            realmTrack.setUser(realmUser);

            realmTracks.add(realmTrack);
        }

        realmAccount.setTracks(realmTracks);

        mRealm.copyToRealm(realmAccount);
        mRealm.commitTransaction();

        mRealm.close();
    }
}
