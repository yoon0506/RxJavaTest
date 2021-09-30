package com.yoon.rxjavatest.Api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Item {
    //    String
    @SerializedName("endnodenm")
    @Expose
    private String endnodenm;
    @SerializedName("endvehicletime")
    @Expose
    private String endvehicletime;
    @SerializedName("routeid")
    @Expose
    private String routeid;
    @SerializedName("routeno")
    @Expose
    private String routeno;
    @SerializedName("routetp")
    @Expose
    private String routetp;
    @SerializedName("startnodenm")
    @Expose
    private String startnodenm;
    @SerializedName("startvehicletime")
    @Expose
    private String startvehicletime;

    public String getEndnodenm() {
        return endnodenm;
    }

    public void setEndnodenm(String endnodenm) {
        this.endnodenm = endnodenm;
    }

    public String getEndvehicletime() {
        return endvehicletime;
    }

    public void setEndvehicletime(String endvehicletime) {
        this.endvehicletime = endvehicletime;
    }

    public String getRouteid() {
        return routeid;
    }

    public void setRouteid(String routeid) {
        this.routeid = routeid;
    }

    public String getRouteno() {
        return routeno;
    }

    public void setRouteno(String routeno) {
        this.routeno = routeno;
    }

    public String getRoutetp() {
        return routetp;
    }

    public void setRoutetp(String routetp) {
        this.routetp = routetp;
    }

    public String getStartnodenm() {
        return startnodenm;
    }

    public void setStartnodenm(String startnodenm) {
        this.startnodenm = startnodenm;
    }

    public String getStartvehicletime() {
        return startvehicletime;
    }

    public void setStartvehicletime(String startvehicletime) {
        this.startvehicletime = startvehicletime;
    }

    @Override
    public String toString() {
        return "BusData{" +
                "endnodenm='" + endnodenm + '\'' +
                ", endvehicletime='" + endvehicletime + '\'' +
                ", routeid='" + routeid + '\'' +
                ", routeno='" + routeno + '\'' +
                ", routetp='" + routetp + '\'' +
                ", startnodenm='" + startnodenm + '\'' +
                ", startvehicletime='" + startvehicletime + '\'' +
                '}';
    }
}
