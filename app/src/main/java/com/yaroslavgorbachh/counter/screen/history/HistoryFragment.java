package com.yaroslavgorbachh.counter.screen.history;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.yaroslavgorbachh.counter.component.history.HistoryCom;
import com.yaroslavgorbachh.counter.databinding.FragmentCounterHistoryBinding;
import com.yaroslavgorbachh.counter.R;

import javax.inject.Inject;

public class HistoryFragment extends Fragment {
    @Inject HistoryCom historyCom;
    public HistoryFragment(){
        super(R.layout.fragment_counter_history);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // inject historyCom
        long id = HistoryFragmentArgs.fromBundle(requireArguments()).getCounterId();
        HistoryViewModel vm = new ViewModelProvider(this).get(HistoryViewModel.class);
        vm.getHistoryComponent(id).inject(this);

        // init v
        HistoryView v = new HistoryView(FragmentCounterHistoryBinding.bind(view), new HistoryView.Callback() {
            @Override
            public void onBack() {
                Navigation.findNavController(view).popBackStack();
            }

            @Override
            public void onClear() {
                historyCom.clean();
            }

            @Override
            public void onRemove(com.yaroslavgorbachh.counter.data.Domain.History history) {
                historyCom.remove(history);
            }

            @Override
            public void onUndo(com.yaroslavgorbachh.counter.data.Domain.History item) {
                historyCom.addItem(item);
            }
        });
        historyCom.getHistory().observe(getViewLifecycleOwner(), v::setHistory);
    }
}
