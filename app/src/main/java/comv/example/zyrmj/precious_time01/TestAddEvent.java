package comv.example.zyrmj.precious_time01;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestAddEvent extends Fragment {


    public TestAddEvent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_add_event, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        TemplateItem t5 = new TemplateItem("offline", "test6", "study"
//                , "test", "4-8:00", "4-7:00");
//         new TemplateItemRepository(getContext()).insertTemplateItems(t5);

    }

}
