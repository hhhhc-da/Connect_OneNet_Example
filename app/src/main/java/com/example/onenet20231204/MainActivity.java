package com.example.onenet20231204;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import com.example.onenet20231204.OneNetStructure;

public class MainActivity extends AppCompatActivity {
    // 预约服务、约单详情、预约历史图片按钮
    protected ImageButton service_start, service_sent, history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitView();
        InitListener();
        Log.i("myError", "onCreate任务完毕");
    }

    class myClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent it = new Intent();

            if (v.getId() == R.id.imageButton) {
                Log.i("myError", "开始运行发送服务请求界面");
                it.setClass(MainActivity.this, CarService.class);

                startActivity(it);
            }

            if (v.getId() == R.id.imageButton2) {
                Log.i("myError", "开始运行当前服务查询界面");
                // 先检查当前有没有预约，然后决定跳转逻辑
                OneNetStructure onenet = new OneNetStructure();

                String status = onenet.GetData("status");
                Log.i("myError", "数据读取成功");

                if (status.equals("0"))
                    it.setClass(MainActivity.this, SentService.class);
                else if(status.equals("1"))
                    it.setClass(MainActivity.this, SentService2.class);
                else
                    Log.i("myError", "status类型错误！！");

                startActivity(it);
            }

            if (v.getId() == R.id.imageButton3) {
                it.setClass(MainActivity.this, GetHistory.class);
                Log.i("myError", "开始运行查询历史记录界面");
                startActivity(it);
            }
        }
    }

    protected void InitView() {
        // 预约新的上门服务
        service_start = findViewById(R.id.imageButton);
        // 查看正在执行的约单
        service_sent = findViewById(R.id.imageButton2);
        // 查看申请的历史记录
        history = findViewById(R.id.imageButton3);

        Log.i("myError", "图片按钮初始化完毕");
    }

    protected void InitListener() {
        myClick mc = new myClick();
        service_start.setOnClickListener(mc);
        service_sent.setOnClickListener(mc);
        history.setOnClickListener(mc);

        Log.i("myError", "监听器初始化完毕");
    }
}