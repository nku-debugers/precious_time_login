package comv.example.zyrmj.precious_time01.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import comv.example.zyrmj.precious_time01.entity.Todo;
import comv.example.zyrmj.precious_time01.entity.relations.TodoCategory;

@Dao
public interface TodoCategoryDao {
    @Insert
    void insert(TodoCategory... todoCategories);
    @Delete
    void delete(TodoCategory... todoCategories);
}
