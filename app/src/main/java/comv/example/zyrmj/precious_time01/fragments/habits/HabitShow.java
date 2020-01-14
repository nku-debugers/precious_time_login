package comv.example.zyrmj.precious_time01.fragments.habits;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.HabitAdapter;
import comv.example.zyrmj.precious_time01.ViewModel.HabitViewModel;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.repository.CategoryRepository;
import comv.example.zyrmj.precious_time01.repository.HabitRepository;
import comv.example.zyrmj.precious_time01.repository.QuoteRepository;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

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
        recyclerView=getView().findViewById(R.id.habit_recycleView);
        final HabitAdapter habitAdapter=new HabitAdapter();
        habitAdapter.setAllRepository(new CategoryRepository(getContext()),new QuoteRepository(getContext()),habitRepository);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(habitAdapter);
        habitViewModel= ViewModelProviders.of(getActivity()).get(HabitViewModel.class);
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
        back = getView().findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitShow_to_personCenterFragment);
            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_habitShow_to_personCenterFragment);
                    return true;
                }
                return false;
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.END) {


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Habit habitToDelete = allHabits.get(viewHolder.getAdapterPosition());
                habitRepository.deleteHabit(habitToDelete);
                Snackbar.make(getView(), "删除了一条箴言", Snackbar.LENGTH_SHORT).
                        setAction("撤销", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        habitRepository.insertHabit(habitToDelete);
                                    }
                                }
                        ).show();

            }
            //在滑动的时候，画出浅灰色背景和垃圾桶图标，增强删除的视觉效果

            Drawable icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_delete_black_24dp);
            Drawable background = new ColorDrawable(Color.LTGRAY);

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;

                int iconLeft, iconRight, iconTop, iconBottom;
                int backTop, backBottom, backLeft, backRight;
                backTop = itemView.getTop();
                backBottom = itemView.getBottom();
                iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                iconBottom = iconTop + icon.getIntrinsicHeight();
                if (dX > 0) {
                    backLeft = itemView.getLeft();
                    backRight = itemView.getLeft() + (int) dX;
                    background.setBounds(backLeft, backTop, backRight, backBottom);
                    iconLeft = itemView.getLeft() + iconMargin;
                    iconRight = iconLeft + icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                } else if (dX < 0) {
                    backRight = itemView.getRight();
                    backLeft = itemView.getRight() + (int) dX;
                    background.setBounds(backLeft, backTop, backRight, backBottom);
                    iconRight = itemView.getRight() - iconMargin;
                    iconLeft = iconRight - icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                } else {
                    background.setBounds(0, 0, 0, 0);
                    icon.setBounds(0, 0, 0, 0);
                }
                background.draw(c);
                icon.draw(c);

            }
        }).attachToRecyclerView(recyclerView);
    }


}
