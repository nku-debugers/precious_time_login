package comv.example.zyrmj.precious_time01.RecycleViewAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.Utils.TimeDiff;
import comv.example.zyrmj.precious_time01.entity.Todo;
import comv.example.zyrmj.precious_time01.fragments.plan.EditPlan;
import me.leefeng.promptlibrary.PromptDialog;

public class TodoAdapter2 extends RecyclerView.Adapter<TodoAdapter2.MyViewHolder> {
    private Activity currentActivity;
    private String userId = "offline";
    private List<Todo> allTodos = new ArrayList<>();
    final String[] weekLabels = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    public TodoAdapter2(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.card_plan_final, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Todo todo = allTodos.get(position);
        holder.number.setText(String.valueOf(position + 1));
        holder.name.setText(todo.getName());
        holder.weekDay.setText(todo.getStartTime().split("-")[0]);
        holder.startTime.setText(todo.getStartTime().split("-")[1]);
        holder.endTime.setText(todo.getEndTime().split("-")[1]);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("userId",userId);
                bundle.putSerializable("todo",todo);
                //查询todo的关系表，得到Label与Quote列表


                //bundle.putSerializable("selectedLabels",selectedLabels);
                //bundle.putSerializable("selectedQuotes",selectedQuotes);
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_modifyPlan_to_updateTodo,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allTodos.size();

    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView number, name, weekDay, startTime, endTime;
        ImageView imageView;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            weekDay = itemView.findViewById(R.id.weekDay);
            startTime = itemView.findViewById(R.id.start_time);
            endTime = itemView.findViewById(R.id.end_time);
            imageView=itemView.findViewById(R.id.next);
        }
    }
}
