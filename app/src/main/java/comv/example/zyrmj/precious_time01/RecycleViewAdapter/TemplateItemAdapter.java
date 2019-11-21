package comv.example.zyrmj.precious_time01.RecycleViewAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;

public class TemplateItemAdapter extends RecyclerView.Adapter<TemplateItemAdapter.MyViewHolder> {
    List<TemplateItem> allTemplateItems=new ArrayList<>();
    static class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView number,name,startTime,endTime;
        //     定义View中的所有组件
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            初始化所有组件
            cardView=itemView.findViewById(R.id.cardview);
            number=itemView.findViewById(R.id.number);
            name=itemView.findViewById(R.id.name);
            startTime=itemView.findViewById(R.id.startTime);
            endTime=itemView.findViewById(R.id.endTime);
        }
    }
public void setAllTemplateItems(List<TemplateItem> templateItems){
        this.allTemplateItems=templateItems;
}

    @NonNull
    @Override
    //加载ViewHolder布局
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.tmpitem_card_view,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TemplateItem templateItem=allTemplateItems.get(position);
//      下面开始利用template中的信息对Viewholder布局中的组件如textView等进行赋值
        holder.number.setText(String.valueOf(position+1));
        holder.name.setText(templateItem.getItemName());
        holder.startTime.setText(templateItem.getStartTime());
        holder.endTime.setText(templateItem.getEndTime());

//        定义itemView点击监听器等触发事件
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }



    @Override
    public int getItemCount() {
        return allTemplateItems.size();
    }



}
