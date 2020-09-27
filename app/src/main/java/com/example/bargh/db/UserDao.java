package com.example.bargh.db;

import com.example.bargh.db.entity.User;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users LIMIT 1")
    User getFirst();

    @Query("DELETE FROM users")
    void deleteAll();

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

}
