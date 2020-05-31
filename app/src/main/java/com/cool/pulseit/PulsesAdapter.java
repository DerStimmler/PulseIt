package com.cool.pulseit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cool.pulseit.database.DatabaseManager;
import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.utils.DateFormatter;

import java.util.List;

public class PulsesAdapter extends RecyclerView.Adapter<PulsesViewHolder>{

    private List<Pulse> _pulses;
    private Context _context;

    public PulsesAdapter(List<Pulse> pulses){
        _pulses = pulses;
    }

    @NonNull
    @Override
    public PulsesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(_context);

        View historyView = inflater.inflate(R.layout.history_row, parent, false);

        PulsesViewHolder viewHolder = new PulsesViewHolder(historyView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PulsesViewHolder holder, int position) {
        Pulse pulse = _pulses.get(position);

        TextView labelPulse = holder.pulseLabel;
        labelPulse.setText(String.valueOf(pulse.pulse));
        TextView labelDate = holder.dateLabel;
        labelDate.setText(DateFormatter.forUi(pulse.date));
    }

    @Override
    public int getItemCount() {
        return _pulses.size();
    }

    public void deleteItem(int position){
        DatabaseManager dbm = new DatabaseManager(_context);

        Pulse pulse = _pulses.get(position);
        dbm.deletePulse(pulse);
        _pulses.remove(position);
        notifyItemRemoved(position);

        Toast.makeText(_context, "Gelöscht", Toast.LENGTH_SHORT).show();
    }
}
