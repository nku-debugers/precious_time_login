package comv.example.zyrmj.precious_time01.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import comv.example.zyrmj.precious_time01.entity.Plan;

@Dao
public interface PlanDao {
    @Insert
    void insertPlan(Plan ...plans);

    @Delete
    void deletePlan(Plan...plans);

    @Query("select * from `Plan` where user_id=:userId order by start_date" )
    List<Plan> getAllPlans(String userId);

    @Query("select * from `Plan` where user_id=:userId and start_date=:startDate")
    Plan getSpecificPlan(String userId,String startDate);
}
