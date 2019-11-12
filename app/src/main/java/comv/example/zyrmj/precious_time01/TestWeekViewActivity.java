package comv.example.zyrmj.precious_time01;

import androidx.appcompat.app.AppCompatActivity;

import comv.example.zyrmj.precious_time01.entity.Category;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.entity.User;
import comv.example.zyrmj.precious_time01.repository.CategoryRepository;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;
import comv.example.zyrmj.precious_time01.repository.UserRepository;


import comv.example.zyrmj.weekviewlibrary.DateTimeInterpreter;
import comv.example.zyrmj.weekviewlibrary.WeekView;
import comv.example.zyrmj.weekviewlibrary.WeekViewEvent;

import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TestWeekViewActivity extends AppCompatActivity implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener, WeekView.EmptyViewClickListener, WeekView.EmptyViewLongPressListener, WeekView.ScrollListener {
    //view
    private WeekView mWeekView;
    // private WeekHeaderView mWeekHeaderView;
    //  private TextView mTv_date;

    List<WeekViewEvent> mNewEvent = new ArrayList<WeekViewEvent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekview);
        assignViews();

        User user=new User();
        new UserRepository(this).insertUsers(user);
           Template template1=new Template("offline","study");
        Template template2=new Template("offline","study2");
         new TemplateRepository(this).insertTemplates(template1,template2);
        Category category=new Category("offline","test");
        new CategoryRepository(this).insertCategory(category);
        TemplateItem t1 = new TemplateItem("offline", "test1", "study"
                , "test", "1-2:00", "1-1:00");
        TemplateItem t2 = new TemplateItem("offline", "test2", "study"
                , "test", "2-17:00", "2-15:00");
        TemplateItem t3 = new TemplateItem("offline", "test3", "study"
                , "test", "0-8:00", "0-7:00");
       TemplateItem t4 = new TemplateItem("offline", "test4", "study"
                , "test", "3-8:00", "3-7:00");
        TemplateItem t5 = new TemplateItem("offline", "test1", "study2"
                , "test", "3-8:00", "3-7:00");
        TemplateItem t6 = new TemplateItem("offline", "test1", "study2"
                , "test", "1-8:00", "1-7:00");
       new TemplateItemRepository(this).insertTemplateItems(t1,t2,t3,t4,t5,t6);


        List<TemplateItem> datas = new TemplateItemRepository(this).getAll();
        int i = 1;
        for (TemplateItem ti : datas) {
            Log.d("列表2", ti.getItemName());
            String weekday = ti.getStartTime().split("-")[0];
            int diff = Integer.valueOf(weekday);//与周一的距离
            String starttime = ti.getStartTime().split("-")[1];
            Log.d("列表2", starttime);
            String starthour = starttime.split(":")[0];
            String endtime = ti.getEndTime().split("-")[1];
            Log.d("列表2", starthour);
            String endhour = endtime.split(":")[0];
            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.DATE, startTime.get(Calendar.DATE) + diff);
            startTime.set(Calendar.MONTH, 9);
            startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(starthour));
            startTime.set(Calendar.MINUTE, 0);
            Calendar endTime = (Calendar) startTime.clone();
            startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endhour));
            WeekViewEvent event = new WeekViewEvent(i, ti.getItemName(), startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_01));
            Log.d("event", event.getName());
            mNewEvent.add(event);
            i++;
        }
//        mDialogAll = new TimePickerDialog.Builder()
//                .setCallBack(this)
//                .setCancelStringId("Cancel")
//                .setSureStringId("Sure")
//                .setTitleStringId("TimePicker")
//                .setYearText("Year")
//                .setMonthText("Month")
//                .setDayText("Day")
//                .setHourText("Hour")
//                .setMinuteText("Minute")
//                .setCyclic(false)
//                .setMinMillseconds(System.currentTimeMillis())
//                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
//                .setCurrentMillseconds(System.currentTimeMillis())
//                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
//                .setType(Type.ALL)
//                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
//                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
//                .setWheelItemTextSize(12)
//                .build();

    }

    private void assignViews() {
        mWeekView = (WeekView) findViewById(R.id.weekview);
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
                Toast.makeText(getApplicationContext(), time.getTime().toString(), Toast.LENGTH_LONG).show();
            }
        };
        mWeekView.setEmptyViewClickListener(emptyViewClickListener);
        setupDateTimeInterpreter();


    }


    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     */
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
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        List<TemplateItem> datas = new TemplateItemRepository(this).getAll();
        int i = 1;
        for (TemplateItem ti : datas) {
            Log.d("列表2", ti.getItemName());
            String weekday = ti.getStartTime().split("-")[0];
            int diff = Integer.valueOf(weekday);//与周一的距离
            String starttime = ti.getStartTime().split("-")[1];
            Log.d("列表2", starttime);
            String starthour = starttime.split(":")[0];
            String endtime = ti.getEndTime().split("-")[1];
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
            startTime.set(Calendar.MINUTE, 0);
            Calendar endTime = (Calendar) startTime.clone();
            startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endhour));
            WeekViewEvent event = new WeekViewEvent(i, ti.getItemName(), endTime, startTime);
            if(i%4==0)
            event.setColor(getResources().getColor(R.color.event_color_01));
            else if(i%4==1)
                event.setColor(getResources().getColor(R.color.event_color_02));
            else if(i%4==2)
                event.setColor(getResources().getColor(R.color.event_color_03));
            else
                event.setColor(getResources().getColor(R.color.event_color_04));
            Log.d("event", event.getName());
           events.add(event);
            i++;
        }

        return events;



    }

    private String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(TestWeekViewActivity.this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(TestWeekViewActivity.this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onEmptyViewClicked(Calendar time) {
        Toast.makeText(TestWeekViewActivity.this, "Empty View clicked " + time.get(Calendar.YEAR) + "/" + time.get(Calendar.MONTH) + "/" + time.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(TestWeekViewActivity.this, "Empty View long  clicked " + time.get(Calendar.YEAR) + "/" + time.get(Calendar.MONTH) + "/" + time.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {

    }

//    @Override
//    public void onSelectedDaeChange(Calendar selectedDate) {
//        mWeekHeaderView.setSelectedDay(selectedDate);
//        mTv_date.setText(selectedDate.get(Calendar.YEAR)+"年"+(selectedDate.get(Calendar.MONTH)+1)+"月");
//    }
}
