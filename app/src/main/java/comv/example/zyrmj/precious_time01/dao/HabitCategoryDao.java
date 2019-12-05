package comv.example.zyrmj.precious_time01.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.relations.HabitCategory;

@Dao
public interface HabitCategoryDao {
    @Insert
    void insert(HabitCategory... habitCategories);
}
