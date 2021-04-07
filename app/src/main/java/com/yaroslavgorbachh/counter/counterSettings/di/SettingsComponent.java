package com.yaroslavgorbachh.counter.counterSettings.di;

import com.yaroslavgorbachh.counter.counterSettings.themes.ColorPickerDialog;
import com.yaroslavgorbachh.counter.counterSettings.SettingsActivity;
import com.yaroslavgorbachh.counter.counterSettings.SettingsFragment;

import dagger.Subcomponent;

@SettingsScope
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
