package comv.example.zyrmj.precious_time01.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.entity.User;

@Dao
public interface TemplateDao {
    @Insert
    void insertTemplate(Template... templates);
    @Delete
    void deleteTemplate(Template... templateItems);
    @Update
    void updateTemplate(Template... templateItems);
    @Query("Select * from Template")
    LiveData<List<Template>> getAllTemplates();
    //根据字段查询
    @Query("SELECT * FROM Template WHERE name= :name AND user_id= :userId")
    Template getSpecificTemplate(String name,String userId);

}
