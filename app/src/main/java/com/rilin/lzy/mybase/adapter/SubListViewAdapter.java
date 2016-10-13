package com.rilin.lzy.mybase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rilin.lzy.mybase.R;

/**
 * 二级目录的子目录的数据适配器
 * @author Administrator
 *
 */
public class SubListViewAdapter extends BaseAdapter {

    private String[][] sub_items;
    private Context context;
    private int root_position;
    private LayoutInflater inflater;

    public SubListViewAdapter(Context context, String[][] sub_items,
                              int position) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.sub_items = sub_items;
        this.root_position = position;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return sub_items[root_position].length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return sub_items[root_position][position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = (View) inflater.inflate(R.layout.root_listview_item,
                    parent, false);
            holder.item_text = (TextView) convertView
                    .findViewById(R.id.item_name_text);
            holder.item_icon = (ImageView)convertView.findViewById(R.id.item_name_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_text.setText(sub_items[root_position][position]);
        //设置icon
        holder.item_icon.setImageResource(R.mipmap.ic_launcher);
        return convertView;
    }
    class ViewHolder{
        TextView item_text;
        ImageView item_icon;
    }

}
