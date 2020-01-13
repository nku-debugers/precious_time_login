package comv.example.zyrmj.precious_time01.entity;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"user_id", "start_date"})
public class Plan implements Serializable {
    @NonNull
    @ColumnInfo(name = "user_id")
    private String userId;

    @ColumnInfo(name = "plan_name")
    private String planName;

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    @NonNull
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(@NonNull String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @ColumnInfo(name = "start_date")
    @NonNull
    private String startDate;

    @ColumnInfo(name = "end_date")
    private String endDate;


    public Plan() {
        this.userId="offline";
    }
}
