package ru.matthewhadzhiev.rssreader;

import android.util.Log;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class AndroidLoggingHandler extends Handler {
    public static void reset(final Handler rootHandler) {
        final Logger rootLogger = LogManager.getLogManager().getLogger("");
        final Handler[] handlers = rootLogger.getHandlers();
        for (final Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        rootLogger.addHandler(rootHandler);
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }

    @SuppressWarnings("WrongConstant")
    @Override
    public void publish(final LogRecord record) {
        if (!super.isLoggable(record))
            return;

        final String name = record.getLoggerName();
        final int maxLength = 30;
        final String tag = name.length() > maxLength ? name.substring(name.length() - maxLength) : name;

        try {
            final int level = getAndroidLevel(record.getLevel());
            Log.println(level, tag, record.getMessage());
            if (record.getThrown() != null) {
                Log.println(level, tag, Log.getStackTraceString(record.getThrown()));
            }
        } catch (final RuntimeException e) {
            Log.e("AndroidLoggingHandler", "Error logging message.", e);
        }
    }

    private static int getAndroidLevel(final Level level) {
        final int value = level.intValue();
        if (value >= 1000) {
            return Log.ERROR;
        } else if (value >= 900) {
            return Log.WARN;
        } else if (value >= 800) {
            return Log.INFO;
        } else {
            return Log.DEBUG;
        }
    }
}
