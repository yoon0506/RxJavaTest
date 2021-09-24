package com.yoon.rxjavatest.busData;

import com.yoon.rxjavatest.Key;

import java.util.HashMap;

public class BusSelection {

    private String currentBusStop;
    private String nextBusStop;
    private String startBusStop;
    private String endBusStop;
    private String busNumber;
    private String busNodeNumber;
    private String routetp;

    public BusSelection() {

    }

    public BusSelection(HashMap data) {
        this.currentBusStop = String.valueOf(data.get(Key.BUS_NODE_NAME));
        this.nextBusStop = String.valueOf(data.get(Key.BUS_NEXT_STOP));
        this.startBusStop = String.valueOf(data.get(Key.BUS_START_STOP));
        this.endBusStop = String.valueOf(data.get(Key.BUS_END_STOP));
        this.busNumber = String.valueOf(data.get(Key.BUS_ROUTE_NO));
        this.busNodeNumber = String.valueOf(data.get(Key.BUS_NODE_NO));
        this.routetp = String.valueOf(data.get(Key.BUS_ROUTE_TP));
    }

    public String getCurrentBusStop() {
        return currentBusStop;
    }

    public String getNextBusStop() {
        return nextBusStop;
    }

    public String getStartBusStop() {
        return startBusStop;
    }

    public String getEndBusStop() {
        return endBusStop;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public String getBusNodeNumber() {
        return busNodeNumber;
    }

    public String getRoutetp() {
        return routetp;
    }
}
