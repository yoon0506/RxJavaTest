package com.yoon.rxjavatest.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yoon.rxjavatest.Define;
import com.yoon.rxjavatest.R;

public class FragmentError extends Fragment {
    private FragmentError This = this;

    // UI
    private TextView mRetryBtn;

    public static boolean mIsShowError = false;

    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsShowError = true;
        mRetryBtn = getActivity().findViewById(R.id.retryBtn);
        mRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsShowError = false;
                if (mListener != null) mListener.didRespond(This, Define.RETRY, null);
            }
        });
    }

    public interface Listener {
        public void didRespond(Fragment fragment, String event, String data);
    }
}
