package com.example.onenet20231204;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import com.example.onenet20231204.Data;

public class GetHistory extends AppCompatActivity {
    // 总积分、历史记录详情
    protected TextView reward, history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_history);

        // 初始化逻辑
        InitView();
        // 获取服务器数据逻辑
        getNewData();
        Log.i("myError" , "onCreate任务完毕");
    }

    protected void InitView(){
        // 总积分
        reward = findViewById(R.id.textView18);
        // 历史记录
        history = findViewById(R.id.textView16);


        Log.i("myError" , "图片按钮初始化完毕");
    }



    protected void getNewData(){
        // 服务器获取数据逻辑
        Data data = new Data();

        OneNetStructure onenet = new OneNetStructure();
        try {
            String json = onenet.GetJson();

            int count = data.putJson(json);
            if (count != 6) {
                Log.e("myError", "获取信息不全:" + String.valueOf(count) + "/6");
                throw new JSONException("获取信息不全:" + String.valueOf(count) + "/6");
            }

            String nowPoint = data.getValue("point");
            reward.setText(nowPoint);
            history.setText(data.toString());

        }
        catch(JSONException e){
            reward.setText("0");
            history.setText("暂无任何历史记录哦~");
        }
    }
}