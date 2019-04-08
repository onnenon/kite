package com.team100.kite_master.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "username";
    static final String HOST_IP= "hostip";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }


    public static void setHostIp(Context ctx, String ip)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(HOST_IP, ip);
        editor.commit();
    }

    public static String getHostIp(Context ctx)
    {
        return getSharedPreferences(ctx).getString(HOST_IP, "");
    }
}