package comv.example.zyrmj.precious_time01.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import comv.example.zyrmj.precious_time01.dao.QuoteDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
import comv.example.zyrmj.precious_time01.entity.Quote;

public class QuoteRepository {
    private QuoteDao quoteDao;


    public QuoteRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context.getApplicationContext());
        quoteDao = appDatabase.quoteDao();
    }

    static class InsertAsyncTask extends AsyncTask<Quote, Void, Void> {
        private QuoteDao quoteDao;

        InsertAsyncTask(QuoteDao quoteDao) {
            this.quoteDao = quoteDao;
        }

        @Override
        protected Void doInBackground(Quote... quotes) {
            quoteDao.insertQuote(quotes);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Quote, Void, Void> {
        private QuoteDao quoteDao;

        DeleteAsyncTask(QuoteDao quoteDao) {
            this.quoteDao = quoteDao;
        }

        @Override
        protected Void doInBackground(Quote... quotes) {
            quoteDao.deleteQuote(quotes);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<String, Void, Void> {
        private QuoteDao quoteDao;

        UpdateAsyncTask(QuoteDao quoteDao) {
            this.quoteDao = quoteDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            quoteDao.updateQuote(strings[0], strings[1], strings[2], strings[3]);
            return null;
        }
    }

    static class GetAllQuotesAsyncTask extends AsyncTask<String, Void, LiveData<List<Quote>>> {
        public GetAllQuotesAsyncTask(QuoteDao quoteDao) {
            this.quoteDao = quoteDao;
        }

        private QuoteDao quoteDao;

        @Override
        protected LiveData<List<Quote>> doInBackground(String... strings) {
            return quoteDao.getAllQuotes(strings[0]);
        }
    }

    static class GetSpecifcAsyncTask extends AsyncTask<String, Void, Quote> {
        private QuoteDao quoteDao;

        public GetSpecifcAsyncTask(QuoteDao quoteDao) {
            this.quoteDao = quoteDao;
        }

        @Override
        protected Quote doInBackground(String... strings) {
            return quoteDao.getSpecificQuote(strings[0], strings[1]);
        }
    }

    public void insertQuote(Quote... quotes) {
        new InsertAsyncTask(quoteDao).execute(quotes);
    }

    public void UpdateQuote(String... strings) {
        new UpdateAsyncTask(quoteDao).execute(strings);
    }

    public void DeleteQuote(Quote... quotes) {
        new DeleteAsyncTask(quoteDao).execute(quotes);
    }

    public LiveData<List<Quote>> getAllQuotes(String userId) {
        try {
            return new GetAllQuotesAsyncTask(quoteDao).execute(userId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Quote getSpecificQuote(String... strings) {
        try {
            return new GetSpecifcAsyncTask(quoteDao).execute(strings).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
