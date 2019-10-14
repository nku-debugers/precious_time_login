package comv.example.zyrmj.precious_time01.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import comv.example.zyrmj.precious_time01.dao.QuoteDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
import comv.example.zyrmj.precious_time01.entity.Quote;

public class QuoteRepository {
    private QuoteDao quoteDao;
    private LiveData<List<Quote>>allQuotes;

    public LiveData<List<Quote>> getAllQuotes() {
        return allQuotes;
    }

    public QuoteRepository (Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context.getApplicationContext());
        quoteDao = appDatabase.quoteDao();
        allQuotes = quoteDao.getAllQuotes();
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
    static class UpdateAsyncTask extends AsyncTask<Quote, Void, Void> {
        private QuoteDao quoteDao;
        UpdateAsyncTask(QuoteDao quoteDao) {
            this.quoteDao = quoteDao;
        }
        @Override
        protected Void doInBackground(Quote... quotes) {
            quoteDao.updateQuote(quotes);
            return null;
        }
    }

    public void insertQuote(Quote... quotes) {
        new InsertAsyncTask(quoteDao).execute(quotes);
    }
    public void UpdateQuote(Quote... quotes) {
        new UpdateAsyncTask(quoteDao).execute(quotes);
    }
    public void DeleteQuote(Quote... quotes) {
        new DeleteAsyncTask(quoteDao).execute(quotes);
    }
}
