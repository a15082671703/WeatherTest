package com.example.a76568.weathertest;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.hanqin.app.Weatherapplication;
import cn.edu.pku.hanqin.bean.City;

/**
 * Created by 76568 on 2016/10/18 0018.
 */
public class SelectCity extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;

    private ListView listView;

    private List<City> data;
    private City tempdata;
    private Weatherapplication App;

    private String SelectedId=null;
    private String SelectedName=null;
    private TextView cityName;

    private TextView selectcity;
    ArrayList<String> city = new ArrayList<String>();
    ArrayList<String> cityId = new ArrayList<String>();

    ArrayList<String> mData = new ArrayList<String>();   //all城市
    ArrayList<String> mDataSub = new ArrayList<String>();   //用作搜索的城市
    private EditText mSearch;
    Handler myhandler = new Handler();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        mBackBtn = (ImageView) findViewById(R.id.back_normal);
        mBackBtn.setOnClickListener(this);



        //
        set_mSearch_TextChanged();
        App = (Weatherapplication) getApplication();
        data = App.getCityList();
        int i = 0;
        while (i < data.size()) {
            // city.add(data.get(i).getCity().toString());
            // cityId.add(data.get(i++).getNumber().toString());
            tempdata = data.get(i++);
            mData.add(tempdata.getProvince().toString() + '(' + tempdata.getNumber().toString() + ')' + '-' + tempdata.getAllFristPY().toString() + tempdata.getCity().toString());
        }
        //
        getmDataSub(mDataSub,"");
        //
        listView = (ListView) findViewById(R.id.city_list);
        adapter = new ArrayAdapter<String>(SelectCity.this, android.R.layout.simple_list_item_1, mDataSub);
        //missing getData()
        listView.setAdapter(adapter);

        //
        cityName =(TextView)findViewById(R.id. title_city_name);
        selectcity = (TextView)findViewById(R.id.title_city_name);

        //初始化具体选择情况
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String cityName = sharedPreferences.getString("main_city_name", "北京");
        selectcity.setText("您此时的地区："+cityName);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int citybegin = mDataSub.get(i).indexOf('-');
                int idbegin = mDataSub.get(i).indexOf('(');
                Toast.makeText(SelectCity.this, "您单击了:" + mDataSub.get(i).substring(citybegin+1), Toast.LENGTH_SHORT).show();
                SelectedId = mDataSub.get(i).substring(idbegin+1,idbegin+10);
                SelectedName = mDataSub.get(i).substring(citybegin+1);
                selectcity.setText("您选择的地区："+SelectedName);
            }
        });
        //
    }

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
                    editor.putString("main_city_name", SelectedName);
                    editor.commit();
                }
                finish();
                break;
            default:
                break;
        }
    }



    //改变
    private void set_mSearch_TextChanged()
    {
        mSearch = (EditText) findViewById(R.id.search_bar);

        mSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                //这是文本框改变之前会执行的动作
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                /**这是文本框改变之后 会执行的动作
                 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
                 * 所以这里我们就需要加上数据的修改的动作了。
                 */
                Log.d("sousuo","begin");
                    myhandler.post(mChanged);
                Log.d("sousuo","end");
            }
        });
    }
    Runnable mChanged = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String data = mSearch.getText().toString();
            mDataSub.clear();

            getmDataSub(mDataSub, data);

            adapter.notifyDataSetChanged();

        }
    };

    private void getmDataSub(ArrayList<String> mDataSubs, String data)
    {
        int length = mData.size();
        Log.d("length","length"+length);
        for(int i = 0; i < length; ++i){
            if(mData.get(i).contains(data) || data.length() == 0){
                mDataSubs.add(mData.get(i));
            }
        }
    }
}
