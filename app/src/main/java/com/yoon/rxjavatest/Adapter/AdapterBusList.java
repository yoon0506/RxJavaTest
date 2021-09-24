package com.yoon.rxjavatest.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.yoon.rxjavatest.AppData;
import com.yoon.rxjavatest.Define;
import com.yoon.rxjavatest.Key;
import com.yoon.rxjavatest.R;
import com.yoon.rxjavatest.busData.BusStop;
import com.yoon.rxjavatest.busData.BusTimeLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterBusList extends BaseAdapter {
    private AdapterBusList This = this;

    private Context mContext;
    protected Fragment mParent;
    private List<BusStop> mBusStopList;

    // 맵 on off를 위한 index
    private ArrayList<HashMap<String, String>> mInfo;

    private Listener mListener;

    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    public AdapterBusList(Context context, List<BusStop> busDataList, Fragment parent) {
        this.mContext = context;
        this.mBusStopList = busDataList;
        this.mParent = parent;
    }

    @Override
    public int getCount() {
        return mBusStopList.size();
    }

    @Override
    public Object getItem(int i) {
        return mBusStopList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    public View getView(final int index, View v, final ViewGroup parent) {
//        if(v == null) {
            v = View.inflate(mContext, R.layout.cell_bus_stop_list, null);
//        }
        try {
            RelativeLayout mmBusStopListContainer = v.findViewById(R.id.busStopListContainer);
            final TextView mmBusStopNumber = (TextView) v.findViewById(R.id.busStopNumber);
            TextView mmBusStopNameAndNextStop = (TextView) v.findViewById(R.id.busStopNameAndNextStop);
            TextView mmBusNumber = (TextView) v.findViewById(R.id.busNumber);
//            TextView mmBusNumberTxt = (TextView) v.findViewById(R.id.busNumberTxt);
            TextView mmArrivedInfo = (TextView) v.findViewById(R.id.arrivedInfo);
            ImageView mmBusIcon = (ImageView) v.findViewById(R.id.busIcon);
            ImageView mmBusIcon2 = (ImageView) v.findViewById(R.id.busIcon2);
//            RelativeLayout mmNotiBtn = (RelativeLayout) v.findViewById(R.id.notiBtn);

                mmBusNumber.setText(mBusStopList.get(index).getBusNum());
                mmBusStopNumber.setText(mBusStopList.get(index).getBusStopNumber());

                String mmBusNextStop = null;
                if (mBusStopList.get(index).getNextBusStop().contains("기점") || mBusStopList.get(index).getNextBusStop().contains("종점")) {
                    mmBusNextStop = " (" + mBusStopList.get(index).getNextBusStop() + ")";
                } else {
                    mmBusNextStop = " (" + mBusStopList.get(index).getNextBusStop() + " 방향)";
                }

                mmBusStopNameAndNextStop.setText(mBusStopList.get(index).getBusStopName() + mmBusNextStop);

                // 도착 정보가 없으면
                if (mBusStopList.get(index).getArrivedInfo() != null) {
                    if (mBusStopList.get(index).getArrivedInfo().equals("0")
                            || mBusStopList.get(index).getArrivedInfo().equals("1")
                            || mBusStopList.get(index).getArrivedInfo().equals("2")) {
                        mmBusIcon.setVisibility(View.GONE);
                        mmArrivedInfo.setVisibility(View.INVISIBLE);
                        mmBusIcon2.setVisibility(View.VISIBLE);
                        ((TextView) v.findViewById(R.id.noBusTxt)).setVisibility(View.GONE);
                        ((RelativeLayout) v.findViewById(R.id.arrivedSoon)).setVisibility(View.VISIBLE);
                    } else if (mBusStopList.get(index).getArrivedInfo().equals("-1")) {
                        mmBusIcon.setVisibility(View.GONE);
                        mmArrivedInfo.setVisibility(View.INVISIBLE);
                        ((TextView) v.findViewById(R.id.noBusTxt)).setVisibility(View.VISIBLE);
                    } else {
                        mmArrivedInfo.setText(mBusStopList.get(index).getArrivedInfo()+"번째 전");
                    }
                } else {
                    Log.i(Define.TAG_HTTP, "arrived info is null");
//                Toast.makeText(mContext, "arrived info is null", Toast.LENGTH_SHORT).show();
                }

//                ArrayList<BusStop> mmNotiBusList = AppData.GetInstance().mNotiBusList;
//                // 알림 리소스 설정
//                if (mmNotiBusList.size() > 0) {
//                    for (int i = 0; i < mmNotiBusList.size(); i++) {
//                        if (mBusStopList.get(index).getBusNum().equals(mmNotiBusList.get(i).getBusNum())
//                                && mBusStopList.get(index).getNodeId().equals(mmNotiBusList.get(i).getNodeId())) {
//                            ((ImageView) v.findViewById(R.id.notiIcon)).setBackgroundResource(R.drawable.noti_on);
//                            mmNotiBtn.setTag("notiOn");
//                        }
//                    }
//                }

                // 일반버스이면 파란색, 마을버스이면 보라색
                if (mBusStopList.get(index).getRoutetp().equals("일반버스")) {
                    if (mBusStopList.get(index).getBusNum().contains("100")
                            || mBusStopList.get(index).getBusNum().contains("200")
                            || mBusStopList.get(index).getBusNum().contains("300")
                            || mBusStopList.get(index).getBusNum().contains("400")) {
//                        mmBusNumber.setTextColor(mContext.getResources().getColor(R.color.textColorYellow));
                        mmBusNumber.setBackgroundResource(R.drawable.rounded_corner_yellow_fill);
                        mmBusNumber.setPadding(60,20,60,20);
//                        mmBusNumberTxt.setTextColor(mContext.getResources().getColor(R.color.textColorYellow));
                        mmBusIcon.setImageResource(R.drawable.icn_bus);
                        mmBusIcon2.setImageResource(R.drawable.icn_bus);
                    } else {
                        mmBusNumber.setBackgroundResource(R.drawable.rounded_corner_blue_fill);
                        mmBusNumber.setPadding(60,20,60,20);
//                        mmBusNumber.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
//                        mmBusNumberTxt.setTextColor(mContext.getResources().getColor(R.color.colorAccent));

                        mmBusIcon.setImageResource(R.drawable.icn_bus);
                        mmBusIcon2.setImageResource(R.drawable.icn_bus);
                    }
                } else {
                    mmBusNumber.setBackgroundResource(R.drawable.rounded_corner_purple_fill);
                    mmBusNumber.setPadding(60,20,60,20);
//                    mmBusNumber.setTextColor(mContext.getResources().getColor(R.color.textColorPurple));
//                    mmBusNumberTxt.setVisibility(View.GONE);
                    mmBusIcon.setImageResource(R.drawable.icn_bus_);
                    mmBusIcon2.setImageResource(R.drawable.icn_bus_);
                }

                mmBusStopListContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, String> mmTempData = new HashMap<>();
                        mmTempData.put(Key.BUS_FINAL_START_STOP, mBusStopList.get(index).getFinalStartBusStop());
                        mmTempData.put(Key.BUS_FINAL_END_STOP, mBusStopList.get(index).getFinalEndBusStop());
                        mmTempData.put(Key.BUS_NODE_NO, mBusStopList.get(index).getBusStopNumber());
                        mmTempData.put(Key.BUS_ROUTE_NO, mBusStopList.get(index).getBusNum());
                        mmTempData.put(Key.BUS_ROUTE_ID, mBusStopList.get(index).getRouteId());
                        mmTempData.put(Key.BUS_NEXT_STOP, mBusStopList.get(index).getNextBusStop());
                        mmTempData.put(Key.BUS_ARRIVAL_CNT, mBusStopList.get(index).getArrivedInfo());
                        mmTempData.put(Key.BUS_NODE_ORD, mBusStopList.get(index).getNodeOrd());
                        mmTempData.put(Key.BUS_ROUTE_TP, mBusStopList.get(index).getRoutetp());
                        mmTempData.put(Key.BUS_NODE_ID, mBusStopList.get(index).getNodeId());

                        AppData.GetInstance().SetSelectedNum(mContext, index);
                        mListener.didRespond(This, Define.EVENT_CLICK_ITEM, mBusStopList.get(index).getBusTimeLineData(), mmTempData);
                    }
                });

//            // 알림 on/off
//            mmNotiBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mmNotiBtn.getTag() != null
//                            && mmNotiBtn.getTag().equals("notiOn")) {
//                        mmNotiBtn.setTag(null);
//                        ((ImageView) v.findViewById(R.id.notiIcon)).setBackgroundResource(R.drawable.noti_off);
//
//                        HashMap<String, String> mmTempData = new HashMap<>();
//                        mmTempData.put(Key.BUS_NODE_ID, mBusStopList.get(index).getNodeId());
//                        mmTempData.put(Key.BUS_NODE_NO, mBusStopList.get(index).getBusStopNumber());
//                        mmTempData.put(Key.BUS_NODE_NAME, mBusStopList.get(index).getBusStopName());
//                        mmTempData.put(Key.BUS_ROUTE_NO, mBusStopList.get(index).getBusNum());
//                        mmTempData.put(Key.BUS_ROUTE_ID, mBusStopList.get(index).getRouteId());
//                        mmTempData.put(Key.BUS_FINAL_START_STOP, mBusStopList.get(index).getFinalStartBusStop());
//                        mmTempData.put(Key.BUS_FINAL_END_STOP, mBusStopList.get(index).getFinalEndBusStop());
//                        mmTempData.put(Key.BUS_NEXT_STOP, mBusStopList.get(index).getNextBusStop());
//                        mmTempData.put(Key.BUS_ARRIVAL_CNT, mBusStopList.get(index).getArrivedInfo());
//                        mmTempData.put(Key.BUS_NODE_ORD, mBusStopList.get(index).getNodeOrd());
//                        mmTempData.put(Key.BUS_ROUTE_TP, mBusStopList.get(index).getRoutetp());
//
//                        mListener.didRespond(This, Define.EVENT_NOTI_OFF, mmTempData);
//                    } else {
//                        mmNotiBtn.setTag("notiOn");
//                        ((ImageView) v.findViewById(R.id.notiIcon)).setBackgroundResource(R.drawable.noti_on);
//
//                        HashMap<String, String> mmTempData = new HashMap<>();
//                        mmTempData.put(Key.BUS_NODE_ID, mBusStopList.get(index).getNodeId());
//                        mmTempData.put(Key.BUS_NODE_NO, mBusStopList.get(index).getBusStopNumber());
//                        mmTempData.put(Key.BUS_NODE_NAME, mBusStopList.get(index).getBusStopName());
//                        mmTempData.put(Key.BUS_ROUTE_NO, mBusStopList.get(index).getBusNum());
//                        mmTempData.put(Key.BUS_ROUTE_ID, mBusStopList.get(index).getRouteId());
//                        mmTempData.put(Key.BUS_FINAL_START_STOP, mBusStopList.get(index).getFinalStartBusStop());
//                        mmTempData.put(Key.BUS_FINAL_END_STOP, mBusStopList.get(index).getFinalEndBusStop());
//                        mmTempData.put(Key.BUS_NEXT_STOP, mBusStopList.get(index).getNextBusStop());
//                        mmTempData.put(Key.BUS_ARRIVAL_CNT, mBusStopList.get(index).getArrivedInfo());
//                        mmTempData.put(Key.BUS_NODE_ORD, mBusStopList.get(index).getNodeOrd());
//                        mmTempData.put(Key.BUS_ROUTE_TP, mBusStopList.get(index).getRoutetp());
//
//                        mListener.didRespond(This, Define.EVENT_NOTI_ON, mmTempData);
//                    }
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        return v;
    }

    public interface Listener {
        //        public void didRespond(Adapter adapter, String event, ArrayList<HashMap<String, String>> dataList);
        public void didRespond(Adapter adapter, String event, ArrayList<BusTimeLine> busTimeLineData, HashMap<String, String> busStopData);

        public void didRespond(Adapter adapter, String event, HashMap<String, String> busStopData);
    }

}

