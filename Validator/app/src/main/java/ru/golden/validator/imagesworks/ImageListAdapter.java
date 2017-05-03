package ru.golden.validator.imagesworks;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ru.golden.validator.R;

final class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.FeedModelViewHolder> {

    private static final String ITEM_LINK_STRING = "ru.golden.validator.LINK";
    private final ArrayList<Image> items;

    ImageListAdapter(final ArrayList<Image> items) {
        this.items = items;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(final ViewGroup parent, final int type) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);
        return new FeedModelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FeedModelViewHolder holder, final int position) {
        final ImageView imageView = (ImageView) holder.imagesView.findViewById(R.id.photo_gallery_image_view);
        Picasso.with(holder.imagesView.getContext())
                .load(items.get(position).getThumbnailUrl().replace("http", "https"))
                .placeholder(R.drawable.ic_yoda_1)
                .error(R.drawable.ic_fail)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private final View imagesView;

        FeedModelViewHolder(final View v) {
            super(v);
            imagesView = v;
        }
    }
}