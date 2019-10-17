package comv.example.zyrmj.precious_time01.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import comv.example.zyrmj.precious_time01.dao.TemplateDao;
import comv.example.zyrmj.precious_time01.dao.TemplateItemDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
import comv.example.zyrmj.precious_time01.entity.Template;

public class TemplateRepository {
    private LiveData<List<Template>> allTemplates;
    private TemplateDao templateDao;

    public LiveData<List<Template>> getAllTemplates() {
        return allTemplates;
    }

    public TemplateRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context.getApplicationContext());
        templateDao =  appDatabase.templateDao();
        allTemplates = templateDao.getAllTemplates();
    }
  static class SelectAsyncTask extends AsyncTask<String,Void ,Template>
  {
      private TemplateDao templateDao;

      OnDataFinishedListener onDataFinishedListener;


      private SelectAsyncTask(TemplateDao templateDao) {
          this.templateDao = templateDao;
      }

      public void setOnDataFinishedListener(
              OnDataFinishedListener onDataFinishedListener) {
          this.onDataFinishedListener = onDataFinishedListener;
      }
      //回调接口：
      public interface OnDataFinishedListener {

          public void onDataSuccessfully(Template template);

          public void onDataFailed();

      }
          @Override
      protected Template doInBackground(String... strings) {
          String name=strings[0];
          String userId=strings[1];
          Template template=templateDao.getSpecificTemplate(name,userId);
         return template;
      }

      @Override
      protected void onPostExecute(Template template) {
          super.onPostExecute(template);
          if(template!=null){
              onDataFinishedListener.onDataSuccessfully(template);
          }else{
              onDataFinishedListener.onDataFailed();
          }

      }
  }
    static class InsertAsyncTask extends AsyncTask<Template, Void, Void> {
        private TemplateDao templateDao;

        private InsertAsyncTask(TemplateDao templateDao) {
            this.templateDao = templateDao;
        }
        @Override
        protected Void doInBackground(Template... templates) {
            templateDao.insertTemplate(templates);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Template, Void, Void> {
        private TemplateDao templateDao;

        private UpdateAsyncTask(TemplateDao templateDao) {
            this.templateDao = templateDao;
        }
        @Override
        protected Void doInBackground(Template... templates) {
            templateDao.updateTemplate(templates);
            return null;
        }


    }

    static class DeleteAsyncTask extends AsyncTask<Template, Void, Void> {
        private TemplateDao templateDao;

        private DeleteAsyncTask(TemplateDao templateDao) {
            this.templateDao = templateDao;
        }
        @Override
        protected Void doInBackground(Template... templates) {
            templateDao.deleteTemplate(templates);
            return null;
        }
    }

    public void insertTemplates(Template... templates) {
        new InsertAsyncTask(templateDao).execute(templates);
    }
    public void updateTemplates(Template... templates) {
        new UpdateAsyncTask(templateDao).execute(templates);
    }
    public void deleteTemplates(Template... templates) {
        new DeleteAsyncTask(templateDao).execute(templates);
    }
}
