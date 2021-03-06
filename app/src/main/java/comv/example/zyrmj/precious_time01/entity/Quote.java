package comv.example.zyrmj.precious_time01.entity;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"user_id", "words"},
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id"))
public class Quote implements Serializable {
    @NonNull
    @ColumnInfo(name = "user_id")
    private String userId;
    @NonNull
    private String words;

    @NotNull
    public String getAuthor() {
        return author;
    }

    public void setAuthor(@NotNull String author) {
        this.author = author;
    }

    @NotNull
    private String author;

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

    public Quote(@NonNull String userId, @NonNull String words, @NotNull String author) {
        this.userId = userId;
        this.words = words;
        this.author = author;
    }
}
