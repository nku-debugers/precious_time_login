package comv.example.zyrmj.precious_time01.entity;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"user_id", "start_date"})
public class Plan {
    @NonNull
    @ColumnInfo(name = "user_id")
    public String userId;

    @ColumnInfo(name = "plan_name")
    public String planName;

    @NonNull
    @ColumnInfo(name = "start_date")
    @NonNull
    public String startDate;

    @ColumnInfo(name = "end_date")
    public String endDate;

    @ColumnInfo(name = "failure_trigger")
    public String failureTrigger;
}
