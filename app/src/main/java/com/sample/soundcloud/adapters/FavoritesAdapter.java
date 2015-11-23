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

/**
 * Created by etiennelawlor on 3/21/15.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // region Member Variables
    private List<RealmTrack> mTracks;
    private OnItemClickListener mOnItemClickListener;
    // endregion

    // region Interfaces
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    // endregion

    // region Constructors
    public FavoritesAdapter() {
        mTracks = new ArrayList<>();
    }
    // endregion

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_row, parent, false);

        return new TrackViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        TrackViewHolder holder = (TrackViewHolder) viewHolder;

        final RealmTrack track = mTracks.get(position);

        if (track != null) {
            setUpArtWork(holder.mArtworkImageView, track);
            setUpUsername(holder.mUsernameTextView, track);
            setUpTitle(holder.mTitleTextView, track);
            setUpDuration(holder.mDurationTextView, track);
            setUpPlaybackCount(holder.mPlaybackCountTextView, track);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }

    // region Helper Methods
    public void add(int position, RealmTrack item) {
        mTracks.add(position, item);
        notifyItemInserted(position);
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(0);
        }
    }

    public void remove(int position) {
        mTracks.remove(position);
        notifyItemRemoved(position);
    }

    public RealmTrack getItem(int position) {
        return mTracks.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
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
        @Bind(R.id.artwork_iv)
        ImageView mArtworkImageView;
        @Bind(R.id.username_tv)
        TextView mUsernameTextView;
        @Bind(R.id.title_tv)
        TextView mTitleTextView;
        @Bind(R.id.duration_tv)
        TextView mDurationTextView;
        @Bind(R.id.playback_count_tv)
        TextView mPlaybackCountTextView;

        public TrackViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    // endregion

}
