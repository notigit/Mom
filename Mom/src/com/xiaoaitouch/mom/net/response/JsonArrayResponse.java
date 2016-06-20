package com.xiaoaitouch.mom.net.response;

import java.util.List;

public class JsonArrayResponse<T> {
    public String msg;
    public int state;
    public List<T> data;

}
