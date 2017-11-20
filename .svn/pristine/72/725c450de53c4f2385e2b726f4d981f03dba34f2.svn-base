package com.iptv.mktech.iptv.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iptv.mktech.iptv.LoginActivity;
import com.iptv.mktech.iptv.utils.gzip.GzipStringRequest;

import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 */

public class VolleyUtils {
    public static void queryIptvServer(Context context, String mode, String code, final LoginActivity.CallBack callBack) {
        Request gzipStringRequest = new GzipStringRequest(Request.Method.GET,
                HttpUtils.attachHttpGetParams(Constant.URL, mode, code),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Log.e("222",response);
                        Log.e("hehui","11111");
//                        List<Package> packageList = gson.fromJson(response, new TypeToken<List<Package>>() {
//                        }.getType());
                        Log.e("hehui","22222");
                        callBack.onRequestComplete(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onRequestFail(error);

            }

        });
        VolleyController.getInstance(context).addToRequestQueue(gzipStringRequest, mode);
    }
}
