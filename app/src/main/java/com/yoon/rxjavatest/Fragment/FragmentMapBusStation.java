package com.yoon.rxjavatest.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.widget.ZoomControlView;
import com.yoon.rxjavatest.AppData;
import com.yoon.rxjavatest.Define;
import com.yoon.rxjavatest.R;
import com.yoon.rxjavatest._Library._Popup;
import com.yoon.rxjavatest.busData.BusStation;
import com.yoon.rxjavatest.busData.BusStopFromCSV;
import com.yoon.rxjavatest.databinding.FragmentMapBusStationBinding;

import java.util.ArrayList;
import java.util.HashMap;


public class FragmentMapBusStation extends Fragment implements OnMapReadyCallback {
    private FragmentMapBusStation This = this;
    private FragmentMapBusStationBinding mBinding;

    // UI
    private NaverMap mNaverMap;
    private MapView mMapView;
    private ImageView mLocationBtn;
    private Marker[] mMarker;
    public Marker mSelectedMarker;
    private TextView[] mTextView;

    // map
    private FusedLocationProviderClient mFusedLocationClient;
    private double mLatitude = -1;
    private double mLongitude = -1;
    private LatLng mLatLng;
    private ArrayList<Marker> mMarkerArrayList;
    private boolean mIsLoadingDone = false;

    // bus
    private BusStopFromCSV mSelectedBusData;
    private String mBusNodeId;
    private String mBusStopName;
    private String mBusNextStopName;
    private String mBusNodeNo;
    private boolean mIsRequestData = false;
    private ArrayList<BusStopFromCSV> mCSVBusStopList = new ArrayList<>(AppData.GetInstance().mCSVBusStopList);

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map_bus_station, container, false);
        View mmView = mBinding.getRoot();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        return mmView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (mListener != null) {
//            mListener.didRespond(This, Define.LOADING, null);
//        }
        mMapView = getActivity().findViewById(R.id.mapView_naver_bus_station);
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
        }
        //  뒤로가기
        mBinding.backBtnMapBusStation.setOnClickListener(v -> {
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

        mBinding.searchBusStopNameBusStation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
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
            }
        });
        // 검색버튼
        ((ImageView) getActivity().findViewById(R.id.searchBusStopBtn_bus_station)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    didSearch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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


            if (mBinding.busStopInfoContainerMapBusStation != null) {
                mBinding.busStopInfoContainerMapBusStation.setVisibility(View.GONE);
            }

            if (mBinding.busStopInfoSmallContainerMapBusStation != null) {
                mBinding.busStopInfoSmallContainerMapBusStation.setVisibility(View.GONE);
            }
            if (mSelectedMarker != null) {
                mSelectedMarker.setVisible(false);
            }
        });
        UiSettings mmUiSettings = mNaverMap.getUiSettings();
        mmUiSettings.setZoomControlEnabled(false);
        mmUiSettings.setCompassEnabled(false);

        mBinding.zoomBusStation.setMap(mNaverMap);

        mBinding.locationBtnBusStation.setOnClickListener(v -> {
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
                Toast.makeText(getContext(), Define.LOCATION_INFORM, Toast.LENGTH_LONG).show();
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
        if (mCSVBusStopList.size() > 0) {
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
                String mmBusNodeNo = showData.getBusNodeNum();
                // 마커
                mMarker[mmDataCnt] = new Marker();
                mMarker[mmDataCnt].setIconPerspectiveEnabled(true);
                mMarker[mmDataCnt].setPosition(new LatLng(mmLat, mmLong));
                mMarker[mmDataCnt].setIcon(OverlayImage.fromResource(R.drawable.marker_station));
                mMarker[mmDataCnt].setMap(mNaverMap);
                mMarker[mmDataCnt].setTag(mmBusNodeNo);
                mMarker[mmDataCnt].setOnClickListener(overlay -> {
                    for (BusStopFromCSV busStopList : mCSVBusStopList) {
                        Double mmLatitude = Double.parseDouble(busStopList.getLatitude() + "");
                        Double mmLongitude = Double.parseDouble(busStopList.getLongitude() + "");
                        mLatLng = new LatLng(mmLatitude, mmLongitude);
                        String mmTempBusNodeNo = busStopList.getBusNodeNum();
                        if (overlay.getTag().toString().equals(mmTempBusNodeNo)) {
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
//                mIsSuccLoading = true;
                mListener.didRespond(This, Define.LOADING_COMPLETE, null);
            }
        }
    }

    // lati, long 기반으로 반경 계산
    final double r2d = 180.0D / 3.141592653589793D;
    final double d2r = 3.141592653589793D / 180.0D;
    final double d2km = 111189.57696D * r2d;

    private double meters(double lt1, double ln1, double lt2, double ln2) {
        final double x = lt1 * d2r;
        final double y = lt2 * d2r;
        return Math.acos(Math.sin(x) * Math.sin(y) + Math.cos(x) * Math.cos(y) * Math.cos(d2r * (ln1 - ln2))) * d2km;
    }

    private void showBusStopInfo2() {
        mBusNodeId = mSelectedBusData.getBusNodeId();
        mBusNodeNo = mSelectedBusData.getBusNodeNum();
        mBusStopName = mSelectedBusData.getBusStopName();
        mBusNextStopName = mSelectedBusData.getNextBusStopName();

        getActivity().runOnUiThread(() -> {

            mBinding.busStopInfoContainerMapBusStation.setVisibility(View.VISIBLE);
            mBinding.busStopInfoSmallContainerMapBusStation.setVisibility(View.VISIBLE);

            mBinding.busStopInfoSmallContainerMapBusStation.setOnClickListener(v -> {
                if (mBinding.busStopInfoSmallContainerMapBusStation.getVisibility() == View.VISIBLE) {
                    return;
                }
            });
            mBinding.nextBtnMapBusStation.setOnClickListener(v -> {
                if (!mIsRequestData) {
                    ArrayList<BusStation> mmBusStationList = AppData.GetInstance().mBusStationList;
                    boolean mIsExist = false;
                    if (mmBusStationList.size() > 0) {
                        for (int i = 0; i < mmBusStationList.size(); i++) {
                            if (mmBusStationList.get(i).getBusNodeId().equals(mBusNodeId)
                                    && mmBusStationList.get(i).getBusNodeNo().equals(mBusNodeNo)
                                    && mmBusStationList.get(i).getBusNodeName().equals(mBusStopName)) {
                                mIsExist = true;
                            }
                        }
                        if (!mIsExist) {
                            addBusStation();
                        } else {
                            Toast.makeText(getContext(), "이미 추가된 버스정류장 입니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        addBusStation();
                    }
                } else {
                    Toast.makeText(getContext(), "데이터를 불러오는 중입니다.", Toast.LENGTH_SHORT).show();
                }
            });

            mBinding.busStopNumberMapBusStation.setText(mSelectedBusData.getBusNodeNum());
            mBinding.busStopNameMapBusStation.setText(mSelectedBusData.getBusStopName());
            if (mSelectedBusData.getNextBusStopName() != null) {
                if (mSelectedBusData.getNextBusStopName().equals("종점")) {
                    mBinding.nextButStopNameBusStation.setText(mSelectedBusData.getNextBusStopName());
                } else {
                    mBinding.nextButStopNameBusStation.setText(mSelectedBusData.getNextBusStopName() + " 방향");
                }
            }
        });
    }

    private void addBusStation() {
        /** 나중에 추가되는 데이터를 리스트의 맨 처음에 삽입 */
        BusStation mmBusStationData = new BusStation(mBusNodeId, mBusNodeNo, mBusStopName, mBusNextStopName);
        ArrayList<BusStation> mmTempCurList = new ArrayList<>();
        mmTempCurList.add(mmBusStationData);
        mmTempCurList.addAll(AppData.GetInstance().mBusStationList);
        AppData.GetInstance().SetBusStationList(mmTempCurList);

        if (mListener != null) {
            mListener.didRespond(This, Define.EVENT_DONE, null);
        }
        /** 나중에 추가되는 데이터를 리스트의 맨 처음에 삽입 끝*/
    }

    private void didSearch() {
        if (getActivity().getCurrentFocus() != null) {
            hideSoftKeyboard(getActivity());
        }
        // request 에러방지 공백처리
        String mmReplaceTxt = mBinding.searchBusStopNameBusStation.getText().toString().trim();

        if (mmReplaceTxt.equals("")) {
            _Popup.GetInstance().ShowConfirmPopup(getContext(), Define.NOTIFY_TITLE, Define.BUS_SEARCH_INFORM, Define.CONFIRM_MSG, new _Popup.ConfirmPopupListener() {
                @Override
                public void didSelectConfirmPopup(String title, String message, String confirmMessage) {
                    if (confirmMessage.equals("확인")) {
                        mBinding.searchBusStopNameBusStation.post(() -> {
                            mBinding.searchBusStopNameBusStation.setFocusableInTouchMode(true);
                            mBinding.searchBusStopNameBusStation.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(mBinding.searchBusStopNameBusStation, 0);
                        });
                    }
                }
            });
        } else {
            try {
                // 버스정류장 이름 찾는 메소드
                searchBusStop(mmReplaceTxt);
                mBinding.searchBusStopNameBusStation.clearFocus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 버스정류장 이름으로 버스정류장 찾는 메소드
    private void searchBusStop(String searchTxt) {
        boolean mmIsExist = false;
        ArrayList<BusStopFromCSV> mmSearchResultTxtList = new ArrayList<>();

        for (int i = 0; i < mCSVBusStopList.size(); i++) {
            if (mCSVBusStopList.get(i).getBusStopName().contains(searchTxt)) {
                mmIsExist = true;
                mmSearchResultTxtList.add(mCSVBusStopList.get(i));
            }
        }

        try {
            if (mmIsExist) {
                if (mmSearchResultTxtList.size() > 0) {
                    if (mBinding.zoomBusStation != null) {
                        mBinding.zoomBusStation.setVisibility(View.GONE);
                    }
                    mBinding.searchResultContainerBusStation.setVisibility(View.VISIBLE);
                    mBinding.searchResultSmallContainerBusStation.setVisibility(View.VISIBLE);
                    mBinding.searchResultContainerBusStation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mBinding.searchResultContainerBusStation.getVisibility() == View.VISIBLE) {
                                mBinding.searchResultContainerBusStation.setVisibility(View.GONE);
                                mBinding.zoomBusStation.setVisibility(View.VISIBLE);
                            }
                            if (mBinding.searchResultSmallContainerBusStation != null && mBinding.searchResultSmallContainerBusStation.getVisibility() == View.VISIBLE) {
                                mBinding.searchResultSmallContainerBusStation.setVisibility(View.GONE);
                            }
                        }
                    });
                    if (mTextView != null) {
                        for (int i = 0; i < mTextView.length; i++) {
                            mBinding.searchResultSmallContainerBusStation.removeView(mTextView[i]);
                        }
                    }
                    mTextView = new TextView[mmSearchResultTxtList.size()];
                    for (int i = 0; i < mmSearchResultTxtList.size(); i++) {
                        mTextView[i] = new TextView(getContext());
                        LinearLayout.LayoutParams mBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        if (i == 0) {
                            mBtnParams.setMargins(30, 30, 30, 20);
                        } else if (i == mmSearchResultTxtList.size() - 1) {
                            mBtnParams.setMargins(30, 20, 30, 30);
                        } else {
                            mBtnParams.setMargins(30, 20, 30, 20);
                        }
                        mTextView[i].setText(mmSearchResultTxtList.get(i).getBusStopName() + "(" + mmSearchResultTxtList.get(i).getNextBusStopName() + " 방향)");
                        mTextView[i].setTag(mmSearchResultTxtList.get(i).getBusStopName());
                        mTextView[i].setLayoutParams(mBtnParams);
                        mTextView[i].setTextSize(16);
                        int finalI = i;
                        mTextView[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 0; i < mCSVBusStopList.size(); i++) {
                                    if (mCSVBusStopList.get(i).getBusStopName().equals(mTextView[finalI].getTag())) {
                                        if (mCSVBusStopList.get(i).getBusNodeId().equals(mmSearchResultTxtList.get(finalI).getBusNodeId())) {
                                            mSelectedBusData = mCSVBusStopList.get(i);
                                            i = mCSVBusStopList.size();
                                        }
                                    }
                                }
                                setSelectedEvent(mSelectedBusData);
                            }
                        });
                        mBinding.searchResultSmallContainerBusStation.addView(mTextView[i]);
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
        if (mBinding.searchResultContainerBusStation != null) {
            mBinding.searchResultContainerBusStation.setVisibility(View.GONE);
        }
        if (mBinding.searchResultSmallContainerBusStation != null && mBinding.searchResultSmallContainerBusStation.getVisibility() == View.VISIBLE) {
            mBinding.searchResultSmallContainerBusStation.setVisibility(View.GONE);
        }
        if (mBinding.zoomBusStation != null) {
            mBinding.zoomBusStation.setVisibility(View.VISIBLE);
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

    public interface Listener {
        public void didRespond(Fragment fragment, String event, HashMap<String, String> data);
    }

}
