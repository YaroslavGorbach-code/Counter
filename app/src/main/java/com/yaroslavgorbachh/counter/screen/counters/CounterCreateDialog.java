package com.yaroslavgorbachh.counter.screen.counters;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.yaroslavgorbachh.counter.databinding.DialogCreateCounterBinding;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.util.ViewUtil;

import java.util.Objects;


public class CounterCreateDialog extends DialogFragment {
    interface Host{
        void onCreateCounter(String title, String group);
        void onDetailed();
    }
    private CounterCreateDialog(){}

    public static CounterCreateDialog newInstance(String group) {
        CounterCreateDialog f = new CounterCreateDialog();
        Bundle args = new Bundle();
        args.putString("group", group);
        f.setArguments(args);
        return f;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       DialogCreateCounterBinding binding = DialogCreateCounterBinding.bind(LayoutInflater.from(requireContext())
               .inflate(R.layout.dialog_create_counter, null));

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .setNegativeButton(R.string.addCounterDialogCounterNegativeButton, null)
                .setPositiveButton(R.string.addCounterDialogCounterPositiveButton, (dialog1, which) -> {
                    String title;
                   if (ViewUtil.titleFilter(binding.title)){
                        title = binding.title.getText().toString();
                   }else {
                       return;
                   }
                    ((Host)requireParentFragment()).onCreateCounter(title, getArguments().getString("group"));
                }).create();
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        binding.detailed.setOnClickListener(v -> {
                dismiss();
                ((Host)requireParentFragment()).onDetailed();
            });
        return dialog;
    }

}
