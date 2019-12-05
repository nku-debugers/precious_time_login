package comv.example.zyrmj.precious_time01.RecycleViewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.Habit;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.MyViewHolder> {
  private List<Habit> allHabits=new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.card_habit, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
final Habit habit=allHabits.get(position);
holder.number.setText(String.valueOf(position+1));
holder.habitName.setText(habit.getName());
holder.completion.setProgress((int) habit.getCompletion());
    }

    @Override
    public int getItemCount() {
        return allHabits.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView number,habitName;
        ProgressBar completion;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardview);
            number=itemView.findViewById(R.id.number);
            habitName=itemView.findViewById(R.id.habitName);
            completion=itemView.findViewById(R.id.completion);
        }
    }
    public void setAllHabits(List<Habit> allHabits)
    {
        this.allHabits=allHabits;
    }
    }

