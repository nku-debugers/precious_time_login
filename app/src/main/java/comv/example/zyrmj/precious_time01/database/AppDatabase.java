package comv.example.zyrmj.precious_time01.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import comv.example.zyrmj.precious_time01.dao.CategoryDao;
import comv.example.zyrmj.precious_time01.dao.HabitCategoryDao;
import comv.example.zyrmj.precious_time01.dao.HabitDao;
import comv.example.zyrmj.precious_time01.dao.HabitQuoteDao;
import comv.example.zyrmj.precious_time01.dao.PlanDao;
import comv.example.zyrmj.precious_time01.dao.QuoteDao;
import comv.example.zyrmj.precious_time01.dao.TemplateDao;
import comv.example.zyrmj.precious_time01.dao.TemplateItemDao;
import comv.example.zyrmj.precious_time01.dao.UserDao;
import comv.example.zyrmj.precious_time01.entity.Category;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.Plan;
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.entity.Todo;
import comv.example.zyrmj.precious_time01.entity.User;
import comv.example.zyrmj.precious_time01.entity.relations.HabitQuote;
import comv.example.zyrmj.precious_time01.entity.relations.HabitCategory;
import comv.example.zyrmj.precious_time01.entity.relations.TodoCategory;
import comv.example.zyrmj.precious_time01.entity.relations.TodoQuote;

//Singleton
@Database(entities = {Category.class, Habit.class, Quote.class, Template.class,
        TemplateItem.class, Todo.class, User.class, HabitQuote.class, HabitCategory.class,
        TodoCategory.class, TodoQuote.class, Plan.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "time_db").allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public abstract CategoryDao categoryDao();

    public abstract TemplateItemDao templateItemDao();

    public abstract TemplateDao templateDao();

    public abstract HabitDao habitDao();

    public abstract UserDao userDao();

    public abstract QuoteDao quoteDao();

    public abstract HabitQuoteDao habitQuoteDao();

    public abstract HabitCategoryDao habitCategoryDao();

    public abstract PlanDao planDao();
}
