package comv.example.zyrmj.precious_time01.ViewModel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import comv.example.zyrmj.precious_time01.entity.Quote;
import comv.example.zyrmj.precious_time01.repository.QuoteRepository;

public class QuoteViewModel extends AndroidViewModel {
    private QuoteRepository quoteRepository;

    public QuoteViewModel(@NonNull Application application) {
        super(application);
        this.quoteRepository = new QuoteRepository(application);
    }

    public LiveData<List<Quote>> getAllQuotes(String userId) {
        return quoteRepository.getAllQuotes(userId);
    }
}
