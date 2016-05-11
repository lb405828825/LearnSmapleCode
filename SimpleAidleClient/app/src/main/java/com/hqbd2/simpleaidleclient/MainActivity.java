package com.hqbd2.simpleaidleclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hqbd2.learnsimpleaidl.ISimpleAidlInterface;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "hqbd2";
    private static final String AIDL_SERVICE_ACTION = "android.intent.action.SimpleAidlService";

    private Button mBtn;
    private TextView mCountTv;

    private ISimpleAidlInterface mSimpleAidlService;
    private ServiceConnection mSc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //通过Binder获取服务代理，操作服务代理就像在同一进程中使用服务一样的。
            mSimpleAidlService = ISimpleAidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind service
//        Intent i = new Intent(AIDL_SERVICE_ACTION);
        Intent i = new Intent();
        i.setClassName("com.hqbd2.learnsimpleaidl", "com.hqbd2.learnsimpleaidl.SimpleAidlService");
        bindService(i, mSc, BIND_AUTO_CREATE);

        mBtn = (Button) findViewById(R.id.button);
        mCountTv = (TextView) findViewById(R.id.textView2);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mSimpleAidlService.printHelloAidl();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                try {
                    int count = mSimpleAidlService.getCount();
                    mCountTv.setText("Aidl-count: " + count);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
