package ru.matthewhadzhiev.rssreader.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.matthewhadzhiev.rssreader.R;
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
        final RssItem rssItem = rssItems.get(position);
        ((TextView)holder.rssFeedView.findViewById(R.id.title_text)).setText(rssItem.getTitle());
        ((TextView)holder.rssFeedView.findViewById(R.id.address_text)).setText(rssItem.getUrl());
    }

    @Override
    public int getItemCount() {
        return rssItems.size();
    }
}
