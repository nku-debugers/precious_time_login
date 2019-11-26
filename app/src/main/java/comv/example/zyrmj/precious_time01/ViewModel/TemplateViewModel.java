package comv.example.zyrmj.precious_time01.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import comv.example.zyrmj.precious_time01.entity.Template;
import comv.example.zyrmj.precious_time01.repository.TemplateRepository;

public class TemplateViewModel extends AndroidViewModel {
    private TemplateRepository templateRepository;

    public TemplateViewModel(@NonNull Application application) {
        super(application);
        templateRepository = new TemplateRepository(application);
    }

    public LiveData<List<Template>> getAllTemplates(String userId) {
        return templateRepository.getAllTemplates(userId);
    }

    public void insertTemplates(Template... templates) {
        templateRepository.insertTemplates(templates);
    }

    public void deleteTemplates(Template... templates) {
        templateRepository.deleteTemplates(templates);
    }

    public void updateTemplates(Template... templates) {
        templateRepository.updateTemplates(templates);
    }
}
