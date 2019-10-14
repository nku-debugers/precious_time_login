package comv.example.zyrmj.precious_time01.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import comv.example.zyrmj.precious_time01.entity.Quote;

@Dao
public interface QuoteDao {
    @Insert
    void insertQuote(Quote... quotes);
    @Update
    void updateQuote(Quote... quotes);
    @Delete
    void deleteQuote(Quote... quotes);
    @Query("select * from Quote")
    LiveData<List<Quote>>getAllQuotes();
}
