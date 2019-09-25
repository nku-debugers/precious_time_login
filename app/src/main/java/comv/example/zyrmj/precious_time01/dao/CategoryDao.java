package comv.example.zyrmj.precious_time01.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import comv.example.zyrmj.precious_time01.entity.Category;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category category);
}
