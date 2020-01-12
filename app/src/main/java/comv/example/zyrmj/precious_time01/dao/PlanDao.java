package comv.example.zyrmj.precious_time01.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import comv.example.zyrmj.precious_time01.entity.Plan;

@Dao
public interface PlanDao {
    @Insert
    void insertPlan(Plan ...plans);

    @Query("select * from `Plan` where user_id=:userId ")
    List<Plan> getAllPlans(String userId);
}
