package com.sample.soundcloud.syncadapters;

/**
 * Created by etiennelawlor on 5/1/15.
 */


import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.sample.soundcloud.SoundcloudConstants;
import com.sample.soundcloud.network.Api;
import com.sample.soundcloud.network.models.Track;
import com.sample.soundcloud.network.models.UserProfile;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;
import retrofit.RetrofitError;
import timber.log.Timber;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class AccountSyncAdapter extends AbstractThreadedSyncAdapter {

    // region Member Variables
    private Realm mRealm;
    private Context mContext;
    // endregion

    // region Constructors
    /**
     * Set up the sync adapter
     */
    public AccountSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        mContext = context;
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public AccountSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        mContext = context;
    }
    // endregion

    /*
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */
    @Override
    public void onPerformSync(
            Account account,
            Bundle bundle,
            String s,
            ContentProviderClient contentProviderClient,
            SyncResult syncResult) {

        // Redownload account info
        try {
            mRealm = Realm.getInstance(mContext);
            loadAccount();
            mRealm.close();
        } catch (RealmMigrationNeededException e) {
            // in this case you need migration.
            // https://github.com/realm/realm-java/tree/master/examples/migrationExample
        }

    }

    // region Helper Methods
    private void loadAccount(){
        Timber.d("Soundcloud : loadAccount()");

        try {

            UserProfile userProfile = Api.getService(Api.getEndpointUrl()).getUserProfileSynchronous(SoundcloudConstants.USERNAME);
            List<Track> tracks = Api.getService(Api.getEndpointUrl()).getFavoriteTracksSynchronous(SoundcloudConstants.USERNAME);

            com.sample.soundcloud.network.models.Account account = new com.sample.soundcloud.network.models.Account(userProfile, tracks);

            com.sample.soundcloud.realm.models.Account cachedAccount = getCachedAccount();
            if ((cachedAccount != null  && !account.equals(cachedAccount)
                        || cachedAccount == null)) {
                // Account has changed or loaded for the first time
                persistAccount(account);
            } else {
                // No changes to the account
            }
        } catch (Exception e) {
            Timber.e(e, "Soundcloud error");

            if(e instanceof RetrofitError){
                RetrofitError.Kind errorKind = ((RetrofitError) e).getKind();

                Timber.e(e, "Soundcloud error : errorMessage - "+getErrorMessage(errorKind));

            }

        }
    }

    private String getErrorMessage(RetrofitError.Kind errorKind) {
        String errorMessage = "";
        switch (errorKind) {
            case NETWORK:
//                                    errorMessage = "Network Error";
                errorMessage = "Can't load data.\nCheck your network connection.";
                break;
            case HTTP:
                errorMessage = "HTTP Error";
                break;
            case UNEXPECTED:
                errorMessage = "Unexpected Error";
                break;
            case CONVERSION:
                errorMessage = "Conversion Error";
                break;
            default:
                break;
        }

        return errorMessage;
    }

    private void persistAccount(com.sample.soundcloud.network.models.Account account){
        UserProfile userProfile = account.getUserProfile();
        List<Track> tracks = account.getTracks();

        mRealm.beginTransaction();

        mRealm.clear(com.sample.soundcloud.network.models.Account.class);

        com.sample.soundcloud.realm.models.Account realmAccount =
                mRealm.createObject(com.sample.soundcloud.realm.models.Account.class);

        com.sample.soundcloud.realm.models.UserProfile realmUserProfile =
                mRealm.createObject(com.sample.soundcloud.realm.models.UserProfile.class);

        realmUserProfile.setAvatarUrl(userProfile.getAvatarUrl());
        realmUserProfile.setCity(userProfile.getCity());
        realmUserProfile.setCountry(userProfile.getCountry());
        realmUserProfile.setFollowersCount(userProfile.getFollowersCount());
        realmUserProfile.setPlaylistCount(userProfile.getPlaylistCount());
        realmUserProfile.setTrackCount(userProfile.getTrackCount());
        realmUserProfile.setUsername(userProfile.getUsername());

        realmAccount.setUserProfile(realmUserProfile);

        RealmList<com.sample.soundcloud.realm.models.Track> realmTracks = new RealmList<>();
        for(Track track : tracks){
            com.sample.soundcloud.realm.models.Track realmTrack
                    = mRealm.createObject(com.sample.soundcloud.realm.models.Track.class);
            realmTrack.setArtworkUrl(track.getArtworkUrl());
            realmTrack.setStreamUrl(track.getStreamUrl());
            realmTrack.setDuration(track.getDuration());
            realmTrack.setId(track.getId());
            realmTrack.setCreatedAt(track.getCreatedAt());
            realmTrack.setPlaybackCount(track.getPlaybackCount());
            realmTrack.setTitle(track.getTitle());

            com.sample.soundcloud.realm.models.UserProfile realmUser
                    = mRealm.createObject(com.sample.soundcloud.realm.models.UserProfile.class);
            realmUser.setUsername(track.getUser().getUsername());
            realmTrack.setUser(realmUser);

            realmTracks.add(realmTrack);
        }

        realmAccount.setTracks(realmTracks);

        mRealm.copyToRealm(realmAccount);

        mRealm.commitTransaction();
    }

    private com.sample.soundcloud.realm.models.Account getCachedAccount(){
        RealmResults<com.sample.soundcloud.realm.models.Account> realmResults
                = mRealm.where(com.sample.soundcloud.realm.models.Account.class).findAll();

        if(realmResults != null && realmResults.size() > 0){
            return realmResults.get(0);
        } else {
            return null;
        }
    }
    // endregion

}