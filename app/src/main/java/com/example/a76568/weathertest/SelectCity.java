package com.example.a76568.weathertest;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

import cn.edu.pku.hanqin.app.Weatherapplication;
import cn.edu.pku.hanqin.bean.City;

/**
 * Created by 76568 on 2016/10/18 0018.
 */
public class SelectCity extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;

    private ListView listView;

    private List<City> data;
    private Weatherapplication App;

    private String SelectedId=null;
    private TextView cityName;

    private TextView selectcity;
    ArrayList<String> city = new ArrayList<String>();
    ArrayList<String> cityId = new ArrayList<String>();

    ArrayList<String> mData = new ArrayList<String>();   //用作搜索的城市
    ArrayList<String> SecityId = new ArrayList<String>();//用作搜索的
    private EditText mSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        mBackBtn = (ImageView) findViewById(R.id.back_normal);
        mBackBtn.setOnClickListener(this);

        mSearch = (EditText) findViewById(R.id.search_bar);

        //

        App = (Weatherapplication) getApplication();
        data = App.getCityList();
        int i = 0;
        while (i < data.size()) {
            city.add(data.get(i).getCity().toString());
            cityId.add(data.get(i++).getNumber().toString());
        }
        //

        listView = (ListView) findViewById(R.id.city_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectCity.this, android.R.layout.simple_list_item_1, city);
        //missing getData()
        listView.setAdapter(adapter);

        //
        cityName =(TextView)findViewById(R.id. title_city_name);
        selectcity = (TextView)findViewById(R.id.title_city_name);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SelectCity.this, "您单击了:" + city.get(i), Toast.LENGTH_SHORT).show();
                SelectedId = cityId.get(i);
                selectcity.setText("您选择的地区："+city.get(i));
            }
        });
        //
    }


    //...搜索
    /*
    private class MyAdapter extends BaseAdapter implements Filterable {
        private MyFilter mFilter;

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = View.inflate(SelectCity.this, android.R.layout.simple_list_item_1,
                        null);
            }

            TextView show = (TextView) convertView.findViewById(R.id.show);

            show.setText(mData.get(position));

            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (null == mFilter) {
                mFilter = new MyFilter();
            }
            return mFilter;
        }

        // 自定义Filter类
        class MyFilter extends Filter {
            @Override
            // 该方法在子线程中执行
            // 自定义过滤规则
            protected android.widget.Filter.FilterResults performFiltering(CharSequence constraint) {
                android.widget.Filter.FilterResults results = new android.widget.Filter.FilterResults();

                List<String> newValues = new ArrayList<String>();
                String filterString = constraint.toString().trim()
                        .toLowerCase();

                // 如果搜索框内容为空，就恢复原始数据
                if (TextUtils.isEmpty(filterString)) {
                    newValues = mBackData;
                } else {
                    // 过滤出新数据
                    for (String str : mBackData) {
                        if (-1 != str.toLowerCase().indexOf(filterString)) {
                            newValues.add(str);
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          android.widget.Filter.FilterResults results) {
                mData = (List<String>) results.values;

                if (results.count > 0) {
                    mAdapter.notifyDataSetChanged();  // 通知数据发生了改变
                } else {
                    mAdapter.notifyDataSetInvalidated(); // 通知数据失效
                }
            }
        }
    }

    // 搜索文本监听器
    private class QueryListener implements SearchView.OnQueryTextListener {
        // 当内容被提交时执行
        @Override
        public boolean onQueryTextSubmit(String query) {
            return true;
        }

        // 当搜索框内内容发生改变时执行
        @Override
        public boolean onQueryTextChange(String newText) {
            if (TextUtils.isEmpty(newText)) {
                ListView.clearTextFilter();  // 清楚ListView的过滤
            } else {
                ListView.setFilterText(newText); // 设置ListView的过滤关键词
            }
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // 获取Menu中searchView组件
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu
                .findItem(R.id.search_bar));

        // 设置监听器
        mSearchView.setOnQueryTextListener(new QueryListener());
        return true;
    }

//搜索。。。


    //
    */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_normal:
                Intent i = new Intent();
                i.putExtra("cityCode", SelectedId);
                setResult(RESULT_OK, i);
                //
                if(SelectedId != null) {
                    SharedPreferences mySharedPreferences = getSharedPreferences("config",
                            Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putString("main_city_code", SelectedId);
                    editor.commit();
                }
                finish();
                break;
            default:
                break;
        }
    }

}
