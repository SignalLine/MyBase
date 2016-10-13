package com.rilin.lzy.mybase.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.jude.utils.JUtils;
import com.rilin.lzy.mybase.MainActivity;
import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;
import com.rilin.lzy.mybase.util.ProgressUtil;
import com.rilin.lzy.mybase.widgets.ClearEditText;


public class SimpleLoginActivity extends Activity implements View.OnClickListener {

    protected static final String TAG = "SimpleLoginActivity";
    private ClearEditText mIdEditText; // 登录ID编辑框
    private ClearEditText mPwdEditText; // 登录密码编辑框
    private PopupWindow mPop; // 下拉弹出窗
    private Button mButtonLogin;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_login);
        mActivity = this;

        initView();
        initData();
    }


    public void initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        JUtils.initialize(getApplication());

        mIdEditText = (ClearEditText) findViewById(R.id.login_edtId);
        mPwdEditText = (ClearEditText) findViewById(R.id.login_edtPwd);
        mButtonLogin = (Button) findViewById(R.id.login_btnLogin);
    }

    public void initData() {
        mButtonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btnLogin:
                if (TextUtils.isEmpty(mIdEditText.getText().toString())) {
                    //设置晃动
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIdEditText.setShakeAnimation();
                            //设置提示
                            JUtils.Toast("用户名不能为空");
                        }
                    });
                    return;
                } else if (TextUtils.isEmpty(mPwdEditText.getText().toString())) {
                    //设置晃动
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPwdEditText.setShakeAnimation();
                            //设置提示
                            JUtils.Toast("密码不能为空");
                        }
                    });
                    return;
                } else {
                    //ProgressUtil.getInstance().showProgress(mActivity,"登录中...");

                    //ProgressUtil.getInstance().showSuccessProgress("登录成功");
                    startActivity(new Intent(SimpleLoginActivity.this,MainActivity.class));

                    //ProgressUtil.getInstance().showDoneProgress();
                    finish();
                }
                break;
        }
    }
}
