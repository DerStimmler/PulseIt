package com.cool.pulseit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cool.pulseit.AnalyticsCalculator;
import com.cool.pulseit.R;
import com.cool.pulseit.database.DatabaseManager;
import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.utils.DateFormatter;
import com.cool.pulseit.utils.Result;
import com.cool.pulseit.utils.StatusSnackbar;
import com.cool.pulseit.utils.Zone;
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
    private TextView _maxPulse;
    private TextView _minPulse;
    private TextView _commonZone;

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

        return _mainActivity;
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

        Result<List<Pulse>> pulsesResult = dbm.getPulses();

        if (!pulsesResult.isOk()) {
            StatusSnackbar.show(getActivity(), pulsesResult.getMessage());
            return;
        }

        AnalyticsCalculator analyticsCalculator = new AnalyticsCalculator(pulsesResult.getValue(), getContext());

        Result<Zone> zoneResult = analyticsCalculator.calculateCommonZone(from, to);
        if (!zoneResult.isOk()) {
            StatusSnackbar.show(getActivity(), zoneResult.getMessage());
            return;
        }
        _commonZone.setText(zoneResult.getValue().toString());

        Result<Integer> minPulseResult = analyticsCalculator.calculateMinPulse(from, to);
        if (!minPulseResult.isOk()) {
            StatusSnackbar.show(getActivity(), minPulseResult.getMessage());
            return;
        }
        _minPulse.setText(String.valueOf(minPulseResult.getValue()));

        Result<Integer> maxPulseResult = analyticsCalculator.calculateMaxPulse(from, to);
        if (!maxPulseResult.isOk()) {
            StatusSnackbar.show(getActivity(), maxPulseResult.getMessage());
            return;
        }
        _maxPulse.setText(String.valueOf(maxPulseResult.getValue()));

        setDatePicker(from, to);
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
        _maxPulse = _mainActivity.findViewById(R.id.analytics_max_pulse);
        _minPulse = _mainActivity.findViewById(R.id.analytics_min_pulse);
        _commonZone = _mainActivity.findViewById(R.id.analytics_common_zone);
    }
}
