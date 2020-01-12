//package comv.example.zyrmj.precious_time01.fragments.plan;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.Switch;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.cardview.widget.CardView;
//import androidx.fragment.app.Fragment;
//
//import com.donkingliang.labels.LabelsView;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import comv.example.zyrmj.precious_time01.R;
//import comv.example.zyrmj.precious_time01.datepicker.CustomDatePicker;
//import comv.example.zyrmj.precious_time01.entity.Category;
//import comv.example.zyrmj.precious_time01.entity.Quote;
//import comv.example.zyrmj.precious_time01.entity.Todo;
//import comv.example.zyrmj.precious_time01.repository.CategoryRepository;
//import comv.example.zyrmj.precious_time01.repository.TodoRepository;
//
//public class UpdateTodo extends Fragment implements View.OnClickListener {
//    static String TAG = "mytag";
//    private Todo myTodo;
//    private Button choseQuote, confirm;
//    private Switch timeType;
//    private boolean timeTypeFlag;
//    private EditText todoName;
//    private LabelsView labelsView;
//    private String userId = "offline";
//    private ArrayList<String> labels, selectedLabels;
//    private CategoryRepository categoryRepository;
//    private TodoRepository todoRepository;
//    private ArrayList<Integer> selectedIndex;
//    private TextView mTvSelectedTime1, mTvSelectedTime2, mTvSelectedTimeWeek, mTvSelectedLength;
//    private CustomDatePicker mTimePicker1, mTimePicker2, mTimePickerWeek, mLengthPicker;
//    private CardView start, end, week;
//    private Date startDate, endDate, lengthDate;
//    private boolean startDateModified, endDateModified;
//    private boolean timeReverse;
//    private ArrayList<Quote> selectedQuotes;
//    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);//设置日期格式
//    private SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm", Locale.CANADA);
//    private String[] weekString = {"日", "一", "二", "三", "四", "五", "六"};
//    private LinearLayout timeLength;
//    private List<Todo> todos;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.add_todo, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (getArguments() != null) {
//            userId = getArguments().getString("userId", "");
//            todos = (List<Todo>) getArguments().getSerializable("todo");
//            myTodo = (Todo) getArguments().getSerializable("mytodo");
//
//        }
//        assignViews();
//        init();
//        enableButtons();
//    }
//
//    private void assignViews() {
//        week = getView().findViewById(R.id.week_card);
//        confirm = getView().findViewById(R.id.todo_confirm);
//        choseQuote = getView().findViewById(R.id.choseQuoteTodo);
//        timeType = getView().findViewById(R.id.todo_time);
//        todoName = getView().findViewById(R.id.todo_name_input);
//        labelsView = getView().findViewById(R.id.todo_category_select);
//        categoryRepository = new CategoryRepository(getContext());
//        mTvSelectedTime1 = getView().findViewById(R.id.tv_selected_start_time_in_todo);
//        mTvSelectedTime2 = getView().findViewById(R.id.tv_selected_end_time_in_todo);
//        mTvSelectedTimeWeek = getView().findViewById(R.id.tv_selected_week);
//        mTvSelectedLength = getView().findViewById(R.id.tv_selected_length);
//        start = getView().findViewById(R.id.card_start);
//        end = getView().findViewById(R.id.card_end);
//        timeLength = getView().findViewById(R.id.todo_length);
//    }
//
//    private void init() {
//        startDateModified = false;
//        endDateModified = false;
//        timeReverse = false;
//        todoName.setText(myTodo.getName());
//        //初始化标签
//        labels = new ArrayList<>();
//        List<Category> categories = categoryRepository.getAllCateories(userId);
//        for (Category category : categories) {
//            labels.add(category.getName());
//        }
//        labels.add("+");
//        labelsView.setLabels(labels); //给labelView设置字符串数组。
//        labelsView.setSelects(selectedIndex);
//        //初始化时间选择
//        if (myTodo.getStartTime().length() != 0) {
//            try {
//                startDate = new SimpleDateFormat("HH:mm", Locale.CHINA).parse(myTodo.getStartTime());
//                endDate = new SimpleDateFormat("HH:mm", Locale.CHINA).parse(myTodo.getEndTime());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            timeType.setChecked(false);
//            mTvSelectedTime1.setText(myTodo.getStartTime());
//            mTvSelectedTime2.setText(myTodo.getEndTime());
//
//        } else {
//            timeType.setChecked(true);
//        }
//        endDate = new Date();
//        selectedQuotes=new ArrayList<>();
//        initTimerPicker1();
//        initTimerPicker2();
//        initTimerPicker3();
//        initTimerPicker4();
//        labels = new ArrayList<>();
//        selectedIndex = new ArrayList<>();
//        List<Category> categories = categoryRepository.getAllCateories(userId);
//        for (Category category : categories) {
//            labels.add(category.getName());
//        }
//        labels.add("+");
//        labelsView.setLabels(labels);
//        mTvSelectedLength.setText("0");
//    }
//
//    @Override
//    public void onClick(View view) {
//
//    }
//
//}
