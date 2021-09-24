package com.yoon.rxjavatest.busData;

import com.yoon.rxjavatest.Key;

import java.util.HashMap;

public class BusTimeLine {

    private String busStopNumber;
    private String busStopManageNumber;
    private String busStopName;
    private String startBusStop;
    private String endBusStop;
    private String nodeOrd;
    private String routetp;

    public BusTimeLine() {
    }

    public BusTimeLine(HashMap<String, String> data) {
        this.busStopNumber = data.get(Key.BUS_NODE_NO);
        this.busStopName = data.get(Key.BUS_NODE_NAME);
        this.startBusStop = data.get(Key.BUS_START_STOP);
        this.endBusStop = data.get(Key.BUS_END_STOP);
        this.nodeOrd = data.get(Key.BUS_NODE_ORD);
        this.routetp = data.get(Key.BUS_ROUTE_TP);
        this.busStopManageNumber = data.get(Key.BUS_NODE_ID);
    }

    public String getBusStopNumber() {
        return busStopNumber;
    }

    public void setBusStopNumber(String busStopNumber) {
        this.busStopNumber = busStopNumber;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public String getBusStopManageNumber() {
        return busStopManageNumber;
    }

    public String getStartBusStop() {
        return startBusStop;
    }

    public String getEndBusStop() {
        return endBusStop;
    }

    public void setBusStopName(String busStopName) {
        this.busStopName = busStopName;
    }

    public String getNodeOrd() {
        return nodeOrd;
    }

    public void setnodeOrd(String nodeOrd) {
        this.nodeOrd = nodeOrd;
    }

    public String getRoutetp() {
        return routetp;
    }

    public void setRoutetp(String routetp) {
        this.routetp = routetp;
    }
}
