package comv.example.zyrmj.precious_time01.fragments.clock;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import comv.example.zyrmj.precious_time01.R;

public class AddTime extends Fragment {
    private Button confirm;
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
                bundle.putString ( "hour", h );
                bundle.putString ( "minute", m );
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_addTime_to_whiteShow, bundle);
            }
        } );
    }
}
