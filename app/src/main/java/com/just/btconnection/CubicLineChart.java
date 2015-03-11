package com.just.btconnection;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

/**
 * Created by tim on 15/3/11.
 */
public class CubicLineChart {

    public static void setChart(LineChart mChart, String description, float minY, float maxY) {
        // no description text
        mChart.setDescription(description);
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);

        //Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        //l.setTypeface(tf);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        //xl.setTypeface(tf);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        //xl.setDrawLabels(false);
        xl.setAvoidFirstLastClipping(true);

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(tf);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaxValue(maxY);
        leftAxis.setAxisMinValue(minY);
        leftAxis.setStartAtZero(false);
//        leftAxis.setSpaceTop(100f);
//        leftAxis.setSpaceBottom(100f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public static void addEntry(LineChart mChart, int mTime, float mData, String chartLabel, int lineColor, int circleColor) {

        LineData data = mChart.getData();

        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet(chartLabel, lineColor, circleColor);
                data.addDataSet(set);
            }

            //float mData = (float) (Math.random() * 100) + 1400f;
            //mTv.setText(String.valueOf(mData));

            // add a new x-value first
            data.addXValue(String.valueOf(mTime));
            data.addEntry(new Entry(mData, set.getEntryCount()), 0);

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRange(20);
            //mChart.setVisibleYRange(200, YAxis.AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getXValCount()-21);

            // this automatically refreshes the chart (calls invalidate())
//             mChart.moveViewTo(data.getXValCount()-7, 55f, AxisDependency.LEFT);

            // redraw the chart
//            mChart.invalidate();
        }
    }

    private static LineDataSet createSet(String chartLabel, int lineColor, int circleColor) {

        LineDataSet set = new LineDataSet(null, chartLabel);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        //line
        set.setColor(lineColor);
        set.setLineWidth(2f);
        //circle
        set.setCircleColor(circleColor);
        set.setCircleSize(4f);
        set.setDrawCircles(false);
        //fill
        set.setFillAlpha(65);
        set.setDrawFilled(true);
        set.setFillColor(ColorTemplate.getHoloBlue());
        //highlight
        set.setHighLightColor(Color.rgb(255, 255, 255));
        //value
        set.setValueTextColor(Color.WHITE);
        set.setDrawValues(false);
        //cubic
        set.setDrawCubic(true);
        set.setCubicIntensity(0.2f);

        return set;
    }
}
