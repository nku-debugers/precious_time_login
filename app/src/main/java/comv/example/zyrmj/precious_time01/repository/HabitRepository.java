package comv.example.zyrmj.precious_time01.repository;
import android.content.Context;
import android.os.AsyncTask;


import java.util.List;

import androidx.lifecycle.LiveData;

import comv.example.zyrmj.precious_time01.dao.HabitCategoryDao;
import comv.example.zyrmj.precious_time01.dao.HabitDao;
import comv.example.zyrmj.precious_time01.dao.HabitQuoteDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.relations.HabitCategory;
import comv.example.zyrmj.precious_time01.entity.relations.HabitQuote;

public class HabitRepository {
    private HabitCategoryDao habitCategoryDao;
    private HabitDao habitDao;
    private HabitQuoteDao habitQuoteDao;

    public HabitRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context.getApplicationContext());
        habitDao = appDatabase.habitDao();
        habitQuoteDao=appDatabase.habitQuoteDao();

    }

    static class InsertHabitQuoteAsyncTask extends AsyncTask<HabitQuote, Void, Void> {
       private HabitQuoteDao habitQuoteDao;

        public InsertHabitQuoteAsyncTask(HabitQuoteDao habitQuoteDao) {
            this.habitQuoteDao = habitQuoteDao;
        }

        @Override
        protected Void doInBackground(HabitQuote... habitQuotes) {
           habitQuoteDao.insert(habitQuotes);
           return null;
        }
    }
    static class InsertHabitAsyncTask extends AsyncTask<Habit,Void,Void>
    {
        private HabitDao habitDao;

        public InsertHabitAsyncTask(HabitDao habitDao) {
            this.habitDao = habitDao;
        }

        @Override
        protected Void doInBackground(Habit... habits) {
            habitDao.insert(habits);
            return null;
        }
    }
    static class DeleteHabitAsyncTask extends AsyncTask<Habit,Void ,Void>
    {
        private HabitDao habitDao;

        public DeleteHabitAsyncTask(HabitDao habitDao) {
            this.habitDao = habitDao;
        }

        @Override
        protected Void doInBackground(Habit... habits) {
            habitDao.deleteHabit(habits);
            return null;
        }
    }

    static class getAllHabitsAsyncTask extends  AsyncTask<String ,Void , LiveData<List<Habit>>>
    {
    private HabitDao habitDao;

        public getAllHabitsAsyncTask(HabitDao habitDao) {
            this.habitDao = habitDao;
        }

        @Override
        protected LiveData<List<Habit>> doInBackground(String... strings) {
            return habitDao.getAllHabits(strings[0]);
        }
    }


    public void insertHabitQuote(HabitQuote...habitQuotes) {
        new InsertHabitQuoteAsyncTask(habitQuoteDao).execute(habitQuotes);
    }
    public void insertHabit(Habit...habits)
    {
        new InsertHabitAsyncTask(habitDao).execute(habits);
    }
    public void deleteHabit(Habit...habits)
    {
        new DeleteHabitAsyncTask(habitDao).execute(habits);
    }

    public LiveData<List<Habit>> getAllHabits(String userId)
    {
        try {
            return new getAllHabitsAsyncTask(habitDao).execute(userId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertHabitCategory(List<HabitCategory>list) {
        new InsertHabitCategoryTask(habitCategoryDao).execute(list);
    }

    static class InsertHabitCategoryTask extends AsyncTask<List<HabitCategory>, Void, Void> {
        private HabitCategoryDao habitCategoryDao;
        InsertHabitCategoryTask(HabitCategoryDao habitCategoryDao) {
            this.habitCategoryDao = habitCategoryDao;
        }

        @Override
        protected Void doInBackground(List<HabitCategory> ...lists) {
            for(int i = 0; i < lists[0].size(); i++) {
                habitCategoryDao.insert(lists[0].get(i));
            }
            return null;
        }
    }
}
