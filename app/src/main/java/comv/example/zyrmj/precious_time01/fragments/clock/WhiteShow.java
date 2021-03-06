package comv.example.zyrmj.precious_time01.fragments.clock;


import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private ImageView back;
    RecyclerView recyclerView;
    private String userId = "offline";
    private List<WhiteApp> whiteApps;
    final WhiteAppAdapter whiteAppAdapter = new WhiteAppAdapter();

    public WhiteShow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_white_show, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        enableButtons();
    }

    public void init() {

        if (needPermissionForBlocking(getActivity().getApplicationContext())) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        recyclerView = getView().findViewById(R.id.white_recyclerView);
        button = getView().findViewById(R.id.white_confirm);
        back = getView().findViewById(R.id.white_show_back);
        initList();


    }

    private void initList() {
        whiteApps = getAllAppNamesPackages();
        whiteAppAdapter.setAllWhiteApps(whiteApps);
        List<WhiteApp> selectedWhiteApps = new ArrayList<>();
        //从SharePreference中读取已选中的白名单app
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("whiteApps", Context.MODE_PRIVATE);
        for (WhiteApp whiteApp : whiteApps) {
            String isWhite = sharedPreferences.getString(whiteApp.AppName, "");
            if (isWhite.equals("true")) {
                selectedWhiteApps.add(whiteApp);
            }
        }
        whiteAppAdapter.setSelectedWhiteApps(selectedWhiteApps);

        //后续获取个人中心的已选择的白名单 todo
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(whiteAppAdapter);
        recyclerView.setItemViewCacheSize(whiteApps.size() - 4);
        //解决RecyclerView点击一个item，后面每间隔9个item就会触发一次同样的事件的问题

    }

    public void enableButtons() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSharedPreference();
                ArrayList<String> whitenames = whiteAppAdapter.getWhiteAppPkgNames();
                Bundle bundle = new Bundle();
                bundle.putString("hour", getArguments().getString("hour"));
                bundle.putString("minute", getArguments().getString("minute"));
                bundle.putString("kind", getArguments().getString("kind"));
                bundle.putInt("single", getArguments().getInt("single"));
                bundle.putString("userId", userId);
                bundle.putSerializable("whitenames", whitenames);
                NavController controller = Navigation.findNavController(getView());
                controller.navigate(R.id.action_whiteShow_to_clockMain, bundle);
            }
        });

        back.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle (  );
                bundle.putInt("single", getArguments().getInt("single"));
                bundle.putString("userId", userId);
                if(getArguments ().getInt ( "single" ) == 1){
                    bundle.putString("kind", getArguments().getString("kind"));
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_whiteShow_to_addTime, bundle);
                }
                else{
                    bundle.putString("hour", getArguments().getString("hour"));
                    bundle.putString("minute", getArguments().getString("minute"));
                    bundle.putString ( "todoName",getArguments().getString("todoName") );
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_whiteShow_to_choseClock, bundle);
                }
            }
        } );

    }

    private void updateSharedPreference() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("whiteApps", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<WhiteApp> selectedWhiteApps = whiteAppAdapter.getSelectedWhiteApps();
        for (WhiteApp whiteApp : whiteApps) {

            int flag = 0;
            String isWhite = sharedPreferences.getString(whiteApp.AppName, "");
            for (WhiteApp selected : selectedWhiteApps) {
                if (whiteApp.AppName.equals(selected.AppName)) {
                    if (isWhite.equals("")) {
                        editor.putString(whiteApp.AppName, "true");
                    }
                    flag = 1;
                    break;
                }
            }
            if (isWhite.equals("true") && flag == 0) {
                editor.remove(whiteApp.AppName);

            }

        }
        editor.apply();

    }

    public static boolean needPermissionForBlocking(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode != AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }

    private List<WhiteApp> getAllAppNamesPackages() {
        // TODO Auto-generated method stub
        PackageManager pm = getActivity().getPackageManager();
        List<PackageInfo> list = pm
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        List<WhiteApp> whiteAppList = new ArrayList<>();
        for (PackageInfo packageInfo : list) {

            String appName = packageInfo.applicationInfo.loadLabel(
                    getActivity().getPackageManager()).toString();
            String packageName = packageInfo.packageName;
            Drawable appIcon = packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager());

            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0 && !packageName.equals("comv.example.zyrmj.precious_time01")) {
                Log.i("zyn", "app_icon : " + appIcon);
                Log.i("zyn", "app_name : " + appName);
                Log.i("zyn", "app_pkt_name : " + packageName);
                WhiteApp whiteApp = new WhiteApp();
                whiteApp.Appicon = appIcon;
                whiteApp.AppName = appName;
                whiteApp.AppPkgName = packageName;
                whiteAppList.add(whiteApp);
            }

        }
        return whiteAppList;
    }
}
