package com.yaroslavgorbach.counter.Fragments.Dialogs;

import android.app.Dialog;
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
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.yaroslavgorbach.counter.Fragments.CountersFragmentDirections;
import com.yaroslavgorbach.counter.R;
import com.yaroslavgorbach.counter.Utility;
import com.yaroslavgorbach.counter.ViewModels.CreateCounterDialogViewModel;

public class CreateCounterDialog extends AppCompatDialogFragment {
    private AutoCompleteTextView mGroups_et;
    private CreateCounterDialogViewModel mViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

       View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_counter, null);
       mViewModel = new ViewModelProvider(this).get(CreateCounterDialogViewModel.class);
        /*initialize fields*/
        mGroups_et = view.findViewById(R.id.filled_exposed_dropdown_createCounter_dialog);

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

        /*each new group sets into dropdown_menu*/
        setGroups(view);

        /*start CreateCounterDetailed_AND_EditCounterActivity*/
            view.findViewById(R.id.LaunchDetailed).setOnClickListener(v -> {
                dismiss();
                NavHostFragment navHostFragment = (NavHostFragment) getParentFragmentManager().findFragmentById(R.id.hostFragment);
                NavController navController = navHostFragment.getNavController();
                NavDirections action = CountersFragmentDirections.actionCountersFragmentToCreateEditCounterFragment2();
                navController.navigate(action);
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
