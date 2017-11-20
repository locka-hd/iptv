package com.iptv.mktech.iptv.utils;


import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2017/10/12.
 */

public class HttpUtils {
    public static String attachHttpGetParams(String url, String mode, String code) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("mode", mode);
        if (code != null) {
            params.put("code", code);
        }
        params.put("mac", Constant.MAC_ADDRESS);
        params.put("sn", Constant.SN);
        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");

        for (int i = 0; i < params.size(); i++) {
            String value = null;
            try {
                value = URLEncoder.encode(values.next(), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            stringBuffer.append(keys.next() + "=" + value);
            if (i != params.size() - 1) {
                stringBuffer.append("&");
            }
        }
        return url + stringBuffer.toString();
    }
}
