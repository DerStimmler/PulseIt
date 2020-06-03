package com.cool.pulseit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cool.pulseit.database.DatabaseManager;
import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.fragments.DetailDialogFragment;
import com.cool.pulseit.utils.DateFormatter;
import com.cool.pulseit.utils.Result;
import com.cool.pulseit.utils.StatusSnackbar;

import java.util.List;

public class PulsesAdapter extends RecyclerView.Adapter<PulsesViewHolder> {

    private List<Pulse> _pulses;
    private Context _context;
    private View _mainActivity;

    public PulsesAdapter(List<Pulse> pulses){

        _pulses = pulses;
    }

    @NonNull
    @Override
    public PulsesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _context = parent.getContext();
        _mainActivity = parent.getRootView();
        final LayoutInflater inflater = LayoutInflater.from(_context);

        View historyView = inflater.inflate(R.layout.history_item, parent, false);

        final PulsesViewHolder viewHolder = new PulsesViewHolder(historyView);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pulse pulse = _pulses.get(viewHolder.getAdapterPosition());
                FragmentManager fm = ((FragmentActivity)_context).getSupportFragmentManager();
                DetailDialogFragment dialog = new DetailDialogFragment(pulse);
                dialog.show(fm, "");
            }
        });

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

    public void deleteItem(int position) {

        Pulse pulse = _pulses.get(position);

        DatabaseManager dbm = new DatabaseManager(_context);
        Result result = dbm.deletePulse(pulse);

        if (!result.isOk()) {
            StatusSnackbar.show((Activity) _context, result.getMessage());
            return;
        }

        _pulses.remove(position);
        notifyItemRemoved(position);

        StatusSnackbar.show((Activity) _context, result.getMessage());
    }
}
