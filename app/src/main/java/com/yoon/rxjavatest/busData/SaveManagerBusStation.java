package com.yoon.rxjavatest.busData;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yoon.rxjavatest.AppData;
import com.yoon.rxjavatest.Define;

import java.util.ArrayList;

import timber.log.Timber;

public class SaveManagerBusStation {

    private static SaveManagerBusStation mInstance = new SaveManagerBusStation();

    public static SaveManagerBusStation GetInstance() {
        return mInstance;
    }

    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public String saveBusStationData(ArrayList<BusStation> busStops) {
        JsonObject obj1 = new JsonObject();
        JsonArray arr1 = new JsonArray();
        for (int i = 0; i < busStops.size(); i++) {
            JsonArray arr2 = new JsonArray();
            ArrayList<BusStation> busStationList = AppData.GetInstance().mBusStationList;
            for (BusStation busStation : busStationList) {
                JsonObject obj2 = new JsonObject();
                obj2.addProperty("nodeNo", busStation.getBusNodeNo());
                obj2.addProperty("nodeName", busStation.getBusNodeName());
                obj2.addProperty("nodeId", busStation.getBusNodeId());
                obj2.addProperty("nextBusStop", busStation.getBusNextStopName());
                arr2.add(obj2);
            }
            obj1.add("busStationInfo", arr2);
            arr1.add(obj1);
        }
        Timber.tag("checkCheck").d("저장된 데이터 : %s",obj1.toString());
        return obj1.toString();
    }

    public void loadData(String json) {
        ArrayList<BusStation> busStationList = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject obj1 = (JsonObject) parser.parse(json);
        JsonArray arr1 = obj1.getAsJsonArray("busStationInfo");
        if (arr1.size() > 0) {
            for (int i = 0; i < arr1.size(); i++) {
                BusStation busStation = new BusStation();
                JsonObject obj2 = (JsonObject) arr1.get(i);
                if(busStation.getBusNodeNo() != null){
                    busStation.setBusNodeNo(obj2.get("nodeNo").getAsString());
                }
                busStation.setBusNodeName(obj2.get("nodeName").getAsString());
                busStation.setBusNodeId(obj2.get("nodeId").getAsString());
                if(busStation.getBusNextStopName() != null) {
                    busStation.setBusNextStopName(obj2.get("nextBusStop").getAsString());
                }
                busStationList.add(busStation);
            }
        }
        if (mListener != null) mListener.didRespond(Define.LOAD_DATA, busStationList);

    }

    public interface Listener {
        public void didRespond(String event, ArrayList<BusStation> data);
    }
}