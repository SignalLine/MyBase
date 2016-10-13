package com.rilin.lzy.mybase.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.application.MyApplication;
import com.rilin.lzy.mybase.model.ProvinceView;
import com.rilin.lzy.mybase.util.L;
import com.rilin.lzy.mybase.util.SystemUtils;
import com.rilin.lzy.mybase.widgets.wheel.CityArrayWheelAdapter;
import com.rilin.lzy.mybase.widgets.wheel.OnWheelChangedListener;
import com.rilin.lzy.mybase.widgets.wheel.OnWheelScrollListener;
import com.rilin.lzy.mybase.widgets.wheel.ProvinceArrayWheelAdapter;
import com.rilin.lzy.mybase.widgets.wheel.TownArrayWheelAdapter;
import com.rilin.lzy.mybase.widgets.wheel.WheelView;

/**
 * Created by lzy on 16/10/8.
 */
public class AreaChooseDialogIndex extends Dialog{

    String title;
    Context context;

    int flag1 = 0;
    int flag2 = 0;

    public int provinceId = 0;
    public int cityId = 0;
    public int townId = 0;

    public String destId = "0";

    ProvinceArrayWheelAdapter<String> provinceAdapter;
    CityArrayWheelAdapter<String> cityAdapter;
    TownArrayWheelAdapter<String> townAdapter;


    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getTownId() {
        return townId;
    }

    public void setTownId(int townId) {
        this.townId = townId;
    }

    WheelView province ;
    WheelView city ;
    WheelView town ;

    public AreaChooseDialogIndex(Context context,String title) {
        super(context);
        this.context=context;
        this.title=title;
        // 加载本地的省市县数据
        if(MyApplication.provinceList_index.size()<1){//如果省市县数据为空,就初始化
//			System.out.println("初始化区域数据");
            String json="";
            json = SystemUtils.getFromAssets(context,"getAreaDataFile.json");//区域
            JSONObject jsonObject=JSONObject.parseObject(json);
            String json2 = jsonObject.getString("AreaList");
            MyApplication.provinceList_index= JSON.parseArray(json2, ProvinceView.class);
        }

        provinceAdapter=new ProvinceArrayWheelAdapter<>(MyApplication.provinceList_index);
        cityAdapter=new CityArrayWheelAdapter<>(MyApplication.provinceList_index.get(0).getCitylist());
        townAdapter=new TownArrayWheelAdapter<>(MyApplication.provinceList_index.get(0).getCitylist().get(0).getCountylist());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.area_choose_activity);
        super.onCreate(savedInstanceState);
        province = (WheelView) findViewById(R.id.province);
        province.setAdapter(provinceAdapter);

        city = (WheelView) findViewById(R.id.city);
        city.setAdapter(cityAdapter);

        town = (WheelView) findViewById(R.id.town);
        town.setAdapter(townAdapter);

        addChangingListener(province, "province");
        addChangingListener(city, "city");
        addChangingListener(town, "town");

        OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int provinceCurrentItem = province.getCurrentItem();
                if(flag1!=provinceCurrentItem){
                    city.setCurrentItem(0);
                }
                flag1=province.getCurrentItem();
                try {
                    cityAdapter.setItems(MyApplication.provinceList_index.get(provinceCurrentItem).getCitylist());
                } catch (IndexOutOfBoundsException e) {
                    // TODO: handle exception
                    cityAdapter.setItems(MyApplication.provinceList_index.get(0).getCitylist());
                    city.setCurrentItem(0);
                    e.printStackTrace();
                }
                city.setAdapter(cityAdapter);

                int cityCurrentItem = city.getCurrentItem();
                if(flag2!=cityCurrentItem){
                    town.setCurrentItem(0);
                }
                flag2=city.getCurrentItem();

                try {
                    townAdapter.setItems(MyApplication.provinceList_index.get(provinceCurrentItem).getCitylist().get(cityCurrentItem).getCountylist());
                } catch (IndexOutOfBoundsException e) {
                    // TODO: handle exception
                    townAdapter.setItems(MyApplication.provinceList_index.get(provinceCurrentItem-1).getCitylist().get(0).getCountylist());
                    town.setCurrentItem(0);
                    e.printStackTrace();
                }

                town.setAdapter(townAdapter);

                int townCurrentItem = town.getCurrentItem();
                provinceId=provinceCurrentItem;
                cityId=cityCurrentItem;
                townId=townCurrentItem;
            }
        };
        //设置滚动监听事件,动态改变内容
        province.addScrollingListener(scrollListener);
        city.addScrollingListener(scrollListener);
        town.addScrollingListener(scrollListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.i("TAG","costomDialog is dismiss!");
    }

    private void addChangingListener(final WheelView wheel, final String label) {
        wheel.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//					wheel.setLabel(newValue != 1 ? label + "s" : label);
//					这里监听滚轮的变化，并且设置滚轮的值
                if(provinceId>MyApplication.provinceList_index.size()-1){
                    return;
                }

                if(cityId>MyApplication.provinceList_index.get(provinceId).getCitylist().size()-1){
                    return;
                }
                if(townId>MyApplication.provinceList_index.get(provinceId).getCitylist().get(cityId).getCountylist().size()-1){
                    return;
                }

                destId=MyApplication.provinceList_index.get(provinceId).getCitylist().get(cityId).getCountylist().get(townId).getCityid();

            }
        });
    }
}
