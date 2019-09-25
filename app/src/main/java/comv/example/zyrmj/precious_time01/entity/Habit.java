package comv.example.zyrmj.precious_time01.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Time;
import java.sql.Timestamp;

@Entity(foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "name",
        childColumns = "category_name"))
public class Habit {
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    public String userId;
    public String name;
    @ColumnInfo(name = "end_time")
    private String endTime;
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
