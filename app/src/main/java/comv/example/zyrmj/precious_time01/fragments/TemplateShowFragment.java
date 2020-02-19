package comv.example.zyrmj.precious_time01.fragments;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.TemplateAdapter;
import comv.example.zyrmj.precious_time01.ViewModel.TemplateViewModel;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;

import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class TemplateShowFragment extends Fragment {
    private List<Template> allTemplates;
    private TemplateRepository templateRepository;
    private RecyclerView recyclerView;
    //需从个人中心fragment传递userId参数,这里为了测试，默认使用offline
    private String userId = "offline";

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
        enableBackButton();
        templateRepository = new TemplateRepository(getContext());
        recyclerView = getView().findViewById(R.id.template_recycleView);
        final TemplateAdapter templateAdapter = new TemplateAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(templateAdapter);
        TemplateViewModel templateViewModel = ViewModelProviders.of(getActivity()).get(TemplateViewModel.class);
        templateViewModel.getAllTemplates(userId).observe(getActivity(), new Observer<List<Template>>() {
            @Override
            public void onChanged(List<Template> templates) {
                templateAdapter.setAllTemplates(templates);
                templateAdapter.notifyDataSetChanged();
                allTemplates = templates;
            }
        });

        ImageView returnimage = getView().findViewById(R.id.returnimage);
        returnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_templateShowFragment_to_personCenterFragment);
            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_templateShowFragment_to_personCenterFragment);
                    return true;
                }
                return false;
            }
        });

        FloatingActionButton addTemplate = getView().findViewById(R.id.addTemplate);
        addTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("请输入模板名称", templateRepository);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.END) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Template templateToDelete = allTemplates.get(viewHolder.getAdapterPosition());
                String templateName = templateToDelete.getName();
                String useId = templateToDelete.getUserId();
                TemplateItemRepository templateItemRepository = new TemplateItemRepository(getContext());
                List<TemplateItem> templateItems = templateItemRepository.getSpecificList(templateName, useId);
                for (int i = 0; i < templateItems.size(); i++) {
                    templateItemRepository.deleteTemplateItems(templateItems.get(i));

                }
                templateRepository.deleteTemplates(templateToDelete);
                Snackbar.make(getView(), "删除了一个模板", Snackbar.LENGTH_SHORT).
                        setAction("撤销", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        templateRepository.insertTemplates(templateToDelete);
                                    }
                                }
                        ).show();

            }
            //在滑动的时候，画出浅灰色背景和垃圾桶图标，增强删除的视觉效果

            Drawable icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_delete_black_24dp);
            Drawable background = new ColorDrawable(Color.LTGRAY);

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;

                int iconLeft, iconRight, iconTop, iconBottom;
                int backTop, backBottom, backLeft, backRight;
                backTop = itemView.getTop();
                backBottom = itemView.getBottom();
                iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                iconBottom = iconTop + icon.getIntrinsicHeight();
                if (dX > 0) {
                    backLeft = itemView.getLeft();
                    backRight = itemView.getLeft() + (int) dX;
                    background.setBounds(backLeft, backTop, backRight, backBottom);
                    iconLeft = itemView.getLeft() + iconMargin;
                    iconRight = iconLeft + icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                } else if (dX < 0) {
                    backRight = itemView.getRight();
                    backLeft = itemView.getRight() + (int) dX;
                    background.setBounds(backLeft, backTop, backRight, backBottom);
                    iconRight = itemView.getRight() - iconMargin;
                    iconLeft = iconRight - icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                } else {
                    background.setBounds(0, 0, 0, 0);
                    icon.setBounds(0, 0, 0, 0);
                }
                background.draw(c);
                icon.draw(c);

            }
        }).attachToRecyclerView(recyclerView);
    }
    private void enableBackButton() {
        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_templateShowFragment_to_personCenterFragment);
                    return true;
                }
                return false;
            }
        });
    }

    public void showDialog(String info, final TemplateRepository templateRepository) {
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
                        if (input.toString().equals("")) {
                            dialog.setContent("模板名不能为空，请重新输入！");
                        } else {
                            String templateName = input.toString();
                            //查找是否有同名Template存在
                            if (templateRepository.getSpecificTemplate(userId, templateName) != null) {
                                Log.i("dialog", "存在同名模板");
                                dialog.setContent("存在同名模板，请重新输入！");
                            } else {
                                Log.i("dialog", "add template");
                                dialog.dismiss();
                                templateRepository.insertTemplates(new Template(userId, templateName));
                                Bundle bundle = new Bundle();
                                bundle.putString("userId", userId);
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
                .autoDismiss(false).show();
    }
}






