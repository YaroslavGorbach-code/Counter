package com.yaroslavgorbachh.counter.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.yaroslavgorbachh.counter.Database.ViewModels.CounterViewModel;
import com.yaroslavgorbachh.counter.R;

import java.util.HashSet;
import java.util.Set;

public class CreateCounterDialog extends AppCompatDialogFragment {

    public interface AddCounterListener{
        void onAddClick(String title, String group);
        void onLaunchDetailedClick();
    }

    private AddCounterListener mListener;
    private CounterViewModel mViewModel;
    private AutoCompleteTextView mGroups_et;

    // Override the Fragment.onAttach() method to instantiate the Listener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (AddCounterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("must implement AddCounterListenerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        @SuppressLint("InflateParams") View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_create_counter, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setPositiveButton(R.string.AddCounterDialogCounterPositiveButton, (dialog, which) -> {

                    TextInputEditText text_et = view.findViewById(R.id.counterTitle_addCounter);

                    /*creating new counter and check title is not empty*/
                    String title = "";
                    String group;

                    if(text_et.getText().toString().trim().isEmpty()){
                        text_et.setError("This field cannot be empty");
                    }else{
                        title = text_et.getText().toString();
                    }

                    /*if group is empty set null*/
                    if(mGroups_et.getText().toString().trim().isEmpty()){
                        group = null;
                    }else{
                        group = mGroups_et.getText().toString();
                    }

                    /*passing variables to create a counter*/
                    if (!(title.trim().isEmpty())){
                        mListener.onAddClick(title, group);
                    }

                })
                .setNegativeButton(R.string.AddCounterDialogCounterNegativeButton, (dialog, which) -> {
                });


        /*initialize fields*/
        mGroups_et = view.findViewById(R.id.filled_exposed_dropdown_createCounter_dialog);
            mViewModel = new ViewModelProvider(this).get(CounterViewModel.class);

        /*each new group sets into dropdown_menu*/
        setGroups(view);

        /*start CreateCounterDetailed_AND_EditCounterActivity*/
            view.findViewById(R.id.LaunchDetailed).setOnClickListener(v -> {
                mListener.onLaunchDetailedClick();
                dismiss();
            });
         return builder.create();
    }

    private void setGroups(View view) {
        mViewModel.getGroups().observe(this, strings -> {
            /*delete the same groups*/
            Set<String> set = new HashSet<>(strings);
            String[] result = set.toArray(new String[0]);

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            view.getContext(),
                            R.layout.dropdown_menu_popup_item,
                            result);
            mGroups_et.setAdapter(adapter);

        });
    }
}
