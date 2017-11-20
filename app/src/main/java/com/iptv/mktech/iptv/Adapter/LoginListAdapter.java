package com.iptv.mktech.iptv.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.iptv.mktech.iptv.R;


/**
 * Created by Administrator on 2017/10/16.
 */

public class LoginListAdapter extends BaseAdapter {
    public final static String TAG = ChannelListAdapter.class.getSimpleName();
    private final Context mContext;
    String[] mItems;
    private ListView mListView;
    private LoginListAdapter.ViewHolder mViewHolder;
    private int mCurrentFocusIndex = 0;
    private String mCode = null;

    public LoginListAdapter(Context context, String[] data, ListView listView) {
        this.mContext = context;
        this.mItems = data;
        this.mListView = listView;
    }

    @Override
    public int getCount() {

        return (mItems == null) ? 0 : mItems.length;
    }

    @Override
    public Object getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_list_login_layout, null);
            mViewHolder = new LoginListAdapter.ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (LoginListAdapter.ViewHolder) convertView.getTag();
        }
        if (position == 0 && mCode != null) {
            mViewHolder.tvTitle.setText(mItems[position] + mCode);
        } else {
            mViewHolder.tvTitle.setText(mItems[position]);
        }
        if (mCurrentFocusIndex == position) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.listview_bg));
        } else {
            convertView.setBackgroundColor(Color.BLACK);
        }


        return convertView;
    }

    public String[] getItems() {
        return mItems;
    }

    public void setItems(String[] items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    public void setmCode(String code) {
        mCode = code;
        notifyDataSetChanged();
    }


    public class ViewHolder {
        private TextView tvTitle;

        public ViewHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
        }
    }

    public void currentIndexChange(int position) {
        mCurrentFocusIndex = position;
        notifyDataSetChanged();
    }

}
