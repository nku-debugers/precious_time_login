package comv.example.zyrmj.precious_time01.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "name",
        childColumns = "category_name"), indices = @Index(value = "category_name"))
public class Habit {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    public String userId;
    public String name;
    @ColumnInfo(name = "end_time")
    private String endTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getCompletion() {
        return completion;
    }

    public void setCompletion(double completion) {
        this.completion = completion;
    }

    @ColumnInfo(name = "start_time")
    private String startTime;
    private String length;
    @ColumnInfo(name = "category_name")
    public String category;
    public double completion;

    public Habit(String userId, String name, String endTime, String startTime,
                 String length, String category, double completion) {
        this.userId = userId;
        this.name = name;
        this.endTime = endTime;
        this.startTime = startTime;
        this.length = length;
        this.category = category;
        this.completion = completion;
    }
}
