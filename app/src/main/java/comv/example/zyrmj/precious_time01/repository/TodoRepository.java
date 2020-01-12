package comv.example.zyrmj.precious_time01.repository;

import android.content.Context;
import android.os.AsyncTask;

import comv.example.zyrmj.precious_time01.dao.TodoCategoryDao;
import comv.example.zyrmj.precious_time01.dao.TodoDao;
import comv.example.zyrmj.precious_time01.dao.TodoQuoteDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
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
        public insertTodoQuoteAsyncTask (TodoQuoteDao todoQuoteDao) {
            this.todoQuoteDao = todoQuoteDao;
        }
        @Override
        protected Void doInBackground(TodoQuote... todoQuotes) {
            todoQuoteDao.insert(todoQuotes);
            return null;
        }
    }
}
