package comv.example.zyrmj.precious_time01.fragments.habits;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.donkingliang.labels.LabelsView;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.Category;
import comv.example.zyrmj.precious_time01.repository.CategoryRepository;
import comv.example.zyrmj.precious_time01.repository.HabitRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddHabit1 extends Fragment {
    private String userId = "offline";
    private HabitRepository habitRepository;
    private CategoryRepository categoryRepository;
    private List<Category> categories;

    private TextView toAddHabit2;
    LabelsView labelsView;

    public AddHabit1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_habit_item, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        labelsView = getView().findViewById(R.id.category);
        final ArrayList<String> label = new ArrayList<>();
        categoryRepository = new CategoryRepository(getContext());
        //添加从数据库中获取的标签名称
        categories = categoryRepository.getAllCateories(userId);
        for (Category category : categories) {
            label.add(category.getName());
        }
        label.add("+");
        labelsView.setLabels(label); //直接设置一个字符串数组就可以了。
        labelsView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {

            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                data = (String) data;
                if (data.equals("+")) {
                    //添加一个新类别
                }
            }
        });
        toAddHabit2 = getView().findViewById(R.id.toAddHabit2);
        toAddHabit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_addHabit1_to_addHabit2);
            }
        });
    }
}
