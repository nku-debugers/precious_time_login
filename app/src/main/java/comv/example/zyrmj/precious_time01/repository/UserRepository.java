package comv.example.zyrmj.precious_time01.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import comv.example.zyrmj.precious_time01.dao.UserDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
import comv.example.zyrmj.precious_time01.entity.User;


public class UserRepository {
    private LiveData<List<User>> allUsers;
    private UserDao userDao;

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public UserRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context.getApplicationContext());
        userDao = appDatabase.userDao();
        allUsers = userDao.getAllUsers();
    }

    static class InsertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        InsertAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insertUser(users);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        UpdateAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.updateUser(users);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        DeleteAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.deleteUser(users);
            return null;
        }
    }

    public void insertUsers(User... users) {
        new InsertAsyncTask(userDao).execute(users);
    }

    public void updateUsers(User... users) {
        new UpdateAsyncTask(userDao).execute(users);
    }

    public void DeleteUsers(User... users) {
        new DeleteAsyncTask(userDao).execute(users);
    }
}
