package com.example.bargh.db;

import com.example.bargh.db.entity.User;
import com.example.bargh.db.entity.UserRepairRequest;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserRepairRequestDao {

    @Query("SELECT * FROM UserRepairRequest")
    List<UserRepairRequest> getAll();

    @Query("SELECT * FROM user LIMIT 1")
    UserRepairRequest getFirst();

    @Insert
    void insertAll(UserRepairRequest... userRepairRequests);

    @Delete
    void delete(UserRepairRequest userRepairRequest);
}
