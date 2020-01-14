package comv.example.zyrmj.precious_time01.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import comv.example.zyrmj.precious_time01.entity.relations.TodoCategory;

@Dao
public interface TodoCategoryDao {
    @Insert
    void insert(TodoCategory... todoCategories);

    @Query("select * from TodoCategory where user_id=:userId and plan_date=:planDate and start_time=:startTime")
    List<TodoCategory> getCategoriesForTodo(String userId, String planDate, String startTime);

    @Query("delete from TodoCategory where user_id=:userId and plan_date=:planDate and start_time=:startTime")
    void delete(String userId, String planDate, String startTime);
}
