package comv.example.zyrmj.precious_time01.fragments.habits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.datepicker.CustomDatePicker;

public class AddHabit extends Fragment implements View.OnClickListener {
    private TextView totalTime, onceTime;
    private CustomDatePicker totalPicker, oncePicker;
    private String userId, templateName;
    Button save;


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
    }

    private void init() {
        getView().findViewById(R.id.total_time).setOnClickListener(this);
        save = getView().findViewById(R.id.confirm);

    }

}
