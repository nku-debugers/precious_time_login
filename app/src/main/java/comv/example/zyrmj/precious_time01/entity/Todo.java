package comv.example.zyrmj.precious_time01.entity;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"user_id", "item_name"})
public class Todo implements Serializable {
    @NonNull
    @ColumnInfo(name = "user_id")
    @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id")
    private String userId;
    @NonNull
    @ColumnInfo(name = "item_name")
    private String name;
    @ColumnInfo(name = "category_name")
    @ForeignKey(entity = Category.class, parentColumns = "name", childColumns = "category_name")
    private String categoryName;
    @ColumnInfo(name = "end_time")
    private String endTime;
    @ColumnInfo(name = "start_time")
    private String startTime;
    private String length;
    private Double completion;

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
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

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Double getCompletion() {
        return completion;
    }

    public void setCompletion(Double completion) {
        this.completion = completion;
    }
}
