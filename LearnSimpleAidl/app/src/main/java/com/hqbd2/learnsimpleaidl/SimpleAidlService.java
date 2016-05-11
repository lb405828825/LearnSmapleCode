package com.hqbd2.learnsimpleaidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.hqbd2.learnsimpleaidl.ISimpleAidlInterface;


public class SimpleAidlService extends Service {

    private final static String TAG = "hqbd2";
    private boolean mFlag = false;
    private static int mCount = 0;

    public SimpleAidlService() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFlag = false;
        System.out.println("------SimpleAidlService, onDestroy()------");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("------SimpleAidlService, onCreate()------");
        mFlag = true;
        //服务端使用线程，mCount向客户端展示服务在运行中;
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("SimpleAidlService, Thread is running");
                while (mFlag){
                    mCount++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("SimpleAidlService, Thread's loop over");
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mSimpleStub;
    }

    //服务stub具体实现aidl中的接口，mSimpleStub通过Binder返回给Proxy代理端使用的；
    ISimpleAidlInterface.Stub mSimpleStub = new ISimpleAidlInterface.Stub() {
        @Override
        public void printHelloAidl() throws RemoteException {
            Log.d(TAG, "Hello, this is SimpleAidlService, you use Aidl invoke");
            System.out.println("Hello, this is SimpleAidlService, you use Aidl invoke");
        }

        @Override
        public int getCount() throws RemoteException {
//            System.out.println("Hello, this is SimpleAidlService, getCount():mCount = " + mCount);
            return mCount;
        }
    };
}
