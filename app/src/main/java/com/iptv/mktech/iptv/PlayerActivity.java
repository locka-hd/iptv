package com.iptv.mktech.iptv;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.BehindLiveWindowException;

import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iptv.mktech.iptv.Adapter.ChannelListAdapter;
import com.iptv.mktech.iptv.ExoPlayer.ExoPlayerControl;
import com.iptv.mktech.iptv.ExoPlayer.ui.PlaybackControlView;
import com.iptv.mktech.iptv.entiy.Account;
import com.iptv.mktech.iptv.entiy.Channel;
import com.iptv.mktech.iptv.entiy.Package;
import com.iptv.mktech.iptv.utils.Constant;
import com.iptv.mktech.iptv.utils.DensityUtil;
import com.iptv.mktech.iptv.Widget.EditDialog;
import com.iptv.mktech.iptv.Widget.ExitDialog;
import com.iptv.mktech.iptv.Widget.InfoDialog;
import com.iptv.mktech.iptv.utils.IptvUtils;
import com.iptv.mktech.iptv.utils.VolleyUtils;
import com.iptv.mktech.iptv.Widget.WarningDialog;
import com.iptv.mktech.iptv.utils.greenDao.AccountManager;
import com.iptv.mktech.iptv.utils.greenDao.ChannelManager;
import com.iptv.mktech.iptv.utils.greenDao.DaoUtils;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class PlayerActivity extends Activity implements Player.EventListener, PlaybackControlView.VisibilityListener {
    public final static String TAG = PlayerActivity.class.getSimpleName();
    public static final String DRM_SCHEME_UUID_EXTRA = "drm_scheme_uuid";
    public static final String DRM_LICENSE_URL = "drm_license_url";
    public static final String DRM_KEY_REQUEST_PROPERTIES = "drm_key_request_properties";
    public static final String PREFER_EXTENSION_DECODERS = "prefer_extension_decoders";
    public static final String ACTION_VIEW = "com.google.android.exoplayer.demo.action.VIEW";
    public static final String EXTENSION_EXTRA = "extension";
    private static final CookieManager DEFAULT_COOKIE_MANAGER;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    private Handler mainHandler;
    private TrackGroupArray lastSeenTrackGroupArray = null;


    /////////////////////////
    RelativeLayout mRelativeLayout;
    private FrameLayout mFrameLayout;
    private View channelListLayout;
    private ImageButton iBRed;
    private ImageButton iBGreen;
    private ImageButton iBYellow;
    private ImageButton iBBlue;
    private ImageButton buttonFav;
    private ImageButton buttonUpdate;
    private TextView tvRed;
    private TextView tvGreen;
    private TextView tvYellow;
    private TextView tvBlue;
    private TextView tvFav;
    private TextView tvUpdate;
    private TextView mTvTitle;
    private SurfaceView mVideoSurfaceView = null;
    private ExitDialog mExitDialog = null;
    private InfoDialog mInfoDialog = null;
    private int mItemDIstance = 0;
    private PopupWindow mChannellsitPopupWindow = null;
    private PopupWindow mPlayControlPopupWindow = null;
    private PopupWindow mBufferingPopupWindow = null;
    private List<Package> mPackageList = null;
    private List<Channel> mAllChannelList = null;
    private List<Channel> mSearchChannelList = null;
    private List<Channel> mFavCahnnelList = null;
    private List<Channel> mCategoryChannelList = null;
    private List<String> mCategoryNameList = null;
    private List<String> mAllChannelNameList = null;
    //    private List<String> mCategoryChannelNameList = null;
    private String mCurrentCategoryName = null;
    private String mCurrentChannelName = null;
    private Channel mCurrentPlayChannle = null;

//    private List<Package> mPackageList;

    private int mLvItemFocusPosition = 0;
    private int mCurrentPlayChannelIndex = 0;
    private ListView mChannelListView;

    private ChannelListAdapter mChannelListAdapter;
    private WindowManager mWindowManager;

    private AccountDao mAccountDao;
    private AccountManager mAccountManager;
    private Account mAccount;
    private boolean isActivityVisiable = false;
    private boolean isNeedShowBufferingWin = false;


    private com.iptv.mktech.iptv.utils.greenDao.PackageManager mPackageManager = null;
    private ChannelManager mChannelManager = null;
    private ExoPlayerControl mExoPlayerControl;
    private boolean inErrorState = false;


    UUID drmSchemeUuid = null;
    String drmLicenseUrl = null;
    String[] keyRequestPropertiesArray = null;
    boolean preferExtensionDecoders = false;
    String action = null;
    Uri uri;
    String[] extensions;

    private WarningDialog mWaitWarningDialog = null;
    private WarningDialog mUpdateFailedDialog = null;
    private int mListViewMode = Constant.MODE_ALLCHANNELS;
    // Activity lifecycle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        setContentView(R.layout.player_activity);
        initView();
        initData();
        mExoPlayerControl.setShouldAutoPlay(true);
        mExoPlayerControl.clearResumePosition();

    }

    @Override
    public void onNewIntent(Intent intent) {
        mExoPlayerControl.releasePlayer();
        mExoPlayerControl.setShouldAutoPlay(true);
        mExoPlayerControl.clearResumePosition();
        setIntent(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            mExoPlayerControl.initializePlayer(false, uri);
            inErrorState = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayerControl.getPlayer() == null)) {
            mExoPlayerControl.initializePlayer(false, uri);
            inErrorState = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            mExoPlayerControl.releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            mExoPlayerControl.releasePlayer();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mExoPlayerControl.initializePlayer(false, uri);
            inErrorState = false;
        } else {
            mExoPlayerControl.showToast(R.string.storage_permission_denied);
            finish();
        }
    }

    public void showPlaybackControlWindown() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View playControlView = layoutInflater.inflate(R.layout.exo_cotrol_layout, null);
        mPlayControlPopupWindow = new PopupWindow(playControlView, DensityUtil.dip2px(getApplicationContext(), 1000), DensityUtil.dip2px(getApplicationContext(), 120));
        mPlayControlPopupWindow.setFocusable(true);
        mPlayControlPopupWindow.setOutsideTouchable(false);

        ImageButton pre = playControlView.findViewById(R.id.exo_prev);
        ImageButton next = playControlView.findViewById(R.id.exo_next);
        ImageButton pause = playControlView.findViewById(R.id.exo_pause);
        ImageButton play = playControlView.findViewById(R.id.exo_play);
        TextView tvNumber = playControlView.findViewById(R.id.tv_num);
        TextView tvTitle = playControlView.findViewById(R.id.tv_content);
        DecimalFormat decimalFormat = new DecimalFormat("0000");
        tvNumber.setText(decimalFormat.format(mCurrentPlayChannelIndex + 1));
        tvTitle.setText(mAllChannelList.get(mCurrentPlayChannelIndex).getStream_nam());
        if (mExoPlayerControl.getPlayer().getPlayWhenReady()) {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        } else {
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNextChannel();
            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPreviousChannel();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExoPlayerControl.pause();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExoPlayerControl.initializePlayer(false, Uri.parse(mAllChannelList.get(mCurrentPlayChannelIndex).getStream_url()));
            }
        });
        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mainHandler.removeMessages(Constant.MSG_DISMISS_CONTROL_WINDOWN);
                    mainHandler.sendEmptyMessageDelayed(Constant.MSG_DISMISS_CONTROL_WINDOWN, 5000);
                }
            }
        };
        pre.setOnFocusChangeListener(focusChangeListener);
        next.setOnFocusChangeListener(focusChangeListener);
        pause.setOnFocusChangeListener(focusChangeListener);
        play.setOnFocusChangeListener(focusChangeListener);
        pause.requestFocus();
        play.requestFocus();
        mPlayControlPopupWindow.showAtLocation(mFrameLayout, Gravity.BOTTOM, 0, 30);
        mainHandler.sendEmptyMessageDelayed(Constant.MSG_DISMISS_CONTROL_WINDOWN, 5000);

    }

    public void showBufferingPopupwindown() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View progressbarView = layoutInflater.inflate(R.layout.progress_bar_bufferiing, null);
        mBufferingPopupWindow = new PopupWindow(progressbarView, DensityUtil.dip2px(getApplicationContext(), 130), DensityUtil.dip2px(getApplicationContext(), 74));
        mBufferingPopupWindow.showAtLocation(mFrameLayout, Gravity.RIGHT, 100, -300);
    }

    public void showChannelsPopupWindown() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View channelListLayout = layoutInflater.inflate(R.layout.list_channel_layout, (ViewGroup) findViewById(R.id.lv_channel_list));
        mRelativeLayout = channelListLayout.findViewById(R.id.rl_root);
        mTvTitle = channelListLayout.findViewById(R.id.tv_title);
        iBRed = channelListLayout.findViewById(R.id.ib_red);
        iBGreen = channelListLayout.findViewById(R.id.ib_green);
        iBYellow = channelListLayout.findViewById(R.id.ib_yellow);
        iBBlue = channelListLayout.findViewById(R.id.ib_blue);
        buttonFav = channelListLayout.findViewById(R.id.ib_fav);
        buttonUpdate = channelListLayout.findViewById(R.id.ib_update);

        tvRed = channelListLayout.findViewById(R.id.tv_red);
        tvGreen = channelListLayout.findViewById(R.id.tv_green);
        tvYellow = channelListLayout.findViewById(R.id.tv_yellow);
        tvBlue = channelListLayout.findViewById(R.id.tv_blue);
        tvFav = channelListLayout.findViewById(R.id.tv_fav);
        tvUpdate = channelListLayout.findViewById(R.id.tv_update);


        mChannelListView = channelListLayout.findViewById(R.id.lv_channel_list);
        mChannellsitPopupWindow = new PopupWindow(channelListLayout, DensityUtil.dip2px(getApplicationContext(), 400),
                DensityUtil.dip2px(getApplicationContext(), 480));
        mListViewMode = IptvUtils.getListviewMode(getApplicationContext());
        Log.e("hehui", "lisrview mode==" + mListViewMode);
        switch (mListViewMode) {
            case Constant.MODE_ONE_CATEGORY_CHANNELS:
                mAllChannelList = mPackageManager.getChannelsByCategoryName(IptvUtils.getCurrentCategoryname(getApplicationContext()));
                break;
            case Constant.MODE_ALL_FAV_CHANNELS:
                mAllChannelList = mChannelManager.getChannelByFav(true);
                break;
            default:
                mAllChannelList = mChannelManager.getAllChannels();
                break;
        }

        mCurrentPlayChannelIndex = IptvUtils.getCurrentPlayChannelIndex(IptvUtils.getCurrentChannelName(getApplicationContext()), mAllChannelList);
        mChannelListAdapter = new ChannelListAdapter(getApplicationContext(), getmAllChannelNameList(mAllChannelList), mChannelListView);
//        mChannelListView.setChoiceMode(Constant.MODE_ALLCHANNELS);
        mChannelListView.setAdapter(mChannelListAdapter);

        mChannelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int mode = mChannelListView.getChoiceMode();
                if (mode == Constant.MODE_ALLCHANNELS || mode == Constant.MODE_SEARCH_CHANNELS || mode == Constant.MODE_FAV || mode == Constant.MODE_ONE_CATEGORY) {
                    IptvUtils.saveItemPosition(getApplicationContext(), view.getTop());
                    mCurrentPlayChannelIndex = i;
                    mCurrentPlayChannle = mAllChannelList.get(i);
                    String channelUrl = mCurrentPlayChannle.getStream_url();
                    //保存当前播放的的channel的名字
                    IptvUtils.saveCurrentChannelName(getApplicationContext(), mCurrentPlayChannle.getStream_name());
                    Log.e("hehui", "currentIndex==" + i + "===========" + IptvUtils.getCurrentPlayChannelIndex(IptvUtils.getCurrentChannelName(getApplicationContext()), mAllChannelList));
                    if (channelUrl != null) {
                        mExoPlayerControl.initializePlayer(false, Uri.parse(channelUrl));
                    }
                    Log.e("hehui", "mode==" + mode);
                    if (mode == Constant.MODE_FAV) {
                        IptvUtils.saveListviewMode(getApplicationContext(), Constant.MODE_ALL_FAV_CHANNELS);
                    }
                    if (mode == Constant.MODE_ONE_CATEGORY) {
                        Log.e("hehui", "111111111111111111111111111");
                        IptvUtils.saveListviewMode(getApplicationContext(), Constant.MODE_ONE_CATEGORY_CHANNELS);
                    }
                    if (mode == Constant.MODE_SEARCH_CHANNELS) {
                        IptvUtils.saveListviewMode(getApplicationContext(), Constant.MODE_ALLCHANNELS);
                    }
                    if (mode == Constant.MODE_ALLCHANNELS) {
                        IptvUtils.saveListviewMode(getApplicationContext(), Constant.MODE_ALLCHANNELS);
                    }
                    mChannellsitPopupWindow.dismiss();
                } else if (mChannelListView.getChoiceMode() == Constant.MODE_ALLCATEGORY) {//显示类别下的节目列表
                    Log.e("hehui", "222222222222222222222222222222");
                    mCurrentCategoryName = mCategoryNameList.get(i);
                    if (mCurrentCategoryName != null) {
                        IptvUtils.saveCurrentCategoryname(getApplicationContext(), mCurrentCategoryName);
                        mAllChannelList = mPackageManager.getChannelsByCategoryName(mCurrentCategoryName);
                        if (mAllChannelList != null) {
                            mAllChannelNameList = getmAllChannelNameList(mAllChannelList);
//                            IptvUtils.saveListviewMode(getApplicationContext(), Constant.MODE_ONE_CATEGORY);
                            refreshListView(Constant.MODE_ONE_CATEGORY, 0, 0);
                        }
                    }
                }
            }
        });
        mChannelListView.addFooterView(new View(PlayerActivity.this));//防止ListView失去focus导致的的按键事件不想赢而添加的emptyiew
        mChannelListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mChannelListAdapter.currentIndexChange(i);
                mLvItemFocusPosition = i;
                IptvUtils.saveFocusItemDistance(getApplicationContext(), view.getTop());
                if ((i + 1) % 10 == 0) {//listview 的一夜显示10个item
                    IptvUtils.saveLastItemDistance(getApplicationContext(), view.getTop());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mChannelListView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    Log.e("hehui", "key==" + keyEvent);
                    int choiceMode = mChannelListView.getChoiceMode();
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.KEYCODE_BACK:
                            if (mChannellsitPopupWindow != null && mChannellsitPopupWindow.isShowing()) {
                                mChannellsitPopupWindow.dismiss();
                                return true;
                            }
                            break;
                        case KeyEvent.KEYCODE_PROG_RED:
                            if (choiceMode != Constant.MODE_ALLCATEGORY) {
                                choiceMode = Constant.MODE_ALLCATEGORY;
                                mCategoryNameList = mPackageManager.getAllCategoryName();
                                refreshListView(choiceMode, 0, 0);
                            } else {
                                choiceMode = Constant.MODE_ALLCHANNELS;
                                mAllChannelList = mPackageManager.getAllChannels();
                                mAllChannelNameList = getmAllChannelNameList(mAllChannelList);
                                refreshListView(choiceMode, IptvUtils.getCurrentPlayChannelIndex(IptvUtils.getCurrentChannelName(getApplicationContext()), mAllChannelList), 0);
                            }
//                            IptvUtils.saveListviewMode(getApplicationContext(), choiceMode);
                            return true;
                        case KeyEvent.KEYCODE_PROG_BLUE://Add FAV  Blue
                            if (choiceMode == Constant.MODE_ALLCHANNELS) {
                                Channel selectedChannel = mAllChannelList.get(mLvItemFocusPosition);
                                saveFavChannel(selectedChannel);
                                if (mLvItemFocusPosition < mAllChannelList.size() - 1) {
                                    //TODO  如何所选中的Item距离顶部的距离自动增加
                                    mChannelListView.setSelection(mLvItemFocusPosition++);
//                                    mChannelListView.smoothScrollToPosition(mLvItemFocusPosition++);
                                } else {
                                    mChannelListView.setSelection(0);
                                }
                            } else if (choiceMode == Constant.MODE_FAV || choiceMode == Constant.MODE_ALL_FAV_CHANNELS
                                    || choiceMode == Constant.MODE_SEARCH_CHANNELS) {
                                choiceMode = Constant.MODE_ALLCHANNELS;
                                mAllChannelList = mPackageManager.getAllChannels();
                                mAllChannelNameList = getmAllChannelNameList(mAllChannelList);
                                refreshListView(choiceMode, IptvUtils.getCurrentPlayChannelIndex(IptvUtils.getCurrentChannelName(getApplicationContext()), mAllChannelList), 0);
                                return true;
                            }
                            break;
                        case KeyEvent.KEYCODE_F://show FAV List   KEYCODE_FAV
                            if (mChannelListView != null
                                    && mChannelListView.isShown()
                                    && choiceMode == Constant.MODE_ALLCHANNELS) {
                                mAllChannelList = mChannelManager.getChannelByFav(true);
                                if (mAllChannelList != null) {
                                    mAllChannelNameList = getmAllChannelNameList(mAllChannelList);
//                                    IptvUtils.saveListviewMode(getApplicationContext(), choiceMode);
                                    refreshListView(Constant.MODE_FAV, 0, 0);
                                }
                                return true;
                            }
                            break;
                        case KeyEvent.KEYCODE_PROG_GREEN:
                            Log.e("hehui", "choice mode ==" + choiceMode);
                            if (mChannelListView != null && mChannelListView.isShown()) {
                                if (choiceMode == Constant.MODE_FAV || choiceMode == Constant.MODE_ALL_FAV_CHANNELS) {//删除
                                    Log.e("hehui", "count==" + mChannelListView.getCount());
                                    if (mChannelListView.getCount() > 1) {
                                        Channel channel = mAllChannelList.get(mLvItemFocusPosition);
                                        channel.setIsFav(false);
                                        mChannelManager.updateChannel(channel);
                                        mAllChannelList.remove(mLvItemFocusPosition);
                                        if (mAllChannelList != null) {
                                            mAllChannelNameList = getmAllChannelNameList(mAllChannelList);
                                            mChannelListAdapter.notifyDataSetChanged();
                                        }
                                    }
                                } else if (choiceMode == Constant.MODE_ALLCHANNELS
                                        || choiceMode == Constant.MODE_ALLCATEGORY
                                        || choiceMode == Constant.MODE_SEARCH_CHANNELS) {
                                    showLoginInfoDialog();
                                }
                                return true;
                            }
                            break;
                        case KeyEvent.KEYCODE_PROG_YELLOW:
                            if (choiceMode == Constant.MODE_ALLCHANNELS
                                    || choiceMode == Constant.MODE_SEARCH_CHANNELS
                                    || choiceMode == Constant.MODE_ALL_FAV_CHANNELS
                                    || choiceMode == Constant.MODE_FAV) {
                                showSearchDialog();
                                return true;
                            }
                            break;
                        case KeyEvent.KEYCODE_DPAD_RIGHT:
                            int lastVisiblePosition = mChannelListView.getLastVisiblePosition();
                            if (lastVisiblePosition + 10 <= mChannelListAdapter.getCount() - 1) {
                                mChannelListView.setSelectionFromTop(mLvItemFocusPosition + 10, IptvUtils.getFocusItenDistance(getApplicationContext()));
                            } else {
                                mChannelListView.setSelection(mChannelListAdapter.getCount() - 1);
                            }
//                            else {
//                                mChannelListView.setSelection(lastVisiblePosition + 1);
//                            }
                            break;
                        case KeyEvent.KEYCODE_DPAD_LEFT:
//                            int firstVisiblePosition = mChannelListView.getFirstVisiblePosition();
                            if (mLvItemFocusPosition - 10 >= 0) {
                                mChannelListView.setSelectionFromTop(mLvItemFocusPosition - 10, IptvUtils.getFocusItenDistance(getApplicationContext()));
                            } else {
                                int position = mChannelListAdapter.getCount() % 10;//最后一列的个数
                                if (position == 0) {
                                    mChannelListView.setSelectionFromTop(mChannelListAdapter.getCount() - position + mLvItemFocusPosition, IptvUtils.getFocusItenDistance(getApplicationContext()));
                                } else if (position >= mLvItemFocusPosition + 1) {
                                    mChannelListView.setSelectionFromTop(mChannelListAdapter.getCount() - position + mLvItemFocusPosition, IptvUtils.getFocusItenDistance(getApplicationContext()));
                                } else {
                                    mChannelListView.setSelectionFromTop(mChannelListAdapter.getCount() - 1, 0);
                                }
                            }

                            break;
                        case KeyEvent.KEYCODE_DPAD_UP:
                            if ((mLvItemFocusPosition > 0)) {
                                if ((mLvItemFocusPosition) % 10 == 0) {
                                    mChannelListView.setSelectionFromTop(mLvItemFocusPosition - 1, IptvUtils.getFocusItenDistance(getApplicationContext()));
                                    return true;
                                }
                            } else {
                                mChannelListView.setSelection(mChannelListAdapter.getCount() - 1);//显示最后一个
                                return true;
                            }
                            break;
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            if (mLvItemFocusPosition + 1 < mChannelListAdapter.getCount()) {
                                if ((mLvItemFocusPosition + 1) % 10 == 0) {
                                    mChannelListView.setSelection(mLvItemFocusPosition + 1);
                                    return true;
                                }
                            } else {
                                mChannelListView.setSelection(0);
                                return true;
                            }

                            break;
                        case KeyEvent.KEYCODE_GUIDE:
                            if (choiceMode == Constant.MODE_ALLCHANNELS) {
                                showWaitWarningDialog();
                                VolleyUtils.queryIptvServer(getApplicationContext(), Constant.MODE_PACKAGES,
                                        IptvUtils.getCode(getApplicationContext()), new LoginActivity.CallBack() {
                                            @Override
                                            public void onRequestComplete(String result) {
                                                updateChannelList(result);
                                                dismissWarningDialog();
                                            }

                                            @Override
                                            public void onRequestFail(VolleyError error) {
                                                dismissWarningDialog();
                                                showUpdateFailedDialog();
                                                Timer timer = new Timer();
                                                timer.schedule(new TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                dismissUpdateFailedDialog();
                                                            }
                                                        });
                                                    }
                                                }, 3000);
                                            }
                                        });
                            }
                            break;
                        default:
                            break;
                    }
                }
                return mChannelListView.onKeyDown(keyEvent.getKeyCode(), keyEvent);
            }

        });
        Log.e("hehui", "mode==" + mListViewMode);
//        refreshListView(mListViewMode, mCurrentPlayChannelIndex, IptvUtils.getItemPosition(getApplicationContext()));
        if (mListViewMode == Constant.MODE_ALL_FAV_CHANNELS || mListViewMode == Constant.MODE_ONE_CATEGORY_CHANNELS) {
            setListViewForFavChannels(mCurrentPlayChannelIndex, IptvUtils.getItemPosition(getApplicationContext()));
        } else {
            mListViewMode = Constant.MODE_ALLCHANNELS;
            IptvUtils.saveListviewMode(getApplicationContext(), Constant.MODE_ALLCHANNELS);
            setListViewForAllChannels(mListViewMode, mCurrentPlayChannelIndex, IptvUtils.getItemPosition(getApplicationContext()));
        }
        mChannelListView.setFocusable(true);
        mChannellsitPopupWindow.setFocusable(true);
        mChannellsitPopupWindow.showAtLocation(mFrameLayout, Gravity.LEFT, 100, 0);
        mChannellsitPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mChannelListView = null;
            }
        });


    }

    private void updateChannelList(String result) {
        Gson gson = new Gson();
        List<Channel> channelList = new ArrayList<Channel>();
        List<Channel> favChannelList = mChannelManager.getChannelByFav(true);
        List<Package> packageList = gson.fromJson(result, new TypeToken<List<Package>>() {
        }.getType());
        if (packageList != null) {
            List<Package> olderPackageList = mPackageManager.loadAllPackages();
            if (olderPackageList != null && olderPackageList.size() > 0) {
                mPackageManager.deleteAllPackages();
                mChannelManager.deleteAllChannels();
            }
            for (Package pk : packageList) {
                mPackageManager.insertObject(pk);

                for (Channel channel : pk.getChannels()) {
                    channel.setPackageId(pk.getPid());
                    channelList.add(channel);
                }
            }
            if (favChannelList != null && favChannelList.size() > 0) {
                for (int i = 0; i < favChannelList.size(); i++) {
                    for (int j = 0; j < channelList.size(); j++) {
                        if (favChannelList.get(i).getStream_nam().equals(channelList.get(j).getStream_nam())) {
                            channelList.get(j).setIsFav(true);
                            break;
                        }
                    }
                }
            }
            mChannelManager.insertMultObject(channelList);
            if (IptvUtils.getListviewMode(getApplicationContext()) == Constant.MODE_ALLCHANNELS) {
                mAllChannelList = channelList;
                mAllChannelNameList = getmAllChannelNameList(mAllChannelList);
                if (mChannelListView != null && mChannelListView.isShown()) {
                    mChannelListAdapter.notifyDataSetChanged();
                }
            }
            if (channelList.get(mCurrentPlayChannelIndex).getStream_nam().equals(mCurrentPlayChannle.getStream_nam())) {
                if (!channelList.get((mCurrentPlayChannelIndex)).getStream_url().equals(mCurrentPlayChannle.getStream_url())) {
                    mExoPlayerControl.initializePlayer(false, Uri.parse(channelList.get(mCurrentPlayChannelIndex).getStream_url()));
                }
            } else {
                String name = mCurrentPlayChannle.getStream_name();
                int currentIndex = -1;
                for (int i = 0; i < channelList.size(); i++) {
                    if (channelList.get(i).getStream_nam().equals(name)) {
                        currentIndex = i;
                        break;
                    }
                }
                if (currentIndex >= 0) {
                    mExoPlayerControl.initializePlayer(false, Uri.parse(channelList.get(currentIndex).getStream_url()));
                } else {
                    mExoPlayerControl.initializePlayer(false, Uri.parse(channelList.get(mCurrentPlayChannelIndex).getStream_url()));
                }
            }
        }
    }

    private void saveFavChannel(Channel selectedChannel) {
        if (selectedChannel != null) {
            selectedChannel.setIsFav(true);
            List<Channel> channelList = mChannelManager.getChannelByFav(true);
            boolean isContain = false;
            if (channelList != null) {
                for (Channel channel : channelList) {
                    if (channel.getStream_nam().equals(selectedChannel.getStream_nam())) {
                        isContain = true;
                        break;
                    }
                }
                if (!isContain) {
                    mChannelManager.updateChannel(selectedChannel);
                }
            } else {
                mChannelManager.updateChannel(selectedChannel);
            }
        }
    }

    private void showUpdateFailedDialog() {
        if (mUpdateFailedDialog == null) {
            mUpdateFailedDialog = new WarningDialog(this);
            mUpdateFailedDialog.setText("Update Channel List Failed!");
        } else {
            if (!mUpdateFailedDialog.isShowing()) {
                mUpdateFailedDialog.show();
            }
        }
    }

    private void dismissUpdateFailedDialog() {
        if (mUpdateFailedDialog != null && mUpdateFailedDialog.isShowing()) {
            mUpdateFailedDialog.dismiss();
        }
    }

    private void dismissWarningDialog() {
        if (mWaitWarningDialog != null && mWaitWarningDialog.isShowing()) {
            mWaitWarningDialog.dismiss();
        }
    }

    private void showWaitWarningDialog() {
        mWaitWarningDialog = new WarningDialog(this);
        mWaitWarningDialog.setText("Please wait...");
        mWaitWarningDialog.show();

    }

    private void initView() {
        mFrameLayout = findViewById(R.id.root);
        mVideoSurfaceView = findViewById(R.id.sv_video);
    }

    private void initData() {
        Intent intent = getIntent();
        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.MSG_DISMISS_CONTROL_WINDOWN:
                        if (mPlayControlPopupWindow != null && mPlayControlPopupWindow.isShowing()) {
                            mPlayControlPopupWindow.dismiss();
                        }
                }
            }
        };
        mExoPlayerControl = ExoPlayerControl.getInstance(this, mVideoSurfaceView, mainHandler);

        mWindowManager = this.getWindowManager();
        DaoUtils.init(getApplicationContext());
        mAccountManager = DaoUtils.getmAccountManager();
        mAccount = mAccountManager.loadAccount();
        mPackageManager = DaoUtils.getmPackageManager();
        mChannelManager = DaoUtils.getmChannelManager();
        mPackageList = mPackageManager.loadAllPackages();
        mAllChannelList = mPackageManager.getAllChannels();
        mCurrentChannelName = IptvUtils.getCurrentChannelName(getApplicationContext());
        mCurrentPlayChannle = mAllChannelList.get(mCurrentPlayChannelIndex);
        mAllChannelNameList = new ArrayList<>();
        if (mAllChannelList != null && mAllChannelList.size() > 0) {
            for (Channel channel : mAllChannelList) {
                mAllChannelNameList.add(channel.getStream_nam());
            }
        }
        mCategoryNameList = mPackageManager.getAllCategoryName();


        drmSchemeUuid = intent.hasExtra(DRM_SCHEME_UUID_EXTRA) ? UUID.fromString(intent.getStringExtra(DRM_SCHEME_UUID_EXTRA)) : null;
        drmLicenseUrl = intent.getStringExtra(DRM_LICENSE_URL);
        keyRequestPropertiesArray = intent.getStringArrayExtra(DRM_KEY_REQUEST_PROPERTIES);
        preferExtensionDecoders = intent.getBooleanExtra(PREFER_EXTENSION_DECODERS, false);
        action = intent.getAction();
        extensions = new String[]{intent.getStringExtra(EXTENSION_EXTRA)};
        uri = intent.getData();

    }


    private List<String> getmAllChannelNameList(List<Channel> channelList) {
        mAllChannelNameList.clear();
        for (Channel channel : channelList) {
            mAllChannelNameList.add(channel.getStream_name());
        }
        return mAllChannelNameList;
    }

    private void refreshListView(int mode, int position, int offY) {
        if (mChannelListView != null && mChannelListView.isShown()) {
            mLvItemFocusPosition = 0;
            if (mode == Constant.MODE_ALLCHANNELS || mode == Constant.MODE_ONE_CATEGORY_CHANNELS || mode == Constant.MODE_ONE_CATEGORY) {
                setListViewForAllChannels(mode, position, offY);
            } else if (mode == Constant.MODE_ALLCATEGORY) {
                setListViewForALlCategory(position, offY);
            } else if (mode == Constant.MODE_FAV || mode == Constant.MODE_ALL_FAV_CHANNELS) {
                setListViewForFavChannels(position, offY);
            }
        }
    }

    private void setListViewForAllChannels(int mode, int position, int offY) {
        if (mode == Constant.MODE_ONE_CATEGORY_CHANNELS || mode == Constant.MODE_ONE_CATEGORY) {
            Log.e("hehui", "5555555555555555555");
            mTvTitle.setText(IptvUtils.getCurrentCategoryname(getApplicationContext()) + "(" + mAccount.getExpire() + ")");
        } else if (mode == Constant.MODE_ALL_FAV_CHANNELS) {
            Log.e("hehui", "66666666666666666666");
            mTvTitle.setText(getString(R.string.fav));
        } else {
            Log.e("hehui", "7777777777777777");
            mTvTitle.setText(getString(R.string.all_channels) + "(" + mAccount.getExpire() + ")");
        }
        mChannelListView.clearChoices();
        mChannelListView.setChoiceMode(mode);
        mChannelListAdapter.setItems(mAllChannelNameList);
        mChannelListView.setSelectionFromTop(position, offY);

        iBRed.setVisibility(View.VISIBLE);
        tvRed.setVisibility(View.VISIBLE);
        tvRed.setText(R.string.channels_categories);

        iBGreen.setVisibility(View.VISIBLE);
        tvGreen.setVisibility(View.VISIBLE);
        tvGreen.setText(R.string.channels_Login_info);

        iBYellow.setVisibility(View.VISIBLE);
        iBYellow.setBackgroundResource(R.mipmap.icon_yellot_button);
        tvYellow.setVisibility(View.VISIBLE);
        tvYellow.setText(R.string.channels_search);

        iBBlue.setVisibility(View.VISIBLE);
        tvBlue.setVisibility(View.VISIBLE);
        tvBlue.setText(R.string.channels_add_fav);


        buttonFav.setVisibility(View.VISIBLE);
        tvFav.setVisibility(View.VISIBLE);
        tvFav.setText(R.string.channels_fav_list);

        buttonUpdate.setVisibility(View.VISIBLE);
        tvUpdate.setVisibility(View.VISIBLE);
        tvUpdate.setText(R.string.channels_update_channels_list);
    }

    private void setListViewForSearchChannels(int position, int offY) {
        mTvTitle.setText(getString(R.string.search));
        mChannelListView.clearChoices();
        mChannelListView.setChoiceMode(Constant.MODE_SEARCH_CHANNELS);
        mChannelListAdapter.setItems(mAllChannelNameList);
        mChannelListView.setSelectionFromTop(position, offY);

        iBRed.setVisibility(View.VISIBLE);
        tvRed.setVisibility(View.VISIBLE);
        tvRed.setText(R.string.channels_categories);

        iBGreen.setVisibility(View.VISIBLE);
        tvGreen.setVisibility(View.VISIBLE);
        tvGreen.setText(R.string.channels_Login_info);

        iBYellow.setVisibility(View.VISIBLE);
        iBYellow.setBackgroundResource(R.mipmap.icon_yellot_button);
        tvYellow.setVisibility(View.VISIBLE);
        tvYellow.setText(R.string.channels_search);

        iBBlue.setVisibility(View.VISIBLE);
        tvBlue.setVisibility(View.VISIBLE);
        tvBlue.setText(R.string.all_channels);

        buttonFav.setVisibility(View.INVISIBLE);
        tvFav.setVisibility(View.INVISIBLE);

        buttonUpdate.setVisibility(View.INVISIBLE);
        tvUpdate.setVisibility(View.INVISIBLE);
    }

    private void setListViewForALlCategory(int position, int offY) {
        mTvTitle.setText(getString(R.string.channels_categories) + "(" + mAccount.getExpire() + ")");
        mChannelListView.clearChoices();
        mChannelListView.setChoiceMode(Constant.MODE_ALLCATEGORY);
        mChannelListAdapter.setItems(mCategoryNameList);
        mChannelListView.setSelectionFromTop(position, offY);
        iBRed.setVisibility(View.VISIBLE);
        tvRed.setVisibility(View.VISIBLE);
        tvRed.setText(R.string.all_channels);

        iBGreen.setVisibility(View.INVISIBLE);
        tvGreen.setVisibility(View.INVISIBLE);

        iBYellow.setVisibility(View.VISIBLE);
        iBYellow.setBackground(getResources().getDrawable(R.mipmap.icon_green_button));
        tvYellow.setVisibility(View.VISIBLE);
        tvYellow.setText(R.string.channels_Login_info);

        iBBlue.setVisibility(View.INVISIBLE);
        tvBlue.setVisibility(View.INVISIBLE);

        buttonFav.setVisibility(View.INVISIBLE);
        tvFav.setVisibility(View.INVISIBLE);

        buttonUpdate.setVisibility(View.INVISIBLE);
        tvUpdate.setVisibility(View.INVISIBLE);
    }

    private void setListViewForFavChannels(int position, int offY) {
        mTvTitle.setText(R.string.fav);
        mChannelListView.clearChoices();
        mChannelListView.setChoiceMode(Constant.MODE_FAV);
        mChannelListAdapter.setItems(mAllChannelNameList);
        mChannelListView.setSelectionFromTop(position, offY);
        iBRed.setVisibility(View.VISIBLE);
        tvRed.setVisibility(View.VISIBLE);
        tvRed.setText(R.string.channels_categories);
        iBGreen.setVisibility(View.VISIBLE);
        tvGreen.setVisibility(View.VISIBLE);
        tvGreen.setText(R.string.delete);
        iBYellow.setVisibility(View.VISIBLE);
        iBYellow.setBackground(getResources().getDrawable(R.mipmap.icon_yellot_button));
        tvYellow.setVisibility(View.VISIBLE);
        tvYellow.setText(R.string.channels_search);
        iBBlue.setVisibility(View.VISIBLE);
        tvBlue.setVisibility(View.VISIBLE);
        tvBlue.setText(R.string.all_channels);

        buttonFav.setVisibility(View.INVISIBLE);
        tvFav.setVisibility(View.INVISIBLE);

        buttonUpdate.setVisibility(View.INVISIBLE);
        tvUpdate.setVisibility(View.INVISIBLE);
    }

    private void dismissBufferingWin() {
        if (mBufferingPopupWindow != null && mBufferingPopupWindow.isShowing()) {
            mBufferingPopupWindow.dismiss();
        }
    }

    private List<Channel> getSearchResult(String key) {
        if (key != null && key.length() > 0) {
            List<Channel> resultList = new ArrayList<>();
            resultList.clear();
            List<Channel> channelList = mChannelManager.getAllChannels();
            if (channelList != null && channelList.size() > 0) {
                for (Channel channel : channelList) {
                    if (channel.getStream_nam().toLowerCase().contains(key.toLowerCase())) {
                        resultList.add(channel);
                    }
                }
            }
            if (resultList != null && resultList.size() > 0) {
                return resultList;
            }
        }
        return null;
    }

    private void showSearchDialog() {
        EditDialog editDialog = new EditDialog(this, new EditDialog.OnEditDialogInterface() {
            @Override
            public void onConfirm(final String text) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSearchChannelList = getSearchResult(text);
                        mAllChannelList = mSearchChannelList;
                        if (mSearchChannelList != null) {
                            mAllChannelNameList = getmAllChannelNameList(mSearchChannelList);
                            setListViewForSearchChannels(0, 0);
                        }
                    }
                });
            }

            @Override
            public void onCancel() {

            }
        });
        editDialog.show();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // If the event was not handled then see if the player view can handle it.
        Log.e("hehui", "keyevent:=" + event);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    showChannelsPopupWindown();
                    return true;
                case KeyEvent.KEYCODE_DPAD_UP:
                    playNextChannel();
                    return true;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    playPreviousChannel();
                    return true;
                case KeyEvent.KEYCODE_BACK:
                    if (mExitDialog == null || !mExitDialog.isShowing()) {
                        showExitDialog();
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_MENU:
                    showPlaybackControlWindown();
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void showExitDialog() {
        mExitDialog = new ExitDialog(this, new ExitDialog.OnExitDialogInterface() {
            @Override
            public void onConfirm() {
                mExitDialog.dismiss();
                mExitDialog = null;
                finish();
            }

            @Override
            public void onCancel() {
                mExitDialog.dismiss();
                mExitDialog = null;
            }
        });

        mExitDialog.show();

    }

    private void showLoginInfoDialog() {
        mInfoDialog = new InfoDialog(this);
        mInfoDialog.setCode(IptvUtils.getCode(getApplicationContext()));
        mInfoDialog.setExpireDate(mAccount.getExpire());
        mInfoDialog.show();

    }

    private void playNextChannel() {
        Channel nextChannel;
        if (mCurrentPlayChannelIndex >= 0 && mCurrentPlayChannelIndex < mAllChannelList.size() - 1) {
            mCurrentPlayChannelIndex++;
            nextChannel = mAllChannelList.get(mCurrentPlayChannelIndex);
        } else {
            mCurrentPlayChannelIndex = 0;
            nextChannel = mAllChannelList.get(0);
        }
        //保存当前播放的channel的名字
        IptvUtils.saveCurrentChannelName(getApplicationContext(), nextChannel.getStream_name());
        mExoPlayerControl.initializePlayer(false, Uri.parse(nextChannel.getStream_url()));
        showPlaybackControlWindown();
    }

    private void playPreviousChannel() {
        Channel nextChannel;
        if (mCurrentPlayChannelIndex > 0) {
            mCurrentPlayChannelIndex--;
            nextChannel = mAllChannelList.get(mCurrentPlayChannelIndex);
        } else {
            mCurrentPlayChannelIndex = mAllChannelList.size() - 1;
            nextChannel = mAllChannelList.get(mAllChannelList.size() - 1);
        }
        IptvUtils.saveCurrentChannelName(getApplicationContext(), nextChannel.getStream_name());
        mExoPlayerControl.initializePlayer(false, Uri.parse(nextChannel.getStream_url()));
        showPlaybackControlWindown();
    }

    @Override
    public void onVisibilityChange(int visibility) {
//        debugRootView.setVisibility(visibility);
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        // Do nothing.
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_ENDED || playbackState == Player.STATE_IDLE) {
            Log.e("hehui", "state==" + playbackState);
//            mExoPlayerControl.initializePlayer(false, Uri.parse(mAllChannelList.get(mSp.getInt(Constant.INDEX_KEY, 0)).getStream_url()));
            VolleyUtils.queryIptvServer(getApplicationContext(), Constant.MODE_PACKAGES,
                    IptvUtils.getCode(getApplicationContext()), new LoginActivity.CallBack() {
                        @Override
                        public void onRequestComplete(String result) {
                            if (result != null) {
                                updateChannelList(result);
                            }
                        }

                        @Override
                        public void onRequestFail(VolleyError error) {

                        }
                    });
            isNeedShowBufferingWin = false;
            dismissBufferingWin();
        } else if (playbackState == Player.STATE_BUFFERING) {
            if (isActivityVisiable) {
                showBufferingPopupwindown();
            } else {
                isNeedShowBufferingWin = true;
            }

        } else if (playbackState == Player.STATE_READY) {
            isNeedShowBufferingWin = false;
            dismissBufferingWin();
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
        // Do nothing.
    }

    @Override
    public void onPositionDiscontinuity() {
        if (inErrorState) {
            // This will only occur if the user has performed a seek whilst in the error state. Update the
            // resume position so that if the user then retries, playback will resume from the position to
            // which they seeked.
            mExoPlayerControl.updateResumePosition();
        }
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        // Do nothing.
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        // Do nothing.
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        if (errorString != null) {
            mExoPlayerControl.showToast(errorString);
        }
        inErrorState = true;
        if (isBehindLiveWindow(e)) {
            mExoPlayerControl.clearResumePosition();
            Channel channel = mAllChannelList.get(mCurrentPlayChannelIndex);
            if (channel != null) {
                mExoPlayerControl.initializePlayer(false, Uri.parse(channel.getStream_url()));
            }

            inErrorState = false;
        } else {
            mExoPlayerControl.updateResumePosition();
        }
    }

    @Override
    @SuppressWarnings("ReferenceEquality")
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        if (trackGroups != lastSeenTrackGroupArray) {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = mExoPlayerControl.getTrackSelection().getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
                        == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                    mExoPlayerControl.showToast(R.string.error_unsupported_video);
                }
                if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
                        == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                    mExoPlayerControl.showToast(R.string.error_unsupported_audio);
                }
            }
            lastSeenTrackGroupArray = trackGroups;
        }
    }

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            isActivityVisiable = true;
            if (isNeedShowBufferingWin) {
                showBufferingPopupwindown();
            }
        }
    }


}
