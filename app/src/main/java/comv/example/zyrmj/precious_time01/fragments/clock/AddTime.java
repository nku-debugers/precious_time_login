package comv.example.zyrmj.precious_time01.fragments.clock;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.WhiteApp;

public class AddTime extends Fragment {
    private Button confirm;
    private ImageView back;
    private EditText hour, minute;
    private String userId="offline";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        back = getView ().findViewById ( R.id.clock_time_back );
        hour = getView ().findViewById ( R.id.clock_hour_input );
        minute = getView ().findViewById ( R.id.clock_minute_input );
    }

    public void enableButtons(){
        confirm.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String h = hour.getText ().toString ();
                String m =minute.getText ().toString ();
                if( TextUtils.isEmpty ( h ) ){
                    h = "0";
                }
                if( TextUtils.isEmpty ( m ) ){
                    m = "0";
                }
                Bundle bundle = new Bundle (  );
                bundle.putString("kind",getArguments().getString("kind"));
                bundle.putInt ( "single", getArguments ().getInt ( "single" ));
                bundle.putString ( "hour", h );
                bundle.putString ( "minute", m );
                if(getArguments().getString("kind").equals ( "1" )){
                    ArrayList<String> whitenames = new ArrayList<> ();
                    bundle.putStringArrayList ( "whitenames", whitenames);
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_addTime_to_clockMain, bundle);
                }
                else{
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_addTime_to_whiteShow, bundle);
                }
            }
        } );

        back.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle (  );
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_addTime_to_choseClock, bundle);
            }
        } );
    }
}
