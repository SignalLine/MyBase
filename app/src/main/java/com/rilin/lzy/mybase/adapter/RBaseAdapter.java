package com.rilin.lzy.mybase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by lzy on 16/9/11.
 */
public abstract class RBaseAdapter<V> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<V> mValueList;

    public interface OnItemClickListener<V>{
        void onItemClick(V itemValue,int viewId,int position);
    }
    //接口,回调分发点击事件
    private OnItemClickListener<V> mOnItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return createViewHolder(parent.getContext(),parent);
    }

    @Override
    @SuppressWarnings("unchecked")//一点会是BaseViewHolder的子类,因为createViewHolder()的返回值
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //BaseViewHolder是我抽象出来的RecyclerView.ViewHolder的基类，下面会有详细讲解
        ((BaseViewHolder) holder).setData(mValueList.get(position), position, mOnItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mValueList == null ? 0 : mValueList.size();
    }

    /**
     * 设置每个Item的点击事件
     * @param listener
     */
    public void setOnClickListener(OnItemClickListener<V> listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 刷新数据
     * @param valueList 新的数据列表
     */
    public void refreshData(List<V> valueList) {
        this.mValueList = valueList;
        notifyDataSetChanged();
    }

    /**
     * 生成viewHolder
     * @param context
     * @param parent
     * @return
     */
    protected abstract BaseViewHolder createViewHolder(Context context,ViewGroup parent);

    /**
     * ViewHolder基类
     * @param <V>
     */
    public abstract class BaseViewHolder<V> extends RecyclerView.ViewHolder{

        public BaseViewHolder(Context context,ViewGroup root,int layoutRes){
            super(LayoutInflater.from(context).inflate(layoutRes,root,false));
            ButterKnife.bind(this,itemView);
        }

        /**
         * 方便子类进行需要Context的操作
         * @return
         */
        public Context getContext(){
            return  itemView.getContext();
        }

        /**
         * 抽象方法,绑定数据
         * 让子类自行对数据和view进行绑定
         *
         * @param itemValue Item的数据
         * @param position
         * @param listener
         */
        protected abstract void bindData(V itemValue,int position,OnItemClickListener listener);

        /**
         * 用于传递数据和信息
         * @param itemValue
         * @param position
         * @param listener
         */
        public void setData(V itemValue,int position,OnItemClickListener listener){
            bindData(itemValue,position,listener);
        }
    }

}

/*
示例代码
public class SampleAdapter extends BaseAdapter<String> {
    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent) {
        //SampleViewHolder继承自BaseViewHolder
        return new SampleViewHolder(context, parent);
    }
}

public class SampleViewHolder extends BaseViewHolder<String> {

    //一个普通的可点击的TextView
    @Bind(R.id.is_tv_content)
    TextView mIsTvContent;

    public SampleViewHolder(Context context, ViewGroup root) {
        //修改了构造方法，在这里显式指定Layout ID
        super(context, root, R.layout.item_sample);
    }

    @Override
    protected void bindData(final String itemValue, final int position, final OnItemClickListener listener) {
        //在这里完成控件的初始化，将其与数据绑定
        if (itemValue != null) {
            mIsTvContent.setText(itemValue);
        }
        //如果需要有点击事件，就通过listener把它传递出去
        mIsTvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果外界没有调用BaseAdapter.setOnClickListener()，
                //listener就为null
                if(listener == null){
                    return;
                }
                //listener不为null就将这个事件传递给外界处理
                listener.onItemClick(itemValue , v.getId() , position);
            }
        });
    }
}*/
