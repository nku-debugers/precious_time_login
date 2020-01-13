package comv.example.zyrmj.precious_time01.repository;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;

import comv.example.zyrmj.precious_time01.dao.HabitCategoryDao;
import comv.example.zyrmj.precious_time01.dao.HabitDao;
import comv.example.zyrmj.precious_time01.dao.HabitQuoteDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
import comv.example.zyrmj.precious_time01.entity.Category;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.Quote;
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
        habitCategoryDao=appDatabase.habitCategoryDao();

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

    static class getAllHabitsAsyncTask2 extends  AsyncTask<String ,Void ,List<Habit>>
    {
        private HabitDao habitDao;

        public getAllHabitsAsyncTask2(HabitDao habitDao) {
            this.habitDao = habitDao;
        }

        @Override
        protected List<Habit> doInBackground(String... strings) {
            return habitDao.getAllHabits2(strings[0]);
        }
    }

    static class getSpecificHabitAsynckTask extends  AsyncTask<String ,Void ,Habit>
    {
        private HabitDao habitDao;

        public getSpecificHabitAsynckTask(HabitDao habitDao) {
            this.habitDao = habitDao;
        }

        @Override
        protected Habit doInBackground(String... strings) {
            return habitDao.getSpecificHabit(strings[0],strings[1]);
        }
    }

    static class getCategories extends  AsyncTask<String ,Void ,List<String>>
    {
        private HabitCategoryDao habitCategoryDao;

        public getCategories(HabitCategoryDao habitCategoryDao) {
            this.habitCategoryDao = habitCategoryDao;
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            List<String> categories=new ArrayList<>();
            List<HabitCategory> habitCategories=habitCategoryDao.getCategoriesForHabit(strings[0],strings[1]);;
            for(HabitCategory hc:habitCategories)
            {
                categories.add(hc.getCategory());
            }
            return categories;
        }
    }

    static class getQuotes extends  AsyncTask<String ,Void ,List<String>>
    {
        private HabitQuoteDao habitQuoteDao;

        public getQuotes(HabitQuoteDao habitQuoteDao) {
            this.habitQuoteDao = habitQuoteDao;
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            List<String> quotes=new ArrayList<>();
            List<HabitQuote> habitQuote=habitQuoteDao.getQuotesForHabit(strings[0],strings[1]);;
            for(HabitQuote hq:habitQuote)
            {
                quotes.add(hq.getWords());
            }
            return quotes;
        }
    }

    static class getAllQuotes extends  AsyncTask<String ,Void ,List<Quote>>
    {
        private HabitQuoteDao habitQuoteDao;

        public getAllQuotes(HabitQuoteDao habitQuoteDao) {
            this.habitQuoteDao = habitQuoteDao;
        }

        @Override
        protected List<Quote> doInBackground(String... strings) {
            List<Quote> quotes=new ArrayList<>();
            List<HabitQuote> habitQuote=habitQuoteDao.getQuotesForHabit(strings[0],strings[1]);
            for(HabitQuote hq:habitQuote)
            {
                Quote quote = new Quote(hq.getUserId(), hq.getWords(), "-1");
                quotes.add(quote);
            }
            return quotes;
        }
    }

    static class deleteCategories extends AsyncTask<String ,Void,Void>
    {
        private HabitCategoryDao habitCategoryDao;

        public deleteCategories(HabitCategoryDao habitCategoryDao) {
            this.habitCategoryDao = habitCategoryDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            habitCategoryDao.delete(strings[0],strings[1]);
            return null;
        }
    }

    static class deleteQuotes extends AsyncTask<String ,Void,Void>
    {
        private HabitQuoteDao habitQuoteDao;

        public deleteQuotes(HabitQuoteDao habitQuoteDao) {
            this.habitQuoteDao = habitQuoteDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            habitQuoteDao.delete(strings[0],strings[1]);
            return null;
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
public List<Habit> getAllHabits2(String userId)
{
    try {
        List<Habit> habits=new getAllHabitsAsyncTask2(habitDao).execute(userId).get();
        return habits;
    } catch (Exception e) {
        e.printStackTrace();
    }
return null;
}
    public Habit getSpecificHabit(String userId,String habitName)
    {
        try {
            Habit result=new getSpecificHabitAsynckTask(habitDao).execute(userId,habitName).get();
            Log.d("result is null",String.valueOf(result==null));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("result is null",String.valueOf(true));
        return null;
    }

    public List<String> getCategories(String userId,String habitName)
    {
        try {
            return new getCategories(habitCategoryDao).execute(userId,habitName).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
return null;
    }

    public List<String> getQuotes(String userId,String habitName) {

        try {
            return new getQuotes(habitQuoteDao).execute(userId,habitName).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Quote> getAllQuotes(String userId, String habitName) {
        try {
            return new getAllQuotes(habitQuoteDao).execute(userId, habitName).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertHabitCategory(HabitCategory...habitCategories) {
        new InsertHabitCategoryTask(habitCategoryDao).execute(habitCategories);
    }

    static class InsertHabitCategoryTask extends AsyncTask<HabitCategory, Void, Void> {
        private HabitCategoryDao habitCategoryDao;
        InsertHabitCategoryTask(HabitCategoryDao habitCategoryDao) {
            this.habitCategoryDao = habitCategoryDao;
        }

        @Override
        protected Void doInBackground(HabitCategory...habitCategories) {
         habitCategoryDao.insert(habitCategories);
            return null;
        }
    }

    public void deleteHabitCategory(String ...strings)
    {
        new deleteCategories(habitCategoryDao).execute(strings);
    }

    public void deleteHabitQuote(String ...strings)
    {
        new deleteQuotes(habitQuoteDao).execute(strings);
    }

}
