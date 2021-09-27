package com.yoon.rxjavatest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Items {

    @SerializedName("item")
    @Expose
    private List<Item> item = null;

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return item.toString();
    }
}
