package com.giriseematechme.bankingforms.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.giriseematechme.bankingforms.RoomDatabase.Form;
import com.giriseematechme.bankingforms.RoomDatabase.FormDao;
import com.giriseematechme.bankingforms.RoomDatabase.LoginDatabase;

import java.util.List;

public class FormRepository {
        private FormDao formDao;
        private LiveData<List<Form>> allForms;
        public FormRepository(Application application) {
            LoginDatabase database = LoginDatabase.getDatabase(application);
            formDao = database.formDao();
            allForms = formDao.getAllForms();
        }
        public void insert(Form from) {
            new InsertNoteAsyncTask(formDao).execute(from);
        }

        public void delete(Form form) {
            new DeleteNoteAsyncTask(formDao).execute(form);
        }
        public void deleteAllForms() {
            new DeleteAllNotesAsyncTask(formDao).execute();
        }
        public LiveData<List<Form>> getAllForms() {
            return allForms;
        }
        private static class InsertNoteAsyncTask extends AsyncTask<Form, Void, Void> {
            private FormDao formDao;
            private InsertNoteAsyncTask(FormDao formDao) {
                this.formDao = formDao;
            }
            @Override
            protected Void doInBackground(Form... from) {
                formDao.insert(from[0]);
                return null;
            }
        }
        private static class DeleteNoteAsyncTask extends AsyncTask<Form, Void, Void> {
            private FormDao formDao;
            private DeleteNoteAsyncTask(FormDao formDao) {
                this.formDao = formDao;
            }
            @Override
            protected Void doInBackground(Form... from) {
                formDao.delete(from[0]);
                return null;
            }
        }
        private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
            private FormDao formDao;
            private DeleteAllNotesAsyncTask(FormDao formDao) {
                this.formDao = formDao;
            }
            @Override
            protected Void doInBackground(Void... voids) {
                formDao.deleteAllForms();
                return null;
            }
        }
}
