package comv.example.zyrmj.precious_time01.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import comv.example.zyrmj.precious_time01.dao.QuoteDao;
import comv.example.zyrmj.precious_time01.dao.TemplateDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.Template;

public class TemplateRepository {
    private TemplateDao templateDao;


    public TemplateRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context.getApplicationContext());
        templateDao = appDatabase.templateDao();
    }

    static class getSpecificTemplateTask extends AsyncTask<String, Void, Template> {
        private TemplateDao templateDao;

        public getSpecificTemplateTask(TemplateDao templateDao) {
            this.templateDao = templateDao;
        }

        @Override
        protected Template doInBackground(String... strings) {
            return templateDao.getSpecificTemplate(strings[0], strings[1]);
        }
    }

    static class getAllTemplates2 extends  AsyncTask<String,Void,List<Template>>
    {
        private TemplateDao templateDao;

        public getAllTemplates2(TemplateDao templateDao) {
            this.templateDao = templateDao;
        }

        @Override
        protected List<Template> doInBackground(String... strings) {
            return  templateDao.getAllTemplates2(strings[0]);
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

    static class GetAllTemplatesAsyncTask extends AsyncTask<String, Void, LiveData<List<Template>>> {
        private TemplateDao templateDao;

        public GetAllTemplatesAsyncTask(TemplateDao templateDao) {
            this.templateDao = templateDao;
        }


        @Override
        protected LiveData<List<Template>> doInBackground(String... strings) {
            return templateDao.getAllTemplates(strings[0]);
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

    public Template getSpecificTemplate(String... strings) {
        return templateDao.getSpecificTemplate(strings[0], strings[1]);
    }

    public LiveData<List<Template>> getAllTemplates(String userId) {
        try {
            return new GetAllTemplatesAsyncTask(templateDao).execute(userId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Template> getAllTemplates2(String userId)
    {
        try {
            return  new getAllTemplates2(templateDao).execute(userId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
