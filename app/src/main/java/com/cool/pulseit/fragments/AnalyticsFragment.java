package com.cool.pulseit.fragments;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cool.pulseit.AnalyticsCalculator;
import com.cool.pulseit.ChartGenerator;
import com.cool.pulseit.R;
import com.cool.pulseit.database.DatabaseManager;
import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.utils.DateFormatter;
import com.cool.pulseit.utils.Result;
import com.cool.pulseit.utils.StatusSnackbar;
import com.cool.pulseit.utils.Zone;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalyticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalyticsFragment extends Fragment {

    private View _mainActivity;
    private EditText _datePicker;
    private PieChart _zonesChart;
    private TextView _circular_max_text;
    private TextView _circular_min_text;
    private TextView _circular_avg_text;
    private FrameLayout _circular_max;
    private FrameLayout _circular_min;
    private FrameLayout _circular_avg;

    public AnalyticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AnalyticsFragments.
     */
    public static AnalyticsFragment newInstance() {
        AnalyticsFragment fragment = new AnalyticsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _mainActivity = inflater.inflate(R.layout.fragment_analytics, container, false);

        getElements();
        setEventListener();
        initDatePicker();
        initCirculars();

        _mainActivity.post(new Runnable() {
            @Override
            public void run() {
                adjustCircularsHeights();
            }
        });

        return _mainActivity;
    }

    private void initCirculars() {
        ((GradientDrawable) _circular_avg.getBackground()).setStroke(10,getResources().getColor(R.color.avg));
        ((GradientDrawable) _circular_min.getBackground()).setStroke(10,getResources().getColor(R.color.min));
        ((GradientDrawable) _circular_max.getBackground()).setStroke(10,getResources().getColor(R.color.max));
    }

    private void adjustCircularsHeights(){
        int width = _circular_avg.getWidth();

        if(width < _circular_min.getWidth())
            width = _circular_min.getWidth();

        if(width < _circular_max.getWidth())
            width = _circular_max.getWidth();

        _circular_avg.setMinimumHeight(width);
        _circular_min.setMinimumHeight(width);
        _circular_max.setMinimumHeight(width);
    }

    private void setEventListener() {
        _datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
                MaterialDatePicker picker = builder.build();
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Field firstField = selection.getClass().getDeclaredFields()[0];
                        firstField.setAccessible(true);

                        Field secondField = selection.getClass().getDeclaredFields()[1];
                        secondField.setAccessible(true);

                        Long firstLong = null;
                        Long secondLong = null;
                        try {
                            firstLong = (Long) firstField.get(selection);
                            secondLong = (Long) secondField.get(selection);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        Date from = new Date(firstLong);
                        Date to = new Date(secondLong);

                        //set time of dates to start of day and midnight
                        Calendar calendar = Calendar.getInstance();

                        calendar.setTime(from);
                        calendar.set(Calendar.HOUR, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);

                        from = calendar.getTime();

                        calendar.setTime(to);
                        calendar.set(Calendar.HOUR, 23);
                        calendar.set(Calendar.MINUTE, 59);
                        calendar.set(Calendar.SECOND, 59);

                        to = calendar.getTime();

                        updateValues(from, to);
                    }
                });
                picker.show(getFragmentManager(), picker.toString());
            }
        });
    }

    private void updateValues(Date from, Date to) {

        DatabaseManager dbm = new DatabaseManager(getContext());

        Result<List<Pulse>> pulsesResult = dbm.getPulses(from,to);

        if (!pulsesResult.isOk()) {
            StatusSnackbar.show(getActivity(), pulsesResult.getMessage());
            return;
        }

        AnalyticsCalculator analyticsCalculator = new AnalyticsCalculator(pulsesResult.getValue(), getContext());

        Result<Integer> avgPulseResult = analyticsCalculator.calculateAveragePulse();
        if (!avgPulseResult.isOk()) {
            StatusSnackbar.show(getActivity(), avgPulseResult.getMessage());
            return;
        }
        _circular_avg_text.setText(avgPulseResult.getValue().toString());

        Result<Integer> minPulseResult = analyticsCalculator.calculateMinPulse();
        if (!minPulseResult.isOk()) {
            StatusSnackbar.show(getActivity(), minPulseResult.getMessage());
            return;
        }
        _circular_min_text.setText(String.valueOf(minPulseResult.getValue()));

        Result<Integer> maxPulseResult = analyticsCalculator.calculateMaxPulse();
        if (!maxPulseResult.isOk()) {
            StatusSnackbar.show(getActivity(), maxPulseResult.getMessage());
            return;
        }
        _circular_max_text.setText(String.valueOf(maxPulseResult.getValue()));

        generateChart(pulsesResult.getValue());

        setDatePicker(from, to);
        adjustCircularsHeights();
    }

    private void generateChart(List<Pulse> pulses) {
        ChartGenerator cg = new ChartGenerator(getContext());
        _zonesChart = cg.generateZonesPieChart(_zonesChart, pulses);
        _zonesChart.invalidate();
    }

    private void initDatePicker() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        Date from = new Date(c.getTimeInMillis());
        Date to = new Date();

        updateValues(from, to);
    }

    private void setDatePicker(Date from, Date to) {
        _datePicker.setText(String.format("%s - %s", DateFormatter.forUiWithoutTime(from), DateFormatter.forUiWithoutTime(to)));
    }

    private void getElements() {
        _datePicker = _mainActivity.findViewById(R.id.analytics_date);
        _zonesChart = _mainActivity.findViewById(R.id.analytics_zone_chart);
        _circular_max_text = _mainActivity.findViewById(R.id.analytics_circular_max_text);
        _circular_max = _mainActivity.findViewById(R.id.analytics_circular_max);
        _circular_min_text = _mainActivity.findViewById(R.id.analytics_circular_min_text);
        _circular_min = _mainActivity.findViewById(R.id.analytics_circular_min);
        _circular_avg_text = _mainActivity.findViewById(R.id.analytics_circular_avg_text);
        _circular_avg = _mainActivity.findViewById(R.id.analytics_circular_avg);
    }
}
