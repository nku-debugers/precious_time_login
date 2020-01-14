package comv.example.zyrmj.precious_time01.RecycleViewAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.Template;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.MyViewHolder> {
    private String option="0";
    private List<Quote> allQutoes = new ArrayList<>();
    private List<Quote> selectedQuotes=new ArrayList<>();

    public QuoteAdapter(List<Quote> allQutoes) {
        this.option="1";
        this.allQutoes = allQutoes;
    }

    public QuoteAdapter() {

    }



    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView number, words, author;
        ImageView next;
        CheckBox checked;

        //     定义View中的所有组件
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            初始化所有组件
            cardView = itemView.findViewById(R.id.cardview);
            number = itemView.findViewById(R.id.quote_number);
            words = itemView.findViewById(R.id.words);
            author = itemView.findViewById(R.id.author);
            next = itemView.findViewById(R.id.next);
            checked=itemView.findViewById(R.id.checked);
        }
    }

    public void setAllQutoes(List<Quote> allQutoes) {
        this.allQutoes = allQutoes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.card_quote, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Quote quote = allQutoes.get(position);
//      下面开始利用template中的信息对Viewholder布局中的组件如textView等进行赋值
        holder.number.setText(String.valueOf(position + 1));
        holder.words.setText(quote.getWords());
        holder.author.setText(quote.getAuthor());

//        定义itemView点击监听器等触发事件  更新quote
        if(option.equals("0"))
        {   holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("quote", quote);
                bundle.putString("userId",quote.getUserId());
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_quoteFragment_to_updateQuoteFragment, bundle);

            }
        });}
        else
        {
            holder.next.setVisibility(View.INVISIBLE);
            holder.checked.setVisibility(View.VISIBLE);
            holder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked){
                        int flag=0;//是否已存在
                        for (Quote q:selectedQuotes)
                        { if(q.getWords().equals(quote.getWords()))
                        {flag=1;
                        break;
                        }
                        }
                           if(flag!=1)
                        selectedQuotes.add(quote);
                    }else{
                        for(Quote q:selectedQuotes) {
                            if(q.getWords().equals(quote.getWords())) {
                                selectedQuotes.remove(q);
                                break;
                            }
                        }
                    }

                }
            });
            for (Quote q:selectedQuotes)
            {
                if (holder.words.getText().toString().equals(q.getWords()))
                {
                    holder.checked.setChecked(true);
                    break;
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return allQutoes.size();
    }
    public List<Quote> getSelectedQuotes()
    {
        return selectedQuotes;
    }
    public void  setSelectedQuotes(List<Quote> selectedQuotes)
    {
        this.selectedQuotes=selectedQuotes;

    }
}
