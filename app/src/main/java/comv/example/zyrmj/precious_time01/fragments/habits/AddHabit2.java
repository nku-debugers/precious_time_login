package comv.example.zyrmj.precious_time01.fragments.habits;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.QuoteAdapter;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.relations.HabitQuote;
import comv.example.zyrmj.precious_time01.repository.HabitRepository;
import comv.example.zyrmj.precious_time01.repository.QuoteRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddHabit2 extends Fragment {
    private String userId = "offline";
    private Button choseQuote,back;
    private HabitRepository habitRepository;
    private Habit newHabit;
    private NiceSpinner timePeriodSpinner,habitPrioritySpinner;
    private EditText habitPlace,advancedMinute;
    private Switch timeRemind;
    private ArrayList<Quote> selectedQuotes;

    public AddHabit2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_habit_item2, container, false);
    }

    private void enableButtons() {
        choseQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Quote> quotes = new QuoteRepository(getContext()).getAllQuotes2(userId);
                final QuoteAdapter quoteAdapter = new QuoteAdapter(quotes);
                new MaterialDialog.Builder(getContext())
                        .title("选择箴言")// 标题
                        // adapter 方法中第一个参数表示自定义适配器，该适配器必须继承 RecyclerView.Adapter
                        // 第二个参数表示布局管理器，如果不需要设置就为 null，可选择的值只有线性布局管理器（LinearLayoutManager）
                        // 和网格布局管理器（GridLayoutManager）两种
                        .adapter(quoteAdapter, new LinearLayoutManager(getContext()))
                        .positiveText("确认")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                               selectedQuotes = (ArrayList<Quote>) quoteAdapter.getSelctedQuotes();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();// 显示对话框
            }
        });
        timePeriodSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //收集界面信息，统一放到habit中传回上一个界面
                setValues();
                NavController controller = Navigation.findNavController(view);
                Bundle bundle = new Bundle();
                bundle.putString("userId",userId);
                bundle.putSerializable("theHabit", newHabit);
                bundle.putSerializable("selectedQuotes",selectedQuotes);
                if(getArguments().getSerializable("labels")!=null)
                    bundle.putSerializable("labels",getArguments().getSerializable("labels"));
                if(getArguments().getSerializable("selectedIndex")!=null)
                    bundle.putSerializable("selectedIndex",getArguments().getSerializable("selectedIndex"));
                controller.navigate(R.id.action_addHabit2_to_addHabit1,bundle);
            }
        });
    }

    public void init()
    {
        choseQuote = getView().findViewById(R.id.choseQuote);
        back=getView().findViewById(R.id.advanced_back);
        timePeriodSpinner = getView().findViewById(R.id.time_period_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("上午", "下午", "晚上"));
        timePeriodSpinner.attachDataSource(dataset);
        habitPrioritySpinner= getView().findViewById(R.id.habbit_priority_spinner);
        List<String> priorities=new LinkedList<>(Arrays.asList("低","中","高"));
        habitPrioritySpinner.attachDataSource(priorities);
        habitPlace=getView().findViewById(R.id.habbit_place_input);
        advancedMinute= getView().findViewById(R.id.advancedminute);
        timeRemind=getView().findViewById(R.id.time_remind_switch);

    }

    public void setValues()
    {
        if(habitPlace.getText() != null) {
            newHabit.setLocation(habitPlace.getText().toString());
        }
        if(timeRemind.isChecked())
        {
            if(advancedMinute.getText()!=null)
            {
                newHabit.setReminder(Integer.valueOf(advancedMinute.getText().toString()));
            }
        }
        else newHabit.setReminder(0);
        newHabit.setExpectedTime(timePeriodSpinner.getSelectedIndex());//0-上午 1-下午 2-晚上
        newHabit.setPriority(habitPrioritySpinner.getSelectedIndex());//0-低 1-中 2-高
        //设置单次时长


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        if(getArguments() != null) {
            userId=getArguments().getString("userId","offline");
            newHabit = (Habit)getArguments().getSerializable("theHabit");
        }
        else
        {
            newHabit=new Habit();
        }
        selectedQuotes=new ArrayList<>();
        habitRepository=new HabitRepository(getContext());
        enableButtons();
    }

}
