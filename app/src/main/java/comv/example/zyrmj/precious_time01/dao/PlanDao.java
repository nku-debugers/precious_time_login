package comv.example.zyrmj.precious_time01.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import comv.example.zyrmj.precious_time01.entity.Plan;
@Dao
public interface PlanDao {
    @Insert
    void insert(Plan... plans);

    @Delete
    void delete(Plan... plans);
}
