package com.sample.soundcloud.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sample.soundcloud.R;
import com.sample.soundcloud.fragments.AccountFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity
{

    // region Member Variables
    @InjectView(R.id.toolbar)Toolbar mToolbar;
    // endregion

    // region Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_fl, AccountFragment.newInstance(), "")
                .commit();

    }
    // endregion

}
