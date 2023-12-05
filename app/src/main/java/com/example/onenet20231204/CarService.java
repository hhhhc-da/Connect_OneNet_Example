package com.example.onenet20231204;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CarService extends AppCompatActivity {
    // 回收类型
    protected CheckBox cycle_types[];
    // 回收地址
    protected String address[];
    protected EditText X,Y;
    // 预约按钮
    protected Button service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_service);

        InitView();
        InitListener();
        Log.i("myError" , "onCreate任务完毕");
    }

    class myClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.button10){
                Log.i("myError" , "开始预约");

                OneNetStructure onenet = new OneNetStructure();
                String json = onenet.GetJson();
                Log.i("myError" , "开始预约");

                Data data = new Data();
                try {
                    int count = data.putJson(json);
                    if (count != 6) {
                        Log.e("myError", "获取信息不全:" + String.valueOf(count) + "/6");
                        throw new JSONException("获取信息不全:" + String.valueOf(count) + "/6");
                    }
                }
                catch(Exception e){
                    Log.e("myError",e.toString());
                }

                String status = data.getValue("status");
                String opt = data.getValue("point");

                if(status.equals("1")){
                    Log.i("myError","您已经有预约了哦，请勿重复预约~");
                    AlertDialog.Builder builder=new AlertDialog.Builder(CarService.this);
                    builder.setTitle("Tips")
                            .setMessage("您已经有预约了哦，请勿重复预约~")
                            .create()
                            .show();
                }
                else
                {
                    Log.i("myError","准备处理预约信息");

                    // String opt = onenet.GetData("point");
                    Log.i("myError","总点数（旧）：" + opt);

                    int result = 0;
                    int dpt = 0;

                    for(int i = 0; i < 4; ++i){
                        result <<= 1;

                        if(cycle_types[i].isChecked()){
                            result |= 1;
                            dpt += 10;
                        }
                    }
                    Log.i("myError","类别：" + String.valueOf(result));

                    Date date=new Date();//此时date为当前的时间
                    System.out.println(date);
                    SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd");//设置当前时间的格式，为年-月-日
                    System.out.println(dateFormat.format(date));
                    SimpleDateFormat dateFormat_min=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");//设置当前时间的格式，为年-月-日 时-分-秒
                    String nowDate = dateFormat_min.format(date);
                    Log.i("myError","当前时间：" + nowDate);

                    Integer npt = new Integer(opt);
                    int rst = npt.intValue() + dpt;
                    opt = String.valueOf(rst);
                    Log.i("myError","总点数（新）：" + opt);

                    String x_pos = X.getText().toString();
                    String y_pos = Y.getText().toString();

                    try {
                        Integer.valueOf(x_pos);
                        Integer.valueOf(y_pos);

                        onenet.PostData(String.valueOf(result), x_pos, y_pos, nowDate, opt, "1");

                        Log.i("myError", "退出发送页面");
                        finish();
                    }
                    catch(Exception e){
                        Log.i("myError","请输入正确的地址");
                        AlertDialog.Builder builder=new AlertDialog.Builder(CarService.this);
                        builder.setTitle("Tips")
                                .setMessage("请输入正确的地址")
                                .create()
                                .show();
                    }
                }
            }
        }
    }

    protected void InitView(){
        // 地址
        X = findViewById(R.id.editTextText2);
        Y = findViewById(R.id.editTextText3);
        address = new String[2];
        address[0] = X.getText().toString();
        address[1] = Y.getText().toString();

        // 类型
        cycle_types = new CheckBox[4];

        cycle_types[0] = findViewById(R.id.checkBox);
        cycle_types[1] = findViewById(R.id.checkBox2);
        cycle_types[2] = findViewById(R.id.checkBox3);
        cycle_types[3] = findViewById(R.id.checkBox4);

        // 开始预约按钮
        service = findViewById(R.id.button10);

        Log.i("myError" , "图片按钮初始化完毕");
    }

    protected void InitListener(){
        myClick mc = new myClick();
        service.setOnClickListener(mc);

        Log.i("myError" , "监听器初始化完毕");
    }
}