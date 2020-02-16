package comv.example.zyrmj.precious_time01.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import comv.example.zyrmj.precious_time01.dao.TodoCategoryDao;
import comv.example.zyrmj.precious_time01.dao.TodoDao;
import comv.example.zyrmj.precious_time01.dao.TodoQuoteDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.Todo;
import comv.example.zyrmj.precious_time01.entity.relations.TodoCategory;
import comv.example.zyrmj.precious_time01.entity.relations.TodoQuote;

public class TodoRepository {
    private TodoDao todoDao;
    private TodoQuoteDao todoQuoteDao;
    private TodoCategoryDao todoCategoryDao;

    public TodoRepository (Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context.getApplicationContext());
        todoDao = appDatabase.todoDao();
        todoCategoryDao = appDatabase.todoCategoryDao();
        todoQuoteDao = appDatabase.todoQuoteDao();
    }

    public void insertTodo(Todo... todos) {
        new insertTodoAsyncTask(todoDao).execute(todos);
    }
    public void insertTodoCategory(TodoCategory... todoCategories) {
        new insertTodoCategoryAsyncTask(todoCategoryDao).execute(todoCategories);
    }
    public void insertTodoQuote(TodoQuote... todoQuotes) {
        new insertTodoQuoteAsyncTask(todoQuoteDao).execute(todoQuotes);
    }
    public void deleteTodo(Todo... todos) {
        new deleteTodoAsyncTask(todoDao).execute(todos);
    }
    public void updateTodo(Todo...todos){ new updateTodoAsyncTask(todoDao).execute(todos);}
    public LiveData<List<Todo>> getLiveTodoByPlanDate(String userId, String planDate) {
        try {
            return new getLiveTodoByPlanDateAsyncTask(todoDao).execute(userId, planDate).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    public List<Todo> getListTodoByPlanDate(String userId, String planDate) {
        try {
            return new getListTodoByPlanDateAsyncTask(todoDao).execute(userId, planDate).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Todo getSpecificTodo(String userId, String planDate, String startTime) {
        try {
            return new getSpecificTodoAsyncTask(todoDao).execute(userId, planDate, startTime).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<String> getCategories(String userId, String planDate, String startTime) {
        try {
            return new getCategoriesAsyncTask(todoCategoryDao).execute(userId, planDate, startTime).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<String> getQuotes(String userId, String planDate, String startTime) {
        try {
            return new getQuoteAsyncTask(todoQuoteDao).execute(userId, planDate, startTime).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Quote> getRealQuote(String userId, String planDate, String startTime) {
        try {
            return new getRealQuoteAsyncTask(todoQuoteDao).execute(userId, planDate, startTime).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void deleteCategory(String userId, String planDate, String startTime) {
        new deleteCategoryAsyncTask(todoCategoryDao).execute(userId, planDate, startTime);
    }
    public void deleteQuote(String userId, String planDate, String startTime) {
        new deleteQuoteAsyncTask(todoQuoteDao).execute(userId, planDate, startTime);
    }

    static class insertTodoAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao todoDao;
        public insertTodoAsyncTask (TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.insert(todos);
            return null;
        }
    }

    static class insertTodoCategoryAsyncTask extends AsyncTask<TodoCategory, Void, Void> {
        private TodoCategoryDao todoCategoryDao;

        public insertTodoCategoryAsyncTask(TodoCategoryDao todoCategoryDao) {
            this.todoCategoryDao = todoCategoryDao;
        }
        @Override
        protected Void doInBackground(TodoCategory... todoCategories) {
            todoCategoryDao.insert(todoCategories);
            return null;
        }
    }

    static class insertTodoQuoteAsyncTask extends AsyncTask<TodoQuote, Void, Void> {
        private TodoQuoteDao todoQuoteDao;
        insertTodoQuoteAsyncTask (TodoQuoteDao todoQuoteDao) {
            this.todoQuoteDao = todoQuoteDao;
        }
        @Override
        protected Void doInBackground(TodoQuote... todoQuotes) {
            todoQuoteDao.insert(todoQuotes);
            return null;
        }
    }

    static class deleteTodoAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao todoDao;

        deleteTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }
        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.delete(todos);
            return null;
        }
    }

    static class updateTodoAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao todoDao;

        updateTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }
        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.update(todos);
            return null;
        }
    }


    static class getLiveTodoByPlanDateAsyncTask extends AsyncTask<String, Void, LiveData<List<Todo>>> {
        private TodoDao todoDao;
        getLiveTodoByPlanDateAsyncTask(TodoDao todoDao){
            this.todoDao = todoDao;
        }
        @Override
        protected LiveData<List<Todo>> doInBackground(String... strings) {
            return todoDao.getLiveTodoByPlanDate(strings[0], strings[1]);
        }
    }

    static class getListTodoByPlanDateAsyncTask extends AsyncTask<String, Void, List<Todo>> {
        private TodoDao todoDao;
        getListTodoByPlanDateAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }
        @Override
        protected List<Todo> doInBackground(String... strings) {
            return todoDao.getListTodoByPlanDate(strings[0], strings[1]);
        }
    }

    static class getSpecificTodoAsyncTask extends AsyncTask<String, Void, Todo> {
        private TodoDao todoDao;
        getSpecificTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }
        @Override
        protected Todo doInBackground(String... strings) {
            return todoDao.getSpecificTodo(strings[0], strings[1], strings[2]);
        }
    }

    static class getCategoriesAsyncTask extends AsyncTask<String, Void, List<String>> {
        private TodoCategoryDao todoCategoryDao;
        getCategoriesAsyncTask(TodoCategoryDao todoCategoryDao) {
            this.todoCategoryDao = todoCategoryDao;
        }
        @Override
        protected List<String> doInBackground(String... strings) {
            List<String> categories = new ArrayList<>();
            List<TodoCategory> todoCategories = todoCategoryDao.getCategoriesForTodo(strings[0], strings[1], strings[2]);
            for(TodoCategory todoCategory:todoCategories) {
                categories.add(todoCategory.getCategory());
            }
            return categories;
        }
    }

    static class getQuoteAsyncTask extends AsyncTask<String, Void, List<String>> {
        private TodoQuoteDao todoQuoteDao;
        getQuoteAsyncTask(TodoQuoteDao todoQuoteDao) {
            this.todoQuoteDao = todoQuoteDao;
        }
        @Override
        protected List<String> doInBackground(String... strings) {
            List<String> quotes = new ArrayList<>();
            List<TodoQuote> todoQuotes = todoQuoteDao.getQuotesForTodo(strings[0], strings[1], strings[2]);
            for(TodoQuote todoQuote:todoQuotes) {
                quotes.add(todoQuote.getWords());
            }
            return quotes;
        }
    }

    static class getRealQuoteAsyncTask extends AsyncTask<String, Void, List<Quote>> {
        private TodoQuoteDao todoQuoteDao;
        getRealQuoteAsyncTask(TodoQuoteDao todoQuoteDao) {
            this.todoQuoteDao = todoQuoteDao;
        }
        @Override
        protected List<Quote> doInBackground(String... strings) {
            List<Quote> quotes = new ArrayList<>();
            List<TodoQuote> todoQuotes = todoQuoteDao.getQuotesForTodo(strings[0], strings[1], strings[2]);
            for(TodoQuote todoQuote:todoQuotes) {
                Quote quote = new Quote(strings[0], todoQuote.getWords(), "-1");
                quotes.add(quote);
            }
            return quotes;
        }
    }

    static class deleteQuoteAsyncTask extends AsyncTask<String, Void, Void> {
        private TodoQuoteDao todoQuoteDao;
        deleteQuoteAsyncTask(TodoQuoteDao todoQuoteDao) {
            this.todoQuoteDao = todoQuoteDao;
        }
        @Override
        protected Void doInBackground(String... strings) {
            todoQuoteDao.delete(strings[0], strings[1], strings[2]);
            return null;
        }
    }



    static class deleteCategoryAsyncTask extends AsyncTask<String, Void, Void> {
        private TodoCategoryDao todoCategoryDao;
        deleteCategoryAsyncTask(TodoCategoryDao todoCategoryDao) {
            this.todoCategoryDao = todoCategoryDao;
        }
        @Override
        protected Void doInBackground(String... strings) {
            todoCategoryDao.delete(strings[0], strings[1], strings[2]);
            return null;
        }
    }
}
