package comv.example.zyrmj.precious_time01.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import comv.example.zyrmj.precious_time01.entity.relations.TodoQuote;
@Dao
public interface TodoQuoteDao {
    @Insert
    void insert(TodoQuote... todoQuotes);

    @Query("select * from TodoQuote where user_id=:userId and plan_date=:planDate and start_time=:startTime")
    List<TodoQuote> getQuotesForTodo(String userId, String planDate, String startTime);

    @Query("delete from TodoQuote where user_id=:userId and plan_date=:planDate and start_time=:startTime")
    void delete(String userId, String planDate, String startTime);
}
