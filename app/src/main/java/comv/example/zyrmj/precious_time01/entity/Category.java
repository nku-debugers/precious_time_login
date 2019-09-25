package comv.example.zyrmj.precious_time01.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(primaryKeys = {"user_id","name"},indices = @Index(value = "name",unique = true))
public class Category {
    @ColumnInfo(name = "user_id")
    @NonNull
    public String userId;
   @NonNull
    public String name;

    public String getUserId() {
        return userId;
    }

    public Category(String userId, String name) {
        this.userId = userId;
        this.name = name;
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
}

