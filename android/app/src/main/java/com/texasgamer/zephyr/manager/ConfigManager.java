package com.texasgamer.zephyr.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;

public class ConfigManager {

    private final String TAG = this.getClass().getSimpleName();
    private final int CACHE_EXPIRATION_IN_SECONDS = 3600;

    public ConfigManager(Context context) {
        if (Constants.FIREBASE_REMOTE_CONFIG_ENABLED) {
            FirebaseRemoteConfigSettings configSettings =
                    new FirebaseRemoteConfigSettings.Builder()
                            .setDeveloperModeEnabled(Constants.FIREBASE_REMOTE_CONFIG_DEV_MODE)
                            .build();

            Log.i(TAG, "Firebase Remote Config enabled (dev "
                    + (configSettings.isDeveloperModeEnabled() ? "enabled)" : "disabled)"));

        } else {
            Log.i(TAG, "Firebase Remote Config disabled");
        }
    }

    public boolean isLoginEnabled() {
        return false;
    }

    public boolean isLoginCardNew() {
        return getBoolean("loginCardNew", false);
    }

    public boolean shouldShowLoginSuccessDialog() {
        return getBoolean("loginSuccessDialog", true);
    }

    private boolean getBoolean(String key, boolean fallback) {
        boolean result;
        result = fallback;

        Log.v(TAG, key + " --> " + result + " (" + fallback + ")");
        return result;
    }

}
