package comv.example.zyrmj.precious_time01.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.Utils.TimeDiff;
import comv.example.zyrmj.precious_time01.entity.Plan;
import comv.example.zyrmj.precious_time01.entity.Todo;
import comv.example.zyrmj.precious_time01.entity.User;
import comv.example.zyrmj.precious_time01.notification.LongRunningService;
import comv.example.zyrmj.precious_time01.repository.PlanRepository;
import comv.example.zyrmj.precious_time01.repository.TodoRepository;
import comv.example.zyrmj.precious_time01.repository.UserRepository;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static comv.example.zyrmj.precious_time01.Utils.TimeDiff.getAlarmMillis;
import static comv.example.zyrmj.precious_time01.Utils.TimeDiff.getCurrentWeekDay;

public class PlanActivity extends AppCompatActivity {
    private List<Todo> alarmTodos;
    private String userId = "offline";
    private Plan showedPlan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        firstRun();
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("userId") != null) {
            userId = intent.getStringExtra("userId");
        }
        showedPlan = choosePlan();
        if (showedPlan != null) {
            List<Todo> temp = new TodoRepository(this).getListTodoByPlanDate(userId, showedPlan.getStartDate());
            alarmTodos = new ArrayList<>();
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
            String today = simpleDateFormat.format(date);
            if (temp.size() > 0) {
                String todoPlanDate = temp.get(0).getPlanDate();

                Calendar cal = Calendar.getInstance();
                String splieTimes[] = todoPlanDate.split("-");
                Date start = new Date((Integer.valueOf(splieTimes[0]) - 1900),
                        (Integer.valueOf(splieTimes[1]) - 1), (Integer.valueOf(splieTimes[2])));
                cal.setTime(start);
                //增加6天
                cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(temp.get(0).getStartTime().substring(0, 1)));
                //Calendar转为Date类型
                Date end = cal.getTime();
                //将增加后的日期转为字符串
                String  todoToday = simpleDateFormat.format(end);
                Log.d("mytag", "onCreate: todoToday is " + todoToday);
                Log.d("mytag", "onCreate: today is " + today);
                TodoRepository tr = new TodoRepository(this);
                if (todoToday.compareTo(today) < 0) {
                    for (int i = 0; i < temp.size(); i++) {
                        if (temp.get(i).getCompletion() == null) {
                            temp.get(i).setCompletion(false);
                            temp.get(i).setFailureTrigger("outdated");
                            tr.updateTodo(temp.get(i));
                        }
                    }
                }
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm", Locale.CHINA);
                String nowTime = simpleDateFormat1.format(date);

                if (todoToday.equals(today)) {
                    for (int i = 0; i < temp.size(); i++) {
                        if (temp.get(i).getCompletion()== null || !temp.get(i).getCompletion())
                            if(TimeDiff.compare(nowTime,temp.get(i).getStartTime().split("-")[1])>0)
                            {
                                temp.get(i).setCompletion(false);
                                temp.get(i).setFailureTrigger("outdated");
                                tr.updateTodo(temp.get(i));
                            }
                            alarmTodos.add(temp.get(i));

                    }
                    setAlarms();
                }

            }
        }

        NavController controller = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, controller);
    }

    private Plan choosePlan() {
        List<Plan> plans = new PlanRepository(this).getAllPlans(userId);
        System.out.println("plans result");
        Date currDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currDateString = sdf.format(currDate);
        Plan chosenPlan = null;
        int diff = Integer.MAX_VALUE;
        for (Plan plan : plans) {
            System.out.println(plan.getPlanName());
            System.out.println(plan.getStartDate());
            System.out.println(plan.getEndDate());
            int distance = TimeDiff.daysBetween(plan.getStartDate(), currDateString);
            if (distance < diff) {
                diff = distance;
                chosenPlan = plan;
            }

        }
//        System.out.println("final plan");
//        System.out.println(chosenPlan.getPlanName());
//        System.out.println(chosenPlan.getStartDate());
//        System.out.println(chosenPlan.getEndDate());
        return chosenPlan;
    }

    private void setAlarms() {
        TodoRepository tr = new TodoRepository(this);
        Log.d("mytag", "setAlarms: The size is " + alarmTodos.size());
        for (int i = 0; i < alarmTodos.size(); i++) {

            if (alarmTodos.get(i).getReminder() > 0) {
                long k = getAlarmMillis(alarmTodos.get(i).getStartTime(), alarmTodos.get(i).getReminder());
                if (k >= 0) {
                    Intent myIntent = new Intent(this, LongRunningService.class);
                    Log.d("mytag", "setAlarms: The k is " + k);
                    myIntent.putExtra("Millis", k);
                    myIntent.putExtra("userId", userId);
                    myIntent.putExtra("todoName", alarmTodos.get(i).getName());
                    myIntent.putExtra("todoStartTime", alarmTodos.get(i).getStartTime());
                    myIntent.setAction("notice");
                    Log.d("mytag", "setAlarms: this is the " + i + " time");
                    startService(myIntent);
                }
                else {
                    alarmTodos.get(i).setCompletion(false);
                    alarmTodos.get(i).setFailureTrigger("outdated");
                    tr.updateTodo(alarmTodos.get(i));
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController controller = Navigation.findNavController(this, R.id.fragment);
        return controller.navigateUp();
    }

    private void firstRun() {
        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun",0);
        Boolean first_run = sharedPreferences.getBoolean("First",true);
        if (first_run){
            sharedPreferences.edit().putBoolean("First",false).commit();
            User u = new User();
            UserRepository re = new UserRepository(this);
            re.insertUsers(u);
            Toast.makeText(this,"第一次",Toast.LENGTH_LONG).show();
        }
    }
}
