package comv.example.zyrmj.precious_time01.fragments.clock;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.activities.TimeActivity;

public class AddTime extends Fragment {
    private Button confirm;
    private EditText hour, minute;
    private String userId="offline";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.clock_time, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId", "offline");
        }
        init();
        enableButtons();
    }

    public void init(){
        confirm = getView ().findViewById ( R.id.button_clock_begin );
        hour = getView ().findViewById ( R.id.clock_hour_input );
        minute = getView ().findViewById ( R.id.clock_minute_input );
    }

    public void enableButtons(){
        confirm.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("hour",hour.getText ().toString ());
                intent.putExtra("minute",minute.getText ().toString ());
                System.out.println ( "hour : "+hour.getText ()+" minute : "+hour.getText () );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.setClass(getContext(), TimeActivity.class);
                startActivity(intent);
            }
        } );
    }
}
