package comv.example.zyrmj.precious_time01.fragments.habits;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.donkingliang.labels.LabelsView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.Category;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.repository.CategoryRepository;
import comv.example.zyrmj.precious_time01.repository.HabitRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddHabit extends Fragment {
    private TextView totalTime, title, habitName,gotoAdvanced;
    private String userId="offline", templateName;
    private Button save;
    private EditText name, weekTime;
    private LabelsView labelsView;
    private Habit newHabit;
    private List<Category>categories;
    private HabitRepository habitRepository;
    private CategoryRepository categoryRepository;


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
            if (getArguments().getSerializable("theHabit") != null) {
                newHabit = (Habit)getArguments().getSerializable("theHabit");
            } else {
                newHabit = new Habit();
            }
        } else {
            newHabit = new Habit();
        }

        init();
        enableButtons();
    }

    private void enableButtons() {
        gotoAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValues();
                NavController controller = Navigation.findNavController(view);
                Bundle bundle = new Bundle();
                bundle.putSerializable("theHabit", newHabit);
                controller.navigate(R.id.action_addHabit1_to_addHabit2);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String habitName = name.getText().toString();
                String length = weekTime.getText().toString();
                Habit habit = new Habit(userId, habitName, length, 0);
                newHabit = habit;
                HabitRepository habitRepository = new HabitRepository(getContext());
                habitRepository.insertHabit(habit);
                // habitRepository.insertHabitCategory(categories);
            }
        });
    }

    private void setValues() {
        if(name!= null) {
            newHabit.setName(name.getText().toString());
        }
        if(weekTime != null) {
            newHabit.setName(weekTime.getText().toString());
        }
    }

    private void init() {
        save = getView().findViewById(R.id.habbit_complete);
        title = getView().findViewById(R.id.habit_title );
        habitName = getView().findViewById(R.id.habbit_name);
        name = getView().findViewById(R.id.habbit_name_input);
        totalTime = getView().findViewById(R.id.week_time);
        gotoAdvanced = getView().findViewById(R.id.advanced_option);
        gotoAdvanced.setClickable(true);
        labelsView=getView().findViewById(R.id.category);
        categoryRepository = new CategoryRepository(getContext());
        final ArrayList<String> labels = new ArrayList<>();
        //添加从数据库中获取的标签名称
        categories = categoryRepository.getAllCateories(userId);
        for (Category category : categories) {
            labels.add(category.getName());
        }
        labels.add("+");
        labelsView.setLabels(labels); //直接设置一个字符串数组就可以了。
        labelsView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {

            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                data = (String) data;
                if (data.equals("+")) {
                    //添加一个新类别
                    Log.d("add ","category");
                }
            }
        });
    }

}
