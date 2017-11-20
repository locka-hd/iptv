package com.iptv.mktech.iptv.entiy;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by Administrator on 2017/10/9.
 */
@Entity
public class Channel implements Parcelable {
    @Id(autoincrement = true)
    private Long cid;
    @Transient
    private String stream_id;
    private String stream_name;
    @Transient
    private String stream_icon;
    @Transient
    private String view_order;
    @NotNull
    private String stream_url;
    private boolean isFav = false;
    private Long packageId;

    public String getStream_id() {
        return this.stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getStream_nam() {
        return this.stream_name;
    }

    public void setStream_nam(String stream_name) {
        this.stream_name = stream_name;
    }

    public String getStream_icon() {
        return this.stream_icon;
    }

    public void setStream_icon(String stream_icon) {
        this.stream_icon = stream_icon;
    }

    public String getView_order() {
        return this.view_order;
    }

    public void setView_order(String view_order) {
        this.view_order = view_order;
    }

    public String getStream_url() {
        return this.stream_url;
    }

    public void setStream_url(String stream_url) {
        this.stream_url = stream_url;
    }

    public Channel() {
    }

    @Generated(hash = 1251909734)
    public Channel(Long cid, String stream_name, @NotNull String stream_url,
            boolean isFav, Long packageId) {
        this.cid = cid;
        this.stream_name = stream_name;
        this.stream_url = stream_url;
        this.isFav = isFav;
        this.packageId = packageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.stream_id);
        dest.writeString(this.stream_name);
        dest.writeString(this.stream_icon);
        dest.writeString(this.view_order);
        dest.writeString(this.stream_url);
        dest.writeByte(this.isFav ? (byte) 1 : (byte) 0);
    }

    public boolean getIsFav() {
        return this.isFav;
    }

    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }
    public Long getCid() {
        return this.cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getStream_name() {
        return this.stream_name;
    }

    public void setStream_name(String stream_name) {
        this.stream_name = stream_name;
    }

    public Long getPackageId() {
        return this.packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    protected Channel(Parcel in) {
        this.stream_id = in.readString();
        this.stream_name = in.readString();
        this.stream_icon = in.readString();
        this.view_order = in.readString();
        this.stream_url = in.readString();
        this.isFav = in.readByte() != 0;
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel source) {
            return new Channel(source);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };
}
