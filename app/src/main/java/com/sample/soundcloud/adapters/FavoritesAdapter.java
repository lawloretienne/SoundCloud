package com.sample.soundcloud.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.soundcloud.R;
import com.sample.soundcloud.realm.models.RealmTrack;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by etiennelawlor on 3/21/15.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // region Constants
    private static final int HEADER = 0;
    private static final int ITEM = 1;
    private static final int LOADING = 2;
    // endregion

    // region Member Variables
    private List<RealmTrack> tracks;
    private OnItemClickListener onItemClickListener;
    private boolean isLoadingFooterAdded = false;
    // endregion

    // region Interfaces
    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
    // endregion

    // region Constructors
    public FavoritesAdapter() {
        tracks = new ArrayList<>();
    }
    // endregion

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case HEADER:
//                viewHolder = createHeaderViewHolder(parent);
                break;
            case ITEM:
                viewHolder = createTrackViewHolder(parent);
                break;
            case LOADING:
                viewHolder = createLoadingViewHolder(parent);
                break;
            default:
                Timber.e("[ERR] type is not supported!!! type is %d", viewType);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case HEADER:
//                bindHeaderViewHolder(viewHolder);
                break;
            case ITEM:
                bindTrackViewHolder(viewHolder, position);
                break;
            case LOADING:
                bindLoadingViewHolder(viewHolder);
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM;
    }

    // region Helper Methods

    private RecyclerView.ViewHolder createTrackViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_row, parent, false);

        final TrackViewHolder holder = new TrackViewHolder(v);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPos = holder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(adapterPos, holder.itemView);
                    }
                }
            }
        });

        return holder;
    }

    private RecyclerView.ViewHolder createLoadingViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more, parent, false);

        return new MoreViewHolder(v);
    }

    private void bindTrackViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final TrackViewHolder holder = (TrackViewHolder) viewHolder;

        final RealmTrack track = tracks.get(position);

        if (track != null) {
            setUpArtWork(holder.artworkImageView, track);
            setUpUsername(holder.usernameTextView, track);
            setUpTitle(holder.titleTextView, track);
            setUpDuration(holder.durationTextView, track);
            setUpPlaybackCount(holder.playbackCountTextView, track);
        }
    }

    private void bindLoadingViewHolder(RecyclerView.ViewHolder viewHolder) {
        MoreViewHolder holder = (MoreViewHolder) viewHolder;
    }

    public void add(RealmTrack item) {
        tracks.add(item);
        notifyItemInserted(tracks.size() - 1);
    }

    public void addAll(List<RealmTrack> realmTracks) {
        for (RealmTrack realmTrack : realmTracks) {
            add(realmTrack);
        }
    }

    private void remove(RealmTrack item) {
        int position = tracks.indexOf(item);
        if (position > -1) {
            tracks.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingFooterAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoading() {
        isLoadingFooterAdded = true;
        add(new RealmTrack());
    }

    public void removeLoading() {
        isLoadingFooterAdded = false;

        int position = tracks.size() - 1;
        RealmTrack item = getItem(position);

        if (item != null) {
            tracks.remove(position);
            notifyItemRemoved(position);
        }
    }

    public RealmTrack getItem(int position) {
        return tracks.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void setUpArtWork(ImageView iv, RealmTrack track) {
        String artworkUrl = track.getArtworkUrl();

        if (!TextUtils.isEmpty(artworkUrl)) {
            artworkUrl = artworkUrl.replace("large.jpg", "t500x500.jpg");

//                        https://i1.sndcdn.com/avatars-000028479557-aid19w-large.jpg
            Picasso.with(iv.getContext())
                    .load(artworkUrl)
                            //                .placeholder(R.drawable.ic_placeholder)
                            //                .error(R.drawable.ic_error)
                    .into(iv);
        }
    }

    private void setUpUsername(TextView tv, RealmTrack track) {
        if (track.getUser() != null) {
            String username = track.getUser().getUsername();
            tv.setText(username);
        }
    }

    private void setUpTitle(TextView tv, RealmTrack track) {
        String title = track.getTitle();
        tv.setText(title);
    }

    private void setUpDuration(TextView tv, RealmTrack track) {
        long duration = track.getDuration();
        duration /= 1000;

        long minutes = duration / 60;
        long seconds = duration % 60;

        String time;
        if (minutes == 0L) {
            if (seconds > 0L) {
                if (seconds < 10L)
                    time = String.format("0:0%s", String.valueOf(seconds));
                else
                    time = String.format("0:%s", String.valueOf(seconds));
            } else {
                time = "0:00";
            }

        } else {
            if (seconds > 0L) {
                if (seconds < 10L)
                    time = String.format("%s:0%s", String.valueOf(minutes), String.valueOf(seconds));
                else
                    time = String.format("%s:%s", String.valueOf(minutes), String.valueOf(seconds));
            } else {
                time = String.format("%s:00", String.valueOf(minutes));
            }
        }

        tv.setText(time);
    }

    private void setUpPlaybackCount(TextView tv, RealmTrack track) {
        int playbackCount = track.getPlaybackCount();
        if (playbackCount > 0) {
            String formattedPlaybackCount = NumberFormat.getNumberInstance(Locale.US).format(playbackCount);
            tv.setText(String.valueOf(formattedPlaybackCount));
        }
    }
    // endregion

    // region Inner Classes

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        // region Views
        @Bind(R.id.artwork_iv)
        ImageView artworkImageView;
        @Bind(R.id.username_tv)
        TextView usernameTextView;
        @Bind(R.id.title_tv)
        TextView titleTextView;
        @Bind(R.id.duration_tv)
        TextView durationTextView;
        @Bind(R.id.playback_count_tv)
        TextView playbackCountTextView;
        // endregion

        // region Constructors
        public TrackViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        // endregion
    }

    public static class MoreViewHolder extends RecyclerView.ViewHolder {
        // region Views
        // endregion

        // region Constructors
        public MoreViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        // endregion
    }

    // endregion

}
