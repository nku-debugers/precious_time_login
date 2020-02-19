package comv.example.zyrmj.precious_time01.fragments.clock;


import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.RecycleViewAdapter.WhiteAppAdapter;
import comv.example.zyrmj.precious_time01.WhiteApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhiteShow extends Fragment {

    private Button button;
   RecyclerView recyclerView;
    private String userId = "offline";
    private List<WhiteApp> whiteApps;
    final WhiteAppAdapter whiteAppAdapter=new WhiteAppAdapter();

    public WhiteShow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.white_show , container , false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated ( savedInstanceState );
        init();
        enableButtons();
    }

    public void init(){

        if(needPermissionForBlocking(getActivity ().getApplicationContext ())) {
            Intent intent = new Intent ( Settings.ACTION_USAGE_ACCESS_SETTINGS );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity ( intent );
        }
        recyclerView=getView().findViewById(R.id.white_recyclerView);
        button=getView().findViewById(R.id.white_confirm);
        initList();


    }

    private void initList()
    {
        whiteApps = getAllAppNamesPackages ();
        whiteAppAdapter.setAllWhiteApps(whiteApps);
        //后续获取个人中心的已选择的白名单 todo
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(whiteAppAdapter);

    }

    public void enableButtons(){
        button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                ArrayList<String> whitenames = whiteAppAdapter.getWhiteAppNames();
                System.out.println("whitenames: "+whitenames.toString());
                Bundle bundle = new Bundle (  );
                bundle.putString ( "hour", getArguments ().getString ( "hour" ));
                bundle.putString ( "minute", getArguments ().getString ( "minute" ));
                bundle.putString ( "kind", getArguments ().getString ( "kind" ));
                bundle.putString("userId",userId);
                bundle.putStringArrayList ( "whitenames",whitenames);
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_whiteShow_to_clockMain,bundle);
            }
        } );
        aSwitch1.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                WhiteApp app = whiteApps.get ( 0 );
                if(app.Iswhite==true){
                    app.Iswhite = false;
                }
                else app.Iswhite = true;
                whiteApps.set ( 0, app );
            }
        } );
        aSwitch2.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                WhiteApp app = whiteApps.get ( 1 );
                if(app.Iswhite==true){
                    app.Iswhite = false;
                }
                else app.Iswhite = true;
                whiteApps.set ( 1, app );
            }
        } );
        aSwitch3.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                WhiteApp app = whiteApps.get ( 2 );
                if(app.Iswhite==true){
                    app.Iswhite = false;
                }
                else app.Iswhite = true;
                whiteApps.set ( 2, app );
            }
        } );
    }

    public static boolean needPermissionForBlocking(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return  (mode != AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }

    private List<WhiteApp> getAllAppNamesPackages() {
        // TODO Auto-generated method stub
        PackageManager pm = getActivity ().getPackageManager();
        List<PackageInfo> list = pm
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        List<WhiteApp> whiteAppList = new ArrayList<> (  );
        for (PackageInfo packageInfo : list) {

            String appName = packageInfo.applicationInfo.loadLabel(
                    getActivity ().getPackageManager()).toString();
            String packageName = packageInfo.packageName;
            Drawable appIcon = packageInfo.applicationInfo.loadIcon ( getActivity ().getPackageManager () );

            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0 && packageName!="comv.example.zyrmj.precious_time01"){
                Log.i("zyn", "app_icon : " + appIcon);
                Log.i("zyn", "app_name : " + appName);
                Log.i("zyn", "app_pkt_name : " + packageName);
                WhiteApp whiteApp = new WhiteApp ();
                whiteApp.Appicon = appIcon;
                whiteApp.AppName = appName;
                whiteApp.AppPkgName = packageName;
                whiteAppList.add ( whiteApp );
            }

        }
        return whiteAppList;
    }
}
