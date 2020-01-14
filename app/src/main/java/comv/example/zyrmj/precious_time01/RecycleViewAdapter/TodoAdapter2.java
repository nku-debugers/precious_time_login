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
    private ArrayList<EditPlan.ToDoExtend> unsatisfiedTodos = new ArrayList<>();
    private ArrayList<EditPlan.ToDoExtend> satisfiedTodos = new ArrayList<>();
    final String[] weekLabels = {"一", "二", "三", "四", "五", "六", "日"};

    public TodoAdapter2(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<EditPlan.ToDoExtend> getUnsatisfiedTodos() {
        return unsatisfiedTodos;
    }

    public void setUnsatisfiedTodos(ArrayList<EditPlan.ToDoExtend> unsatisfiedTodos) {
        this.unsatisfiedTodos = unsatisfiedTodos;
        allTodos = new ArrayList<>();
        for (EditPlan.ToDoExtend toDoExtend : unsatisfiedTodos) {
            allTodos.add(toDoExtend.getTodo());
        }
    }

    public List<EditPlan.ToDoExtend> getSatisfiedTodos() {
        return satisfiedTodos;
    }

    public void setSatisfiedTodos(ArrayList<EditPlan.ToDoExtend> satisfiedTodos) {
        this.satisfiedTodos = satisfiedTodos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.card_plan_modify, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Todo todo = allTodos.get(position);
        holder.number.setText(String.valueOf(position + 1));
        holder.name.setText(todo.getName());
        //根据startTime判断不同类型
        //存在weekday
        if (todo.getStartTime().contains("-")) {
            holder.weekDay.setText(weekLabels[Integer.valueOf(todo.getStartTime().split("-")[0])]);
            if (todo.getStartTime().contains(":")) {
                holder.startTime.setText(todo.getStartTime().split("-")[1]);
                holder.endTime.setText(todo.getEndTime().split("-")[1]);
                holder.times.setText("");
            } else {
                holder.times.setText((todo.getStartTime().split("-")[1]).split(" ")[0]);
                holder.startTime.setText("");
                holder.endTime.setText("");
            }
        }

        //不存在weekDay  habit未完成的todo
        else {
            holder.times.setText(todo.getStartTime().split(" ")[0]);
            holder.weekDay.setText("");
            holder.startTime.setText("");
            holder.endTime.setText("");
        }

        holder.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断具体时间是否填写
                if (!holder.startTime.getText().toString().contains(":")) {
                    PromptDialog promptDialog = new PromptDialog(currentActivity);
                    promptDialog.showWarn("请填写具体时间！");

                } else {
                    System.out.println("conflict");
                    System.out.println(holder.weekDay.getText().toString());
                    System.out.println(holder.startTime.getText().toString());
                    System.out.println(holder.endTime.getText().toString());
                    //判断是否与现有时间冲突
                    if (!checkTimeConflict(todo.getStartTime().split("-")[0],
                            holder.startTime.getText().toString(), holder.endTime.getText().toString())) {
                        PromptDialog promptDialog = new PromptDialog(currentActivity);
                        promptDialog.showWarn("存在时间冲突！");
                    } else {
                        EditPlan.ToDoExtend toDoExtend = unsatisfiedTodos.get(position);
                        unsatisfiedTodos.remove(toDoExtend);
                        satisfiedTodos.add(toDoExtend);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userId", userId);
                        bundle.putSerializable("unsatisfiedTodos", unsatisfiedTodos);
                        bundle.putSerializable("satisfiedTodos", satisfiedTodos);
                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_modifyPlan_self, bundle);

                    }

                }


            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("card","true");
                bundle.putString("option","update");
                bundle.putSerializable("userId", userId);
                bundle.putSerializable("unsatisfiedTodos", unsatisfiedTodos);
                System.out.println("unsatisfied: "+String.valueOf(unsatisfiedTodos.size()));
                bundle.putSerializable("satisfiedTodos", satisfiedTodos);
                bundle.putString("index",String.valueOf(position));
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_modifyPlan_to_updateTodo,bundle);


            }
        });
    }

    @Override
    public int getItemCount() {
        return unsatisfiedTodos.size();
    }

    private boolean checkTimeConflict(String weekDay, String startTime, String endTime) {
        String alreadyExistStart;
        String alreadyExistEnd;
        List<Todo> todos = new ArrayList<>();
        for (EditPlan.ToDoExtend toDoExtend : satisfiedTodos) {
            todos.add(toDoExtend.getTodo());
        }
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getStartTime().substring(0, 1).equals(weekDay)) {
                alreadyExistStart = todos.get(i).getStartTime().substring(2);
                alreadyExistEnd = todos.get(i).getEndTime().substring(2);

                if (!checkExceedStart(alreadyExistStart, alreadyExistEnd, startTime)) {
                    return false;
                }
                if (!checkExceedEnd(alreadyExistStart, alreadyExistEnd, endTime)) {
                    return false;
                }
                if ( !checkIfContained(alreadyExistStart, alreadyExistEnd, startTime, endTime)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkExceedStart(String start, String end, String ready) {
        if (TimeDiff.compare(start, ready) == 0) {
            return false;
        }
        if ((TimeDiff.compare(start, ready) == -1) && TimeDiff.compare(end, ready) == 1) {
            return false;
        }
        return true;
    }

    private boolean checkExceedEnd(String start, String end, String ready) {
        if (TimeDiff.compare(end, ready) == 0) {
            return false;
        }
        if ((TimeDiff.compare(start, ready) == -1) && TimeDiff.compare(end, ready) == 1) {
            return false;
        }
        return true;
    }

    private boolean checkIfContained(String start, String end, String realStart, String realEnd) {
        return TimeDiff.compare(start, realStart) < 0 || TimeDiff.compare(end, realEnd) > 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView number, name, weekDay, startTime, endTime, times;
        ImageView up;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            weekDay = itemView.findViewById(R.id.weekDay);
            startTime = itemView.findViewById(R.id.start_time);
            endTime = itemView.findViewById(R.id.end_time);
            times = itemView.findViewById(R.id.times);
            up = itemView.findViewById(R.id.up);


        }
    }
}
