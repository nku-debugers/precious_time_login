package comv.example.zyrmj.precious_time01.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;

import androidx.room.Query;
import comv.example.zyrmj.precious_time01.entity.Category;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.relations.HabitCategory;

@Dao
public interface HabitCategoryDao {
    @Insert
    void insert(HabitCategory... habitCategories);
    @Query("select * from habitcategory where user_id=:userId and habit_name=:habitName")
    List<HabitCategory> getCategoriesForHabit(String userId,String habitName);
    @Query("delete from habitcategory where user_id=:userId and habit_name=:habitName")
    void delete(String userId,String habitName);
}
