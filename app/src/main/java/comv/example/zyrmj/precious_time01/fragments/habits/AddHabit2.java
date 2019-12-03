package comv.example.zyrmj.precious_time01.fragments.habits;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
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
    private Button choseQuote;
    private HabitRepository habitRepository;


    public AddHabit2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_habit_item2, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        habitRepository=new HabitRepository(getContext());
        choseQuote = getView().findViewById(R.id.choseQuote);
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
                                List<Quote> selectedQuotes = quoteAdapter.getSelctedQuotes();

                                for (Quote q : selectedQuotes) {
                                    Habit h=new Habit("offline","test","test","test","test","test",0.0);
                                    HabitQuote habitQuote=new HabitQuote(q,h) ;
                                habitRepository.insertHabitQuote(habitQuote);
                                }
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
    }
}
