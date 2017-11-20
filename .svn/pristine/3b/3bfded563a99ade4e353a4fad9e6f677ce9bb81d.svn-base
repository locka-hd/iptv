package com.iptv.mktech.iptv.utils;

/**
 * Created by Administrator on 2017/10/12.
 */

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyController {
    private static final String TAG = "VolleyController";
    private RequestQueue reqQueue;
    private static VolleyController mInstance;
    private Context mContext;

    private VolleyController(Context context) {
        mContext = context;
    }

    public static VolleyController getInstance(Context context) {
        if (mInstance == null) {
            synchronized (VolleyController.class) {
                if (mInstance == null) {
                    mInstance = new VolleyController(context);
                }
            }
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (reqQueue == null) {
            synchronized (VolleyController.class) {
                if (reqQueue == null) {
                    reqQueue = Volley.newRequestQueue(mContext);
                }
            }
        }
        return reqQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (reqQueue != null) {
            reqQueue.cancelAll(tag);
        }
    }
}
