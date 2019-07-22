package com.wxb.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;

public class MainActivity extends AppCompatActivity {

    private NetworkChangeReceiver networkChangeReceiver;
    private IntentFilter intentFilter;
    private Button Send_broadcast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Send_broadcast=(Button)findViewById(R.id.Send);
        Send_broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发给自己
                Intent intent=new Intent("com.wxb.broadcasttest.MyBroadcast");
                //ComponentName("com.wxb.broadcasttest","com.wxb.broadcasttest.MyReceiver"))
                // 解释如下，8.0以后，需要提供接收器包名com.wxb.broadcasttest，和接收器类名全路径，
                // 如MyBrocast接收器的全路径为com.wxb.broadcasttest.MyReceiver即包名加类名
                intent.setComponent(new ComponentName("com.wxb.broadcasttest","com.wxb.broadcasttest.MyReceiver"));
                sendBroadcast(intent);
                //发给包名为com.wxb.brocasttest2的APP
                Intent intent1=new Intent("com.wxb.brocasttest2.DATA");
               // intent1.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                //intent1.setComponent(new ComponentName("com.wxb.brocasttest2","com.wxb.brocasttest2.AnotherBroadcaster"));
                intent1.setPackage("com.wxb.brocasttest2");
                sendBroadcast(intent1);
            }
        });
        intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver=new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);
    }
    class NetworkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            AndPermission.with(MainActivity.this)
                   .permission("android.permission.ACCESS_NETWORK_STATE").requestCode(100)
                   .send();
            ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null&&networkInfo.isAvailable())
            Toast.makeText(context,"Network is avaiable",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context,"Network is not avaiable",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }
}
