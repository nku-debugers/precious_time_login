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

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.Category;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.repository.CategoryRepository;
import comv.example.zyrmj.precious_time01.repository.HabitRepository;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddHabit extends Fragment {
    private TextView totalTime, title, habitName,gotoAdvanced;
    private String userId="offline", templateName;
    private Button save;
    private EditText name, weekTime;
    private LabelsView labelsView;
    private Habit newHabit;
    private HabitRepository habitRepository;
    private CategoryRepository categoryRepository;
    private ArrayList<String> labels = new ArrayList<>();
    private ArrayList<String> selectedLabels=new ArrayList<>();

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
            userId = getArguments().getString("userId", "");
            templateName = getArguments().getString("templateName", "");
        }
        init();
        enableButtons();
    }



    private void enableButtons() {
        gotoAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_addHabit1_to_addHabit2);
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String habitName = name.getText().toString();
                String length = weekTime.getText().toString();
                Habit habit = new Habit(userId, habitName, length, 0);
                newHabit = habit;
                HabitRepository habitRepository = new HabitRepository(getContext());
                habitRepository.insertHabit(habit);
                // habitRepository.insertHabitCategory(categories);
            }
        });
    }

    private void init() {
        save = getView().findViewById(R.id.habbit_complete);
        title = getView().findViewById(R.id.habbit_title);
        habitName = getView().findViewById(R.id.habbit_name);
        name = getView().findViewById(R.id.habbit_name_input);
        totalTime = getView().findViewById(R.id.week_time);
        gotoAdvanced = getView().findViewById(R.id.advanced_option);
        gotoAdvanced.setClickable(true);
        labelsView=getView().findViewById(R.id.category);
        categoryRepository = new CategoryRepository(getContext());

        //添加从数据库中获取的标签名称
        List<Category>categories = categoryRepository.getAllCateories(userId);
        for (Category category : categories) {
            labels.add(category.getName());
        }
        labels.add("+");
        labelsView.setLabels(labels); //给labelView设置字符串数组。
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
                data=(String)data;
                if(!data.equals("+")) {
                    if(isSelect)
                    selectedLabels.add((String)data);
                    else selectedLabels.remove(data);
                }

            }
        });

    }

//添加一个新类别时弹出Dialog
    public void showDialog(String info) {
        for(String label:selectedLabels)
        {
            Log.d("selected",label);
        }
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
                                Log.i("dialog", "add category");
                                dialog.dismiss();
                                labels.remove("+");
                                labels.add(categoryName);
                                labels.add("+");
                                labelsView.setLabels(labels); //直接设置一个字符串数组就可以了。
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
