package com.cool.pulseit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cool.pulseit.database.DatabaseManager;
import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.utils.DateFormatter;
import com.cool.pulseit.utils.Result;
import com.cool.pulseit.utils.StatusSnackbar;

import org.w3c.dom.Text;

import java.util.List;

public class PulsesAdapter extends RecyclerView.Adapter<PulsesViewHolder> {

    private List<Pulse> _pulses;
    private Context _context;
    private View _mainActivity;
    private Dialog myDialog;

    public PulsesAdapter(List<Pulse> pulses){

        _pulses = pulses;
    }

    @NonNull
    @Override
    public PulsesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _context = parent.getContext();
        _mainActivity = parent.getRootView();
        LayoutInflater inflater = LayoutInflater.from(_context);

        View historyView = inflater.inflate(R.layout.history_row, parent, false);

        final PulsesViewHolder viewHolder = new PulsesViewHolder(historyView);

        // Dialog ini
        myDialog = new Dialog(_context);
        myDialog.setContentView(R.layout.dialog_history);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView dialog_pulse_tv = (TextView) myDialog.findViewById(R.id.dialog_pulse_id);
                TextView dialog_date_tv = (TextView) myDialog.findViewById(R.id.dialog_date_id);

                Pulse pulse = _pulses.get(viewHolder.getAdapterPosition());
                dialog_pulse_tv.setText(String.valueOf(pulse.pulse));
                dialog_date_tv.setText(DateFormatter.forUi(pulse.date));
                Toast.makeText(_context, "Test Click" + String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show(); //Todo: raus löschen am ende
                myDialog.show();
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
