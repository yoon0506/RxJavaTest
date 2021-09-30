package com.yoon.rxjavatest.Request;

import com.yoon.rxjavatest.Key;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestRxRouteAcctoBusList {

    private RequestRouteAcctoBusList.Listener mListener = null;
    public void setListener(RequestRouteAcctoBusList.Listener listener) {
        mListener = listener;
    }
    public void request(String routeId, RequestRouteAcctoBusList.Listener listener) {
        setListener(listener);

        String mmUrl = Key.GET_ROUTE_ARVL_BUS_LIST + Key.SERVICE_KEY + "&cityCode=" + "32010" + "&routeId=" + routeId + "&_type=json";


//        requestJsonData(mmUrl);
    }

    public RouteAcctoBus getBusAPI(){
        Retrofit mmRetrofit = new Retrofit.Builder()
                .baseUrl(Key.BASE_URI)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mmRetrofit.create(RouteAcctoBus.class);
    }

    public RouteAcctoBus getServiceAPI(){

        HttpLoggingInterceptor mmLogInterceptor = new HttpLoggingInterceptor();
        mmLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient mmClient = new OkHttpClient.Builder()
                .addInterceptor(mmLogInterceptor)
                .build();

        Retrofit mmRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(mmClient)
                .baseUrl(Key.BASE_URI)
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

    public interface RouteAcctoBus{
        @GET("BusLcInfoInqireService/getRouteAcctoBusLcList?")
        Call<List<String>> getCallBus(
                @Query(value = "serviceKey", encoded = true) String serviceKey,
                @Query(value = "cityCode", encoded = true) String cityCode,
                @Query(value = "routeNo", encoded = true) String routeNo,
                @Query("_type") String json
        );

        @GET("BusLcInfoInqireService/getRouteAcctoBusLcList?")
        Observable<List<String>> getObBus(
                @Query(value = "serviceKey", encoded = true) String serviceKey,
                @Query(value = "cityCode", encoded = true) String cityCode,
                @Query(value = "routeNo", encoded = true) String routeNo,
                @Query("_type") String json
        );
    }
}
