package comv.example.zyrmj.precious_time01.RecycleViewAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.WhiteApp;
import comv.example.zyrmj.precious_time01.entity.Quote;

public class WhiteAppAdapter extends RecyclerView.Adapter<WhiteAppAdapter.MyViewHolder> {
    public List<WhiteApp> getAllWhiteApps() {
        return allWhiteApps;
    }

    public void setAllWhiteApps(List<WhiteApp> allWhiteApps) {
        this.allWhiteApps = allWhiteApps;
    }

    public List<WhiteApp> getSelectedWhiteApps() {

        System.out.println("white size "+selectedWhiteApps.size());
        return selectedWhiteApps;
    }

    public void setSelectedWhiteApps(List<WhiteApp> selectedWhiteApps) {
        this.selectedWhiteApps = selectedWhiteApps;
    }

    private List<WhiteApp> allWhiteApps=new ArrayList<>();
    private List<WhiteApp> selectedWhiteApps=new ArrayList<>();
    private ArrayList<String> whiteAppPkgNames =new ArrayList<>();//返回所有选中App的name

    public ArrayList<String> getWhiteAppPkgNames() {
        return whiteAppPkgNames;
    }

    public void setWhiteAppPkgNames(ArrayList<String> whiteAppPkgNames) {
        this.whiteAppPkgNames = whiteAppPkgNames;
    }

    // 读取个人中心设置白名单，设定默认选中项
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.card_white, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final  WhiteApp whiteApp=allWhiteApps.get(position);
        holder.appName.setText(whiteApp.AppName);
        holder.appIcon.setImageDrawable(whiteApp.Appicon);
        holder.allowed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if(b==true)
               {
                   int flag=0;//是否已存在
                   for(WhiteApp whiteApp1:selectedWhiteApps)
                   {
                       if(whiteApp1.AppName.equals(whiteApp.AppName))
                       {
                           flag=1;
                           break;
                       }
                   }
                   if(flag==0)
                   {  whiteAppPkgNames.add(whiteApp.AppPkgName);
                    selectedWhiteApps.add(whiteApp);
                   }


               }
               else
               {
                   for(String name: whiteAppPkgNames)
                   {
                       if(name.equals(whiteApp.AppPkgName))
                       {
                           whiteAppPkgNames.remove(name);
                           break;
                       }
                   }
                   for(WhiteApp wa:selectedWhiteApps)
                   {
                       if(wa.AppName.equals(whiteApp.AppName))
                       {
                           System.out.println("delete white");
                           selectedWhiteApps.remove(wa);
                           System.out.println("white size"+selectedWhiteApps.size());
                           break;

                       }

                   }

               }

            }
        });

        for (WhiteApp wa:selectedWhiteApps)
        {
            if (holder.appName.getText().toString().equals(wa.AppName))
            {
                holder.allowed.setChecked(true);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return allWhiteApps.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView appName;
        ImageView appIcon;
        Switch allowed;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardview_white);
            appName=itemView.findViewById(R.id.app_name);
            appIcon=itemView.findViewById(R.id.app_icon);
            allowed=itemView.findViewById(R.id.allowed);
        }
    }
    }

