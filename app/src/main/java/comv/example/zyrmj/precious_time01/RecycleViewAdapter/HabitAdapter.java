package comv.example.zyrmj.precious_time01.RecycleViewAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.dao.CategoryDao;
import comv.example.zyrmj.precious_time01.dao.HabitDao;
import comv.example.zyrmj.precious_time01.database.AppDatabase;
import comv.example.zyrmj.precious_time01.entity.Category;
import comv.example.zyrmj.precious_time01.entity.Habit;
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.relations.HabitCategory;
import comv.example.zyrmj.precious_time01.repository.CategoryRepository;
import comv.example.zyrmj.precious_time01.repository.HabitRepository;
import comv.example.zyrmj.precious_time01.repository.QuoteRepository;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.MyViewHolder> {
  private List<Habit> allHabits=new ArrayList<>();
  private CategoryRepository categoryRepository;
  private QuoteRepository quoteRepository;
  private HabitRepository habitRepository;
  private String userId="offline";

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

//        定义itemView点击监听器等触发事件
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询关系信息
                //HabitCategory
                List<Category> categories=categoryRepository.getAllCateories(userId);
                List<String> selectedCategories=habitRepository.getCategories(userId,habit.getName());
                ArrayList<Integer> selectedIndex=new ArrayList<>();
                for(String sc:selectedCategories)
                {
                    for(int i=0;i<categories.size();i++)
                    {
                        if(sc.equals(categories.get(i).getName()))
                        {
                            selectedIndex.add(i);
                            break;
                        }
                    }
                }
                //HabitQuote
                List<Quote> quotes=quoteRepository.getAllQuotes2(userId);
                List<String> selectedQuoteStrings=habitRepository.getQuotes(userId,habit.getName());
                ArrayList<Quote>  selectedQuotes=new ArrayList<>();
                for(String sq:selectedQuoteStrings)
                {
                    for(Quote q:quotes)
                    {
                        if(sq.equals(q.getWords()))
                        {
                           selectedQuotes.add(q);
                            break;
                        }
                    }

                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("theHabit", habit);
                bundle.putSerializable("selectedIndex",selectedIndex);
                bundle.putSerializable("selectedQuotes",selectedQuotes);
                bundle.putString("isUpdate","1");
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_habitShow_to_addHabit1, bundle);
            }
        });


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
            number=itemView.findViewById(R.id.habit_number);
            habitName=itemView.findViewById(R.id.habit_name);
            completion=itemView.findViewById(R.id.completion);
        }
    }
    public void setAllHabits(List<Habit> allHabits)
    {
        this.allHabits=allHabits;
    }
    public void setAllRepository(CategoryRepository categoryRepository,QuoteRepository quoteRepository,HabitRepository habitRepository)
    {
        this.categoryRepository=categoryRepository;
        this.quoteRepository=quoteRepository;
        this.habitRepository=habitRepository;
    }
    public void setUserId(String userId)
    {
        this.userId=userId;
    }

    }

