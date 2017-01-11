package com.sample.soundcloud.activities;

/**
 * Created by etiennelawlor on 5/7/15.
 */

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sample.soundcloud.R;
import com.sample.soundcloud.fragments.AccountFragment;
import com.sample.soundcloud.network.ServiceGenerator;
import com.sample.soundcloud.network.SoundCloudService;
import com.sample.soundcloud.network.interceptors.AuthorizedNetworkInterceptor;
import com.sample.soundcloud.utilities.FontCache;
import com.sample.soundcloud.utilities.NetworkLogUtility;
import com.sample.soundcloud.utilities.NetworkUtility;
import com.sample.soundcloud.utilities.TrestleUtility;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MediaPlayerActivity extends AppCompatActivity {

    // region Views
    @BindView(R.id.pause)
    ImageButton pauseImageButton;
    @BindView(R.id.play)
    ImageButton playImageButton;
    @BindView(R.id.artist_tv)
    TextView artistTextView;
    @BindView(R.id.title_tv)
    TextView titleTextView;
    @BindView(R.id.cover_image_iv)
    ImageView coverImageImageView;
    @BindView(R.id.sb)
    SeekBar seekBar;
    @BindView(R.id.total_time_tv)
    TextView totalTimeTextView;
    @BindView(R.id.current_time_tv)
    TextView currentTimeTextView;
    @BindView(R.id.media_fl)
    FrameLayout mediaFrameLayout;
    @BindView(R.id.pb)
    ProgressBar progressBar;
    @BindView(R.id.error_ll)
    LinearLayout errorLinearLayout;
    @BindView(R.id.error_tv)
    TextView errorTextView;
    // endregion

    // region Member Variables
    private MediaPlayer mediaPlayer;
    private String artist = "";
    private String title = "";
    private String coverImage = "";
    private SoundCloudService soundCloudService;
    private List<Call> calls;
    private Typeface font;
    private long trackId;

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
    @OnClick(R.id.reload_btn)
    public void onReloadButtonClicked() {
        errorLinearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Call getStreamInfoCall = soundCloudService.getStreamInfo(trackId);
        calls.add(getStreamInfoCall);
        getStreamInfoCall.enqueue(getStreamInfoCallback);
    }

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
            mediaFrameLayout.setVisibility(View.VISIBLE);

            artistTextView.setText(artist);
            titleTextView.setText(title);

            if(!TextUtils.isEmpty(coverImage)){
                Picasso.with(getApplicationContext())
                        .load(coverImage)
                        .into(coverImageImageView);
            }

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

    private Callback<ResponseBody> getStreamInfoCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            progressBar.setVisibility(View.GONE);

            if (!response.isSuccessful()) {
                int responseCode = response.code();
                switch (responseCode) {
                    case 504: // 504 Unsatisfiable Request (only-if-cached)
                        errorTextView.setText("Can't load data.\nCheck your network connection.");
                        errorLinearLayout.setVisibility(View.VISIBLE);
                        break;
                    case 429: // 429 Too Many Requests
                        errorTextView.setText("The stream cannot be played\nnow. Please try again later.");
                        errorLinearLayout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                return;
            }

            if (!isFinishing()) {

                String audioFile = response.raw().networkResponse().request().url().toString();

                if (!TextUtils.isEmpty(audioFile)) {

                    audioFile = getUpdatedAudioFile(audioFile);

                    Timber.d("Audiofile - "+audioFile);

                    // create a media player
                    mediaPlayer = new MediaPlayer();

                    // try to load data and play
                    try {

                        // give data to mMediaPlayer
                        mediaPlayer.setDataSource(audioFile);

                        mediaPlayer.setOnPreparedListener(mediaPlayerOnPreparedListener);

                        // media player asynchronous preparation
                        mediaPlayer.prepareAsync();

                        // execute this code at the end of asynchronous media player preparation
                        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                                return false;
                            }
                        });
                    } catch (IOException e) {
                        finish();
//                        Toast.makeText(MediaPlayerActivity.this, getString(R.string.file_not_found), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    mediaFrameLayout.setVisibility(View.VISIBLE);
                }
            }

            okhttp3.Response rawResponse = response.raw();
            if(rawResponse != null){
                Headers headers = rawResponse.headers();
                if(headers != null){
                    String[] strings = headers.toString().split("\n");
                    for(String string : strings){
                        if(string.equals("Warning: 110 HttpURLConnection \"Response is stale\"")){
                            Snackbar.make(findViewById(R.id.main_content),
                                    TrestleUtility.getFormattedText("Network connection is unavailable.", font, 16),
                                    Snackbar.LENGTH_LONG)
                                    .show();
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            NetworkLogUtility.logFailure(call, t);

            progressBar.setVisibility(View.GONE);

            if(NetworkUtility.isKnownException(t)){
                errorTextView.setText("Can't load data.\nCheck your network connection.");
                errorLinearLayout.setVisibility(View.VISIBLE);
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

        font = FontCache.getTypeface("MavenPro-Medium.ttf", this);

        String streamUrl = "";

        // get data from main activity intent
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if(extras != null){
                streamUrl = extras.getString(AccountFragment.AUDIO_STREAM_URL);
                artist = extras.getString(AccountFragment.AUDIO_ARTIST);
                title = extras.getString(AccountFragment.AUDIO_TITLE);
                coverImage = extras.getString(AccountFragment.IMG_URL);
            }
        }

        Uri streamUri = Uri.parse(streamUrl);
        trackId = Long.valueOf(streamUri.getPathSegments().get(1));

        calls = new ArrayList<>();

        soundCloudService = ServiceGenerator.createService(
                SoundCloudService.class,
                SoundCloudService.BASE_URL,
                new AuthorizedNetworkInterceptor(this));

        Call getStreamInfoCall = soundCloudService.getStreamInfo(trackId);
        calls.add(getStreamInfoCall);
        getStreamInfoCall.enqueue(getStreamInfoCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);

        Timber.d("onDestroy() : calls.size() - " + calls.size());

        for (final Call call : calls) {
            Timber.d("onDestroy() : call.cancel() - " + call.toString());

            try {
                call.cancel();
            } catch (NetworkOnMainThreadException e) {
                Timber.d("onDestroy() : NetworkOnMainThreadException thrown");
                e.printStackTrace();
            }
        }

        calls.clear();
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

    private String getUpdatedAudioFile(String aFile){
//        String audioFile = "";
        Uri audioUri = Uri.parse(aFile);
        String scheme = audioUri.getScheme();
        String host = audioUri.getHost();
        String encodedPath = audioUri.getEncodedPath();

        String queryString = "?";
        Set<String> queryParameterNames = audioUri.getQueryParameterNames();
        for (String s : queryParameterNames) {
            if(!(s.equals("client_id"))){
                String queryParam = audioUri.getQueryParameter(s);
                queryString += String.format("%s=%s&", s, queryParam);
            }
        }

        return String.format("%s://%s%s%s", scheme, host, encodedPath, queryString.substring(0, queryString.length()-1));
    }
    // endregion
}
