package com.example.bargh.db.entity;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;

@Entity (primaryKeys = {"user","timestamp"} , tableName = "user_repair_requests")
public class UserRepairRequest {

    @Ignore
    public static final int STATE_PENDING = 0;
    @Ignore
    public static final int STATE_DOING = 1;
    @Ignore
    public static final int STATE_DONE = 2;
    @Ignore
    public static final int STATE_CANCELLED = 3;


    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "info")
    private String info;

    @NonNull
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

    @NonNull
    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @Ignore
    public UserRepairRequest (String type, String info, @NonNull String user, int state , double lat , double lng){

        this.type = type;
        this.info = info;
        this.user = user;
        this.state = state;
        this.lat = lat;
        this.lng = lng;
    }

    public UserRepairRequest () {}

    public String getStateString (){
        switch (state) {

            case STATE_PENDING:
                return "در حال بررسی";
            case STATE_DOING:
                return "در حال انجام";
            case STATE_DONE:
                return "انجام شده";
            case STATE_CANCELLED:
            default:
                return "لغو شده";
        }
    }

    public String getStateStringByKey(int key){

        switch (key) {
            case STATE_PENDING:
                return "در حال بررسی";
            case STATE_DOING:
                return "در حال انجام";
            case STATE_DONE:
                return "انجام شده";
            case STATE_CANCELLED:
            default:
                return "لغو شده";
        }
    }

    @NonNull
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
