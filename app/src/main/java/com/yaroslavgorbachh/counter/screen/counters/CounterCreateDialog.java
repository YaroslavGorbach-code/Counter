package com.yaroslavgorbachh.counter.screen.counters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.yaroslavgorbachh.counter.databinding.DialogCreateCounterBinding;
import com.yaroslavgorbachh.counter.feature.InputFilters;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.utill.Utility;

import java.util.ArrayList;
import java.util.Objects;


public class CounterCreateDialog extends DialogFragment {
    interface Host{
        void onCreateCounter(String title, String group);
        void onDetailed();
    }

    public static CounterCreateDialog newInstance(String group, ArrayList<String> groups) {
        CounterCreateDialog f = new CounterCreateDialog();
        Bundle args = new Bundle();
        args.putString("group", group);
        args.putStringArrayList("groups", groups);
        f.setArguments(args);
        return f;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       DialogCreateCounterBinding binding = DialogCreateCounterBinding.bind(LayoutInflater.from(requireContext())
               .inflate(R.layout.dialog_create_counter, null));
       if (getArguments()!=null && getArguments().getString("group")!=null)
           binding.groups.setText(getArguments().getString("group"));

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .setNegativeButton(R.string.addCounterDialogCounterNegativeButton, null)
                .setPositiveButton(R.string.addCounterDialogCounterPositiveButton, (dialog1, which) -> {
                    String group = InputFilters.groupsFilter(binding.groups);
                    String title;
                   if (InputFilters.titleFilter(binding.title)){
                        title = binding.title.getText().toString();
                   }else {
                       return;
                   }
                    ((Host)requireParentFragment()).onCreateCounter(title, group);
                }).create();
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        binding.getRoot().getContext(),
                        R.layout.item_popup,
                        Utility.deleteTheSameGroups(getArguments().getStringArrayList("groups")));
        binding.groups.setAdapter(adapter);
        binding.detailed.setOnClickListener(v -> {
                dismiss();
                ((Host)requireParentFragment()).onDetailed();
            });
        return dialog;
    }

}