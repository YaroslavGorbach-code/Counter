package com.yaroslavgorbachh.counter.Fragments.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.yaroslavgorbachh.counter.Activityes.CreateEditCounterActivity;
import com.yaroslavgorbachh.counter.Activityes.MainActivity;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.ViewModels.CreateCounterDialogViewModel;

import java.util.HashSet;
import java.util.Set;

public class CreateCounterDialog extends AppCompatDialogFragment {
    private AutoCompleteTextView mGroups_et;
    private CreateCounterDialogViewModel mViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

       View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_counter, null);
       mViewModel = new ViewModelProvider(this).get(CreateCounterDialogViewModel.class);

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
                        mViewModel.createCounter(title, group);
                    }

                })
                .setNegativeButton(R.string.AddCounterDialogCounterNegativeButton, (dialog, which) -> {
                });


        /*initialize fields*/
        mGroups_et = view.findViewById(R.id.filled_exposed_dropdown_createCounter_dialog);
          //  mViewModel = new ViewModelProvider(this).get(CounterViewModel.class);

        /*each new group sets into dropdown_menu*/
        setGroups(view);

        /*start CreateCounterDetailed_AND_EditCounterActivity*/
            view.findViewById(R.id.LaunchDetailed).setOnClickListener(v -> {
                startActivity(new Intent(getContext(), CreateEditCounterActivity.class ));
                dismiss();
            });
         return builder.create();
    }

    private void setGroups(View view) {
        mViewModel.getGroups().observe(this, groups -> {
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            view.getContext(),
                            R.layout.dropdown_menu_popup_item,
                            Utility.deleteTheSameGroups(groups));
            mGroups_et.setAdapter(adapter);
        });
    }
}
