package com.example.lex.networktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = (Button) findViewById(R.id.send_request);
        responseText = (TextView) findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_request){
            sendRequestWithHttpURLConnection();
        }
    }

    private void sendRequestWithHttpURLConnection() {
        //开启线程来发送网络请求
//        Log.i("-----", "run: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null ;
                BufferedReader reader = null ;
                try {
                    URL url = new URL("http://www.163.com");
//                    HttpClient： API数量过多、扩展困难
//                    InputStream inputStream = url.openStream();
//                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
//                    byte[]by=new byte[1024];
//                    int n=0;
//                    while((n=inputStream.read(by))!=-1){
//                        baos.write(by,0,n);
//                    }
//                    inputStream.close();
//                    baos.close();
//                    String response = new String(baos.toByteArray());
                    connection = (HttpURLConnection) url.openConnection();
//                    提交数据给服务器
//                    connection.setRequestMethod("POST");
//                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//                    out.writeBytes("username=admin&password=123456");
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    //下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) !=null){
                       response.append(line);
                    }
                    Log.i("-----", "run: "+response);
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (reader !=null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(final String response) {
        //Android中不允许在子线程中进行UI操作，通过这个方法将县城切换到主线程
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //在这里进行UI操作，将结果显示到界面上
                    responseText.setText(response);
                }
            });
    }

}



