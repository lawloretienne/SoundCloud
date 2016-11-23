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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sample.soundcloud.R;
import com.sample.soundcloud.SoundcloudApplication;
import com.sample.soundcloud.SoundcloudConstants;
import com.sample.soundcloud.activities.MediaPlayerActivity;
import com.sample.soundcloud.adapters.FavoritesAdapter;
import com.sample.soundcloud.models.Account;
import com.sample.soundcloud.network.Api;
import com.sample.soundcloud.network.models.Track;
import com.sample.soundcloud.network.models.UserProfile;
import com.sample.soundcloud.otto.BusProvider;
import com.sample.soundcloud.realm.RealmUtility;
import com.sample.soundcloud.realm.models.RealmAccount;
import com.sample.soundcloud.realm.models.RealmTrack;
import com.sample.soundcloud.realm.models.RealmUserProfile;
import com.sample.soundcloud.ui.CircleTransformation;
import com.sample.soundcloud.utilities.SoundcloudUtility;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.exceptions.RealmMigrationNeededException;
import retrofit.RetrofitError;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class AccountFragment extends Fragment implements // region Interfaces
        FavoritesAdapter.OnItemClickListener
// endregion
{

    // region Constants
    // The authority for the sync adapter's content provider
    private static final String AUTHORITY = "com.sample.soundcloud.provider";
    // An account type, in the form of a domain name
    private static final String ACCOUNT_TYPE = "com.sample.soundcloud";
    private static final long SYNC_INTERVAL = 60L;
    // endregion

    // region Views
    @Bind(R.id.avatar_iv)
    ImageView avatarImageView;
    @Bind(R.id.username_tv)
    TextView usernameTextView;
    @Bind(R.id.location_tv)
    TextView locationTextView;
    @Bind(R.id.followers_count_tv)
    TextView followersCountTextView;
    @Bind(R.id.track_count_tv)
    TextView trackCountTextView;
    @Bind(R.id.playlist_count_tv)
    TextView playlistCountTextView;
    @Bind(R.id.favorites_rv)
    RecyclerView favoritesRecyclerView;
    @Bind(android.R.id.empty)
    View emptyView;
    @Bind(R.id.account_ll)
    LinearLayout accountLinearLayout;
    @Bind(R.id.pb)
    ProgressBar progressBar;
    @Bind(R.id.error_ll)
    LinearLayout errorLinearLayout;
    @Bind(R.id.error_tv)
    TextView errorTextView;
    // endregion

    // region Member Variables
    private FavoritesAdapter favoritesAdapter;
    private Realm realm;
    // endregion

    // region Listeners

    private RealmChangeListener accountChangedListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            Timber.d("Soundcloud : mAccountChangedListener : onChange()");

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

        BusProvider.getInstance().register(this);

        Context context = SoundcloudApplication.getInstance().getApplicationContext();
        try {
            realm = Realm.getInstance(context);
        } catch (RealmMigrationNeededException e) {
            Realm.deleteRealm(RealmUtility.getRealmConfiguration(context));
            realm = Realm.getInstance(context);
        }

        realm.addChangeListener(accountChangedListener);

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
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        favoritesRecyclerView.setLayoutManager(layoutManager);
        favoritesAdapter = new FavoritesAdapter();
        favoritesAdapter.setOnItemClickListener(this);

        favoritesRecyclerView.setAdapter(favoritesAdapter);

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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        realm.removeChangeListener(accountChangedListener);
        realm.close();

        RefWatcher refWatcher = SoundcloudApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    // endregion

    // region FavoritesAdapter.OnItemClickListener Methods
    @Override
    public void onItemClick(int position) {
        final RealmTrack track = favoritesAdapter.getItem(position);

        String streamUrl = track.getStreamUrl();
        long duration = track.getDuration();

        if (duration < SoundcloudConstants.TEN_MINUTES) {
            playInMediaPlayer(track, streamUrl);
        } else {
            playInBrowser(streamUrl);
        }
    }

    // endregion

    // region Otto Methods
//    @Subscribe
//    public void onNetworkConnected(NetworkConnectedEvent event) {
//        Timber.d("Soundcloud : onNetworkConnected()");
//
//        if(mErrorLinearLayout.getVisibility() == View.VISIBLE){
//            mErrorLinearLayout.setVisibility(View.GONE);
//            mProgressBar.setVisibility(View.VISIBLE);
//        }
//
//        loadAccount();
//
//    }
//
//    @Subscribe
//    public void onNetworkDisconnected(NetworkConnectedEvent event) {
//        Timber.d("Soundcloud : onNetworkDisconnected()");
//
////        if(mAccountLinearLayout.getVisibility() == View.VISIBLE){
////            Toast.makeText(getActivity(), "Network Disconnected", Toast.LENGTH_SHORT).show();
////        }
//    }
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

            Observable.combineLatest(
                    Api.getService(Api.getEndpointUrl()).getUserProfile(SoundcloudConstants.USERNAME),
                    Api.getService(Api.getEndpointUrl()).getFavoriteTracks(SoundcloudConstants.USERNAME),
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
                        public void call(Throwable throwable) {
                            Timber.e(throwable, "Soundcloud error");

                            if (throwable instanceof RetrofitError) {
                                RetrofitError.Kind errorKind = ((RetrofitError) throwable).getKind();

                                errorTextView.setText(getErrorMessage(errorKind));
                                Timber.e(throwable, "Soundcloud error : errorMessage - " + getErrorMessage(errorKind));

                                progressBar.setVisibility(View.GONE);
                                if (accountLinearLayout.getVisibility() == View.GONE)
                                    errorLinearLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }

    }

    private String getErrorMessage(RetrofitError.Kind errorKind) {
        String errorMessage = "";
        switch (errorKind) {
            case NETWORK:
//                                    errorMessage = "Network Error";
                errorMessage = "Can't load data.\nCheck your network connection.";
                break;
            case HTTP:
                errorMessage = "HTTP Error";
                break;
            case UNEXPECTED:
                errorMessage = "Unexpected Error";
                break;
            case CONVERSION:
                errorMessage = "Conversion Error";
                break;
            default:
                break;
        }

        return errorMessage;
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

    private void setUpAvatar(ImageView iv, RealmUserProfile userProfile) {
        String avatarUrl = userProfile.getAvatarUrl();
        if (!TextUtils.isEmpty(avatarUrl)) {
            avatarUrl = avatarUrl.replace("large.jpg", "t500x500.jpg");

            Transformation roundedTransformation
                    = new CircleTransformation(getResources().getColor(R.color.primary),
                    getResources().getColor(R.color.primary));

            int dimension = SoundcloudUtility.dp2px(getActivity(), 70);

//                        https://i1.sndcdn.com/avatars-000028479557-aid19w-large.jpg
            Picasso.with(getActivity())
                    .load(avatarUrl)
                    .transform(roundedTransformation)
//                                .fit()
                    .resize(dimension, dimension)
                    .centerCrop()
                            //                .placeholder(R.drawable.ic_placeholder)
                            //                .error(R.drawable.ic_error)
                    .into(iv);
        }
    }

    private void setUpUsername(TextView tv, RealmUserProfile userProfile) {
        String userName = userProfile.getUsername();
        if (!TextUtils.isEmpty(userName)) {
            usernameTextView.setText(userName);
            usernameTextView.setVisibility(View.VISIBLE);
        } else {
            usernameTextView.setVisibility(View.GONE);
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

        if (tracks != null) {
            for (RealmTrack track : tracks) {
                favoritesAdapter.add(favoritesAdapter.getItemCount(), track);
            }
        }

        if (favoritesAdapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
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
        intent.putExtra(SoundcloudConstants.AUDIO_STREAM_URL, streamUrl);
        intent.putExtra(SoundcloudConstants.AUDIO_ARTIST, artist);
        intent.putExtra(SoundcloudConstants.AUDIO_TITLE, title);
        intent.putExtra(SoundcloudConstants.IMG_URL, artworkUrl);

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
                SoundcloudConstants.USERNAME, ACCOUNT_TYPE);
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
