package ru.matthewhadzhiev.rssreader.feed;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.database.RssBaseHelper;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema;
import ru.matthewhadzhiev.rssreader.rssworks.RssItem;

final class RssFeedListAdapter
        extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {

    private final ArrayList<RssItem> rssItems;

    static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private final View rssFeedView;

        FeedModelViewHolder(final View v) {
            super(v);
            rssFeedView = v;
        }
    }

    RssFeedListAdapter(final ArrayList<RssItem> rssFeedModels) {
        rssItems = rssFeedModels;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(final ViewGroup parent,final int type) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_item, parent, false);
        return new FeedModelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FeedModelViewHolder holder, final int position) {
        final RssItem rssItem = rssItems.get(rssItems.size() - 1 -position);

        final TextView titleText = (TextView) holder.rssFeedView.findViewById(R.id.title_text);
        titleText.setText(rssItem.getTitle());

        if (rssItem.isReaded()) {
            titleText.setTypeface(Typeface.DEFAULT);
        }

        final TextView addressText = (TextView)holder.rssFeedView.findViewById(R.id.address_text);
        addressText.setText(rssItem.getUrl());

        titleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                rssItem.setReaded(true);
                try {
                    final ContentValues values = RssBaseHelper.getContentValuesForAll(rssItem);
                    new RssBaseHelper(view.getContext()).getWritableDatabase().update(RssItemsDbSchema.RssAllItemsTable.NAME, values,
                            "title = ?",
                            new String[] { rssItem.getTitle() });
                } catch (final Throwable ignored) {
                }

                view.getContext().startActivity(new Intent(view.getContext(), FullItemActivity.class)
                       .putExtra(FullItemActivity.ITEM_DESCRIPTION,rssItem.getDescription()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return rssItems.size();
    }
}
