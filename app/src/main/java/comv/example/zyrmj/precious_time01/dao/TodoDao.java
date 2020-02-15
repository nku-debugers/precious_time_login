package comv.example.zyrmj.precious_time01.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import androidx.room.Update;
import comv.example.zyrmj.precious_time01.entity.Todo;

@Dao
public interface TodoDao {
    @Insert
    void insert(Todo... todos);

    @Update
    void update(Todo...todos);

    @Delete
    void delete(Todo... todos);

    @Query("SELECT * FROM Todo WHERE user_id=:userId and plan_date=:planDate order by start_time")
    LiveData<List<Todo>> getLiveTodoByPlanDate(String userId, String planDate);

    @Query("SELECT * FROM Todo WHERE user_id=:userId and plan_date=:planDate order by start_time")
    List<Todo> getListTodoByPlanDate(String userId, String planDate);

    @Query("SELECT * FROM Todo WHERE user_id=:userId and plan_date=:planDate and start_time=:startTime")
    Todo getSpecificTodo(String userId, String planDate, String startTime);
}
