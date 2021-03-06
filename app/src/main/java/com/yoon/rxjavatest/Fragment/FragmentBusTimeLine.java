package com.yoon.rxjavatest.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.yoon.rxjavatest.Adapter.AdapterBusTimeLine;
import com.yoon.rxjavatest.R;
import com.yoon.rxjavatest.busData.BusTimeLine;
import com.yoon.rxjavatest.databinding.FragmentBusTimeLineBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentBusTimeLine extends Fragment {
    private FragmentBusTimeLine This = this;
    private FragmentBusTimeLineBinding mBinding;
    // request
//    private RequestRouteAcctoBusList mRequestRouteAcctoBusList;
//    // UI
//    private ListView mBinding.listViewBusTimeLine;
//
    public AdapterBusTimeLine mAdapterBusTimeLine;
    private ArrayList<BusTimeLine> mBusStopList;
    public HashMap<String, String> mBusData;
    private String mBusStopNum;
    private int mBusArrivalCnt;

    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bus_time_line, container, false);
        View mmView = mBinding.getRoot();
        return mmView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mBusStopList = AppData.GetInstance().mBusTimeLineList;
//        mBusStopNum = mBusData.get(Key.BUS_NODE_NO);
//
//        if (mBusData.get(Key.BUS_ARRIVAL_CNT) == null) {
//            mBusArrivalCnt = -1;
//        } else {
//            mBusArrivalCnt = Integer.parseInt(mBusData.get(Key.BUS_ARRIVAL_CNT));
//        }
//
//        ((TextView) getActivity().findViewById(R.id.busTimeLineTitle)).setText(mBusData.get(Key.BUS_ROUTE_NO));
//        if (mBusData.get(Key.BUS_NEXT_STOP).contains("??????") || mBusData.get(Key.BUS_NEXT_STOP).contains("??????")) {
//            ((TextView) getActivity().findViewById(R.id.busNextStopName)).setText(mBusData.get(Key.BUS_NEXT_STOP));
//        } else {
//            ((TextView) getActivity().findViewById(R.id.busNextStopName)).setText(mBusData.get(Key.BUS_NEXT_STOP) + " ??????");
//        }
//        ((TextView) getActivity().findViewById(R.id.finalStartStopName)).setText(mBusData.get(Key.BUS_FINAL_START_STOP));
//        ((TextView) getActivity().findViewById(R.id.finalEndStopName)).setText(mBusData.get(Key.BUS_FINAL_END_STOP));
//
        mAdapterBusTimeLine = new AdapterBusTimeLine(getContext(), mBusStopList, This);
//        // ?????? ?????? ???????????? ????????? ???????????? ??????.
        mAdapterBusTimeLine.mBusStopNum = mBusStopNum;
//        if (mBusArrivalCnt != -1) {
//            mAdapterBusTimeLine.mShowBusNodeOrd = (Integer.parseInt(mBusData.get(Key.BUS_NODE_ORD)) - 1) - mBusArrivalCnt;
//        } else {
//            mAdapterBusTimeLine.mShowBusNodeOrd = -1;
//        }
//        mAdapterBusTimeLine.setListener(new AdapterBusTimeLine.Listener() {
//            @Override
//            public void didRespond(Adapter adapter, String event, ArrayList<HashMap<String, String>> busStopData) {
//                if (event.equals(Define.EVENT_CLICK_ITEM) && busStopData != null) {
//                }
//            }
//        });
//
//        new Thread() {
//            public void run() {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mBinding.listViewBusTimeLine.setAdapter(mAdapterBusTimeLine);
//                        // ?????? ????????? ?????????
//                        mBinding.listViewBusTimeLine.setSelection(Integer.parseInt(mBusData.get(Key.BUS_NODE_ORD)) - 3);
//                    }
//                });
//            }
//        }.start();
//
//        // ???????????? ??????
//        ((ImageView) getActivity().findViewById(R.id.backBtnBusTimeLine)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) mListener.didRespond(This, Define.EVENT_BACK, null);
//            }
//        });
//
//        // ?????? ??????
//        ((ImageView) getActivity().findViewById(R.id.deleteBtn)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                _Popup.GetInstance().ShowBinaryPopup(getContext(), "?????? ?????? ????????? ??????????????????????", Define.CONFIRM_MSG, Define.CANCEL_MSG, new _Popup.BinaryPopupListener() {
//                    @Override
//                    public void didSelectBinaryPopup(String mainMessage, String selectMessage) {
//                        if (selectMessage.equals("??????")) {
//                            ArrayList<BusStop> mmTempBusStopList = AppData.GetInstance().mBusStopList;
//                            mmTempBusStopList.remove(AppData.GetInstance().GetSelectedNum(getContext()));
//                            AppData.GetInstance().SetBusStopList(mmTempBusStopList);
//
//                            if (mListener != null) {
//                                mListener.didRespond(This, Define.EVENT_DELETE, null);
//                            }
//                        }
//                    }
//                });
//            }
//        });
//
//        requestRouteAcctoBusList();
//    }
//
//    public void requestRouteAcctoBusList() {
//        if (!mBusData.get(Key.BUS_ROUTE_ID).equals("")) {
//            String mmRouteId = mBusData.get(Key.BUS_ROUTE_ID);
//
//            mRequestRouteAcctoBusList = new RequestRouteAcctoBusList();
//            mRequestRouteAcctoBusList.request(mmRouteId, new RequestRouteAcctoBusList.Listener() {
//                @Override
//                public void didRespond(ArrayList<HashMap<String, String>> data, String error) {
//                    if (error.equals("0")) {
//                        if (data.size() > 0) {
//                            if (mAdapterBusTimeLine != null) {
//                                mAdapterBusTimeLine.mCurrentBusNodeOrd = new int[data.size()];
//                                for (int i = 0; i < data.size(); i++) {
//                                    mAdapterBusTimeLine.mCurrentBusNodeOrd[i] = Integer.parseInt(data.get(i).get(Key.BUS_NODE_ORD));
//                                }
//                            }
//                        }
//                        updateBusTimeLineList();
//                    } else {
//                        if (mListener != null) {
//                            mListener.didRespond(This, Define.LOADING_COMPLETE, null);
//                        }
//                    }
//                }
//            });
//        }else{
//        }
    }
//
//
    public void updateBusTimeLineList() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAdapterBusTimeLine != null) {
                    mAdapterBusTimeLine.notifyDataSetChanged();
                }
            }
        });

    }

    public interface Listener {
        public void didRespond(Fragment fragment, String event, HashMap<String, String> data);
    }

}
