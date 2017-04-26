package ru.golden.flickrviewer.feed;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ru.golden.flickrviewer.PhotoItem;
import ru.golden.flickrviewer.R;


final class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.FeedModelViewHolder> {

    private final ArrayList<PhotoItem> photoItems;

    FeedListAdapter(final ArrayList<PhotoItem> photoItems) {
        this.photoItems = photoItems;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(final ViewGroup parent, final int type) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_item, parent, false);
        return new FeedModelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FeedModelViewHolder holder, final int position) {
        final ImageView imageView = (ImageView) holder.photoFeedView.findViewById(R.id.photo_gallery_image_view);
        Picasso.with(holder.photoFeedView.getContext())
                .load(photoItems.get(position).getUrl())
                .placeholder(R.drawable.ic_yoda_1)
                .error(R.drawable.ic_fail)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return photoItems.size();
    }

    static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private final View photoFeedView;

        FeedModelViewHolder(final View v) {
            super(v);
            photoFeedView = v;
        }
    }
}
