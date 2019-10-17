package comv.example.zyrmj.precious_time01.viewModels;

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
    LiveData<List<Template>> getAllTemplates() {
        return templateRepository.getAllTemplates();
    }

    void insertTemplates(Template... templates) {
        templateRepository.insertTemplates(templates);
    }

    void deleteTemplates(Template... templates) {
        templateRepository.deleteTemplates(templates);
    }

    void updateTemplates(Template... templates) {
        templateRepository.updateTemplates(templates);
    }
}
