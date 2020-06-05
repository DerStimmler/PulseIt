package com.cool.pulseit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cool.pulseit.ChartGenerator;
import com.cool.pulseit.MaximumHeartRateCalculator;
import com.cool.pulseit.R;
import com.cool.pulseit.Share;
import com.cool.pulseit.ZoneCalculator;
import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.utils.DateFormatter;
import com.github.mikephil.charting.charts.BarChart;

public class DetailDialogFragment extends DialogFragment {

    private final Pulse _pulse;
    private View _view;

    public DetailDialogFragment(Pulse pulse) {
        _pulse = pulse;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.detail_dialog, container, false);
        getDialog().setTitle("Details");

        init();
        setEventListener();

        return _view;
    }

    private void setEventListener() {
        ImageView shareIcon = _view.findViewById(R.id.detail_dialog_share_icon);
        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share.shareView(_view.getContext(), _view.findViewById(R.id.detail_dialog_framelayout));
            }
        });
    }

    private void init() {
        // Darstellung erster Card ->Puls+Datum+Zone
        TextView dialog_pulse_tv = _view.findViewById(R.id.detail_dialog_pulse);
        TextView dialog_date_tv = _view.findViewById(R.id.detail_dialog_date);
        TextView dialog_zones_tv = _view.findViewById(R.id.detail_dialog_zone);
        TextView dialog_description = _view.findViewById(R.id.detail_dialog_description);


        dialog_pulse_tv.setText(String.valueOf(_pulse.pulse));
        dialog_date_tv.setText(DateFormatter.forUiWithTime(_pulse.date));
        dialog_description.setText(_pulse.description);

        ZoneCalculator zoneCalculator = new ZoneCalculator(_pulse.pulse,_pulse.settings.age,_pulse.settings.weight, _pulse.settings.gender);
        String zone = zoneCalculator.calculateZone();
        dialog_zones_tv.setText(zone);

        //Dastellung zweiter Card -> Settings
        TextView dialog_gender_tv = _view.findViewById(R.id.detail_dialog_settings_gender);
        TextView dialog_age_tv = _view.findViewById(R.id.detail_dialog_settings_age);
        TextView dialog_weight_tv = _view.findViewById(R.id.detail_dialog_settings_weight);

        dialog_gender_tv.setText(String.valueOf(_pulse.settings.gender));
        dialog_age_tv.setText(String.valueOf(_pulse.settings.age));
        dialog_weight_tv.setText(String.valueOf(_pulse.settings.weight));


        BarChart chart = _view.findViewById(R.id.detail_dialog_chart);
        MaximumHeartRateCalculator mhrCalculator = new MaximumHeartRateCalculator(_pulse.settings.age, _pulse.settings.weight, _pulse.settings.gender);
        int mhr = mhrCalculator.calculateMaximumHeartRate();
        ChartGenerator cg = new ChartGenerator(getContext());

        chart = cg.classifyPulseChart(chart, _pulse, mhr);

        chart.invalidate();
    }




}
