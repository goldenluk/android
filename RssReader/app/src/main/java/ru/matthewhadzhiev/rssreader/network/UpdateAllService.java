package ru.matthewhadzhiev.rssreader.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;


public final class UpdateAllService extends IntentService {
    public UpdateAllService() {
        super("");
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        //TODO сервис для обновления новостей через определенные промежутки времени
    }
}
