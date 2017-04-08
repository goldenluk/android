package ru.matthewhadzhiev.rssreader.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import ru.matthewhadzhiev.rssreader.R;
import ru.matthewhadzhiev.rssreader.feed.UpdateAllService;

//А потому что я не вижу смысла использовать тут фрагменты
@SuppressWarnings("deprecation")
public final class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences,
                                          final String key) {
        if (key.equals(getString(R.string.key_update)))  {
            if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(getString(R.string.key_update),false)) {
                UpdateAllService.setServiceAlarm(getApplicationContext(), true);
            } else {
                UpdateAllService.setServiceAlarm(getApplicationContext(), false);
            }
        }

        if (key.equals(getString(R.string.key_update_freq)))  {
            UpdateAllService.setServiceAlarm(getApplicationContext(), false);
            UpdateAllService.setServiceAlarm(getApplicationContext(), true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
