package com.yoon.rxjavatest.busData;

public class BusStationDetail {
    private String busArriveInfo;
    private String arrTime;
    private String routeId;
    private String routeNo;
    private String routeTp;

    public BusStationDetail(String busArriveInfo, String arrTime, String routeId, String routeNo, String routeTp) {
        this.busArriveInfo = busArriveInfo;
        this.arrTime = arrTime;
        this.routeId = routeId;
        this.routeNo = routeNo;
        this.routeTp = routeTp;
    }

    public String getBusArriveInfo() {
        return busArriveInfo;
    }

    public void setBusArriveInfo(String busArriveInfo) {
        this.busArriveInfo = busArriveInfo;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getRouteTp() {
        return routeTp;
    }

    public void setRouteTp(String routeTp) {
        this.routeTp = routeTp;
    }

}
