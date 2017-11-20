package com.iptv.mktech.iptv.utils.greenDao;

import android.content.Context;

/**
 * Created by Administrator on 2017/10/14.
 */

public class DaoUtils {
    private static PackageManager mPackageManager;
    private static ChannelManager mChannelManager;
    private static AccountManager mAccountManager;
    public static Context context;

    public static void init(Context context) {
        DaoUtils.context = context.getApplicationContext();
    }

    /**
     * 单列模式获取CustomerManager对象
     *
     * @return
     */
    public synchronized static PackageManager getmPackageManager() {
        if (mPackageManager == null) {
            mPackageManager = new PackageManager(context);
        }
        return mPackageManager;
    }

    /**
     * 单列模式获取StudentManager对象
     *
     * @return
     */
    public synchronized static ChannelManager getmChannelManager() {
        if (mChannelManager == null) {
            mChannelManager = new ChannelManager(context);
        }
        return mChannelManager;
    }

    public synchronized static AccountManager getmAccountManager() {
        if (mAccountManager == null) {
            mAccountManager = new AccountManager(context);
        }
        return mAccountManager;
    }
}
