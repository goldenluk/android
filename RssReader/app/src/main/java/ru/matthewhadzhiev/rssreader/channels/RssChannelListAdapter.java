package ru.matthewhadzhiev.rssreader.channels;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.rssworks.RssChannel;

final class RssChannelListAdapter extends RecyclerView.Adapter<RssChannelListAdapter.ChannelModelViewHolder> {

    private final List<RssChannel> channelItems;

    static class ChannelModelViewHolder extends RecyclerView.ViewHolder {
        private final View channelsView;

        ChannelModelViewHolder(final View v) {
            super(v);
            channelsView = v;
        }
    }

    RssChannelListAdapter(final List<RssChannel> rssChannelsModels) {
        channelItems = rssChannelsModels;


    }

    @Override
    public RssChannelListAdapter.ChannelModelViewHolder onCreateViewHolder(final ViewGroup parent, final int type) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channel_item, parent, false);
        return new ChannelModelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RssChannelListAdapter.ChannelModelViewHolder holder, final int position) {
        final RssChannel channelItem = channelItems.get(position);

        final TextView channelName = (TextView) holder.channelsView.findViewById(R.id.channel_name);
        channelName.setText(channelItem.getAddress());

        final CheckBox isChannelActiveCheckBox = (CheckBox) holder.channelsView.findViewById(R.id.is_active_channel_checkbox);
        isChannelActiveCheckBox.setChecked(channelItem.isActive());

        isChannelActiveCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //TODO Реализация сего листенера
            }
        });

        final Button deleteChannel = (Button) holder.channelsView.findViewById(R.id.delete_channel_button);
        deleteChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //TODO удаляем сей канал
            }
        });
    }

    @Override
    public int getItemCount() {
        return channelItems.size();
    }


}
