package comv.example.zyrmj.precious_time01.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import comv.example.zyrmj.precious_time01.entity.Template;

@Dao
public interface TemplateDao {
    @Insert
    void insertTemplate(Template... templates);

    @Delete
    void deleteTemplate(Template... templateItems);

    @Update
    void updateTemplate(Template... templateItems);

    @Query("Select * from Template where user_id=:userId")
    LiveData<List<Template>> getAllTemplates(String userId);

    @Query("Select * from Template where user_id=:userId")
     List<Template>getAllTemplates2(String userId);

    //根据字段查询
    @Query("SELECT * FROM Template WHERE user_id=:userId AND name= :name")
    Template getSpecificTemplate(String userId, String name);

}
