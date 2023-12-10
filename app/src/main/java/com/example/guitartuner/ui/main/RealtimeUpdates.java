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

public class RealtimeUpdates extends Fragment {
    boolean Start_Graph = false;
    double mLastRandom = 2;
    Random mRand = new Random();
    private Handler mHandler = new Handler();
    private final int Nb_serie = 3;
    private int count_serie = 0;
    private final int max_val = 30;
    private double[][] new_val = new double[Nb_serie][2];
    private double[][] old_val = new double[Nb_serie][2];
    private Runnable[] mTimer1 = new Runnable[Nb_serie];
    private LineGraphSeries<DataPoint>[] mSeries2 = new LineGraphSeries[Nb_serie];
    private double graph2LastXValue = 5d;
    private GraphView graph;
    private View rootView;
    private final DataPoint[] newzero = new DataPoint[]{new DataPoint(0, 0)};
    private VarGlob VarG;
    private DataPoint[] Lim_accordageP, Lim_accordageN;


    public void RealtimeUpdatesInit(VarGlob VarG) {
        this.VarG = VarG;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fraggraph, container, false);
        //super.onViewCreated(rootView, savedInstanceState);
        graph = (GraphView) rootView.findViewById(R.id.graph);
        graph.setVisibility(View.INVISIBLE);

        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(max_val);
        graph.getViewport().setMinY(-5);
        graph.getViewport().setMaxY(5);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        //graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
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

    public void NewData(double[] Data, int serie) {

        new_val[serie] = Data;
    }

    public void Stop_graph() {


        for (int i = 0; i < mSeries2.length; i++) {
            mHandler.removeCallbacks(mTimer1[i]);
        }


        Start_Graph = false;
        graph2LastXValue = 0;
    }

    public void Start_graph() {
        Start_Graph = true;
        Lim_accordageP = new DataPoint[]{new DataPoint(0, this.VarG.Ecart_Acrodage)};
        Lim_accordageN = new DataPoint[]{new DataPoint(0, -this.VarG.Ecart_Acrodage)};
        graph.removeAllSeries();
        graph.clearAnimation();

        new_val = new double[Nb_serie][2];
        old_val = new double[Nb_serie][2];
        mTimer1 = new Runnable[Nb_serie];
        mSeries2 = new LineGraphSeries[Nb_serie];
        mHandler = new Handler();

        graph.setVisibility(View.VISIBLE);
        for (int i = 0; i < mSeries2.length; i++) {
            mSeries2[i] = new LineGraphSeries();

            if (i == 1) {


                mSeries2[i].resetData(Lim_accordageP);
            } else if (i == 2) {
                mSeries2[i].resetData(Lim_accordageN);
            } else {

                mSeries2[i].resetData(newzero);
            }

            graph.addSeries(mSeries2[i]);
        }
        mSeries2[0].setColor(Color.GRAY);
        mSeries2[1].setColor(Color.RED);
        mSeries2[2].setColor(Color.RED);


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
        if (old_val[number][1] != new_val[number][1]) {
            mSeries2[number].appendData(new DataPoint(new_val[number][1], new_val[number][0]), true, max_val);
            old_val[number] = new_val[number];
        }
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