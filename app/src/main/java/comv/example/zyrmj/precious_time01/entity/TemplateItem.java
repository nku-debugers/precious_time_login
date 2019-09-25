package comv.example.zyrmj.precious_time01.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"user_id", "item_name"})
public class TemplateItem {
    @NonNull
    @ColumnInfo(name = "user_id")
    @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id")
    private String userId;
    @NonNull
    @ColumnInfo(name = "item_name")
    private  String itemName;
    @ColumnInfo(name = "habit_name")
    @ForeignKey(entity = Habit.class, parentColumns = "name", childColumns = "habit_name")
    private  String habitName;
    @ColumnInfo(name = "category_name")
    @ForeignKey(entity = Category.class, parentColumns = "name", childColumns = "category_name")
    private String categoryName;
    @ColumnInfo(name = "end_time")
    private String endTime;
    @ColumnInfo(name = "start_time")
    private String startTime;
}
