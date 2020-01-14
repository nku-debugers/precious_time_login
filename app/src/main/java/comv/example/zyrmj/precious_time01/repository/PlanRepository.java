package comv.example.zyrmj.precious_time01.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import comv.example.zyrmj.precious_time01.dao.PlanDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
import comv.example.zyrmj.precious_time01.entity.Plan;


public class PlanRepository {
    private PlanDao planDao;

    public PlanRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context.getApplicationContext());
        planDao=appDatabase.planDao();
    }

    static class InsertPlanAsyncTask extends AsyncTask<Plan, Void, Void> {
        private PlanDao planDao;

        public InsertPlanAsyncTask(PlanDao planDao) {
            this.planDao = planDao;
        }

        @Override
        protected Void doInBackground(Plan ...plans) {
           planDao.insertPlan(plans);
            return null;
        }
    }

    static class DeletePlanAsyncTask extends AsyncTask<Plan, Void, Void> {
        private PlanDao planDao;

        public DeletePlanAsyncTask(PlanDao planDao) {
            this.planDao = planDao;
        }

        @Override
        protected Void doInBackground(Plan ...plans) {
            planDao.deletePlan(plans);
            return null;
        }
    }


    static class getAllPlansAsyncTask extends  AsyncTask<String,Void, List<Plan>>
    {
        private PlanDao planDao;

        public getAllPlansAsyncTask(PlanDao planDao) {
            this.planDao = planDao;
        }

        @Override
        protected List<Plan> doInBackground(String...strings) {
            List<Plan> plans=new ArrayList<>();
            plans=planDao.getAllPlans(strings[0]);
            return plans;
        }
    }

    public void insertPlan(Plan...plans)
    {
        new InsertPlanAsyncTask(planDao).execute(plans);
    }
    public void deletePlan(Plan...plans)
    {
        new DeletePlanAsyncTask(planDao).execute(plans);
    }

    public List<Plan> getAllPlans(String userId)
    {

        try {
            List<Plan> allPlans=new ArrayList<>();
          allPlans=new getAllPlansAsyncTask(planDao).execute(userId).get();
           return allPlans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
