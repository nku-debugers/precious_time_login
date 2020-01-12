package comv.example.zyrmj.precious_time01.entity;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"user_id", "start_time", "plan_date"})
public class Todo implements Serializable {
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
    @ColumnInfo(name = "user_id")
    @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id")
    private String userId;
    @NonNull
    @ColumnInfo(name = "item_name")
    private String name;
    @ColumnInfo(name = "end_time")
    private String endTime;
    @ColumnInfo(name = "start_time")
    @NonNull
    private String startTime;
    @ColumnInfo(name = "failure_trigger")
    public String failureTrigger;
    private String length;
    private Double completion;
    private Integer type;//0-template 1-habit 2-userItem

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

//    public Todo(@NonNull String userId, @NonNull String name, String endTime, @NonNull String startTime, String failureTrigger, String length, Integer type,Double completion) {
//        this.userId = userId;
//        this.name = name;
//        this.endTime = endTime;
//        this.startTime = startTime;
//        this.failureTrigger = failureTrigger;
//        this.length = length;
//        this.completion = completion;
//        this.type = type;
//    }

    public Todo() {
        this.completion=0.0;
        this.userId="offline";
    }

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

    public String getFailureTrigger() {
        return failureTrigger;
    }

    public void setFailureTrigger(String failureTrigger) {
        this.failureTrigger = failureTrigger;
    }
}
