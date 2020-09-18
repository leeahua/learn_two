package com.example.learn.learn_two;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import com.example.learn.learn_two.model.GatewayRequest;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.EOFException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String EXTENT_MSG = "msg";
    public static final String gateway_base_url = "http://10.1.31.101:1111/iotm/api/node/";
    public static final String gateway_registe_url = "/v1/token/get";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /** Called when the user taps the Send button */
    public void sendMessage(View view){
        EditText editTextVin = (EditText)findViewById(R.id.edit_content);
        EditText editTextType = (EditText)findViewById(R.id.editText_type);
        String vin = editTextVin.getText().toString();
        String carModelId = editTextType.getText().toString();
        if(StringUtils.isBlank(vin) || StringUtils.isBlank(carModelId)){
            Toast.makeText(this,"VIN|车型号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = getBrokerNode(vin,carModelId);
            }
        });
        thread.start();
        //获取TCP服务端口

        Intent intent = new Intent(context, DisplayMessageActivity.class);
        intent.putExtra(EXTENT_MSG, "here");
        startActivity(intent);
    }

    private String getBrokerNode(String vin,String carModelId) {
        GatewayRequest gatewayRequest = new GatewayRequest(vin,carModelId);
        Gson gson = new Gson();
        String data = gson.toJson(gatewayRequest);
        Log.d("getBrokerNode","data:"+data);
        String result = null;
        try {
            result = get(vin);
            Log.d("getBrokerNode","result:"+result);
        } catch (Exception e) {
            Log.e("request", "exception", e);
        }
        return result;
    }

    private String get(String vin) {
        Request request = new Request.Builder()
                .url(gateway_base_url+vin)
                .get()
                .build();
        callRequest(request);
        return "success";

    }

    private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(gateway_base_url+url)
                .post(body)
                .build();
        callRequest(request);
        return "loading";
    }

    private void callRequest(Request request){
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("request","failed", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Gson gson = new Gson();
                if(response.isSuccessful()){
                    try {
                        String string = response.body().string().trim();
                        JSONObject jsonData = (JSONObject) new JSONTokener(string).nextValue();
                        Log.d("request","success, data"+jsonData.toString());
                        //TODO  和node建立连接

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.e("request", "error--->");
                }
            }
        });
    }
}
