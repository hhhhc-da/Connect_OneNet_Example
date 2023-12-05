package com.example.onenet20231204;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SentService extends AppCompatActivity {
    // 返回按钮、快速预约按钮
    protected Button ret, quick_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_service);

        InitView();
        InitListener();
        Log.i("myError" , "onCreate任务完毕");
    }

    class myClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent it = new Intent();

            if(v.getId() == R.id.button2){
                Log.i("myError" , "跳转到快速上门页面");
                it.setClass(SentService.this,CarService.class);

                startActivity(it);
            }

            if(v.getId() == R.id.button3){
                Log.i("myError" , "返回上一个界面");
                finish();
            }
        }
    }

    protected void InitView(){
        // 预约新的上门服务
        quick_start = findViewById(R.id.button2);
        // 返回按钮
        ret = findViewById(R.id.button3);


        Log.i("myError" , "图片按钮初始化完毕");
    }

    protected void InitListener(){
        myClick mc = new myClick();
        ret.setOnClickListener(mc);
        quick_start.setOnClickListener(mc);

        Log.i("myError" , "监听器初始化完毕");
    }
}