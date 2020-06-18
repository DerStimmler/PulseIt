package com.cool.pulseit.ui.history;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cool.pulseit.R;

public class PulsesViewHolder extends RecyclerView.ViewHolder {

    public TextView descriptionLabel;
    public TextView pulseLabel;
    public TextView dateLabel;

    public PulsesViewHolder(@NonNull View itemView) {
        super(itemView);

        pulseLabel = itemView.findViewById(R.id.history_row_pulse);
        dateLabel = itemView.findViewById(R.id.history_row_date);
        descriptionLabel = itemView.findViewById(R.id.history_row_description);
    }
}
