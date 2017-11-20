package com.iptv.mktech.iptv.utils.greenDao;

import android.content.Context;

import com.iptv.mktech.iptv.ChannelDao;
import com.iptv.mktech.iptv.entiy.Channel;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/10/12.
 */

public class ChannelManager extends BaseDao<Channel> {
    private ChannelDao mChannelDao;

    public ChannelManager(Context context) {
        super(context);
        mChannelDao = daoSession.getChannelDao();
    }

    public Channel loadById(Long stream_id) {
        return mChannelDao.load(stream_id);
    }

    public Long getID(Channel channel) {
        return mChannelDao.getKey(channel);
    }

    public List<Channel> getChannelByFav(boolean isFav) {
        QueryBuilder queryBuilder = mChannelDao.queryBuilder();
        List<Channel> channelList = queryBuilder.where(ChannelDao.Properties.IsFav.eq(isFav))
                .build().list();
        return channelList;
    }

    public Channel getChannelByName(String name) {
        Channel channel = mChannelDao.queryBuilder()
                .where(ChannelDao.Properties.Stream_name.eq(name))
                .build().unique();
        return channel;
    }

    public List<Channel> getAllChannels() {
        return mChannelDao.loadAll();
    }

    public void deleteAllChannels() {
        mChannelDao.deleteAll();
    }
    public void updateChannel(Channel channel){
//        mChannelDao.insert(channel);
        mChannelDao.update(channel);
    }

    public void deleteById(long id) {
        mChannelDao.deleteByKey(id);
    }

    public void deleteByIds(List<Long> ids) {
        mChannelDao.deleteByKeyInTx(ids);
    }

    public void deleteChannle(Channel channel) {
        mChannelDao.delete(channel);
    }


}
