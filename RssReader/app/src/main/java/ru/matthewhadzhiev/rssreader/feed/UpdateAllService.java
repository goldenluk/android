package ru.matthewhadzhiev.rssreader.feed;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.matthewhadzhiev.rssreader.AndroidLoggingHandler;
import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.database.RssBaseHelper;
import ru.matthewhadzhiev.rssreader.database.RssItemsDbSchema;
import ru.matthewhadzhiev.rssreader.rssworks.Parser;
import ru.matthewhadzhiev.rssreader.rssworks.RssChannel;
import ru.matthewhadzhiev.rssreader.rssworks.RssItem;


public final class UpdateAllService extends IntentService {
    public UpdateAllService() {
        super("");
    }

    private static Intent newIntent(final Context context) {
        return new Intent(context, UpdateAllService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        final Intent newIntent = AllOrNewItemsActivity.newIntent(this);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, newIntent, 0);

        InputStream inputStream = null;


        AndroidLoggingHandler.reset(new AndroidLoggingHandler());
        final Logger logger = Logger.getLogger("FeedNewsActivity");

        final SQLiteDatabase database;
        try {
            database = new RssBaseHelper(this).getWritableDatabase();
            final ArrayList<RssChannel> channels = new RssBaseHelper(this).getChannels();
            int newItemsCount = 0;
            for (final RssChannel channel: channels) {
                if (channel.isActive()) {
                    final URL url = new URL(channel.getAddress());
                    inputStream = url.openConnection().getInputStream();
                    final ArrayList<RssItem> items = Parser.parseFeed(inputStream);
                    final ArrayList<RssItem> itemsFromDb = new RssBaseHelper(getApplicationContext()).getItems(true);

                    for (final RssItem item : items) {
                        item.setUrl(channel.getAddress());
                        boolean addOrNot = true;

                        final ContentValues values = RssBaseHelper.getContentValuesForAll(item);
                        for (final RssItem itemFromDb: itemsFromDb) {
                            if (itemFromDb.getTitle().equals(item.getTitle())) {
                                addOrNot = false;
                                break;
                            }
                        }
                        if (addOrNot) {
                            newItemsCount++;
                            database.insert(RssItemsDbSchema.RssAllItemsTable.NAME, null, values);
                        }
                    }
                }
            }

            if (newItemsCount != 0) {
                final Notification notification = new NotificationCompat.Builder(this).
                        setContentText("Новостей добавлено " + newItemsCount)
                        .setContentTitle("RssReader")
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_yoda_1)
                        .setAutoCancel(true)
                        .build();

                final NotificationManagerCompat notificationManagerCompat =
                        NotificationManagerCompat.from(this);
                notificationManagerCompat.notify(0, notification);
            }

            try {
                inputStream.close();
            } catch (final Exception ignored) {}
        } catch (final Throwable exception) {
            logger.log(Level.INFO, "Проблемы с базой" + exception.getMessage());
        }
    }

    public static void setServiceAlarm(final Context context, final boolean isOn) {
        final Intent i = UpdateAllService.newIntent(context);
        final PendingIntent pendingIntent = PendingIntent.getService(context, 0, i, 0);

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                    Long.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.key_update_freq), ""))*1000*60, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}
