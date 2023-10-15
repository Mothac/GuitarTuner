package com.example.guitartuner.ui.main;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.guitartuner.R;
import com.example.guitartuner.VarGlob;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class FrequencyRealtimeUpdates extends Fragment {
    boolean Start_Graph = false;
    double mLastRandom = 2;
    Random mRand = new Random();
    private Handler mHandler = new Handler();
    private int Nb_serie = 1;
    private int count_serie = 0;
    private final int max_val = 3000;
    private boolean[] NewDataIN = new boolean[Nb_serie];
    private DataPoint[][] new_val = new DataPoint[Nb_serie][];

    private Runnable[] mTimer1 = new Runnable[Nb_serie];
    private LineGraphSeries<DataPoint>[] mSeries2 = new LineGraphSeries[Nb_serie];
    private double graph2LastXValue = 5d;
    private GraphView graph;
    private View rootView;
    private final DataPoint[] newzero = new DataPoint[]{new DataPoint(0, 0)};
    private VarGlob VarG;
    private DataPoint[] Lim_accordageP, Lim_accordageN;


    public void FrequencyRealtimeUpdates(VarGlob VarG) {
        this.VarG = VarG;
        Nb_serie = VarG.Nb_freq_affich;
        new_val = new DataPoint[Nb_serie][];

        mTimer1 = new Runnable[Nb_serie];
        NewDataIN = new boolean[Nb_serie];
        mSeries2 = new LineGraphSeries[Nb_serie];


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragfreqgraph, container, false);
        //super.onViewCreated(rootView, savedInstanceState);
        graph = (GraphView) rootView.findViewById(R.id.graph2);
        graph.setVisibility(View.INVISIBLE);

        graph.getViewport().setMinY(0.0);
        //graph.getViewport().setMinX(VarG.Limit_low_freq);
       // graph.getViewport().setMaxX(VarG.max_freq_analyse);
        //graph.getViewport().setXAxisBoundsManual(true);

        // activate horizontal zooming and scrolling
       graph.getViewport().setScalable(true);
// activate horizontal scrolling
graph.getViewport().setScrollable(true);

        graph.getGridLabelRenderer().setTextSize(10);
// activate horizontal and vertical zooming and scrolling
       // graph.getViewport().setScalableY(true);


// activate vertical scrolling
        //graph.getViewport().setScrollableY(true);
        //graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        //    graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        return rootView;
    }

    /*
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            // ((MainActivity) activity).onSectionAttached(
            // getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
        }
    */
    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {

        super.onPause();
    }

    public void Stop_graph() {


        for (int i = 0; i < mSeries2.length; i++) {
            mHandler.removeCallbacks(mTimer1[i]);
        }


        Start_Graph = false;
        graph2LastXValue = 0;
    }

    public void Start_graph() {
        graph.getViewport().setMinX(this.VarG.Limit_low_freq);
        graph.getViewport().setMaxX(this.VarG.max_freq_analyse/2.0);


        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        Start_Graph = true;

        graph.removeAllSeries();
        graph.clearAnimation();

        new_val = new DataPoint[Nb_serie][0];
        mTimer1 = new Runnable[Nb_serie];
        mSeries2 = new LineGraphSeries[Nb_serie];
        mHandler = new Handler();

        graph.setVisibility(View.VISIBLE);
        for (int i = 0; i < mSeries2.length; i++) {
            mSeries2[i] = new LineGraphSeries();


            //  mSeries2[i].resetData(newzero);


            graph.addSeries(mSeries2[i]);
        }
        mSeries2[0].setColor(Color.WHITE);


        Create_handler();

    }

    private void Create_handler() {
        count_serie = 0;
        for (int i = 0; i < mSeries2.length; i++) {
            mTimer1[i] = new Runnable() {
                private final int number1 = count_serie += 1;

                @Override
                public void run() {
                    int number = number1 - 1;
                    Addserie(number);
                    mHandler.postDelayed(this, 20);

                }

            };
            mHandler.postDelayed(mTimer1[i], 5);
        }

    }


    private void Addserie(int number) {
        if (NewDataIN[number] && new_val[number] != null && mSeries2[number] != null) {
            mSeries2[number].resetData(new_val[number]);
            NewDataIN[number] = false;
        }
    }

    public void NewData(double[] Data, int serie) {
        new_val[serie] = new DataPoint[Data.length];
        for (int i = 0; i < Data.length; i++) {
            new_val[serie][i] = new DataPoint(i * VarG.resolution, Data[i]);

        }

        NewDataIN[serie] = true;
    }


    private DataPoint[] generateData() {
        int count = 30;
        DataPoint[] values = new DataPoint[count];
        for (int i = 0; i < count; i++) {
            double x = i;
            double f = mRand.nextDouble() * 0.15 + 0.3;
            double y = Math.sin(i * f + 2) + mRand.nextDouble() * 0.3;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    private double getRandom() {
        return mLastRandom += mRand.nextDouble() * 0.5 - 0.25;
    }
}