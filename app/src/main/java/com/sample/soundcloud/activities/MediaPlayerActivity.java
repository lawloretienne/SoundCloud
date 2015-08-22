package com.sample.soundcloud.activities;

/**
 * Created by etiennelawlor on 5/7/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sample.soundcloud.R;
import com.sample.soundcloud.SoundcloudConstants;
import com.sample.soundcloud.network.Api;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;
import timber.log.Timber;

public class MediaPlayerActivity extends Activity {

    // region Member Variables
    @InjectView(R.id.pause)
    ImageButton mPauseImageButton;
    @InjectView(R.id.play)
    ImageButton mPlayImageButton;
    @InjectView(R.id.artist_tv)
    TextView mArtistTextView;
    @InjectView(R.id.title_tv)
    TextView mTitleTextView;
    @InjectView(R.id.cover_image_iv)
    ImageView mCoverImageImageView;
    @InjectView(R.id.sb)
    SeekBar mSeekBar;
    @InjectView(R.id.total_time_tv)
    TextView mTotalTimeTextView;
    @InjectView(R.id.current_time_tv)
    TextView mCurrentTimeTextView;
    @InjectView(R.id.media_rl)
    RelativeLayout mMediaRelativeLayout;
    @InjectView(R.id.pb)
    ProgressBar mProgressBar;

    private MediaPlayer mMediaPlayer;
    private String mArtist = "";
    private String mTitle = "";
    private String mCoverImage = "";

    private static Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if (mMediaPlayer != null) {

                //set max value
                int mDuration = mMediaPlayer.getDuration();
                mSeekBar.setMax(mDuration);

                //update total time text view
                mTotalTimeTextView.setText(getTimeString(mDuration));

                //set progress to current position
                int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                mSeekBar.setProgress(mCurrentPosition);

                //update current time text view
                mCurrentTimeTextView.setText(getTimeString(mCurrentPosition));

                //handle drag on seekbar
                mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

                if (mCurrentPosition == mDuration) {
                    mPauseImageButton.setVisibility(View.GONE);
                    mPlayImageButton.setVisibility(View.VISIBLE);
                }
            }

            //repeat above code every second
            mHandler.postDelayed(this, 10);
        }
    };
    // endregion

    // region Listeners
    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (mMediaPlayer != null && fromUser) {
                mMediaPlayer.seekTo(progress);
            }
        }
    };

    private OnPreparedListener mMediaPlayerOnPreparedListener = new OnPreparedListener() {
        public void onPrepared(final MediaPlayer mp) {

            mProgressBar.setVisibility(View.GONE);
            mMediaRelativeLayout.setVisibility(View.VISIBLE);

            mArtistTextView.setText(mArtist);
            mTitleTextView.setText(mTitle);

            Picasso.with(getApplicationContext())
                    .load(mCoverImage)
                    .into(mCoverImageImageView);

            mPlayImageButton.setVisibility(View.GONE);
            mPauseImageButton.setVisibility(View.VISIBLE);

            //start media player
            mp.start();

            //update seekbar
            mRunnable.run();
        }
    };
    // endregion

    // region Callbacks
    private Callback<Response> mGetStreamInfoCallback = new Callback<Response>() {
        @Override
        public void success(Response response, Response response2) {

            if (!isFinishing()) {

                String audioFile = response.getUrl();

                if (!TextUtils.isEmpty(audioFile)) {

                    // create a media player
                    mMediaPlayer = new MediaPlayer();

                    // try to load data and play
                    try {

                        // give data to mMediaPlayer
                        mMediaPlayer.setDataSource(audioFile);
                        // media player asynchronous preparation
                        mMediaPlayer.prepareAsync();

                        // execute this code at the end of asynchronous media player preparation
                        mMediaPlayer.setOnPreparedListener(mMediaPlayerOnPreparedListener);
                    } catch (IOException e) {
                        finish();
//                        Toast.makeText(MediaPlayerActivity.this, getString(R.string.file_not_found), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    mMediaRelativeLayout.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Timber.d("failure()");

            if(error != null){
                Response response = error.getResponse();
                if(response != null){
                    String reason = response.getReason();
                    Timber.d("failure() : reason -"+reason);

                    TypedInput body = response.getBody();
                    if(body != null){
                        Timber.d("failure() : body.toString() -"+body.toString());
                    }

                    int status = response.getStatus();
                    Timber.d("failure() : status -"+status);
                }

                Throwable cause = error.getCause();
                if(cause != null){
                    Timber.d("failure() : cause.toString() -"+cause.toString());
                }

                Object body = error.getBody();
                if(body != null){
                    Timber.d("failure() : body.toString() -"+body.toString());
                }
            }
        }
    };
    // endregion

    // region Lifecycle Methods
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title and go full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // inflate layout
        setContentView(R.layout.activity_media_player);
        ButterKnife.inject(this);

        String streamUrl = "";

        // get data from main activity intent
        Intent intent = getIntent();
        if (intent != null) {
            streamUrl = intent.getStringExtra(SoundcloudConstants.AUDIO_STREAM_URL);
            mArtist = intent.getStringExtra(SoundcloudConstants.AUDIO_ARTIST);
            mTitle = intent.getStringExtra(SoundcloudConstants.AUDIO_TITLE);
            mCoverImage = intent.getStringExtra(SoundcloudConstants.IMG_URL);
        }

        Uri streamUri = Uri.parse(streamUrl);
        long trackId = Long.valueOf(streamUri.getPathSegments().get(1));

        Api.getService(Api.getEndpointUrl()).getStreamInfo(trackId, mGetStreamInfoCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }
    // endregion

    // region Helper Methods
    public void play(View view) {
        mPlayImageButton.setVisibility(View.GONE);
        mPauseImageButton.setVisibility(View.VISIBLE);
        mMediaPlayer.start();
    }

    public void pause(View view) {
        mPlayImageButton.setVisibility(View.VISIBLE);
        mPauseImageButton.setVisibility(View.GONE);
        mMediaPlayer.pause();
    }

    public void stop(View view) {
        mMediaPlayer.seekTo(0);
        mMediaPlayer.pause();
    }

    public void seekForward(View view) {
        //set seek time
        int seekForwardTime = 5000;

        // get current song position
        int currentPosition = mMediaPlayer.getCurrentPosition();
        // check if seekForward time is lesser than song duration
        if (currentPosition + seekForwardTime <= mMediaPlayer.getDuration()) {
            // forward song
            mMediaPlayer.seekTo(currentPosition + seekForwardTime);
        } else {
            // forward to end position
            mMediaPlayer.seekTo(mMediaPlayer.getDuration());
        }
    }

    public void seekBackward(View view) {
        //set seek time
        int seekBackwardTime = 5000;

        // get current song position
        int currentPosition = mMediaPlayer.getCurrentPosition();
        // check if seekBackward time is greater than 0 sec
        if (currentPosition - seekBackwardTime >= 0) {
            // forward song
            mMediaPlayer.seekTo(currentPosition - seekBackwardTime);
        } else {
            // backward to starting position
            mMediaPlayer.seekTo(0);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();

        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        finish();
    }

    private String getTimeString(long millis) {
        StringBuilder builder = new StringBuilder();

        long hours = millis / (1000 * 60 * 60);
        long minutes = (millis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = ((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000;

        if (hours > 0L) {
            builder.append(String.format("%02d", hours))
                    .append(":");
        }

        if (minutes > 9) {
            builder.append(String.format("%02d", minutes))
                    .append(":");
        } else {
            builder.append(String.format("%01d", minutes))
                    .append(":");
        }

        builder.append(String.format("%02d", seconds));

        return builder.toString();
    }
    // endregion
}
