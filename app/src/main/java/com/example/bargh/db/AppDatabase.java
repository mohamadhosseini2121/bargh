package com.example.bargh.db;

import android.content.Context;

import com.example.bargh.db.entity.User;
import com.example.bargh.db.entity.UserRepairRequest;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, UserRepairRequest.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract UserRepairRequestDao userRepairRequestDao();

    public static AppDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    AppDatabase.class,
                    "AppDB").allowMainThreadQueries().build();
        }
        return instance;

    }
}

