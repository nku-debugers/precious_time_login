package comv.example.zyrmj.precious_time01.fragments.personCenter;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import comv.example.zyrmj.precious_time01.R;
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.repository.QuoteRepository;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateQuoteFragment extends Fragment {
    private Quote quote;
    private EditText words, author;
    private Button save, clear;
    private ImageView back;
    private String userId = "offline";
    private QuoteRepository quoteRepository;

    public UpdateQuoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_quote_item, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            quote = (Quote) getArguments().getSerializable("quote");
        }
        quoteRepository = new QuoteRepository(getContext());
        words = getView().findViewById(R.id.words);
        words.setText(quote.getWords());
        author = getView().findViewById(R.id.author);
        author.setText(quote.getAuthor());
        save = getView().findViewById(R.id.quote_save );
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (words.getText().toString().equals("") || author.getText().toString().equals("")) {
                    PromptDialog promptDialog = new PromptDialog(getActivity());
                    promptDialog.showWarn("输入框中内容不能为空！");
                } else {
                    Quote newQuote = new Quote(userId, words.getText().toString(), author.getText().toString());
                    if (quoteRepository.getSpecificQuote(newQuote.getUserId(), newQuote.getWords()) != null) {
                        PromptDialog promptDialog = new PromptDialog(getActivity());
                        promptDialog.showWarn("所添加箴言已存在！");
                    } else {
                        quoteRepository.UpdateQuote(quote.getUserId(), quote.getWords(), newQuote.getWords(), newQuote.getAuthor());
                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_updateQuoteFragment_to_quoteFragment);
                    }
                }
            }
        });
        clear = getView().findViewById(R.id.quote_clear );
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                words.setText("");
                author.setText("");
            }
        });
        back = getView().findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromptDialog promptDialog = new PromptDialog(getActivity());
                PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        NavController controller = Navigation.findNavController(getView());
                        controller.navigate(R.id.action_updateQuoteFragment_to_quoteFragment);
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
                promptDialog.showWarnAlert("您的数据将不会被保存，是否退出？", cancel, confirm);


            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
                    PromptDialog promptDialog = new PromptDialog(getActivity());
                    PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
                        @Override
                        public void onClick(PromptButton button) {
                            NavController controller = Navigation.findNavController(getView());
                            controller.navigate(R.id.action_updateQuoteFragment_to_quoteFragment);
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
                    promptDialog.showWarnAlert("您的数据将不会被保存，是否退出？", cancel, confirm);
                    return true;
                }
                return false;
            }
        });


    }
}
