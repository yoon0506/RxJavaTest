package com.yoon.rxjavatest.busData;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yoon.rxjavatest.AppData;
import com.yoon.rxjavatest.Define;

import java.util.ArrayList;

public class SaveManagerBusList {

    private static SaveManagerBusList mInstance = new SaveManagerBusList();

    public static SaveManagerBusList GetInstance() {
        return mInstance;
    }

    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public String saveData(ArrayList<BusStop> busStops) {
        JsonObject obj1 = new JsonObject();
        JsonArray arr1 = new JsonArray();
        for (int i = 0; i < busStops.size(); i++) {
            JsonArray arr2 = new JsonArray();
            ArrayList<BusStop> busStopList = AppData.GetInstance().mBusStopList;
//            ArrayList<BusTimeLine> busTimeLineList = AppData.GetInstance().mBusTimeLineList;
            for (BusStop busStop : busStopList) {
                JsonObject obj2 = new JsonObject();
                obj2.addProperty("busStopNumber", busStop.getBusStopNumber());
                obj2.addProperty("busStopName", busStop.getBusStopName());
                obj2.addProperty("nextBusStop", busStop.getNextBusStop());
                if (busStop.getArrivedInfo() != null) {
                    obj2.addProperty("arrivedInfo", busStop.getArrivedInfo());
                } else {
                    obj2.addProperty("arrivedInfo", "-1");
                }
                obj2.addProperty("busNum", busStop.getBusNum());
                obj2.addProperty("nodeId", busStop.getNodeId());
                obj2.addProperty("nodeOrd", busStop.getNodeOrd());
                obj2.addProperty("routeId", busStop.getRouteId());
                obj2.addProperty("routetp", busStop.getRoutetp());
                obj2.addProperty("latitude", busStop.getLatitude());
                obj2.addProperty("longitude", busStop.getLongitude());
                obj2.addProperty("finalStartBusStop", busStop.getFinalStartBusStop());
                obj2.addProperty("finalEndBusStop", busStop.getFinalEndBusStop());

                arr2.add(obj2);
            }
            obj1.add("busStop", arr2);
            arr1.add(obj1);
        }
        return obj1.toString();
    }

    public void loadData(String json) {
        ArrayList<BusStop> busStopList = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject obj1 = (JsonObject) parser.parse(json);
        JsonArray arr1 = obj1.getAsJsonArray("busStop");
        if (arr1.size() > 0) {
            for (int i = 0; i < arr1.size(); i++) {
                BusStop busStop = new BusStop();
                JsonObject obj2 = (JsonObject) arr1.get(i);
                busStop.setBusStopNumber(obj2.get("busStopNumber").getAsString());
                busStop.setBusStopName(obj2.get("busStopName").getAsString());
                busStop.setNextBusStop(obj2.get("nextBusStop").getAsString());
                busStop.setArrivedInfo("10");
                busStop.setBusNum(obj2.get("busNum").getAsString());
                busStop.setNodeId(obj2.get("nodeId").getAsString());
                busStop.setNodeOrd(obj2.get("nodeOrd").getAsString());
                busStop.setRouteId(obj2.get("routeId").getAsString());
                busStop.setRoutetp(obj2.get("routetp").getAsString());
                busStop.setLatitude(obj2.get("latitude").getAsDouble());
                busStop.setLongitude(obj2.get("longitude").getAsDouble());
                busStop.setFinalStartBusStop(obj2.get("finalStartBusStop").getAsString());
                busStop.setFinalEndBusStop(obj2.get("finalEndBusStop").getAsString());

                busStopList.add(busStop);
            }
        }
        if (mListener != null) mListener.didRespond(Define.LOAD_DATA, busStopList);

    }

    public interface Listener {
        public void didRespond(String event, ArrayList<BusStop> data);
    }
}
