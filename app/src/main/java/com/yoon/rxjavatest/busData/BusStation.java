package com.yoon.rxjavatest.busData;

import java.util.ArrayList;

public class BusStation {
    private String busNodeId;
    private String busNodeName;
    private String busNodeNo;
    private String busNextStopName;
    private ArrayList<BusStationDetail> arrivalBusInfo = new ArrayList<>();

    public BusStation(){}

    public BusStation(String string){
        this.busNodeId = string;
        this.busNodeNo = null;
        this.busNodeName = null;
        this.busNextStopName = null;
    }

    public BusStation(String busNodeId, String busNodeNo, String busNodeName, String busNextStopName) {
        this.busNodeId = busNodeId;
        this.busNodeNo = busNodeNo;
        this.busNodeName = busNodeName;
        this.busNextStopName = busNextStopName;
    }

    public String getBusNodeId() {
        return busNodeId;
    }

    public void setBusNodeId(String busNodeId) {
        this.busNodeId = busNodeId;
    }

    public String getBusNodeName() {
        return busNodeName;
    }

    public void setBusNodeName(String busNodeName) {
        this.busNodeName = busNodeName;
    }

    public String getBusNodeNo() {
        return busNodeNo;
    }

    public void setBusNodeNo(String busNodeNo) {
        this.busNodeNo = busNodeNo;
    }

    public String getBusNextStopName() {
        return busNextStopName;
    }

    public void setBusNextStopName(String busNextStopName) {
        this.busNextStopName = busNextStopName;
    }

    public ArrayList<BusStationDetail> getArrivalBusInfo() {
        return arrivalBusInfo;
    }

    public void setArrivalBusInfo(ArrayList<BusStationDetail> arrivalBusInfo) {
        this.arrivalBusInfo = arrivalBusInfo;
    }
}
