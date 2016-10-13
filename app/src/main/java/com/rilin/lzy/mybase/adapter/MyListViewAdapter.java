package com.rilin.lzy.mybase.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.bean.KeyValue;

import java.util.List;

/**
 * Created by dengyaming on 16/9/11.
 */
public class MyListViewAdapter extends BaseAdapter {

    private List<KeyValue> keyValueList;
    private Activity activity;

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private CallBack callBack;


    public MyListViewAdapter(List<KeyValue> keyValueList, Activity activity, CallBack callBack){
        this.keyValueList = keyValueList;
        this.activity = activity;
        this.callBack = callBack;
    }

    @Override
    public int getCount() {
        return keyValueList.size();
    }

    @Override
    public Object getItem(int position) {
        return keyValueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_person, null);
            viewHolder = new ViewHolder();
            viewHolder.leftTextView = (TextView) convertView.findViewById(R.id.left_text);
            viewHolder.rightTextView = (TextView) convertView.findViewById(R.id.right_text);
            viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.linear);
            viewHolder.linearLayout = (LinearLayout)convertView.findViewById(R.id.linear_top);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.leftTextView.setText(keyValueList.get(position).getKey());
        viewHolder.rightTextView.setText(keyValueList.get(position).getValue());

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCallBack().OnResult(position);
            }
        });

        if(position == 5 || position == 6 || position ==9 || position ==12){
            viewHolder.linearLayout.setVisibility(View.VISIBLE);
        }else {
            viewHolder.linearLayout.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder{
        TextView leftTextView, rightTextView;
        RelativeLayout relativeLayout;
        LinearLayout linearLayout;

    }

    public interface CallBack{
        void OnResult(int position);
    }
}
