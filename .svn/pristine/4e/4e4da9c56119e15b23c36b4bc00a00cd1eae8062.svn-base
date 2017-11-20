package com.iptv.mktech.iptv.entiy;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/10/9.
 */
@Entity
public class Account implements Parcelable {
    private int status;
    private String message;
    private String osd;
    private String expire;
    private String user_agent;


    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOsd() {
        return this.osd;
    }

    public void setOsd(String osd) {
        this.osd = osd;
    }

    public String getExpire() {
        return this.expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getUser_agent() {
        return this.user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.message);
        dest.writeString(this.osd);
        dest.writeString(this.expire);
        dest.writeString(this.user_agent);
    }

    protected Account(Parcel in) {
        this.status = in.readInt();
        this.message = in.readString();
        this.osd = in.readString();
        this.expire = in.readString();
        this.user_agent = in.readString();
    }

    @Generated(hash = 1444422218)
    public Account(int status, String message, String osd, String expire,
            String user_agent) {
        this.status = status;
        this.message = message;
        this.osd = osd;
        this.expire = expire;
        this.user_agent = user_agent;
    }

    @Generated(hash = 882125521)
    public Account() {
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
