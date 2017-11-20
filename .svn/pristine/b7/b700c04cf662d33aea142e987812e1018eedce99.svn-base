package com.iptv.mktech.iptv.utils.gzip;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import com.android.volley.NetworkResponse;

public class GzipUtil {public static final String HEADER_ENCODING = "Content-Encoding";
	public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	public static final String ENCODING_GZIP = "gzip";

	public static final long HEADER_ENCODING_RANGE = 1024;//1kb

	/**
	 *
	 * @param str
	 * @param charset
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] compress(String str, String charset)
			throws IOException, UnsupportedEncodingException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		try {
			gzip.write(str.getBytes(charset));
			gzip.close();
			return out.toByteArray();
		} catch (Exception e) {
			throw new IOException(e);
		} finally {

			if (gzip != null) {
				gzip.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * gzip压缩
	 * @param buffer
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] compress(byte[] buffer) throws IOException,
			UnsupportedEncodingException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		try {
			gzip.write(buffer);
			gzip.close();
			return out.toByteArray();
		} finally {

			if (gzip != null) {
				gzip.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 判断是否需要gzip解压
	 * @param response
	 * @return
	 */
	public static boolean isGzipped(NetworkResponse response) {
		Map<String, String> headers = response.headers;
		return headers != null
				&& !headers.isEmpty()
				&& (headers.containsKey(HEADER_ENCODING) && headers.get(
				HEADER_ENCODING).equalsIgnoreCase(ENCODING_GZIP));
	}

	/**
	 * gzip解压
	 * @param compressed
	 * @return
	 * @throws IOException
	 */
	public static byte[] decompressResponse(byte[] compressed)
			throws IOException {
		ByteArrayOutputStream baos = null;
		try {
			int size;
			ByteArrayInputStream memstream = new ByteArrayInputStream(compressed);
			GZIPInputStream gzip = new GZIPInputStream(memstream);
			final int buffSize = 256;
			byte[] tempBuffer = new byte[buffSize];
			baos = new ByteArrayOutputStream();
			while ((size = gzip.read(tempBuffer, 0, buffSize)) != -1) {
				baos.write(tempBuffer, 0, size);
			}
			return baos.toByteArray();
		} finally {
			if (baos != null) {
				baos.close();
			}
		}
	}
}