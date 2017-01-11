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
import android.util.Log;

import com.sample.soundcloud.SoundcloudApplication;
import com.sample.soundcloud.fragments.AccountFragment;
import com.sample.soundcloud.network.ServiceGenerator;
import com.sample.soundcloud.network.SoundCloudService;
import com.sample.soundcloud.network.interceptors.AuthorizedNetworkInterceptor;
import com.sample.soundcloud.network.models.Track;
import com.sample.soundcloud.network.models.UserProfile;
import com.sample.soundcloud.realm.RealmUtility;
import com.sample.soundcloud.realm.models.RealmAccount;
import com.sample.soundcloud.utilities.NetworkUtility;

import java.util.List;

import io.realm.Realm;
import io.realm.exceptions.RealmMigrationNeededException;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class AccountSyncAdapter extends AbstractThreadedSyncAdapter {

    private Context context;
    private SoundCloudService soundCloudService;
    // endregion

    // region Constructors
    /**
     * Set up the sync adapter
     */
    public AccountSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        this.context = context;
        initService();
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

        this.context = context;
        initService();
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

        Log.d("Soundcloud", "Soundcloud : onPerformSync() called with: account = [" + account + "], bundle = [" + bundle + "], s = [" + s + "], contentProviderClient = [" + contentProviderClient + "], syncResult = [" + syncResult + "]");

        Realm realm = Realm.getDefaultInstance();
        try {
            // Redownload account info
            loadAccount();
        } finally {
            realm.close();
        }
    }

    // region Helper Methods
    private void loadAccount(){
        Observable.combineLatest(
                soundCloudService.getUserProfile(AccountFragment.USERNAME),
                soundCloudService.getFavoriteTracks(AccountFragment.USERNAME),
                new Func2<UserProfile, List<Track>, com.sample.soundcloud.models.Account>() {
                    @Override
                    public com.sample.soundcloud.models.Account call(UserProfile userProfile, List<Track> tracks) {
                        return new com.sample.soundcloud.models.Account(userProfile, tracks);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<com.sample.soundcloud.models.Account>() {
                    @Override
                    public void call(com.sample.soundcloud.models.Account account) {
                        RealmAccount cachedAccount = RealmUtility.getCachedAccount();
                        if ((account != null && cachedAccount != null && !account.equals(cachedAccount)
                                || cachedAccount == null)) {
                            // Account has changed or loaded for the first time
                            RealmUtility.persistAccount(account);

                            Timber.d("Soundcloud : loadAccount() : There were changes to the account");
                        } else {
                            // No changes to the account
                            Timber.d("Soundcloud : loadAccount() : No changes to the account");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable t) {
                        Timber.e(t, "Soundcloud error");

                        if (NetworkUtility.isKnownException(t)) {

//                            int responseCode = ((HttpException) t).code();
//                            if(responseCode == 504) { // 504 Unsatisfiable Request (only-if-cached)
////                                errorTextView.setText("Can't load data.\nCheck your network connection.");
////                                errorLinearLayout.setVisibility(View.VISIBLE);
//
//                                Timber.e("504 Unsatisfiable Request (only-if-cached)");
//
////                                Snackbar.make(findViewById(R.id.main_content),
////                                        TrestleUtility.getFormattedText("Network connection is unavailable.", font, 16),
////                                        Snackbar.LENGTH_LONG)
////                                        .show();
//                            }
                        }
                    }
                });
    }

    private void initService(){
        soundCloudService = ServiceGenerator.createService(
                SoundCloudService.class,
                SoundCloudService.BASE_URL,
                new AuthorizedNetworkInterceptor(context));
    }
    // endregion

}