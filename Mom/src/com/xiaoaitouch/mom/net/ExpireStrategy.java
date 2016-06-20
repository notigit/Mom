package com.xiaoaitouch.mom.net;

import com.android.volley.NetworkResponse;

public interface ExpireStrategy {
    public long expires(NetworkResponse response);
}
