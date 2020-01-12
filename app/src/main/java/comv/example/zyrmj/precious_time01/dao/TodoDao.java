package comv.example.zyrmj.precious_time01.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import comv.example.zyrmj.precious_time01.entity.Todo;

@Dao
public interface TodoDao {
    @Insert
    void insert(Todo... todos);
    @Delete
    void delete(Todo... todos);
}
