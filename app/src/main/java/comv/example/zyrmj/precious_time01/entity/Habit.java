package comv.example.zyrmj.precious_time01.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Entity(primaryKeys = {"user_id","habit_name"},
        foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "user_id"), indices = @Index(value = "user_id"))
public class Habit implements Serializable {
    @NonNull
    @ColumnInfo(name = "user_id")
    public String userId;
    @NotNull
    @ColumnInfo(name="habit_name")
    public String name;

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

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public double getCompletion() {
        return completion;
    }

    public void setCompletion(double completion) {
        this.completion = completion;
    }

    private String length;
    @ColumnInfo(name = "category_name")
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

    public Habit(@NotNull String userId, String name, String length, double completion) {
        this.userId = userId;
        this.name = name;
        this.length = length;
        this.completion = completion;
    }
    @Ignore
    public Habit(){}
}
