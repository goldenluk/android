package ru.matthewhadzhiev.rssreader.feed;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
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
    private final Context context;

    RssFeedListAdapter(final ArrayList<RssItem> rssFeedModels, final Context context) {
        rssItems = rssFeedModels;
        this.context = context;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(final ViewGroup parent,final int type) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_item, parent, false);
        return new FeedModelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FeedModelViewHolder holder, final int position) {
        final RssItem rssItem = rssItems.get(position);

        final TextView titleText = (TextView) holder.rssFeedView.findViewById(R.id.title_text);
        titleText.setText(rssItem.getTitle());

        if (rssItems.get(holder.getAdapterPosition()).isReaded()) {
            titleText.setTypeface(Typeface.DEFAULT);
        } else {
            titleText.setTypeface(Typeface.DEFAULT_BOLD);
        }

        final TextView addressText = (TextView)holder.rssFeedView.findViewById(R.id.address_text);
        addressText.setText(rssItem.getUrl());

        titleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                rssItems.get(holder.getAdapterPosition()).setReaded();
                try {
                    final ContentValues values = RssBaseHelper.getContentValuesForAll(rssItem);
                    new RssBaseHelper(context).getWritableDatabase().update(RssItemsDbSchema.RssAllItemsTable.NAME, values,
                            "title = ?",
                            new String[] { rssItem.getTitle() });
                } catch (final Throwable ignored) {
                }

                final Intent fullItem = new Intent(context, FullItemActivity.class)
                        .putExtra(FullItemActivity.ITEM_DESCRIPTION_STRING, rssItem.getDescription())
                        .putExtra(FullItemActivity.ITEM_TITLE_STRING, rssItem.getTitle())
                        .putExtra(FullItemActivity.ITEM_LINK_STRING, rssItem.getLink());
                view.getContext().startActivity(fullItem);
                if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_show_readed),false)) {
                    rssItems.remove(holder.getAdapterPosition());
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return rssItems.size();
    }

    static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private final View rssFeedView;

        FeedModelViewHolder(final View v) {
            super(v);
            rssFeedView = v;
        }
    }
}
