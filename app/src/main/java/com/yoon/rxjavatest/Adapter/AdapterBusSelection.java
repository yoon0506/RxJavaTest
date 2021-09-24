package com.yoon.rxjavatest.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.yoon.rxjavatest.Define;
import com.yoon.rxjavatest.R;
import com.yoon.rxjavatest.busData.BusSelection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterBusSelection extends BaseAdapter {
    private AdapterBusSelection This = this;

    private Context mContext;
    protected Fragment mParent;
    private List<BusSelection> mBusList;
    private int mSelectedNum = -1;

    // UI
    private RelativeLayout mBusListContainer;

    // 맵 on off를 위한 index
    private ArrayList<HashMap<String, String>> mInfo;

    private Listener mListener;

    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    public AdapterBusSelection(Context context, List<BusSelection> busList, Fragment parent) {
        this.mContext = context;
        this.mBusList = busList;
        this.mParent = parent;
    }

    @Override
    public int getCount() {
        return mBusList.size();
    }

    @Override
    public Object getItem(int i) {
        return mBusList.get(i);
    }

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(final int index, View v, final ViewGroup parent) {
        if (v == null) {
            v = View.inflate(mContext, R.layout.cell_bus_selection, null);
        }

        try {
            mBusListContainer = v.findViewById(R.id.busInfoContainer_busSelection);
            TextView mmBusSelectionNumber = (TextView) v.findViewById(R.id.busNumber_busSelection);
            TextView mmEndBusStop = (TextView) v.findViewById(R.id.finalBusStopInfo_busSelection);
            TextView mmCurrentBusStop = (TextView) v.findViewById(R.id.currentBusStop_busSelection);
            TextView mmNextBusStop = (TextView) v.findViewById(R.id.nextBusStop_busSelection);
            ImageView mmDirection = (ImageView) v.findViewById(R.id.direction_busSelection);

            mmEndBusStop.setText(mBusList.get(index).getEndBusStop() + " 방향");
            mmCurrentBusStop.setText(mBusList.get(index).getCurrentBusStop());
            mmNextBusStop.setText(mBusList.get(index).getNextBusStop());
            mmBusSelectionNumber.setText(mBusList.get(index).getBusNumber());

            // 일반버스이면 파란색, 마을버스이면 보라색
            if (mBusList.get(index).getRoutetp().equals("일반버스")) {
                if (mBusList.get(index).getBusNumber().contains("100")
                        || mBusList.get(index).getBusNumber().contains("200")
                        || mBusList.get(index).getBusNumber().contains("300")
                        || mBusList.get(index).getBusNumber().contains("400")) {
                    mmBusSelectionNumber.setTextColor(mContext.getResources().getColor(R.color.yellow));
                    mmCurrentBusStop.setBackgroundResource(R.drawable.rounded_corner_yellow);
                    mmCurrentBusStop.setPadding(20,15,20,15);
                    mmDirection.setBackgroundResource(R.drawable.direction_y);
                } else {
                    mmBusSelectionNumber.setTextColor(mContext.getResources().getColor(R.color.blue));
                    mmCurrentBusStop.setBackgroundResource(R.drawable.rounded_corner_blue);
                    mmCurrentBusStop.setPadding(20,15,20,15);
                    mmDirection.setBackgroundResource(R.drawable.direction_b);
                }
            } else {
                mmBusSelectionNumber.setTextColor(mContext.getResources().getColor(R.color.purple));
                mmCurrentBusStop.setBackgroundResource(R.drawable.rounded_corner_purple);
                mmCurrentBusStop.setPadding(20,15,20,15);
                mmDirection.setBackgroundResource(R.drawable.direction_p);
            }


            mBusListContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedNum = index;
                    notifyDataSetChanged();

                    mListener.didRespond(This, Define.EVENT_CLICK_ITEM, mBusList.get(index));
                }
            });

            if (mSelectedNum == index) {
                mBusListContainer.setBackgroundResource(R.drawable.rounded_corner_select);
            } else {
                mBusListContainer.setBackgroundResource(R.drawable.rounded_corner_non);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }
//
//    private void resetContainer(){
//        for(int i= 0;i<mBusList.size();i++){
//            if(mBusListContainer.getTag().equals("busContainer"+i)){
//                mBusListContainer.setBackgroundResource(R.drawable.rounded_corner_non);
//            }
//        }
//    }

    public interface Listener {
        //        public void didRespond(Adapter adapter, String event, ArrayList<HashMap<String, String>> dataList);
        public void didRespond(Adapter adapter, String event, BusSelection busStopData);
    }

}

