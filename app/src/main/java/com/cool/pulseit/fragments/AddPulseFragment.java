package com.cool.pulseit.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.cool.pulseit.HeartRateCalculator;
import com.cool.pulseit.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPulseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPulseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private View _mainActivity;
    private Button _tapButton;
    private NumberPicker _pulseNumberPicker;
    private Button _saveButton;
    private HeartRateCalculator _heartRateCalculator;

    public AddPulseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPulseFragment.
     */
    public static AddPulseFragment newInstance(String param1, String param2) {
        AddPulseFragment fragment = new AddPulseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _mainActivity =  inflater.inflate(R.layout.fragment_add_pulse, container, false);

        _heartRateCalculator = new HeartRateCalculator(getActivity());

        getViewElements();
        initializePulseNumberPicker();
        initializeEventListeners();

        return _mainActivity;
    }

    private void initializeEventListeners() {
        _saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                savePulse();
            }
        });
        _tapButton.setOnClickListener(new View.OnClickListener(){
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

    }

    private void initializePulseNumberPicker() {
        _pulseNumberPicker.setMinValue(0);
        _pulseNumberPicker.setMaxValue(500);
        _pulseNumberPicker.setValue(0);
    }

    private void getViewElements() {
        _tapButton = _mainActivity.findViewById(R.id.add_pulse_button_tap);
        _pulseNumberPicker = _mainActivity.findViewById(R.id.addPulse_input_pulse);
        _saveButton = _mainActivity.findViewById(R.id.add_pulse_button_save);
    }
}
