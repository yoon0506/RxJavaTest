package com.yoon.rxjavatest.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.yoon.rxjavatest.AppData;
import com.yoon.rxjavatest.Define;
import com.yoon.rxjavatest.Key;
import com.yoon.rxjavatest.R;
import com.yoon.rxjavatest.busData.BusStation;
import com.yoon.rxjavatest.busData.BusStationDetail;
import com.yoon.rxjavatest.busData.BusStop;
import com.yoon.rxjavatest.databinding.CellBusStationListBinding;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class AdapterBusStation extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private Context mContext;
    private ArrayList<BusStation> mBusStationList = new ArrayList<>();
    private BusStationViewHolder mMainHolder;

    // 도착예정인 버스 정보 노출 수.
    private static final int SHOW_BUS_COUNT = 5;

    // rxjava
    private PublishSubject<BusStation> mPublishSubject;
    // listener
    private Listener mListener;

    public AdapterBusStation(Context mContext) {
        this.mPublishSubject = PublishSubject.create();
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_bus_station_list, parent, false);
            holder = new BusStationViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_list_view_footer, parent, false);
            holder = new FooterViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FooterViewHolder) {
            FooterViewHolder mmFooterViewHolder = (FooterViewHolder) holder;
        } else {
            BusStationViewHolder mainHolder = (BusStationViewHolder) holder;
            mainHolder.mmBinding.busStopNumber.setText(mBusStationList.get(position).getBusNodeNo());
            String mmBusNextStop = null;
            if (mBusStationList.get(position).getBusNodeName().contains("기점") || mBusStationList.get(position).getBusNodeName().contains("종점")) {
                mmBusNextStop = "(" + mBusStationList.get(position).getBusNodeName() + ")";
            } else {
                mmBusNextStop = "(" + mBusStationList.get(position).getBusNodeName() + " 방향)";
            }

            mainHolder.mmBinding.busStopName.setText(mBusStationList.get(position).getBusNodeName());
            mainHolder.mmBinding.busNextStopName.setText(mmBusNextStop);
            // 버스 도착정보 리스트

            mainHolder.mmBinding.busLayoutContainer.setOrientation(LinearLayout.VERTICAL);
            if (mBusStationList.get(position).getArrivalBusInfo().size() > 0) {
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
                    mainHolder.mmBinding.busLayoutContainer.addView(mmBusDetailView);
                }
                // 버스의 도착정보가 없다면
            } else {
                View mmBusDetailView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.cell_no_bus, null, false);
                mainHolder.mmBinding.busLayoutContainer.addView(mmBusDetailView);
                Timber.d("노뻐쓰");
            }

            mainHolder.getLongClickObserver(mBusStationList.get(position)).subscribe(mPublishSubject);
        }
    }

    @Override
    public int getItemCount() {
        return mBusStationList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mBusStationList.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    class BusStationViewHolder extends RecyclerView.ViewHolder {
        public CellBusStationListBinding mmBinding;

        public BusStationViewHolder(@NonNull View itemView) {
            super(itemView);
            mmBinding = DataBindingUtil.bind(itemView);
        }

        Observable<BusStation> getLongClickObserver(BusStation item) {
            return Observable.create(e -> mmBinding.busStopListContainer.setOnLongClickListener(
                    v -> {
                        e.onNext(item);
                        Timber.tag("checkCheck").d("item : %s", item.getBusNodeName());
                        return false;
                    }
            ));
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);

            ImageView mAddBusBtn = itemView.findViewById(R.id.addBusBtn);
            mAddBusBtn.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.addBusStation();
                }
            });
        }
    }

    private void clearData(){
        int mmSize = mBusStationList.size();
        mBusStationList.clear();
        notifyItemRangeRemoved(0, mmSize);
        Timber.tag("checkCheck").d("clearData");
        notifyDataSetChanged();
    }

    public void updateItems(ArrayList<BusStation> busData){
        clearData();
        mBusStationList.addAll(busData);
    }

    public void setListener(Listener listener){
        mListener = listener;
    }

    public interface Listener {
        public void addBusStation();
    }

    public PublishSubject<BusStation> getItemPublishSubject() {
        return mPublishSubject;
    }
}
