package com.cool.pulseit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cool.pulseit.MaximumHeartRateCalculator;
import com.cool.pulseit.R;
import com.cool.pulseit.database.DatabaseManager;
import com.cool.pulseit.entities.Settings;
import com.cool.pulseit.utils.Result;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View _mainActivity;
    private TextView _weightNumberView;
    private TextView _ageNumberView;
    private TextView _genderView;
    private TextView _resultView;

    public SettingsResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsResultFragment newInstance(String param1, String param2) {
        SettingsResultFragment fragment = new SettingsResultFragment();
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
        _mainActivity = inflater.inflate(R.layout.fragment_settings_result, container, false);
        getViewElements();
        DatabaseManager dbm = new DatabaseManager(this.getContext());
        Result<Settings> result = dbm.getLatestSettings();

        _weightNumberView.setText(String.valueOf(result.getValue().weight));
        _ageNumberView.setText(String.valueOf(result.getValue().age));
        _genderView.setText(String.valueOf(result.getValue().gender));

        MaximumHeartRateCalculator maximumHeartRateCalculator = new MaximumHeartRateCalculator(result.getValue().age,result.getValue().weight, result.getValue().gender);
        int maximumHeartRate = maximumHeartRateCalculator.calculateMaximumHeartRate();
        _resultView.setText(String.valueOf(maximumHeartRate));


        return _mainActivity;
    }

    private void getViewElements() {
        _weightNumberView = _mainActivity.findViewById(R.id.settings_result_weight);
        _ageNumberView = _mainActivity.findViewById(R.id.settings_result_age);
        _genderView = _mainActivity.findViewById(R.id.settings_result_gender);
        _resultView = _mainActivity.findViewById(R.id.settings_result_result);
    }
}
