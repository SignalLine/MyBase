package com.rilin.lzy.mybase.my;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;
import com.rilin.lzy.mybase.application.MyApplication;
import com.rilin.lzy.mybase.model.CityView;
import com.rilin.lzy.mybase.model.CountyView;
import com.rilin.lzy.mybase.model.ProvinceView;
import com.rilin.lzy.mybase.util.AssetsUtils;
import com.rilin.lzy.mybase.util.BitmapHelper;
import com.rilin.lzy.mybase.util.L;
import com.rilin.lzy.mybase.widgets.AreaChooseDialogIndex;
import com.rilin.lzy.mybase.widgets.ImageCircleView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

//图片选择
public class PhotoActivity extends BaseActivity implements View.OnClickListener {

    private static final java.lang.String TAG = PhotoActivity.class.getSimpleName();
    private TextView mTextPickAddress,mTextPickAddress2,mTextPickTime,mTextPickAge,mTextPickSex;
    private ImageCircleView mCircleView;
    private Activity activity;
    private static final int REQUEST_CODE = 10;
    private ImageView mImageHead;
    private Button mButtonSelect;
    private Bitmap headImg;//头像Bitmap

    //选择地区
    private AreaChooseDialogIndex areaChooseDialogIndex;
    private CountyView areaView = null;
    private ProvinceView provinceView = null;
    private CityView cityView = null;

    private String proviceid = "";
    private String cityid = "";
    private String countyid = "";// 选择全市的时候，为”“
    private String proviceName,cityName,countyName;
    @Override
    public int setViewId() {
        return R.layout.activity_photo;
    }

    @Override
    public void initView() {
        activity = this;

        mTextPickTime = getView(R.id.pick_tv_time);
        mTextPickAddress = getView(R.id.pick_tv_address);
        mTextPickAddress2 = getView(R.id.pick_tv_address2);
        mCircleView = getView(R.id.pick_icv_photo);
        mTextPickAge = getView(R.id.pick_tv_age);
        mTextPickSex = getView(R.id.pick_tv_sex);
        mImageHead = getView(R.id.pick_iv_head);
        mButtonSelect = getView(R.id.pick_btn_select);

        //初始化区域json数据
        areaChooseDialogIndex = new AreaChooseDialogIndex(PhotoActivity.this, "区域选择");// 普通的区域选择
        areaChooseDialogIndex.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        areaChooseDialogIndex.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void initData() {
        mTextPickAddress.setOnClickListener(this);
        mTextPickAddress2.setOnClickListener(this);
        mTextPickTime.setOnClickListener(this);
        mCircleView.setOnClickListener(this);
        mTextPickAge.setOnClickListener(this);
        mTextPickSex.setOnClickListener(this);
        mButtonSelect.setOnClickListener(this);
    }

    @Override
    public void requestData() {

    }

    @Override
    public void destroyResource() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                //这个地方的PhotoPickerActivity需要在配置清单中声明,项目中不要有跟这个重名的Activity和xml布局
                /*
                * 需要添加的依赖
                compile 'com.github.bumptech.glide:glide:3.6.0'
                compile 'com.nineoldandroids:library:2.4.0'
                compile 'me.iwf.photopicker:PhotoPicker:0.2.9@aar'
                compile 'com.mcxiaoke.volley:library:1.0.19'
                * */
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                Bitmap imageBitmap = BitmapHelper.compressBitmap(photos.get(0),20,80);
                mCircleView.setImageBitmap(imageBitmap);
            }
        }

        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case 1://相册
                    if (data != null) {
                        startPhotoZoom(data.getData());
                    }
                    break;
                case 2://相机拍照
                    File temp_file = new File( Environment.getExternalStorageDirectory() + "/user_picture.jpg");
                    startPhotoZoom(Uri.fromFile(temp_file));
                    break;
                case 3://裁剪
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            Bitmap photo = extras.getParcelable("data");
                            headImg=photo;
                            mImageHead.setImageBitmap(photo);
                            commit();
                        }
                    }
                    break;
                default:
                    break;
            }
        }

    }
    //上传图片
    private void commit() {
        if (headImg == null) {
            Toast.makeText(PhotoActivity.this, "没有选择图片", Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO:上传图片,保存图片

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pick_tv_address://选择地址
                ArrayList<AddressPicker.Province> data = new ArrayList<>();
                String json = AssetsUtils.readText(this,"city.json");
                data.addAll(JSON.parseArray(json,AddressPicker.Province.class));

                AddressPicker picker = new AddressPicker(this,data);
                picker.setAnimationStyle(R.style.Animation_CustomPopup);

                picker.setHideProvince(false);

                picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                    @Override
                    public void onAddressPicked(AddressPicker.Province province, AddressPicker.City city, AddressPicker.County county) {
                        mTextPickAddress.setText(province.getAreaName() + " " +
                                    city.getAreaName() + " " + county.getAreaName());
                    }
                });

                picker.show();
                break;
            case R.id.pick_tv_address2://选择地址
                areaChoose();
                break;
            case R.id.pick_tv_age://选择年龄
                NumberPicker picker1 = new NumberPicker(this);
                picker1.setAnimationStyle(R.style.Animation_CustomPopup);
                picker1.setOffset(1);//偏移量
                picker1.setRange(0,100);//数字范围
                picker1.setSelectedItem(30);//默认选中的数字
                picker1.setLabel("岁");
                picker1.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int position, String option) {
                        mTextPickTime.setText(position + "");
                    }
                });

                picker1.show();
                break;
            case R.id.pick_tv_time://选中时间
                Calendar calendar = Calendar.getInstance();
                DatePicker picker2 = new DatePicker(this);
                picker2.setAnimationStyle(R.style.Animation_CustomPopup);
                picker2.setRange(calendar.get(Calendar.YEAR)-15,calendar.get(Calendar.YEAR) + 15);
                picker2.setSelectedItem(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1,calendar.get(Calendar.DAY_OF_MONTH));
                picker2.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        mTextPickTime.setText(year + "-" + month + "-" + day);
                    }
                });
                picker2.show();
                break;

            case  R.id.pick_icv_photo://选择图片
                PhotoPickerIntent intent = new PhotoPickerIntent(activity);
                intent.setPhotoCount(1);
                intent.setShowCamera(true);
                intent.setShowGif(true);
                startActivityForResult(intent,REQUEST_CODE);
                break;

            case R.id.pick_tv_sex://选择性别
                final String[] item = new String[]{"男", "女"};
                new AlertDialog.Builder(this).setTitle("单选框").setIcon(
                        android.R.drawable.ic_dialog_info)
                        .setTitle("注意")
                        .setSingleChoiceItems(
                                item, -1,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        mTextPickSex.setText(item[which]);
                                        dialog.dismiss();
                                    }})
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.pick_btn_select://选择头像

                ShowPickDialog();

                break;
            default:
                break;
        }
    }

    // 选择图片dialog
    private void ShowPickDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("设置图片...")
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image/*");
                        startActivityForResult(intent, 1);
                    }
                })
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);

                        // 下面这句指定调用相机拍照后的照片存储的路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                .fromFile(new File(Environment
                                        .getExternalStorageDirectory(),
                                        "user_picture.jpg")));
                        startActivityForResult(intent, 2);
                    }
                }).show();
    }

    /**
     * 裁剪图片方法实现
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高 (如果这里的裁切数值太大了，会造成裁切失败)
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    // 区域选择dialog
    private void areaChoose() {
        if(areaChooseDialogIndex!=null){
            areaChooseDialogIndex.show();
        }else{
            return;
        }
        Button btn_yes = (Button) areaChooseDialogIndex
                .findViewById(R.id.button_yes);
        Button btn_no = (Button) areaChooseDialogIndex.findViewById(R.id.button_no);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                areaChooseDialogIndex.cancel();
                if (areaChooseDialogIndex.provinceId == 0) {// 切换地区
                    proviceid="";
                    cityid="";
                    countyid="";

                } else {// 选择了区域
                    provinceView = MyApplication.provinceList_index.get(areaChooseDialogIndex.provinceId);

                    cityView = MyApplication.provinceList_index
                            .get(areaChooseDialogIndex.provinceId).getCitylist()
                            .get(areaChooseDialogIndex.cityId);

                    areaView = MyApplication.provinceList_index
                            .get(areaChooseDialogIndex.provinceId).getCitylist()
                            .get(areaChooseDialogIndex.cityId).getCountylist()
                            .get(areaChooseDialogIndex.townId);

                    if (areaView != null) {// 全市
                        if (areaView.getCountyid() == null
                                || "".equals(areaView.getCountyid())) {
                            String cityname = MyApplication.provinceList_index
                                    .get(areaChooseDialogIndex.provinceId)
                                    .getCitylist().get(areaChooseDialogIndex.cityId)
                                    .getCityname();

                            MyApplication.area_city_name=cityname;
                        } else {// 具体到县
                            if("市区".equals(areaView.getCountyname())){
                                String cityname = MyApplication.provinceList_index
                                        .get(areaChooseDialogIndex.provinceId)
                                        .getCitylist().get(areaChooseDialogIndex.cityId)
                                        .getCityname();
                                MyApplication.area_city_name=cityname;
                            }else{
                                MyApplication.area_county_name=areaView.getCountyname();
                            }
                        }
                        MyApplication.provinceid=proviceid = areaView.getProvinceid();
                        MyApplication.cityid=cityid = areaView.getCityid();
                        MyApplication.countyid=countyid = areaView.getCountyid(); // 如果选全市，那么countyid就为”“
                        MyApplication.area_county_name = areaView.getCountyname();
                        MyApplication.area_province_name = provinceView.getProvincename();
                        MyApplication.area_city_name = cityView.getCityname();
                        L.i(TAG,"------->>>" + MyApplication.area_province_name + "-"
                                + MyApplication.area_city_name + "-"
                                + MyApplication.area_county_name);

                    }

                }
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                areaChooseDialogIndex.cancel();
            }
        });
    }

}
