package com.sample.soundcloud.fragments;

/**
 * Created by etiennelawlor on 4/13/15.
 */

import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sample.soundcloud.R;
import com.sample.soundcloud.SoundcloudApplication;
import com.sample.soundcloud.activities.MediaPlayerActivity;
import com.sample.soundcloud.adapters.FavoritesAdapter;
import com.sample.soundcloud.models.Account;
import com.sample.soundcloud.network.ServiceGenerator;
import com.sample.soundcloud.network.SoundCloudService;
import com.sample.soundcloud.network.interceptors.AuthorizedNetworkInterceptor;
import com.sample.soundcloud.network.models.Track;
import com.sample.soundcloud.network.models.UserProfile;
import com.sample.soundcloud.realm.RealmUtility;
import com.sample.soundcloud.realm.models.RealmAccount;
import com.sample.soundcloud.realm.models.RealmTrack;
import com.sample.soundcloud.realm.models.RealmUserProfile;
import com.sample.soundcloud.utilities.NetworkUtility;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.exceptions.RealmMigrationNeededException;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;


public class AccountFragment extends Fragment implements // region Interfaces
        FavoritesAdapter.OnItemClickListener
// endregion
{

    // region Constants
    public static final String AUDIO_STREAM_URL = "AUDIO_STREAM_URL";
    public static final String AUDIO_ARTIST = "AUDIO_ARTIST";
    public static final String AUDIO_TITLE = "AUDIO_TITLE";
    public static final String IMG_URL = "IMG_URL";

    //    public static final String USERNAME = "hardwell";
//    public static final String USERNAME = "eric-oetting";
    public static final String USERNAME = "kaskade";
//    public static final String USERNAME = "lawlorslaw";
//    public static final String USERNAME = "calvinharris";
//    public static final String USERNAME = "mallywobbles";
//    public static final String USERNAME = "dillonfrancis";
//    public static final String USERNAME = "zedd";
//    public static final String USERNAME = "martingarrix";
//    public static final String USERNAME = "tiesto";
//    public static final String USERNAME = "deadmau5";
//    public static final String USERNAME = "rachael-shreve";

    private static final int TEN_MINUTES = 600000;
    public static final int PAGE_LIMIT = 5;

    // The authority for the sync adapter's content provider
    private static final String AUTHORITY = "com.sample.soundcloud.provider";
    // An account type, in the form of a domain name
    private static final String ACCOUNT_TYPE = "com.sample.soundcloud";
    private static final long SYNC_INTERVAL = 60L;
    // endregion

    // region Views
    @BindView(R.id.avatar_iv)
    CircleImageView avatarImageView;
    @BindView(R.id.username_tv)
    TextView usernameTextView;
    @BindView(R.id.location_tv)
    TextView locationTextView;
    @BindView(R.id.followers_count_tv)
    TextView followersCountTextView;
    @BindView(R.id.track_count_tv)
    TextView trackCountTextView;
    @BindView(R.id.playlist_count_tv)
    TextView playlistCountTextView;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(android.R.id.empty)
    LinearLayout emptyLinearLayout;
    @BindView(R.id.account_ll)
    LinearLayout accountLinearLayout;
    @BindView(R.id.pb)
    ProgressBar progressBar;
    @BindView(R.id.error_ll)
    LinearLayout errorLinearLayout;
    @BindView(R.id.error_tv)
    TextView errorTextView;
    // endregion

    // region Member Variables
    private FavoritesAdapter favoritesAdapter;
    private Realm realm;
    private Unbinder unbinder;
    private SoundCloudService soundCloudService;
    private CompositeSubscription compositeSubscription;
    // endregion

    // region Listeners
    @OnClick(R.id.reload_btn)
    public void onReloadButtonClicked() {
        emptyLinearLayout.setVisibility(View.GONE);
        errorLinearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        loadAccount();
    }

    private RealmChangeListener accountChangedListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            Timber.d("Soundcloud : accountChangedListener : onChange()");

            errorLinearLayout.setVisibility(View.GONE);

            RealmAccount cachedAccount = RealmUtility.getCachedAccount();

            if (cachedAccount != null) {
                if (cachedAccount.getUserProfile() != null) {
                    setUpUserProfile(cachedAccount.getUserProfile());
                }

                if (cachedAccount.getTracks() != null) {
                    setUpFavoriteTracks(cachedAccount.getTracks());
                }
            }

            progressBar.setVisibility(View.GONE);
            accountLinearLayout.setVisibility(View.VISIBLE);
        }
    };
    // endregion

    // region Callbacks

    // endregion

    // region Constructors
    public AccountFragment() {
    }
    // endregion

    // region Factory Methods
    public static AccountFragment newInstance(Bundle extras) {
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    // endregion

    // region Lifecycle Methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeSubscription = new CompositeSubscription();

        realm = Realm.getDefaultInstance();
        realm.addChangeListener(accountChangedListener);

        soundCloudService = ServiceGenerator.createService(
                SoundCloudService.class,
                SoundCloudService.BASE_URL,
                new AuthorizedNetworkInterceptor(getContext()));

        // Create account, if needed
        android.accounts.Account androidAccount = createSyncAccount(getActivity());

        // Turn on periodic syncing
        ContentResolver.addPeriodicSync(
                androidAccount,
                AUTHORITY,
                Bundle.EMPTY,
                SYNC_INTERVAL);

        ContentResolver.setSyncAutomatically(androidAccount, AUTHORITY, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        favoritesAdapter = new FavoritesAdapter();
        favoritesAdapter.setOnItemClickListener(this);
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        recyclerView.setAdapter(favoritesAdapter);

        // TODO handle pagination
        //        mFavoritesRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int visibleItemCount = mLayoutManager.getChildCount();
//                int totalItemCount = mLayoutManager.getItemCount();
//                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
//
//                if (!mIsLoading) {
//                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
//                        loadMoreItems();
//                    }
//                }
//
//            }
//        });

//        mFavoritesRecyclerView.setVisibility(View.GONE);

        loadAccount();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        compositeSubscription.unsubscribe();

        realm.removeChangeListener(accountChangedListener);
        realm.close();

        RefWatcher refWatcher = SoundcloudApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    // endregion

    // region FavoritesAdapter.OnItemClickListener Methods
    @Override
    public void onItemClick(int position, View view) {
        final RealmTrack track = favoritesAdapter.getItem(position);

        String streamUrl = track.getStreamUrl();
        long duration = track.getDuration();

        if (duration < TEN_MINUTES) {
            playInMediaPlayer(track, streamUrl);
        } else {
            playInBrowser(streamUrl);
        }
    }

    // endregion

    // region Helper Methods

    private void loadAccount() {

        if (RealmUtility.isAccountCached()) {

            // Account is cached
            RealmAccount cachedAccount = RealmUtility.getCachedAccount();

            if (cachedAccount != null) {
                if (cachedAccount.getUserProfile() != null) {
                    setUpUserProfile(cachedAccount.getUserProfile());
                }

                if (cachedAccount.getTracks() != null) {
                    setUpFavoriteTracks(cachedAccount.getTracks());
                }
            }

            progressBar.setVisibility(View.GONE);
            accountLinearLayout.setVisibility(View.VISIBLE);

        } else {
            // Account is not cached

            Subscription subscription = Observable.combineLatest(
                    soundCloudService.getUserProfile(USERNAME),
                    soundCloudService.getFavoriteTracks(USERNAME),
                    new Func2<UserProfile, List<Track>, Account>() {
                        @Override
                        public Account call(UserProfile userProfile, List<Track> tracks) {
                            return new Account(userProfile, tracks);
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Account>() {
                        @Override
                        public void call(Account account) {
                            if (account != null) {
                                RealmUtility.persistAccount(account);
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable t) {
                            Timber.e(t, "Soundcloud error");
                            progressBar.setVisibility(View.GONE);

                            if (NetworkUtility.isKnownException(t)) {
//                                int responseCode = ((HttpException) throwable).code();
//                                if(responseCode == 504) { // 504 Unsatisfiable Request (only-if-cached)
//                                    errorTextView.setText("Can't load data.\nCheck your network connection.");
//                                    errorLinearLayout.setVisibility(View.VISIBLE);
//                                }

                                errorTextView.setText("Can't load data.\nCheck your network connection.");
                                errorLinearLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
            compositeSubscription.add(subscription);

        }

    }

    private void setUpUserProfile(RealmUserProfile userProfile) {
        if (userProfile != null) {
            setUpAvatar(avatarImageView, userProfile);
            setUpUsername(usernameTextView, userProfile);
            setUpLocation(locationTextView, userProfile);
            setUpFollowersCount(followersCountTextView, userProfile);
            setUpTrackCount(trackCountTextView, userProfile);
            setUpPlaylistCount(playlistCountTextView, userProfile);
        }
    }

    private void setUpAvatar(CircleImageView iv, RealmUserProfile userProfile) {
        String avatarUrl = userProfile.getAvatarUrl();
        if (!TextUtils.isEmpty(avatarUrl)) {
            // https://i1.sndcdn.com/avatars-000028479557-aid19w-large.jpg
            avatarUrl = avatarUrl.replace("large.jpg", "t500x500.jpg");
            Picasso.with(getActivity())
                    .load(avatarUrl)
                    .placeholder(R.color.primary)
                    .into(iv);
        }
    }

    private void setUpUsername(TextView tv, RealmUserProfile userProfile) {
        String userName = userProfile.getUsername();
        if (!TextUtils.isEmpty(userName)) {
            tv.setText(userName);
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    private void setUpLocation(TextView tv, RealmUserProfile userProfile) {
        String city = userProfile.getCity();
        String country = userProfile.getCountry();

        String location = "";
        if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(city)) {
            location = String.format("%s, %s", city, country);
        } else if (!TextUtils.isEmpty(city)) {
            location = String.format("%s", city);
        } else if (!TextUtils.isEmpty(country)) {
            location = String.format("%s", country);
        }

        if (!TextUtils.isEmpty(location)) {
            locationTextView.setText(location);
            locationTextView.setVisibility(View.VISIBLE);
        } else {
            locationTextView.setVisibility(View.GONE);
        }
    }

    private void setUpFollowersCount(TextView tv, RealmUserProfile userProfile) {
        int followersCount = userProfile.getFollowersCount();
        if (followersCount > 0) {
            if (followersCount > 1000) {
                String formattedFollowersCount = NumberFormat.getNumberInstance(Locale.US).format(followersCount);
                tv.setText(String.format("%s followers", formattedFollowersCount));
            } else {
                String formattedFollowersCount = getResources().getQuantityString(R.plurals.follower_count, followersCount, followersCount);
                tv.setText(formattedFollowersCount);
            }

            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    private void setUpTrackCount(TextView tv, RealmUserProfile userProfile) {
        int trackCount = userProfile.getTrackCount();
        if (trackCount > 0) {
            if (trackCount > 1000) {
                String formattedTrackCount = NumberFormat.getNumberInstance(Locale.US).format(trackCount);
                tv.setText(String.format("%s tracks", formattedTrackCount));
            } else {
                String formattedTrackCount = getResources().getQuantityString(R.plurals.track_count, trackCount, trackCount);
                tv.setText(formattedTrackCount);
            }

            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    private void setUpPlaylistCount(TextView tv, RealmUserProfile userProfile) {
        int playlistCount = userProfile.getPlaylistCount();
        if (playlistCount > 0) {
            if (playlistCount > 1000) {
                String formattedPlaylistCount = NumberFormat.getNumberInstance(Locale.US).format(playlistCount);
                tv.setText(String.format("%s playlists", formattedPlaylistCount));
            } else {
                String formattedPlaylistCount = getResources().getQuantityString(R.plurals.playlist_count, playlistCount, playlistCount);
                tv.setText(formattedPlaylistCount);
            }

            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    private void setUpFavoriteTracks(RealmList<RealmTrack> tracks) {
        favoritesAdapter.clear();

        favoritesAdapter.addAll(tracks);

        if (favoritesAdapter.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyLinearLayout.setVisibility(View.VISIBLE);
        } else {
            emptyLinearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void playInMediaPlayer(RealmTrack track, String streamUrl) {
        String artworkUrl = track.getArtworkUrl();
        String title = track.getTitle();
        String artist = "";

        RealmUserProfile userProfile = track.getUser();
        if (userProfile != null) {
            artist = userProfile.getUsername();
        }

        if (!TextUtils.isEmpty(artworkUrl)) {
            artworkUrl = artworkUrl.replace("large.jpg", "t500x500.jpg");
        }

        Intent intent = new Intent(getActivity(), MediaPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(AUDIO_STREAM_URL, streamUrl);
        bundle.putString(AUDIO_ARTIST, artist);
        bundle.putString(AUDIO_TITLE, title);
        bundle.putString(IMG_URL, artworkUrl);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void playInBrowser(String streamUrl) {
        // Getting stream info returns a large response for longer tracks
        // so just load track in a browser
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format("%s?client_id=%s", streamUrl, getString(R.string.client_id))));
        startActivity(browserIntent);
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    private android.accounts.Account createSyncAccount(Context context) {
        // Create the account type and default account
        android.accounts.Account newAccount = new android.accounts.Account(
                USERNAME, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        Context.ACCOUNT_SERVICE);

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
        /*
         * If you don't set android:syncable="true" in
         * in your <provider> element in the manifest,
         * then call context.setIsSyncable(account, AUTHORITY, 1)
         * here.
         */

            Timber.d("Soundcloud : createSyncAccount() : success");

        } else {
        /*
         * The account exists or some other error occurred. Log this, report it,
         * or handle it internally.
         */
            Timber.d("Soundcloud : createSyncAccount() : failure");

        }

        return newAccount;
    }
    // endregion
}
