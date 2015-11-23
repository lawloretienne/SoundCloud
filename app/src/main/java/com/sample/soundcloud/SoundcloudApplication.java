package com.sample.soundcloud;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;

import timber.log.Timber;

public class SoundcloudApplication extends Application {

    // region Static Variables
    private static SoundcloudApplication sCurrentApplication = null;
    // endregion

    // region Member Variables
    private RefWatcher mRefWatcher;
    // endregion

    // region Callbacks
    // endregion

    @Override
    public void onCreate() {
        super.onCreate();

//        if (!Fabric.isInitialized()) {
//            final Fabric fabric = new Fabric.Builder(this)
//                    .kits(new Crashlytics())
//                    .debuggable(true)
//                    .build();
//
//            Fabric.with(fabric);
//        }


        mRefWatcher = LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }

        sCurrentApplication = this;

    }

    // region Helper Methods
    public static SoundcloudApplication getInstance() {
        return sCurrentApplication;
    }

    public static File getCacheDirectory()  {
        return sCurrentApplication.getCacheDir();
    }

    public static RefWatcher getRefWatcher(Context context) {
        SoundcloudApplication application = (SoundcloudApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }
    // endregion

    // region Inner Classes

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

//            FakeCrashLibrary.log(priority, tag, message);
//
//            if (t != null) {
//                if (priority == Log.ERROR) {
//                    FakeCrashLibrary.logError(t);
//                } else if (priority == Log.WARN) {
//                    FakeCrashLibrary.logWarning(t);
//                }
//            }
        }
    }

    // endregion
}

