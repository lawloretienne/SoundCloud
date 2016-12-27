package com.sample.soundcloud.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sample.soundcloud.R;
import com.sample.soundcloud.fragments.AccountFragment;
import com.sample.soundcloud.utilities.FontCache;
import com.sample.soundcloud.utilities.TrestleUtility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // region Views
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    // endregion

    // endregion

    // region Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_SoundCloud_MainActivity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Typeface font = FontCache.getTypeface("MavenPro-Medium.ttf", this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(TrestleUtility.getFormattedText("SoundCloud", font));
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_fl);
        if (fragment == null) {
            fragment = AccountFragment.newInstance(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_fl, fragment, "")
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .attach(fragment)
                    .commit();
        }

    }
    // endregion

}
