package com.giriseematechme.bankingforms.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.giriseematechme.bankingforms.R;
import com.giriseematechme.bankingforms.RoomDatabase.Form;
import com.giriseematechme.bankingforms.RoomDatabase.LoginTable;

import java.util.ArrayList;
import java.util.List;

public class UserFormsAdapter extends RecyclerView.Adapter<UserFormsAdapter.UserViewHolder> {
    private List<Form> formsList= new ArrayList<>();
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_forms_item, parent, false);
        return new UserViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.textViewName.setText(formsList.get(position).getName());
        holder.textViewEmail.setText(formsList.get(position).getEmail());
        holder.textViewAddress.setText(formsList.get(position).getAddress());
    }
    @Override
    public int getItemCount() {
        Log.v(UserFormsAdapter.class.getSimpleName(),""+formsList.size());
        return formsList.size();
    }
    public void setForms(List<Form> formsList) {
        this.formsList = formsList;
        notifyDataSetChanged();
    }
    public Form getFormAt(int position) {
        return formsList.get(position);
    }

    /**
     * ViewHolder class
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView textViewName;
        public AppCompatTextView textViewEmail;
        public AppCompatTextView textViewAddress;

        public UserViewHolder(View view) {
            super(view);
            textViewName = (AppCompatTextView) view.findViewById(R.id.textViewName);
            textViewEmail = (AppCompatTextView) view.findViewById(R.id.textViewEmail);
            textViewAddress = (AppCompatTextView) view.findViewById(R.id.textViewAddress);
        }
    }
}
