package comv.example.zyrmj.precious_time01.ViewModel;


import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.repository.HabitRepository;

public class HabitViewModel extends AndroidViewModel {
    private HabitRepository habitRepository;
    public HabitViewModel(@NonNull Application application) {
        super(application);
        this.habitRepository=new HabitRepository(application);
    }
    public LiveData<List<Habit>> getAllHabits(String userId)
    {
        return habitRepository.getAllHabits(userId);
    }
}
