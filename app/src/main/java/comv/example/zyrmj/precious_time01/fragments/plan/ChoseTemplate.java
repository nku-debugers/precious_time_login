package comv.example.zyrmj.precious_time01.fragments.plan;


import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.sql.Time;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.TemplateAdapter;
import comv.example.zyrmj.precious_time01.Utils.TimeDiff;
import comv.example.zyrmj.precious_time01.activities.PersonCenterActivity;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChoseTemplate extends Fragment {
    private Button choseTemplate,noTemplate,newTemplate;
    private String userId="offline";
    private ImageView clock,plan,personcenter;


    public ChoseTemplate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chose_template, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments()!=null&&getArguments().getString("userId")!=null)
        { userId=getArguments().getString("userId");
        }
        init();
        enableButtons();
    }
    public void init()
    {
        choseTemplate=getView().findViewById(R.id.import_model);
        noTemplate=getView().findViewById(R.id.import_no_model);
        clock=getView().findViewById(R.id.clock);
        plan=getView().findViewById(R.id.plan);
        personcenter=getView().findViewById(R.id.personcenter);

    }
    public void enableButtons()
    {
        choseTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Template> templates=new TemplateRepository(getContext()).getAllTemplates2(userId);
                final TemplateAdapter templateAdapter=new TemplateAdapter(templates);
                new MaterialDialog.Builder(getContext())
                        .autoDismiss(false)
                        .title("选择模板")
                        .adapter(templateAdapter,new LinearLayoutManager(getContext()))
                        .positiveText("确认")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Template selectedTemplate=templateAdapter.getSelectedTemplate();
                                if(selectedTemplate==null)
                                {
                                    Toast.makeText(getContext(),"请选择一个模板",Toast.LENGTH_SHORT);
                                }
                                else //跳转到周视图
                                {
                                    Log.d("template",selectedTemplate.getName());
                                    dialog.dismiss();
                                   Bundle bundle=new Bundle();
                                   bundle.putString("userId",userId);
                                   bundle.putString("templateName",selectedTemplate.getName());
                                    NavController controller = Navigation.findNavController(getView());
                                    controller.navigate(R.id.action_choseTemplate_to_editPlan,bundle);
                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();

                            }
                        })
                        .show();
            }
        });
        noTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("userId",userId);
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_choseTemplate_to_editPlan,bundle);
            }
        });

        personcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("userId",userId);
                intent.setClass(getContext(), PersonCenterActivity.class);
                startActivity(intent);
            }
        });
    }
    }