package com.just.btconnection.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.just.btconnection.CubicLineChart;
import com.just.btconnection.R;


public class GraphFragment extends Fragment {

    public static int mTime = 0;
    public static LineChart mChart, mChart2, mChart3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mChart = (LineChart) getActivity().findViewById(R.id.chart1);
        mChart2 = (LineChart) getActivity().findViewById(R.id.chart11);
        mChart3 = (LineChart) getActivity().findViewById(R.id.chart111);


        CubicLineChart.setChart(mChart, "data of x", 1300f, 1600f);
        CubicLineChart.setChart(mChart2, "data of y", 1100f, 1400f);
        CubicLineChart.setChart(mChart3, "data of z", 1500f, 1800f);
    }

}
