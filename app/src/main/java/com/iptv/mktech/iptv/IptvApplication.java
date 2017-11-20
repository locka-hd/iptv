package com.iptv.mktech.iptv;

import android.app.Application;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.iptv.mktech.iptv.utils.greenDao.ChannelManager;
import com.iptv.mktech.iptv.utils.greenDao.PackageManager;

/**
 * Created by Administrator on 2017/10/13.
 */

public class IptvApplication extends Application {
    private static ChannelManager mChannelManager;
    private static PackageManager mPackageManager;
    protected String userAgent;

    @Override
    public void onCreate() {
        super.onCreate();
//        initGreenDao();
        userAgent = Util.getUserAgent(this, "HD-IPTV");
    }

    private void initGreenDao() {
        mChannelManager = new ChannelManager(this);
        mPackageManager = new PackageManager(this);
    }

    public static PackageManager getmPackageManager() {
        return mPackageManager;
    }

    public static ChannelManager getmChannelManager() {
        return mChannelManager;
    }


    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }
}
