package com.yaroslavgorbachh.counter.counterSettings.di;

import com.yaroslavgorbachh.counter.counterSettings.ColorPickerDialog;
import com.yaroslavgorbachh.counter.counterSettings.SettingsActivity;
import com.yaroslavgorbachh.counter.counterSettings.SettingsFragment;
import com.yaroslavgorbachh.counter.createEditCounter.CreateCounterDialog;
import com.yaroslavgorbachh.counter.createEditCounter.CreateEditCounterFragment;

import dagger.Subcomponent;

@Subcomponent(modules = SettingsModule.class)
public interface SettingsComponent {


    @Subcomponent.Factory
    interface Builder {
        SettingsComponent create();
    }

    void inject(SettingsFragment fragment);
    void inject(ColorPickerDialog dialog);
    void inject(SettingsActivity activity);

}
