package com.example.guitartuner.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.guitartuner.FTT.JavaFFT;
import com.example.guitartuner.R;
import com.example.guitartuner.Utilities.Calculation;
import com.example.guitartuner.VarGlob;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FreqFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FreqFragment extends Fragment {
    private final VarGlob VarG;
    FrequencyRealtimeUpdates Graph_frag;
    Run1 run;
    private boolean FOCUSOLD = false;
    private boolean PAUSEOLD = false;
    private boolean FOCUS = false;
    private boolean PAUSE = false;
    private AudioRecord ar;
    private boolean EXIT = false;
    private TextView FREQCAL,NOTECAL;

    public FreqFragment(VarGlob varG) {
        // Required empty public constructor
        VarG = varG;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param varG Parameter 1.
     * @return A new instance of fragment FreqFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FreqFragment newInstance(VarGlob varG) {
        FreqFragment fragment = new FreqFragment(varG);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        Log.d("CUST1", "Start FreqFrag");
        if (getArguments() != null) {

        }
        run = new Run1();
        run.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        Graph_frag = new FrequencyRealtimeUpdates();
        Graph_frag.FrequencyRealtimeUpdates(VarG);
        FragmentManager childFragMan = getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
        childFragTrans.add(R.id.fragFreqGraphCont, Graph_frag);
        childFragTrans.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_freq, container, false);
        super.onViewCreated(view, savedInstanceState);

        NOTECAL = (TextView) view.findViewById(R.id.NOTECAL);
        NOTECAL.setText("-");

        FREQCAL = (TextView) view.findViewById(R.id.FREQCAL);
        FREQCAL.setText("0.0");

        EXIT = false;
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //  Graph_frag.Start_graph();
    }


    @Override
    public void onResume() {
        super.onResume();
        PAUSE = false;
        Log.d("CUST1", "resume2: " + PAUSE);

    }

    @Override
    public void onPause() {
        PAUSE = true;
        Log.d("CUST1", "pause2: " + PAUSE);
        super.onPause();
    }

    public void stopGraphUI() {
        VarG.main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Graph_frag.Stop_graph();
            }
        });
    }

    public void startGraphUI() {
        VarG.main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Graph_frag.Start_graph();
            }
        });
    }

    public boolean isfocused() {
        boolean foc = false;

        View fragmentRootView = this.getView();
        if (fragmentRootView != null && fragmentRootView.getGlobalVisibleRect(new Rect())) {
            foc = true;
        }
        return foc;
    }


    class Run1 extends AsyncTask<Void, Void, String> {
        private final int[] index_corde = new int[2];

        boolean finish = false;
        int ColorBase = Color.GRAY;


        protected void onPreExecute() {
            Log.d("CUST1", "onPreExecute  FreqFrag");

            finish = false;
        }

        public boolean finish_task() {

            return finish;
        }


        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        protected String doInBackground(Void... voids) {

            Calculation calculation =new Calculation(VarG);
            Log.d("CUST1", "IN----------1");

            if (ActivityCompat.checkSelfPermission(VarG.main, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.d("CUST1", "OUT no permission");
                return null;
            }
            ar = new AudioRecord(MediaRecorder.AudioSource.MIC, VarG.F_acq, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, VarG.F_acq);

            AudioFormat format = ar.getFormat();

            byte[] buf = new byte[VarG.sampless]; // <--- increase this for higher frequency resolution
            final int numberOfSamples = buf.length / format.getFrameSizeInBytes();
            final int numberOfMag = (buf.length / (format.getFrameSizeInBytes() / 2));
            JavaFFT fft = new JavaFFT(numberOfSamples);

            int index = -1;

            FOCUS = isfocused();
            Log.d("CUST1", "Focus 2: " + FOCUS);
            while (!EXIT) {


                FOCUS = isfocused();

                if (FOCUS) {

                    index++;
                    if (FOCUS != FOCUSOLD) {


                        calculation=new Calculation(VarG);
                        startGraphUI();
                        ar.startRecording();
                        Log.d("CUST1", "Start Record 2");
                        index = 0;

                    }


                    //Begin reading
                    ar.read(buf, 0, buf.length);
                    float[] samples = fft.decode(buf, format, false);

                    float[][] transformed = fft.transform(samples);
                    float[] realPart = transformed[0];
                    float[] imaginaryPart = transformed[1];
                    double[] magnitudes = fft.toMagnitudes(realPart, imaginaryPart);
                    if (VarG.NB_lissage > 0) {
                        magnitudes = calculation.Lissage(magnitudes);
                    }
                    if (index > VarG.Nb_freq_affich - 1) {
                        index = 0;
                    }
                    Graph_frag.NewData(magnitudes, index);

                    if (!calculation.AnalyseNoiseDataReady() && VarG.NB_analyse_Noise > 0) {
////ANALYSE NOISE
                        calculation.AnalyseNoise(magnitudes);

                    } else {
////Code
                        if (VarG.NB_analyse_Noise > 0) {
                            magnitudes = calculation.AnalyseNoiseData(magnitudes);
                        }
                    }
                        calculation.FindMaxMag(magnitudes);
                        double[] temp2 = calculation.FindFreqListen();

                        String NoteFound=calculation.CloseNote(temp2[0]);

                        Log.d("CUST1", "Last_freq 2: " + temp2[0] + " - Countfreq_ecart_tot:" + temp2[1]);
                    Log.d("CUST1", "---------------------------------");
                        VarG.main.runOnUiThread(new Runnable() {
                            public void run() {
                                // runs on UI thread

                                FREQCAL.setText(VarG.df.format(temp2[0]));
                                NOTECAL.setText(NoteFound);

                            }
                        });




                    //    if (index > VarG.Nb_freq_affich - 1) {
                    //    index = 0;
                 //   }



                //    Graph_frag.NewData(magnitudes, index);


                } else {

                    if (FOCUS != FOCUSOLD) {
                        ar.stop();
                        stopGraphUI();


                        Log.d("CUST1", "Stop Record 2");
                        //Graph_frag.onStop();
                        //EXIT=true;

                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                FOCUSOLD = FOCUS;
                PAUSEOLD = PAUSE;

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("CUST1", "onPostExecute  FreqFrag");
            finish = true;

        }
    }
}