package com.yaroslavgorbachh.counter.screen.counter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.component.counter.CounterCom;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentCounterBinding;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.feature.ad.AdManager;
import com.yaroslavgorbachh.counter.feature.ad.AdManagerImp;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CounterFragment extends Fragment {
    private CounterView mV;
    @Inject CounterCom counterCom;
    @Inject AdManager adManager;

    public CounterFragment() {
        super(R.layout.fragment_counter);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // inject component
        long id = CounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        CounterViewModel vm = new ViewModelProvider(this).get(CounterViewModel.class);
        vm.getCounterComponent(id).inject(this);

        // load ad
        adManager.loadInterstitialAd(view.getContext());

        // init view
        mV = new CounterView(FragmentCounterBinding.bind(requireView()), requireActivity(), counterCom.getFastCountInterval(), new CounterView.Callback() {
            @Override
            public void onDelete() {
                counterCom.delete();
                Navigation.findNavController(view).popBackStack();
            }

            @Override
            public void onEdit() {
                NavDirections action = CounterFragmentDirections
                        .actionCounterFragmentToCreateEditCounterFragment()
                        .setCounterId(id);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onHistory() {
                NavDirections action = CounterFragmentDirections
                        .actionCounterFragmentToCounterHistoryFragment()
                        .setCounterId(id);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onAbout() {
                NavDirections action = CounterFragmentDirections
                        .actionCounterFragmentToAboutCounterFragment()
                        .setCounterId(id);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onFullScreen() {
                NavDirections action = CounterFragmentDirections.
                        actionCounterFragmentToFullscreenCounterFragment()
                        .setCounterId(id);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onBack() {
                Navigation.findNavController(view).popBackStack();
            }

            @Override
            public void onInc() {
                counterCom.incCounter(new Repo.ValueCallback() {
                    @Override
                    public void onMax() {
                        Toast.makeText(requireContext(), R.string.thisIsMaximum, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onMin() {
                        Toast.makeText(requireContext(), R.string.thisIsMinimum, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onDec() {
                counterCom.decCounter(new Repo.ValueCallback() {
                    @Override
                    public void onMax() {
                        Toast.makeText(requireContext(), R.string.thisIsMaximum, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onMin() {
                        Toast.makeText(requireContext(), R.string.thisIsMinimum, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onReset() {
                counterCom.resetCounter(copy -> {
                    Snackbar.make(requireView(), getResources().getString(R.string.counterReset), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.counterResetUndo), v1 -> counterCom.insert(copy)).show();
                });
            }
        });

        counterCom.getCounter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mV::setCounter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mV.unregisterReceiver(requireContext());
        adManager.showInterstitialAd(requireActivity());
    }
}
