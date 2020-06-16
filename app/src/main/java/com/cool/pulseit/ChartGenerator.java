package com.cool.pulseit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.utils.ArrayHelper;
import com.cool.pulseit.utils.Zone;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartGenerator {

    private Context _context = MainActivity.getContext();

    public PieChart generateZonesPieChart(PieChart chart, List<Pulse> pulses) {

        List<PieEntry> entries = getPieEntries(pulses);
        PieDataSet dataSet = getPieDataset(entries);
        PieData data = getPieData(dataSet, chart);

        setStandardPieChartOptions(chart);

        chart.setData(data);

        return chart;
    }

    private PieData getPieData(PieDataSet dataSet, PieChart chart) {
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(18f);

        return data;
    }

    private PieDataSet getPieDataset(List<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "Label");
        dataSet.setColors(getPieChartColours(entries));
        dataSet.setValueTextColor(Color.BLACK);

        return dataSet;
    }

    private int[] getBarChartColours() {
        Map<Zone, Integer> colorMap = getColours();

        List<Integer> colors = new ArrayList<>();

        colors.add(colorMap.get(Zone.NONE));
        colors.add(colorMap.get(Zone.VERYLIGHT));
        colors.add(colorMap.get(Zone.LIGHT));
        colors.add(colorMap.get(Zone.MODERATE));
        colors.add(colorMap.get(Zone.HARD));
        colors.add(colorMap.get(Zone.VERYHARD));

        return ArrayHelper.toArray(colors);
    }

    private int[] getPieChartColours(List<PieEntry> entries) {
        Map<Zone, Integer> colorMap = getColours();

        List<Integer> colors = new ArrayList<>();

        for (PieEntry entry : entries) {
            colors.add(colorMap.get(Zone.toEnum(entry.getLabel())));
        }

        return ArrayHelper.toArray(colors);
    }

    private Map<Zone, List<Pulse>> getPieMap(List<Pulse> pulses) {
        Map<Zone, List<Pulse>> map = new HashMap<>();

        for (Pulse pulse : pulses) {
            ZoneCalculator zoneCalculator = new ZoneCalculator(pulse);
            Zone z = zoneCalculator.calculateZone();

            List<Pulse> temp = new ArrayList<>();

            if (map.get(z) == null) {

                temp.add(pulse);
                map.put(z, temp);
                continue;
            }

            temp.addAll(map.get(z));
            temp.add(pulse);

            map.put(z, temp);
        }

        return map;
    }

    private List<PieEntry> getPieEntries(List<Pulse> pulses) {

        Map<Zone, List<Pulse>> map = getPieMap(pulses);

        List<PieEntry> entries = new ArrayList<>();

        for (Zone zone : map.keySet()) {
            PieEntry entry = new PieEntry(100f / pulses.size() * map.get(zone).size(), zone.toString());
            entries.add(entry);
        }

        return entries;
    }

    public BarChart classifyPulseChart(BarChart chart, Pulse pulse, int maxPulse) {

        List<BarEntry> entries = getBarEntries(maxPulse);

        BarDataSet dataSet = createBarDataSet(entries);

        BarData bardata = createBarData(dataSet);

        addLimitLines(chart, entries, pulse);

        chart.setData(bardata);
        setStandardBarChartOptions(chart);
        int count = maxPulse / 10;
        chart.getAxisLeft().setLabelCount(count);

        return chart;
    }

    public BarChart generateInfoChart(BarChart chart) {

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, new float[]{50f, 10f, 10f, 10f, 10f, 10f}));

        BarDataSet dataSet = createBarDataSet(entries);

        BarData bardata = createBarData(dataSet);

        addLimitLines(chart, entries, null);

        chart.setData(bardata);
        setStandardBarChartOptions(chart);
        chart.getAxisLeft().setValueFormatter(new PercentFormatter());
        chart.getAxisLeft().setAxisMaximum(110f);
        chart.getAxisLeft().setLabelCount(11);

        return chart;
    }

    private String[] getStackLabels() {
        return new String[]{Zone.VERYLIGHT.toString(), Zone.LIGHT.toString(), Zone.MODERATE.toString(), Zone.HARD.toString(), Zone.VERYHARD.toString()};
    }

    private Map<Zone, Integer> getColours() {
        Map<Zone, Integer> map = new HashMap<>();
        map.put(Zone.NONE, ResourcesCompat.getColor(_context.getResources(), R.color.lightblue, null));
        map.put(Zone.VERYLIGHT, ResourcesCompat.getColor(_context.getResources(), R.color.grey, null));
        map.put(Zone.LIGHT, ResourcesCompat.getColor(_context.getResources(), R.color.turquoise, null));
        map.put(Zone.MODERATE, ResourcesCompat.getColor(_context.getResources(), R.color.green, null));
        map.put(Zone.HARD, ResourcesCompat.getColor(_context.getResources(), R.color.orange, null));
        map.put(Zone.VERYHARD, ResourcesCompat.getColor(_context.getResources(), R.color.red, null));

        return map;
    }

    private void addLimitLines(BarChart chart, List<BarEntry> entries, Pulse pulse) {
        
        chart.getAxisLeft().removeAllLimitLines();

        LimitLine ll;
        float x = entries.get(0).getYVals()[0];
        ll = new LimitLine(x / 2, Zone.NONE.toString());
        ll.setLineColor(Color.TRANSPARENT);
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(x, String.valueOf((int) (x)));
        ll.setTextColor(Color.LTGRAY);
        ll.setLineColor(Color.LTGRAY);
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(entries.get(0).getYVals()[1] + x, String.valueOf((int) (entries.get(0).getYVals()[1] + x)));
        ll.setTextColor(Color.LTGRAY);
        ll.setLineColor(Color.LTGRAY);
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(entries.get(0).getYVals()[1] / 2 + x, Zone.VERYLIGHT.toString());
        ll.setLineColor(Color.TRANSPARENT);
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[1];
        ll = new LimitLine(entries.get(0).getYVals()[2] + x, String.valueOf((int) (entries.get(0).getYVals()[2] + x)));
        ll.setTextColor(Color.LTGRAY);
        ll.setLineColor(Color.LTGRAY);
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(entries.get(0).getYVals()[2] / 2 + x, Zone.LIGHT.toString());
        ll.setLineColor(Color.TRANSPARENT);
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[2];
        ll = new LimitLine(entries.get(0).getYVals()[3] + x, String.valueOf((int) (entries.get(0).getYVals()[3] + x)));
        ll.setTextColor(Color.LTGRAY);
        ll.setLineColor(Color.LTGRAY);
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(entries.get(0).getYVals()[3] / 2 + x, Zone.MODERATE.toString());
        ll.setLineColor(Color.TRANSPARENT);
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[3];
        ll = new LimitLine(entries.get(0).getYVals()[4] + x, String.valueOf((int) (entries.get(0).getYVals()[4] + x)));
        ll.setTextColor(Color.LTGRAY);
        ll.setLineColor(Color.LTGRAY);
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(entries.get(0).getYVals()[4] / 2 + x, Zone.HARD.toString());
        ll.setLineColor(Color.TRANSPARENT);
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[4];
        ll = new LimitLine(entries.get(0).getYVals()[5] + x, String.valueOf((int) (entries.get(0).getYVals()[5] + x)));
        ll.setTextColor(Color.LTGRAY);
        ll.setLineColor(Color.LTGRAY);
        chart.getAxisLeft().addLimitLine(ll);
        ll = new LimitLine(entries.get(0).getYVals()[5] / 2 + x, Zone.VERYHARD.toString());
        ll.setLineColor(Color.TRANSPARENT);
        chart.getAxisLeft().addLimitLine(ll);
        x += entries.get(0).getYVals()[5];
        if (pulse != null) {
            ll = new LimitLine(pulse.pulse, "Eigener Puls");
            ll.setLineColor(Color.RED);
            ll.setTextColor(Color.RED);
            chart.getAxisLeft().addLimitLine(ll);
        }
    }

    private BarData createBarData(BarDataSet dataSet) {
        BarData bardata = new BarData(dataSet);
        bardata.setBarWidth(0.5f);

        return bardata;
    }

    private void setStandardBarChartOptions(BarChart chart) {
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

    private void setStandardPieChartOptions(PieChart chart) {
        chart.getDescription().setEnabled(false);
        chart.setUsePercentValues(true);
        chart.getLegend().setEnabled(false);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setExtraLeftOffset(20f);
        chart.setExtraRightOffset(20f);
        chart.setExtraBottomOffset(20f);

        SpannableString zoneIcon = new SpannableString("XXX");
        Drawable d = ContextCompat.getDrawable(_context, R.drawable.ic_fitness_center_black_24dp);
        d.setBounds(0,0,d.getIntrinsicWidth() + 80, d.getIntrinsicHeight() + 80);
        ImageSpan span = new ImageSpan(d,ImageSpan.ALIGN_BASELINE);
        zoneIcon.setSpan(span, 0 , 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        chart.setCenterText(zoneIcon);
    }

    private List<BarEntry> getBarEntries(int maxPulse) {

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
        dataSet.setColors(getBarChartColours());
        dataSet.setStackLabels(getStackLabels());
        dataSet.setValueTextColor(Color.TRANSPARENT);

        return dataSet;
    }


}
