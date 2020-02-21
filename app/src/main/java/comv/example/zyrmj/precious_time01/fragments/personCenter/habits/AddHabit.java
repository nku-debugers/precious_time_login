package comv.example.zyrmj.precious_time01.fragments.personCenter.habits;


import android.graphics.Color;
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
import android.widget.ImageView;
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
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.relations.HabitCategory;
import comv.example.zyrmj.precious_time01.entity.relations.HabitQuote;
import comv.example.zyrmj.precious_time01.repository.CategoryRepository;
import comv.example.zyrmj.precious_time01.repository.HabitRepository;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddHabit extends Fragment {
    private TextView totalTime, title, habitName, gotoAdvanced;
    private String userId = "offline";
    private Button save;
    private EditText name, weekTime,numPerWeek;
    private LabelsView labelsView;
    private ImageView back;
    private Habit newHabit;
    private Habit oldHabit;//更新时使用
    private HabitRepository habitRepository;
    private CategoryRepository categoryRepository;
    private ArrayList<String> labels;
    private ArrayList<Integer> selectedIndex;//用于页面跳转后恢复所选标签
    private ArrayList<String> selectedLabels;
    private boolean isUpdate=false;

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
                if(getArguments().getString("isUpdate")!=null) {
                    if (getArguments().getString("isUpdate").equals("1")) {
                        oldHabit = new Habit();
                        oldHabit.setName(newHabit.getName());
                        oldHabit.setUserId(newHabit.getUserId());
                        oldHabit.setTime4once(newHabit.getTime4once());
                        oldHabit.setPriority(newHabit.getPriority());
                        oldHabit.setExpectedTime(newHabit.getExpectedTime());
                        oldHabit.setReminder(newHabit.getReminder());
                        oldHabit.setLocation(newHabit.getLocation());
                        oldHabit.setCompletion(newHabit.getCompletion());
                        oldHabit.setLength(newHabit.getLength());
                        Log.d("oldhabit", oldHabit.getName());
                        isUpdate = true;
                    }
                    if (getArguments().getString("isUpdate").equals("2")) {
                        oldHabit = (Habit) getArguments().getSerializable("oldHabit");
                        isUpdate = true;
                    }
                }
            } else {
                newHabit = new Habit();
            }
            if (getArguments().getSerializable("labels") != null) {
                labels = (ArrayList<String>) getArguments().getSerializable("labels");
            }

            if (getArguments().getSerializable("selectedIndex") != null) {
                selectedIndex = (ArrayList<Integer>) getArguments().getSerializable("selectedIndex");
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
                bundle.putSerializable("labels", labels);
                bundle.putSerializable("selectedIndex", selectedIndex);
                if (getArguments() != null) {
                    if (getArguments().getSerializable("selectedQuotes") != null) {
                        ArrayList<Quote> selectedQuotes = (ArrayList<Quote>) getArguments().getSerializable("selectedQuotes");
                        bundle.putSerializable("selectedQuotes", selectedQuotes);
                    }
                }
                if(isUpdate==true)
                {
                    bundle.putString("isUpdate","2");
                    bundle.putSerializable("oldHabit",oldHabit);
                }
                controller.navigate(R.id.action_addHabit1_to_addHabit2, bundle);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValues();//先更新界面上的信息

                //                    //根据selectedIndex与labels计算selectedLabels
                selectedLabels = new ArrayList<>();
                for (int index : selectedIndex) {
                    selectedLabels.add(labels.get(index));
                }

                //插入newHabit
                if (newHabit.getName() == null || (newHabit.getName().replace(" ", "").equals(""))) {
                    PromptDialog promptDialog = new PromptDialog(getActivity());
                    promptDialog.showWarn("习惯名称不能为空！");
                } else {
                    if (newHabit.getLength() == null || newHabit.getLength().replace(" ", "").equals("")) {
                        PromptDialog promptDialog = new PromptDialog(getActivity());
                        promptDialog.showWarn("总时长不能为空！");
                    } else {
                        if (selectedLabels.size() == 0) {
                            PromptDialog promptDialog = new PromptDialog(getActivity());
                            promptDialog.showWarn("请至少选择一个标签！");
                        } else {
                            Habit sameHabit=habitRepository.getSpecificHabit(userId, name.getText().toString());
                            if (((sameHabit!= null)&&(isUpdate==false))||((isUpdate==true)&&(!(sameHabit.getName().equals(oldHabit.getName()))))){
                                    PromptDialog promptDialog = new PromptDialog(getActivity());
                                promptDialog.showWarn("存在同名习惯！");

                            } else {
                                //update:先删除
                                if(isUpdate==true)
                                {
                                    Log.d("delete","true");
                                    habitRepository.deleteHabit(oldHabit);
                                    habitRepository.deleteHabitCategory(userId,oldHabit.getName());
                                    habitRepository.deleteHabitQuote(userId,oldHabit.getName());
                                }
                                List<Category> oldCategories = categoryRepository.getAllCateories(userId);
                                //插入新类别
                                for (String label : selectedLabels) {
                                    int flag = 1;//记录是否是新类别
                                    for (Category category : oldCategories) {
                                        if (label.equals(category.getName())) {
                                            flag = 0;
                                            break;
                                        }
                                    }
                                    if (flag == 1) {
                                        categoryRepository.insertCategory(new Category(userId, label));
                                    }
                                }

                                //add

                               habitRepository.insertHabit(newHabit);
                               //插入关系
                               for (String label : selectedLabels) {
                                   habitRepository.insertHabitCategory(new HabitCategory(userId, label, newHabit.getName()));
                               }
                               ArrayList<Quote> selectedQuotes = new ArrayList<>();
                               if (getArguments() != null) {
                                   if (getArguments().getSerializable("selectedQuotes") != null) {
                                       selectedQuotes = (ArrayList<Quote>) getArguments().getSerializable("selectedQuotes");
                                   }
                               }
                               for (Quote q : selectedQuotes) {
                                   habitRepository.insertHabitQuote(new HabitQuote(q, newHabit));
                               }
                                NavController controller = Navigation.findNavController(view);
                               controller.navigate(R.id.action_addHabit1_to_habitShow);
                           }



                        }
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
        if(numPerWeek.getText()!=null)
        {
            newHabit.setNumPerWeek(Integer.valueOf(numPerWeek.getText().toString()));
        }
        }
    }

    private void init() {
        save = getView().findViewById(R.id.habbit_complete);
        title = getView().findViewById(R.id.habit_title);
        habitName = getView().findViewById(R.id.habbit_name);
        name = getView().findViewById(R.id.habbit_name_input);
        if (newHabit.getName() != null)
            name.setText(newHabit.getName());
        totalTime = getView().findViewById(R.id.week_time);
        weekTime = getView().findViewById(R.id.week_time_input);
        if (newHabit.getLength() != null)
            weekTime.setText(newHabit.getLength());
        numPerWeek=getView().findViewById(R.id.numPerWeek);
        if(newHabit.getNumPerWeek()!=0)
            numPerWeek.setText(String.valueOf(newHabit.getNumPerWeek()));
        gotoAdvanced = getView().findViewById(R.id.advanced_option);
        gotoAdvanced.setClickable(true);
        labelsView = getView().findViewById(R.id.category);
        categoryRepository = new CategoryRepository(getContext());
        habitRepository = new HabitRepository(getContext());

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
        if(labels==null && selectedIndex!=null) //更改页面
        {
            labels = new ArrayList<>();
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
                        selectedIndex.add(position);
                    } else {
                        selectedIndex.remove(Integer.valueOf(position));
                    }
                }

            }
        });

        back = getView().findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromptDialog promptDialog = new PromptDialog(getActivity());
                PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        NavController controller = Navigation.findNavController(getView());
                        controller.navigate(R.id.action_addHabit1_to_habitShow);
                    }
                });
                PromptButton cancel = new PromptButton("取消", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        //Nothing
                    }
                });
                confirm.setTextColor(Color.parseColor("#DAA520"));
                confirm.setFocusBacColor(Color.parseColor("#FAFAD2"));
                promptDialog.showWarnAlert("您的数据将不会被保存，是否退出？", cancel, confirm);


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
                                        ArrayList<Integer> lastSelected = new ArrayList<>();
                                        for (Integer index : selectedIndex) {
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




