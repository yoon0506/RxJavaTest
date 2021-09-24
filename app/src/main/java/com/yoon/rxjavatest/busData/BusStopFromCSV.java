package com.yoon.rxjavatest.busData;

import com.yoon.rxjavatest.Key;

import java.util.HashMap;

public class BusStopFromCSV {

    private String busNodeId;
    private String busNodeNum;
    // nodeNm
    private String busStopName;
    private String nextBusStopName;
    private double latitude;
    private double longitude;

//    private ArrayList<String> routeNo = new ArrayList<>();
//    private ArrayList<String> routeId = new ArrayList<>();

    public BusStopFromCSV(HashMap<String, String> busStopData) {
        this.busNodeId = busStopData.get(Key.BUS_NODE_ID);
        this.busStopName = busStopData.get(Key.BUS_NODE_NAME);
        this.busNodeNum = busStopData.get(Key.BUS_NODE_NO);
        this.nextBusStopName = busStopData.get(Key.BUS_NEXT_STOP);
        this.latitude = Double.parseDouble(busStopData.get(Key.BUS_LATITUDE));
        this.longitude = Double.parseDouble(busStopData.get(Key.BUS_LONGITUDE));
//        this.routeNo.add(busStopData.get(Key.BUS_ROUTE_NO));
//        this.routeId.add(busStopData.get(Key.BUS_ROUTE_ID));
    }

    public String getBusNodeId() {
        return busNodeId;
    }


    public String getBusNodeNum() {
        return busNodeNum;
    }


    public String getBusStopName() {
        return busStopName;
    }


    public String getNextBusStopName() {
        return nextBusStopName;
    }

    public void setNextBusStopName(String nextBusStopName) {
        this.nextBusStopName = nextBusStopName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

//    public ArrayList<String> getRouteNo() {
//        return routeNo;
//    }
//
//    public void setRouteNo(ArrayList<String> routeNo) {
//        this.routeNo = routeNo;
//    }
//
//    public ArrayList<String> getRouteId() {
//        return routeId;
//    }
//
//    public void setRouteId(ArrayList<String> routeId) {
//        this.routeId = routeId;
//    }

}
