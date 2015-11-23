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

import com.sample.soundcloud.SoundcloudApplication;
import com.sample.soundcloud.SoundcloudConstants;
import com.sample.soundcloud.network.Api;
import com.sample.soundcloud.network.models.Track;
import com.sample.soundcloud.network.models.UserProfile;
import com.sample.soundcloud.realm.RealmUtility;
import com.sample.soundcloud.realm.models.RealmAccount;

import java.util.List;

import io.realm.Realm;
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

        Context context = SoundcloudApplication.getInstance().getApplicationContext();
        try{
            mRealm = Realm.getInstance(context);
        } catch (RealmMigrationNeededException e) {
            Realm.deleteRealm(RealmUtility.getRealmConfiguration(context));
            mRealm = Realm.getInstance(context);
        }

        // Redownload account info
        loadAccount();
        mRealm.close();

    }

    // region Helper Methods
    private void loadAccount(){
        Timber.d("Soundcloud : loadAccount()");

        try {

            UserProfile userProfile = Api.getService(Api.getEndpointUrl()).getUserProfileSynchronous(SoundcloudConstants.USERNAME);
            List<Track> tracks = Api.getService(Api.getEndpointUrl()).getFavoriteTracksSynchronous(SoundcloudConstants.USERNAME);

            com.sample.soundcloud.models.Account account = new com.sample.soundcloud.models.Account(userProfile, tracks);

            RealmAccount cachedAccount = RealmUtility.getCachedAccount();
            if ((cachedAccount != null  && !account.equals(cachedAccount)
                        || cachedAccount == null)) {
                // Account has changed or loaded for the first time
                RealmUtility.persistAccount(account);

                Timber.d("Soundcloud : loadAccount() : There were changes to the account");
            } else {
                // No changes to the account
                Timber.d("Soundcloud : loadAccount() : No changes to the account");
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
    // endregion

}