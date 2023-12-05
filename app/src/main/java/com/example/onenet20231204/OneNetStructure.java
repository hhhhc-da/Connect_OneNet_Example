package com.example.onenet20231204;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OneNetStructure {
    private static final String API_KEY = "zfd04fM6KXk53KAZdmTdfNeWnPs=";
    private static final String DEVICE_ID = "1170345122";
    private static final String URL = "http://api.heclouds.com/devices/" + DEVICE_ID + "/datapoints";
    public String ret = new String("");
    public boolean error = false;

    // String status = onenet.GetData("status");
    public String GetData(String query) {
        OkHttpClient client = new OkHttpClient();

        // 构建请求
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
        urlBuilder.addQueryParameter("datastream_ids", "type,x_pos,y_pos,date,point,status"); // 设置要查询的数据流 ID
        urlBuilder.addQueryParameter("limit", "1"); // 设置限制返回的数据点数量
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header("api-key", API_KEY)
                .get()
                .build();

        // 发送请求并处理响应
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败处理
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功处理
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    Log.i("myError","HTTP请求成功（GET）");
                    Log.i("myError",responseBody);

                    try {
                        // 将 JSON 字符串转换为 JSONObject 对象
                        JSONObject jsonObject = new JSONObject(responseBody);
                        // 从 JSONObject 中获取 "data" 对应的 JSONObject
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        // 从 dataObject 中获取 "datastreams" 对应的 JSONArray
                        JSONArray datastreamsArray = dataObject.getJSONArray("datastreams");
                        // 遍历 datastreamsArray，查找 id 为 "status" 的数据流
                        for (int i = 0; i < datastreamsArray.length(); i++) {
                            JSONObject streamObject = datastreamsArray.getJSONObject(i);
                            if (streamObject.getString("id").equals("status")) {
                                // 从 streamObject 中获取 "datapoints" 对应的 JSONArray
                                JSONArray datapointsArray = streamObject.getJSONArray("datapoints");
                                // 获取第一个 datapoint 中 "value" 对应的字符串
                                String statusValue = datapointsArray.getJSONObject(0).getString("value");
                                // 输出 status 值
                                Log.i("myError",query + ": " + statusValue);
                                ret = statusValue;
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        Log.i("myError",e.toString());
                    }
                }
                else {
                    error = true;
                    Log.i("myError","HTTP请求失败（GET）");
                }

                response.close();
            }
        });

        while(ret.equals("")){
            // 错误退出
            if(error == true)
                break;

            // 多线程等待
            try {
                Log.i("myError","暂停100ms");
                Thread.sleep(100); // 暂停 200 毫秒
            } catch (InterruptedException e) {
                Log.i("myError",e.toString());
            }
        }

        String tmp = ret;
        ret = new String("");
        return tmp;
    }

    public String GetJson() {
        OkHttpClient client = new OkHttpClient();

        // 构建请求
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
        urlBuilder.addQueryParameter("datastream_ids", "type,x_pos,y_pos,date,point,status"); // 设置要查询的数据流 ID
        urlBuilder.addQueryParameter("limit", "1"); // 设置限制返回的数据点数量
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header("api-key", API_KEY)
                .get()
                .build();

        // 发送请求并处理响应
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败处理
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功处理
                if (response.isSuccessful()) {
                    ret = response.body().string();

                    Log.i("myError","HTTP请求成功（GET）");
                    Log.i("myError", ret);
                }
                else {
                    error = true;
                    Log.i("myError","HTTP请求失败（GET）");
                }

                response.close();
            }
        });

        while(ret.equals("")){
            // 错误退出
            if(error == true)
                break;

            // 多线程等待
            try {
                Log.i("myError","暂停100ms");
                Thread.sleep(100); // 暂停 200 毫秒
            } catch (InterruptedException e) {
                Log.i("myError",e.toString());
            }
        }

        String tmp = ret;
        ret = new String("");
        return tmp;
    }

    // onenet.PostSingle("status","0");
    public void PostSingle(String id, String value){
        OkHttpClient client = new OkHttpClient();

        // 构建 JSON 格式的数据
        String json = "{\"datastreams\":[{\"id\":\"" + id + "\",\"datapoints\":[{\"value\":\"" + value + "\"}]}]}";

        // 构建请求
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(URL)
                .header("api-key", API_KEY)
                .post(body)
                .build();

        Log.i("myError",request.toString());

        // 发送请求并处理响应
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败处理
                Log.i("myError",e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功处理
                if (!response.isSuccessful()) {
                    Log.i("myError","HTTP请求（POST）失败");
                }
                else{
                    Log.i("myError","HTTP请求（POST）成功");
                    Log.i("myError",response.toString());
                }

                response.close();
            }
        });
    }

    // onenet.PostData("4","100","200","20231204-2025","100","0");
    public void PostData(String type, String x_pos, String y_pos, String date,String point, String status) {
        OkHttpClient client = new OkHttpClient();

        // 构建 JSON 格式的数据
        String json = "{\"datastreams\":[{\"id\":\"type\",\"datapoints\":[{\"value\":\"" + type + "\"}]},{\"id\":\"x_pos\",\"datapoints\":[{\"value\":\"" + x_pos + "\"}]},{\"id\":\"y_pos\",\"datapoints\":[{\"value\":\"" + y_pos + "\"}]},{\"id\":\"date\",\"datapoints\":[{\"value\":\"" + date + "\"}]},{\"id\":\"point\",\"datapoints\":[{\"value\":\"" + point + "\"}]},{\"id\":\"status\",\"datapoints\":[{\"value\":\"" + status + "\"}]}]}";

        // 构建请求
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(URL)
                .header("api-key", API_KEY)
                .post(body)
                .build();

        Log.i("myError",request.toString());

        // 发送请求并处理响应
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败处理
                Log.i("myError",e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功处理
                if (!response.isSuccessful()) {
                    Log.i("myError","HTTP请求（POST）失败");
                }
                else{
                    Log.i("myError","HTTP请求（POST）成功");
                    Log.i("myError",response.toString());
                }

                response.close();
            }
        });
    }
}
