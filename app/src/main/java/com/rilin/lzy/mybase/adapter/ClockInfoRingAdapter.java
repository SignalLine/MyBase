package com.rilin.lzy.mybase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rilin.lzy.mybase.R;

import java.util.List;

/**
 * Created by lzy on 16/9/30.
 */
public class ClockInfoRingAdapter extends BaseAdapter {

    private Context context;
    private List<String> list_ring;
    private LayoutInflater mInflater;


    public ClockInfoRingAdapter(Context context, List<String> list_ring){
        this.context = context;
        this.list_ring = list_ring;
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list_ring.size();
    }

    @Override
    public Object getItem(int position) {
        return list_ring.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_listview_clock_ring, null);
            viewHolder = new ViewHolder();

            viewHolder.mRing = (TextView) convertView.findViewById(R.id.ring_tv);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mRing.setText(list_ring.get(position));

        return convertView;
    }

    private static class ViewHolder{

        TextView mRing;

    }

}
