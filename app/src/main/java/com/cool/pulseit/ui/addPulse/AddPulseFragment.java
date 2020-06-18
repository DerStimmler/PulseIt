package com.cool.pulseit.ui.addPulse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;

import com.cool.pulseit.R;
import com.cool.pulseit.database.DatabaseManager;
import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.entities.Settings;
import com.cool.pulseit.utils.Result;
import com.cool.pulseit.utils.StatusSnackbar;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPulseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPulseFragment extends Fragment {

    private View _view;
    private ImageButton _tapButton;
    private NumberPicker _pulseNumberPicker;
    private Button _saveButton;
    private HeartRateCalculator _heartRateCalculator;
    private EditText _descriptionText;

    public AddPulseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddPulseFragment.
     */
    public static AddPulseFragment newInstance() {
        AddPulseFragment fragment = new AddPulseFragment();
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
        _view = inflater.inflate(R.layout.fragment_add_pulse, container, false);

        _heartRateCalculator = new HeartRateCalculator();

        getViewElements();
        initializePulseNumberPicker();
        initializeEventListeners();

        _tapButton.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pulse));

        return _view;
    }

    private void initializeEventListeners() {
        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePulse();
            }
        });
        _tapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tap();
            }
        });
    }

    private void tap() {
        _heartRateCalculator.tap();
        int rate = _heartRateCalculator.calculate();
        _pulseNumberPicker.setValue(rate);
    }

    private void savePulse() {
        DatabaseManager dbm = new DatabaseManager(this.getContext());

        int pulseValue = _pulseNumberPicker.getValue();
        Date date = new Date();
        String description = _descriptionText.getText().toString();

        Result<Settings> result = dbm.getLatestSettings();

        if (!result.isOk()) {
            StatusSnackbar.show(getActivity(), result.getMessage());
            return;
        }

        Pulse pulse = new Pulse(date, pulseValue, description, result.getValue());

        result = dbm.savePulse(pulse);

        if (!result.isOk()) {
            StatusSnackbar.show(getActivity(), result.getMessage());
            return;
        }

        StatusSnackbar.show(getActivity(), result.getMessage());
    }

    private void initializePulseNumberPicker() {
        _pulseNumberPicker.setMinValue(0);
        _pulseNumberPicker.setMaxValue(500);
        _pulseNumberPicker.setValue(0);
    }

    private void getViewElements() {
        _tapButton = _view.findViewById(R.id.add_pulse_button_tap);
        _pulseNumberPicker = _view.findViewById(R.id.addPulse_input_pulse);
        _saveButton = _view.findViewById(R.id.add_pulse_button_save);
        _descriptionText = _view.findViewById(R.id.add_pulse_input_description);
    }
}
