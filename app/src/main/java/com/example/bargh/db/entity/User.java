package com.example.bargh.db.entity;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @Ignore
    public static final int USER_TYPE_CLIENT = 0;
    @Ignore
    public static final int USER_TYPE_FIXER = 1;
    @Ignore
    public static final int USER_TYPE_ADMIN = 2;


    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "email")
    private String email;

    @PrimaryKey
    @ColumnInfo(name = "mobile_number")
    @NonNull
    private String mobileNumber;

    @ColumnInfo(name = "user_type")
    private int userType;

    @ColumnInfo(name = "user_pic_url")
    private String userPicUrl;

    public User(String firstName, String lastName, String email, @NonNull String mobileNumber, int userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.userType = userType;
    }

    @Ignore
    public User(String firstName, String lastName, String email, @NonNull String mobileNumber, int userType, String userPicUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.userType = userType;
    }


    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(@NonNull String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
