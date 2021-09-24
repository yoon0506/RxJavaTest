package com.yoon.rxjavatest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;

import com.yoon.rxjavatest.databinding.ActivityLandingBinding;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import timber.log.Timber;

public class ActivityLanding extends AppCompatActivity {
    private ActivityLanding This = this;
    private ActivityLandingBinding mBinding;
    private AnimationDrawable mAnimDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(This, R.layout.activity_landing);
        Timber.plant(new Timber.DebugTree());

        Observable.timer(2000L, TimeUnit.MILLISECONDS)
                .subscribe(temp -> {
                    goToActivityMain();
                    Timber.tag("checkCheck").d("액티비티 실행.");
                });
//        Handler delayHandler = new Handler();
//        delayHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                goToActivityMain();
//            }
//        }, 2000);

        final AnimationDrawable mmAnimation = (AnimationDrawable) mBinding.frogImage.getBackground();
        mBinding.frogImage.post(() -> mmAnimation.start());

        // 종료
        if (getIntent().getBooleanExtra(Key.EVENT_APP_EXIT, false)) {
            this.finishAndRemoveTask();
            System.exit(0);
        }
    }


    private void goToActivityMain() {
        Intent mmIntent = new Intent(This, ActivityMain.class);
        startActivity(mmIntent);
    }
}