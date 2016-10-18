package com.example.a76568.weathertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by 76568 on 2016/10/18 0018.
 */
public class SelectCity extends Activity implements View.OnClickListener{
   private ImageView mBackBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        mBackBtn = (ImageView)findViewById(R.id.back_normal);
        mBackBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
    switch (v.getId()){
        case R.id.back_normal:
            Intent i = new Intent();
            i.putExtra("cityCode","101270801");
            setResult(RESULT_OK,i);
            finish();
            break;
        default:
            break;
    }
    }
}
