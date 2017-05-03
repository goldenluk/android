package ru.golden.validator.imagesworks;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import ru.golden.validator.R;

final class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.FeedModelViewHolder> {

    private static final String EXTRA_STRING_URL = "ru.golden.validator.URL";
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
        final TextView id = (TextView) holder.imagesView.findViewById(R.id.image_id);
        id.setText(String.format(Locale.getDefault(),"%d",items.get(position).getId()));

        final TextView title = (TextView) holder.imagesView.findViewById(R.id.image_title);
        title.setText(items.get(position).getTitle());

        final ImageView imageView = (ImageView) holder.imagesView.findViewById(R.id.image_view);
        Picasso.with(holder.imagesView.getContext())
                .load(items.get(position).getThumbnailUrl().replace("http", "https"))
                .placeholder(R.drawable.ic_yoda_1)
                .error(R.drawable.ic_fail)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.getContext().startActivity(new Intent(view.getContext(), FullImageActivity.class)
                .putExtra(EXTRA_STRING_URL, items.get(holder.getAdapterPosition()).getUrl()));
            }
        });
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