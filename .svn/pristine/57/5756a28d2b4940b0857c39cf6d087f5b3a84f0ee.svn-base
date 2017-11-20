package com.iptv.mktech.iptv.entiy;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

import com.iptv.mktech.iptv.PackageDao;
import com.iptv.mktech.iptv.DaoSession;
import com.iptv.mktech.iptv.ChannelDao;

/**
 * Created by Administrator on 2017/10/9.
 */
@Entity
public class Package implements Parcelable {
    @Id(autoincrement = true)
    private Long pid;
    private int id;
    private String category_name;
    private String category_type;
    @Transient
    private String view_order;
    @Transient
    private int parent;
    @ToMany(referencedJoinProperty = "packageId" )
    private List<Channel> channels;

    @Override
    public int describeContents() {
        return 0;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 60666436)
    public List<Channel> getChannels() {
        if (channels == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChannelDao targetDao = daoSession.getChannelDao();
            List<Channel> channelsNew = targetDao._queryPackage_Channels(pid);
            synchronized (this) {
                if (channels == null) {
                    channels = channelsNew;
                }
            }
        }
        return channels;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.category_name);
        dest.writeString(this.category_type);
        dest.writeString(this.view_order);
        dest.writeInt(this.parent);
        dest.writeTypedList(this.channels);
    }
    public int getParent() {
        return this.parent;
    }
    public void setParent(int parent) {
        this.parent = parent;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1313248352)
    public synchronized void resetChannels() {
        channels = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCategory_name() {
        return this.category_name;
    }
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
    public String getCategory_type() {
        return this.category_type;
    }
    public void setCategory_type(String category_type) {
        this.category_type = category_type;
    }
    public String getView_order() {
        return this.view_order;
    }
    public void setView_order(String view_order) {
        this.view_order = view_order;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
    public Long getPid() {
        return this.pid;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 943005894)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPackageDao() : null;
    }
    public Package() {
    }

    protected Package(Parcel in) {
        this.id = in.readInt();
        this.category_name = in.readString();
        this.category_type = in.readString();
        this.view_order = in.readString();
        this.parent = in.readInt();
        this.channels = in.createTypedArrayList(Channel.CREATOR);
    }
    @Generated(hash = 724119932)
    public Package(Long pid, int id, String category_name, String category_type) {
        this.pid = pid;
        this.id = id;
        this.category_name = category_name;
        this.category_type = category_type;
    }

    public static final Parcelable.Creator<Package> CREATOR = new Parcelable.Creator<Package>() {
        @Override
        public Package createFromParcel(Parcel source) {
            return new Package(source);
        }

        @Override
        public Package[] newArray(int size) {
            return new Package[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 40750146)
    private transient PackageDao myDao;
}
