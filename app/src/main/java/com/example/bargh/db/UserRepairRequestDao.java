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

    @Query("SELECT * FROM user_repair_requests")
    List<UserRepairRequest> getAll();

    @Query("SELECT * FROM user_repair_requests LIMIT 1")
    UserRepairRequest getFirst();

    @Query("DELETE FROM user_repair_requests")
    void wipeTable();

    @Insert
    void insertAll(List<UserRepairRequest> userRepairRequests);

    @Insert
    void insert(UserRepairRequest userRepairRequest);

    @Delete
    void delete(UserRepairRequest userRepairRequest);
}
