package com.xiaoaitouch.mom.droid;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MomAdapter<T> extends BaseAdapter {
    private final List<T> list;

    protected MomAdapter() {
        this.list = new ArrayList<T>();
    }

    protected MomAdapter(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public T getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addToFirst(T data) {
        assert (list != null);

        list.add(0, data);
    }

    public void addToLast(T data) {
        assert (list != null);

        list.add(list.size(), data);
    }

    public void addAll(List<T> datas) {
        if (datas != null) {
            this.list.addAll(datas);
        }
    }

    public void clear() {
        list.clear();
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
