package comv.example.zyrmj.precious_time01.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

import java.util.List;

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
    @Query("Select * from TemplateItem WHERE template_name=:templateName AND user_id=:userId" )
    List<TemplateItem>getSpecificTemplateItems(String templateName,String userId);
    @Query("Select * from TemplateItem WHERE template_name=:templateName AND user_id=:userId" )
    LiveData<List<TemplateItem>>getSpecificTemplateItems2(String templateName,String userId);
    @Query("Select * from TemplateItem")
    List<TemplateItem>getAll();

    @Query("Select * from TemplateItem WHERE template_name=:templateName AND user_id=:userId AND start_time like  :week || '%'")
    List<TemplateItem>getSameWeek(String week, String templateName, String userId);

//    @RawQuery("Select * from TemplateItem WHERE start_time like *")
//    List<TemplateItem>getSameWeek(String week);
}
