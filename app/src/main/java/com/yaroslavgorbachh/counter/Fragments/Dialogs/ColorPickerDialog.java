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
import com.yaroslavgorbachh.counter.ColorPicker;
import com.yaroslavgorbachh.counter.Fragments.CountersFragmentDirections;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.ViewModels.CreateCounterDialogViewModel;

public class ColorPickerDialog extends AppCompatDialogFragment {

    public static ColorPickerDialog newInstance() {
        return new ColorPickerDialog();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_color_picker, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setNegativeButton(R.string.addCounterDialogCounterNegativeButton, null);
        ColorPicker colorPicker; // TODO: 2/18/2021 init

         return builder.create();
    }

}
