package ru.matthewhadzhiev.rssreader.channels;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.matthewhadzhiev.rssreader.AndroidLoggingHandler;
import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.database.RssBaseHelper;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema;
import ru.matthewhadzhiev.rssreader.rssworks.RssChannel;

final class RssChannelListAdapter extends RecyclerView.Adapter<RssChannelListAdapter.ChannelModelViewHolder> {

    private final ArrayList<RssChannel> channelItems;
    private final Context context;
    private final Logger logger;

    static class ChannelModelViewHolder extends RecyclerView.ViewHolder {
        private final View channelsView;

        ChannelModelViewHolder(final View v) {
            super(v);
            channelsView = v;
        }
    }

    RssChannelListAdapter(final ArrayList<RssChannel> rssChannelsModels, final Context context) {
        channelItems = rssChannelsModels;
        this.context = context;
        AndroidLoggingHandler.reset(new AndroidLoggingHandler());
        logger = Logger.getLogger("RssChannelListAdapter");
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

        isChannelActiveCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                try {
                    final SQLiteDatabase database = new RssBaseHelper(context).getWritableDatabase();

                    if (isChannelActiveCheckBox.isChecked()) {
                        channelItem.setActive(true);
                        database.update(RssItemsDbSchema.RssChannelsTable.NAME, RssBaseHelper.getContentValuesChannel(channelItem),
                                RssItemsDbSchema.RssChannelsTable.Cols.ADDRESS + "= ?", new String[] { channelItem.getAddress()});
                    } else {
                        channelItem.setActive(false);
                        database.update(RssItemsDbSchema.RssChannelsTable.NAME, RssBaseHelper.getContentValuesChannel(channelItem),
                                RssItemsDbSchema.RssChannelsTable.Cols.ADDRESS + "= ?", new String[] { channelItem.getAddress()});
                    }
                } catch (final Exception e) {
                    logger.log(Level.WARNING, "Не смогли записать в базу");
                }
            }
        });


        final Button deleteChannel = (Button) holder.channelsView.findViewById(R.id.delete_channel_button);
        deleteChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try {
                    final SQLiteDatabase database = new RssBaseHelper(context).getWritableDatabase();
                    database.delete(RssItemsDbSchema.RssItemsTable.NAME, RssItemsDbSchema.RssItemsTable.Cols.ADDRESS + "= ?", new String[] { channelItem.getAddress()});
                    database.delete(RssItemsDbSchema.RssChannelsTable.NAME, RssItemsDbSchema.RssChannelsTable.Cols.ADDRESS + "= ?", new String[] { channelItem.getAddress()});
                    channelItems.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                } catch (final Exception e) {
                    logger.log(Level.WARNING, "Не смогли записать в базу");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return channelItems.size();
    }


}
