package com.yoon.rxjavatest.Request;

import com.yoon.rxjavatest.Key;
import com.yoon.rxjavatest._Library.HttpUtil;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public class RequestRxRouteAcctoBusList {

    // json parsing
    private URL mURL = null;
    private String mUrlStr;
    private HttpUtil mHttpUtil;
    private String mmTempJsonStr3;
    private JSONObject mJsonObject;
    ArrayList<HashMap<String, String>> mDataHashMapList = new ArrayList<HashMap<String, String>>();


    private String BASE_URI = "http://openapi.tago.go.kr/openapi/service/";
    private String SUB_URI = "BusLcInfoInqireService/getRouteAcctoBusLcList?";
    private RequestRouteAcctoBusList.Listener mListener = null;

    public void setListener(RequestRouteAcctoBusList.Listener listener) {
        mListener = listener;
    }

    public void request(String routeId, RequestRouteAcctoBusList.Listener listener) {
        setListener(listener);

        String mmUrl = Key.GET_ROUTE_ARVL_BUS_LIST + Key.SERVICE_KEY + "&cityCode=" + "32010" + "&routeId=" + routeId + "&_type=json";


//        requestJsonData(mmUrl);
    }

    private RouteAcctoBus getBusAPI(){
        Retrofit mmRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mmRetrofit.create(RouteAcctoBus.class);
    }

    private RouteAcctoBus getServiceAPI(){

        HttpLoggingInterceptor mmLogInterceptor = new HttpLoggingInterceptor();
        mmLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient mmClient = new OkHttpClient.Builder()
                .addInterceptor(mmLogInterceptor)
                .build();

        Retrofit mmRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(mmClient)
                .baseUrl(BASE_URI)
                .build();

        return mmRetrofit.create(RouteAcctoBus.class);
    }

    private RequestRxRouteAcctoBusList(){

    }

    private static class Singleton{
        private static final RequestRxRouteAcctoBusList mInstance = new RequestRxRouteAcctoBusList();
    }

    public static RequestRxRouteAcctoBusList getInstance(){
        return Singleton.mInstance;
    }

    private interface RouteAcctoBus{
        @GET("serviceKey={serviceKey}&cityCode={cityCode}&routeId={routeId}&_type=json")
        Call<List<String>> getCallBus(
                @Path("serviceKey") String serviceKey, @Path("cityCode") String cityCode,
                @Path("routeId") String routeId
        );

        Observable<List<String>> getObBus(
                @Path("serviceKey") String serviceKey, @Path("cityCode") String cityCode,
                @Path("routeId") String routeId
        );
    }
}
