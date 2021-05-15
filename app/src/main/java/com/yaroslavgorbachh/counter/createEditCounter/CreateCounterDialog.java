package com.yaroslavgorbachh.counter.createEditCounter;

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
import com.yaroslavgorbachh.counter.InputFilters;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.countersList.CountersFragmentDirections;

import javax.inject.Inject;

public class CreateCounterDialog extends AppCompatDialogFragment {
    private AutoCompleteTextView mGroups_et;
    private TextInputEditText mCounterName_et;
    private CreateCounterDialogViewModel mViewModel;

    public static CreateCounterDialog newInstance(String group) {
        CreateCounterDialog f = new CreateCounterDialog();
        Bundle args = new Bundle();
        args.putString("group", group);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication app = (MyApplication) requireActivity().getApplication();
        app.appComponent.inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_counter, null);
       mGroups_et = view.findViewById(R.id.filled_exposed_dropdown_createCounter_dialog);
       mCounterName_et = view.findViewById(R.id.title);

       mViewModel = new ViewModelProvider(this).get(CreateCounterDialogViewModel.class);

       if (getArguments()!=null)
           mGroups_et.setText(getArguments().getString("group"));

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setNegativeButton(R.string.addCounterDialogCounterNegativeButton, null)
                .setPositiveButton(R.string.addCounterDialogCounterPositiveButton, (dialog, which) -> {
                    String group = InputFilters.groupsFilter(mGroups_et);
                    String title;

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
            view.findViewById(R.id.detailed).setOnClickListener(v -> {
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
                            R.layout.item_popup,
                            Utility.deleteTheSameGroups(groups));
            mGroups_et.setAdapter(adapter);
        });
    }

}
