package comv.example.zyrmj.precious_time01.ViewModel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.entity.TemplateItem;
import comv.example.zyrmj.precious_time01.repository.TemplateItemRepository;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;

public class TemplateItemViewModel extends AndroidViewModel {
    private TemplateItemRepository templateItemRepository;
    public TemplateItemViewModel(@NonNull Application application) {
        super(application);
        templateItemRepository = new TemplateItemRepository(application);
    }
    public LiveData<List<TemplateItem>> getSpecificTemplateItems(String templateName, String userId) {
        return templateItemRepository.getSpecificList2(templateName,userId);
    }


}
