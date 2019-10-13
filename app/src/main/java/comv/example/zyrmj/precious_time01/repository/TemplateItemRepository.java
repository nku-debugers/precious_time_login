package comv.example.zyrmj.precious_time01.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import comv.example.zyrmj.precious_time01.dao.TemplateItemDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;

public class TemplateItemRepository {
    private LiveData<List<TemplateItem>>allTemplateItems;
    private TemplateItemDao templateItemDao;
    public TemplateItemRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context.getApplicationContext());
        templateItemDao = appDatabase.templateItemDao();
        allTemplateItems = templateItemDao.getAllTemplateItems();
    }

    public LiveData<List<TemplateItem>> getAllTemplateItems() {
        return allTemplateItems;
    }

    public void insertTemplateItems(TemplateItem... templateItems) {
        new InsertAsyncTask(templateItemDao).execute(templateItems);
    }
    public void updateTemplateItems(TemplateItem... templateItems) {
        new UpdateAsyncTask(templateItemDao).execute(templateItems);
    }
    public void deleteTemplateItems(TemplateItem... templateItems) {
        new DeleteAsyncTask(templateItemDao).execute(templateItems);
    }

    static class InsertAsyncTask extends AsyncTask<TemplateItem, Void, Void> {
        private TemplateItemDao templateItemDao;

        private InsertAsyncTask(TemplateItemDao templateItemDao) {
            this.templateItemDao = templateItemDao;
        }

        @Override
        protected Void doInBackground(TemplateItem... templateItems) {
            templateItemDao.insertTemplateItem(templateItems);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<TemplateItem, Void, Void> {
        private TemplateItemDao templateItemDao;

        private DeleteAsyncTask(TemplateItemDao templateItemDao) {
            this.templateItemDao = templateItemDao;
        }

        @Override
        protected Void doInBackground(TemplateItem... templateItems) {
            templateItemDao.deleteTemplateItem(templateItems);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<TemplateItem, Void, Void> {
        private TemplateItemDao templateItemDao;

        private UpdateAsyncTask(TemplateItemDao templateItemDao) {
            this.templateItemDao = templateItemDao;
        }

        @Override
        protected Void doInBackground(TemplateItem... templateItems) {
            templateItemDao.updateTemplateItem(templateItems);
            return null;
        }
    }
}
