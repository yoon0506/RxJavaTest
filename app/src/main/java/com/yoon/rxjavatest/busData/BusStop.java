package com.yoon.rxjavatest.busData;

import com.yoon.rxjavatest.Key;

import java.util.ArrayList;
import java.util.HashMap;

public class BusStop {

    private String busStopNumber;
    private String busStopName;
    private String nextBusStop;
    private String arrivedInfo;
    private String busNum;
    private String nodeId;
    private String nodeOrd;
    private String routeId;
    private String routeTp;

    private double latitude;
    private double longitude;

    private ArrayList<BusTimeLine> mBusTimeLineData;

    private String finalStartBusStop;
    private String finalEndBusStop;


    public BusStop() {
    }

    public BusStop(String busStopName) {
        this.busStopName = busStopName;
    }

    public BusStop(String busNodeId, String busRouteId) {
        this.nodeId = busNodeId;
        this.routeId = busRouteId;
    }

    public BusStop(HashMap<String,String> busStopData) {
        this.busStopNumber = busStopData.get(Key.BUS_NODE_NO);
        this.busStopName = busStopData.get(Key.BUS_NODE_NAME);
        this.busNum = busStopData.get(Key.BUS_ROUTE_NO);
        this.nodeId = busStopData.get(Key.BUS_NODE_ID);
        this.nodeOrd = busStopData.get(Key.BUS_NODE_ORD);
        this.routeId = busStopData.get(Key.BUS_ROUTE_ID);
        this.routeTp = busStopData.get(Key.BUS_ROUTE_TP);
    }

    public BusStop(BusStop busStopData) {
        this.busStopNumber = busStopData.getBusStopNumber();
        this.busStopName = busStopData.getBusStopName();
        this.nextBusStop = busStopData.getNextBusStop();
        this.arrivedInfo = busStopData.getArrivedInfo();
        this.busNum = busStopData.getBusNum();
        this.nodeId = busStopData.getNodeId();
        this.nodeOrd = busStopData.getNodeOrd();
        this.routeId = busStopData.getRouteId();
        this.routeTp = busStopData.getRoutetp();
        this.latitude = busStopData.getLatitude();
        this.longitude = busStopData.getLongitude();
        this.finalStartBusStop = busStopData.getFinalStartBusStop();
        this.finalEndBusStop = busStopData.getFinalEndBusStop();
    }

    public BusStop(BusStop busStopData, ArrayList<HashMap<String, String>> busTimeLineData) {
        this.busStopNumber = busStopData.getBusStopNumber();
        this.busStopName = busStopData.getBusStopName();
        this.nextBusStop = busStopData.getNextBusStop();
        this.arrivedInfo = busStopData.getArrivedInfo();
        this.busNum = busStopData.getBusNum();
        this.nodeId = busStopData.getNodeId();
        this.nodeOrd = busStopData.getNodeOrd();
        this.routeId = busStopData.getRouteId();
        this.routeTp = busStopData.getRoutetp();
        this.latitude = busStopData.getLatitude();
        this.longitude = busStopData.getLongitude();
        this.finalStartBusStop = busStopData.getFinalStartBusStop();
        this.finalEndBusStop = busStopData.getFinalEndBusStop();

        mBusTimeLineData = new ArrayList<>();
        for (int i = 0; i < busTimeLineData.size(); i++) {
            HashMap<String, String> mmMap = busTimeLineData.get(i);
            BusTimeLine mBusTimeLine = new BusTimeLine(mmMap);
            mBusTimeLineData.add(mBusTimeLine);
        }
    }

    public String getBusStopNumber() {
        return busStopNumber;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public String getNextBusStop() {
        return nextBusStop;
    }

    public String getBusNum() {
        return busNum;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getArrivedInfo() {
        return arrivedInfo;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public ArrayList<BusTimeLine> getBusTimeLineData() {
        return mBusTimeLineData;
    }

    public String getRoutetp() {
        return routeTp;
    }

    public void setRoutetp(String routeTp) {
        this.routeTp = routeTp;
    }

    public void setBusStopNumber(String busStopNumber) {
        this.busStopNumber = busStopNumber;
    }

    public void setBusStopName(String busStopName) {
        this.busStopName = busStopName;
    }

    public void setNextBusStop(String nextBusStop) {
        this.nextBusStop = nextBusStop;
    }

    public void setArrivedInfo(String arrivedInfo) {
        this.arrivedInfo = arrivedInfo;
    }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }


    public void setBusTimeLineData(ArrayList<BusTimeLine> mBusTimeLineData) {
        this.mBusTimeLineData = mBusTimeLineData;
    }

    public String getFinalStartBusStop() {
        return finalStartBusStop;
    }

    public void setFinalStartBusStop(String finalStartBusStop) {
        this.finalStartBusStop = finalStartBusStop;
    }

    public String getFinalEndBusStop() {
        return finalEndBusStop;
    }

    public void setFinalEndBusStop(String finalEndBusStop) {
        this.finalEndBusStop = finalEndBusStop;
    }

    public String getNodeOrd() {
        return nodeOrd;
    }

    public void setNodeOrd(String nodeOrd) {
        this.nodeOrd = nodeOrd;
    }
}
