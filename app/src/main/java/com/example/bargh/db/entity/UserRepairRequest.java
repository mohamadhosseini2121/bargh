package com.example.bargh.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class UserRepairRequest {

    public static final int STATE_PENDING = 0;
    public static final int STATE_DOING = 1;
    public static final int STATE_DONE = 2;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "info")
    private String info;

    @ColumnInfo(name = "user")
    private String user;

    @ColumnInfo(name = "state")
    private int state;

    @ColumnInfo(name = "lat")
    private double lat;

    @ColumnInfo(name = "lng")
    private double lng;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    public String getStateString (){
        switch (state) {

            case STATE_PENDING:
                return "در حال بررسی";
            case STATE_DOING:
                return "در حال انجام";
            default:
                return "انجام شده";
        }
    }

    public String getTimestamp() { return timestamp; }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


}
