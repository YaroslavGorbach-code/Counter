package com.yaroslavgorbachh.counter.Fragments.Dialogs;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.yaroslavgorbachh.counter.Fragments.CountersFragmentDirections;
import com.yaroslavgorbachh.counter.InputFilters;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.ViewModels.CreateCounterDialogViewModel;

public class CreateCounterDialog extends AppCompatDialogFragment {
    private AutoCompleteTextView mGroups_et;
    private CreateCounterDialogViewModel mViewModel;
    private TextInputEditText mCounterName_et;

    public static CreateCounterDialog newInstance(String group) {
        CreateCounterDialog f = new CreateCounterDialog();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("group", group);
        f.setArguments(args);
        return f;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

       View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_counter, null);
       mViewModel = new ViewModelProvider(this).get(CreateCounterDialogViewModel.class);
       mGroups_et = view.findViewById(R.id.filled_exposed_dropdown_createCounter_dialog);
       mCounterName_et = view.findViewById(R.id.counterTitle_addCounter);
       if (getArguments()!=null)
       mGroups_et.setText(getArguments().getString("group"));

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setNegativeButton(R.string.addCounterDialogCounterNegativeButton, null)
                .setPositiveButton(R.string.addCounterDialogCounterPositiveButton, (dialog, which) -> {
                    String group = InputFilters.groupsFilter(mGroups_et);
                    String title = "";

                   if (InputFilters.titleFilter(mCounterName_et)){
                        title = mCounterName_et.getText().toString();
                   }else {
                       return;
                   }

                        mViewModel.createCounter(title, group);
                });

        /*each new group sets into dropdown_menu*/
        setGroups(view.getContext());

        /*start CreateCounterDetailed_AND_EditCounterActivity*/
            view.findViewById(R.id.LaunchDetailed).setOnClickListener(v -> {
                dismiss();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.hostFragment);
                navController.navigate(CountersFragmentDirections.actionCountersFragmentToCreateEditCounterFragment2());
                Utility.hideKeyboard(requireActivity());
            });
         return builder.create();
    }

    private void setGroups(Context context) {
        mViewModel.getGroups().observe(this, groups -> {
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            context,
                            R.layout.dropdown_menu_popup_item,
                            Utility.deleteTheSameGroups(groups));
            mGroups_et.setAdapter(adapter);
        });
    }
}
