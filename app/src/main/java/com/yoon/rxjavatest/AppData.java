package com.yoon.rxjavatest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
//
//import busData.BusSelection;
//import busData.BusStop;
//import busData.BusStopFromCSV;
//import busData.BusTimeLine;

import com.yoon.rxjavatest.busData.BusSelection;
import com.yoon.rxjavatest.busData.BusStop;
import com.yoon.rxjavatest.busData.BusStopFromCSV;
import com.yoon.rxjavatest.busData.BusTimeLine;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AppData {

    private static AppData mInstance = new AppData();

    public static AppData GetInstance() {
        return mInstance;
    }

    public Activity mActivity;

    // notification
    public boolean mIsPushNoti = false;
    public boolean mIsFromPushNoti = false;
    public boolean mIsCheckBoxVisible = false;
    public boolean mIsBusOn = false;
    public HashMap<String, String> mNotificationData = new HashMap<>();

    public int GetLoadingTimer(Context context){
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return mmPref.getInt(Key.LOADING_TIMER, 5000);
    }

    public boolean SetLoadingTimer(Context context, int loadingTimer) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor mmEdt = mmPref.edit();
        mmEdt.putInt(Key.LOADING_TIMER, loadingTimer);
        return mmEdt.commit();
    }

    // 네이버 맵을 작동할때만 임시로 trustManager를 넘기는 boolean.
    public boolean mIsNaverMapOn = false;

    public String GetMessageURL(Context context) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return mmPref.getString(Key.NOTIFICATION_URL, "");
    }

    public boolean SetMessageURL(Context context, String currentURL) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor mmEdt = mmPref.edit();
        mmEdt.putString(Key.NOTIFICATION_URL, currentURL);
        return mmEdt.commit();
    }

    public String GetTokenKey(Context context) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return mmPref.getString(Key.TOKEN_ID, "");
    }

    public boolean SetTokenKey(Context context, String tokenKey) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor mmEdt = mmPref.edit();
        mmEdt.putString(Key.TOKEN_ID, tokenKey);
        return mmEdt.commit();
    }

    public boolean GetSSLCheck(Context context) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return mmPref.getBoolean(Key.SSL_CHECK, false);
    }

    public boolean SetSSLCheck(Context context, Boolean sslCheck) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor mmEdt = mmPref.edit();
        mmEdt.putBoolean(Key.SSL_CHECK, sslCheck);
        return mmEdt.commit();
    }

    public boolean GetPushState(Context context) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return mmPref.getBoolean(Key.PUSH_STATE, false);
    }

    public boolean SetPushState(Context context, boolean pushState) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor mmEdt = mmPref.edit();
        mmEdt.putBoolean(Key.PUSH_STATE, pushState);
        return mmEdt.commit();
    }

    public String GetReceivedMsg(Context context) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return mmPref.getString(Key.RECEIVED_MSG, "");
    }

    public boolean SetReceivedMsg(Context context, String receivedMsg) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor mmEdt = mmPref.edit();
        mmEdt.putString(Key.RECEIVED_MSG, receivedMsg);
        return mmEdt.commit();
    }

    public int GetSelectedNum(Context context) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return mmPref.getInt(Key.SELECTED_NUM, -1);
    }

    public boolean SetSelectedNum(Context context, int selectedNum) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor mmEdt = mmPref.edit();
        mmEdt.putInt(Key.SELECTED_NUM, selectedNum);
        return mmEdt.commit();
    }

    public String GetCurrentURL(Context context) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return mmPref.getString(Key.CURRENT_URL, "");
    }

    public boolean SetCurrentURL(Context context, String currentURL) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor mmEdt = mmPref.edit();
        mmEdt.putString(Key.CURRENT_URL, currentURL);
        return mmEdt.commit();
    }

    public String GetSearchResult(Context context) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        return mmPref.getString(Key.SEARCH_RESULT, "");
    }

    public boolean SetSearchResult(Context context, String searchResult) {
        SharedPreferences mmPref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        SharedPreferences.Editor mmEdt = mmPref.edit();
        mmEdt.putString(Key.SEARCH_RESULT, searchResult);
        return mmEdt.commit();
    }


//
//
//    // csv에서 받은 버스 정류장 정보 리스트
    public ArrayList<BusStopFromCSV> mCSVBusStopList = new ArrayList<>();
//
//    // 버스 정류장 리스트
    public ArrayList<BusStop> mBusStopList = new ArrayList<>();

    public ArrayList<BusStop> SetBusStopList(ArrayList<BusStop> arrayList) {
        if (arrayList != null) {
            mBusStopList = new ArrayList<>();
            for (BusStop busStopList : arrayList) {
                mBusStopList.add(busStopList);
            }
        }
        return mBusStopList;
    }

    // 선택된 버스의 타임라인 정보
    public ArrayList<BusTimeLine> mBusTimeLineList = new ArrayList<>();

    public ArrayList<BusTimeLine> SetBusTimeLineList(ArrayList<BusTimeLine> arrayList) {
        if (arrayList != null) {
            mBusTimeLineList = new ArrayList<>();
            for (BusTimeLine busTimeLineList : arrayList) {
                mBusTimeLineList.add(busTimeLineList);
            }
        }
        return mBusTimeLineList;
    }

//    // 해당 정류장의 버스 리스트 정보
    public ArrayList<BusSelection> mBusSelectionDataList = new ArrayList<>();
//
    public ArrayList<BusSelection> SetBusSelectionDataList(ArrayList<BusSelection> arrayList) {
        if (arrayList != null) {
            mBusSelectionDataList = new ArrayList<>();
            for (BusSelection busTimeLineList : arrayList) {
                mBusSelectionDataList.add(busTimeLineList);
            }
        }
        return mBusSelectionDataList;
    }
//
//    // 알림을 받을 버스 리스트 정보
//    public ArrayList<BusStop> mNotiBusList = new ArrayList<>();
//
//    public ArrayList<BusStop> SetNotiBusStopList(ArrayList<BusStop> arrayList) {
//        if (arrayList != null) {
//            mNotiBusList = new ArrayList<>();
//            for (BusStop notiBusStopList : arrayList) {
//                mNotiBusList.add(notiBusStopList);
//            }
//        }
//        return mNotiBusList;
//    }
//
//    // 댓글 알림 리스트 정보
//    public ArrayList<CommentData> mNotiCommentList = new ArrayList<>();
//
//    public ArrayList<CommentData> SetNotiCommentList(ArrayList<CommentData> arrayList) {
//        if (arrayList != null) {
//            mNotiCommentList = new ArrayList<>();
//            for (CommentData commentList : arrayList) {
//                mNotiCommentList.add(commentList);
//            }
//        }
//        return mNotiCommentList;
//    }
//
//    // 모든 푸시 알림 리스트 정보
//    public ArrayList<PushData> mPushDataList = new ArrayList<>();
//
//    public ArrayList<PushData> SetPushDataList(ArrayList<PushData> arrayList) {
//        if (arrayList != null) {
//            mPushDataList = new ArrayList<>();
//            for (PushData pushDataList : arrayList) {
//                mPushDataList.add(pushDataList);
//            }
//        }
//        return mPushDataList;
//    }

    @SuppressLint("MissingPermission")
    public boolean CheckInternetConnection(Context context) {

        ConnectivityManager con_manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected());
    }

    public InputStream readFromAssets(String filename) throws Exception {
        InputStream caInput = mActivity.getAssets().open(filename);
        return caInput;
    }

}
