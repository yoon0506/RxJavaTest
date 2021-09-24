package com.yoon.rxjavatest.Fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yoon.rxjavatest.AppData;
import com.yoon.rxjavatest.Define;
import com.yoon.rxjavatest.R;

import java.util.Timer;
import java.util.TimerTask;


public class FragmentLoading extends Fragment {

    private FragmentLoading This = this;

    public static Timer mLoadingTimer = null;
    public static boolean mShowLoadingImg = false;
    public static boolean mIsSuccLoading = false;

    // UI
    protected RelativeLayout mBackground_B;
    protected ImageView mLoadingImg;

    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//
//        if(MAIN_EVENT.equals(Define.FRAGMENT_MAP_GET_BUS_DATA)){
//            mBackground_B = getActivity().findViewById(R.id.backgroundForGetBusData);
//            mLoadingImg = getActivity().findViewById(R.id.loadingImgForGetBusData);
//        }else {
//            mBackground_B = getActivity().findViewById(R.id.backgroundForMain_B);
//            mLoadingImg = getActivity().findViewById(R.id.loadingImgForMain);
//        }

        mLoadingImg.setVisibility(View.VISIBLE);
        mBackground_B.setVisibility(View.VISIBLE);
        mShowLoadingImg = true;

        final AnimationDrawable mmAnimation = (AnimationDrawable) mLoadingImg.getBackground();
        mLoadingImg.post(new Runnable() {
            @Override
            public void run() {
                mmAnimation.start();
            }
        });

        if (mLoadingTimer != null) {
            mLoadingTimer.cancel();
            mLoadingTimer.purge();
            mLoadingTimer = null;
        }
        removeAfterSeconds();
    }

    private void removeAfterSeconds() {
        mLoadingTimer = new Timer();
        mLoadingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                remove();
            }
        }, AppData.GetInstance().GetLoadingTimer(getContext()));
    }

    public void remove() {

        if (mLoadingTimer == null) {
//            MAIN_EVENT = "main";
        }
        if (!mIsSuccLoading) {
            if (mListener != null) mListener.didRespond(This, Define.LOADING_REMOVE, null);
        } else {
            mIsSuccLoading = false;
        }
        mShowLoadingImg = false;

        if (mLoadingTimer != null) {
            mLoadingTimer.cancel();
            mLoadingTimer.purge();
            mLoadingTimer = null;
        }
//        MAIN_EVENT = "main";
    }

    public interface Listener {
        public void didRespond(Fragment fragment, String event, String data);
    }
}
