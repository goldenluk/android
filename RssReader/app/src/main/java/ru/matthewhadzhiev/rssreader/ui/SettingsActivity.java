package ru.matthewhadzhiev.rssreader.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import ru.matthewhadzhiev.rssreader.R;


public final class SettingsActivity extends PreferenceActivity {
    //А потому что я не вижу смысла использовать тут фрагменты
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
