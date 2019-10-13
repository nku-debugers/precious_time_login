package comv.example.zyrmj.precious_time01.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;

@Dao
public interface TemplateItemDao {
    @Insert
    void insertTemplateItem(TemplateItem... templateItems);
    @Delete
    void deleteTemplateItem(TemplateItem... templateItems);
    @Update
    void updateTemplateItem(TemplateItem... templateItems);
    @Query("Select * from TemplateItem")
    LiveData<List<TemplateItem>>getAllTemplateItems();
}
