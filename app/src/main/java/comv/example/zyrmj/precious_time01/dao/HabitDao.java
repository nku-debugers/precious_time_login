package comv.example.zyrmj.precious_time01.dao;


import androidx.room.Dao;
import androidx.room.Insert;

import comv.example.zyrmj.precious_time01.entity.Habit;


@Dao
public interface HabitDao {
    @Insert
    void insert(Habit... habits);
}


