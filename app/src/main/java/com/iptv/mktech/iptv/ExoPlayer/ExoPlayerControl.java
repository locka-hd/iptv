package com.iptv.mktech.iptv.ExoPlayer;


import android.content.Context;

import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.ViewGroup;

import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.iptv.mktech.iptv.ExoPlayer.ui.SimpleExoPlayerView;
import com.iptv.mktech.iptv.IptvApplication;
import com.iptv.mktech.iptv.PlayerActivity;

import java.util.UUID;

/**
 * Created by Administrator on 2017/10/19.
 */

public class ExoPlayerControl {
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private Context mContext;
    private SimpleExoPlayer mPlayer;
    private Handler mMainHandler;
    private DataSource.Factory mMediaDataSourceFactory;

    private boolean shouldAutoPlay;
    private int resumeWindow;
    private long resumePosition;
    private EventLogger mEventLogger;
    private DefaultTrackSelector trackSelector;
    private TrackSelectionHelper trackSelectionHelper;
    private boolean inErrorState;
    private Object imaAdsLoader; // com.google.android.exoplayer2.ext.ima.ImaAdsLoader
    private Uri loadedAdTagUri;
    private ViewGroup adOverlayViewGroup;
    private SurfaceView surfaceView;
    private TrackSelection.Factory adaptiveTrackSelectionFactory;
    public static ExoPlayerControl mExoPlayerControl = null;


    private ExoPlayerControl(Context context, SurfaceView surfaceView, Handler handler) {
        mContext = context;
        this.surfaceView = surfaceView;
        mMediaDataSourceFactory = buildDataSourceFactory(true);
        mMainHandler = handler;
        adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        trackSelectionHelper = new TrackSelectionHelper(trackSelector, adaptiveTrackSelectionFactory);
        mEventLogger = new EventLogger(trackSelector);
    }

    public static synchronized ExoPlayerControl getInstance(Context context, SurfaceView surfaceView, Handler handler) {
        if (mExoPlayerControl == null) {
            return new ExoPlayerControl(context, surfaceView, handler);
        } else {
            return mExoPlayerControl;
        }
    }


    private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(UUID uuid, String licenseUrl,
                                                                              String[] keyRequestPropertiesArray) throws UnsupportedDrmException {
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
                buildHttpDataSourceFactory(false));
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
                        keyRequestPropertiesArray[i + 1]);
            }
        }
        return new DefaultDrmSessionManager<>(uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback,
                null, mMainHandler, mEventLogger);
    }


    /**
     * Returns a new HttpDataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *                          DataSource factory.
     * @return A new HttpDataSource factory.
     */
    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return ((IptvApplication) mContext.getApplicationContext())
                .buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }


    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *                          DataSource factory.
     * @return A new DataSource factory.
     */
    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((IptvApplication) mContext.getApplicationContext())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }


    public MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
                : Util.inferContentType("." + overrideExtension);
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultSsChunkSource.Factory(mMediaDataSourceFactory), mMainHandler, mEventLogger);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultDashChunkSource.Factory(mMediaDataSourceFactory), mMainHandler, mEventLogger);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, mMediaDataSourceFactory, mMainHandler, mEventLogger);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, mMediaDataSourceFactory, new DefaultExtractorsFactory(),
                        mMainHandler, mEventLogger);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    public void updateResumePosition() {
        resumeWindow = mPlayer.getCurrentWindowIndex();
        resumePosition = Math.max(0, mPlayer.getContentPosition());
    }

    public void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }


    public void initializePlayer(boolean preferExtensionDecoders, Uri uri) {
        boolean needNewPlayer = mPlayer == null;
        if (needNewPlayer) {
            @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode =
                    ((IptvApplication) mContext.getApplicationContext()).useExtensionRenderers()
                            ? (preferExtensionDecoders ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                            : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                            : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
            DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory((PlayerActivity) mContext, null, extensionRendererMode);
            mPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
            mEventLogger = new EventLogger(trackSelector);
            mPlayer.setRepeatMode(1);
            mPlayer.addListener((PlayerActivity) mContext);
            mPlayer.addListener(mEventLogger);
            mPlayer.addMetadataOutput(mEventLogger);
            mPlayer.setAudioDebugListener(mEventLogger);
            mPlayer.setVideoDebugListener(mEventLogger);
            mPlayer.setVideoSurface(surfaceView.getHolder().getSurface());
            mPlayer.setPlayWhenReady(shouldAutoPlay);
        }
        mPlayer.setPlayWhenReady(shouldAutoPlay);
        if (Util.maybeRequestReadExternalStoragePermission((PlayerActivity) mContext, uri)) {
            // The player will be reinitialized if the permission is granted.
            return;
        }
        MediaSource mediaSource = buildMediaSource(uri, null);
        boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            mPlayer.seekTo(resumeWindow, resumePosition);
        }
        mPlayer.prepare(mediaSource, !haveResumePosition, false);
        inErrorState = false;
    }


    public void releasePlayer() {
        if (mPlayer != null) {
            shouldAutoPlay = mPlayer.getPlayWhenReady();
            updateResumePosition();
            mPlayer.release();
            mPlayer = null;
            trackSelector = null;
            trackSelectionHelper = null;
            mEventLogger = null;
        }
    }

    public void showToast(int messageId) {
        showToast(mContext.getString(messageId));
    }

    public void showToast(String message) {
        Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public DefaultTrackSelector getTrackSelection() {
        return trackSelector;
    }

    public void setShouldAutoPlay(boolean need) {
        shouldAutoPlay = need;
    }

    public SimpleExoPlayer getPlayer() {
        return mPlayer;
    }

    public void pause() {
        if (mPlayer != null) {
            if (mPlayer.getPlayWhenReady()) {
                mPlayer.setPlayWhenReady(false);
            }
        }
    }
}
