package com.cool.pulseit;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.res.ResourcesCompat;

import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.utils.Zone;
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

    public ChartGenerator(Context context) {
        _context = context;
        _colors = getColours();
        _stackLabels = getStackLabels();
    }

    public BarChart classifyPulseChart(BarChart chart, Pulse pulse, int maxPulse) {

        List<BarEntry> entries = getEntries(maxPulse);

        BarDataSet dataSet = createBarDataSet(entries);

        BarData bardata = createBarData(dataSet);

        addLimitLines(chart, entries, pulse);

        chart.setData(bardata);
        setStandardChartOptions(chart);

        return chart;
    }

    public BarChart generateInfoChart(BarChart chart) {

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, new float[]{50f, 10f, 10f, 10f, 10f, 10f}));

        BarDataSet dataSet = createBarDataSet(entries);

        BarData bardata = createBarData(dataSet);

        addLimitLines(chart, entries, null);

        chart.setData(bardata);
        setStandardChartOptions(chart);
        chart.getAxisLeft().setValueFormatter(new PercentFormatter());

        return chart;
    }

    private String[] getStackLabels() {
        return new String[]{Zone.VERYLIGHT.getValue(), Zone.LIGHT.getValue(), Zone.MODERATE.getValue(), Zone.HARD.getValue(), Zone.VERYHARD.getValue()};
    }

    private int[] getColours() {
        return new int[]{Color.TRANSPARENT, ResourcesCompat.getColor(_context.getResources(), R.color.grey, null), ResourcesCompat.getColor(_context.getResources(), R.color.turquoise, null), ResourcesCompat.getColor(_context.getResources(), R.color.green, null), ResourcesCompat.getColor(_context.getResources(), R.color.orange, null), ResourcesCompat.getColor(_context.getResources(), R.color.red, null)};
    }

    private void addLimitLines(BarChart chart, List<BarEntry> entries, Pulse pulse) {
        LimitLine ll;
        float x = entries.get(0).getYVals()[0];
        ll = new LimitLine(x);
        ll.setLineColor(Color.LTGRAY);
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(entries.get(0).getYVals()[1] + x);
        ll.setLineColor(Color.LTGRAY);
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(entries.get(0).getYVals()[1] / 2 + x, Zone.VERYLIGHT.getValue());
        ll.setLineColor(Color.TRANSPARENT);
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[1];
        ll = new LimitLine(entries.get(0).getYVals()[2] + x);
        ll.setLineColor(Color.LTGRAY);
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(entries.get(0).getYVals()[2] / 2 + x, Zone.LIGHT.getValue());
        ll.setLineColor(Color.TRANSPARENT);
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[2];
        ll = new LimitLine(entries.get(0).getYVals()[3] + x);
        ll.setLineColor(Color.LTGRAY);
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(entries.get(0).getYVals()[3] / 2 + x, Zone.MODERATE.getValue());
        ll.setLineColor(Color.TRANSPARENT);
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[3];
        ll = new LimitLine(entries.get(0).getYVals()[4] + x);
        ll.setLineColor(Color.LTGRAY);
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(entries.get(0).getYVals()[4] / 2 + x, Zone.HARD.getValue());
        ll.setLineColor(Color.TRANSPARENT);
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[4];
        ll = new LimitLine(entries.get(0).getYVals()[5] + x);
        ll.setLineColor(Color.LTGRAY);
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(entries.get(0).getYVals()[5] / 2 + x, Zone.VERYHARD.getValue());
        ll.setLineColor(Color.TRANSPARENT);
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[5];
        if (pulse != null) {
            ll = new LimitLine(pulse.pulse, "Eigener Puls");
            chart.getAxisLeft().addLimitLine(ll);
        }
    }

    private BarData createBarData(BarDataSet dataSet) {
        BarData bardata = new BarData(dataSet);
        bardata.setBarWidth(0.5f);

        return bardata;
    }

    private void setStandardChartOptions(BarChart chart) {
        chart.getXAxis().setAxisMaximum(0.2f);
        chart.getXAxis().setAxisMinimum(1f);
        chart.getXAxis().setLabelCount(0, true);
        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setGranularityEnabled(true);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setDrawLabels(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        chart.setHighlightFullBarEnabled(false);
        chart.getData().setHighlightEnabled(false);
        chart.setDrawBarShadow(false);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setSpaceMin(0);
        chart.getXAxis().setSpaceMax(0);
        chart.setBackgroundColor(Color.WHITE);

        chart.setFitBars(true);
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

    private BarDataSet createBarDataSet(List<BarEntry> entries) {
        BarDataSet dataSet = new BarDataSet(entries, "Label");
        dataSet.setColors(_colors);
        dataSet.setStackLabels(_stackLabels);
        dataSet.setValueTextColor(Color.TRANSPARENT);

        return dataSet;
    }


}
