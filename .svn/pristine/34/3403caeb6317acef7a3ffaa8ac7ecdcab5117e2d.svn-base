package com.iptv.mktech.iptv.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.TextView;

import com.iptv.mktech.iptv.R;
import com.iptv.mktech.iptv.entiy.Channel;

import java.text.DecimalFormat;
import java.util.List;

import static android.graphics.Color.RED;

public class ChannelListAdapter extends BaseAdapter {
    public final static String TAG = ChannelListAdapter.class.getSimpleName();
    private final Context mContext;
    List<String> mItems;
    private ListView mListView;
    private ViewHolder mViewHolder;
    private int mCurrentFocusIndex = 0;
    DecimalFormat decimalFormat = new DecimalFormat("0000");

    public ChannelListAdapter(Context context, List<String> data, ListView listView) {

        this.mContext = context;
        this.mItems = data;
        this.mListView = listView;
    }

    @Override
    public int getCount() {

        return (mItems == null) ? 0 : mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_list_channel_list, null);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.tvNumber.setText(decimalFormat.format(position + 1));
        mViewHolder.tvChannelTitle.setText(mItems.get(position));
        if (mCurrentFocusIndex == position) {
            mViewHolder.tvNumber.setBackground(mContext.getResources().getDrawable(R.drawable.textview_boarder));
            mViewHolder.tvChannelTitle.setBackground(mContext.getResources().getDrawable(R.drawable.textview_boarder));
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.listview_item_focus));
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.listview_item_bg));
            mViewHolder.tvNumber.setBackground(null);
            mViewHolder.tvChannelTitle.setBackground(null);
        }


        return convertView;
    }

    public List<String> getItems() {
        return mItems;
    }

    public void setItems(List<String> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }


    public class ViewHolder {
        private TextView tvNumber;
        private TextView tvChannelTitle;

        public ViewHolder(View view) {
            tvNumber = (TextView) view.findViewById(R.id.tv_number);
            tvChannelTitle = (TextView) view.findViewById(R.id.tv_channel_title);
        }
    }

    public void currentIndexChange(int position) {
        mCurrentFocusIndex = position;
        notifyDataSetChanged();
    }

}
