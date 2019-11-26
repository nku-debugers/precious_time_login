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

    @Query("UPDATE QUOTE SET words=:newWords,author=:newAuthor WHERE user_id=:userId AND words=:oldWords")
    void updateQuote(String userId, String oldWords, String newWords, String newAuthor);

    @Delete
    void deleteQuote(Quote... quotes);

    @Query("select * from Quote where user_id=:userId")
    LiveData<List<Quote>> getAllQuotes(String userId);

    @Query("select * from quote where user_id=:userId and words=:words")
    Quote getSpecificQuote(String userId, String words);
}
