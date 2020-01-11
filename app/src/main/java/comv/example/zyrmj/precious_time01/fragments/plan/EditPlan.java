package comv.example.zyrmj.precious_time01.fragments.plan;


import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;
import comv.example.zyrmj.weekviewlibrary.DateTimeInterpreter;
import comv.example.zyrmj.weekviewlibrary.WeekView;
import comv.example.zyrmj.weekviewlibrary.WeekViewEvent;
import com.ddz.floatingactionbutton.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPlan extends Fragment  implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EmptyViewClickListener,
        WeekView.EmptyViewLongPressListener, WeekView.ScrollListener{
    private Button confirm;
    private FloatingActionMenu fl_menu;
    private com.ddz.floatingactionbutton.FloatingActionButton addHabit,addToDo,habitList,toDoList;
    private WeekView mWeekView;
    String userId, templateName;
    private ImageView returnImge;
    private List<WeekViewEvent> events;
    private List<TemplateItem> datas;


    public EditPlan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_plan, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId", "");
            templateName = getArguments().getString("templateName", "");
        }
        assignViews();
        enableBackButton();

    }
    private void assignViews() {
        confirm=getView().findViewById(R.id.create_plan);
        fl_menu = getView().findViewById(R.id.menu);
        addHabit=getView().findViewById(R.id.addHabit);
        addToDo=getView().findViewById(R.id.addToDo);
        habitList=getView().findViewById(R.id.habitList);
        toDoList=getView().findViewById(R.id.toDoList);
        mWeekView = (WeekView) getView().findViewById(R.id.weekview);
        returnImge = getView().findViewById(R.id.toChoseTemplate);
        returnImge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_editPlan_to_choseTemplate);

            }
        });

        mWeekView.setNumberOfVisibleDays(7);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setScrollListener(this);
        WeekView.EmptyViewClickListener emptyViewClickListener = new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                Toast.makeText(getContext(), time.getTime().toString(), Toast.LENGTH_LONG).show();
            }
        };
        mWeekView.setEmptyViewClickListener(emptyViewClickListener);
        setupDateTimeInterpreter();

    }

    private void setupDateTimeInterpreter(/*final boolean shortDate*/) {
        final String[] weekLabels = {"一", "二", "三", "四", "五", "六", "日"};
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
//                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
//                String weekday = weekdayNameFormat.format(date.getTime());
//                SimpleDateFormat format = new SimpleDateFormat("d", Locale.getDefault());
//                //return format.format(date.getTime());
//                int index = date.get(Calendar.DAY_OF_WEEK) - 1;
//                return weekLabels[index];
                return null;

            }

            @Override
            public String interpretTime(int hour) {
                return String.format("%02d:00", hour);

            }

            @Override
            public String interpretWeek(int date) {
                return weekLabels[date % 7];

            }
        });
    }

    private void enableBackButton() {
        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_editPlan_to_choseTemplate);
                    return true;
                }
                return false;
            }
        });

        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"addHabit",Toast.LENGTH_SHORT);
            }
        });

        addToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_editPlan_to_addToDo2);
            }
        });

        habitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"HabitList",Toast.LENGTH_SHORT);
            }
        });

        toDoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"ToDoList",Toast.LENGTH_SHORT);
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //生成最终计划
            }
        });
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        int index = event.getIndex();
        TemplateItem templateItem = datas.get(index);
        Bundle bundle = new Bundle();
        bundle.putSerializable("templateItem", templateItem);
        bundle.putString("viewOption", "0");
//        NavController controller = Navigation.findNavController(getView());
//        controller.navigate(R.id.action_testWeekView_to_updateTemplateItemFragment, bundle);


    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        events = new ArrayList<WeekViewEvent>();
        datas = new TemplateItemRepository(getContext()).getSpecificList(templateName, userId);
        Log.d("templateitem",String.valueOf(datas.size()));
        int i = 1;
        int index = 0;
        for (TemplateItem ti : datas) {
            Log.d("列表2", ti.getItemName());
            String weekday = ti.getStartTime().split("-")[0];
            int diff = Integer.valueOf(weekday);//与周一的距离
            String starttime = ti.getStartTime().split("-")[1];
            Log.d("列表2", starttime);
            String starthour = starttime.split(":")[0];
            String startminute = starttime.split(":")[1];
            String endtime = ti.getEndTime().split("-")[1];
            String endminute = endtime.split(":")[1];
            Log.d("列表2", starthour);
            String endhour = endtime.split(":")[0];
            Calendar startTime = Calendar.getInstance();
            int currdiff = startTime.get(Calendar.DAY_OF_WEEK) - 2;
            if (currdiff < 0) currdiff = 6;
            int distance = diff - currdiff;
            startTime.set(Calendar.DATE, startTime.get(Calendar.DATE) + distance);
            startTime.set(Calendar.MONTH, newMonth - 1);
            startTime.set(Calendar.YEAR, newYear);
            startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(starthour));
            startTime.set(Calendar.MINUTE, Integer.valueOf(startminute));
            Calendar endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endhour));
            endTime.set(Calendar.MINUTE, Integer.valueOf(endminute));
            WeekViewEvent event = new WeekViewEvent(i, ti.getItemName(), startTime, endTime, index);
            event.setColor(getResources().getColor(R.color.event_color_05)); //template中事项统一颜色
            index += 1;
            events.add(event);
            i++;
        }
        return events;
    }


    @Override
    public void onEmptyViewClicked(Calendar time) {

    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {

    }

    @Override
    public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {

    }
}
