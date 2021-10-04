package com.yoon.rxjavatest.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.yoon.rxjavatest.AppData;
import com.yoon.rxjavatest.Key;
import com.yoon.rxjavatest.R;
import com.yoon.rxjavatest.busData.BusStation;
import com.yoon.rxjavatest.busData.BusStationDetail;
import com.yoon.rxjavatest.databinding.CellBusStationListBinding;

import java.util.ArrayList;
import java.util.HashMap;

import timber.log.Timber;

public class AdapterBusStation extends RecyclerView.Adapter<AdapterBusStation.BusStationViewHolder> {

    private Context mContext;
    private ArrayList<BusStation> mBusStationList;

    // 도착예정인 버스 정보 노출 수.
    private static final int SHOW_BUS_COUNT = 5;

    private Listener mListener;

    public void SetListener(Listener listener) {
        mListener = listener;
    }

    public AdapterBusStation(Context mContext, ArrayList<BusStation> mBusStationList) {
        this.mContext = mContext;
        this.mBusStationList = mBusStationList;
    }

    @NonNull
    @Override
    public BusStationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_bus_station_list, parent, false);
        return new BusStationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusStationViewHolder holder, int position) {

        holder.mmBinding.busStopNumber.setText(mBusStationList.get(position).getBusNodeNo());
        String mmBusNextStop = null;
        if (mBusStationList.get(position).getBusNodeName().contains("기점") || mBusStationList.get(position).getBusNodeName().contains("종점")) {
            mmBusNextStop = "(" + mBusStationList.get(position).getBusNodeName() + ")";
        } else {
            mmBusNextStop = "(" + mBusStationList.get(position).getBusNodeName() + " 방향)";
        }

        holder.mmBinding.busStopName.setText(mBusStationList.get(position).getBusNodeName());
        holder.mmBinding.busNextStopName.setText(mmBusNextStop);
        // 버스 도착정보 리스트

        if (mBusStationList.get(position).getArrivalBusInfo().size() > 0) {
            holder.mmBinding.busLayoutContainer.setOrientation(LinearLayout.VERTICAL);
            for (BusStationDetail busDetailData : mBusStationList.get(position).getArrivalBusInfo()) {
                View mmBusDetailView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.cell_bus_station_detail, null, false);
                // 버스 번호
                TextView mmBusNumber = (TextView) mmBusDetailView.findViewById(R.id.busNum_stop);
                mmBusNumber.setText(busDetailData.getRouteNo());
                // 버스 도착 정보
                TextView mmArrivedInfoTxt = (TextView) mmBusDetailView.findViewById(R.id.arrivedInfo_stop);
                String mmArrivedInfo = busDetailData.getBusArriveInfo();
                if (mmArrivedInfo.equals("0")
                        || mmArrivedInfo.equals("1")
                        || mmArrivedInfo.equals("2")) {
                    ((TextView) mmBusDetailView.findViewById(R.id.arrivedInfoTxt_stop)).setVisibility(View.GONE);
                    mmArrivedInfoTxt.setVisibility(View.GONE);
                    ((TextView) mmBusDetailView.findViewById(R.id.arrivedSoonTxt)).setVisibility(View.VISIBLE);
                } else {
                    // 도착 정보 시간(분)
//                            int mmArriveMin = Integer.parseInt(busDetailData.getArrTime()) / 60;
//                            mmArrivedInfoTxt.setText(mmArriveMin + "");
                    // 도착 정보 카운트
                    mmArrivedInfoTxt.setText(mmArrivedInfo);
                }
                // 일반버스이면 파란색, 마을버스이면 보라색
                if (busDetailData.getRouteTp().equals("일반버스")) {
                    if (busDetailData.getRouteNo().contains("100")
                            || busDetailData.getRouteNo().contains("200")
                            || busDetailData.getRouteNo().contains("300")
                            || busDetailData.getRouteNo().contains("400")) {
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
                holder.mmBinding.busLayoutContainer.addView(mmBusDetailView);
            }
            // 버스 정류장의 마지막줄이면 밑에 여백추가
//                    if(i == mBusStationList.size()-1){
//                        RelativeLayout mmMarginBottom = mmBusDetailView.findViewById(R.id.marginBottom);
//                        mmMarginBottom.setVisibility(View.VISIBLE);
//                    }

            // 버스의 도착정보가 없다면
        } else {
            holder.mmBinding.busLayoutContainer.setOrientation(LinearLayout.VERTICAL);
            View mmBusDetailView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.cell_no_bus, null, false);
            holder.mmBinding.busLayoutContainer.addView(mmBusDetailView);
            Timber.d("노뻐쓰");
        }

    }

    @Override
    public int getItemCount() {
        return mBusStationList.size();
    }

    class BusStationViewHolder extends RecyclerView.ViewHolder {
        public CellBusStationListBinding mmBinding;

        public BusStationViewHolder(@NonNull View itemView) {
            super(itemView);
            mmBinding = DataBindingUtil.bind(itemView);

            mmBinding.busStopListContainer.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                HashMap<String, String> mmTempData = new HashMap<>();
                mmTempData.put(Key.BUS_NODE_ID, mBusStationList.get(position).getBusNodeId());
                mmTempData.put(Key.BUS_NODE_NAME, mBusStationList.get(position).getBusNodeName());
                mmTempData.put(Key.BUS_NODE_NO, mBusStationList.get(position).getBusNodeNo());
                AppData.GetInstance().SetSelectedNum(mContext, position);
                if (mListener != null) {
                    mListener.eventRemoveItem(mmTempData);
                }
                return false;
            });
        }

        void onBind() {

        }
    }

    public interface Listener {
        public void eventRemoveItem(HashMap<String, String> busData);
    }
}
