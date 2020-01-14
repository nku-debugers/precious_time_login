package comv.example.zyrmj.precious_time01.entity.relations;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.Todo;
import comv.example.zyrmj.precious_time01.entity.User;

@Entity(primaryKeys = {"user_id", "words", "start_time", "plan_date"},
        foreignKeys = @ForeignKey(entity = User.class,
                                parentColumns = "id",
                                childColumns = "user_id"),
        indices = @Index(value = "user_id"))
public class TodoQuote {
    @NonNull
    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(@NonNull String planDate) {
        this.planDate = planDate;
    }

    @NonNull
    @ColumnInfo(name = "plan_date")
    private String planDate;

    @NonNull
    @ColumnInfo(name = "user_id")
    private String userId;
    @NonNull
    private String words;

    @NonNull
    @ColumnInfo(name = "start_time")
    private String startTime;

    public TodoQuote(@NonNull Quote quote, @NonNull Todo todo) {
        this.startTime = todo.getStartTime();
        this.userId = quote.getUserId();
        this.words = quote.getWords();
        this.planDate = todo.getPlanDate();
    }

    public TodoQuote() {
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getWords() {
        return words;
    }

    public void setWords(@NonNull String words) {
        this.words = words;
    }

    @NonNull
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(@NonNull String startTime) {
        this.startTime = startTime;
    }
}
