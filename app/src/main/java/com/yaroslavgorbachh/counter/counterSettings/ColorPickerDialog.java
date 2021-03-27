package com.yaroslavgorbachh.counter.counterSettings;

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

import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.di.ViewModelProviderFactory;

import javax.inject.Inject;


public class ColorPickerDialog extends AppCompatDialogFragment {
    public static final String THEME_CHANGED_BROADCAST = "THEME_CHANGED_BROADCAST";
    private SettingsViewModel mViewModel;

    @Inject ViewModelProviderFactory viewModelProviderFactory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication application = (MyApplication) requireActivity().getApplication();
        application.appComponent.settingsComponentFactory().create().inject(this);
    }

    public static ColorPickerDialog newInstance() {
        return new ColorPickerDialog();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_color_picker, null);
       mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(SettingsViewModel.class);
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
