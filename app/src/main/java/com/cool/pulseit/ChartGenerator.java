package com.cool.pulseit;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.res.ResourcesCompat;

import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.utils.Zones;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

public class ChartGenerator {

    private int[] _colors;
    private Context _context;
    private String[] _stackLabels;

    public ChartGenerator(Context context){
        _context = context;
        _colors = getColours();
        _stackLabels = getStackLabels();
    }

    private String[] getStackLabels() {
        return new String[]{Zones.VERYLIGHT.getValue(),Zones.LIGHT.getValue(),Zones.MODERATE.getValue(),Zones.HARD.getValue(), Zones.VERYHARD.getValue()};
    }

    private int[] getColours() {
        return new int[]{Color.TRANSPARENT,ResourcesCompat.getColor(_context.getResources(), R.color.grey, null), ResourcesCompat.getColor(_context.getResources(), R.color.turquoise, null),ResourcesCompat.getColor(_context.getResources(), R.color.green, null),ResourcesCompat.getColor(_context.getResources(), R.color.orange, null),ResourcesCompat.getColor(_context.getResources(), R.color.red, null)};
    }

    public BarChart classifyPulseChart(BarChart chart, Pulse pulse, int maxPulse){

        List<BarEntry> entries = getEntries(maxPulse);

        BarDataSet dataSet = new BarDataSet(entries,"Label");
        dataSet.setColors(_colors);
        dataSet.setStackLabels(_stackLabels);
        dataSet.setValueTextColor(Color.TRANSPARENT);

        BarData bardata = new BarData(dataSet);
        bardata.setBarWidth(0.5f);

        LimitLine ll;
        float x = entries.get(0).getYVals()[0];
        ll = new LimitLine(entries.get(0).getYVals()[1] + x, Zones.VERYLIGHT.getValue());
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[1];
        ll = new LimitLine(entries.get(0).getYVals()[2] + x, Zones.LIGHT.getValue());
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[2];
        ll = new LimitLine(entries.get(0).getYVals()[3] + x, Zones.MODERATE.getValue());
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[3];
        ll = new LimitLine(entries.get(0).getYVals()[4] + x, Zones.HARD.getValue());
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[4];
        ll = new LimitLine(entries.get(0).getYVals()[5] + x, Zones.VERYHARD.getValue());
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[5];
        ll = new LimitLine(pulse.pulse, "Eigener Puls");
        chart.getAxisLeft().addLimitLine(ll);

        chart.setData(bardata);
        chart.getXAxis().setAxisMaximum(1f);
        chart.getXAxis().setAxisMinimum(1f);
        chart.getXAxis().setLabelCount(0,true);
        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setGranularityEnabled(true);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setDrawLabels(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        chart.setHighlightFullBarEnabled(false);
        chart.getData().setHighlightEnabled(false);
        chart.setFitBars(true);

        return chart;
    }

    private List<BarEntry> getEntries(int maxPulse) {

        float[] yValues = new float[]{
                maxPulse * 0.5f - maxPulse * 0.0f,
                maxPulse * 0.6f - maxPulse * 0.5f,
                maxPulse * 0.7f - maxPulse * 0.6f,
                maxPulse * 0.8f - maxPulse * 0.7f,
                maxPulse * 0.9f - maxPulse * 0.8f,
                maxPulse * 1.0f - maxPulse * 0.9f
        };

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, yValues));

        return entries;
    }

    public BarChart generateInfoChart(BarChart chart){

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1,new float[]{50f,10f,10f,10f,10f,10f}));

        BarDataSet dataSet = new BarDataSet(entries,"Label");
        dataSet.setColors(_colors);
        dataSet.setStackLabels(_stackLabels);
        dataSet.setValueTextColor(Color.TRANSPARENT);

        BarData bardata = new BarData(dataSet);
        bardata.setBarWidth(0.5f);

        LimitLine ll;
        ll = new LimitLine(60f, Zones.VERYLIGHT.getValue());
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(70f, Zones.LIGHT.getValue());
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(80f, Zones.MODERATE.getValue());
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(90f, Zones.HARD.getValue());
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(100f, Zones.VERYHARD.getValue());
        chart.getAxisLeft().addLimitLine(ll);

        chart.setData(bardata);
        chart.getXAxis().setAxisMaximum(1f);
        chart.getXAxis().setAxisMinimum(1f);
        chart.getXAxis().setLabelCount(0,true);
        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisLeft().setAxisMaximum(110f);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setGranularityEnabled(true);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setDrawLabels(false);
        chart.getAxisLeft().setValueFormatter(new PercentFormatter());
        chart.setHighlightFullBarEnabled(false);
        chart.getData().setHighlightEnabled(false);
        chart.setFitBars(true);

        return chart;
    }
}
