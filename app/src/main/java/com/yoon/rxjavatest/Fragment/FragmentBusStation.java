package com.yoon.rxjavatest.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yoon.rxjavatest.Adapter.AdapterBusStation;
import com.yoon.rxjavatest.Api.Example;
import com.yoon.rxjavatest.Api.Item;
import com.yoon.rxjavatest.AppData;
import com.yoon.rxjavatest.Define;
import com.yoon.rxjavatest.Key;
import com.yoon.rxjavatest.R;
import com.yoon.rxjavatest.Request.RxArvlInfoInquireService;
import com.yoon.rxjavatest._Library._Popup;
import com.yoon.rxjavatest.busData.BusStation;
import com.yoon.rxjavatest.busData.BusStationDetail;
import com.yoon.rxjavatest.busData.SaveManagerBusStation;
import com.yoon.rxjavatest.databinding.FragmentBusStationBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FragmentBusStation extends Fragment {
    private FragmentBusStation This = this;
    private FragmentBusStationBinding mBinding;

    // fragment
    public FragmentMapBusStation mFragmentMapBusStation;
    // UI
    private View mFooterView;
    private ImageView mAddBusBtn;

    // adapter
    private AdapterBusStation mAdapterBusStation;

    // rxJava
    private Disposable mDisposable;
    private CompositeDisposable mCompositeDisposable;

    // bus
    private ArrayList<BusStation> mBusStationList = new ArrayList<>(AppData.GetInstance().mBusStationList);
    private static final String ROWS = "300";

    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bus_station, container, false);
        View mmView = mBinding.getRoot();
        return mmView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            mAdapterBusStation = new AdapterBusStation(getContext(), mBusStationList);
            mBinding.timeLineListView.setAdapter(mAdapterBusStation);
            mBinding.timeLineListView.setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapterBusStation.SetListener(busData -> {
                if (busData != null) {
                    _Popup.GetInstance().ShowBinaryPopup(getContext(), busData.get(Key.BUS_NODE_NAME) + " " + Define.DELETE_BUS_STATION_INFORM, Define.CONFIRM_MSG, Define.CANCEL_MSG,
                            (mainMessage, selectMessage) -> {
                                if (selectMessage.equals("확인")) {
                                    for (BusStation data : mBusStationList) {
                                        if (data.getBusNodeId().equals(busData.get(Key.BUS_NODE_ID))) {
                                            AppData.GetInstance().mBusStationDetailList.remove(data);
                                        }
                                    }

                                    // 임시.. mBusStationList와 mBusStationDetailList 통합 필요
                                    for (BusStation data : mBusStationList) {
                                        if (busData.get(Key.BUS_NODE_ID).contains(data.getBusNodeId())) {
                                            AppData.GetInstance().mBusStationList.remove(data);
                                        }
                                    }
                                }
                            });
                }
            });
            // 새로고침 버튼
            mBinding.updateBtn.setOnClickListener(v -> updateBusStationList());
            // 서버로부터 도착 정보 받아옴
            if (mBusStationList.size() > 0) {
                for (BusStation data : mBusStationList) {
                    String mmNodeId = "CCB" + data.getBusNodeId();
                    rxArvlInfoInquireService(mmNodeId);
                }

            } else {
                setAdapter();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rxArvlInfoInquireService(String mmNodeId) {
        RxArvlInfoInquireService.BusArvlInfo mmService = RxArvlInfoInquireService.getInstance().getServiceAPI();
        Observable<Example> mmObservable = mmService.getObArvlInfo(Key.SERVICE_KEY, Key.CITY_CODE, mmNodeId, ROWS, Key.TYPE_JSON);

        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(
                mmObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Example>() {
                                           @Override
                                           public void onNext(@NonNull Example strings) {
                                               Timber.tag("checkCheck").d("strings.toString() : %s", strings.toString());
                                               List<Item> busData = strings.getResponse().getBody().getItems().getItem();
                                               ArrayList<BusStationDetail> mmBusStationDetailList = new ArrayList<>();
                                               String mmBusStationDetailNodeId = "";
                                               for (Item data : busData) {
                                                   BusStationDetail detailData = new BusStationDetail(data.getArrprevstationcnt() + "", data.getArrtime() + "", data.getNodeid(), data.getNodenm(), data.getRouteid(), data.getRouteno(), data.getRoutetp());
                                                   mmBusStationDetailNodeId = detailData.getNodeId();
                                                   mmBusStationDetailList.add(detailData);
                                               }

                                               for (BusStation busStationData : mBusStationList) {
                                                   if (mmBusStationDetailNodeId.contains(busStationData.getBusNodeId())) {
                                                       busStationData.setArrivalBusInfo(mmBusStationDetailList);
                                                   }
                                               }

                                               // 버스 정류장 데이터 갱신
                                               AppData.GetInstance().SetBusStationList(mBusStationList);

//                                               Item busData = strings.getResponse().getBody().getItems().getItem();
//                                               BusStationDetail detailData = new BusStationDetail(busData.getArrprevstationcnt() + "", busData.getArrtime() + "", busData.getNodeid(), busData.getNodenm(), busData.getRouteid(), busData.getRouteno(), busData.getRoutetp());
//                                               mmBusStationDetailList.add(detailData);
                                               if (mAdapterBusStation != null) {
                                                   mAdapterBusStation.notifyDataSetChanged();
                                                   mBinding.timeLineListView.setAdapter(mAdapterBusStation);
                                               }
                                           }

                                           @Override
                                           public void onError(@NonNull Throwable e) {
                                               Timber.tag("checkCheck").d("e : %s", e);
                                           }

                                           @Override
                                           public void onComplete() {

                                           }

                                       }
                        ));
    }

    private void setAdapter() {
        try {
            mFooterView = getLayoutInflater().inflate(R.layout.bus_list_view_footer, null, false);
            mFooterView.setTag("footer");
            // 등록된 버스가 1개 이상 있으면
            if (mBusStationList.size() > 0) {
                mBinding.timeLineListView.addView(mFooterView);
                // 버스 추가 더하기 버튼
                mAddBusBtn = mFooterView.findViewById(R.id.addBusBtn);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBinding.busAddInfo.setVisibility(View.VISIBLE);
                        // 버스 추가 더하기 버튼
                        mAddBusBtn = mBinding.addBusBtn;
                    }
                });
            }
            mAddBusBtn.setOnClickListener(v -> showFragmentMapBusStation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFragmentMapBusStation() {
        try {
            FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentMapBusStation = new FragmentMapBusStation();
            mFragmentMapBusStation.setListener((FragmentMapBusStation.Listener) (fragment, event, data) -> {
                if (event.equals(Define.EVENT_BACK) && data == null) {
                    removeFragment(mFragmentMapBusStation);
                    // ㅇㅇㅇㅇㅇ추후rx로 수정
                    new Thread((Runnable) () -> getActivity().runOnUiThread((Runnable) () -> ((FrameLayout) getActivity().findViewById(R.id.mainFullFrame)).setVisibility(View.GONE)));
                } else if (event.equals(Define.EVENT_DONE) && data == null) {
                    // 버스 정류장 추가
                    saveByPreference(createSaveData());

                    // ㅇㅇㅇㅇㅇ추후rx로 수정
                    new Thread((Runnable) () -> getActivity().runOnUiThread((Runnable) () -> ((FrameLayout) getActivity().findViewById(R.id.mainFullFrame)).setVisibility(View.GONE)));
                    removeFragment(mFragmentMapBusStation);
                    if (mListener != null) mListener.didRespond(This, Define.EVENT_DONE, null);
                } else if (event.equals(Define.LOADING) && data == null) {
//                    showFragmentLoading();
//                    MAIN_EVENT = Define.FRAGMENT_MAP_BUS_STATION;
                } else if (event.equals(Define.LOADING_COMPLETE) && data == null) {
//                    if (mFragmentLoading != null) {
//                        if (mLoadingTimer != null) {
//                            mLoadingTimer.cancel();
//                            mLoadingTimer.purge();
//                            mLoadingTimer = null;
//                        }
//                        mFragmentLoading.remove();
//                        mShowLoadingImg = false;
//                        removeFragment(mFragmentLoading);
                }
            });
            ((FrameLayout) getActivity().findViewById(R.id.mainFullFrame)).setVisibility(View.VISIBLE);
            mFragmentTransaction.add(R.id.mainFullFrame, mFragmentMapBusStation);
            mFragmentTransaction.commitAllowingStateLoss();
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
//
//    private void RequestTrafficList() {
//        if (mListener != null) mListener.didRespond(This, Define.LOADING, null);
//
//        String mmUserId = AppData.GetInstance().GetUserID(getContext());
//
//        mRequestTrafficList = new RequestTrafficList();
//        mRequestTrafficList.request(mmUserId, new RequestTrafficList.Listener() {
//            @Override
//            public void didRespond(RequestRoot request, HashMap<String, String> properties, String error) {
//                if (error.equals("0")) {
//                    String mmTempBoardID = properties.get(Key.BOARD_ID);
//                    String mmTempBoardURL = properties.get(Key.BOARD_URL);
//                    AppData.GetInstance().SetBoardID(getContext(), mmTempBoardID);
//                    AppData.GetInstance().SetCurrentURL(getContext(), mmTempBoardURL);
//                    requestWebURL();
//                }
//            }
//        });
//    }

    private void updateBusStationList() {
    }

    public String createSaveData() {
        ArrayList<BusStation> mmTempBusStopList = AppData.GetInstance().mBusStationList;
        return SaveManagerBusStation.GetInstance().saveBusStationData(mmTempBusStopList);
    }

    private void saveByPreference(String data) {
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("busStationInfo", data);
        editor.commit();
        Timber.d("saved!");
    }

    public void removeFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.remove(fragment);
            mFragmentTransaction.commitAllowingStateLoss();
            fragment.onDestroy();
            fragment.onDetach();
            fragment = null;
        }
    }

    public interface Listener {
        public void didRespond(Fragment fragment, String event, HashMap<String, String> data);
    }
}
