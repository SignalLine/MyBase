package com.rilin.lzy.mybase.my.tv_vitamio;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rilin.lzy.mybase.R;

public class TVHomeActivity extends AppCompatActivity {

    private ListView mListView;
    public String[] name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvhome);

        mListView = (ListView) findViewById(R.id.lv);
        //55
        name=new String[]{"CCTV-1","CCTV-2","CCTV-3","CCTV-4","CCTV-5","CCTV-6","CCTV-7","CCTV-8","CCTV-9","CCTV-10","CCTV-11","CCTV-12","CCTV-13","CCTV-14","CCTV-15"
                ,"重庆卫视","东方卫视","东南卫视","广东卫视","广西卫视","贵州卫视","湖北卫视","湖南卫视","江苏卫视","辽宁卫视","旅游卫视","内蒙古卫视","山东卫视","山西卫视","深圳卫视","金鹰卡通","炫动卡通"
                ,"优漫卡通","中国教育1","CCTV-NEWS","安徽卫视","北京卫视","甘肃卫视","河北卫视","浙江卫视","黑龙江卫视","河南卫视","江西卫视","宁夏卫视","陕西卫视","四川卫视","BTV-kaku少儿","天津卫视",
                "兵团卫视","暂无","XX综合","XX蒙语","XX影视","XX旅游","XX妇女儿童","新疆卫视","新疆2","新疆3","新疆4","新疆5","新疆6","新疆7","新疆8","新疆9","新疆10","新疆11","新疆12","XE-TV","乌鲁木齐卫视?"
                ,"dongman"
        };

        mListView.setAdapter(new Adapter(name,this));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TVHomeActivity.this,VideoPlayerActivity.class);
                intent.putExtra("count",position);
                startActivity(intent);
            }
        });

    }


    class Adapter extends BaseAdapter {

        private String[] name;
        private Context mContext;
        public Adapter(String[] name, Context context){
            this.name = name;
            mContext = context;
        }

        @Override
        public int getCount() {
            return name.length;
        }

        @Override
        public Object getItem(int position) {
            return name[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if(convertView == null){
                vh = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.tv_list_item,null);
                vh.tv1 = (TextView) convertView.findViewById(R.id.list_tv);
                convertView.setTag(vh);
            }else {
                vh = (ViewHolder) convertView.getTag();
            }
            TextView tv1 = vh.tv1;
            tv1.setText(name[position] + "");

            return convertView;
        }

        class ViewHolder{
            TextView tv1;
        }
    }

}
