package com.yoon.rxjavatest.Request;

import com.yoon.rxjavatest.Api.Example;
import com.yoon.rxjavatest.Key;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RxArvlInfoInquireService {

    public RxArvlInfoInquireService.BusArvlInfo getBusAPI(){
        Retrofit mmRetrofit = new Retrofit.Builder()
                .baseUrl(Key.BASE_URI)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mmRetrofit.create(RxArvlInfoInquireService.BusArvlInfo.class);
    }

    public RxArvlInfoInquireService.BusArvlInfo getServiceAPI(){

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

        return mmRetrofit.create(RxArvlInfoInquireService.BusArvlInfo.class);
    }

    private RxArvlInfoInquireService(){

    }

    private static class Singleton{
        private static final RxArvlInfoInquireService mInstance = new RxArvlInfoInquireService();
    }

    public static RxArvlInfoInquireService getInstance(){
        return RxArvlInfoInquireService.Singleton.mInstance;
    }

    public interface BusArvlInfo{
        @GET("/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList?")
        Observable<Example> getObArvlInfo(
                @Query(value = "serviceKey", encoded = true) String serviceKey,
                @Query(value = "cityCode", encoded = true) String cityCode,
                @Query(value = "nodeid", encoded = true) String nodeId,
                @Query(value = "numOfRows", encoded = true) String nomOfRows,
                @Query("_type") String json
        );
    }
}
