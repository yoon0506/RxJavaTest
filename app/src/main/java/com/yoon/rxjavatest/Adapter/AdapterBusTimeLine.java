package com.yoon.rxjavatest.Adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.tcqq.timelineview.TimelineView;
import com.yoon.rxjavatest.R;
import com.yoon.rxjavatest.busData.BusTimeLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterBusTimeLine extends BaseAdapter {
    private AdapterBusTimeLine This = this;

    private Context mContext;
    private View mView;
    public String mBusStopNum;
    protected Fragment mParent;
    private List<BusTimeLine> mBusTimeLineList;
    public int mShowBusNodeOrd = -1;
    public int[] mCurrentBusNodeOrd;
    boolean mIsShowBusIcon;
    // UI
    private RelativeLayout mBusTimeLineContainer;
    private View mBusIcon;

    // 맵 on off를 위한 index
    private ArrayList<HashMap<String, String>> mInfo;

    private Listener mListener;

    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    public AdapterBusTimeLine(Context context, List<BusTimeLine> busTimeLineDataList, Fragment parent) {
        this.mContext = context;
        this.mBusTimeLineList = busTimeLineDataList;
        this.mParent = parent;
    }

    @Override
    public int getCount() {
        return mBusTimeLineList.size();
    }

    @Override
    public Object getItem(int i) {
        return mBusTimeLineList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int index, View v, final ViewGroup parent) {
        v = View.inflate(mContext, R.layout.cell_bus_time_line, null);
        try {
            TimelineView mmTimeLine = (TimelineView) v.findViewById(R.id.timeLineStick);
            TextView mmBusTimeLineNumber = (TextView) v.findViewById(R.id.busStopNumber_TimeLineCell);
            TextView mmBusTimeLineName = (TextView) v.findViewById(R.id.busStopName_TimeLineCell);
            ImageView mmBusTimeLineArrivedInfo = (ImageView) v.findViewById(R.id.busStopInfo_TimeLineCell);
            mmBusTimeLineArrivedInfo.setVisibility(View.GONE);

//            mmTimeLine.setMarker(ContextCompat.getDrawable(mContext, R.drawable.icn_station), ContextCompat.getColor(mContext, R.color.textColorBlack));
//            mmTimeLine.setMarkerColor(ContextCompat.getColor(mContext, R.color.textColorBlack));

            mmBusTimeLineNumber.setText(mBusTimeLineList.get(index).getBusStopNumber());
            mmBusTimeLineName.setText(mBusTimeLineList.get(index).getBusStopName());

            // 선택한 정류장 번호이면 보임 + 아이콘 이미지 변경.
//            if (mBusTimeLineList.get(index).getBusStopNumber().equals(mBusStopNum)) {
            if (mBusTimeLineList.get(index).getBusStopNumber().equals(mBusStopNum)) {
                mmBusTimeLineArrivedInfo.setVisibility(View.VISIBLE);
                mmTimeLine.setMarker(ContextCompat.getDrawable(mContext, R.drawable.icn_fav_station), ContextCompat.getColor(mContext, R.color.white));
            } else {
                mmTimeLine.setMarker(ContextCompat.getDrawable(mContext, R.drawable.icn_station), ContextCompat.getColor(mContext, R.color.gray));
            }


            if (mCurrentBusNodeOrd != null && mCurrentBusNodeOrd.length > 0) {
                mIsShowBusIcon = false;
                for (int i = 0; i < mCurrentBusNodeOrd.length; i++) {
                    if (index == mCurrentBusNodeOrd[i] - 1) {
                        mIsShowBusIcon = true;
                    }

                    if (mIsShowBusIcon) {
                        ((ImageView) v.findViewById(R.id.busIcon_TimeLine)).setVisibility(View.VISIBLE);
                    } else {
                        ((ImageView) v.findViewById(R.id.busIcon_TimeLine)).setVisibility(View.GONE);
                    }
                }
            } else {
                ((ImageView) v.findViewById(R.id.busIcon_TimeLine)).setVisibility(View.GONE);
            }

//            if (mShowBusNodeOrd != -1) {
//
//                if (index == mShowBusNodeOrd) {
//
//                    ((ImageView) v.findViewById(R.id.busIcon_TimeLine)).setVisibility(View.VISIBLE);
//                } else {
//                    ((ImageView) v.findViewById(R.id.busIcon_TimeLine)).setVisibility(View.GONE);
//                }
//                // "-1"이면(도착정보 없음이면) 아이콘 숨김.
//            } else {
//                ((ImageView) v.findViewById(R.id.busIcon_TimeLine)).setVisibility(View.GONE);
//            }
//            mmTimeLine.setMarker(ContextCompat.getDrawable(mContext, R.drawable.icn_station), ContextCompat.getColor(mContext, R.color.textColorBlack));
//            mmTimeLine.setMarker(ContextCompat.getDrawable(mContext, R.drawable.icn_station));

        } catch (Exception e) {
            e.printStackTrace();
        }

        mView = v;
        return v;
    }
//
//    public void setBusIconPosition(int position) {
//        if (mShowBusNodeOrd != -1) {
//            mBusTimeLineContainer = mView.findViewById(R.id.busTimeLineContainer);
//            if (mBusIcon != null) {
//                mBusIcon.destroyDrawingCache();
//                mBusIcon = null;
//            }
//            mBusIcon = ((Activity) mContext).getLayoutInflater().inflate(R.layout.bus_icon, null, false);
//            mBusIcon.setTag("busIcon");
//
//            getView(position, mView, null);
//        }
//    }

    public interface Listener {
        public void didRespond(Adapter adapter, String event, ArrayList<HashMap<String, String>> dataList);
    }

}

