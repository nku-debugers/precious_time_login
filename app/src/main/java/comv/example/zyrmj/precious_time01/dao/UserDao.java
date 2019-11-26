package comv.example.zyrmj.precious_time01.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import comv.example.zyrmj.precious_time01.entity.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User... users);

    @Update
    void updateUser(User... users);

    @Delete
    void deleteUser(User... users);

    @Query("select * from User")
    LiveData<List<User>> getAllUsers();
}
