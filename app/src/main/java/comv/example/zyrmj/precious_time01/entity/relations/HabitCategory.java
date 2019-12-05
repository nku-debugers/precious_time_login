package comv.example.zyrmj.precious_time01.entity.relations;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import comv.example.zyrmj.precious_time01.entity.Category;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.User;

@Entity(primaryKeys = {"user_id","habit_name", "category_name"},
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id"), indices = @Index(value = "user_id"))
public class HabitCategory {
    @NonNull
    @ColumnInfo(name = "user_id")
    private String userId;

    @NonNull
    @ColumnInfo(name = "category_name")
    private String category;

    @NonNull
    @ColumnInfo(name = "habit_name")
    private String habitName;

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
    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(@NonNull String habitName) {
        this.habitName = habitName;
    }

    public HabitCategory(@NonNull Category category, @NonNull Habit habit) {
        this.habitName = habit.name;
        this.userId = category.getUserId();
        this.category = category.name;
    }

    public HabitCategory() {
    }
}
