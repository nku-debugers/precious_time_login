package comv.example.zyrmj.precious_time01.RecycleViewAdapter;

import android.os.Bundle;
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
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.entity.Template;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.MyViewHolder> {
   private List<Quote> allQutoes=new ArrayList<>();
    static class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView number,words,author;
        ImageView next;
        //     定义View中的所有组件
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            初始化所有组件
            cardView=itemView.findViewById(R.id.cardview);
            number=itemView.findViewById(R.id.number);
            words=itemView.findViewById(R.id.words);
            author=itemView.findViewById(R.id.author);
            next=itemView.findViewById(R.id.next);
        }
    }
    public void setAllQutoes(List<Quote> allQutoes){this.allQutoes=allQutoes;}
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.card_quote,parent,false);
        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
       final Quote quote=allQutoes.get(position);
//      下面开始利用template中的信息对Viewholder布局中的组件如textView等进行赋值
        holder.number.setText(String.valueOf(position+1));
        holder.words.setText(quote.getWords());
       holder.author.setText(quote.getAuthor());

//        定义itemView点击监听器等触发事件  更新quote
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("quote",quote);
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_quoteFragment_to_updateQuoteFragment, bundle);

            }
        });

    }

    @Override
    public int getItemCount() {
        return allQutoes.size();
    }
}
