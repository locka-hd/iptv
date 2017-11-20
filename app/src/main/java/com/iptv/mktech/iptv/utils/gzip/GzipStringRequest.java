package com.iptv.mktech.iptv.utils.gzip;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.iptv.mktech.iptv.utils.XOR;

public class GzipStringRequest extends StringRequest {
    private boolean mGzipEnabled = true;

    public GzipStringRequest(int method, String url, Listener<String> listener,
                             ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public GzipStringRequest(String url, Listener<String> listener,
                             ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        if (mGzipEnabled) {
            headers.put(GzipUtil.HEADER_ACCEPT_ENCODING, GzipUtil.ENCODING_GZIP);
        }
        return headers;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed = null;
        try {
            if (mGzipEnabled && GzipUtil.isGzipped(response)) {
                byte[] data = GzipUtil.decompressResponse(response.data);
                parsed = XOR.decode(data);
            }
            if (parsed == null) {
                parsed = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));
            }
            return Response.success(parsed,
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * Disables GZIP compressing (enabled by default)
     */
    public void disableGzip() {
        mGzipEnabled = false;
    }

}
