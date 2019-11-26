package comv.example.zyrmj.precious_time01.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "name",
        childColumns = "category_name"),indices=@Index(value = "category_name"))
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

    public String location;     //执行地点
    public int priority;        //优先级
    public int reminder;        //提醒
    private String time4once;     //单次时长

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

    public String getTime4once() {
        return time4once;
    }

    public void setTime4once(String time4once) {
        this.time4once = time4once;
    }

    public int getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(int expectedTime) {
        this.expectedTime = expectedTime;
    }

    @ColumnInfo(name = "expected_time")
    public int expectedTime;    //希望安排时间

    public Habit(@NotNull String userId, String name, String endTime, String startTime,
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
