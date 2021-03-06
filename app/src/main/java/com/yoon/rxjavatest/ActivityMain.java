package com.yoon.rxjavatest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.naver.maps.map.NaverMapSdk;
import com.yoon.rxjavatest.Fragment.FragmentBus;
import com.yoon.rxjavatest.Fragment.FragmentBusStation;
import com.yoon.rxjavatest.databinding.ActivityMainBinding;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ActivityMain extends AppCompatActivity {
    private ActivityMain This = this;
    private ActivityMainBinding mBinding;

    // fragment
    private Fragment mCurrentFragment;
    private FragmentBus mFragmentBus;
    private FragmentBusStation mFragmentBusStation;

    // rxJava
    private Disposable mDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // naver map client id 지정
        NaverMapSdk.getInstance(This).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("m8y7a2oirw"));

        mBinding = DataBindingUtil.setContentView(This, R.layout.activity_main);

        // 상태바 색깔 변경
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.blue));

        // btn click event 중복 방지 처리
        mDisposable = getObservable().throttleFirst(5000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                // 로그처리..?...
//                .doOnComplete(()->Timber.tag("checkCheck").d("버튼 클릭."))
                .subscribe();

        showFragmentBus();
    }

    private View.OnClickListener mBtnClickEvent = v -> {
        if (mBinding.busBtn.equals(v)) {
            if(getCurFragmentName().equals(Define.FRAGMENT_BUS)){
                return;
            }
            Timber.tag("checkCheck").d("fragmentBus.");
            showFragmentBus();
        } else if (mBinding.busStationBtn.equals(v)) {
            if(getCurFragmentName().equals(Define.FRAGMENT_BUS_STATION)){
                return;
            }
            Timber.tag("checkCheck").d("fragmentBusStation.");
            showFragmentBusStation();
        }
    };


    private void showFragmentBus() {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentBus = new FragmentBus();
        mFragmentBus.setListener((fragment, event, data) -> {
            if (event.equals(Define.EVENT_DONE) && data == null) {
                showFragmentBus();
            } else if (event.equals(Define.RELOAD_LIST) && data == null) {
                showFragmentBus();
            } else if (event.equals(Define.LOADING) && data == null) {
//                    showFragmentLoading(mFragmentBus.toString());
            } else if (event.equals(Define.LOADING_COMPLETE) && data == null) {
//                    if (mFragmentLoading != null) {
//                        if (mLoadingTimer != null) {
//                            mLoadingTimer.cancel();
//                            mLoadingTimer.purge();
//                            mLoadingTimer = null;
//                        }
//                        mFragmentLoading.remove();
//                        mShowLoadingImg = false;
//                        removeFragment(mFragmentLoading);
//                    }
            }
        });
        mFragmentTransaction.replace(R.id.mainFrame, mFragmentBus);
        mFragmentTransaction.commitAllowingStateLoss();
        mCurrentFragment = mFragmentBus;

        setBottomResource();
    }


    private void showFragmentBusStation() {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentBusStation = new FragmentBusStation();
        mFragmentBusStation.setListener((fragment, event, data) -> {
            if (event.equals(Define.LOADING) && data == null) {
//                    showFragmentLoading(mFragmentTransportation.toString());
//                    MAIN_EVENT = Define.FRAGMENT_BUS_STATION;
            } else if (event.equals(Define.FRAGMENT_BUS_STATION_DETAIL) && data == null) {
//                    showFragmentBusStationDetail();
            } else if (event.equals(Define.LOADING_COMPLETE) && data == null) {
//                    if (mFragmentLoading != null) {
//                        if (mLoadingTimer != null) {
//                            mLoadingTimer.cancel();
//                            mLoadingTimer.purge();
//                            mLoadingTimer = null;
//                        }
//                        mFragmentLoading.remove();
//                        mShowLoadingImg = false;
//                        removeFragment(mFragmentLoading);
//                    }
            }
        });
        mFragmentTransaction.replace(R.id.mainFrame, mFragmentBusStation);
        mFragmentTransaction.commitAllowingStateLoss();
        mCurrentFragment = mFragmentBusStation;

        setBottomResource();
    }


    private void setBottomResource() {
        mBinding.busBtn.setBackgroundResource(R.drawable.btn_bus_off);
        mBinding.busStationBtn.setBackgroundResource(R.drawable.btn_station_off);

        if (getCurFragmentName().equals(Define.FRAGMENT_BUS)) {
            mBinding.busBtn.setBackgroundResource(R.drawable.btn_bus_on);
        } else if (getCurFragmentName().equals(Define.FRAGMENT_BUS_STATION)) {
            mBinding.busStationBtn.setBackgroundResource(R.drawable.btn_station_on);
        }
    }


    private Observable<View> getObservable() {
        return Observable.create(e ->
        {
            mBinding.busBtn.setOnClickListener(mBtnClickEvent);
            mBinding.busStationBtn.setOnClickListener(mBtnClickEvent);
        });
    }

    private String getCurFragmentName() {
        if(mCurrentFragment == null)
            return "";

        String[] mmTempSplitStr = mCurrentFragment.toString().split("\\{");
        String mmCurFragmentStr = mmTempSplitStr[0];
        return mmCurFragmentStr;
    }

    private long mBackKeyPressedTime = 0;

    @Override
    public void onBackPressed() {
        // 두 번 눌러 종료.
        if (System.currentTimeMillis() <= mBackKeyPressedTime + 3000) {
            exitApp();
        }
        if (System.currentTimeMillis() > mBackKeyPressedTime + 3000) {
            mBackKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(This, "'뒤로가기' 버튼을 한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void exitApp() {
        Intent mmIntent = new Intent(This, ActivityLanding.class);
        mmIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mmIntent.putExtra(Key.EVENT_APP_EXIT, true);
        startActivity(mmIntent);
    }
}