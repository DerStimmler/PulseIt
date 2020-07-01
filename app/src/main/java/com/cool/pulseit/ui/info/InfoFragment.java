package com.cool.pulseit.ui.info;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cool.pulseit.R;
import com.cool.pulseit.services.ChartGenerator;
import com.github.mikephil.charting.charts.BarChart;

import io.github.kexanie.library.MathView;

public class InfoFragment extends Fragment {
    private View _view;
    private MathView _info_formula_one;
    private MathView _info_formula_two;
    private BarChart _chart;

    public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
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
        _view = inflater.inflate(R.layout.fragment_info, container, false);
        _info_formula_one = _view.findViewById(R.id.info_formula_one);
        _info_formula_one.setText(getString(R.string.info_formula_male));

        _info_formula_two = _view.findViewById(R.id.info_formula_two);
        _info_formula_two.setText(getString(R.string.info_formula_female));

        _chart = _view.findViewById(R.id.info_chart);

        TextView info_others = _view.findViewById(R.id.info_others);
        info_others.setMovementMethod(LinkMovementMethod.getInstance());

        initChart();

        return _view;
    }

    private void initChart() {

        ChartGenerator cg = new ChartGenerator();
        _chart = cg.generateInfoChart(_chart);
        _chart.invalidate();
    }

}
