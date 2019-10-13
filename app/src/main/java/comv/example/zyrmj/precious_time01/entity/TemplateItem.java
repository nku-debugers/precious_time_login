package comv.example.zyrmj.precious_time01.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"user_id", "item_name"}, foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id"),
        @ForeignKey(entity = Template.class, parentColumns = "name", childColumns = "template_name")})
public class TemplateItem {
    @NonNull
    @ColumnInfo(name = "user_id")
    @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id")
    private String userId;
    @NonNull
    @ColumnInfo(name = "item_name")
    private  String itemName;
    @ColumnInfo(name = "template_name")
    @ForeignKey(entity = Template.class, parentColumns = "name", childColumns = "template_name")
    private  String templateName;
    @ColumnInfo(name = "category_name")
    @ForeignKey(entity = Category.class, parentColumns = "name", childColumns = "category_name")
    private String categoryName;
    @ColumnInfo(name = "end_time")
    private String endTime;
    @ColumnInfo(name = "start_time")
    private String startTime;

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getItemName() {
        return itemName;
    }

    public void setItemName(@NonNull String itemName) {
        this.itemName = itemName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public TemplateItem(@NonNull String userId, @NonNull String itemName, String templateName,
                        String categoryName, String endTime, String startTime) {
        this.userId = userId;
        this.itemName = itemName;
        this.templateName = templateName;
        this.categoryName = categoryName;
        this.endTime = endTime;
        this.startTime = startTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
