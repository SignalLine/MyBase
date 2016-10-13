package com.rilin.lzy.mybase.my;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.jude.utils.JUtils;
import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;
import com.rilin.lzy.mybase.bean.KeyValue;
import com.rilin.lzy.mybase.util.BitmapHelper;
import com.rilin.lzy.mybase.widgets.FixGridLayout;
import com.rilin.lzy.mybase.widgets.FlowRadioGroup;
import com.rilin.lzy.mybase.widgets.ImageCircleView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * 由于是动态加载的数据,需要在滑动的时候实时记录所添加或者所改变的值
 *
 */
public class RecyclerUserActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = RecyclerUserActivity.class.getSimpleName();
    private CoordinatorLayout mCoordinatorLayout;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mActionButton;


    private ArrayList<KeyValue> mDatas;
    private ImageCircleView patientHeadImage;
    private static final int REQUEST_CODE = 10;
    private Activity activity;

    @Override
    public int setViewId() {
        return R.layout.activity_recycler_user;
    }

    @Override
    public void initView() {
        activity = this;
        mCoordinatorLayout = getView(R.id.coordinator_layout);
        mRecyclerView = getView(R.id.id_recyclerview);
        mActionButton = getView(R.id.id_float_button);

        mActionButton.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //添加测试数据
        mDatas = new ArrayList<>();
        KeyValue keyValue;

        keyValue = new KeyValue();
        keyValue.setKey("title");
        keyValue.setValue("title1");
        mDatas.add(keyValue);

        keyValue = new KeyValue();
        keyValue.setKey("type");
        keyValue.setValue("img");
        mDatas.add(keyValue);

        keyValue = new KeyValue();
        keyValue.setKey("type");
        keyValue.setValue("radio");
        mDatas.add(keyValue);

        keyValue = new KeyValue();
        keyValue.setKey("type");
        keyValue.setValue("checkbox");
        mDatas.add(keyValue);

        keyValue = new KeyValue();
        keyValue.setKey("title");
        keyValue.setValue("title2");
        mDatas.add(keyValue);

        keyValue = new KeyValue();
        keyValue.setKey("type");
        keyValue.setValue("name");
        mDatas.add(keyValue);

        keyValue = new KeyValue();
        keyValue.setKey("type");
        keyValue.setValue("time");
        mDatas.add(keyValue);

        keyValue = new KeyValue();
        keyValue.setKey("type");
        keyValue.setValue("blood");
        mDatas.add(keyValue);

        //设置Adapter
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(new UserAdapter());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_float_button:
                toast("保存成功");
                break;
        }
    }


    class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{

        private int sexId,bloodId;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_user,parent,false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            FlowRadioGroup rg;

            LinearLayout linearLayout;

            LinearLayout l1 = holder.mLinearLayout;
            //进行移除操作,否则在滑动时候回出现错误
            l1.removeAllViews();

            l1.setBackgroundColor(Color.parseColor("#ffffff"));
            //获取对应的集合中的数据
            KeyValue keyValue = mDatas.get(position);

            if("type".equals(keyValue.getKey())){
                TextView itemText = new TextView(getApplicationContext());
                itemText.setTextColor(Color.parseColor("#666666"));
                itemText.setTextSize(16);
                LinearLayout.LayoutParams itemTextParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT

                );
                itemTextParams.setMargins(JUtils.dip2px(20),JUtils.dip2px(10),JUtils.dip2px(20),JUtils.dip2px(15));
                l1.addView(itemText,itemTextParams);

                switch (keyValue.getValue()) {
                    case "img":
                        itemText.setText("修改头像");

                        RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());

                        patientHeadImage = new ImageCircleView(getApplicationContext());
                        patientHeadImage.setImageResource(R.mipmap.patient);
                        patientHeadImage.setBorderWidth(2);
                        patientHeadImage.setBorderColor(Color.WHITE);

                        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                                200, 200
                        );
                        imageParams.setMargins(JUtils.dip2px(20),JUtils.dip2px(5),JUtils.dip2px(20),0);
                        relativeLayout.addView(patientHeadImage,imageParams);

                        patientHeadImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PhotoPickerIntent intent = new PhotoPickerIntent(getApplicationContext());
                                intent.setPhotoCount(1);
                                intent.setShowCamera(true);
                                intent.setShowGif(true);
                                startActivityForResult(intent, REQUEST_CODE);
                            }
                        });

                        l1.addView(relativeLayout);

                        TextView itemTextViewTitle = new TextView(getApplicationContext());
                        itemTextViewTitle.setText("修改头像");
                        itemTextViewTitle.setTextSize(16);
                        itemTextViewTitle.setTextColor(Color.DKGRAY);
                        itemTextViewTitle.setTextColor(Color.parseColor("#666666"));
                        RelativeLayout.LayoutParams pTextBox = new RelativeLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        pTextBox.leftMargin = 20;
                        pTextBox.addRule(RelativeLayout.RIGHT_OF, patientHeadImage.getId());
                        pTextBox.setMargins(JUtils.dip2px(100), JUtils.dip2px(10), JUtils.dip2px(20), 0);
                        pTextBox.addRule(RelativeLayout.CENTER_VERTICAL);
                        relativeLayout.addView(itemTextViewTitle,pTextBox);


                        break;
                    case "radio":
                        itemText.setText("性别");

                        rg = new FlowRadioGroup(getApplicationContext());
                        LinearLayout.LayoutParams pradio = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT

                        );

                        pradio.setMargins(JUtils.dip2px(20), JUtils.dip2px(10), JUtils.dip2px(20), JUtils.dip2px(15));
                        String[] sex = {"男","女"};
                        final RadioButton[] rbs = new RadioButton[sex.length];
                        for (int i = 0; i < rbs.length; i++) {
                            rbs[i] = new RadioButton(getApplicationContext());
                            rbs[i].setTextColor(Color.DKGRAY);
//                            rbs[i].setTag(i+1);
                            rbs[i].setTag(sex[i] + "," + i);
                            rbs[i].setButtonDrawable(R.drawable.radiobutton_selector);
                            rbs[i].setText(sex[i]);
                            rg.addView(rbs[i],pradio);
                            rg.check(rbs[0].getId());
                            rbs[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String s = (String) v.getTag();
                                    String[] split = s.split(",");
                                    if(split.length >= 2){
                                        if(rbs[Integer.parseInt(split[1])].isChecked()){
                                            toast("您选择的性别是:---->" + split[0]);
                                        }
                                    }


                                }
                            });
                            rg.setLeft(20);
//                            rg.setRight(20);
                        }
                        //添加到布局中
                        l1.addView(rg,pradio);
                        break;
                    case "checkbox":
                        itemText.setText("病因");
                        FixGridLayout fixGridLayout = new FixGridLayout(getApplicationContext());
                        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        int width = wm.getDefaultDisplay().getWidth();
                        int height = wm.getDefaultDisplay().getHeight();
                        fixGridLayout.setmCellHeight(200);
                        fixGridLayout.setmCellWidth(width / 2 - 40);

                        FixGridLayout.LayoutParams fp = new FixGridLayout.LayoutParams(
                                FixGridLayout.LayoutParams.WRAP_CONTENT,
                                FixGridLayout.LayoutParams.WRAP_CONTENT
                        );

                        final String[] radioArray = {"ABI(获得性脑损伤)","SCI(脊髓损伤)","进展性疾病","血管性","炎症性","中毒","肿瘤","创伤","缺氧","退行性变","其他"};
                        for (int i = 0; i < radioArray.length; i++) {
                            CheckBox checkBox = new CheckBox(getApplicationContext());
                            checkBox.setText(radioArray[i]);
                            checkBox.setTextColor(Color.DKGRAY);
                            checkBox.setTag(i);
                            checkBox.setLayoutParams(fp);
                            checkBox.setButtonDrawable(R.drawable.checkbox_selector);
                            //随机设置选中CheckBox,方便验证
                            Random random = new Random();
                            int r = random.nextInt(radioArray.length);
                            if(r == i){
                                checkBox.setChecked(true);
                            }

                            checkBox.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(((CheckBox)v).isChecked()){
                                        Integer pos = (Integer) v.getTag();
                                        if (pos != null) {
                                            Log.i(TAG,"选中的pos--------->>>>" + radioArray[pos]);
                                        }

                                    }else {
                                        Integer pos = (Integer) v.getTag();
                                        if (pos != null) {
                                            Log.i(TAG,"没有选中的pos--------->>>>" + radioArray[pos]);
                                        }
                                    }
                                }
                            });

                            fixGridLayout.addView(checkBox,fp);
                        }
                        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        p.setMargins(JUtils.dip2px(20),JUtils.dip2px(5),JUtils.dip2px(20),0);
                        l1.addView(fixGridLayout,p);
                        break;
                    case "name":
                        itemText.setText("姓名");

                        EditText editName = new EditText(getApplicationContext());
                        editName.setBackgroundColor(Color.GRAY);
                        editName.setTextColor(Color.DKGRAY);
                        editName.setBackgroundResource(R.drawable.edit_frame_color);
                        editName.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                Log.i(TAG,"输入的姓名----------->>>" + s.toString());
                                if(!TextUtils.isEmpty(s.toString()))
                                    toast(s.toString());
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        editName.setTextSize(16);
                        editName.setPadding(JUtils.dip2px(10),0,0,0);
                        editName.setGravity(Gravity.CENTER_VERTICAL);

                        LinearLayout.LayoutParams pTextBox2 = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        pTextBox2.setMargins(JUtils.dip2px(20), JUtils.dip2px(5), JUtils.dip2px(20), JUtils.dip2px(15));
                        l1.addView(editName,pTextBox2);
                        break;
                    case "blood":
                        itemText.setText("血型");
                        rg = new FlowRadioGroup(getApplicationContext());
                        LinearLayout.LayoutParams pradio1 = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT

                        );

                        pradio1.setMargins(JUtils.dip2px(20), JUtils.dip2px(10), JUtils.dip2px(20), JUtils.dip2px(15));
                        String[] blood = {"A","B","AB","O"};
                        final RadioButton[] rbs1 = new RadioButton[blood.length];
                        for (int i = 0; i < rbs1.length; i++) {
                            rbs1[i] = new RadioButton(getApplicationContext());
                            rbs1[i].setTextColor(Color.DKGRAY);
//                            rbs[i].setTag(i+1);
                            rbs1[i].setTag(blood[i] + "," + i);
                            rbs1[i].setButtonDrawable(R.drawable.radiobutton_selector);
                            rbs1[i].setText(blood[i]);
                            rg.addView(rbs1[i],pradio1);
                            rg.check(rbs1[0].getId());
                            //全局变量接收当前的id
                            bloodId = i;
                            rbs1[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String s = (String) v.getTag();
                                    String[] split = s.split(",");
                                    if(split.length >= 2){
                                        if(rbs1[Integer.parseInt(split[1])].isChecked()){
                                            toast("您选择的血型是:---->" + split[0]);
                                        }
                                    }
                                }
                            });
                            rg.setLeft(20);
//                            rg.setRight(20);
                        }
                        //添加到布局中
                        l1.addView(rg,pradio1);
                        break;
                    case "time":
                        itemText.setText("入院时间");

                        final EditText editTime = new EditText(getApplicationContext());
                        editTime.setBackgroundColor(Color.GRAY);
                        editTime.setTextColor(Color.DKGRAY);
                        editTime.setInputType(InputType.TYPE_NULL);
                        editTime.setBackgroundResource(R.drawable.edit_frame_color);

                        final TimePickerView pvTime = new TimePickerView(activity, TimePickerView.Type.YEAR_MONTH_DAY);
                        //控制时间范围
                        Calendar calendar = Calendar.getInstance();
                        pvTime.setRange(calendar.get(Calendar.YEAR) - 50, calendar.get(Calendar.YEAR));
                        pvTime.setTime(new Date());
                        pvTime.setCyclic(true);
                        pvTime.setTitle("入院时间");
                        pvTime.setCancelable(true);
                        //时间选择后回调
                        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

                            @Override
                            public void onTimeSelect(Date date) {
                                editTime.setText(getTime(date));
                            }
                        });

                        editTime.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                Log.i(TAG,"输入的日期----------->>>" + s.toString());
                                if(!TextUtils.isEmpty(s.toString()))
                                    toast(s.toString());
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                        editTime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.show();
                            }
                        });
                        editTime.setTextSize(16);
                        editTime.setPadding(JUtils.dip2px(10),0,0,0);
                        editTime.setGravity(Gravity.CENTER_VERTICAL);

                        LinearLayout.LayoutParams line = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        line.setMargins(JUtils.dip2px(20), JUtils.dip2px(5), JUtils.dip2px(20), JUtils.dip2px(15));
                        l1.addView(editTime,line);
                        break;
                    default:
                        break;
                }

                //完成一个选项进行划线处理
                TextView lineTextView2 = new TextView(getApplicationContext());
                lineTextView2.setBackgroundColor(Color.parseColor("#eeeeee"));
                LinearLayout.LayoutParams pTitleText3 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        JUtils.dip2px(1)
                );
                pTitleText3.setMargins(JUtils.dip2px(20), 0, JUtils.dip2px(20),0);
                l1.addView(lineTextView2, pTitleText3);
            }else {//title
                switch (keyValue.getValue()) {
                    case "title1":
                        TextView title = new TextView(getApplicationContext());
                        title.setText("基本信息");
                        title.setTextSize(18);

                        title.setGravity(Gravity.CENTER_VERTICAL);
                        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, JUtils.dip2px(40)
                        );

                        title.setBackgroundColor(Color.parseColor("#fff9ee"));
                        title.setTextColor(Color.parseColor("#ffb629"));
                        title.setPadding(JUtils.dip2px(20), 0, 0, 0);
                        titleParams.setMargins(JUtils.dip2px(0),0,0,0);
                        l1.addView(title,titleParams);

                        break;
                    case "title2":
                        TextView title2 = new TextView(getApplicationContext());
                        title2.setText("详细信息");
                        title2.setTextSize(18);
                        title2.setGravity(Gravity.CENTER_VERTICAL);
                        LinearLayout.LayoutParams titleParams2 = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, JUtils.dip2px(40)
                        );

                        title2.setBackgroundColor(Color.parseColor("#fff9ee"));
                        title2.setTextColor(Color.parseColor("#ffb629"));
                        title2.setPadding(JUtils.dip2px(20), 0, 0, 0);
                        titleParams2.setMargins(JUtils.dip2px(0),0,0,0);
                        l1.addView(title2,titleParams2);
                        break;
                    default:
                        break;
                }
                TextView lineText = new TextView(getApplicationContext());
                lineText.setBackgroundColor(Color.parseColor("#eeeeee"));
                LinearLayout.LayoutParams lineTextParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, JUtils.dip2px(1)
                );
                lineTextParams.setMargins(0,0,0,0);
                l1.addView(lineText,lineTextParams);
            }

        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {
            View holdView;
            LinearLayout mLinearLayout;

            public MyViewHolder(View view)
            {
                super(view);

                holdView = view;
                mLinearLayout = (LinearLayout) view.findViewById(R.id.sl);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                Log.i(TAG,"----------------------->>>>" + photos.get(0));
                Bitmap imageBitmap = BitmapHelper.compressBitmap(photos.get(0),10,10);

                patientHeadImage.setImageBitmap(imageBitmap);

            }
        }
    }

    public  String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    public void requestData() {}

    @Override
    public void destroyResource() {}
}
