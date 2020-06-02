package com.cool.pulseit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.cool.pulseit.R;
import com.cool.pulseit.database.DatabaseManager;
import com.cool.pulseit.entities.Settings;
import com.cool.pulseit.utils.Gender;
import com.cool.pulseit.utils.Result;
import com.cool.pulseit.utils.StatusSnackbar;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsInputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsInputFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View _mainActivity;
    private NumberPicker _weightNumberPicker;
    private NumberPicker _ageNumberPicker;
    private Spinner _genderSpinner;
    private Button _saveButton;

    public SettingsInputFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsInputFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsInputFragment newInstance(String param1, String param2) {
        SettingsInputFragment fragment = new SettingsInputFragment();
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
        _mainActivity = inflater.inflate(R.layout.fragment_settings_input, container, false);

        getViewElements();

        initializeWeightNumberPicker();
        initializeAgeNumberPicker();

        initializeEventListeners();

        return _mainActivity;
    }

    private void initializeEventListeners() {
        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    private void getViewElements() {
        _weightNumberPicker = _mainActivity.findViewById(R.id.settings_input_weight);
        _ageNumberPicker = _mainActivity.findViewById(R.id.settings_input_age);
        _genderSpinner = _mainActivity.findViewById(R.id.settings_spinner_gender);
        _saveButton = _mainActivity.findViewById(R.id.settings_button_save);
    }

    private void initializeWeightNumberPicker() {
        _weightNumberPicker.setMinValue(0);
        _weightNumberPicker.setMaxValue(500);

        DatabaseManager dbm = new DatabaseManager(this.getContext());
        Result<Settings> result = dbm.getLatestSettings();

        if (!result.isOk()) {
            _weightNumberPicker.setValue(60);
        } else {
            _weightNumberPicker.setValue(result.getValue().weight);
        }
    }

    private void initializeAgeNumberPicker() {
        _ageNumberPicker.setMinValue(0);
        _ageNumberPicker.setMaxValue(150);

        DatabaseManager dbm = new DatabaseManager(this.getContext());
        Result<Settings> result = dbm.getLatestSettings();

        if (!result.isOk()) {
            _ageNumberPicker.setValue(30);
        } else {
            _ageNumberPicker.setValue(result.getValue().age);
        }
    }

    public void saveSettings() {
        int age = _ageNumberPicker.getValue();
        int weight = _weightNumberPicker.getValue();
        Gender gender = Gender.toEnum(_genderSpinner.getSelectedItem().toString());
        Date date = new Date();

        Settings settings = new Settings(gender, weight, age, date);

        DatabaseManager dbm = new DatabaseManager(this.getContext());
        Result result = dbm.saveSettings(settings);

        if (!result.isOk()) {
            StatusSnackbar.show(getActivity(), result.getMessage());
            return;
        }

        StatusSnackbar.show(getActivity(), result.getMessage());
    }
}