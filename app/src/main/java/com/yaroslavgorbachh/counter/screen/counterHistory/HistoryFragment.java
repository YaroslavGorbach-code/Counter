package com.yaroslavgorbachh.counter.screen.counterHistory;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.component.history.History;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentCounterHistoryBinding;
import com.yaroslavgorbachh.counter.feature.Animations;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Models.CounterHistory;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class HistoryFragment extends Fragment {

    public HistoryFragment(){
        super(R.layout.fragment_counter_history);
    }

    @Inject Repo repo;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication application = (MyApplication) requireActivity().getApplication();
        application.appComponent.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init history
        long id = HistoryFragmentArgs.fromBundle(requireArguments()).getCounterId();
        HistoryViewModel vm = new ViewModelProvider(this).get(HistoryViewModel.class);
        History history = vm.getHistoryComponent(repo, id);

        // init v
        HistoryView v = new HistoryView(FragmentCounterHistoryBinding.bind(view), new HistoryView.Callback() {
            @Override
            public void onBack() {
                Navigation.findNavController(view).popBackStack();
            }

            @Override
            public void onClearHistory() {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.clear_history_title))
                        .setMessage(getString(R.string.clear_history_message))
                        .setPositiveButton(getString(R.string.clear_history_pos_button), (dialog, which) -> history.clean())
                        .setNegativeButton(getString(R.string.clear_history_neg_button), null)
                        .create()
                        .show();
            }
        });
        history.getHistory().observe(getViewLifecycleOwner(), v::setHistory);
    }
}
