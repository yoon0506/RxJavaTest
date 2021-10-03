package com.yoon.rxjavatest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.yoon.rxjavatest._Library._Popup;
import com.yoon.rxjavatest._Library._Yoon._Internet;
import com.yoon.rxjavatest.busData.BusStop;
import com.yoon.rxjavatest.busData.BusStopFromCSV;
import com.yoon.rxjavatest.databinding.ActivityLandingBinding;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Timed;
import timber.log.Timber;

public class ActivityLanding extends AppCompatActivity {
    private ActivityLanding This = this;
    private ActivityLandingBinding mBinding;
    private AnimationDrawable mAnimDrawable;

    private Disposable mDisposable;

    protected boolean mSetSetting = false;
    private ArrayList<HashMap<String, String>> mBusStationInfo = new ArrayList<HashMap<String, String>>();

    private String mBusStopInfoFileName = "bus_stop_info.csv";
    
    // 버스 타임라인에서 선택한 정류장의 데이터
    private BusStop mBusStopData;
    private ArrayList<BusStop> mBusStopList = new ArrayList<>();
    // 선택된 버스 정류소 정보
    protected String mBusRouteNum = null;
    protected String mBusNodeNum = null;
    protected String mBusRouteId = null;
    protected String mBusNodeName = null;
    protected String mBusNodeId = null;
    protected String mBusRouteTp = null;
    protected String mBusStartStopName = null;
    protected String mBusEndStopName = null;
    protected String mBusNextBusStop = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(This, R.layout.activity_landing);
        Timber.plant(new Timber.DebugTree());

        _Internet.GetInstance().getConnectivityStatus(getApplication(), new _Internet.Listener() {
            @Override
            public void result(int result) {
                if (result == _Internet.GetInstance().TYPE_CONNECTED) { //인터넷 연결 o
                    //퍼미션 상태 확인
                    if (!hasPermissions(PERMISSIONS)) {
                        //퍼미션 허가 안되어있다면 사용자에게 요청
                        requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                    } else {
                        getBusStationInfoFromCSV();
                        // 정류장 구현 후 설정할것
                        // 저장된 데이터 불러옴
//                        loadSavedData();
                    }
                } else {  // 인터넷 연결 x
                    _Popup.GetInstance().ShowConfirmPopup(This, Define.NOTIFY_TITLE, Define.NETWORK_INFORM, Define.CONFIRM_MSG, new _Popup.ConfirmPopupListener() {
                        @Override
                        public void didSelectConfirmPopup(String title, String message, String confirmMessage) {
                            if (confirmMessage.equals(Define.CONFIRM_MSG)) {
                                Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                                mSetSetting = true;
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

        // 종료
        if (getIntent().getBooleanExtra(Key.EVENT_APP_EXIT, false)) {
            this.finishAndRemoveTask();
            System.exit(0);
        }
    }

    private void getBusStationInfoFromCSV(){
        ArrayList<BusStopFromCSV> mmTempBusDataList = new ArrayList<>();
        mBusStationInfo = new ArrayList<>();
        try {
            InputStream is = getAssets().open(mBusStopInfoFileName);
            InputStreamReader isr = new InputStreamReader(is, "EUC-KR");
            BufferedReader br = new BufferedReader(isr);
            CSVReader reader = new CSVReaderBuilder(br)
                    .withCSVParser(new CSVParserBuilder().withSeparator(',').build()).build();
            List<String[]> nextLine = reader.readAll();
            
            for (String[] str : nextLine){
                String mmNodeId = str[3].trim();
                String mmNodeNum = str[3].trim();
                String mmBusStopName = str[4].trim();
//                String mmNextBusStopName = str[5];
                String mmLati = str[6].trim();
                String mmLongi = str[5].trim();
                HashMap<String, String> mmData = new HashMap<>();
                mmData.put(Key.BUS_NODE_ID, mmNodeId);
//                mmData.put(Key.BUS_NODE_NO, mmNodeNum);
                mmData.put(Key.BUS_NODE_NAME, mmBusStopName);
//                mmData.put(Key.BUS_NEXT_STOP, mmNextBusStopName);
                mmData.put(Key.BUS_LATITUDE, mmLati);
                mmData.put(Key.BUS_LONGITUDE, mmLongi);
                BusStopFromCSV mmCsvBusStopData = new BusStopFromCSV(mmData);
                mmTempBusDataList.add(mmCsvBusStopData);
            }
            AppData.GetInstance().SetCSVBusStopList(mmTempBusDataList);
            startAnim();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startAnim() {
        final AnimationDrawable mmAnimation = (AnimationDrawable) mBinding.frogImage.getBackground();
        mBinding.frogImage.post(mmAnimation::start);

        // 중복 실행 방지
        mDisposable = getObservable().throttleFirst(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    Timber.tag("checkCheck").d("액티비티 실행.");
                });
    }

    private void goToActivityMain() {
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .subscribe(temp -> {
                    Intent mmIntent = new Intent(This, ActivityMain.class);
                    startActivity(mmIntent);
                });
    }

    static final int PERMISSIONS_REQUEST_CODE = 1000;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private boolean hasPermissions(String[] permissions) {
        int result;

        //스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (String perms : permissions) {
            result = ContextCompat.checkSelfPermission(this, perms);
            if (result == PackageManager.PERMISSION_DENIED) {
                //허가 안된 퍼미션 발견
                return false;
            }
        }
        //모든 퍼미션이 허가되었음
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean notify = grantResults[0]
                            == PackageManager.PERMISSION_GRANTED;
                    boolean accessFineLocation = grantResults[1]
                            == PackageManager.PERMISSION_GRANTED;
                    boolean accessCoarseLocation = grantResults[2]
                            == PackageManager.PERMISSION_GRANTED;

                    if (!notify || !accessFineLocation || !accessCoarseLocation) {
                        _Popup.GetInstance().ShowConfirmPopup(This, Define.NOTIFY_TITLE, Define.PERMISSION_DENIED_INFORM, Define.CONFIRM_MSG, new _Popup.ConfirmPopupListener() {
                            @Override
                            public void didSelectConfirmPopup(String title, String message, String confirmMessage) {
                                if (confirmMessage.equals(Define.CONFIRM_MSG)) {
                                    finish();
                                }
                            }
                        });
                    } else {
                        getBusStationInfoFromCSV();
                        // 저장된 데이터 불러옴
//                        loadSavedData();
                    }
                }
                break;
        }
    }


    private int mCntData = 0;
    // 사용자가 저장한 버스 정류장의 개수
    private int mTotalCnt = 0;

    private void loadSavedData() {
//        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(This);
//        String savedData = mPref.getString("busDataList", "");
//        if (savedData == null || savedData.equals("") || savedData.equals("{}")) {
//            loadSavedNotiBusData();
//        } else {
//            SaveManagerBusList.GetInstance().setListener(new SaveManagerBusList.Listener() {
//                @Override
//                public void didRespond(String event, ArrayList<BusStop> data) {
//                    if (event.equals(Define.LOAD_DATA) && data != null) {
//                        AppData.GetInstance().SetBusStopList(data);
//                        mTotalCnt = data.size();
//                        if (mCntData < mTotalCnt) {
//                            mBusRouteNum = data.get(mCntData).getBusNum();
//                            mBusNodeNum = data.get(mCntData).getBusStopNumber();
//                            mBusRouteId = data.get(mCntData).getRouteId();
//                            mBusNodeName = data.get(mCntData).getBusStopName();
//                            mBusNodeId = data.get(mCntData).getNodeId();
//                            mBusRouteTp = data.get(mCntData).getRoutetp();
//                            mBusStartStopName = data.get(mCntData).getFinalStartBusStop();
//                            mBusEndStopName = data.get(mCntData).getFinalEndBusStop();
//                            mBusNextBusStop = data.get(mCntData).getNextBusStop();
//                            requestBusRouteInfo();
//                        }
//                    }
//                }
//            });
//            SaveManagerBusList.GetInstance().loadData(savedData);
//        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mSetSetting) {
            _Popup.GetInstance().ShowConfirmPopup(This, Define.NOTIFY_TITLE, Define.RESTART_INFORM, Define.CONFIRM_MSG, new _Popup.ConfirmPopupListener() {
                @Override
                public void didSelectConfirmPopup(String title, String message, String confirmMessage) {
                    mSetSetting = false;
                    restart(This);
                }
            });
        }
    }

    private void restart(Context context) {
        Intent mStartActivity = new Intent(context, ActivityLanding.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    private Observable<View> getObservable() {
        return Observable.create(temp -> goToActivityMain());
    }
}