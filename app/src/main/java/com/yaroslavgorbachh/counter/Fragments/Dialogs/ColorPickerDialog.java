package com.yaroslavgorbachh.counter.Fragments.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.yaroslavgorbachh.counter.ColorPicker.ColorPicker;
import com.yaroslavgorbachh.counter.Database.Models.AppStyle;
import com.yaroslavgorbachh.counter.Database.Repo;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.ColorPicker.ScrollColorPicker;
import com.yaroslavgorbachh.counter.ViewModels.ColorPickerDialogViewModel;


public class ColorPickerDialog extends AppCompatDialogFragment {
    public static final String THEME_CHANGED_BROADCAST = "THEME_CHANGED_BROADCAST";
    private ColorPickerDialogViewModel mViewModel;

    public static ColorPickerDialog newInstance() {
        return new ColorPickerDialog();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_color_picker, null);
       mViewModel = new ViewModelProvider(this).get(ColorPickerDialogViewModel.class);
       ColorPicker colorPicker = new ScrollColorPicker(view, getResources());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setPositiveButton(getString(R.string.apply), (dialog, which) -> {
                    mViewModel.changeThemeColor(colorPicker.getColor(requireContext()), requireContext().getResources());

                    // notify MainActivity that theme changed
                    Intent intent = new Intent(THEME_CHANGED_BROADCAST);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    getActivity().recreate();
                });

         return builder.create();
    }

}
