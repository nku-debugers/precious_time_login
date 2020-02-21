package comv.example.zyrmj.precious_time01.fragments.personCenter;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.TemplateItemAdapter;
import comv.example.zyrmj.precious_time01.ViewModel.TemplateItemViewModel;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class TmpItemListFragment extends Fragment {
    private List<TemplateItem> allTemplateItems;
    private TemplateItemRepository templateItemRepository;
    private RecyclerView recyclerView;
    private Switch chageView;
    private TextView title;
    private FloatingActionButton add;
    private ImageView toTemplateView;
    String userId;
    String templateName;

    public TmpItemListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tmpitemlistview, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId", "");
            templateName = getArguments().getString("templateName", "");
        }

        templateItemRepository = new TemplateItemRepository(getContext());
        recyclerView = getView().findViewById(R.id.tmpitemlist_recycleView);
        final TemplateItemAdapter templateItemAdapter = new TemplateItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(templateItemAdapter);
        TemplateItemViewModel templateItemViewModel = ViewModelProviders.of(getActivity()).get(TemplateItemViewModel.class);
        templateItemViewModel.getSpecificTemplateItems(templateName, userId).observe(getActivity(), new Observer<List<TemplateItem>>() {


            @Override
            public void onChanged(List<TemplateItem> templateItems) {
                templateItemAdapter.setAllTemplateItems(templateItems);
                templateItemAdapter.notifyDataSetChanged();
                allTemplateItems = templateItems;
            }
        });
        chageView = getView().findViewById(R.id.week_switch );
        chageView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userId);
                    bundle.putString("templateName", templateName);
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_tmpItemListFragment_to_testWeekView, bundle);
                } else {


                }
            }
        });
        title = getView().findViewById(R.id.title);
        title.setText(templateName);
        add = getView().findViewById(R.id.add );
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(getView());
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("templateName", templateName);
                bundle.putString("viewOption", "1");
                controller.navigate(R.id.action_tmpItemListFragment_to_addTemplateItem, bundle);
            }
        });
        toTemplateView = getView().findViewById(R.id.toTemplateView);
        toTemplateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_tmpItemListFragment_to_templateShowFragment);
            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_tmpItemListFragment_to_templateShowFragment);
                    return true;
                }
                return false;
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.END) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final TemplateItem templateItemToDelete = allTemplateItems.get(viewHolder.getAdapterPosition());
                templateItemRepository.deleteTemplateItems(templateItemToDelete);
                Snackbar.make(getView(), "删除了一个模板事项", Snackbar.LENGTH_SHORT).
                        setAction("撤销", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        templateItemRepository.insertTemplateItems(templateItemToDelete);
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
}
