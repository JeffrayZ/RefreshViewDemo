package com.example.administrator.refresh;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.refreshlibrary.RefreshListener;
import com.example.refreshlibrary.RefreshView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private RefreshView refreshView;
    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshView = findViewById(R.id.refresh_view);

        refreshView.setListener(new RefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(1,3000);
            }

            @Override
            public void onLoadMore() {
                mHandler.sendEmptyMessageDelayed(-1,3000);
            }
        });
    }

    static class MyHandler extends Handler {
        private WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity activity) {
            super();
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Log.e("MyHandler", "下拉距离 >>>>> ");
                weakReference.get().refreshView.refreshOver();
            } else if (msg.what == -1) {
                Log.e("MyHandler", "上拉距离 >>>>> ");
                weakReference.get().refreshView.loadOver();
            }
        }
    }
}
