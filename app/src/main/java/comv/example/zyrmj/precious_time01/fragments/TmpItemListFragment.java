package comv.example.zyrmj.precious_time01.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.TemplateItemAdapter;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class TmpItemListFragment extends Fragment {
    private List<TemplateItem> allTemplateItems;
    private TemplateItemRepository templateItemRepository;
    private RecyclerView recyclerView;
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
        if(getArguments()!=null)
        {  userId=getArguments().getString("userId","");
            templateName=getArguments().getString("templateName","");}

        templateItemRepository=new TemplateItemRepository(getContext());
        recyclerView=getView().findViewById(R.id.recycleView);
        final TemplateItemAdapter templateItemAdapter=new TemplateItemAdapter();
        allTemplateItems= templateItemRepository.getSpecificList(templateName,userId);
        templateItemAdapter.setAllTemplateItems(allTemplateItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(templateItemAdapter);




    }
}
