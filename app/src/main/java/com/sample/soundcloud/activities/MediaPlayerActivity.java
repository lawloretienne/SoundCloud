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

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;
import timber.log.Timber;

public class MediaPlayerActivity extends Activity {

    // region Views
    @Bind(R.id.pause)
    ImageButton pauseImageButton;
    @Bind(R.id.play)
    ImageButton playImageButton;
    @Bind(R.id.artist_tv)
    TextView artistTextView;
    @Bind(R.id.title_tv)
    TextView titleTextView;
    @Bind(R.id.cover_image_iv)
    ImageView coverImageImageView;
    @Bind(R.id.sb)
    SeekBar seekBar;
    @Bind(R.id.total_time_tv)
    TextView totalTimeTextView;
    @Bind(R.id.current_time_tv)
    TextView currentTimeTextView;
    @Bind(R.id.media_rl)
    RelativeLayout mediaRelativeLayout;
    @Bind(R.id.pb)
    ProgressBar progressBar;
    // endregion

    // region Member Variables
    private MediaPlayer mediaPlayer;
    private String artist = "";
    private String title = "";
    private String coverImage = "";

    private static Handler handler = new Handler();

    private final Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (mediaPlayer != null) {

                //set max value
                int mDuration = mediaPlayer.getDuration();
                seekBar.setMax(mDuration);

                //update total time text view
                totalTimeTextView.setText(getTimeString(mDuration));

                //set progress to current position
                int mCurrentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(mCurrentPosition);

                //update current time text view
                currentTimeTextView.setText(getTimeString(mCurrentPosition));

                //handle drag on seekbar
                seekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

                if (mCurrentPosition == mDuration) {
                    pauseImageButton.setVisibility(View.GONE);
                    playImageButton.setVisibility(View.VISIBLE);
                }
            }

            //repeat above code every second
            handler.postDelayed(this, 10);
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
            if (mediaPlayer != null && fromUser) {
                mediaPlayer.seekTo(progress);
            }
        }
    };

    private OnPreparedListener mediaPlayerOnPreparedListener = new OnPreparedListener() {
        public void onPrepared(final MediaPlayer mp) {

            progressBar.setVisibility(View.GONE);
            mediaRelativeLayout.setVisibility(View.VISIBLE);

            artistTextView.setText(artist);
            titleTextView.setText(title);

            Picasso.with(getApplicationContext())
                    .load(coverImage)
                    .into(coverImageImageView);

            playImageButton.setVisibility(View.GONE);
            pauseImageButton.setVisibility(View.VISIBLE);

            //start media player
            mp.start();

            //update seekbar
            runnable.run();
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
                    mediaPlayer = new MediaPlayer();

                    // try to load data and play
                    try {

                        // give data to mMediaPlayer
                        mediaPlayer.setDataSource(audioFile);
                        // media player asynchronous preparation
                        mediaPlayer.prepareAsync();

                        // execute this code at the end of asynchronous media player preparation
                        mediaPlayer.setOnPreparedListener(mediaPlayerOnPreparedListener);
                    } catch (IOException e) {
                        finish();
//                        Toast.makeText(MediaPlayerActivity.this, getString(R.string.file_not_found), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    mediaRelativeLayout.setVisibility(View.VISIBLE);
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
        ButterKnife.bind(this);

        String streamUrl = "";

        // get data from main activity intent
        Intent intent = getIntent();
        if (intent != null) {
            streamUrl = intent.getStringExtra(SoundcloudConstants.AUDIO_STREAM_URL);
            artist = intent.getStringExtra(SoundcloudConstants.AUDIO_ARTIST);
            title = intent.getStringExtra(SoundcloudConstants.AUDIO_TITLE);
            coverImage = intent.getStringExtra(SoundcloudConstants.IMG_URL);
        }

        Uri streamUri = Uri.parse(streamUrl);
        long trackId = Long.valueOf(streamUri.getPathSegments().get(1));

        Api.getService(Api.getEndpointUrl()).getStreamInfo(trackId, mGetStreamInfoCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
    // endregion

    // region Helper Methods
    public void play(View view) {
        playImageButton.setVisibility(View.GONE);
        pauseImageButton.setVisibility(View.VISIBLE);
        mediaPlayer.start();
    }

    public void pause(View view) {
        playImageButton.setVisibility(View.VISIBLE);
        pauseImageButton.setVisibility(View.GONE);
        mediaPlayer.pause();
    }

    public void stop(View view) {
        mediaPlayer.seekTo(0);
        mediaPlayer.pause();
    }

    public void seekForward(View view) {
        //set seek time
        int seekForwardTime = 5000;

        // get current song position
        int currentPosition = mediaPlayer.getCurrentPosition();
        // check if seekForward time is lesser than song duration
        if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
            // forward song
            mediaPlayer.seekTo(currentPosition + seekForwardTime);
        } else {
            // forward to end position
            mediaPlayer.seekTo(mediaPlayer.getDuration());
        }
    }

    public void seekBackward(View view) {
        //set seek time
        int seekBackwardTime = 5000;

        // get current song position
        int currentPosition = mediaPlayer.getCurrentPosition();
        // check if seekBackward time is greater than 0 sec
        if (currentPosition - seekBackwardTime >= 0) {
            // forward song
            mediaPlayer.seekTo(currentPosition - seekBackwardTime);
        } else {
            // backward to starting position
            mediaPlayer.seekTo(0);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
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
