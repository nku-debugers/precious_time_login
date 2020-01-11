package comv.example.zyrmj.precious_time01.fragments.plan;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import comv.example.zyrmj.precious_time01.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddToDo extends Fragment {


    public AddToDo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_to_do, container, false);
    }

}
