package comv.example.zyrmj.precious_time01.repository;

import android.content.Context;
import android.os.AsyncTask;


import comv.example.zyrmj.precious_time01.dao.HabitDao;
import comv.example.zyrmj.precious_time01.dao.HabitQuoteDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
import comv.example.zyrmj.precious_time01.entity.relations.HabitQuote;

public class HabitRepository {
    private HabitDao habitDao;
    private HabitQuoteDao habitQuoteDao;

    public HabitRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context.getApplicationContext());
        habitDao = appDatabase.habitDao();
        habitQuoteDao=appDatabase.habitQuoteDao();

    }

    static class InsertAsyncTask extends AsyncTask<HabitQuote, Void, Void> {
       private HabitQuoteDao habitQuoteDao;

        public InsertAsyncTask(HabitQuoteDao habitQuoteDao) {
            this.habitQuoteDao = habitQuoteDao;
        }

        @Override
        protected Void doInBackground(HabitQuote... habitQuotes) {
           habitQuoteDao.insert(habitQuotes);
           return null;
        }
    }

    public void insertHabitQuote(HabitQuote...habitQuotes) {
        new InsertAsyncTask(habitQuoteDao).execute(habitQuotes);
    }

}
