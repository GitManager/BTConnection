package com.just.btconnection;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;



public class ChartTestActivity extends ActionBarActivity implements OnChartValueSelectedListener {

    private Button mBtn,xBtn,yBtn,zBtn;
    private int mTime = 0;
    private LineChart mChart, mChart2, mChart3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_test);

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart2 = (LineChart) findViewById(R.id.chart11);
        mChart3 = (LineChart) findViewById(R.id.chart111);
        mChart.setOnChartValueSelectedListener(this);
        mChart2.setOnChartValueSelectedListener(this);
        mChart3.setOnChartValueSelectedListener(this);

        CubicLineChart.setChart(mChart, "description of the data of x", 1300f, 1600f);
        CubicLineChart.setChart(mChart2, "description of the data of y", 1100f, 1400f);
        CubicLineChart.setChart(mChart3, "description of the data of z", 1500f, 1800f);

        mBtn = (Button) findViewById(R.id.mBtn);
        mBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                mTime+=1;
                float mData = (float) (Math.random() * 100) + 1400f;
                float mData2 = (float) (Math.random() * 100) + 1200f;
                float mData3 = (float) (Math.random() * 100) + 1600f;
                CubicLineChart.addEntry(mChart,mTime,mData,"x-data",Color.rgb(0, 0, 255),ColorTemplate.getHoloBlue());
                CubicLineChart.addEntry(mChart2,mTime,mData2,"y-data",Color.rgb(255,0,0),ColorTemplate.getHoloBlue());
                CubicLineChart.addEntry(mChart3,mTime,mData3,"z-data",Color.rgb(0, 255, 0),ColorTemplate.getHoloBlue());
            }
        });

        xBtn = (Button) findViewById(R.id.xBtn);
        yBtn = (Button) findViewById(R.id.yBtn);
        zBtn = (Button) findViewById(R.id.zBtn);
        xBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChart.getVisibility()==View.VISIBLE){
                    mChart.setVisibility(View.INVISIBLE);
                }else {
                    mChart.setVisibility(View.VISIBLE);
                }
            }
        });
        yBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChart2.getVisibility()==View.VISIBLE){
                    mChart2.setVisibility(View.INVISIBLE);
                }else {
                    mChart2.setVisibility(View.VISIBLE);
                }
            }
        });
        zBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChart3.getVisibility()==View.VISIBLE){
                    mChart3.setVisibility(View.INVISIBLE);
                }else {
                    mChart3.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
