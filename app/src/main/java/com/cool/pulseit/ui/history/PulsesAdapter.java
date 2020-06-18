package com.cool.pulseit.ui.history;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cool.pulseit.ui.main.MainActivity;
import com.cool.pulseit.R;
import com.cool.pulseit.database.DatabaseManager;
import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.utils.DateFormatter;
import com.cool.pulseit.utils.Result;
import com.cool.pulseit.utils.StatusSnackbar;

import java.util.ArrayList;
import java.util.List;

public class PulsesAdapter extends RecyclerView.Adapter<PulsesViewHolder> implements Filterable {

    private List<Pulse> _pulses;
    private List<Pulse> _filteredPulses;
    private Context _context = MainActivity.getContext();
    private View _view;

    public PulsesAdapter(List<Pulse> pulses) {
        _pulses = new ArrayList<>(pulses);
        _filteredPulses = new ArrayList<>(pulses);
    }

    @NonNull
    @Override
    public PulsesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _view = parent.getRootView();
        final LayoutInflater inflater = LayoutInflater.from(_context);

        View historyView = inflater.inflate(R.layout.history_item, parent, false);

        final PulsesViewHolder viewHolder = new PulsesViewHolder(historyView);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pulse pulse = _filteredPulses.get(viewHolder.getAdapterPosition());
                FragmentManager fm = ((FragmentActivity) _context).getSupportFragmentManager();
                DetailDialogFragment dialog = new DetailDialogFragment(pulse);
                dialog.show(fm, "");
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PulsesViewHolder holder, int position) {
        Pulse pulse = _filteredPulses.get(position);

        TextView labelPulse = holder.pulseLabel;
        labelPulse.setText(String.valueOf(pulse.pulse));
        TextView labelDate = holder.dateLabel;
        labelDate.setText(DateFormatter.forUiWithTime(pulse.date));
        TextView labelDescription = holder.descriptionLabel;
        labelDescription.setText(pulse.description);
    }

    @Override
    public int getItemCount() {
        return _filteredPulses.size();
    }

    public void deleteItem(int position) {

        Pulse pulse = _filteredPulses.get(position);

        DatabaseManager dbm = new DatabaseManager(_context);
        Result result = dbm.deletePulse(pulse);

        if (!result.isOk()) {
            StatusSnackbar.show((Activity) _context, result.getMessage());
            return;
        }

        _filteredPulses.remove(pulse);
        _pulses.remove(pulse);
        notifyItemRemoved(position);

        StatusSnackbar.show((Activity) _context, result.getMessage());
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Pulse> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(_pulses);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Pulse pulse : _pulses) {
                        if (pulse.description.toLowerCase().contains(filterPattern)) {
                            filteredList.add(pulse);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                _filteredPulses.clear();
                _filteredPulses.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
