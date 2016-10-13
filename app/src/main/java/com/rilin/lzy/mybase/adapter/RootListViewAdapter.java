package com.rilin.lzy.mybase.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rilin.lzy.mybase.R;

/**
 * 二级目录的根目录的数据适配器
 * @author Administrator
 *
 */
public class RootListViewAdapter extends BaseAdapter {


    private Context context;

    private LayoutInflater inflater;

    private String[] items;

    private int selectedPosition = -1;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }


    public RootListViewAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }



    public void setItems(String[] items) {
        this.items = items;
    }



    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
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
        if(convertView == null){

            holder = new ViewHolder();
            convertView = (View)inflater.inflate(R.layout.root_listview_item, parent , false);
            holder.item_text =(TextView) convertView.findViewById(R.id.item_name_text);
            holder.item_layout = (LinearLayout)convertView.findViewById(R.id.root_item);
            holder.item_icon = (ImageView) convertView.findViewById(R.id.item_name_icon);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        /**
         * 该项被选中时改变背景色
         */
        if(selectedPosition == position){
//          Drawable item_bg = new ColorDrawable(R.color.sub_list_color);
            holder.item_layout.setBackgroundColor(Color.WHITE);
        }else{
//          Drawable item_bg = new ColorDrawable(R.color.sub_list_color);
            holder.item_layout.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.item_text.setText(items[position]);
        // 设置icon
        holder.item_icon.setImageResource(R.mipmap.ic_launcher);
        return convertView;
    }
    class ViewHolder{
        TextView item_text;
        ImageView item_icon;
        LinearLayout item_layout;
    }

}
