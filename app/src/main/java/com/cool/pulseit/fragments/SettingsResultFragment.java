package com.cool.pulseit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cool.pulseit.ChartGenerator;
import com.cool.pulseit.MaximumHeartRateCalculator;
import com.cool.pulseit.R;
import com.cool.pulseit.database.DatabaseManager;
import com.cool.pulseit.entities.Settings;
import com.cool.pulseit.utils.Gender;
import com.cool.pulseit.utils.Result;
import com.cool.pulseit.utils.StatusSnackbar;
import com.github.mikephil.charting.charts.BarChart;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsResultFragment extends Fragment {

    private View _view;
    private TextView _weightNumberView;
    private TextView _ageNumberView;
    private TextView _genderView;
    private TextView _resultView;
    private BarChart _chart;

    public SettingsResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsResultFragment.
     */
    public static SettingsResultFragment newInstance() {
        SettingsResultFragment fragment = new SettingsResultFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_settings_result, container, false);
        getViewElements();

        DatabaseManager dbm = new DatabaseManager(this.getContext());
        Result<Settings> result = dbm.getLatestSettings();

        if (!result.isOk()) {
            StatusSnackbar.show(getActivity(), result.getMessage());
            return _view;
        }

        updateValues(result.getValue().age, result.getValue().weight, result.getValue().gender);

        return _view;
    }

    private void getViewElements() {
        _weightNumberView = _view.findViewById(R.id.settings_result_weight);
        _ageNumberView = _view.findViewById(R.id.settings_result_age);
        _genderView = _view.findViewById(R.id.settings_result_gender);
        _resultView = _view.findViewById(R.id.settings_result_result);
        _chart = _view.findViewById(R.id.settings_result_chart);
    }

    public void updateValues(int age, int weight, Gender gender) {
        _ageNumberView.setText(String.valueOf(age));
        _weightNumberView.setText(String.valueOf(weight));
        _genderView.setText(gender.toString());
        MaximumHeartRateCalculator maximumHeartRateCalculator = new MaximumHeartRateCalculator(age, weight, gender);
        int maximumHeartRate = maximumHeartRateCalculator.calculateMaximumHeartRate();
        _resultView.setText(String.valueOf(maximumHeartRate));


        ChartGenerator cg = new ChartGenerator();

        _chart = cg.classifyPulseChart(_chart, null, maximumHeartRate);
        _chart.invalidate();
    }
}
