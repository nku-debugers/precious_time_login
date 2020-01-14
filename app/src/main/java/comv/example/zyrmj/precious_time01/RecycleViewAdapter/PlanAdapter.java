package comv.example.zyrmj.precious_time01.RecycleViewAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.Plan;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.MyViewHolder> {
    private List<Plan> allPlans;
    private int belongto;//0-weekView 1-listView

    public int getBelongto() {
        return belongto;
    }

    public void setBelongto(int belongto) {
        this.belongto = belongto;
    }

    public List<Plan> getAllPlans() {
        return allPlans;
    }

    public void setAllPlans(List<Plan> allPlans) {
        this.allPlans = allPlans;
    }

    public PlanAdapter() {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.card_plan, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Plan plan=allPlans.get(position);
        holder.number.setText(String.valueOf(position+1));
        holder.name.setText(plan.getPlanName());
        holder.startDate.setText(plan.getStartDate());
        holder.endDate.setText(plan.getEndDate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("userId",plan.getUserId());
                bundle.putSerializable("plan",plan);
                NavController controller= Navigation.findNavController(view);
                if (belongto==0) {
                    controller.navigate(R.id.action_planShow_to_planWeekView, bundle);
                } else {
                    controller.navigate(R.id.action_planShow_to_planTodosListView, bundle);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return allPlans.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        TextView number, name, startDate,endDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            startDate=itemView.findViewById(R.id.start_date);
            endDate=itemView.findViewById(R.id.end_date);

        }
    }
}
