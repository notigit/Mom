package com.xiaoaitouch.mom.net;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.google.gson.Gson;
import com.xiaoaitouch.mom.droid.BaseApplication;
import com.xiaoaitouch.mom.util.Logger;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class PersistentCookieStore implements CookieStore {

    private CookieStore mStore;
    private Gson mGson;

    public static final class Prefs {
        private static final String FILE_NAME = "sevendo_cookie";
        private static final String COOKIE = "cookie";
        static SharedPreferences mPrefs;
        static {
            mPrefs = BaseApplication.sContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }

        public static String getJsonSessionCookie() {
            String cookie = mPrefs.getString(COOKIE, "");

            Logger.d(cookie);

            return cookie;
        }

        public static void saveJsonSessionCookie(String json) {
            Logger.d(json);

            mPrefs.edit().putString(COOKIE, json).apply();
        }

    }

    public PersistentCookieStore() {
        mGson = new Gson();
        mStore = new CookieManager().getCookieStore();
        String jsonSessionCookie = Prefs.getJsonSessionCookie();
        HttpCookie cookie = mGson.fromJson(jsonSessionCookie, HttpCookie.class);
        if (cookie != null) {
            mStore.add(URI.create(cookie.getDomain()), cookie);
        }
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        if (cookie.getName().equals("sessionid")) {
            remove(URI.create(cookie.getDomain()), cookie);
            Prefs.saveJsonSessionCookie(mGson.toJson(cookie));
        }

        mStore.add(URI.create(cookie.getDomain()), cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return mStore.get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return mStore.getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return mStore.getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return mStore.remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return mStore.removeAll();
    }
}
