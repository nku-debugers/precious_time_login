package comv.example.zyrmj.precious_time01.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import comv.example.zyrmj.precious_time01.dao.CategoryDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
import comv.example.zyrmj.precious_time01.entity.Category;

public class CategoryRepository {
    private CategoryDao categoryDao;

    public CategoryRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context.getApplicationContext());
        categoryDao = appDatabase.categoryDao();
    }

    static class GetAllAsyncTask extends  AsyncTask<String ,Void ,List<Category>>
    {
        private CategoryDao categoryDao;

        public GetAllAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected List<Category> doInBackground(String... strings) {

            return categoryDao.getAllCategories(strings[0]);
        }
    }

    static class InsertAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao categoryDao;

        InsertAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.insert(categories);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao categoryDao;

        UpdateAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.update(categories);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao categoryDao;

        DeleteAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.delete(categories);
            return null;
        }
    }
    public List<Category> getAllCateories(String userId) {
        try {
            return  new GetAllAsyncTask(categoryDao).execute(userId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void insertCategory(Category... categories) {
        new InsertAsyncTask(categoryDao).execute(categories);
    }

    public void updateCategory(Category... categories) {
        new UpdateAsyncTask(categoryDao).execute(categories);
    }

    public void DeleteCategory(Category... categories) {
        new DeleteAsyncTask(categoryDao).execute(categories);
    }
}
