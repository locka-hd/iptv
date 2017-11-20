package com.iptv.mktech.iptv.entiy;

/**
 * Created by Administrator on 2017/10/17.
 */

public class RestoreMessage {
    private int status;
    private String message;
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {

        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
