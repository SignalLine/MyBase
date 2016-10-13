package com.rilin.lzy.mybase.my;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;
import com.rilin.lzy.mybase.util.DateUtil;

import java.util.Calendar;
import java.util.Date;


public class UserEditActivity extends BaseActivity implements View.OnClickListener {

    private EditText editText;
    private String type;
    private int position;
    private ImageView mImageBack;
    private TextView tittleTextView,commitTextView;

    @Override
    public int setViewId() {
        return R.layout.activity_user_edit;
    }

    @Override
    public void initView() {
        mImageBack = getView(R.id.iv_back);
        editText = (EditText)findViewById(R.id.edit);
        commitTextView = (TextView) findViewById(R.id.iv_sure);
        tittleTextView = (TextView)findViewById(R.id.tv_title);
    }

    @Override
    public void initData() {
        commitTextView.setOnClickListener(this);
        mImageBack.setOnClickListener(this);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        position = intent.getIntExtra("position",-1);
        tittleTextView.setText(intent.getStringExtra("tittle"));
        editText.setText(intent.getStringExtra("value"));
        final String value = intent.getStringExtra("value");
        if("text".equals(type)){

        }else if("date".equals(type)){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
            editText.clearFocus();
            final TimePickerView pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
            //控制时间范围
            Calendar calendar = Calendar.getInstance();
            pvTime.setRange(calendar.get(Calendar.YEAR) - 50, calendar.get(Calendar.YEAR));
            pvTime.setTime(new Date());
            pvTime.setCyclic(true);
            pvTime.setCancelable(true);
            pvTime.show();
            //时间选择后回调
            pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date) {
                    editText.setText(DateUtil.getDateFormat(date));
                }
            });
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pvTime.show();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
                }
            });



        }else if("sex".equals(type)){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
            editText.clearFocus();

            final String[] item = new String[]{"男", "女"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                    .setTitle("单选框")
//                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setSingleChoiceItems(
                            item, -1,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    editText.setText(item[which]);
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String result = editText.getText().toString().trim();
                            Intent intent = new Intent();
                            intent.putExtra("result", result);
                            intent.putExtra("position", position);
                            /*
                             * 调用setResult方法表示我将Intent对象返回给之前的那个Activity，这样就可以在onActivityResult方法中得到Intent对象，
                             */
                            setResult(101, intent);
                            //结束当前这个Activity对象的生命
                            finish();
                        }
                    });
            builder.show();
        }else if("诊断".equals(type)){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
            editText.clearFocus();

            final String[] item = new String[]{"1", "2", "3", "4", "5", "6", "7"};
            new AlertDialog.Builder(this)
//                    .setTitle("单选框")
//                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setSingleChoiceItems(
                            item, -1,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    editText.setText(item[which]);
                                    dialog.dismiss();
                                }})
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String result = editText.getText().toString().trim();
                            Intent intent = new Intent();
                            intent.putExtra("result", result);
                            intent.putExtra("position", position);
                            /*
                             * 调用setResult方法表示我将Intent对象返回给之前的那个Activity，这样就可以在onActivityResult方法中得到Intent对象，
                             */
                            setResult(101, intent);
                            //结束当前这个Activity对象的生命
                            finish();
                        }
                    })
                    .show();
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
        switch (v.getId()){
            case R.id.iv_sure:
                String result = editText.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("result", result);
                intent.putExtra("position", position);
                /*
                 * 调用setResult方法表示我将Intent对象返回给之前的那个Activity，这样就可以在onActivityResult方法中得到Intent对象，
                 */
                setResult(101, intent);
                //结束当前这个Activity对象的生命
                finish();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}
