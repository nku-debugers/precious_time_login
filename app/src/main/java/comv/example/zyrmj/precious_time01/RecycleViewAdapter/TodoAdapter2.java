package comv.example.zyrmj.precious_time01.RecycleViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import comv.example.zyrmj.precious_time01.activities.ClockActivity;
import comv.example.zyrmj.precious_time01.entity.Plan;
import comv.example.zyrmj.precious_time01.entity.Todo;
import comv.example.zyrmj.precious_time01.fragments.plan.EditPlan;
import comv.example.zyrmj.precious_time01.repository.TodoRepository;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

public class TodoAdapter2 extends RecyclerView.Adapter<TodoAdapter2.MyViewHolder> {
   private String userId = "offline";
    private List<Todo> allTodos = new ArrayList<>();
    private int modify=0;
    private Plan showedPlan;
    private TodoRepository todoRepository;
    private Activity currActivity;

    public Activity getCurrActivity() {
        return currActivity;
    }

    public void setCurrActivity(Activity currActivity) {
        this.currActivity = currActivity;
    }

    public TodoRepository getTodoRepository() {
        return todoRepository;
    }

    public void setTodoRepository(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Plan getShowedPlan() {
        return showedPlan;
    }

    public void setShowedPlan(Plan showedPlan) {
        this.showedPlan = showedPlan;
    }

    public List<Todo> getAllTodos() {
        return allTodos;
    }

    public void setAllTodos(List<Todo> allTodos) {
        this.allTodos = allTodos;
    }

    public int getModify() {
        return modify;
    }

    public void setModify(int modify) {
        this.modify = modify;
    }

    final String[] weekLabels = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    public TodoAdapter2() {

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
        int index=Integer.valueOf(todo.getStartTime().split("-")[0]);
        holder.weekDay.setText(weekLabels[index]);
        holder.startTime.setText(todo.getStartTime().split("-")[1]);
        holder.endTime.setText(todo.getEndTime().split("-")[1]);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modify == 1 && todo.getType() != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mytodo", todo);
                    bundle.putSerializable("plan", showedPlan);
                    bundle.putString("userId", userId);
                    bundle.putInt("modify", 1);
                    bundle.putString("weekView", "true");
                    bundle.putString("option", "update");
                    //deleteTodo
                    todoRepository.deleteTodo(todo);
                    todoRepository.deleteCategory(todo.getUserId(), todo.getPlanDate(), todo.getStartTime());
                    todoRepository.deleteQuote(todo.getUserId(), todo.getPlanDate(), todo.getStartTime());
                    NavController controller = Navigation.findNavController(view);
                    controller.navigate(R.id.action_planTodosListView_to_updateTodoAfterPlanned, bundle);
                }
                if (modify == 0 && todo.getType() != 0) {
                    PromptDialog promptDialog = new PromptDialog(currActivity);
                    PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
                        @Override
                        public void onClick(PromptButton button) {
                            //todo 2/14 计算任务时长,传递给手机管控模块
                            Intent intent = new Intent();
                            intent.putExtra("userId", userId);
                            //传递任务时长
                            intent.setClass(currActivity, ClockActivity.class);
                            currActivity.startActivity(intent);
                        }
                    });
                    PromptButton cancel = new PromptButton("取消", new PromptButtonListener() {
                        @Override
                        public void onClick(PromptButton button) {
                            //Nothing
                        }
                    });
                    confirm.setTextColor(Color.parseColor("#DAA520"));
                    confirm.setFocusBacColor(Color.parseColor("#FAFAD2"));
                    promptDialog.showWarnAlert("开始进行此项活动？", cancel, confirm);
                }
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



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            weekDay = itemView.findViewById(R.id.weekDay);
            startTime = itemView.findViewById(R.id.start_time);
            endTime = itemView.findViewById(R.id.end_time);
        }
    }
}
