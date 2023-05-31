package com.texasgamer.zephyr.service;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.texasgamer.zephyr.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NotificationService extends NotificationListenerService {

    private final String TAG = this.getClass().getSimpleName();

    private final Map<String, Set<Integer>> existingNotifications = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG, "onNotificationPosted");

        if (isValidNotification(sbn)) {
            Notification n = sbn.getNotification();
            String title;
            String text;

            // TODO: This is super janky and awful and needs to be cleaned up.
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                title = n.extras.getString(Notification.EXTRA_TITLE);
                try {
                    text = n.extras.getCharSequence(Notification.EXTRA_TEXT).toString();
                } catch (Exception e) {
                    try {
                        text = n.tickerText.toString();
                    } catch (Exception ex) {
                        text = "";
                    }
                }
            } else {
                try {
                    title = getPackageManager().getApplicationLabel(getPackageManager()
                            .getApplicationInfo(sbn.getPackageName(), PackageManager.GET_META_DATA)).toString();
                } catch (PackageManager.NameNotFoundException e) {
                    title = getString(R.string.notif_default_title);
                }

                try {
                    text = n.tickerText.toString();
                } catch (Exception ex) {
                    text = "";
                }
            }

            Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getPackageName() + "\t" + title + "\t" + text);
            Intent i = new Intent("com.texasgamer.zephyr.SOCKET_SERVICE");
            i.putExtra("type", "notification");
            i.putExtra("id", sbn.getId());
            i.putExtra("package", sbn.getPackageName());
            i.putExtra("title", title);
            i.putExtra("text", text);
            sendBroadcast(i);

        } else {
            Log.i(TAG, "Ignoring notification from " + sbn.getPackageName());
        }

        Set<Integer> existing = getExistingNotificationSet(sbn.getPackageName());
        existing.add(sbn.getId());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG, "onNotificationRemoved");
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());

        Set<Integer> existing = getExistingNotificationSet(sbn.getPackageName());
        existing.remove(sbn.getId());
    }

    private boolean isValidNotification(StatusBarNotification sbn) {
        Set<Integer> existing = getExistingNotificationSet(sbn.getPackageName());

        return PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(getString(R.string.pref_app_notif_base) + "-" + sbn.getPackageName(), true) &&
                getPackageManager().getLaunchIntentForPackage(sbn.getPackageName()) != null
                && sbn.isClearable()
                && !NotificationCompat.isGroupSummary(sbn.getNotification()) &&
                existing.contains(sbn.getId());
    }

    private Set<Integer> getExistingNotificationSet(String pkgName) {
        Set<Integer> set = existingNotifications.get(pkgName);
        if (set == null) {
            set = new HashSet<>();
            existingNotifications.put(pkgName, set);
        }

        return set;
    }
}
