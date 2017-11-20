package com.iptv.mktech.iptv.utils.greenDao;

import android.content.Context;
import android.util.Log;

import com.iptv.mktech.iptv.PackageDao;
import com.iptv.mktech.iptv.entiy.Channel;
import com.iptv.mktech.iptv.entiy.Package;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/13.
 */

public class PackageManager extends BaseDao<Package> {
    private PackageDao mPackageDao;

    public PackageManager(Context context) {
        super(context);
        mPackageDao = daoSession.getPackageDao();
    }

    public List<Channel> getChannelsByCategoryName(String categoryName) {
        Package pk = mPackageDao.queryBuilder().
                where(PackageDao.Properties.Category_name.eq(categoryName))
                .build().unique();
        if (pk != null) {
            List<Channel> channelList = pk.getChannels();
            return pk.getChannels();
        } else {
            return null;
        }
    }

    public List<String> getAllCategoryName() {
        List<Package> packageList = mPackageDao.loadAll();
        if (packageList != null) {
            List<String> strList = new ArrayList<>();
            for (Package pk : packageList) {
                strList.add(pk.getCategory_name());
            }
            return strList;
        } else {
            return null;
        }
    }

    public List<Channel> getAllChannels() {
        List<String> stringList = getAllCategoryName();
        if (stringList != null) {
            List<Channel> channelList = new ArrayList<>();
            for (int i = 0; i < stringList.size(); i++) {
                channelList.addAll(getChannelsByCategoryName(stringList.get(i)));
            }
            if (channelList != null && channelList.size() > 0) {
                return channelList;
            }
        }
        return null;

    }

    public List<Package> loadAllPackages() {
        return mPackageDao.loadAll();
    }


    public void updateMultObject(final List<Package> objects) {
        if (null == objects || objects.isEmpty()) {
            return;
        }
        try {
            mPackageDao.updateInTx(objects);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void deleteAllPackages() {
        mPackageDao.deleteAll();
    }
}
