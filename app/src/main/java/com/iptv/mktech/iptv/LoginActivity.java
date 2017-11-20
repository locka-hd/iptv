package com.iptv.mktech.iptv;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iptv.mktech.iptv.Adapter.LoginListAdapter;
import com.iptv.mktech.iptv.entiy.Account;
import com.iptv.mktech.iptv.entiy.Channel;
import com.iptv.mktech.iptv.entiy.Package;
import com.iptv.mktech.iptv.entiy.RestoreMessage;
import com.iptv.mktech.iptv.utils.Constant;
import com.iptv.mktech.iptv.Widget.EditDialog;
import com.iptv.mktech.iptv.utils.IptvUtils;
import com.iptv.mktech.iptv.utils.NetworkManager;
import com.iptv.mktech.iptv.utils.VolleyUtils;
import com.iptv.mktech.iptv.Widget.WarningDialog;
import com.iptv.mktech.iptv.utils.greenDao.AccountManager;
import com.iptv.mktech.iptv.utils.greenDao.ChannelManager;
import com.iptv.mktech.iptv.utils.greenDao.DaoUtils;
import com.iptv.mktech.iptv.utils.greenDao.PackageManager;
import com.iptv.mktech.iptv.utils.VolleyController;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends Activity {
    private VolleyController mVolleyController;
    private PackageManager mPackageManager;
    private ChannelManager mChannelmanager;
    private ListView mLvLogin;
    private LoginListAdapter mLoginListAdapter;
    private String[] mDataList;
    private int mCurrentFocusIndex = 0;
    private Account mAccount;
    private AccountManager mAccountManager;
    private String mCode;
    private WarningDialog mLoginWaitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_acrivty);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mLvLogin = (ListView) findViewById(R.id.lv_login);
    }

    private void initData() {
        if (!NetworkManager.isNetworkAvailable(getApplicationContext())) {
            showNetworkUnaviableDialog();
        }
        DaoUtils.init(getApplicationContext());
        mCode = IptvUtils.getCode(getApplicationContext());
        mAccountManager = DaoUtils.getmAccountManager();
        mPackageManager = new PackageManager(getApplicationContext());
        mChannelmanager = new ChannelManager(getApplicationContext());
        mVolleyController = VolleyController.getInstance(getApplicationContext());
        mDataList = getResources().getStringArray(R.array.login_info);
        mLoginListAdapter = new LoginListAdapter(getApplicationContext(), mDataList, mLvLogin);
        mLvLogin.setAdapter(mLoginListAdapter);
        if (mCode != null && !mCode.equals("null")) {
            mLoginListAdapter.setmCode(mCode);
        }
    }

    private void initEvent() {
        mLvLogin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentFocusIndex = i;
                switch (mCurrentFocusIndex) {
                    case 0://input  code
                        showInputCodeDialog();
                        break;
                    case 1://confirm
                        if (mCode != null && !mCode.equals("null")) {
                            mLvLogin.setVisibility(View.INVISIBLE);
                            showWaitingDialog();
                            VolleyUtils.queryIptvServer(getApplicationContext(), Constant.MODE_ACTIVE, mCode, new CallBack() {
                                @Override
                                public void onRequestComplete(String result) {
                                    Gson gson = new Gson();
                                    mAccount = gson.fromJson(result, Account.class);
                                    Log.e("hehui", "status==" + mAccount.getStatus());
                                    if (mAccount != null && mAccount.getStatus() == 100) {
                                        mAccountManager.deleteAllAccount();// 先查询在存储
                                        mAccountManager.insertAccount(mAccount);
                                        if (mLoginWaitingDialog != null && mLoginWaitingDialog.isShowing()) {
                                            mLoginWaitingDialog.dismiss();
                                            showAccountMsgDialog(mAccount);
                                        }
                                        IptvUtils.saveCode(getApplicationContext(), mCode);
                                    } else {
                                        if (mLoginWaitingDialog != null && mLoginWaitingDialog.isShowing()) {
                                            mLoginWaitingDialog.dismiss();
                                        }
                                        showLoginFailedDialog();
                                    }
                                }

                                @Override
                                public void onRequestFail(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), R.string.network_request_error, Toast.LENGTH_LONG).show();
                                    if (mLoginWaitingDialog != null && mLoginWaitingDialog.isShowing()) {
                                        mLoginWaitingDialog.dismiss();
                                    }
                                    mLvLogin.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Please input your code first!", Toast.LENGTH_LONG);
                        }
                        break;
                    case 2://delete code
                        break;
                    case 3://restore code
                        VolleyUtils.queryIptvServer(getApplicationContext(), Constant.MODE_RESTORE, null, new CallBack() {
                            @Override
                            public void onRequestComplete(String result) {
                                if (result != null) {
                                    Gson gson = new Gson();
                                    RestoreMessage message = gson.fromJson(result, RestoreMessage.class);
                                    if (message.getStatus() == 100) {
                                        Log.e("hehui", "code==" + message.getCode());
                                        mCode = message.getCode();
                                        IptvUtils.saveCode(getApplicationContext(), mCode);
                                        if (mLvLogin != null && mLvLogin.isShown()) {
                                            mLoginListAdapter.setmCode(mCode);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onRequestFail(VolleyError error) {
                                Toast.makeText(getApplicationContext(), R.string.network_request_error, Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    default:
                        break;

                }
            }
        });
        mLvLogin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentFocusIndex = i;
                mLoginListAdapter.currentIndexChange(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public interface CallBack {
        void onRequestComplete(String result);

        void onRequestFail(VolleyError error);
    }

    private void showInputCodeDialog() {
        EditDialog editDialog = new EditDialog(this, new EditDialog.OnEditDialogInterface() {
            @Override
            public void onConfirm(String text) {
                if (text != null && text.length() > 0) {
                    mCode = text;
                    mLoginListAdapter.setmCode(mCode);
                }
            }

            @Override
            public void onCancel() {

            }
        });
        editDialog.show();
    }

    private void showLoginFailedDialog() {
        final WarningDialog warningDialog = new WarningDialog(this);
        warningDialog.setText("Login failed" + "\n");
        warningDialog.show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                warningDialog.dismiss();
                finish();
            }
        }, 5000);
    }

    private void showNetworkUnaviableDialog() {
        final WarningDialog warningDialog = new WarningDialog(this);
        warningDialog.setText("Network unavailable ! " + "\n");
        warningDialog.show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                warningDialog.dismiss();
                finish();
            }
        }, 2000);
    }

    private void showWaitingDialog() {
        if (mLoginWaitingDialog == null) {
            mLoginWaitingDialog = new WarningDialog(this);
            mLoginWaitingDialog.setText("Please Wait..." + "\n");
        }
        if (!mLoginWaitingDialog.isShowing()) {
            mLoginWaitingDialog.show();
        }
    }


    private void showAccountMsgDialog(Account account) {
        final WarningDialog msgDialog = new WarningDialog(this);
        String str = String.format("%s\n %s\nExpire:%s\n", account.getMessage(), account.getOsd(), account.getExpire());
        msgDialog.setText(str);
        msgDialog.show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                msgDialog.dismiss();
            }
        }, 2000);
        msgDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                showWaitingDialog();
                VolleyUtils.queryIptvServer(getApplicationContext(), Constant.MODE_PACKAGES, mCode, new CallBack() {
                    @Override
                    public void onRequestComplete(String result) {
                        Gson gson = new Gson();
                        List<Channel> channelList = new ArrayList<Channel>();
                        List<Channel> favChannelList = mChannelmanager.getChannelByFav(true);
                        List<Package> packageList = gson.fromJson(result, new TypeToken<List<Package>>() {
                        }.getType());
                        if (packageList != null) {
                            List<Package> olderPackageList = mPackageManager.loadAllPackages();
                            if (olderPackageList != null && olderPackageList.size() > 0) {
                                mPackageManager.deleteAllPackages();
                                mChannelmanager.deleteAllChannels();
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
                            mChannelmanager.insertMultObject(channelList);
                            Log.e("hehui", "channelListSize==" + channelList.size());
                            showPlayerActivity(channelList);

                        }
                    }

                    @Override
                    public void onRequestFail(VolleyError error) {
                        Log.e("hehui", "request error");
                    }
                });
            }
        });
    }

    private void showPlayerActivity(List<Channel> channelList) {
        if (channelList != null) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(channelList.get(IptvUtils.
                    getCurrentPlayChannelIndex(IptvUtils.getCurrentChannelName(getApplicationContext()), channelList)).getStream_url()));
            intent.setAction(PlayerActivity.ACTION_VIEW);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginWaitingDialog = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLoginWaitingDialog != null && mLoginWaitingDialog.isShowing()) {
            mLoginWaitingDialog.dismiss();
        }
    }

}
