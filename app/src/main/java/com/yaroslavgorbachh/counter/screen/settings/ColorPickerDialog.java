package com.yaroslavgorbachh.counter.screen.settings;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.yaroslavgorbachh.counter.R;


public class ColorPickerDialog extends AppCompatDialogFragment {
    public interface Host{
        void onThemeChange(int color);
    }

    public static ColorPickerDialog newInstance() {
        return new ColorPickerDialog();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_color_picker, null);
       ColorPicker colorPicker = new ScrollColorPicker(view, getResources());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setPositiveButton(getString(R.string.apply), (dialog, which) ->
                        ((Host)requireParentFragment()).onThemeChange(colorPicker.getColor(requireContext())));

         return builder.create();
    }

}
