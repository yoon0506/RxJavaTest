package com.yoon.rxjavatest.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.widget.ZoomControlView;
import com.yoon.rxjavatest.Api.Example;
import com.yoon.rxjavatest.Api.Item;
import com.yoon.rxjavatest.AppData;
import com.yoon.rxjavatest.Define;
import com.yoon.rxjavatest.Key;
import com.yoon.rxjavatest.R;
import com.yoon.rxjavatest.Request.RxArvlInfoInquireService;
import com.yoon.rxjavatest._Library._Popup;
import com.yoon.rxjavatest.busData.BusSelection;
import com.yoon.rxjavatest.busData.BusStation;
import com.yoon.rxjavatest.busData.BusStationDetail;
import com.yoon.rxjavatest.busData.BusStopFromCSV;
import com.yoon.rxjavatest.databinding.FragmentMapBinding;

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

public class FragmentMap extends Fragment implements OnMapReadyCallback {
    private FragmentMap This = this;
    private FragmentMapBinding mBinding;

    // Fragment
    public FragmentBusSelection mFragmentBusSelection;
    // UI
    private NaverMap mNaverMap;
    private MapView mMapView;
    private Marker[] mMarker;
    public Marker mSelectedMarker;
    private TextView[] mTextView;
    // rxjava
    private Disposable mDisposable;
    private CompositeDisposable mCompositeDisposable;

    // map
    public ZoomControlView mZoomControlView;
    private FusedLocationProviderClient mFusedLocationClient;
    private double mLatitude = -1;
    private double mLongitude = -1;
    private LatLng mLatLng;
    private ArrayList<Marker> mMarkerArrayList;
    private boolean mIsLoadingDone = false;

    // bus
    private int mDataCnt = 0;
    private int mTotalDataCnt = 0;
    private BusStopFromCSV mSelectedBusData;
    private String mBusNodeName;
    private String mBusNextStop;
    private String mBusNodeNumber;
    private String mBusNodeId;
    private String mBusRouteId;
    private ArrayList<BusStopFromCSV> mCSVBusStopList = new ArrayList<>(AppData.GetInstance().mCSVBusStopList);

    ArrayList<HashMap<String, String>> mBusNumInfo = new ArrayList<>();
    private ArrayList<BusSelection> mBusSelectionDataList = new ArrayList<>();
    private boolean mIsRequestData = false;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    public int mTempLoadingTime;

    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        View mmView = mBinding.getRoot();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        return mmView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mListener != null) {
            mListener.didRespond(This, Define.LOADING, null);
        }
        mMapView = getActivity().findViewById(R.id.mapView);
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
        }
        //  뒤로가기
        mBinding.backBtnMap.setOnClickListener((View.OnClickListener) v -> {
            try {
                if (getActivity().getCurrentFocus() != null) {
                    hideSoftKeyboard(getActivity());
                }
                clearMap();
                if (mListener != null) mListener.didRespond(This, Define.EVENT_BACK, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mBinding.searchBusStopName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (getActivity().getCurrentFocus() != null) {
                    hideSoftKeyboard(getActivity());
                }
                try {
                    didSearch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });
        // 검색버튼
        mBinding.searchBusStopBtn.setOnClickListener((View.OnClickListener) v -> {
            try {
                didSearch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        setMap();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearMap();
    }

    public void clearMap() {
        if (mIsLoadingDone) {
            try {
                if (mMapView != null) {
                    mMapView.onStop();
                    mMapView.onDestroy();
                    mMapView = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        mMapView.onStart();
        mNaverMap = naverMap;
        mNaverMap.setMinZoom(11.0);
        mNaverMap.setMaxZoom(18.0);
        mNaverMap.addOnCameraIdleListener(() -> {
            // 뿌려진 마커 초기화
            if (mMarkerArrayList != null) {
                if (mMarkerArrayList.size() > 0) {
                    for (int i = 0; i < mMarkerArrayList.size(); i++) {
                        mMarkerArrayList.get(i).setMap(null);
                    }
                }
                mLatitude = mNaverMap.getCameraPosition().target.latitude;
                mLongitude = mNaverMap.getCameraPosition().target.longitude;
                addMarkers();
            }
        });

        mNaverMap.setOnMapClickListener((pointF, latLng) -> {
            if (getActivity().getCurrentFocus() != null) {
                hideSoftKeyboard(getActivity());
            }
            if (mBinding.busStopInfoContainerMap != null) {
                mBinding.busStopInfoContainerMap.setVisibility(View.GONE);
            }
            if (mBinding.busStopInfoSmallContainerMap != null) {
                mBinding.busStopInfoSmallContainerMap.setVisibility(View.GONE);
            }
            if (mSelectedMarker != null) {
                mSelectedMarker.setVisible(false);
            }
        });
        UiSettings mmUiSettings = mNaverMap.getUiSettings();
        mmUiSettings.setZoomControlEnabled(false);
        mmUiSettings.setCompassEnabled(false);

        mBinding.zoom.setMap(mNaverMap);
        mBinding.locationBtn.setOnClickListener(v -> {

            if (isLocationEnabled()) {
                // 뿌려진 마커 초기화
                if (mMarkerArrayList != null) {
                    if (mMarkerArrayList.size() > 0) {
                        for (int i = 0; i < mMarkerArrayList.size(); i++) {
                            mMarkerArrayList.get(i).setMap(null);
                        }
                    }
                }
                getCurrentUserLocation();
            } else {
                Toast.makeText(getContext(), "'위치'를 활성화해주세요.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        getCurrentUserLocation();
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    private void setMap() {
        mMapView.getMapAsync(This);
    }


    // 현재 유저 위치 찾음.
    @SuppressLint("MissingPermission")
    private void getCurrentUserLocation() {

        OnCompleteListener<Location> mCompleteListener = task -> {
            Location location = task.getResult();
            if (location == null) {
                Toast.makeText(getContext(), "현재 위치 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                double mmLatitude = 37.881276299999996;
                double mmLongitude = 127.73007620000001;

                mLatitude = mmLatitude;
                mLongitude = mmLongitude;

                try {
                    findMyAddress(mLatitude, mLongitude);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                double mmLatitude = location.getLatitude();
                double mmLongitude = location.getLongitude();

                mLatitude = mmLatitude;
                mLongitude = mmLongitude;

                try {
                    findMyAddress(mLatitude, mLongitude);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mFusedLocationClient.getLastLocation().addOnCompleteListener(getActivity(), mCompleteListener);
    }

    private void findMyAddress(Double latitude, Double longitude) {
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(latitude, longitude));
        mNaverMap.moveCamera(cameraUpdate);
        addMarkers();
    }

    // ### csv 데이터에서 마커 뿌리는부분
    private void addMarkers() {
        int mmDataCnt = -1;
        ArrayList<BusStopFromCSV> mmShowData = new ArrayList<>();
        // csv에서 받은 데이터 뿌리는 부분
        if (AppData.GetInstance().mCSVBusStopList.size() > 0) {
            // 받은 데이터에서 일정 범위 이내의 데이터만 선별
            for (BusStopFromCSV busData : mCSVBusStopList) {
                double mmLat = busData.getLatitude();
                double mmLong = busData.getLongitude();
                double mmDistance = meters(mLatitude, mLongitude, mmLat, mmLong);

                if (mmDistance < 300) {
                    mmShowData.add(busData);
                }
            }
            if (mMarkerArrayList != null) {
                if (mMarkerArrayList.size() > 0) {
                    mMarkerArrayList.clear();
                }
            }
            mMarkerArrayList = new ArrayList<>();

            // 받은 데이터만큼 마커 추가.
            mMarker = new Marker[mmShowData.size()];
            for (BusStopFromCSV showData : mmShowData) {
                mmDataCnt++;
                double mmLat = showData.getLatitude() - 0.00004;
                double mmLong = showData.getLongitude() + 0.00004;
                String mmBusNodeId = showData.getBusNodeId();
                // 마커
                mMarker[mmDataCnt] = new Marker();
                mMarker[mmDataCnt].setIconPerspectiveEnabled(true);
                mMarker[mmDataCnt].setPosition(new LatLng(mmLat, mmLong));
                mMarker[mmDataCnt].setIcon(OverlayImage.fromResource(R.drawable.marker_station));
                mMarker[mmDataCnt].setMap(mNaverMap);
                mMarker[mmDataCnt].setTag(mmBusNodeId);
                mMarker[mmDataCnt].setOnClickListener(overlay -> {
                    for (BusStopFromCSV busStopList : mCSVBusStopList) {
                        Double mmLatitude = Double.parseDouble(busStopList.getLatitude() + "");
                        Double mmLongitude = Double.parseDouble(busStopList.getLongitude() + "");
                        mLatLng = new LatLng(mmLatitude, mmLongitude);
                        String mmTempBusNodeId = busStopList.getBusNodeId();
                        if (overlay.getTag().toString().equals(mmTempBusNodeId)) {
                            mSelectedBusData = busStopList;
                            if (mSelectedMarker != null) {
                                mSelectedMarker.setMap(null);
                            }
                            mSelectedMarker = new Marker();
                            mSelectedMarker.setZIndex(1000);
                            mSelectedMarker.setPosition(new LatLng(mmLat, mmLong));
                            mSelectedMarker.setIcon(OverlayImage.fromResource(R.drawable.marker_station_on));
                            mSelectedMarker.setMap(mNaverMap);
                            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(mmLat, mmLong));
                            mNaverMap.moveCamera(cameraUpdate);
                            showBusStopInfo2();
                        }
                    }
                    return true;
                });
                mMarkerArrayList.add(mMarker[mmDataCnt]);
            }
            mIsLoadingDone = true;
            if (mListener != null) {
                mListener.didRespond(This, Define.LOADING_COMPLETE, null);
            }
        }
    }

    final double r2d = 180.0D / 3.141592653589793D;
    final double d2r = 3.141592653589793D / 180.0D;
    final double d2km = 111189.57696D * r2d;

    private double meters(double lt1, double ln1, double lt2, double ln2) {
        final double x = lt1 * d2r;
        final double y = lt2 * d2r;
        return Math.acos(Math.sin(x) * Math.sin(y) + Math.cos(x) * Math.cos(y) * Math.cos(d2r * (ln1 - ln2))) * d2km;
    }

    // ㅇㅇㅇㅇㅇ추후에 rx로 변경 필요
    private void showBusStopInfo2() {
        mBusNodeNumber = mSelectedBusData.getBusNodeNum();
        mBusNodeName = mSelectedBusData.getBusStopName();
        mBusNodeId = mSelectedBusData.getBusNodeId();

        getActivity().runOnUiThread(() -> {
            mBinding.busStopInfoContainerMap.setVisibility(View.VISIBLE);
            mBinding.busStopInfoSmallContainerMap.setVisibility(View.VISIBLE);
            mBinding.busStopInfoSmallContainerMap.setOnClickListener(v -> {
                if (mBinding.busStopInfoSmallContainerMap.getVisibility() == View.VISIBLE) {
                    return;
                }
            });
            mBinding.nextBtnMap.setOnClickListener(v -> {
                if (!mIsRequestData) {
                    clickNextBtn();
                } else {
                    Toast.makeText(getContext(), "데이터를 불러오는 중입니다.", Toast.LENGTH_SHORT).show();
                }
            });

            mBinding.busStopNumberMap.setText(mSelectedBusData.getBusNodeNum());
            mBinding.busStopNameMap.setText(mSelectedBusData.getBusStopName());
            if (mSelectedBusData.getNextBusStopName() != null) {
                if (mSelectedBusData.getNextBusStopName().equals("종점")) {
                    mBinding.nextButStopName.setText(mSelectedBusData.getNextBusStopName());
                    mBusNextStop = mSelectedBusData.getNextBusStopName();
                } else {
                    mBinding.nextButStopName.setText(mSelectedBusData.getNextBusStopName() + " 방향");
                    mBusNextStop = mSelectedBusData.getNextBusStopName();
                }
            }
        });
    }

    // 다음 버튼 클릭했을 때 동작.
    public void clickNextBtn() {
        if (mBusSelectionDataList != null && mBusSelectionDataList.size() > 0) {
            mBusSelectionDataList.clear();
            mBusSelectionDataList = null;
            mBusSelectionDataList = new ArrayList<>();
        }
        rxArvlInfoInquireService(mBusNodeId);
    }

    private ArrayList<HashMap<String, String>> mmBusInfoMapList;


    private void rxArvlInfoInquireService(String mmNodeId) {
        RxArvlInfoInquireService.BusArvlInfo mmService = RxArvlInfoInquireService.getInstance().getServiceAPI();
        Observable<Example> mmObservable = mmService.getObArvlInfo(Key.SERVICE_KEY, Key.CITY_CODE, mmNodeId, Key.ROWS, Key.TYPE_JSON);

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
                                               for (Item data : busData) {
                                                   BusStationDetail detailData = new BusStationDetail(data.getArrprevstationcnt() + "", data.getArrtime() + "", data.getNodeid(), data.getNodenm(), data.getRouteid(), data.getRouteno(), data.getRoutetp());
                                                   mmBusStationDetailList.add(detailData);
                                               }
//                                               Item busData = strings.getResponse().getBody().getItems().getItem();
//                                               BusStationDetail detailData = new BusStationDetail(busData.getArrprevstationcnt() + "", busData.getArrtime() + "", busData.getNodeid(), busData.getNodenm(), busData.getRouteid(), busData.getRouteno(), busData.getRoutetp());
//                                               mmBusStationDetailList.add(detailData);
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

    // 1. "버스 노선 조회 서비스"의 "노선번호목록조회" 기능으로 버스 번호를 입력해 해당 버스 번호(노선 번호의) 출발점, 종점 정류소 데이터를 얻는다.
    private void requestBusInformation(String routeNo) {
//        mRequestBusInformation = new RequestBusInformation();
//        mRequestBusInformation.request(routeNo, new RequestBusInformation.Listener() {
//            @Override
//            public void didRespond(ArrayList<HashMap<String, String>> data, String error) {
//                if (error.equals("0") && data != null) {
//                    if (mBusNumInfo.size() > 0) {
//                        mBusNumInfo.clear();
//                        mBusNumInfo = null;
//                        mBusNumInfo = new ArrayList<>();
//                    }
//                    mBusNumInfo = data;
//
//                    for (int i = 0; i < mBusNumInfo.size(); i++) {
//                        if (mBusNumInfo.get(i).get(Key.BUS_ROUTE_ID).equals(mBusRouteId)) {
//                            mBusNumInfo.get(i).put(Key.BUS_NODE_NAME, mBusNodeName);
//                            mBusNumInfo.get(i).put(Key.BUS_NEXT_STOP, mBusNextStop);
//
//                            BusSelection mmBusSelectionData = new BusSelection(mBusNumInfo.get(i));
//                            mBusSelectionDataList.add(mmBusSelectionData);
//                        }
//                    }
//
//                    mDataCnt++;
//
//                    if (mDataCnt < mTotalDataCnt) {
//                        mBusRouteId = mmBusInfoMapList.get(mDataCnt).get(Key.BUS_ROUTE_ID);
//                        String mmBusRouteNo = mmBusInfoMapList.get(mDataCnt).get(Key.BUS_ROUTE_NO);
//                        mBusNextStop = mmBusInfoMapList.get(mDataCnt).get(Key.BUS_NEXT_STOP);
//                        requestBusInformation(mmBusRouteNo);
//                    } else {
//                        mDataCnt = 0;
//                        if (AppData.GetInstance().mBusSelectionDataList.size() > 0) {
//                            AppData.GetInstance().mBusSelectionDataList.clear();
//                            AppData.GetInstance().mBusSelectionDataList = null;
//                        }
//                        AppData.GetInstance().SetBusSelectionDataList(mBusSelectionDataList);
//                        showFragmentBusSelection();
//                        mIsRequestData = false;
//
//                        if (mListener != null) {
////                            mIsSuccLoading = true;
//                            mListener.didRespond(This, Define.LOADING_COMPLETE, null);
//                        }
//                    }
//                } else if (error.equals("-1") && data == null) {
//                    mDataCnt++;
//
//                    if (mDataCnt < mTotalDataCnt) {
//                        mBusRouteId = mmBusInfoMapList.get(mDataCnt).get(Key.BUS_ROUTE_ID);
//                        String mmBusRouteNo = mmBusInfoMapList.get(mDataCnt).get(Key.BUS_ROUTE_NO);
//                        mBusNextStop = mmBusInfoMapList.get(mDataCnt).get(Key.BUS_NEXT_STOP);
//                        requestBusInformation(mmBusRouteNo);
//                    } else {
//                        mDataCnt = 0;
//                        if (AppData.GetInstance().mBusSelectionDataList.size() > 0) {
//                            AppData.GetInstance().mBusSelectionDataList.clear();
//                            AppData.GetInstance().mBusSelectionDataList = null;
//                        }
//                        AppData.GetInstance().SetBusSelectionDataList(mBusSelectionDataList);
//                        showFragmentBusSelection();
//                        mIsRequestData = false;
//                        AppData.GetInstance().SetLoadingTimer(getContext(), mTempLoadingTime);
//
//                        if (mListener != null) {
////                            mIsSuccLoading = true;
//                            mListener.didRespond(This, Define.LOADING_COMPLETE, null);
//                        }
//                    }
//                }
//            }
//        });
    }

    private void showFragmentBusSelection() {
        if (mFragmentBusSelection != null) {
            removeFragment(mFragmentBusSelection);
        }

        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentBusSelection = new FragmentBusSelection();
        mFragmentBusSelection.mBusNodeNum = mBusNodeNumber;
        mFragmentBusSelection.mBusNodeName = mBusNodeName;
        mFragmentBusSelection.mBusNodeId = mBusNodeId;
        mFragmentBusSelection.setListener(new FragmentBusSelection.Listener() {
            @Override
            public void didRespond(Fragment fragment, String event, HashMap<String, String> data) {
                if (event.equals(Define.EVENT_BACK) && data == null) {
                    removeFragment(mFragmentBusSelection);

                } else if (event.equals(Define.EVENT_DONE) && data == null) {
                    removeFragment(mFragmentBusSelection);
                    if (mListener != null) mListener.didRespond(This, Define.EVENT_DONE, null);
                }
            }
        });
        ((FrameLayout) getActivity().findViewById(R.id.mainFullFrame)).setVisibility(View.VISIBLE);
        mFragmentTransaction.add(R.id.mainFullFrame, mFragmentBusSelection);
        mFragmentTransaction.commit();
    }

    private void didSearch() {
        if (getActivity().getCurrentFocus() != null) {
            hideSoftKeyboard(getActivity());
        }
        // request 에러방지 공백처리
        String mmTempSearchTxt = mBinding.searchBusStopName.getText().toString();
        String mmReplaceTxt = null;
        if (mmTempSearchTxt.contains(" "))
            mmReplaceTxt = mmTempSearchTxt.replace(" ", "");
        else
            mmReplaceTxt = mmTempSearchTxt;

        if (mmReplaceTxt.equals("")) {
            _Popup.GetInstance().ShowConfirmPopup(getContext(), Define.NOTIFY_TITLE, Define.BUS_SEARCH_INFORM, Define.CONFIRM_MSG, new _Popup.ConfirmPopupListener() {
                @Override
                public void didSelectConfirmPopup(String title, String message, String confirmMessage) {
                    if (confirmMessage.equals("확인")) {
                        mBinding.searchBusStopName.post(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.searchBusStopName.setFocusableInTouchMode(true);
                                mBinding.searchBusStopName.requestFocus();
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(mBinding.searchBusStopName, 0);
                            }
                        });
                    }
                }
            });
        } else {
            try {
                // 버스정류장 이름 찾는 메소드
                searchBusStop(mmReplaceTxt);
                mBinding.searchBusStopName.clearFocus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 버스정류장 이름으로 버스정류장 찾는 메소드
    @SuppressLint("SetTextI18n")
    private void searchBusStop(String searchTxt) {

        boolean mmIsExist = false;
        ArrayList<BusStopFromCSV> mmSearchResultTxtList = new ArrayList<>();
        for (int i = 0; i < AppData.GetInstance().mCSVBusStopList.size(); i++) {
            if (AppData.GetInstance().mCSVBusStopList.get(i).getBusStopName().contains(searchTxt)) {
                mmIsExist = true;
                mmSearchResultTxtList.add(AppData.GetInstance().mCSVBusStopList.get(i));
            }
        }
        try {
            if (mmIsExist) {
                if (mmSearchResultTxtList.size() > 0) {
                    if (mZoomControlView != null) {
                        mZoomControlView.setVisibility(View.GONE);
                    }
                    mBinding.searchResultContainer.setVisibility(View.VISIBLE);
                    mBinding.searchResultSmallContainer.setVisibility(View.VISIBLE);
                    mBinding.searchResultContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mBinding.searchResultContainer.getVisibility() == View.VISIBLE) {
                                mBinding.searchResultContainer.setVisibility(View.GONE);
                                mZoomControlView.setVisibility(View.VISIBLE);
                            }
                            if (mBinding.searchResultSmallContainer != null && mBinding.searchResultSmallContainer.getVisibility() == View.VISIBLE) {
                                mBinding.searchResultSmallContainer.setVisibility(View.GONE);
                            }
                        }
                    });
                    if (mTextView != null) {
                        for (int i = 0; i < mTextView.length; i++) {
                            mBinding.searchResultSmallContainer.removeView(mTextView[i]);
                        }
                    }
                    mTextView = new TextView[mmSearchResultTxtList.size()];

                    int mmCnt = 0;
                    for(BusStopFromCSV busStopCSVData : mmSearchResultTxtList){
                        mTextView[mmCnt] = new TextView(getContext());
                        LinearLayout.LayoutParams mBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        if (mmCnt == 0) {
                            mBtnParams.setMargins(30, 30, 30, 20);
                        } else if (mmCnt == mmSearchResultTxtList.size() - 1) {
                            mBtnParams.setMargins(30, 20, 30, 30);
                        } else {
                            mBtnParams.setMargins(30, 20, 30, 20);
                        }
                        mTextView[mmCnt].setText(busStopCSVData.getBusStopName() + "(" + busStopCSVData.getNextBusStopName() + " 방향)");
                        mTextView[mmCnt].setTag(busStopCSVData.getBusStopName());
                        mTextView[mmCnt].setLayoutParams(mBtnParams);
                        mTextView[mmCnt].setTextSize(16);
                        int finalI = mmCnt;
                        mTextView[mmCnt].setOnClickListener(v -> {
                            if (busStopCSVData.getBusStopName().equals(mTextView[finalI].getTag())) {
                                if (busStopCSVData.getBusNodeId().equals(mmSearchResultTxtList.get(finalI).getBusNodeId())) {
                                    mSelectedBusData = busStopCSVData;
                                }
                            }
                            setSelectedEvent(mSelectedBusData);
                        });
                        mBinding.searchResultSmallContainer.addView(mTextView[mmCnt]);
                        mmCnt++;
                   }
                }
            } else {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "일치하는 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSelectedEvent(BusStopFromCSV busData) {
        if (mBinding.searchResultContainer != null) {
            mBinding.searchResultContainer.setVisibility(View.GONE);
        }
        if (mBinding.searchResultSmallContainer != null && mBinding.searchResultSmallContainer.getVisibility() == View.VISIBLE) {
            mBinding.searchResultSmallContainer.setVisibility(View.GONE);
        }
        if (mZoomControlView != null) {
            mZoomControlView.setVisibility(View.VISIBLE);
        }

        Double latitude = busData.getLatitude();
        Double longitude = busData.getLongitude();

        if (mSelectedMarker != null) {
            mSelectedMarker.setMap(null);
        }
        mSelectedMarker = new Marker();
        mSelectedMarker.setZIndex(1000);
        mSelectedMarker.setPosition(new LatLng(latitude - 0.00004, longitude + 0.00004));
        mSelectedMarker.setIcon(OverlayImage.fromResource(R.drawable.marker_station_on));
        mSelectedMarker.setMap(mNaverMap);

        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(latitude, longitude));
        mNaverMap.moveCamera(cameraUpdate);
        showBusStopInfo2();
    }

    // 키보드 숨기기.
    private void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void removeFragment(Fragment fragment) {
        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.remove(fragment);
        mFragmentTransaction.commit();
        fragment.onDestroy();
        fragment.onDetach();
        fragment = null;
    }

    public interface Listener {
        public void didRespond(Fragment fragment, String event, HashMap<String, String> data);
    }
}

