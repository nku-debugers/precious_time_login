package comv.example.zyrmj.precious_time01.fragments.habits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.List;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.datepicker.CustomDatePicker;
import comv.example.zyrmj.precious_time01.entity.Category;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.relations.HabitCategory;
import comv.example.zyrmj.precious_time01.repository.HabitRepository;

public class AddHabit extends Fragment implements View.OnClickListener {
    private TextView totalTime, title, habitName, habitCategory, gotoAdvanced;
    private String userId, templateName;
    private Button save;
    private EditText name, weekTime;
    private Habit newHabit;
    private List<HabitCategory>categories;

    @Override
    public void onClick(View view) {
    }

    public AddHabit() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_habit_item, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId", "");
            templateName = getArguments().getString("templateName", "");
        }
        init();
        enableButtons();
    }



    private void enableButtons() {
        gotoAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(getView());
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("templateName", templateName);
                controller.navigate(R.id.action_templateShowFragment_to_personCenterFragment);
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String habitName = name.getText().toString();
                String length = weekTime.getText().toString();
                Habit habit = new Habit(userId, habitName, length, 0);
                this.newHabit = habit;
                HabitRepository habitRepository = new HabitRepository(getContext());
                habitRepository.insertHabit(habit);
                habitRepository.insertHabitCategory(categories);
            }
        });
    }

    private void init() {
        save = getView().findViewById(R.id.habbit_complete);
        title = getView().findViewById(R.id.habbit_title);
        habitName = getView().findViewById(R.id.habbit_name);
        name = getView().findViewById(R.id.habbit_name_input);
        habitCategory = getView().findViewById(R.id.habbit_category);
        totalTime = getView().findViewById(R.id.week_time);
        gotoAdvanced = getView().findViewById(R.id.advanced_option);
        gotoAdvanced.setClickable(true);
    }

}
