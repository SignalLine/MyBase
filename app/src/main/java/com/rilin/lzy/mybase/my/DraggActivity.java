package com.rilin.lzy.mybase.my;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;
import com.rilin.lzy.mybase.adapter.RBaseAdapter;
import com.rilin.lzy.mybase.util.L;
import com.rilin.lzy.mybase.widgets.DraggableGridViewPager;

public class DraggActivity extends BaseActivity {
    private static final String TAG = "DraggActivity";
    private DraggableGridViewPager mDraggableGridViewPager;
    private Button mAddButton;
    private Button mRemoveButton;

    private ArrayAdapter<String> mAdapter;

    private int mGridCount;

    @Override
    public int setViewId() {
        return R.layout.activity_dragg;
    }

    @Override
    public void initView() {
        mDraggableGridViewPager = (DraggableGridViewPager) findViewById(R.id.draggable_grid_view_pager);
        mAddButton = (Button) findViewById(R.id.add);
        mRemoveButton = (Button) findViewById(R.id.remove);

        mAdapter = new ArrayAdapter<String>(this, 0) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final String text = getItem(position);
                if (convertView == null) {
                    convertView = (TextView) getLayoutInflater().inflate(R.layout.draggable_grid_item, null);
                }
                ((TextView) convertView).setText(text);
                return convertView;
            }

        };
        mDraggableGridViewPager.setAdapter(mAdapter);
        mDraggableGridViewPager.setOnPageChangeListener(new DraggableGridViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                L.i(TAG, "onPageScrolled position=" + position + ", positionOffset=" + positionOffset
                        + ", positionOffsetPixels=" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                L.i(TAG, "onPageSelected position=" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                L.i(TAG, "onPageScrollStateChanged state=" + state);
            }
        });
        mDraggableGridViewPager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showToast(((TextView) view).getText().toString());
            }
        });
        mDraggableGridViewPager.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showToast(((TextView) view).getText().toString() + " long clicked!!!");
                return true;
            }
        });
        mDraggableGridViewPager.setOnRearrangeListener(new DraggableGridViewPager.OnRearrangeListener() {
            @Override
            public void onRearrange(int oldIndex, int newIndex) {
                L.i(TAG, "OnRearrangeListener.onRearrange from=" + oldIndex + ", to=" + newIndex);
                String item = mAdapter.getItem(oldIndex);
                mAdapter.setNotifyOnChange(false);
                mAdapter.remove(item);
                mAdapter.insert(item, newIndex);
                mAdapter.notifyDataSetChanged();
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGridCount++;
                mAdapter.add("Grid" + mGridCount);
            }
        });

        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter.getCount() > 0) {
                    mAdapter.remove(mAdapter.getItem(mAdapter.getCount() - 1));
                }
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initData() {

    }

    @Override
    public void requestData() {

    }

    @Override
    public void destroyResource() {

    }
}
