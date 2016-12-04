package com.sample.soundcloud.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
public class FavoritesAdapter extends BaseAdapter<RealmTrack> {


    // region Member Variables
    private FooterViewHolder footerViewHolder;
    // endregion

    // region Constructors
    public FavoritesAdapter() {
        super();
    }
    // endregion

    @Override
    public int getItemViewType(int position) {
        return (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }

    @Override
    protected RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent) {
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

    @Override
    protected RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_footer, parent, false);

        final FooterViewHolder holder = new FooterViewHolder(v);
        holder.reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onReloadClickListener != null){
                    onReloadClickListener.onReloadClick();
                }
            }
        });

        return holder;
    }

    @Override
    protected void bindHeaderViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    protected void bindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final TrackViewHolder holder = (TrackViewHolder) viewHolder;

        final RealmTrack track = getItem(position);

        if (track != null) {
            setUpArtWork(holder.artworkImageView, track);
            setUpUsername(holder.usernameTextView, track);
            setUpTitle(holder.titleTextView, track);
            setUpDuration(holder.durationTextView, track);
            setUpPlaybackCount(holder.playbackCountTextView, track);
        }
    }

    @Override
    protected void bindFooterViewHolder(RecyclerView.ViewHolder viewHolder) {
        FooterViewHolder holder = (FooterViewHolder) viewHolder;
        footerViewHolder = holder;
    }

    @Override
    protected void displayLoadMoreFooter() {
        if(footerViewHolder!= null){
            footerViewHolder.errorRelativeLayout.setVisibility(View.GONE);
            footerViewHolder.loadingFrameLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void displayErrorFooter() {
        if(footerViewHolder!= null){
            footerViewHolder.loadingFrameLayout.setVisibility(View.GONE);
            footerViewHolder.errorRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void addFooter() {
        isFooterAdded = true;
        add(new RealmTrack());
    }

    // region Helper Methods
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

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        // region Views
        @Bind(R.id.loading_fl)
        FrameLayout loadingFrameLayout;
        @Bind(R.id.error_rl)
        RelativeLayout errorRelativeLayout;
        @Bind(R.id.pb)
        ProgressBar progressBar;
        @Bind(R.id.reload_btn)
        Button reloadButton;
        // endregion

        // region Constructors
        public FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        // endregion
    }

    // endregion

}
