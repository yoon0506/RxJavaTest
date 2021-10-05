package com.yoon.rxjavatest.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.yoon.rxjavatest.Adapter.AdapterBusList;
import com.yoon.rxjavatest.Api.Example;
import com.yoon.rxjavatest.AppData;
import com.yoon.rxjavatest.Define;
import com.yoon.rxjavatest.Key;
import com.yoon.rxjavatest.R;
import com.yoon.rxjavatest.busData.BusStop;
import com.yoon.rxjavatest.busData.BusTimeLine;
import com.yoon.rxjavatest.busData.SaveManagerBusList;
import com.yoon.rxjavatest.databinding.FragmentBusBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class FragmentBus extends Fragment {
    private FragmentBus This = this;
    private FragmentBusBinding mBinding;

    // Fragment
    public FragmentMap mFragmentMap;
    public FragmentBusTimeLine mFragmentBusTimeLine;
    private FragmentLoading mFragmentLoading;
    public FragmentError mFragmentError;

    private View mFooterView;
    private AdapterBusList mAdapterBusList;
    private ArrayList<BusStop> mBusStopList;
    private String mNodeId;
    private String mNodeOrd;
    private String mRouteId;
    // 서버로부터 데이터를 받아오기 전에 update버튼 눌리는것 방지
    private boolean mGetDataDone = false;
    // 데이터를 몇 번 불러오는지 확인 (데이터를 새로고침할 때 더하기가 여러 번 추가되는것 방지)
    private int mGetDataCnt = 0;
    // 데이터의 총 개수
    private int mDataTotalCnt = -1;
    // 현재 세고있는 데이터의 순서
    private int mDataCnt = 0;
    private int mFirstVisiblePosition;
    private HashMap<String, String> mSelectedData;

    // UI
    private ImageView mAddBusBtn;

    // rxJava
    private Disposable mDisposable;
    private CompositeDisposable mCompositeDisposable;

    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bus, container, false);
        View mmView = mBinding.getRoot();
        return mmView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {

            mDisposable = getObservable().throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
            // 새로고침 버튼
            mBinding.updateBtn.setOnClickListener(v -> updateBusList());
            mFirstVisiblePosition = 0;

            //test
            requestBusInfoRequireService();

            // 서버로부터 도착 정보 받아옴
            mDataTotalCnt = AppData.GetInstance().mBusStopList.size();

            if (mDataCnt < mDataTotalCnt) {
                ArrayList<BusStop> mmTempData = AppData.GetInstance().mBusStopList;

                mNodeId = mmTempData.get(mDataCnt).getNodeId();
                mRouteId = mmTempData.get(mDataCnt).getRouteId();
                mNodeOrd = mmTempData.get(mDataCnt).getNodeOrd();
                requestRouteAcctoBusList();
                if (mListener != null) {
                    mListener.didRespond(This, Define.LOADING, null);
                }

            } else {  // 데이터 없을경우
                mDataCnt = 0;
                setAdapter();
                mGetDataDone = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFragmentMap() {
        if (mFragmentMap != null) {
            removeFragment(mFragmentMap);
        }

        try {
            FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentMap = new FragmentMap();
            mFragmentMap.setListener((fragment, event, data) -> {
                if (event.equals(Define.EVENT_BACK) && data == null) {
                    removeFragment(mFragmentMap);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((FrameLayout) getActivity().findViewById(R.id.mainFullFrame)).setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                } else if (event.equals(Define.EVENT_DONE) && data == null) {
                    // 버스 추가
                    saveByPreference(createSaveData());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((FrameLayout) getActivity().findViewById(R.id.mainFullFrame)).setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                    removeFragment(mFragmentMap);
                    if (mListener != null) mListener.didRespond(This, Define.EVENT_DONE, null);
                } else if (event.equals(Define.LOADING) && data == null) {
//                        showFragmentLoading();
//                        MAIN_EVENT = Define.FRAGMENT_MAP;
                } else if (event.equals(Define.LOADING + "getBusData") && data == null) {
//                        MAIN_EVENT = Define.FRAGMENT_MAP_GET_BUS_DATA;
//                        showFragmentLoading();
                } else if (event.equals(Define.LOADING_COMPLETE) && data == null) {
                    if (mFragmentLoading != null) {
//                            if (mLoadingTimer != null) {
//                                mLoadingTimer.cancel();
//                                mLoadingTimer.purge();
//                                mLoadingTimer = null;
//                            }
                        mFragmentLoading.remove();
//                            mShowLoadingImg = false;
                        removeFragment(mFragmentLoading);
                    }
                }
            });
            ((FrameLayout) getActivity().findViewById(R.id.mainFullFrame)).setVisibility(View.VISIBLE);
            mFragmentTransaction.add(R.id.mainFullFrame, mFragmentMap);
            mFragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFragmentBusTimeLine(HashMap<String, String> busStopData) {

        if (mFragmentBusTimeLine != null) {
            removeFragment(mFragmentBusTimeLine);
        }

        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentBusTimeLine = new FragmentBusTimeLine();
        mFragmentBusTimeLine.mBusData = busStopData;
        mFragmentBusTimeLine.setListener(new FragmentBusTimeLine.Listener() {
            @Override
            public void didRespond(Fragment fragment, String event, HashMap data) {
                if (event.equals(Define.EVENT_BACK) && data == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((FrameLayout) getActivity().findViewById(R.id.mainFullFrame)).setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                    removeFragment(mFragmentBusTimeLine);
                } else if (event.equals(Define.EVENT_DELETE) && data == null) {
                    saveByPreference(createSaveData());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((FrameLayout) getActivity().findViewById(R.id.mainFullFrame)).setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                    removeFragment(mFragmentBusTimeLine);

                    if (mListener != null) mListener.didRespond(This, Define.RELOAD_LIST, null);

                }
            }
        });
        ((FrameLayout) getActivity().findViewById(R.id.mainFullFrame)).setVisibility(View.VISIBLE);
        mFragmentTransaction.add(R.id.mainFullFrame, mFragmentBusTimeLine);
        mFragmentTransaction.commitAllowingStateLoss();
    }

    private void requestRouteAcctoBusList() {
//        mRequestRouteAcctoBusList = new RequestRouteAcctoBusList();
//        mRequestRouteAcctoBusList.request(mRouteId, new RequestRouteAcctoBusList.Listener() {
//            @Override
//            public void didRespond(ArrayList<HashMap<String, String>> data, String error) {
//                if (error.equals("0")) {
//                    if (data.size() > 0) {
//                        if (mNodeId != null) {
//                            ArrayList<Integer> mmTempOrdList = new ArrayList<>();
//                            int mmNodeOrd = Integer.parseInt(mNodeOrd);
//                            for (int i = 0; i < data.size(); i++) {
//                                int mmBusNodeOrd = Integer.parseInt(data.get(i).get(Key.BUS_NODE_ORD));
//                                if (mmNodeOrd - mmBusNodeOrd >= 0) {
//                                    mmTempOrdList.add(mmNodeOrd - mmBusNodeOrd);
//                                }
//                            }
//
//                            // 제일 가까운 정류장 찾기
//                            int mmMinOrd = 1000;
//                            if (mmTempOrdList.size() > 0) {
//                                for (int i = 0; i < mmTempOrdList.size(); i++) {
//                                    int mmTempOrd = mmTempOrdList.get(i);
//                                    if (mmTempOrd <= mmMinOrd) {
//                                        mmMinOrd = mmTempOrd;
//                                    }
//                                }
//                                // 정류장 알림
////                                if (mmMinOrd >= 0) {
////
////                                    AppData.GetInstance().mBusStopList.get(mDataCnt).setArrivedInfo(mmMinOrd + "");
////
////                                    if (mmMinOrd == Define.NOTI_BUS_ARRIVAL_CNT) {
////                                        if (AppData.GetInstance().mNotiBusList.size() > 0) {
////                                            String mmTempNodeId = AppData.GetInstance().mBusStopList.get(mDataCnt).getNodeId();
////                                            String mmTempRouteNo = AppData.GetInstance().mBusStopList.get(mDataCnt).getBusNum();
////                                            for (int i = 0; i < AppData.GetInstance().mNotiBusList.size(); i++) {
////                                                if (AppData.GetInstance().mNotiBusList.get(i).getNodeId().equals(mmTempNodeId)
////                                                        && AppData.GetInstance().mNotiBusList.get(i).getBusNum().equals(mmTempRouteNo)) {
////                                                    Timber.d("main : 푸시 리퀘스트 던질 예정");
////                                                    HashMap<String, String> mmBusData = new HashMap<>();
////                                                    mmBusData.put(Key.NOTI_NODE_ID, mmTempNodeId);
////                                                    mmBusData.put(Key.NOTI_ROUTE_NO, mmTempRouteNo);
////                                                    mmBusData.put(Key.NOTI_ARRIVAL_COUNT, Define.NOTI_BUS_ARRIVAL_CNT + "");
////
////                                                    // 푸시알림 받기 설정을 했을 때, 버스 5번째 전이면 request던짐.
////                                                    if (AppData.GetInstance().GetPushState(getContext())) {
////                                                        requestArrivalPushInfo(mmBusData);
////                                                    }
////                                                }
////                                            }
////
////                                        }
////                                    }
////                                }
//                            } else {
//                                AppData.GetInstance().mBusStopList.get(mDataCnt).setArrivedInfo("-1");
//                            }
//                        }
//                    }
//                } else if (error.equals("1")) {
////                    mHandler.postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
////                            // 사용하고자 하는 코드
////                            Timber.d("정상 // 현재 도착하는 데이터 없음.");
////                        }
////                    }, 0);
//                    AppData.GetInstance().mBusStopList.get(mDataCnt).setArrivedInfo("-1");
//
//                } else if (error.equals("-1")) {
////                    mHandler.postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
////                            // 사용하고자 하는 코드
////                            Timber.d("통신 오류.");
////                        }
////                    }, 0);
//                    AppData.GetInstance().mBusStopList.get(mDataCnt).setArrivedInfo("-1");
//                }
//
//                mDataCnt++;
//                if (mDataTotalCnt > mDataCnt) {
//                    ArrayList<BusStop> mmTempData = AppData.GetInstance().mBusStopList;
//                    mNodeId = mmTempData.get(mDataCnt).getNodeId();
//                    mRouteId = mmTempData.get(mDataCnt).getRouteId();
//                    mNodeOrd = mmTempData.get(mDataCnt).getNodeOrd();
//                    requestRouteAcctoBusList();
//                } else {
//                    mDataCnt = 0;
//                    setAdapter();
//                    mGetDataDone = true;
//                }
//            }
//        });
    }

    private void requestBusInfoRequireService() {
        startRx();
    }

    private void startRx() {
//        mRouteId = "CCB250020001";
//        RequestBusRouteInfoInquireService.RouteAcctoBus mmService = RequestBusRouteInfoInquireService.getInstance().getServiceAPI();
////        Observable<Example> mmObservable = mmService.getObBus(Key.SERVICE_KEY, Key.CITY_CODE, mRouteId, Key.TYPE_JSON);
//        Observable<Example> mmObservable = mmService.getObBus(Key.SERVICE_KEY, Key.CITY_CODE);
//
//        mCompositeDisposable = new CompositeDisposable();
//        mCompositeDisposable.add(
//                mmObservable.subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(new DisposableObserver<Example>() {
//                                           @Override
//                                           public void onNext(@NonNull Example strings) {
////                                               for (Example data : strings) {
////                                                   Timber.tag("checkCheck").d("data : %s", data.toString());
////                                               }
//                                               Timber.tag("checkCheck").d("strings.toString() : %s", strings.toString());
////                                               List<Item> busData = strings.getResponse().getBody().getItems().getItem();
////                                               Item busData = strings.getResponse().getBody().getItems().getItem();
//
//                                           }
//
//                                           @Override
//                                           public void onError(@NonNull Throwable e) {
//                                               Timber.tag("checkCheck").d("e : %s", e);
//                                           }
//
//                                           @Override
//                                           public void onComplete() {
//
//                                           }
//
//                                       }
//                        ));
    }


    //
//    private void requestArrivalPushInfo(HashMap<String, String> busData) {
//        String mmUserId = AppData.GetInstance().GetUserID(getContext());
//        String mmNotiId = "arrival";
//        String mmNodeId = busData.get(Key.NOTI_NODE_ID);
//        String mmRouteNo = busData.get(Key.NOTI_ROUTE_NO);
//        String mmArrivalCnt = busData.get(Key.NOTI_ARRIVAL_COUNT);
//
//        mRequestArrivalPushInfo = new RequestArrivalPushInfo();
//        mRequestArrivalPushInfo.request(mmUserId, mmNotiId, mmNodeId, mmRouteNo, mmArrivalCnt, new RequestArrivalPushInfo.Listener() {
//            @Override
//            public void didRespond(RequestRoot request, HashMap<String, String> properties, String error) {
//                if (error.equals("0")) {
//                    Timber.d("푸시 리퀘스트 성공.");
//                } else {
//                    Timber.d("푸시 리퀘스트 실패.");
//                }
//            }
//        });
//
//    }

    private void saveByPreference(String data) {
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("busDataList", data);
        editor.commit();
        Timber.d("saved.");
    }

    public String createSaveData() {
        ArrayList<BusStop> mmTempBusStopList = AppData.GetInstance().mBusStopList;
//        AppData.GetInstance().SetBusStopList(mmTempBusStopList);
        return SaveManagerBusList.GetInstance().saveData(mmTempBusStopList);
    }

    private void setAdapter() {
        Timber.tag("checkCheck").d("어뎁터");
        if (!mGetDataDone) {

            mAdapterBusList = new AdapterBusList(getContext(), AppData.GetInstance().mBusStopList, This);
            mAdapterBusList.notifyDataSetChanged();
            mAdapterBusList.setListener(new AdapterBusList.Listener() {
                @Override
                public void didRespond(Adapter adapter, String event, ArrayList<BusTimeLine> busTimeLineData, HashMap<String, String> busStopData) {
                    if (event.equals(Define.EVENT_CLICK_ITEM) && busTimeLineData != null && busStopData != null) {
                        AppData.GetInstance().SetBusTimeLineList(busTimeLineData);
                        mSelectedData = new HashMap<>();
                        mSelectedData = busStopData;
                        showFragmentBusTimeLine(mSelectedData);
                    }
                }

                @Override
                public void didRespond(Adapter adapter, String event, HashMap<String, String> busStopData) {
                    // 버스 도착 알림
                    if (event.equals(Define.EVENT_NOTI_ON) && busStopData != null) {
//                        BusStop mmNotiBusStop = new BusStop(busStopData);
//                        if (AppData.GetInstance().mNotiBusList.size() > 0) {
//                            Boolean mmIsExist = false;
//                            for (int i = 0; i < AppData.GetInstance().mNotiBusList.size(); i++) {
//                                if (AppData.GetInstance().mNotiBusList.get(i).getNodeId().equals(mmNotiBusStop.getNodeId())
//                                        && AppData.GetInstance().mNotiBusList.get(i).getBusNum().equals(mmNotiBusStop.getBusNum())) {
//                                    mmIsExist = true;
//                                }
//                            }
//                            if (!mmIsExist) {
//                                AppData.GetInstance().mNotiBusList.add(mmNotiBusStop);
//                                Toast.makeText(getContext(), mmNotiBusStop.getBusStopName() + "정류장으로 오는 " + mmNotiBusStop.getBusNum() + "번 알림을 설정하였습니다.", Toast.LENGTH_SHORT).show();
//                                saveByPreferenceNotiBusList(createSaveNotiBusData());
//                            } else {
//                                Toast.makeText(getContext(), "이미 알림설정이 되어있습니다.", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            AppData.GetInstance().mNotiBusList.add(mmNotiBusStop);
//                            Toast.makeText(getContext(), mmNotiBusStop.getBusStopName() + "정류장으로 오는 " + mmNotiBusStop.getBusNum() + "번 알림을 설정하였습니다.", Toast.LENGTH_SHORT).show();
//                            saveByPreferenceNotiBusList(createSaveNotiBusData());
//                        }
                    } else if (event.equals(Define.EVENT_NOTI_OFF) && busStopData != null) {
//                        BusStop mmNotiBusStop = new BusStop(busStopData);
//                        if (AppData.GetInstance().mNotiBusList.size() > 0) {
//                            for (int i = 0; i < AppData.GetInstance().mNotiBusList.size(); i++) {
//                                if (AppData.GetInstance().mNotiBusList.get(i).getNodeId().equals(mmNotiBusStop.getNodeId())
//                                        && AppData.GetInstance().mNotiBusList.get(i).getBusNum().equals(mmNotiBusStop.getBusNum())) {
//                                    AppData.GetInstance().mNotiBusList.remove(i);
//
//                                    Toast.makeText(getContext(), mmNotiBusStop.getBusStopName() + "정류장으로 오는 " + mmNotiBusStop.getBusNum() + "번 알림을 해제하였습니다.", Toast.LENGTH_SHORT).show();
//                                    saveByPreferenceNotiBusList(createSaveNotiBusData());
//                                }
//                            }
//                        } else {
//                            AppData.GetInstance().mNotiBusList.clear();
//                            saveByPreferenceNotiBusList(createSaveNotiBusData());
//                        }
                    }
                }
            });

            try {
                mFooterView = getLayoutInflater().inflate(R.layout.bus_list_view_footer, null, false);
                mFooterView.setTag("footer");

                // 등록된 버스가 1개 이상 있으면
                if (AppData.GetInstance().mBusStopList.size() > 0) {

                    if (mGetDataCnt < 1) {
                        mBinding.timeLineListView.addFooterView(mFooterView);
                        mGetDataCnt++;
                    }
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
                // 버스 추가 더하기 버튼
                Timber.tag("checkCheck").d("안녕");
                mAddBusBtn.setOnClickListener(v -> showFragmentMap());

//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Timber.d("데이터 갱신.");
//                        mAdapterBusList.notifyDataSetChanged();
//                        mBinding.timeLineListView.setAdapter(mAdapterBusList);
//                        mBinding.timeLineListView.setSelectionFromTop(mFirstVisiblePosition, 0);
//                    }
//                });

                mBinding.timeLineListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (scrollState == SCROLL_STATE_IDLE) {
                            mFirstVisiblePosition = view.getFirstVisiblePosition();
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (mBinding.timeLineListView.getChildCount() > 0) {
                            mFirstVisiblePosition = view.getFirstVisiblePosition();
                        }
                    }
                });

//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mListener != null) {
//                            mIsSuccLoading = true;
//                            mListener.didRespond(This, Define.LOADING_COMPLETE, null);
//                        }
//                    }
//                }, 500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //
//    private void showFragmentLoading() {
//        if (mFragmentLoading != null) {
//            removeFragment(mFragmentLoading);
//        }
//
//        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
//        mFragmentLoading = new FragmentLoading();
//        mFragmentLoading.setListener(new FragmentLoading.Listener() {
//            @Override
//            public void didRespond(Fragment fragment, String event, String data) {
//                if (event.equals(Define.LOADING_REMOVE)) {
//                    if (mFragmentLoading != null) {
//                        if (!mIsSuccLoading) {
//                            if (mFragmentMap != null) {
//                                AppData.GetInstance().SetLoadingTimer(getContext(), mFragmentMap.mTempLoadingTime);
//                            }
//                            showFragmentError();
//                        } else {
//                            mIsSuccLoading = false;
//                        }
//                        mShowLoadingImg = false;
//                        removeFragment(mFragmentLoading);
//                    }
//                }
//            }
//        });
//        mFragmentTransaction.add(R.id.mainFullFrame, mFragmentLoading);
//        mFragmentTransaction.commit();
//    }
//
//    private void showFragmentError() {
//        if (mFragmentError != null) {
//            removeFragment(mFragmentError);
//        }
//        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
//        mFragmentError = new FragmentError();
//        mFragmentError.setListener(new FragmentError.Listener() {
//            @Override
//            public void didRespond(Fragment fragment, String event, String data) {
//                if (event.equals(Define.RETRY)) {
//                    if (mFragmentError != null) {
//                        // 맵 재요청
//                        removeFragment(mFragmentError);
//                        if (MAIN_EVENT.equals(Define.FRAGMENT_MAP_GET_BUS_DATA)) {
//                            if (mFragmentMap != null) {
//                                mFragmentMap.clickNextBtn();
//                            }
//                        } else {
//                            showFragmentMap();
//                        }
//                    }
//                }
//            }
//        });
//        mFragmentTransaction.add(R.id.mainFullFrame, mFragmentError);
//        mFragmentTransaction.commit();
//    }
//
//    //  주기적으로 업데이트
//    private Handler mHandler = new Handler(Looper.getMainLooper());
//
//    //    public void updateBusList(int selectNum) {
    public void updateBusList() {
//        if (mGetDataDone) {
//            mGetDataDone = false;
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (getContext() != null) {
//                        Toast.makeText(getContext(), "최신 정보를 불러오는 중입니다.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }, 0);
//            mDataCnt = 0;
//
//            if (mDataCnt < mDataTotalCnt) {
//                ArrayList<BusStop> mmTempData = AppData.GetInstance().mBusStopList;
//                if (mmTempData.get(Define.INSERT_AD_CNT).getBusStopName().equals("광고")) {
//                    if (mDataCnt != Define.INSERT_AD_CNT) {
//                        mNodeId = mmTempData.get(mDataCnt).getNodeId();
//                        mRouteId = mmTempData.get(mDataCnt).getRouteId();
//                        mNodeOrd = mmTempData.get(mDataCnt).getNodeOrd();
//                    } else {
//                        if (mDataCnt + 1 < AppData.GetInstance().mBusStopList.size()) {
//                            mDataCnt++;
//                            mNodeId = mmTempData.get(mDataCnt).getNodeId();
//                            mRouteId = mmTempData.get(mDataCnt).getRouteId();
//                            mNodeOrd = mmTempData.get(mDataCnt).getNodeOrd();
//                        }
//                    }
//                } else {
//                    mNodeId = mmTempData.get(mDataCnt).getNodeId();
//                    mRouteId = mmTempData.get(mDataCnt).getRouteId();
//                    mNodeOrd = mmTempData.get(mDataCnt).getNodeOrd();
//                }
//                requestRouteAcctoBusList();
//            } else {
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 사용하고자 하는 코드
//                        Toast.makeText(getContext(), "버스를 추가해주세요.", Toast.LENGTH_SHORT).show();
//                    }
//                }, 0);
//            }
//        } else {
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getContext(), "최신 정보를 불러오는 중입니다.", Toast.LENGTH_SHORT).show();
//                        }
//                    }, 0);
//                }
//            }, 0);
//        }
    }

    private Observable<View> getObservable() {
        return Observable.create(e -> mBinding.addBusBtn.setOnClickListener(e::onNext));
    }

    public void removeFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.remove(fragment);
            mFragmentTransaction.commit();
            fragment.onDestroy();
            fragment.onDetach();
            fragment = null;
        }
    }


    public interface Listener {
        public void didRespond(Fragment fragment, String event, HashMap<String, String> data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposable.dispose();
    }
}
