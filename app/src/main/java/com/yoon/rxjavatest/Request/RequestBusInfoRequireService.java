package com.yoon.rxjavatest.Request;

import com.yoon.rxjavatest.Key;
import com.yoon.rxjavatest.Example;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestBusInfoRequireService {

    public RequestBusInfoRequireService.RouteAcctoBus getBusAPI(){
        Retrofit mmRetrofit = new Retrofit.Builder()
                .baseUrl(Key.BASE_URI)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mmRetrofit.create(RequestBusInfoRequireService.RouteAcctoBus.class);
    }

    public RequestBusInfoRequireService.RouteAcctoBus getServiceAPI(){

        HttpLoggingInterceptor mmLogInterceptor = new HttpLoggingInterceptor();
        mmLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient mmClient = new OkHttpClient.Builder()
                .addInterceptor(mmLogInterceptor)
                .build();

        Retrofit mmRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(mmClient)
                .baseUrl(Key.BASE_URI)
                .build();

        return mmRetrofit.create(RequestBusInfoRequireService.RouteAcctoBus.class);
    }

    private RequestBusInfoRequireService(){

    }

    private static class Singleton{
        private static final RequestBusInfoRequireService mInstance = new RequestBusInfoRequireService();
    }

    public static RequestBusInfoRequireService getInstance(){
        return RequestBusInfoRequireService.Singleton.mInstance;
    }

    public interface RouteAcctoBus{
        @GET("BusRouteInfoInqireService/getRouteNoList?")
        Call<Example> getCallBus(
                @Query(value = "serviceKey", encoded = true) String serviceKey,
                @Query(value = "cityCode", encoded = true) String cityCode,
                @Query(value = "routeId", encoded = true) String routeId,
                @Query("_type") String json
        );

        @GET("BusRouteInfoInqireService/getRouteNoList?")
        Observable<Example> getObBus(
                @Query(value = "serviceKey", encoded = true) String serviceKey,
                @Query(value = "cityCode", encoded = true) String cityCode,
                @Query(value = "routeId", encoded = true) String routeId,
                @Query("_type") String json
        );
    }
}
