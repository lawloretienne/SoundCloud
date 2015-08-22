package com.sample.soundcloud.network;

public enum EndpointUrl {

    PRODUCTION ("http://api.soundcloud.com");

    // region Member Variables
    private final String mValue;
    // endregion

    EndpointUrl(String value) {
        this.mValue = value;
    }

    @Override
    public String toString() {
        return mValue;
    }
}