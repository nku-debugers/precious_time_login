package comv.example.zyrmj.precious_time01.entity.relations;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import comv.example.zyrmj.precious_time01.entity.User;

@Entity(primaryKeys = {"user_id", "category_name", "start_time", "plan_date"},
        foreignKeys = @ForeignKey(entity = User.class,
                                parentColumns = "id",
                                childColumns = "user_id"),
        indices = @Index(value = "user_id"))

public class TodoCategory {
    @NonNull
    @ColumnInfo(name = "user_id")
    private String userId;

    @NonNull
    @ColumnInfo(name = "category_name")
    private String category;

    @NonNull
    @ColumnInfo(name = "start_time")
    private String startTime;

    @NonNull
    @ColumnInfo(name = "plan_date")
    private String planDate;

    @NonNull
    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(@NonNull String planDate) {
        this.planDate = planDate;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    @NonNull
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(@NonNull String startTime) {
        this.startTime = startTime;
    }

    public TodoCategory(@NonNull String userId, @NonNull String category, @NonNull String startTime, @NonNull String planDate) {
        this.userId = userId;
        this.category = category;
        this.startTime = startTime;
        this.planDate = planDate;
    }
}
