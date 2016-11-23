package com.sample.soundcloud.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sample.soundcloud.R;
import com.sample.soundcloud.fragments.AccountFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // region Member Variables
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    // endregion

    // region Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_fl, AccountFragment.newInstance(), "")
                .commit();

    }
    // endregion

}
