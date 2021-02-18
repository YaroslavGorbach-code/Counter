package com.yaroslavgorbachh.counter.Fragments.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.yaroslavgorbachh.counter.ColorPicker;
import com.yaroslavgorbachh.counter.Fragments.CountersFragmentDirections;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.ScrollColorPicker;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.ViewModels.CreateCounterDialogViewModel;

import static com.yaroslavgorbachh.counter.ScrollColorPicker.THEME_VALUE_KEY;

public class ColorPickerDialog extends AppCompatDialogFragment {
    public static final String THEME_CHANGED_BROADCAST = "THEME_CHANGED_BROADCAST";
    private SharedPreferences mPref;

    public static ColorPickerDialog newInstance() {
        return new ColorPickerDialog();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_color_picker, null);
        ColorPicker colorPicker = new ScrollColorPicker(view);
        mPref = getActivity().getSharedPreferences(THEME_VALUE_KEY, Context.MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setPositiveButton("Apply", (dialog, which) -> {
                    mPref.edit().putInt(THEME_VALUE_KEY, colorPicker.getColor()).apply();
                    Intent intent = new Intent(THEME_CHANGED_BROADCAST);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    getActivity().recreate();
                });

         return builder.create();
    }

}
