package com.rilin.lzy.mybase.my;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;


public class BottomSheetActivity extends BaseActivity implements View.OnClickListener {

    private Button mButton1, mButton2;
    private BottomSheetBehavior mBehavior;
    private NestedScrollView mNestedScrollView;

    private boolean hasRequest;

    @Override
    public int setViewId() {
        return R.layout.activity_bottom_sheet;
    }

    @Override
    public void initView() {
        mButton1 = getView(R.id.btn_info);
        mButton2 = getView(R.id.btn_select);
        mBehavior = BottomSheetBehavior.from(findViewById(R.id.scroll));
        //add // 设置当关闭时 底部 的高度 app:behavior_peekHeight="50dp"
        mBehavior.setPeekHeight(10);
        //这里为红色的部分---------添加了这行代码
        mBehavior.setHideable(false);//设置当拉升到底部是否可以隐藏  app:behavior_hideable="true"
        mNestedScrollView = (NestedScrollView) findViewById(R.id.scroll);
    }

    @Override
    public void initData() {
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                /**
                 *public static final int STATE_DRAGGING = 1;  //拖动
                 public static final int STATE_SETTLING = 2;//沉降中
                 public static final int STATE_EXPANDED = 3;//打开了
                 public static final int STATE_COLLAPSED = 4;//关闭了
                 public static final int STATE_HIDDEN = 5;//隐藏了
                 */
//                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//
//                } else {
//                    mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (!hasRequest && mBehavior.getPeekHeight() == 10 && slideOffset > 0) {
                    hasRequest = true;
                    updateOffsets(bottomSheet);
//                    bottomSheet.requestLayout();
                }
            }
        });
    }

    private void updateOffsets(View view) {

        // Manually invalidate the view and parent to make sure we get drawn pre-M
        if (Build.VERSION.SDK_INT < 23) {
            tickleInvalidationFlag(view);
            final ViewParent vp = view.getParent();
            if (vp instanceof View) {
                tickleInvalidationFlag((View) vp);
            }
        }
    }

    private static void tickleInvalidationFlag(View view) {
        final float y = ViewCompat.getTranslationY(view);
        ViewCompat.setTranslationY(view, y + 1);
        ViewCompat.setTranslationY(view, y);
    }

    @Override
    public void requestData() {

    }

    @Override
    public void destroyResource() {

    }

    //介绍点击事件
    public void intro() {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.scroll));
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            //STATE_COLLAPSED
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.btn_select:
                    select();
                    break;
                case R.id.btn_info:
                    intro();
                    break;
            }
        }
    }

    public void select() {
        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(this)
                .inflate(R.layout.list, null);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(recyclerView);
        dialog.show();
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String text) {
                Toast.makeText(BottomSheetActivity.this, text, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    static class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private OnItemClickListener mItemClickListener;

        public void setOnItemClickListener(OnItemClickListener li) {
            mItemClickListener = li;
        }

        @Override
        public Adapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new Holder(item);
        }

        @Override
        public void onBindViewHolder(final Adapter.Holder holder, int position) {
            holder.tv.setText("item " + position);
            if (mItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(holder.getLayoutPosition(),
                                holder.tv.getText().toString());
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tv;

            public Holder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.text);
            }
        }

        interface OnItemClickListener {
            void onItemClick(int position, String text);
        }
    }
}
