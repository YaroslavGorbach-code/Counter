package com.yaroslavgorbachh.counter.Fragments.Dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.yaroslavgorbachh.counter.ColorPicker;
import com.yaroslavgorbachh.counter.Database.Models.AppStyle;
import com.yaroslavgorbachh.counter.Database.Repo;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.ScrollColorPicker;


public class ColorPickerDialog extends AppCompatDialogFragment {
    public static final String THEME_CHANGED_BROADCAST = "THEME_CHANGED_BROADCAST";
    private Repo mRepo;

    public static ColorPickerDialog newInstance() {
        return new ColorPickerDialog();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_color_picker, null);
        mRepo = new Repo(getActivity().getApplication());
        ColorPicker colorPicker = new ScrollColorPicker(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())

                .setView(view)
                // TODO: 2/18/2021 translate
                .setPositiveButton("Apply", (dialog, which) -> {
                    if (colorPicker.getColor(getContext())==getContext().getResources().getColor(R.color.colorAccent)){
                        mRepo.changeTheme(new AppStyle(1, R.style.AppTheme));
                    }
                    if (colorPicker.getColor(getContext())==getContext().getResources().getColor(R.color.colorAccent_orange)){
                        mRepo.changeTheme(new AppStyle(1, R.style.AppTheme_orange));
                    }
                    if (colorPicker.getColor(getContext())==getContext().getResources().getColor(R.color.colorAccent_blue)){
                        mRepo.changeTheme(new AppStyle(1, R.style.AppTheme_blue));
                    }
                    if (colorPicker.getColor(getContext())==getContext().getResources().getColor(R.color.colorAccent_yellow)){
                        mRepo.changeTheme(new AppStyle(1, R.style.AppTheme_yellow));
                    }

                    Intent intent = new Intent(THEME_CHANGED_BROADCAST);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    getActivity().recreate();
                });

         return builder.create();
    }

}
