/*
package cn.edu.pku.hanqin.adapter;

*/
/**
 * Created by 76568 on 2016/11/28 0028.
 *//*


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.a76568.weathertest.R;

import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.hanqin.bean.City;


*/
/**
 * ListView��Adapter���ؼ�����SectionIndexer�ӿ��е���������
 * @author Test
 *
 *//*

public class CityListAdapter extends CommonAdapter<City> implements SectionIndexer {

    private List<City> allData;
    private List<City> queryData;

    public CityListAdapter(Context context, List<City> mDatas,
                           int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        allData = mDatas;
        queryData = new ArrayList<City>();
    }

    public void queryData(String query){
        queryData.clear();
        for(City bean : allData){
            String name = bean.getCity();
            if(!TextUtils.isEmpty(name) && name.contains(query)){
                queryData.add(bean);
            }
        }
        mDatas = queryData;
        notifyDataSetChanged();
    }

    public void resetData(){
        mDatas = allData;
        notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder holder, City item, int position) {
        TextView nameTv = holder.getView(R.id.list_item_name);
        TextView headTv = holder.getView(R.id.list_item_head);
        //����position == getPositionForSection(getSectionForPostion(position))�ж�ĳ����ĸ�ǲ����״γ���
        if(position == 0 && getPositionForSection(getSectionForPosition(position)) == -1){
            headTv.setVisibility(View.VISIBLE);
            headTv.setText("#");
        }else if(position == getPositionForSection(getSectionForPosition(position))){
            headTv.setVisibility(View.VISIBLE);
            //��section indexת��Ϊ��д��ĸ
            char c = (char) (getSectionForPosition(position) + 65);
            if(c >= 'A' && c <= 'Z'){
                headTv.setText(String.valueOf(c));
            }
        }else{
            headTv.setVisibility(View.GONE);
        }
        nameTv.setText(item.getCity());
    }

    @Override
    public int getPositionForSection(int arg0) {              //�ؼ�������ͨ��section index��ȡ��ListView�е�λ��
        //���ݲ���arg0������65��õ���Ӧ�Ĵ�д��ĸ
        char c = (char)(arg0 + 65);
        //ѭ������ListView�е����ݣ�������һ������ĸΪ����ľ���Ҫ�ҵ�λ��
        for(int i = 0; i < getCount(); i++){
            if(mDatas.get(i).getFirstPY() == c){
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int arg0) {              //�ؼ�������ͨ����ListView�е�λ�û�ȡSection index
        //��ȡ��λ�õĳ���������ĸ
        char c = (char) (mDatas.get(arg0).getFirstLetter());
        //�������ĸ��A��Z֮�䣬�򷵻�A��Z����������0��25
        if(c >= 'A' && c <= 'Z'){
            return c - 'A';
        }
        //�������ĸ����A��Z����ĸ���򷵻�26�������ͽ��ᱻ���ൽ#����
        return 26;
    }

    @Override
    public Object[] getSections() {
        return null;
    }


}

*/
