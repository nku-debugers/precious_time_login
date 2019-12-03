package comv.example.zyrmj.precious_time01.dao;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;

import androidx.room.Query;
import comv.example.zyrmj.precious_time01.entity.Habit;


@Dao
public interface HabitDao {
    @Insert
    void insert(Habit... habits);

    @Query("SELECT * FROM Habit WHERE user_id=:userId")
    LiveData<List<Habit>> getAllHabits(String userId);
}


