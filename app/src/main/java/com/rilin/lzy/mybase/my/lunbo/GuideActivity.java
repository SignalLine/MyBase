package com.rilin.lzy.mybase.my.lunbo;

import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rilin.lzy.mybase.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGABannerUtil;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = GuideActivity.class.getSimpleName();
    private TextView mSkipTv;
    private Button mEnterBtn;
    private BGABanner mBackgroundBanner;
    private BGABanner mForegroundBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
        setListener();
        processLogic();

    }

    private void initView() {
        mSkipTv = (TextView) findViewById(R.id.tv_guide_skip);
        mEnterBtn = (Button) findViewById(R.id.btn_guide_enter);
        mBackgroundBanner = (BGABanner) findViewById(R.id.banner_guide_background);
        mForegroundBanner = (BGABanner) findViewById(R.id.banner_guide_foreground);
    }

    private void setListener() {
        mSkipTv.setOnClickListener(this);
        mEnterBtn.setOnClickListener(this);

        //监听页码切换时间,控制跳过按钮和进入主界面的显示和隐藏
        mForegroundBanner.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == mForegroundBanner.getItemCount() - 2){
                    ViewCompat.setAlpha(mEnterBtn,positionOffset);
                    ViewCompat.setAlpha(mSkipTv,1.0f - positionOffset);

                    if(positionOffset > 0.5f){
                        mEnterBtn.setVisibility(View.VISIBLE);
                        mSkipTv.setVisibility(View.GONE);
                    }else {
                        mEnterBtn.setVisibility(View.GONE);
                        mSkipTv.setVisibility(View.VISIBLE);
                    }
                }else if(position == mForegroundBanner.getItemCount() - 1){
                    mSkipTv.setVisibility(View.GONE);
                    mEnterBtn.setVisibility(View.VISIBLE);
                    ViewCompat.setAlpha(mEnterBtn, 1.0f);
                } else {
                    mSkipTv.setVisibility(View.VISIBLE);
                    ViewCompat.setAlpha(mSkipTv, 1.0f);
                    mEnterBtn.setVisibility(View.GONE);
                }
            }
        });
    }

    private void processLogic() {
        mBackgroundBanner.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mForegroundBanner.setOverScrollMode(View.OVER_SCROLL_NEVER);

        //初始化方法一:通过传入数据模型饼结婚Adapter的方式初始化
        mBackgroundBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                ((ImageView)view).setImageResource((int)model);
            }
        });
        mBackgroundBanner.setData(Arrays.asList(R.mipmap.uoko_guide_background_1,R.mipmap.uoko_guide_background_2,R.mipmap.uoko_guide_background_3),null);

        //初始化方法2:通过直接传入视图集合的方式初始化
        List<View> views = new ArrayList<>();
        views.add(BGABannerUtil.getItemImageView(this,R.mipmap.uoko_guide_foreground_1));
        views.add(BGABannerUtil.getItemImageView(this,R.mipmap.uoko_guide_foreground_2));
        views.add(BGABannerUtil.getItemImageView(this,R.mipmap.uoko_guide_foreground_3));
        mForegroundBanner.setData(views);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_guide_enter || v.getId() == R.id.tv_guide_skip){
            startActivity(new Intent(GuideActivity.this, ListWithHeadActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在界面可见时候,给背景Banner设置一个白色背景,避免滑动过程中两个Banner都设置透明度后能看到Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
    }
}
