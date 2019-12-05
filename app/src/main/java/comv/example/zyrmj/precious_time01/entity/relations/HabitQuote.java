package comv.example.zyrmj.precious_time01.entity.relations;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.User;

@Entity(primaryKeys = {"user_id", "words", "habit_name"},
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id"), indices = @Index(value = "user_id"))
public class HabitQuote {
    @NonNull
    @ColumnInfo(name = "user_id")
    private String userId;
    @NonNull
    private String words;

    @NonNull
    @ColumnInfo(name = "habit_name")
    private String habitName;

    public HabitQuote(@NonNull Quote quote, @NonNull Habit habit) {
        this.habitName = habit.name;
        this.userId = quote.getUserId();
        this.words = quote.getWords();
    }

    public HabitQuote() {
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
    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(@NonNull String habitName) {
        this.habitName = habitName;
    }
}
