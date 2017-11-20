package com.iptv.mktech.iptv.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.iptv.mktech.iptv.entiy.Channel;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2017/10/22.
 */

public class IptvUtils {

    public static int getCurrentPlayChannelIndex(String channelName,List<Channel> channelList) {
        int index = 0;
        for (int i = 0; i < channelList.size(); i++) {
            if (channelList.get(i).getStream_nam().equals(channelName)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static void saveCurrentChannelName(Context context, String name) {
        SharedPreferences mSp = context.getSharedPreferences(Constant.SHARE_PREFENCE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSp.edit();
        mEditor.putString(Constant.CURRENT_CHANNEL_NAME, name);
        mEditor.commit();
    }

    public static String getCurrentChannelName(Context context) {
        SharedPreferences mSp = context.getSharedPreferences(Constant.SHARE_PREFENCE_KEY, MODE_PRIVATE);
        return mSp.getString(Constant.CURRENT_CHANNEL_NAME, null);
    }

    public static void saveCode(Context context, String code) {
        SharedPreferences mSp = context.getSharedPreferences(Constant.SHARE_PREFENCE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSp.edit();
        mEditor.putString(Constant.CODE, code);
        mEditor.commit();
    }

    public static String getCode(Context context) {
        SharedPreferences mSp = context.getSharedPreferences(Constant.SHARE_PREFENCE_KEY, MODE_PRIVATE);
        return mSp.getString(Constant.CODE, null);
    }

    public static void saveItemPosition(Context context, int offY) {
        SharedPreferences mSp = context.getSharedPreferences(Constant.SHARE_PREFENCE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSp.edit();
        mEditor.putInt(Constant.OFFY_KEY, offY);
        mEditor.commit();
    }

    public static int getItemPosition(Context context) {
        SharedPreferences mSp = context.getSharedPreferences(Constant.SHARE_PREFENCE_KEY, MODE_PRIVATE);
        return mSp.getInt(Constant.OFFY_KEY, 0);
    }

    public static void saveLastItemDistance(Context context, int distance) {
        SharedPreferences mSp = context.getSharedPreferences(Constant.SHARE_PREFENCE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSp.edit();
        mEditor.putInt(Constant.FOCUSOFF_TOP_KEY, distance);
        mEditor.commit();
    }

    public int getLastItemDistance(Context context) {
        SharedPreferences mSp = context.getSharedPreferences(Constant.SHARE_PREFENCE_KEY, MODE_PRIVATE);
        return mSp.getInt(Constant.FOCUSOFF_TOP_KEY, 0);
    }


    public static void saveFocusItemDistance(Context context, int distance) {
        SharedPreferences mSp = context.getSharedPreferences(Constant.SHARE_PREFENCE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSp.edit();
        mEditor.putInt(Constant.FOCUSOFF_KEY, distance);
        mEditor.commit();
    }

    public static int getFocusItenDistance(Context context) {
        SharedPreferences mSp = context.getSharedPreferences(Constant.SHARE_PREFENCE_KEY, MODE_PRIVATE);
        return mSp.getInt(Constant.FOCUSOFF_KEY, 0);
    }

    public static void saveListviewMode(Context context, int mode) {
        SharedPreferences mSp = context.getSharedPreferences(Constant.SHARE_PREFENCE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSp.edit();
        mEditor.putInt(Constant.MODE_KEY, mode);
        mEditor.commit();
    }

    public static int getListviewMode(Context context) {
        SharedPreferences mSp = context.getSharedPreferences(Constant.SHARE_PREFENCE_KEY, MODE_PRIVATE);
        return mSp.getInt(Constant.MODE_KEY, Constant.MODE_ALLCHANNELS);
    }

    public static  void saveCurrentCategoryname(Context context, String name) {
        SharedPreferences mSp = context.getSharedPreferences(Constant.SHARE_PREFENCE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSp.edit();
        mEditor.putString(Constant.CURRENT_CATEGORY_NAMWE_KEY, name);
        mEditor.commit();
    }

    public static String getCurrentCategoryname(Context context) {
        return context.getSharedPreferences(Constant.SHARE_PREFENCE_KEY, MODE_PRIVATE).getString(Constant.CURRENT_CATEGORY_NAMWE_KEY, null);
    }
}
