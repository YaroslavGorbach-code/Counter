package com.yaroslavgorbach.counter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;

public class AddCounterDialog extends AppCompatDialogFragment {

    public interface AddCounterListener{

        void onAddClick(String title);
        void onLaunchDetailedClick();
    }

    private AddCounterListener mListener;

    // Override the Fragment.onAttach() method to instantiate the Listener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            mListener = (AddCounterListener) context;

        } catch (ClassCastException e) {

            throw new ClassCastException("must implement AddCounterListenerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        @SuppressLint("InflateParams") View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_add_counter, null);



        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setPositiveButton("Add", (dialog, which) -> {

                    TextInputEditText text = view.findViewById(R.id.counterTitle_addCounter);
                    String title = text.getText().toString();
                    mListener.onAddClick(title);

                })

                .setNegativeButton("Cancel", (dialog, which) -> {

                });

            view.findViewById(R.id.LaunchDetailed).setOnClickListener(v -> {

                mListener.onLaunchDetailedClick();

            });

         return builder.create();





    }
}
