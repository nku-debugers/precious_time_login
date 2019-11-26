
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

    public Integer ifTimeConfilict(String ... strings) {
        try {
            return new FindByTime(templateItemDao).execute(strings).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateTemplateItems(String... strings) {
        new UpdateAsyncTask(templateItemDao).execute(strings);
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



    static class FindByTime extends AsyncTask<String, Void, Integer> {
        private TemplateItemDao templateItemDao;
        FindByTime(TemplateItemDao templateItemDao){
            super();
            this.templateItemDao = templateItemDao;
        }
        @Override
        protected Integer doInBackground(String... strings) {
            //String 1 week 2 startTime 3 templateName 4 userID,
            List<TemplateItem> items = templateItemDao.getSameWeek(strings[0], strings[2], strings[3]);
            if (items.size() == 0) {
                return 1;
            }
            if(strings[4].equals("start")) {
                if (strings.length == 5) {
                    for (int i = 0; i < items.size(); i++) {
                        if (strings[1].compareTo(items.get(i).getStartTime()) >= 0
                                && strings[1].compareTo(items.get(i).getEndTime()) < 0) {
                            return 0;
                        }
                    }
                } else {
                    for (int i = 0; i < items.size(); i++) {
                        if (strings[4].equals(items.get(i).getStartTime())) {
                            continue;
                        }
                        if (strings[1].compareTo(items.get(i).getStartTime()) > 0
                                && strings[1].compareTo(items.get(i).getEndTime()) < 0) {
                            return 0;
                        }
                    }

                }
            }
            else
            {
                if (strings.length == 5) {
                    for (int i = 0; i < items.size(); i++) {
                        if (strings[1].compareTo(items.get(i).getStartTime()) > 0
                                && strings[1].compareTo(items.get(i).getEndTime()) < 0) {
                            return 0;
                        }
                    }
                } else {
                    for (int i = 0; i < items.size(); i++) {
                        if (strings[4].equals(items.get(i).getStartTime())) {
                            continue;
                        }
                        if (strings[1].compareTo(items.get(i).getStartTime()) > 0
                                && strings[1].compareTo(items.get(i).getEndTime()) <=0) {
                            return 0;
                        }
                    }

                }

            }
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

    static class UpdateAsyncTask extends AsyncTask<String, Void, Void> {
        private TemplateItemDao templateItemDao;

        private UpdateAsyncTask(TemplateItemDao templateItemDao) {
            this.templateItemDao = templateItemDao;
        }


        @Override
        protected Void doInBackground(String... strings) {
            templateItemDao.updateTemplateItem(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5]);
            return null;
        }
    }
}
