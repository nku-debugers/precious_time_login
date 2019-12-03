package comv.example.zyrmj.precious_time01.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import comv.example.zyrmj.precious_time01.entity.relations.HabitQuote;

@Dao
public interface HabitQuoteDao {
    @Insert
    void insert(HabitQuote... habitQuotes);
}
