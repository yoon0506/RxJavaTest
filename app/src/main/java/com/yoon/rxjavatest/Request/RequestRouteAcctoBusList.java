package com.yoon.rxjavatest.Request;

import com.yoon.rxjavatest.Define;
import com.yoon.rxjavatest.Key;
import com.yoon.rxjavatest._Library.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import timber.log.Timber;

public class RequestRouteAcctoBusList {

    // json parsing
    private URL mURL = null;
    private String mUrlStr;
    private HttpUtil mHttpUtil;
    private String mmTempJsonStr3;
    private JSONObject mJsonObject;
    ArrayList<HashMap<String, String>> mDataHashMapList = new ArrayList<HashMap<String, String>>();

    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void request(String routeId, Listener listener) {
        setListener(listener);

        String mmUrl = Key.GET_ROUTE_ARVL_BUS_LIST + Key.SERVICE_KEY + "&cityCode=" + "32010" + "&routeId=" + routeId + "&_type=json";


        requestJsonData(mmUrl);
    }

    private void requestJsonData(String url) {
        try {
            mURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mUrlStr = mURL.toString();
        if (!mUrlStr.equals("")) {
            mHttpUtil = new HttpUtil(mUrlStr);
            mHttpUtil.execute(mUrlStr);
            mHttpUtil.setListener(new HttpUtil.Listener() {
                @Override
                public void didRespond(String event, String data, String error) {
                    if (error.equals("0")) {
                        if (event.equals(Define.RECEIVED_DATA) && data != null) {
                            jsonParser(data);
                            Timber.d("received message : %s",data);
                        }
                    } else {
                        Timber.d("receive message is null");
                        jsonParser(data);
                    }
                }
            });
            Timber.d("url : %s",mUrlStr);
        }
    }

    // error code
    // -1 : json parser 데이터가 아예 없음
    // 0 : 성공
    // 1 : 통신 성공하였으나 현재 도착정보가 없음.
    private void jsonParser(String jsonString) {
        if(jsonString != null || !jsonString.equals("")) {

            mDataHashMapList.clear();

            String[] mmTempJsonStr = jsonString.split("\"body\":");
            String[] mmTempJsonStr2 = mmTempJsonStr[1].split("\"totalCount\":");
            mmTempJsonStr3 = mmTempJsonStr2[1].replace("}}}", "");

            try {
                if (mmTempJsonStr3.equals("0")) {
                    if (mListener != null) mListener.didRespond(null, "1");
                    Timber.d("해당 버스 정보 없음.");

                } else {
                    mJsonObject = new JSONObject(mmTempJsonStr[1]).getJSONObject("items");
                    // 버스 정보 한개
                    if (mmTempJsonStr3.equals("1")) {
                        JSONObject mmJsonObject2 = mJsonObject.getJSONObject("item");
                        HashMap<String, String> mmBusHash = new HashMap<>();
                        Timber.d("json object : %s",mmJsonObject2);
                        ArrayList<String> mmTempKeyList = new ArrayList<>();
                        Iterator mmIter = mmJsonObject2.keys();
                        while (mmIter.hasNext()) {
                            String mmTempKey = mmIter.next().toString();
                            mmTempKeyList.add(mmTempKey);
                        }
                        for (int i = 0; i < mmTempKeyList.size(); i++) {
                            mmBusHash.put(mmTempKeyList.get(i), mmJsonObject2.getString(mmTempKeyList.get(i)));
                        }
                        mDataHashMapList.add(mmBusHash);
                        if (mListener != null) mListener.didRespond(mDataHashMapList, "0");
                        Timber.d("버스 정보 1개");
                        // 버스 정보 2개 이상
                    } else {
                        JSONArray mmJsonArray = mJsonObject.getJSONArray("item");

                        for (int i = 0; i < mmJsonArray.length(); i++) {
                            HashMap<String, String> mmBusHash = new HashMap<>();
                            JSONObject obj = mmJsonArray.getJSONObject(i);
                            ArrayList<String> mmTempKeyList = new ArrayList<>();

                            Iterator mmIter = obj.keys();
                            while (mmIter.hasNext()) {
                                String mmTempKey = mmIter.next().toString();
                                mmTempKeyList.add(mmTempKey);
                            }

                            for (int j = 0; j < mmTempKeyList.size(); j++) {
                                mmBusHash.put(mmTempKeyList.get(j), obj.getString(mmTempKeyList.get(j)));
                            }

                            mDataHashMapList.add(mmBusHash);
                        }
                        if (mListener != null) mListener.didRespond(mDataHashMapList, "0");
                        Timber.d("버스 정보 2개 이상");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            if (mListener != null) {
                mListener.didRespond(null, "-1");
            }
        }
    }

    public interface Listener {
        public void didRespond(ArrayList<HashMap<String, String>> data, String error);
    }
}
