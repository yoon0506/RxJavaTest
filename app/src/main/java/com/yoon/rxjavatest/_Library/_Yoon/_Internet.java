package com.yoon.rxjavatest._Library._Yoon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class _Internet {


    private static _Internet instance = new _Internet();

    public static _Internet GetInstance() {
        return instance;
    }

    private Listener mListener;

    public void SetListener(Listener listener) {
        mListener = listener;
    }

    public final int TYPE_CONNECTED = 1;
    public final int TYPE_NOT_CONNECTED = 2;

    // 인터넷 연결 되어있는지 체크(activityLanding에 결과를 보내줌)
    public void getConnectivityStatus(Context context, Listener listener) {
        SetListener(listener);
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            int type = networkInfo.getType();
            if (type == ConnectivityManager.TYPE_MOBILE || type == ConnectivityManager.TYPE_WIFI)    // 3g 또는 LTE 연결 or WIFI.
            {
                if(mListener != null){
                    mListener.result(TYPE_CONNECTED);
                }
            }else{
                if(mListener != null){
                    mListener.result(TYPE_NOT_CONNECTED);
                }
            }
        }
    }

    // 인터넷 연결 되어있는지 체크
    @SuppressLint("MissingPermission")
    public boolean CheckInternetConnection(Context context) {
        ConnectivityManager con_manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected());
    }

    public interface Listener {
        public void result(int result);
    }
}
