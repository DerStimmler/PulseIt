package com.cool.pulseit.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cool.pulseit.R;
import com.cool.pulseit.database.DatabaseManager;
import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.utils.Result;
import com.cool.pulseit.utils.StatusSnackbar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoryFragment extends Fragment {

    private View _view;
    private RecyclerView _recyclerView;
    private List<Pulse> _pulses;
    private PulsesAdapter _adapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.pulse_search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                _adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_history, container, false);
        DatabaseManager dbm = new DatabaseManager(_view.getContext());

        Result<List<Pulse>> result = dbm.getPulses();

        if (!result.isOk()) {
            StatusSnackbar.show(getActivity(), result.getMessage());
        } else {

            _pulses = result.getValue();

            Collections.sort(_pulses, new Comparator<Pulse>() {
                @Override
                public int compare(Pulse o1, Pulse o2) {
                    return o2.date.compareTo(o1.date);
                }
            });

            _adapter = new PulsesAdapter(_pulses);

            _recyclerView = _view.findViewById(R.id.history_recyclerview);
            _recyclerView.setAdapter(_adapter);
            _recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

            PulsesSwipeToDeleteCallback pulsesSwipeToDeleteCallback = new PulsesSwipeToDeleteCallback(_adapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(pulsesSwipeToDeleteCallback);
            itemTouchHelper.attachToRecyclerView(_recyclerView);
        }

        return _view;
    }
}
