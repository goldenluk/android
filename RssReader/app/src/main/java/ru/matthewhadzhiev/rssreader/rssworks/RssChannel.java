package ru.matthewhadzhiev.rssreader.rssworks;


public final class RssChannel {
    private final String address;
    private final boolean isActive;

    public RssChannel(String address, boolean isActive) {
        this.address = address;
        this.isActive = isActive;
    }

    public String getAddress() {
        return address;
    }

    public boolean isActive() {
        return isActive;
    }
}
