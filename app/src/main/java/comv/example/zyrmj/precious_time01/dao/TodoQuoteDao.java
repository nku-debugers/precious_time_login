package comv.example.zyrmj.precious_time01.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import comv.example.zyrmj.precious_time01.entity.relations.TodoQuote;
@Dao
public interface TodoQuoteDao {
    @Insert
    void insert(TodoQuote... todoQuotes);
}
