package ru.matthewhadzhiev.rssreader.rssworks;


public final class RssChannel {
    private final String address;
    private boolean isActive;

    public RssChannel(final String address, final boolean isActive) {
        this.address = address;
        this.isActive = isActive;
    }

    public String getAddress() {
        return address;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive (final boolean isActive) {
        this.isActive = isActive;
    }
}
