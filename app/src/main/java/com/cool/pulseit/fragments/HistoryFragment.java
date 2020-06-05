package com.cool.pulseit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cool.pulseit.PulsesAdapter;
import com.cool.pulseit.PulsesSwipeToDeleteCallback;
import com.cool.pulseit.R;
import com.cool.pulseit.database.DatabaseManager;
import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.utils.Result;
import com.cool.pulseit.utils.StatusSnackbar;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    private View _mainActivity;
    private RecyclerView _recyclerView;
    private List<Pulse> _pulses;
    private PulsesAdapter _adapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HistoryFragment.
     */
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
        _mainActivity = inflater.inflate(R.layout.fragment_history, container, false);

        DatabaseManager dbm = new DatabaseManager(_mainActivity.getContext());

        Result<List<Pulse>> result = dbm.getPulses();

        if (!result.isOk()) {
            StatusSnackbar.show(getActivity(), result.getMessage());
        } else {
            _pulses = result.getValue();

            _adapter = new PulsesAdapter(_pulses);

            _recyclerView = _mainActivity.findViewById(R.id.history_recyclerview);
            _recyclerView.setAdapter(_adapter);
            _recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

            PulsesSwipeToDeleteCallback pulsesSwipeToDeleteCallback = new PulsesSwipeToDeleteCallback(_adapter, getContext());
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(pulsesSwipeToDeleteCallback);
            itemTouchHelper.attachToRecyclerView(_recyclerView);
        }

        return _mainActivity;
    }
}
