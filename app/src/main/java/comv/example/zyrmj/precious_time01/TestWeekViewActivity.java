package comv.example.zyrmj.precious_time01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;
import comv.example.zyrmj.weekviewlibrary.DateTimeInterpreter;
import comv.example.zyrmj.weekviewlibrary.WeekDayView;
import comv.example.zyrmj.weekviewlibrary.WeekHeaderView;
import comv.example.zyrmj.weekviewlibrary.WeekView;
import comv.example.zyrmj.weekviewlibrary.WeekViewEvent;

import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TestWeekViewActivity extends AppCompatActivity implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener,WeekView.EmptyViewClickListener,WeekView.EmptyViewLongPressListener,WeekView.ScrollListener{
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
        WeekView.EmptyViewClickListener emptyViewClickListener=new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                Toast.makeText(getApplicationContext(),time.getTime().toString(),Toast.LENGTH_LONG).show();
            }
        };
        mWeekView.setEmptyViewClickListener(emptyViewClickListener);
        setupDateTimeInterpreter();


    }




    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     *

     */
   private void setupDateTimeInterpreter(/*final boolean shortDate*/) {
        final String[] weekLabels={"日","一","二","三","四","五","六"};
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat("d", Locale.getDefault());
                return format.format(date.getTime());

            }

            @Override
            public String interpretTime(int hour) {
                return String.format("%02d:00", hour);

            }

            @Override
            public String interpretWeek(int date) {
                if(date>7||date<1){
                    return null;
                }
                return weekLabels[date-1];

            }
        });
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        // Populate the week view with some events.
       List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, 9);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, 9);
        WeekViewEvent event = new WeekViewEvent(1, "This is a Event!!", startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);
        Log.d("checkdate",String.valueOf(newYear)+String.valueOf(newMonth));


        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 4);
        startTime.set(Calendar.MINUTE, 20);
        startTime.set(Calendar.MONTH, newMonth );
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 5);
        endTime.set(Calendar.MINUTE, 0);
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 2);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);
        Log.d("checkdate",String.valueOf(newYear)+String.valueOf(newMonth));

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        startTime.add(Calendar.DATE, 1);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(3, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 15);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(4, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_04));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 1);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
        startTime.set(Calendar.HOUR_OF_DAY, 15);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);
        events.addAll(mNewEvent);
        return mNewEvent;
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
