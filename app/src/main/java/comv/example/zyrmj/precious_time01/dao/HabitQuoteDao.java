package comv.example.zyrmj.precious_time01.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import comv.example.zyrmj.precious_time01.entity.relations.HabitQuote;

@Dao
public interface HabitQuoteDao {
    @Insert
    void insert(HabitQuote... habitQuotes);
    @Query("select * from habitquote where user_id=:userId and habit_name=:habitName")
    List<HabitQuote> getQuotesForHabit(String userId, String habitName);
    @Query("delete from habitquote where user_id=:userId and habit_name=:habitName")
    void delete(String userId,String habitName);
}
