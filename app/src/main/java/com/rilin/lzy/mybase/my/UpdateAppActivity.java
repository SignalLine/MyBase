
package com.rilin.lzy.mybase.my;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.downmanager.UpdateManager;

public class UpdateAppActivity extends AppCompatActivity {

    private String updateInfo = "更新内容\n" + "    1. 就是想更新~~~~~\n" + "    2. 真的不费流量~~~~~\n" + "    ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_app);

        init();
    }

    private void init() {
        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //false 如果是最新版本就不吐司提示
                new UpdateManager(UpdateAppActivity.this).checkUpdate(updateInfo,false);
            }
        });
    }
}
