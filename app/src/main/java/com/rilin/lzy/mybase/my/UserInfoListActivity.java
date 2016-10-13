package com.rilin.lzy.mybase.my;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;
import com.rilin.lzy.mybase.adapter.MyListViewAdapter;
import com.rilin.lzy.mybase.bean.KeyValue;
import com.rilin.lzy.mybase.bean.UserBean;
import com.rilin.lzy.mybase.util.StrUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UserInfoListActivity extends BaseActivity implements View.OnClickListener {

    private ListView listView;
    private MyListViewAdapter myListViewAdapter;
    private List<KeyValue> keyValueList = new ArrayList<>();
    private String[] left = new String[]{"姓名", "性别","出生日期", "年龄", "住址", "首次录入日期", "身高", "体重", "BMI", "电话1", "电话2", "家庭电话", "门诊号", "住院号", "确定诊断日期", "病程", "诊断"};
    private String result_value;
    private int position;
    private ImageView commitImageView;

    @Override
    public int setViewId() {
        return R.layout.activity_user_info_list;
    }

    @Override
    public void initView() {
        listView = (ListView)findViewById(R.id.list_view);
        commitImageView = (ImageView)findViewById(R.id.iv_sure);
    }

    @Override
    public void initData() {
        commitImageView.setOnClickListener(this);

        UserBean userBean = new UserBean();
        for (int i=0; i<left.length; i++){
            KeyValue keyValue = new KeyValue();
            keyValue.setKey(left[i]);
            switch (i){
                case 0:
                    keyValue.setValue(userBean.getName());
                    break;
                case 1:
                    keyValue.setValue(userBean.getSex());
                    break;
                case 2:
                    keyValue.setValue(userBean.getBirthday());
                    break;
                case 3:
                    keyValue.setValue(userBean.getAge());
                    break;
                case 4:
                    keyValue.setValue(userBean.getAddress());
                    break;
                case 5:
                    keyValue.setValue(userBean.getFirst_insp());
                    break;
                case 6:
                    keyValue.setValue(userBean.getHeight());
                    break;
                case 7:
                    keyValue.setValue(userBean.getWeight());
                    break;
                case 8:
                    keyValue.setValue(userBean.getBsa());
                    break;
                case 9:
                    keyValue.setValue(userBean.getPhone());
                    break;
                case 10:
                    keyValue.setValue(userBean.getPhone1());
                    break;
                case 11:
                    keyValue.setValue(userBean.getPhone2());
                    break;
                case 12:
                    keyValue.setValue(userBean.getPatient_id());
                    break;
                case 13:
                    keyValue.setValue(userBean.getHosp_id());
                    break;
                case 14:
                    keyValue.setValue(userBean.getDeath_time());
                    break;
                case 15:
                    keyValue.setValue(userBean.getPath_type());
                    break;
                case 16:
                    keyValue.setValue(userBean.getMarital_state());
                default:
                    break;
            }
            keyValueList.add(keyValue);
        }
        setList();
    }

    public void setList(){
        myListViewAdapter = new MyListViewAdapter(keyValueList, this, new MyListViewAdapter.CallBack() {
            @Override
            public void OnResult(int position) {
                Intent intent = new Intent(UserInfoListActivity.this, UserEditActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("tittle", keyValueList.get(position).getKey());
                intent.putExtra("value", keyValueList.get(position).getValue());
                if(position == 0 || position == 4 || position == 6 || position == 7 || position == 9 || position == 10 || position == 11 || position == 12 || position == 13 || position == 15){
                    intent.putExtra("type", "text");
                    startActivityForResult(intent, 100);
                }else if(position == 1){
                    intent.putExtra("type", "sex");
                    startActivityForResult(intent, 100);
                }else if(position == 2 || position == 5 || position == 14){
                    intent.putExtra("type", "date");
                    startActivityForResult(intent, 100);
                }else if(position == 3 || position == 8){
                    return;
                }else if(position == 16){
                    intent.putExtra("type", "诊断");
                    startActivityForResult(intent, 100);
                }
            }
        });
        listView.setAdapter(myListViewAdapter);
    }

    @Override
    public void requestData() {}

    @Override
    public void destroyResource() {}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_sure:
                toast("保存成功");
                break;
            default:
                break;
        }
    }

    /**
     * 所有的Activity对象的返回值都是由这个方法来接收
     * requestCode: 表示的是启动一个Activity时传过去的requestCode值
     * resultCode：表示的是启动后的Activity回传值时的resultCode值
     * data：表示的是启动后的Activity回传过来的Intent对象
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == 101)
        {
            result_value = data.getStringExtra("result");
            position = data.getIntExtra("position", -1);
            keyValueList.get(position).setValue(result_value);

            if(position == 2){
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                String timeString = format.format(date);

                String time =  timeString.substring(0, 4);
                String endTime = result_value.substring(0,4);

                int age = Integer.parseInt(time) - Integer.parseInt(endTime);
                keyValueList.get(position+1).setValue(String.valueOf(age));
            }else if(position == 6 || position == 7){
                if(keyValueList.get(6).getValue() != null && keyValueList.get(6).getValue().length()>0 && keyValueList.get(7).getValue() != null && keyValueList.get(7).getValue().length()>0){

                    String height = keyValueList.get(6).getValue();
                    String weight = keyValueList.get(7).getValue();

                    if (StrUtil.isNumber(height) && StrUtil.isNumber(weight)){
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");

                        double h1 = Double.parseDouble(height)/100;
                        double w1 = Double.parseDouble(weight);
                        double b = w1/(h1*h1);

                        String bmi =  decimalFormat.format(b);
                        keyValueList.get(8).setValue(bmi);
                    }
                }
            }
            myListViewAdapter.notifyDataSetChanged();

        }
    }
}
