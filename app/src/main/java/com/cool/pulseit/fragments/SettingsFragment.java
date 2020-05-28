package com.cool.pulseit.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.cool.pulseit.R;
import com.cool.pulseit.database.DatabaseManager;
import com.cool.pulseit.entities.Settings;
import com.cool.pulseit.utils.Gender;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
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

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        _mainActivity = inflater.inflate(R.layout.fragment_settings, container, false);

        getViewElements();

        initializeWeightNumberPicker();
        initializeAgeNumberPicker();

        initializeEventListeners();

        return _mainActivity;
    }

    private void initializeEventListeners() {
        _saveButton.setOnClickListener(new View.OnClickListener(){
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
        _weightNumberPicker.setValue(60);
    }

    private void initializeAgeNumberPicker() {
        _ageNumberPicker.setMinValue(0);
        _ageNumberPicker.setMaxValue(150);
        _ageNumberPicker.setValue(30);
    }

    public void saveSettings(){
        int age = _ageNumberPicker.getValue();
        int weight = _weightNumberPicker.getValue();
        Gender gender = Gender.toEnum(_genderSpinner.getSelectedItem().toString());

        Settings settings = new Settings(gender,weight,age);

        DatabaseManager dbm = new DatabaseManager(this.getContext());

        dbm.saveSettings(settings);

        Toast.makeText(this.getContext(), "Gespeichert", Toast.LENGTH_SHORT).show();
    }


}
