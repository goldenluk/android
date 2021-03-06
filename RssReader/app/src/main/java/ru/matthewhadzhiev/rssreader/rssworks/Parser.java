package ru.matthewhadzhiev.rssreader.rssworks;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.matthewhadzhiev.rssreader.AndroidLoggingHandler;

public final class Parser {

    public static ArrayList<RssItem> parseFeed(final InputStream inputStream) throws XmlPullParserException,
            IOException {
        AndroidLoggingHandler.reset(new AndroidLoggingHandler());
        final Logger logger = Logger.getLogger("AddChannelActivity");
        if (inputStream == null) {
            throw new IOException();
        }
        String title = null;
        String link = null;
        String description = null;
        boolean isItem = false;
        final ArrayList<RssItem> items = new ArrayList<>();

        try {
            final XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                final int eventType = xmlPullParser.getEventType();

                final String name = xmlPullParser.getName();
                if(name == null) {
                    continue;
                }

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                }

                if (title != null && link != null && description != null) {
                    if(isItem) {
                        final RssItem item = new RssItem(title, link, description, false);
                        items.add(item);
                    }
                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            try {
                inputStream.close();
            } catch (final IOException e) {
                logger.log(Level.WARNING, "Не распарсили данные");
            }
        }
    }
}
