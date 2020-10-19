package com.giriseematechme.bankingforms.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.giriseematechme.bankingforms.Repository.FormRepository;
import com.giriseematechme.bankingforms.RoomDatabase.Form;

import java.util.List;

public class FormViewModel extends AndroidViewModel {
    private FormRepository repository;
    private LiveData<List<Form>> allForms;
    public FormViewModel(@NonNull Application application) {
        super(application);
        repository = new FormRepository(application);
        allForms = repository.getAllForms();
    }
    public void insert(Form form) {
        repository.insert(form);
    }

    public void delete(Form form) {
        repository.delete(form);
    }
    public void deleteAllForms() {
        repository.deleteAllForms();
    }
    public LiveData<List<Form>> getForms() {
        return allForms;
    }
}
