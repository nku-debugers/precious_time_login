package comv.example.zyrmj.precious_time01.database;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import comv.example.zyrmj.precious_time01.dao.CategoryDao;
import comv.example.zyrmj.precious_time01.dao.HabitDao;
import comv.example.zyrmj.precious_time01.entity.Category;
import comv.example.zyrmj.precious_time01.entity.Habit;


@Database(entities = {Category.class, Habit.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();
    public abstract HabitDao habitDao();
}
