
package comv.example.zyrmj.precious_time01.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import androidx.lifecycle.LiveData;

import java.util.List;

import comv.example.zyrmj.precious_time01.dao.TemplateItemDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;

public class TemplateItemRepository {

    private LiveData<List<TemplateItem>>allTemplateItems;
    private TemplateItemDao templateItemDao;
    private static String TAG = "newjk";

    public TemplateItemRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context.getApplicationContext());
        templateItemDao = appDatabase.templateItemDao();
        allTemplateItems = templateItemDao.getAllTemplateItems();
    }

    public LiveData<List<TemplateItem>> getAllTemplateItems() {
        return allTemplateItems;
    }

    public void insertTemplateItems(TemplateItem... templateItems) {
        Log.d("mytag", "inside insert fuc");

        try {
            new InsertAsyncTask(templateItemDao).execute(templateItems).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer ifTimeConfilict(String week, String start) {
        try {
            return new FindByTime(week, start, templateItemDao).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateTemplateItems(TemplateItem... templateItems) {
        new UpdateAsyncTask(templateItemDao).execute(templateItems);
    }
    public void deleteTemplateItems(TemplateItem... templateItems) {
        new DeleteAsyncTask(templateItemDao).execute(templateItems);
    }
    public List<TemplateItem> getAll() {
        try {
            return new GetList(templateItemDao).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<TemplateItem> getSpecificList(String templateName,String userId)
    {
        try {
            return new GetSpecificList(templateItemDao).execute(templateName,userId).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
return null;
    }
    public LiveData<List<TemplateItem>> getSpecificList2(String templateName,String userId)
    {

        return templateItemDao.getSpecificTemplateItems2(templateName,userId);
    }

    static class GetSpecificList extends AsyncTask<String,Void,List<TemplateItem>>
    { private TemplateItemDao templateItemDao;
        private GetSpecificList(TemplateItemDao templateItemDao) {
            this.templateItemDao = templateItemDao;
        }

        @Override
        protected List<TemplateItem> doInBackground(String... strings) {
            return templateItemDao.getSpecificTemplateItems(strings[0],strings[1]);
            //string[0] templateName string[1] userId
        }
    }



    static class FindByTime extends AsyncTask<Void, Void, Integer> {
        String week;
        String startTime;
        private TemplateItemDao templateItemDao;
        FindByTime(String k, String start, TemplateItemDao templateItemDao){
            super();
            week = k;
            this.startTime = start;
            this.templateItemDao = templateItemDao;
        }
        @Override
        protected Integer doInBackground(Void... voids) {
            List<TemplateItem> items = templateItemDao.getSameWeek(week);
            Log.d("ddf", "doInBackground: the week is: "+week);
            if (items.size() == 0) {
                Log.d("ddf", "nothing found");
                return 1;
            }
            for (int i = 0; i < items.size(); i++) {
                Log.d("ddf", "mytime is : " + startTime);
                Log.d("ddf", "the comp is : " + items.get(i).getStartTime());
                Log.d("ddf", "the ecomp is : " + items.get(i).getEndTime());
                if (startTime.compareTo(items.get(i).getStartTime()) > 0
                        && startTime.compareTo(items.get(i).getEndTime()) < 0) {
                    Log.d("ddf", "thre is a confilit");
                    return 0;
                }
            }
            Log.d("ddf", "is , but no blank");
            return 1;
        }
    }

    static class GetList extends  AsyncTask<Void, Void, List<TemplateItem>> {
        private TemplateItemDao templateItemDao;
        private GetList(TemplateItemDao templateItemDao) {
            this.templateItemDao = templateItemDao;
        }


        @Override
        protected List<TemplateItem> doInBackground(Void... voids) {
            return templateItemDao.getAll();
        }
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
