package com.texasgamer.zephyr.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.LoginEvent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;

import java.util.UUID;

import io.fabric.sdk.android.Fabric;

public class MetricsManager {

    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;

    public MetricsManager(Context context) {
        mContext = context;

        String uuid = getUuid();
    }

    public void logEvent(@StringRes int iri, Bundle extras) {
        Log.v(TAG, mContext.getString(iri) + ": " + (extras != null ? extras.toString() : "null"));
        firebaseEvent(iri, extras);
        fabricEvent(iri, extras);
    }

    public void logLogin(String method, boolean success) {
        Log.v(TAG, "login: " + method + " " + success);

    }

    private void firebaseEvent(@StringRes int iri, Bundle extras) {
    }

    private void fabricEvent(@StringRes int iri, Bundle extras) {
    }

    private String getUuid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String uuid = preferences.getString(mContext.getString(R.string.pref_uuid), "");

        if (uuid.isEmpty()) {
            uuid = UUID.randomUUID().toString();
            preferences.edit().putString(mContext.getString(R.string.pref_uuid), uuid).apply();
        }

        return uuid;
    }
}
