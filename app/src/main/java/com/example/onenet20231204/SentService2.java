package com.example.onenet20231204;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.onenet20231204.Data;
import org.json.JSONException;
import java.util.Vector;

public class SentService2 extends AppCompatActivity {
    // 预约位置、回收类型
    protected TextView location, cycle_type;
    // 结束预约按钮
    protected Button end_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_service2);

        // 初始化逻辑
        InitView();
        InitListener();
        // 获取服务器数据逻辑
        getNewDisplay();
        Log.i("myError" , "onCreate任务完毕");
    }

    class myClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.button4){
                Log.i("myError" , "结束按钮");
                // 向服务器发送结束标志（发送紧急召回）
                OneNetStructure onenet = new OneNetStructure();
                onenet.PostSingle("status","0");

                Log.i("myError" , "发送结束命令完毕");
                finish();
            }
        }
    }

    protected void InitView(){
        // 预约位置
        location = findViewById(R.id.textView13);
        // 回收类型
        cycle_type = findViewById(R.id.textView14);
        // 结束按钮
        end_button = findViewById(R.id.button4);

        Log.i("myError" , "图片按钮初始化完毕");
    }

    protected void InitListener(){
        myClick mc = new myClick();
        end_button.setOnClickListener(mc);

        Log.i("myError" , "监听器初始化完毕");
    }

    protected void getNewDisplay(){
        OneNetStructure onenet = new OneNetStructure();
        String json = onenet.GetJson();

        Data data = new Data();
        try {
            int count = data.putJson(json);
            if (count != 6) {
                Log.e("myError", "获取信息不全:" + String.valueOf(count) + "/6");
                throw new JSONException("获取信息不全:" + String.valueOf(count) + "/6");
            }

            String x_pos = data.getValue("x_pos");
            String y_pos = data.getValue("y_pos");

            String st1 = "X: " + x_pos + ", Y: " + y_pos;
            Log.i("myError", st1);

            location.setText(st1);

            String type = data.getValue("type");
            int it = new Integer(type).intValue();
            Log.i("myError","检测到类型: " + String.valueOf(it));

            Vector<String> vec = new Vector<>();
            String[] tps = new String[]{"其他","电池","布料","塑料瓶"};

            for(int i = 1;i < 5;++i){
                if((it & (1<<i)) != 0){
                    vec.add(tps[i-1]);
                }
            }

            String output = "";

            for(String str : vec){
                output += str + "\t";
            }

            cycle_type.setText(output);
        }
        catch(JSONException e){
            location.setText("X:未知, Y:未知");
            cycle_type.setText("未知错误!! 请刷新重试");
        }
    }
}