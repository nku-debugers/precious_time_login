package comv.example.zyrmj.precious_time01.fragments.habits;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.HabitAdapter;
import comv.example.zyrmj.precious_time01.ViewModel.HabitViewModel;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.repository.HabitRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class HabitShow extends Fragment {
    private String userId="offline";
    private List<Habit> allHabits;
    private HabitRepository habitRepository;
    private RecyclerView recyclerView;
    private ImageView back;
    private FloatingActionButton addHabit;
    private HabitViewModel habitViewModel;


    public HabitShow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_habit_show, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        habitRepository=new HabitRepository(getContext());
        recyclerView=getView().findViewById(R.id.recycleView);
        final HabitAdapter habitAdapter=new HabitAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(habitAdapter);
        HabitViewModel habitViewModel= ViewModelProviders.of(getActivity()).get(HabitViewModel.class);
        habitViewModel.getAllHabits(userId).observe(getActivity(), new Observer<List<Habit>>() {
            @Override
            public void onChanged(List<Habit> habits) {
                habitAdapter.setAllHabits(habits);
                habitAdapter.notifyDataSetChanged();
                allHabits=habits;
            }
        });

        addHabit = getView().findViewById(R.id.add);
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitShow_to_addHabit1);
            }
        });
    }
}
