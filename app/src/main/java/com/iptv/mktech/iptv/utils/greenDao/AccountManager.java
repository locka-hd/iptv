package com.iptv.mktech.iptv.utils.greenDao;

import android.content.Context;

import com.iptv.mktech.iptv.AccountDao;
import com.iptv.mktech.iptv.entiy.Account;

/**
 * Created by Administrator on 2017/10/16.
 */

public class AccountManager extends BaseDao<Account> {
    private AccountDao mAccountDao;

    public AccountManager(Context context) {
        super(context);
        mAccountDao = daoSession.getAccountDao();
    }


    public Account loadAccount() {
        return mAccountDao.queryBuilder().build().unique();
    }

    public void insertAccount(Account account) {
        if (account != null) {
            mAccountDao.insert(account);
        }
    }

    public void deleteAccount(Account account) {
        if (account != null) {
            mAccountDao.delete(account);
        }
    }

    public void deleteAllAccount() {
        mAccountDao.deleteAll();
    }
}
