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

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.MyViewHolder> {
    List<Template> allTemplates = new ArrayList<>();

    public void setAllTemplates(List<Template> allTemplates) {
        this.allTemplates = allTemplates;
    }

    @NonNull
    @Override
    //加载ViewHolder布局
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.card_model, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Template template = allTemplates.get(position);
//      下面开始利用template中的信息对Viewholder布局中的组件如textView等进行赋值
        holder.number.setText(String.valueOf(position + 1));
        holder.name.setText(template.getName());
        holder.userId = template.getUserId();

//        定义itemView点击监听器等触发事件
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", holder.userId);
                bundle.putString("templateName", holder.name.getText().toString());
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_templateShowFragment_to_testWeekView, bundle);
//               获取template完整的数据库中的信息 用Bundle传递数据，跳转到update和delete fragment
            }
        });
    }

    @Override
    public int getItemCount() {
        return allTemplates.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView number, name;
        String userId;

        //     定义View中的所有组件
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            初始化所有组件
            cardView = itemView.findViewById(R.id.cardview);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
        }
    }


}
