package com.example.a76568.weathertest;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 76568 on 2016/12/25 0025.
 */
public class Welcome extends Activity implements ViewPager.OnPageChangeListener{
    private ViewPager vpw;
    private ViewPagerAdapter vpaw;
    private List<View> vieww;

    private ImageView[] dots;
    private int[] ids = {R.id.dot1,R.id.dot2,R.id.dot3};

    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        initViews();
        initDots();
        btn = (Button) vieww.get(2).findViewById(R.id.btn_begin);
        btn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                btn.setText("请等待...");
                Intent i = new Intent(Welcome.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    void initDots(){
        dots = new ImageView[vieww.size()];
        for(int i=0 ;i <vieww.size() ;i++) {
            dots[i] = (ImageView) findViewById(ids[i]);
        }
    }
    private void initViews(){
        LayoutInflater inflater = LayoutInflater.from(this);
        vieww = new ArrayList<View>();
        vieww.add(inflater.inflate(R.layout.welcome1,null));
        vieww.add(inflater.inflate(R.layout.welcome2,null));
        vieww.add(inflater.inflate(R.layout.welcome3,null));
        vpaw = new ViewPagerAdapter(vieww,this);
        vpw = (ViewPager) findViewById(R.id.welcome);
        vpw.setAdapter(vpaw);
        vpw.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int a = 0 ; a< ids.length ; a++){
            if(a == position){
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            }else
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
