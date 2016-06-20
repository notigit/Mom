package com.xiaoaitouch.mom.net;

import com.android.volley.NetworkResponse;

public class DefaultExpire implements ExpireStrategy {

    @Override
    public long expires(NetworkResponse response) {
        return System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7;
    }

}
