package com.yoon.rxjavatest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {
    @SerializedName("items")
    @Expose
    private Items items;
    @SerializedName("numOfRows")
    @Expose
    private Integer numOfRows;
    @SerializedName("pageNo")
    @Expose
    private Integer pageNo;
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
