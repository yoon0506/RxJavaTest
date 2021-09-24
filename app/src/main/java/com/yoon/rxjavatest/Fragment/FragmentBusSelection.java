package com.yoon.rxjavatest.Fragment;

import androidx.fragment.app.Fragment;


import java.util.HashMap;

//import Request.RequestBusStopInformation22;

public class FragmentBusSelection extends Fragment {
    private FragmentBusSelection This = this;
//
//    // UI
//    private ListView mListView;
//
//    // Reuqest
//    private RequestBusInformation mRequestBusInformation;
//    private RequestBusRouteInfo mRequestBusRouteInfo;
//
//    private ArrayList<BusStop> mBusStopList;
//    private ArrayList<BusTimeLine> mBusTimeLineDataList;
//    private AdapterBusSelection mAdapterBusList;
//
    // 이전 프레그먼트(맵)에서 선택한 버스 정류소 정보
    private String mBusRouteNum = null;
    public String mBusNodeNum = null;
    private String mBusRouteId = null;
    private String mBusRouteTp = null;
    public String mBusNodeName = null;
    public String mBusNodeId = null;
    private String mStartBusStopName = null;
    private String mEndBusStopName = null;
    private String mNextBusStopName = null;
//
//    private BusStop mBusStopData;
//    protected BusTimeLine mBusTimeLineData;
//    private Handler mHandler = new Handler(Looper.getMainLooper());
//    private boolean mIsRequestData = false;
//
//    // 버스 타임라인에서 선택한 정류장의 nodeOrd(순서)
//    private int mBusNodeOrd = -1;
//
//    // 버스 타임라인에서 선택한 정류장의 데이터
//    private BusSelection mSelectBusData = new BusSelection();
//
//    // json data
//    protected ArrayList<HashMap<String, String>> mBusNumInfo = new ArrayList<HashMap<String, String>>();
//    protected ArrayList<HashMap<String, String>> mBusStationInfo;

    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_bus_selection, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        mAdapterBusList = new AdapterBusSelection(getContext(), AppData.GetInstance().mBusSelectionDataList, This);
//        mAdapterBusList.setListener(new AdapterBusSelection.Listener() {
//            @Override
//            public void didRespond(Adapter adapter, String event, BusSelection busStopData) {
//                if (event.equals(Define.EVENT_CLICK_ITEM) && busStopData != null) {
//                    mBusRouteNum = busStopData.getBusNumber();
//                    mNextBusStopName = busStopData.getNextBusStop();
//                }
//            }
//        });
//
//        ((ImageView) getActivity().findViewById(R.id.backBtn_BusSelection)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) mListener.didRespond(This, Define.EVENT_BACK, null);
//            }
//        });
//
//        ((TextView) getActivity().findViewById(R.id.confirmBtn_BusSelection)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (AppData.GetInstance().GetLoadingTimer(getContext()) <= 20000) {
//                    int mmTempTimer = AppData.GetInstance().GetLoadingTimer(getContext()) + 1200;
//                    AppData.GetInstance().SetLoadingTimer(getContext(), mmTempTimer);
//                    Timber.d("timer : %s", AppData.GetInstance().GetLoadingTimer(getContext()));
//                }
//
//                if (!mIsRequestData) {
//                    if (mBusRouteNum != null) {
//                        boolean mmIsExist = false;
//                        for (int i = 0; i < AppData.GetInstance().mBusStopList.size(); i++) {
//                            if (!AppData.GetInstance().mBusStopList.get(i).getBusStopName().equals("광고")) {
//                                if (AppData.GetInstance().mBusStopList.get(i).getNodeId().equals(mBusNodeId)
//                                        && AppData.GetInstance().mBusStopList.get(i).getBusStopNumber().equals(mBusNodeNum)
//                                        && AppData.GetInstance().mBusStopList.get(i).getBusStopName().equals(mBusNodeName)
//                                        && AppData.GetInstance().mBusStopList.get(i).getBusNum().equals(mBusRouteNum)) {
//                                    mmIsExist = true;
//                                }
//                            }
//                        }
//
//                        if (mmIsExist) {
//                            mHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getContext(), "이미 등록된 정보 입니다.", Toast.LENGTH_SHORT).show();
//                                }
//                            }, 0);
//                        } else {
//                            requestBusInformation();
//                        }
//                    } else {
//                        mHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getActivity(), "버스를 선택해주세요.", Toast.LENGTH_SHORT).show();
//                            }
//                        }, 0);
//                    }
//                } else {
//                    Toast.makeText(getContext(), "데이터를 불러오는 중입니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        mListView = getActivity().findViewById(R.id.listView_BusSelection);
//        mListView.setAdapter(mAdapterBusList);
//
//    }
//
//    // 1. "버스 노선 조회 서비스"의 "노선번호목록조회" 기능으로 버스 번호를 입력해 해당 버스 번호(노선 번호의) routeId를 얻는다.
//    private void requestBusInformation() {
//        mIsRequestData = true;
//
//        mRequestBusInformation = new RequestBusInformation();
//        mRequestBusInformation.request(mBusRouteNum, new RequestBusInformation.Listener() {
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
//                    // 받아온 데이터 중에서 선택한 버스 번호와 일치하는 데이터를 선택함.
//                    for (int i = 0; i < mBusNumInfo.size(); i++) {
//                        if (mBusNumInfo.get(i).get(Key.BUS_ROUTE_NO).equals(mBusRouteNum)) {
//                            mBusRouteId = mBusNumInfo.get(i).get(Key.BUS_ROUTE_ID);
//                            mBusRouteTp = mBusNumInfo.get(i).get(Key.BUS_ROUTE_TP);
//                            BusSelection mTempData = new BusSelection(mBusNumInfo.get(i));
//                            mSelectBusData = mTempData;
//
//                            for (int j = 0; j < AppData.GetInstance().mBusSelectionDataList.size(); j++) {
//                                if (mBusNumInfo.get(i).get(Key.BUS_ROUTE_NO).equals(AppData.GetInstance().mBusSelectionDataList.get(j).getBusNumber())) {
//                                    mStartBusStopName = AppData.GetInstance().mBusSelectionDataList.get(j).getStartBusStop();
//                                    mEndBusStopName = AppData.GetInstance().mBusSelectionDataList.get(j).getEndBusStop();
//                                }
//                            }
//                            // 일치하는 데이터 찾으면 for문 빠져나옴.
//                            i = mBusNumInfo.size();
//                        }
//                    }
//                    RequestBusRouteInfo();
//                } else if (error.equals("-1")) {
//                }
//            }
//        });
//
//    }
//
//    private void RequestBusRouteInfo() {
//        String mmUserID = AppData.GetInstance().GetUserID(getContext());
//
//        mRequestBusRouteInfo = new RequestBusRouteInfo();
//        mRequestBusRouteInfo.request(mmUserID, mBusRouteId, mBusRouteNum, new RequestBusRouteInfo.Listener() {
//            @Override
//            public void didRespond(RequestRoot request, HashMap<String, Object> properties, String error) {
//                if (error.equals("0")) {
//                    String mmTempUserID = String.valueOf(properties.get(Key.USER_ID));
//                    if (mmUserID.equals(mmTempUserID)) {
//                        if (properties.get(Key.ROUTE_LIST) != null) {
//                            ArrayList<HashMap<String, String>> mmList = (ArrayList) properties.get(Key.ROUTE_LIST);
//                            mBusStationInfo = new ArrayList<HashMap<String, String>>();
//                            for (int i = 0; i < mmList.size(); i++) {
//                                mmList.get(i).put(Key.BUS_ROUTE_NO, mBusRouteNum);
//                                mmList.get(i).put(Key.BUS_ROUTE_TP, mBusRouteTp);
//                                mBusStationInfo.add(mmList.get(i));
//                                if (mmList.get(i).get(Key.BUS_NODE_ID).equals(mBusNodeId)) {
//                                    BusStop mmBusStopData = new BusStop(mmList.get(i));
//                                    mBusStopData = mmBusStopData;
//                                }
//                            }
//                        }
//                    } else {
//                        if (mListener != null) {
//                            mListener.didRespond(This, Define.LOADING_COMPLETE, null);
//                        }
//                    }
//
//                    if (mBusStopData != null) {
//                        mBusStopData.setRoutetp(mSelectBusData.getRoutetp());
//                        mBusStopData.setFinalStartBusStop(mStartBusStopName);
//                        mBusStopData.setFinalEndBusStop(mEndBusStopName);
//                        mBusStopData.setNextBusStop(mNextBusStopName);
//
//                        // 타임라인 데이터 리스트 저장
//                        mBusTimeLineData = new BusTimeLine();
//                        mBusTimeLineDataList = new ArrayList<BusTimeLine>();
//                        for (int i = 0; i < mBusStationInfo.size(); i++) {
//                            HashMap<String, String> mmMap = mBusStationInfo.get(i);
//                            BusTimeLine mBusTimeLine = new BusTimeLine(mmMap);
//                            mBusTimeLineDataList.add(mBusTimeLine);
//                        }
//
//                        mBusStopList = new ArrayList<>();
//                        if (!mBusStopData.equals("")) {
//                            BusStop mBusStop = new BusStop(mBusStopData, mBusStationInfo);
//                            mBusStopList.add(mBusStop);
//                        }
//
//                        /** 나중에 추가되는 데이터를 리스트의 맨 처음에 삽입 */
//                        ArrayList<BusStop> mmTempBusStopList = new ArrayList<>();
//                        ArrayList<BusStop> mmBusStopList = AppData.GetInstance().mBusStopList;
//                        mmTempBusStopList.addAll(mBusStopList);
//
//                        for (int i = 0; i < mmBusStopList.size(); i++) {
//                            mmTempBusStopList.add(mmBusStopList.get(i));
//                        }
//
//                        if (AppData.GetInstance().mBusStopList.size() > 0) {
//                            AppData.GetInstance().mBusStopList.clear();
//                            AppData.GetInstance().mBusStopList = null;
//                        }
//
//                        AppData.GetInstance().SetBusStopList(mmTempBusStopList);
//
//                        /** 나중에 추가되는 데이터를 리스트의 맨 처음에 삽입 끝*/
//
//                        if (mListener != null) mListener.didRespond(This, Define.EVENT_DONE, null);
//
//                    } else {
//                    }
//                }
//            }
//        });
//    }

    public interface Listener {
        public void didRespond(Fragment fragment, String event, HashMap<String, String> data);
    }

}
