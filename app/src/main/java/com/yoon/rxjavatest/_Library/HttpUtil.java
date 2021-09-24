package com.yoon.rxjavatest._Library;

import android.os.AsyncTask;

import com.yoon.rxjavatest.Define;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import timber.log.Timber;

public class HttpUtil extends AsyncTask<String, Void, String> {

    private String mStr;
    private String mReceiveMsg;

    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public HttpUtil(String mStr) {
        this.mStr = mStr;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(mStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            if (urlConnection.getResponseCode() == urlConnection.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((mStr = reader.readLine()) != null) {
                    buffer.append(mStr);
                }
                mReceiveMsg = buffer.toString();
                reader.close();
                if (mListener != null) {
                    mListener.didRespond(Define.RECEIVED_DATA, mReceiveMsg, "0");
                }
            } else {
                Timber.d("통신 결과 : %d 에러",urlConnection.getResponseCode());
                if (mListener != null) {
                    mListener.didRespond(Define.RECEIVED_DATA, mReceiveMsg, "1");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mReceiveMsg;
    }

    public interface Listener {
        public void didRespond(String event, String data, String error);
    }
}
