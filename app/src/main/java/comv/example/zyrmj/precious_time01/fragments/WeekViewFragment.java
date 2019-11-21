package comv.example.zyrmj.precious_time01.fragments;


import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;
import comv.example.zyrmj.weekviewlibrary.DateTimeInterpreter;
import comv.example.zyrmj.weekviewlibrary.WeekView;
import comv.example.zyrmj.weekviewlibrary.WeekViewEvent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeekViewFragment extends Fragment implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener, WeekView.EmptyViewClickListener, WeekView.EmptyViewLongPressListener, WeekView.ScrollListener  {
    private WeekView mWeekView;
    String userId,templateName;
    private FloatingActionButton add;
    private TextView title;
    private ImageView returnImge;
    private Switch changeView;

    public WeekViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.weekview, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments()!=null)
        {  userId=getArguments().getString("userId","");
            templateName=getArguments().getString("templateName","");}
        assignViews();
    }
    private void assignViews() {
        title=getView().findViewById(R.id.title);
        title.setText(templateName);
        mWeekView = (WeekView) getView().findViewById(R.id.weekview);
        returnImge=getView().findViewById(R.id.toTemplateView);
        returnImge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_testWeekView_to_templateShowFragment);

            }
        });
        add = (FloatingActionButton)getView().findViewById(R.id.floatingActionButton2);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(getView());
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("templateName", templateName);
                controller.navigate(R.id.action_testWeekView_to_addTemplateItem, bundle);
            }
        });
        // mWeekHeaderView= (WeekHeaderView) findViewById(R.id.weekheaderview);
        //mTv_date =(TextView)findViewById(R.id.tv_date);
        //init WeekView
        mWeekView.setNumberOfVisibleDays(7);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setEventLongPressListener(this);
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
        changeView=getView().findViewById(R.id.switch1);
        changeView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {}
                else
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userId);
                    bundle.putString("templateName",templateName);
                    bundle.putString("viewOption","0");
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_testWeekView_to_tmpItemListFragment, bundle);

                }
            }
        });

    }

    private void setupDateTimeInterpreter(/*final boolean shortDate*/) {
        final String[] weekLabels = { "一", "二", "三", "四", "五", "六","日"};

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
                return weekLabels[date %7];

            }
        });
    }



    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        List<TemplateItem> datas = new TemplateItemRepository(getContext()).getSpecificList(templateName,userId);
        int i = 1;
        for (TemplateItem ti : datas) {
            Log.d("列表2", ti.getItemName());
            String weekday = ti.getStartTime().split("-")[0];
            int diff = Integer.valueOf(weekday);//与周一的距离
            String starttime = ti.getStartTime().split("-")[1];
            Log.d("列表2", starttime);
            String starthour = starttime.split(":")[0];
            String startminute=starttime.split(":")[1];
            String endtime = ti.getEndTime().split("-")[1];
            String endminute=endtime.split(":")[1];
            Log.d("列表2", starthour);
            String endhour = endtime.split(":")[0];
            Calendar startTime = Calendar.getInstance();
            int currdiff=startTime.get(Calendar.DAY_OF_WEEK)-2;
            if(currdiff<0) currdiff=6;
            int distance=diff-currdiff;
            startTime.set(Calendar.DATE, startTime.get(Calendar.DATE) + distance);
            startTime.set(Calendar.MONTH, newMonth - 1);
            startTime.set(Calendar.YEAR, newYear);
            startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(starthour));
            startTime.set(Calendar.MINUTE, Integer.valueOf(startminute));
            Calendar endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endhour));
            endTime.set(Calendar.MINUTE, Integer.valueOf(endminute));
            WeekViewEvent event = new WeekViewEvent(i, ti.getItemName(), startTime, endTime);
            if(i%4==0)
                event.setColor(getResources().getColor(R.color.event_color_01));
            else if(i%4==1)
                event.setColor(getResources().getColor(R.color.event_color_02));
            else if(i%4==2)
                event.setColor(getResources().getColor(R.color.event_color_03));
            else
                event.setColor(getResources().getColor(R.color.event_color_04));
            events.add(event);
            i++;
        }
        return events;
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

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
