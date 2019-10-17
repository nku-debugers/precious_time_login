package comv.example.zyrmj.precious_time01;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.TemplateAdapter;
import comv.example.zyrmj.precious_time01.ViewModel.TemplateViewModel;
import comv.example.zyrmj.precious_time01.entity.Template;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    }
}
