package com.yoon.rxjavatest.Request;

import com.yoon.rxjavatest.Key;
import com.yoon.rxjavatest.Api.Example;


import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestBusRouteInfoInquireService {

    public RequestBusRouteInfoInquireService.RouteAcctoBus getBusAPI(){
        Retrofit mmRetrofit = new Retrofit.Builder()
                .baseUrl(Key.BASE_URI)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mmRetrofit.create(RequestBusRouteInfoInquireService.RouteAcctoBus.class);
    }

    public RequestBusRouteInfoInquireService.RouteAcctoBus getServiceAPI(){

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

        return mmRetrofit.create(RequestBusRouteInfoInquireService.RouteAcctoBus.class);
    }

    private RequestBusRouteInfoInquireService(){

    }

    private static class Singleton{
        private static final RequestBusRouteInfoInquireService mInstance = new RequestBusRouteInfoInquireService();
    }

    public static RequestBusRouteInfoInquireService getInstance(){
        return RequestBusRouteInfoInquireService.Singleton.mInstance;
    }

    public interface RouteAcctoBus{
//        @GET("BusRouteInfoInqireService/getRouteNoList?")
//        Call<Example> getCallBus(
//                @Query(value = "serviceKey", encoded = true) String serviceKey,
//                @Query(value = "cityCode", encoded = true) String cityCode,
//                @Query(value = "routeId", encoded = true) String routeId,
//                @Query("_type") String json
//        );

        @GET("BusRouteInfoInqireService/getRouteNoList?")
        Observable<Example> getObBus(
                @Query(value = "serviceKey", encoded = true) String serviceKey,
                @Query(value = "cityCode", encoded = true) String cityCode,
                @Query(value = "routeId", encoded = true) String routeId,
                @Query("_type") String json
        );
    }
}
