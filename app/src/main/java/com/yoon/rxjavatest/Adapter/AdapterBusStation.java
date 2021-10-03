package com.yoon.rxjavatest.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.http.SslError;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;;

import com.yoon.rxjavatest.AppData;
import com.yoon.rxjavatest.Define;
import com.yoon.rxjavatest.Key;
import com.yoon.rxjavatest.R;
import com.yoon.rxjavatest._Library._Popup;
import com.yoon.rxjavatest.busData.BusStationDetail;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import timber.log.Timber;

public class AdapterBusStation extends BaseAdapter {
    private AdapterBusStation This = this;

    protected Context mContext;
    protected Fragment mParent;
    protected RelativeLayout mBusArrivalInfoCell;

    // 도착예정인 버스 정보 노출 수.
    private static final int SHOW_BUS_COUNT = 5;
    // 맵 on off를 위한 index
    private ArrayList<HashMap<String, String>> mInfo;

    private ArrayList<BusStationDetail> mBusDetailList = new ArrayList<>(AppData.GetInstance().mBusStationDetailList);
    private Listener mListener;

    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    public AdapterBusStation(Context context, Fragment parent) {
        this.mContext = context;
        this.mParent = parent;
    }

    @Override
    public int getCount() {
        return mBusDetailList.size();
    }

    @Override
    public Object getItem(int i) {
        return mBusDetailList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ResourceAsColor", "ResourceType", "SetTextI18n", "CutPasteId"})
    @Override
    public View getView(final int index, View v, final ViewGroup parent) {
        v = View.inflate(mContext, R.layout.cell_bus_station_list, null);
        try {
            RelativeLayout mmBusStationListContainer = v.findViewById(R.id.busStopListContainer);
            final TextView mmBusStopNumber = (TextView) v.findViewById(R.id.busStopNumber);
            TextView mmBusStopName = (TextView) v.findViewById(R.id.busStopName);
            TextView mmBusNextStopName = (TextView) v.findViewById(R.id.busNextStopName);
            
                mmBusStopNumber.setText(mBusDetailList.get(index).getRouteTp());
                String mmBusNextStop = null;
                if (mBusDetailList.get(index).getNodeNm().contains("기점") || mBusDetailList.get(index).getNodeNm().contains("종점")) {
                    mmBusNextStop = "(" + mBusDetailList.get(index).getNodeNm() + ")";
                } else {
                    mmBusNextStop = "(" + mBusDetailList.get(index).getNodeNm() + " 방향)";
                }
                mmBusStopName.setText(mBusDetailList.get(index).getNodeNm());
                mmBusNextStopName.setText(mmBusNextStop);
                // 버스 도착정보 리스트
                if (mBusDetailList.size() > 0) {
                    LinearLayout mBusStationLayout = (LinearLayout) v.findViewById(R.id.busLayoutContainer);
                    mBusStationLayout.setOrientation(LinearLayout.VERTICAL);
                    for (int i = 0; i < mBusDetailList.size(); i++) {
                        View mmBusDetailView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.cell_bus_station_detail, null, false);
                        mBusArrivalInfoCell = (RelativeLayout) mmBusDetailView.findViewById(R.id.busInfoCell);

                        if (i < SHOW_BUS_COUNT) {
                            // 버스 정류장의 마지막줄이면 밑에 여백추가
                            if(i == mBusDetailList.size()-1){
                                RelativeLayout mmMarginBottom = mmBusDetailView.findViewById(R.id.marginBottom);
                                mmMarginBottom.setVisibility(View.VISIBLE);
                            }
                            // 버스 번호
                            TextView mmBusNumber = (TextView) mmBusDetailView.findViewById(R.id.busNum_stop);
                            mmBusNumber.setText(mBusDetailList.get(i).getRouteNo());
                            // 버스 도착 정보
                            TextView mmArrivedInfoTxt = (TextView) mmBusDetailView.findViewById(R.id.arrivedInfo_stop);
                            String mmArrivedInfo = mBusDetailList.get(i).getBusArriveInfo();
                            if (mmArrivedInfo.equals("0")
                                    || mmArrivedInfo.equals("1")
                                    || mmArrivedInfo.equals("2")) {
                                ((TextView)mmBusDetailView.findViewById(R.id.arrivedInfoTxt_stop)).setVisibility(View.GONE);
                                mmArrivedInfoTxt.setVisibility(View.GONE);
                                ((TextView)mmBusDetailView.findViewById(R.id.arrivedSoonTxt)).setVisibility(View.VISIBLE);
                            }else {
                                //시간(분)
//                            int mmArriveMin = Integer.parseInt(mBusDetailList.get(i).getArrTime()) / 60;
//                            mmArrivedInfoTxt.setText(mmArriveMin + "");
                                //카운ㅌ트
                                mmArrivedInfoTxt.setText(mmArrivedInfo);
                            }

                            // 일반버스이면 파란색, 마을버스이면 보라색
                            if (mBusDetailList.get(i).getRouteTp().equals("일반버스")) {
                                if (mBusDetailList.get(i).getRouteNo().contains("100")
                                        || mBusDetailList.get(i).getRouteNo().contains("200")
                                        || mBusDetailList.get(i).getRouteNo().contains("300")
                                        || mBusDetailList.get(i).getRouteNo().contains("400")) {
                                    mmBusNumber.setBackgroundResource(R.drawable.rounded_corner_yellow_fill);
                                    mmBusNumber.setPadding(60, 20, 60, 20);
                                } else {
                                    mmBusNumber.setBackgroundResource(R.drawable.rounded_corner_blue_fill);
                                    mmBusNumber.setPadding(60, 20, 60, 20);
                                }
                            } else {
                                mmBusNumber.setBackgroundResource(R.drawable.rounded_corner_purple_fill);
                                mmBusNumber.setPadding(60, 20, 60, 20);
                            }
                            mBusStationLayout.addView(mmBusDetailView);
                        }
                    }
                    // 버스의 도착정보가 없다면
                } else {
                    LinearLayout mBusStationLayout = (LinearLayout) v.findViewById(R.id.busLayoutContainer);
                    mBusStationLayout.setOrientation(LinearLayout.VERTICAL);
                    View mmBusDetailView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.cell_no_bus, null, false);
                    mBusStationLayout.addView(mmBusDetailView);
                    Timber.d("노뻐쓰");
                }
                mmBusStationListContainer.setOnLongClickListener(v1 -> {
                    HashMap<String, String> mmTempData = new HashMap<>();
                    mmTempData.put(Key.BUS_NODE_ID, mBusDetailList.get(index).getNodeId());
                    mmTempData.put(Key.BUS_NODE_NAME, mBusDetailList.get(index).getNodeNm());
                    mmTempData.put(Key.BUS_NODE_NO, mBusDetailList.get(index).getRouteNo());
                    AppData.GetInstance().SetSelectedNum(mContext, index);
                    if(mListener != null) {
                        mListener.didRespond(This, Define.EVENT_LONG_CLICK_ITEM, mmTempData);
                    }
                    return false;
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public interface Listener {
        public void didRespond(Adapter adapter, String event, HashMap<String, String> busStopData);
    }
}

