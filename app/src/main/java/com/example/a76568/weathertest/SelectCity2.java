/*
package com.example.a76568.weathertest;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Filter;

import cn.edu.pku.hanqin.adapter.CityListAdapter;
import cn.edu.pku.hanqin.app.Weatherapplication;
import cn.edu.pku.hanqin.bean.City;

*/
/**
 * Created by 76568 on 2016/10/18 0018.
 *//*

public class SelectCity2 extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;


    private List<City> data;
    private Weatherapplication App;

    private String SelectedId=null;
    private TextView cityName;

    private TextView selectcity;
    ArrayList<String> city = new ArrayList<String>();
    ArrayList<String> cityId = new ArrayList<String>();

    private EditText searchEt;
    private ListView listView;
    private ProgressDialog pd;
    private TextView hintTv;
    private SideBar sideBar;
    private CityListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        mBackBtn = (ImageView) findViewById(R.id.back_normal);
        mBackBtn.setOnClickListener(this);

        searchEt = (EditText) findViewById(R.id.search_bar);

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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectCity2.this, android.R.layout.simple_list_item_1, city);
        //missing getData()
        listView.setAdapter(adapter);

        //
        cityName =(TextView)findViewById(R.id. title_city_name);
        selectcity = (TextView)findViewById(R.id.title_city_name);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SelectCity2.this, "您单击了:" + city.get(i), Toast.LENGTH_SHORT).show();
                SelectedId = cityId.get(i);
                selectcity.setText("您选择的地区："+city.get(i));
            }
        });
        //
    }


    private void initView(){
        hintTv = (TextView) findViewById(R.id.centerHintTv);
        searchEt = (EditText) findViewById(R.id.search_bar);
        listView = (ListView) findViewById(R.id.city_list);
        sideBar = (SideBar) findViewById(R.id.sidebar);
        pd = new ProgressDialog(this);
        pd.setTitle("��ʾ");
        pd.setMessage("���ڽ������ݣ����Ե�...");
        pd.setCancelable(false);
        new ParseXmlTask().execute();

        searchEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if(adapter != null){
                    int len = arg0.length();
                    if(len == 0){
                        adapter.resetData();
                    }else if(len > 0){
                        adapter.queryData(arg0.toString());
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        //����SideBar����ָ���º�̧���¼�
        sideBar.setOnSelectListener(new OnSelectListener() {

            @Override
            public void onSelect(String s) {

                //��ָ����ʱ��ʾ�������ĸ
                hintTv.setVisibility(View.VISIBLE);
                hintTv.setText(s);
                //���SideBar���µ���#�ţ���ListView��λ��λ��0
                if("#".equals(s)){
                    listView.setSelection(0);
                    return ;
                }
                //��ȡ��ָ���µ���ĸ���ڵĿ�����
                int section = s.toCharArray()[0];
                //���ݿ�������ȡ����ĸ�״���ListView�г��ֵ�λ��
                int pos = adapter.getPositionForSection(section - 65);
                //��λListView��������ĸ�״γ��ֵ�λ��
                listView.setSelection(pos);
            }

            @Override
            public void onMoveUp(String s) {
                hintTv.setVisibility(View.GONE);
                hintTv.setText(s);
            }
        });
    }

    //��ʾ�����б�
    private void showListView(final List<CityBean> list){
        adapter = new CityListAdapter(this, list, R.layout.list_item);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //���ListView��ĳ��item����ʾ��ǰѡ��ĳ�����
                String name = adapter.getItem(arg2).getName();
                Toast toast = Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    private class ParseXmlTask extends AsyncTask<Void, Void, Void> {

        private XmlPullParser pullParser;
        private List<ProvinceBean> provinceList;
        private ProvinceBean province;
        private CityBean city;
        private CountyBean county;

        public ParseXmlTask(){
            provinceList = new ArrayList<ProvinceBean>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //��ʼ����ǰ���½�������
            pullParser = Xml.newPullParser();
            //��rawĿ¼�»�ȡ�����������ݵ�������
            InputStream is = getResources().openRawResource(R.raw.cities);
            try {
                pullParser.setInput(is, "UTF-8");
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            //��ʾ���ȶԻ���
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            int eventType = 1;
            try {
                eventType = pullParser.getEventType();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            //whileѭ������xml����
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_TAG:
                        String startTag = pullParser.getName();
                        if("province".equals(startTag)){//ʡ
                            province = new ProvinceBean();
                            province.setId(pullParser.getAttributeValue(null, "id"));
                            province.setName(pullParser.getAttributeValue(null, "name"));
                        }else if("city".equals(startTag)){//��
                            city = new CityBean();
                            city.setId(pullParser.getAttributeValue(null, "id"));
                            city.setName(pullParser.getAttributeValue(null, "name"));
                            String name = city.getName();
                            if(!TextUtils.isEmpty(name) && name.length() > 0){
                                //��ȡ������������ĸ�������ȡ����Сд��
                                String pinyin = CharacterParser.getInstance().getPinYinSpelling(name.substring(0, 1));
                                //-32��ȡСд����ĸ��Ӧ�Ĵ�д��ĸ
                                city.setFirstLetter((char)(pinyin.charAt(0) - 32));
                            }
                        }else if("county".equals(startTag)){//������
                            county = new CountyBean();
                            county.setId(pullParser.getAttributeValue(null, "id"));
                            county.setName(pullParser.getAttributeValue(null, "name"));
                            county.setWeatherCode(pullParser.getAttributeValue(null, "weatherCode"));
                            if(city != null){
                                city.getCountyList().add(county);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = pullParser.getName();
                        if("city".equals(endTag) && province != null){
                            province.getCityList().add(city);
                        }else if("province".equals(endTag) && province != null){
                            provinceList.add(province);
                        }
                        break;
                }
                try {
                    eventType = pullParser.next();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(provinceList != null){
                //forѭ����ȡ���еĳ�����
                for(ProvinceBean province : provinceList){
                    List<CityBean> cityList = province.getCityList();
                    for(CityBean city : cityList){
                        list.add(city);
                    }
                }
                Collections.sort(list, comparator);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pd.dismiss();
            showListView(list);
        }

    }

    private Comparator<CityBean> comparator = new Comparator<CityBean>() {

        @Override
        public int compare(CityBean arg0, CityBean arg1) {
            //��ȡ��������Ӧ��ƴ����ͨ���Ƚ�ƴ����ȷ�����е��Ⱥ����
            String pinyin0 = CharacterParser.getInstance().getPinYinSpelling(arg0.getName());
            String pinyin1 = CharacterParser.getInstance().getPinYinSpelling(arg1.getName());
            return pinyin0.compareTo(pinyin1);
        }
    };


    //
    *//*

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
*/
