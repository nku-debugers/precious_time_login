package comv.example.zyrmj.precious_time01.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.TemplateAdapter;
import comv.example.zyrmj.precious_time01.ViewModel.TemplateViewModel;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TemplateShowFragment extends Fragment {


    public TemplateShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_template_show, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final TemplateRepository templateRepository=new TemplateRepository(getContext());
        RecyclerView recyclerView=getView().findViewById(R.id.recycleView);
        final TemplateAdapter templateAdapter=new TemplateAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(templateAdapter);
        TemplateViewModel templateViewModel= ViewModelProviders.of(getActivity()).get(TemplateViewModel.class);
        templateViewModel.getAllTemplates().observe(getActivity(), new Observer<List<Template>>() {
            @Override
            public void onChanged(List<Template> templates) {
                templateAdapter.setAllTemplates(templates);
                templateAdapter.notifyDataSetChanged();
            }
        });

        ImageView returnimage=getView().findViewById(R.id.returnimage);
        returnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller= Navigation.findNavController(view);
                controller.navigate(R.id.action_templateShowFragment_to_personCenterFragment);
            }
        });

        FloatingActionButton addTemplate=getView().findViewById(R.id.addTemplate);
        addTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              showDialog("请输入模板名称",templateRepository);

            }
        });

        ImageView returnImage=getView().findViewById(R.id.returnimage);


    }
    public void showDialog(String info, final TemplateRepository templateRepository)
    {
        new MaterialDialog.Builder(getContext())
                .title("添加新模板")
                .content(info)
//                                .widgetColor(Color.BLUE)//输入框光标的颜色
                .inputType(InputType.TYPE_CLASS_TEXT)
                //前2个一个是hint一个是预输入的文字
                .input("", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                //判断模板名是否为空
                        if(input.toString().equals(""))
                        {
                            dialog.setContent("模板名不能为空，请重新输入！");
                        }
                        else {
                             String userId="offline",templateName=input.toString();
                            Log.i("yqy", "输入的是：" + input);
                            //查找是否有同名Template存在
                            if(templateRepository.getSpecificTemplate(userId,templateName)!=null)
                            {
                                Log.i("dialog","存在同名模板");
                                dialog.setContent("存在同名模板，请重新输入！");
                            }
                            else {
                                Log.i("dialog","add template");
                                dialog.dismiss();
                               templateRepository.insertTemplates(new Template(userId,templateName));
                                Bundle bundle = new Bundle();
                                bundle.putString("userId", "offline");
                                bundle.putString("templateName", input.toString());
                                NavController controller = Navigation.findNavController(getView());
                                controller.navigate(R.id.action_templateShowFragment_to_testWeekView, bundle);
//               获取template完整的数据库中的信息 用Bundle传递数据，跳转到update和delete fragment
                            }
                        }
                    }
                })

                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
               .autoDismiss(false) .show();

    }


    }

