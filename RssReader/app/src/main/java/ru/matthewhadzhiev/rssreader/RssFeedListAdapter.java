package ru.matthewhadzhiev.rssreader;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class RssFeedListAdapter
        extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {

    private List<RssItem> mRssItems;

    static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private View rssFeedView;

        FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
        }
    }

    RssFeedListAdapter(List<RssItem> rssFeedModels) {
        mRssItems = rssFeedModels;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_item, parent, false);
        return new FeedModelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final RssItem rssItem = mRssItems.get(position);
        ((TextView)holder.rssFeedView.findViewById(R.id.title_text)).setText(rssItem.getTitle());
        ((TextView)holder.rssFeedView.findViewById(R.id.description_text))
                .setText(rssItem.getDescription());
        ((TextView)holder.rssFeedView.findViewById(R.id.link_text)).setText(rssItem.getLink());
    }

    @Override
    public int getItemCount() {
        return mRssItems.size();
    }
}
