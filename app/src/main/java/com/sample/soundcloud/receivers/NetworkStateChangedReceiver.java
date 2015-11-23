package com.sample.soundcloud.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sample.soundcloud.otto.BusProvider;
import com.sample.soundcloud.otto.events.NetworkConnectedEvent;
import com.sample.soundcloud.otto.events.NetworkDisconnectedEvent;

/**
 * Created by etiennelawlor on 4/19/15.
 */
public class NetworkStateChangedReceiver extends BroadcastReceiver {

    // region Constructors
    public NetworkStateChangedReceiver() {
    }
    // endregion

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                BusProvider.getInstance().post(new NetworkConnectedEvent());
            } else {
                BusProvider.getInstance().post(new NetworkDisconnectedEvent());
            }
        }
    }
}
