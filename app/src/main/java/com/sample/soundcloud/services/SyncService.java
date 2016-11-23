package com.sample.soundcloud.services;

/**
 * Created by etiennelawlor on 5/1/15.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.sample.soundcloud.syncadapters.AccountSyncAdapter;

/**
 * Define a Service that returns an IBinder for the
 * sync adapter class, allowing the sync adapter framework to call
 * onPerformSync().
 */
public class SyncService extends Service {

    // region Static Variables
    // Storage for an instance of the sync adapter
    private static AccountSyncAdapter accountSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object syncAdapterLock = new Object();
    // endregion

    /*
     * Instantiate the sync adapter object.
     */
    @Override
    public void onCreate() {
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (syncAdapterLock) {
            if (accountSyncAdapter == null) {
                accountSyncAdapter = new AccountSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     */
    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the AccountSyncAdapter
         * constructors call super()
         */
        return accountSyncAdapter.getSyncAdapterBinder();
    }
}