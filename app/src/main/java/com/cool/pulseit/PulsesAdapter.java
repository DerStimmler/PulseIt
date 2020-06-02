package com.cool.pulseit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.github.mikephil.charting.charts.BarChart;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
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
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Darstellung erster Card ->Puls+Datum+Zone
                TextView dialog_pulse_tv = myDialog.findViewById(R.id.dialog_pulse_id);
                TextView dialog_date_tv = myDialog.findViewById(R.id.dialog_date_id);
                TextView dialog_zones_tv = myDialog.findViewById(R.id.dialog_zones_id);

                Pulse pulse = _pulses.get(viewHolder.getAdapterPosition());
                dialog_pulse_tv.setText(String.valueOf(pulse.pulse));
                dialog_date_tv.setText(DateFormatter.forUi(pulse.date));

                ZoneCalculator zoneCalculator = new ZoneCalculator(pulse.pulse,pulse.settings.age,pulse.settings.weight, pulse.settings.gender);
                String zone = zoneCalculator.calculateZone();
                dialog_zones_tv.setText(zone);

                //Dastellung zweiter Card -> Settings
                TextView dialog_gender_tv = myDialog.findViewById(R.id.dialog_settings_gender);
                TextView dialog_age_tv = myDialog.findViewById(R.id.dialog_settings_age);
                TextView dialog_weight_tv = myDialog.findViewById(R.id.dialog_settings_weight);

                dialog_gender_tv.setText(String.valueOf(pulse.settings.gender));
                dialog_age_tv.setText(String.valueOf(pulse.settings.age));
                dialog_weight_tv.setText(String.valueOf(pulse.settings.weight));


                BarChart chart = myDialog.findViewById(R.id.detail_chart);
                MaximumHeartRateCalculator mhrCalculator = new MaximumHeartRateCalculator(pulse.settings.age, pulse.settings.weight, pulse.settings.gender);
                int mhr = mhrCalculator.calculateMaximumHeartRate();
                ChartGenerator cg = new ChartGenerator(_context);

                chart = cg.classifyPulseChart(chart, pulse, mhr);

                chart.invalidate();

                Toast.makeText(_context, "Test Click" + String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show(); //Todo: raus löschen am ende
                myDialog.show();

                share(myDialog);
            }
        });

        return viewHolder;
    }

    private void share(Dialog dialog) {
        LinearLayout layout = dialog.findViewById(R.id.history_row_linear_layout);

        layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(layout.getMeasuredWidth(), layout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        layout.layout(0,0, layout.getMeasuredWidth(), layout.getMeasuredHeight());
        layout.draw(canvas);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, byteArray);
        myDialog.getContext().startActivity(Intent.createChooser(shareIntent,"Share via..."));
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
