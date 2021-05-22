package com.yaroslavgorbachh.counter.screen.history;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.yaroslavgorbachh.counter.component.history.HistoryComponent;
import com.yaroslavgorbachh.counter.data.Domain.History;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentCounterHistoryBinding;
import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.R;

import javax.inject.Inject;

public class HistoryFragment extends Fragment {

    public HistoryFragment(){
        super(R.layout.fragment_counter_history);
    }

    @Inject Repo repo;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        App application = (App) requireActivity().getApplication();
        application.appComponent.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init history
        long id = HistoryFragmentArgs.fromBundle(requireArguments()).getCounterId();
        HistoryViewModel vm = new ViewModelProvider(this).get(HistoryViewModel.class);
        HistoryComponent historyComponent = vm.getHistoryComponent(repo, id);

        // init v
        HistoryView v = new HistoryView(FragmentCounterHistoryBinding.bind(view), new HistoryView.Callback() {
            @Override
            public void onBack() {
                Navigation.findNavController(view).popBackStack();
            }

            @Override
            public void onClear() {
                historyComponent.clean();
            }

            @Override
            public void onRemove(History history) {
                historyComponent.remove(history);
            }

            @Override
            public void onUndo(History item) {
                historyComponent.addItem(item);
            }
        });
        historyComponent.getHistory().observe(getViewLifecycleOwner(), v::setHistory);
    }
}
