package com.rilin.lzy.mybase.my;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.jude.utils.JUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;



public class FloatButtonActivity extends BaseActivity implements View.OnClickListener {

    private FloatingActionsMenu mActionsMenu;
    private FloatingActionButton mFirstActionButton,mSecondActionButton,mThirdActionButton,mLeftActionButton;
    private FrameLayout mFrameLayout;

    private Toolbar toolbar;
    private SearchView searchView;
    private ImageView userImageView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    public int setViewId() {
        return R.layout.activity_float_button;
    }

    @Override
    public void initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setStatusBarTintResource(R.color.ziti);

        mFrameLayout = getView(R.id.frame_layout);

        mActionsMenu = getView(R.id.float_btn_menu);
        mFirstActionButton = getView(R.id.first);
        mSecondActionButton = getView(R.id.second);
        mThirdActionButton = getView(R.id.three);

        mLeftActionButton = getView(R.id.float_btn_add);

        navigationView = getView(R.id.navigation_view);
        drawerLayout = getView(R.id.drawer_layout);

        searchView = (SearchView)findViewById(R.id.id_search);
        searchView.setQueryHint("搜索姓名");

        searchView.setMaxWidth(JUtils.getScreenWidth());

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userImageView.setVisibility(View.INVISIBLE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                userImageView.setVisibility(View.VISIBLE);

                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // patientInfoModel.downLoadIPatients(activity, newText);
                if (!TextUtils.isEmpty(newText)) {
                    toast(newText);
                }

                return false;
            }
        });

        if(android.os.Build.VERSION.RELEASE.compareTo("5.0.0") <= 0){

            android.support.design.widget.AppBarLayout appBarLayout = (android.support.design.widget.AppBarLayout)findViewById(R.id.app_bar);
            android.support.design.widget.CoordinatorLayout.LayoutParams layoutParams = (android.support.design.widget.CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            layoutParams.height = dip2px(this,44);
            layoutParams.topMargin = dip2px(this, 20);
            appBarLayout.setLayoutParams(layoutParams);

            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            android.view.ViewGroup.MarginLayoutParams relativeLayout1 =  (android.view.ViewGroup.MarginLayoutParams)toolbar.getLayoutParams(); // 取控件mGrid当前的布局参数
            relativeLayout1.topMargin = dip2px(this,1);
            relativeLayout1.height = dip2px(this,44);
            toolbar.setLayoutParams(relativeLayout1);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new MyOnMenuItemClickListener());

        userImageView = (ImageView)findViewById(R.id.patient);
        userImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // startActivity(new Intent(MainActivity.this, UserActivity.class));
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        //让图片显示他本身 的颜色
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            MenuItem parentMenuItem;
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if(parentMenuItem != null){
                    parentMenuItem.setChecked(false);
                }
                item.setChecked(true);
                parentMenuItem = item;

                switch (item.getItemId()){
                    case R.id.navItem1:
                        toast("item1");
                        break;
                    case R.id.navItem2:
                        toast("item2");
                        break;
                    case R.id.navItem3:
                        toast("item3");
                        break;
                    case R.id.navItem4:
                        toast("当前版本号:1.0.0");
                        break;
                    default:
                        break;
                }

                return false;
            }
        });
    }

    @Override
    public void initData() {
        mFrameLayout.getBackground().setAlpha(0);
        mActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                mFrameLayout.getBackground().setAlpha(120);
                mFrameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mActionsMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                mFrameLayout.getBackground().setAlpha(0);
                mFrameLayout.setOnTouchListener(null);
            }
        });

        mFirstActionButton.setOnClickListener(this);
        mSecondActionButton.setOnClickListener(this);
        mThirdActionButton.setOnClickListener(this);
        mLeftActionButton.setOnClickListener(this);
    }

    private class MyOnMenuItemClickListener implements Toolbar.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String str = "";
            switch (item.getItemId()){
                case R.id.id_search:
                    str = "setting";
                    break;
            }
            toast(str);
            return true;
        }
    }

    @Override
    public void requestData() {

    }

    @Override
    public void destroyResource() {

    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.first:
                    toast("This is First ActionButton");
                    mFrameLayout.getBackground().setAlpha(0);
                    mActionsMenu.collapse();
                    break;
                case R.id.second:
                    toast("This is Second");
                    mFrameLayout.getBackground().setAlpha(0);
                    mActionsMenu.collapse();
                    break;
                case R.id.three:
                    toast("This is Third");
                    mFrameLayout.getBackground().setAlpha(0);
                    mActionsMenu.collapse();
                    break;
                case R.id.float_btn_add:
                    toast("This is Add");
                    break;
            }
        }
    }

    public  int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
