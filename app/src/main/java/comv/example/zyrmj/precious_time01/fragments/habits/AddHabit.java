package comv.example.zyrmj.precious_time01.fragments.habits;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.donkingliang.labels.LabelsView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.Category;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.repository.CategoryRepository;
import comv.example.zyrmj.precious_time01.repository.HabitRepository;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddHabit extends Fragment {
    private TextView totalTime, title, habitName, gotoAdvanced;
    private String userId = "offline", templateName;
    private Button save;
    private EditText name, weekTime;
    private LabelsView labelsView;
    private Habit newHabit;
    private HabitRepository habitRepository;
    private CategoryRepository categoryRepository;
    private ArrayList<String> labels;
    private ArrayList<Integer> selectedIndex;//用于页面跳转后恢复所选标签
    private ArrayList<String> selectedLabels = new ArrayList<>();

    public AddHabit() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_habit_item, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId", "offline");
            if (getArguments().getSerializable("theHabit") != null) {
                newHabit = (Habit) getArguments().getSerializable("theHabit");
            } else {
                newHabit = new Habit();
            }
            if(getArguments().getSerializable("labels")!=null)
            {
                labels=(ArrayList<String>)getArguments().getSerializable("labels");
            }

            if(getArguments().getSerializable("selectedIndex")!=null)
            {
                selectedIndex=(ArrayList<Integer>)getArguments().getSerializable("selectedIndex");
            }

        } else {
            newHabit = new Habit();

        }

        init();
        enableButtons();
    }

    private void enableButtons() {
        gotoAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValues();
                NavController controller = Navigation.findNavController(view);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putSerializable("theHabit", newHabit);
                bundle.putSerializable("labels",labels);
                bundle.putSerializable("selectedIndex",selectedIndex);
                controller.navigate(R.id.action_addHabit1_to_addHabit2, bundle);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String habitName = name.getText().toString();
//                String length = weekTime.getText().toString();
//                Habit habit = new Habit(userId, habitName, length, 0);
//                newHabit = habit;
//                HabitRepository habitRepository = new HabitRepository(getContext());
//                habitRepository.insertHabit(habit);
                // habitRepository.insertHabitCategory(categories);
                Log.d("newHabit", newHabit.getName());
                Log.d("newHabit", String.valueOf(newHabit.getCompletion()));
                Log.d("newHabit", newHabit.getLength());
                Log.d("newHabit", newHabit.getLocation());
                Log.d("newHabit", String.valueOf(newHabit.getExpectedTime()));
                Log.d("newHabit", String.valueOf(newHabit.getPriority()));
                Log.d("newHabit", String.valueOf(newHabit.getReminder()));
                if (getArguments() != null) {
                    if (getArguments().getSerializable("selectedQuotes") != null) {
                        ArrayList<Quote> quotes = (ArrayList<Quote>) getArguments().getSerializable("selectedQuotes");
                        Log.d("newHabit", String.valueOf(quotes.size()));
                    }
                }
            }
        });
    }

    private void setValues() {
        if (name.getText() != null) {
            newHabit.setName(name.getText().toString());
        }
        if (weekTime.getText() != null) {
            newHabit.setLength(weekTime.getText().toString());
        }
    }

    private void init() {
        save = getView().findViewById(R.id.habbit_complete);
        title = getView().findViewById(R.id.habbit_title);
        habitName = getView().findViewById(R.id.habbit_name);
        name = getView().findViewById(R.id.habbit_name_input);
        if (newHabit.getName() != null)
            name.setText(newHabit.getName());
        totalTime = getView().findViewById(R.id.week_time);
        weekTime = getView().findViewById(R.id.week_time_input);
        if (newHabit.getLength() != null)
        weekTime.setText(newHabit.getLength());
        gotoAdvanced = getView().findViewById(R.id.advanced_option);
        gotoAdvanced.setClickable(true);
        labelsView = getView().findViewById(R.id.category);
        categoryRepository = new CategoryRepository(getContext());
        //添加从数据库中获取的标签名称(第一次进入页面时使用)
        if (labels == null && selectedIndex == null) {
            labels = new ArrayList<>();
            selectedIndex = new ArrayList<>();
            List<Category> categories = categoryRepository.getAllCateories(userId);
            for (Category category : categories) {
                labels.add(category.getName());
            }
            labels.add("+");
        }
        labelsView.setLabels(labels); //给labelView设置字符串数组。
        labelsView.setSelects(selectedIndex);
        labelsView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {

            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                data = (String) data;
                if (data.equals("+")) {
                    showDialog("");

                }
            }
        });
        labelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                data = (String) data;
                if (!data.equals("+")) {
                    if (isSelect) {
                        selectedLabels.add((String) data);
                        selectedIndex.add(position);
                        Log.d("selected", selectedIndex.toString());
                    } else {
                        selectedLabels.remove(data);
                        selectedIndex.remove(Integer.valueOf(position));
                    }
                }

            }
        });

    }

    //添加一个新类别时弹出Dialog
    public void showDialog(String info) {

        new MaterialDialog.Builder(getContext())
                .title("添加新类别")
                .content(info)
                .inputType(InputType.TYPE_CLASS_TEXT)
                //前2个一个是hint一个是预输入的文字
                .input("", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        //判断模板名是否为空
                        if (input.toString().equals("")) {
                            dialog.setContent("类名不能为空，请重新输入！");
                        } else {
                            String categoryName = input.toString();
                            //查找是否有同名Category存在
                            if (categoryRepository.getSpecificCategory(userId, categoryName) != null) {
                                Log.i("dialog", "存在同名类别");
                                dialog.setContent("存在同名类别，请重新输入！");
                            } else {
                                Log.d("in selected1", selectedIndex.toString());
                                labels.remove("+");
                                Log.d("in selected2", selectedIndex.toString());
                                labels.add(categoryName);
                                labels.add("+");
                                Log.d("in selected3", selectedIndex.toString());
                                ArrayList<Integer> lastSelected =new ArrayList<>();
                                for(Integer index:selectedIndex)
                                {
                                    lastSelected.add(index);
                                }
                                Log.d("in selected4", lastSelected.toString());
                                labelsView.setLabels(labels);
                                Log.d("in selected5", lastSelected.toString());
                                labelsView.setSelects(lastSelected);
                                Log.d("selected", selectedIndex.toString());
                                dialog.dismiss();
                                //

                            }
                        }
                    }
                })

                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .autoDismiss(false).show();

    }

}
