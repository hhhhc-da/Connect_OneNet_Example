package com.example.onenet20231204;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Data{
    private final String[] nporj = {"type","x_pos","y_pos","point","date","status"};
    private Map<String, String> f;

    public String getValue(String key){
        return f.get(key);
    }

    public Data(){
        f = new HashMap<>();
    }

    public Data(String json){
        f = new HashMap<>();

        try {
            // 将 JSON 字符串转换为 JSONObject 对象
            JSONObject jsonObject = new JSONObject(json);
            // 从 JSONObject 中获取 "data" 对应的 JSONObject
            JSONObject dataObject = jsonObject.getJSONObject("data");
            // 从 dataObject 中获取 "datastreams" 对应的 JSONArray
            JSONArray datastreamsArray = dataObject.getJSONArray("datastreams");
            // 遍历 datastreamsArray，查找 id 为 "status" 的数据流
            for (int i = 0; i < datastreamsArray.length(); i++) {
                JSONObject streamObject = datastreamsArray.getJSONObject(i);
                for(String query : nporj) {
                    if (streamObject.getString("id").equals(query)) {
                        // 从 streamObject 中获取 "datapoints" 对应的 JSONArray
                        JSONArray datapointsArray = streamObject.getJSONArray("datapoints");
                        // 获取第一个 datapoint 中 "value" 对应的字符串
                        String statusValue = datapointsArray.getJSONObject(0).getString("value");
                        // 记录 status 值
                        f.put(query, statusValue);
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            Log.i("myError",e.toString());
        }
    }

    public int putJson(String json)
    {
        int count = 0;

        try {
            // 将 JSON 字符串转换为 JSONObject 对象
            JSONObject jsonObject = new JSONObject(json);
            // 从 JSONObject 中获取 "data" 对应的 JSONObject
            JSONObject dataObject = jsonObject.getJSONObject("data");
            // 从 dataObject 中获取 "datastreams" 对应的 JSONArray
            JSONArray datastreamsArray = dataObject.getJSONArray("datastreams");
            // 遍历 datastreamsArray，查找 id 为 "status" 的数据流
            for (int i = 0; i < datastreamsArray.length(); i++) {
                JSONObject streamObject = datastreamsArray.getJSONObject(i);
                for(String query : nporj) {
                    if (streamObject.getString("id").equals(query)) {
                        // 从 streamObject 中获取 "datapoints" 对应的 JSONArray
                        JSONArray datapointsArray = streamObject.getJSONArray("datapoints");
                        // 获取第一个 datapoint 中 "value" 对应的字符串
                        String statusValue = datapointsArray.getJSONObject(0).getString("value");
                        // 记录 status 值
                        f.put(query, statusValue);
                        count++;
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            Log.i("myError",e.toString());
        }

        return count;
    }

    @Override
    public String toString(){
        String str = "";

        String x_pos = getValue("x_pos");
        String y_pos = getValue("y_pos");

        String st1 = "X: " + x_pos + ", Y: " + y_pos;
        Log.i("myError", st1);

        String type = getValue("type");
        int it = new Integer(type).intValue();
        Log.i("myError","检测到类型: " + String.valueOf(it));

        Vector<String> vec = new Vector<>();
        String[] tps = new String[]{"其他","电池","布料","塑料瓶"};

        for(int i = 1;i < 5;++i){
            if((it & (1<<i)) != 0){
                vec.add(tps[i-1]);
            }
        }

        String st2 = "";

        for(String str1 : vec){
            st2 += str1 + "\t";
        }

        String st3 = getValue("point");
        String date = getValue("date");

        str += ("预约时间:\n\t" + date + "\n");
        str += ("回收类型:\n\t" + st2 + "\n");
        str += ("预约位置:\n\t" + st1);

        return str;
    }

    public void clear(){
        f.clear();
    }
}
