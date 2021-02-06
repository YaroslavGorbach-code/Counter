package com.yaroslavgorbach.counter.Fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.yaroslavgorbach.counter.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}