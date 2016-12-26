package com.example.a76568.weathertest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by 76568 on 2016/12/26 0026.
 */
public class Share extends Activity implements View.OnClickListener {
    private ImageView Btnback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);

        Btnback = (ImageView) findViewById(R.id.share_back);
        Btnback.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_back:
                Intent i = new Intent();
                //i.putExtra("cityCode", SelectedId);
                setResult(RESULT_OK, i);
                finish();
                //
                break;
            default:
                break;
        }
    }
}
