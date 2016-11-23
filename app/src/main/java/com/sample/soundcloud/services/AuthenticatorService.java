package com.sample.soundcloud.services;

/**
 * Created by etiennelawlor on 5/1/15.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.sample.soundcloud.accounts.StubAuthenticator;

/**
 * A bound Service that instantiates the authenticator
 * when started.
 */
public class AuthenticatorService extends Service {

    // region Member Variables
    // Instance field that stores the authenticator object
    private StubAuthenticator authenticator;
    // endregion

    @Override
    public void onCreate() {
        // Create a new authenticator object
        authenticator = new StubAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}