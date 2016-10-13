package com.rilin.lzy.mybase.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rilin.lzy.mybase.R;

import java.util.Date;

/**
 * Created by rilintech on 16/10/8.
 */
public class RefreshableListView extends ListView {
    // 更新时间
    private TextView head_lastUpdatedTextView = null;
    // 添加控制器（是否可以刷新）
    private boolean isCanPullRefresh = true;
    // 添加监听器
    private OnExplainListener mExplainListener = null;
    // 添加的加载更多按钮
    private View mFooterContainer;
    private TextView mButtonExplain;
    private boolean isCanExplain=true;
    // 控制是否启用下拉刷新控制器
    public void setCanPullRefresh(boolean isCanPullRefresh) {
        this.isCanPullRefresh = isCanPullRefresh;
    }

    // ----------------------------------
    private View mHeaderContainer = null;
    private View mHeaderView = null;
    private ImageView mArrow = null;
    private ProgressBar mProgress = null;
    private TextView mText = null;
    private float mY = 0;
    private float mHistoricalY = 0;
    private int mHistoricalTop = 0;
    private int mInitialHeight = 0;
    private boolean mFlag = false;
    private boolean mArrowUp = false;
    private boolean mIsRefreshing = false;
    private int mHeaderHeight = 0;
    private OnRefreshListener mListener = null;

    private static final int REFRESH = 0;
    private static final int NORMAL = 1;
    private static final int HEADER_HEIGHT_DP = 62;

    public RefreshableListView(Context context) {
        super(context);
        initialize();
    }

    public RefreshableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public RefreshableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public void setOnRefreshListener(OnRefreshListener l) {
        mListener = l;
    }

    // 添加 监听
    public void setOnExplainListener(OnExplainListener l) {
        mExplainListener = l;
    }

    public void completeRefreshing() {
        mProgress.setVisibility(View.INVISIBLE);
        mArrow.setVisibility(View.VISIBLE);
        mHandler.sendMessage(mHandler.obtainMessage(NORMAL, mHeaderHeight, 0));
        mIsRefreshing = false;
        invalidateViews();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanPullRefresh) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mHandler.removeMessages(REFRESH);
                    mHandler.removeMessages(NORMAL);
                    mY = mHistoricalY = ev.getY();
                    if (mHeaderContainer.getLayoutParams() != null)
                        mInitialHeight = mHeaderContainer.getLayoutParams().height;
                    break;
                case MotionEvent.ACTION_MOVE:
                    try {
                        mHistoricalTop = getChildAt(0).getTop();
                    } catch (IndexOutOfBoundsException e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!mIsRefreshing) {
                        if (mArrowUp) {
                            startRefreshing();
                            mHandler.sendMessage(mHandler.obtainMessage(REFRESH,
                                    (int) (ev.getY() - mY) / 2 + mInitialHeight, 0));
                        } else {
                            if (getChildAt(0).getTop() == 0)
                                mHandler.sendMessage(mHandler
                                        .obtainMessage(NORMAL,
                                                (int) (ev.getY() - mY) / 2
                                                        + mInitialHeight, 0));
                        }
                    } else {
                        mHandler.sendMessage(mHandler.obtainMessage(REFRESH,
                                (int) (ev.getY() - mY) / 2 + mInitialHeight, 0));
                    }
                    mFlag = false;
                    break;
            }
        }
        try {
            return super.onTouchEvent(ev);
        } catch (Exception e) {
            // TODO: handle exception
//				e.printStackTrace();
            return false;
        }
//		return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isCanPullRefresh) {
            if (ev.getAction() == MotionEvent.ACTION_MOVE
                    && getFirstVisiblePosition() == 0) {
                float direction = ev.getY() - mHistoricalY;
                int height = (int) (ev.getY() - mY) / 2 + mInitialHeight;
                if (height < 0)
                    height = 0;

                // Scrolling downward
                if (direction > 0) {
                    // Refresh bar is extended if top pixel of the first item is
                    // visible
                    if (getChildAt(0).getTop() == 0) {
                        if (mHistoricalTop < 0) {
                            mY = ev.getY();
                            mHistoricalTop = 0;
                        }
                        // Extends refresh bar
                        setHeaderHeight(height);

                        // Stop list scroll to prevent the list from
                        // overscrolling
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        mFlag = false;
                    }
                } else if (direction < 0) {
                    // Scrolling upward
                    try {// ·ÀÖ¹ÏÂ±êÔ½½ç
                        // Refresh bar is shortened if top pixel of the
                        // first
                        // item is visible
                        if (getChildAt(0).getTop() == 0) {
                            setHeaderHeight(height);

                            // If scroll reaches top of the list, list scroll is
                            // enabled
                            if (getChildAt(1) != null
                                    && getChildAt(1).getTop() <= 1 && !mFlag) {
                                ev.setAction(MotionEvent.ACTION_DOWN);
                                mFlag = true;
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }

                mHistoricalY = ev.getY();
            }
        }
        try {

            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }

    private void initialize() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeaderContainer = inflater.inflate(R.layout.refreshable_list_header,
                null);
        mHeaderView = mHeaderContainer
                .findViewById(R.id.refreshable_list_header);
        mArrow = (ImageView) mHeaderContainer
                .findViewById(R.id.refreshable_list_arrow);
        mProgress = (ProgressBar) mHeaderContainer
                .findViewById(R.id.refreshable_list_progress);
        mText = (TextView) mHeaderContainer
                .findViewById(R.id.refreshable_list_text);
        addHeaderView(mHeaderContainer);
        // 更新时间
        head_lastUpdatedTextView = (TextView) mHeaderContainer
                .findViewById(R.id.head_lastUpdatedTextView);

        head_lastUpdatedTextView.setText("最近更新:" + new Date().toLocaleString());

        // 添加的footView 更多按钮
        mFooterContainer = inflater.inflate(R.layout.explain, null);
        mButtonExplain = (TextView) mFooterContainer
                .findViewById(R.id.tv_explain);
        mButtonExplain.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mExplainListener != null) {
                    if(isCanExplain){
                        mExplainListener.onExplain();
                    }
                }
            }
        });
        addFooterView(mFooterContainer);

        mHeaderHeight = (int) (HEADER_HEIGHT_DP * getContext().getResources()
                .getDisplayMetrics().density);
        setHeaderHeight(0);
    }

    private void setHeaderHeight(int height) {
        if(isCanPullRefresh){
            if (height <= 1)
                mHeaderView.setVisibility(View.GONE);
            else
                mHeaderView.setVisibility(View.VISIBLE);

            // Extends refresh bar
            LayoutParams lp = (LayoutParams) mHeaderContainer.getLayoutParams();
            if (lp == null)
                lp = new LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT);
            lp.height = height;
            mHeaderContainer.setLayoutParams(lp);

            // Refresh bar shows up from bottom to top
            LinearLayout.LayoutParams headerLp = (LinearLayout.LayoutParams) mHeaderView
                    .getLayoutParams();
            if (headerLp == null)
                headerLp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT);
            headerLp.topMargin = -mHeaderHeight + height;
            mHeaderView.setLayoutParams(headerLp);

            if (!mIsRefreshing) {
                // If scroll reaches the trigger line, start refreshing
                if (height > mHeaderHeight && !mArrowUp) {
                    mArrow.startAnimation(AnimationUtils.loadAnimation(
                            getContext(), R.anim.rotate));
                    mText.setText("松开可以刷新");
                    rotateArrow();
                    mArrowUp = true;
                } else if (height < mHeaderHeight && mArrowUp) {
                    mArrow.startAnimation(AnimationUtils.loadAnimation(
                            getContext(), R.anim.rotate));
                    mText.setText("下拉可以刷新");
                    rotateArrow();
                    mArrowUp = false;
                }
            }
        }
    }

    private void rotateArrow() {
        Drawable drawable = mArrow.getDrawable();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.save();
        canvas.rotate(180.0f, canvas.getWidth() / 2.0f,
                canvas.getHeight() / 2.0f);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        canvas.restore();
        mArrow.setImageBitmap(bitmap);
    }

    private void startRefreshing() {
        mArrow.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.VISIBLE);
        mText.setText("加载中...");
        mIsRefreshing = true;

        if (mListener != null)
            mListener.onRefresh();
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int limit = 0;
            switch (msg.what) {
                case REFRESH:
                    limit = mHeaderHeight;
                    break;
                case NORMAL:
                    limit = 0;
                    break;
            }

            // Elastic scrolling
            if (msg.arg1 >= limit) {
                setHeaderHeight(msg.arg1);
                int displacement = (msg.arg1 - limit) / 5;
                if (displacement == 0)
                    mHandler.sendMessage(mHandler.obtainMessage(msg.what,
                            msg.arg1 - 1, 0));
                else
                    mHandler.sendMessage(mHandler.obtainMessage(msg.what,
                            msg.arg1 - displacement, 0));
            }
        }

    };

    // 底部加载更多按钮
    public interface OnRefreshListener {
        public void onRefresh();
    }

    public interface OnExplainListener {
        public void onExplain();
    }

    public void setFootViewIsOver() {
        isCanExplain=false;
        mButtonExplain.setFocusable(false);
        mButtonExplain.setClickable(false);
        mButtonExplain.setText("所有信息加载完毕");
        mButtonExplain.setVisibility(VISIBLE);
    }
    //底部更多按钮 可以自定义文字和文字左边图片（图片用R中id传递）
    public void setFootViewIsMore(String prompt,int img) {
        if(notEmptyOrNull(prompt)){
            isCanExplain=false;
            mButtonExplain.setFocusable(false);
            mButtonExplain.setClickable(false);
            mButtonExplain.setText("  "+prompt);
        }else{
            isCanExplain=true;
            mButtonExplain.setFocusable(true);
            mButtonExplain.setClickable(true);
            mButtonExplain.setText("加载更多...");
        }
        if(img!=0){
            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.mipmap.add_new_business_circle_bg);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mButtonExplain.setCompoundDrawables(drawable, null, null, null); //设置左图标
            mButtonExplain.setTextSize(20);
        }
        mButtonExplain.setVisibility(VISIBLE);
    }

    public void setFootViewIsGone() {
        mButtonExplain.setEnabled(false);
        this.removeFooterView(mFooterContainer);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        // TODO Auto-generated method stub
        try {
            super.setAdapter(adapter);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("RefreshableListView set adapter Exception");
        }
    }

    public static boolean notEmptyOrNull(String str) {
        if (str != null && !"".equals(str))
            return true;
        return false;
    }
}
