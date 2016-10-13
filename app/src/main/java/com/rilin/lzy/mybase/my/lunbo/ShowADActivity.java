package com.rilin.lzy.mybase.my.lunbo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.application.MyApplication;
import com.rilin.lzy.mybase.model.BannerModel;
import com.rilin.lzy.mybase.model.Engine;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.TransitionEffect;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowADActivity extends AppCompatActivity implements BGABanner.OnItemClickListener, BGABanner.Adapter {

    private BGABanner mDefaultBanner;//第一个
    private BGABanner mCubeBanner;//2
    private BGABanner mAccordionBanner;//3
    private BGABanner mFlipBanner;//4
    private BGABanner mRotateBanner;//5
    private BGABanner mAlphaBanner;//6
    private BGABanner mZoomFadeBanner;//7
    private BGABanner mFadeBanner;//8
    private BGABanner mZoomCenterBanner;//9
    private BGABanner mZoomBanner;//10
    private BGABanner mStackBanner;//11
    private BGABanner mZoomStackBanner;//12
    private BGABanner mDepthBanner;//13

    private Engine mEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ad);

        mEngine = MyApplication.getApplication().getEngine();

        initView();
        setListener();
        loadData();

    }

    private void initView() {
        mDefaultBanner = (BGABanner) findViewById(R.id.banner_main_default);
        mCubeBanner = (BGABanner) findViewById(R.id.banner_main_cube);
        mAccordionBanner = (BGABanner) findViewById(R.id.banner_main_accordion);
        mFlipBanner = (BGABanner) findViewById(R.id.banner_main_flip);
        mRotateBanner = (BGABanner) findViewById(R.id.banner_main_rotate);
        mAlphaBanner = (BGABanner) findViewById(R.id.banner_main_alpha);
        mZoomFadeBanner = (BGABanner) findViewById(R.id.banner_main_zoomFade);
        mFadeBanner = (BGABanner) findViewById(R.id.banner_main_fade);
        mZoomCenterBanner = (BGABanner) findViewById(R.id.banner_main_zoomCenter);
        mZoomBanner = (BGABanner) findViewById(R.id.banner_main_zoom);
        mStackBanner = (BGABanner) findViewById(R.id.banner_main_stack);
        mZoomStackBanner = (BGABanner) findViewById(R.id.banner_main_zoomStack);
        mDepthBanner = (BGABanner) findViewById(R.id.banner_main_depth);
    }

    private void setListener() {
        mDefaultBanner.setOnItemClickListener(this);
        mCubeBanner.setOnItemClickListener(this);
    }

    private void loadData() {
        loadData(mDefaultBanner, 1);
        loadData(mCubeBanner, 2);
        loadData(mAccordionBanner, 3);
        loadData(mFlipBanner, 4);
        loadData(mRotateBanner, 5);
        loadData(mAlphaBanner, 6);
        loadData(mZoomFadeBanner, 3);
        loadData(mFadeBanner, 4);
        loadData(mZoomCenterBanner, 5);
        loadData(mZoomBanner, 6);
        loadData(mStackBanner, 3);
        loadData(mZoomStackBanner, 4);
        loadData(mDepthBanner, 5);
    }

    private void loadData(final BGABanner banner, int count){
        mEngine.fetchItemsWithItemCount(count).enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
                BannerModel bannerModel = response.body();
                //设置adapter
                banner.setAdapter(ShowADActivity.this);
                banner.setData(bannerModel.imgs,bannerModel.tips);
            }

            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {
                Toast.makeText(ShowADActivity.this, "网络数据加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {
        Toast.makeText(ShowADActivity.this, "点击了第" + (position + 1) + "页", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
        Glide.with(ShowADActivity.this)
                .load(model)
                .placeholder(R.mipmap.holder)
                .error(R.mipmap.holder)
                .into((ImageView)view);
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.tv_main_select_page_one://选择第一页
                mDefaultBanner.setCurrentItem(0);
                break;
            case R.id.tv_main_select_page_two://选择第2页
                mDefaultBanner.setCurrentItem(1);
                break;
            case R.id.tv_main_select_page_three://选择第3页
                mDefaultBanner.setCurrentItem(2);
                break;
            case R.id.tv_main_select_page_four://选择第4页
                mDefaultBanner.setCurrentItem(3);
                break;
            case R.id.tv_main_select_page_five://选择第5页
                mDefaultBanner.setCurrentItem(4);
                break;
            case R.id.tv_main_get_item_count://广告总页数
                Toast.makeText(ShowADActivity.this, "广告总页数为 " + mDefaultBanner.getItemCount(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_main_get_current_item://当前页数
                Toast.makeText(ShowADActivity.this, "当前索引位置为" + mDefaultBanner.getCurrentItem(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_main_load_one_item://加载1页
                loadData(mDefaultBanner,1);
                break;
            case R.id.tv_main_load_two_item://加载2页
                loadData(mDefaultBanner,2);
                break;
            case R.id.tv_main_load_three_item://加载3页
                loadData(mDefaultBanner,3);
                break;
            case R.id.tv_main_load_five_item://加载5页
                loadData(mDefaultBanner,5);
                break;
            case R.id.tv_main_cube://cube
                mDefaultBanner.setTransitionEffect(TransitionEffect.Cube);
                break;
            case R.id.tv_main_depth:
                mDefaultBanner.setTransitionEffect(TransitionEffect.Depth);
                break;
            case R.id.tv_main_flip:
                mDefaultBanner.setTransitionEffect(TransitionEffect.Flip);
                break;
            case R.id.tv_main_rotate:
                mDefaultBanner.setTransitionEffect(TransitionEffect.Rotate);
                break;
            case R.id.tv_main_alpha:
                mDefaultBanner.setTransitionEffect(TransitionEffect.Alpha);
                break;
            default:
                break;
        }
    }

}
