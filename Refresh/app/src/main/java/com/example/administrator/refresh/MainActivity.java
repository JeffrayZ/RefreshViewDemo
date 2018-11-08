package com.example.administrator.refresh;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        mHandler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });
    }

    static class MyHandler extends Handler{
        private WeakReference<MainActivity> weakReference;
        public MyHandler(MainActivity activity) {
            super();
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            weakReference.get().refreshView.refreshOver();
        }
    }
}
