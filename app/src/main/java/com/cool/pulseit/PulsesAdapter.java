package com.cool.pulseit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cool.pulseit.entities.Pulse;

import java.util.List;

public class PulsesAdapter extends RecyclerView.Adapter<PulsesViewHolder>{

    private List<Pulse> _pulses;

    public PulsesAdapter(List<Pulse> pulses){
        _pulses = pulses;
    }

    @NonNull
    @Override
    public PulsesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

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
        labelDate.setText(pulse.date.toString());
    }

    @Override
    public int getItemCount() {
        return _pulses.size();
    }


}
