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
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.guitartuner.FTT.Filtre;
import com.example.guitartuner.FTT.JavaFFT;
import com.example.guitartuner.R;
import com.example.guitartuner.Utilities.Calculation;
import com.example.guitartuner.VarGlob;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TunerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TunerFragment extends Fragment {
    private final VarGlob VarG;

    private final int maxCount = 0;

    RealtimeUpdates Graph_frag;
    Button[] String_tune;
    Button[] Display;

    TextView ERRORTXT;
    Run run;
    private boolean[] Forcage;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    // private String mParam1;
    // private String mParam2;
    View.OnClickListener btnListStringSet = new View.OnClickListener() {
        @Override
        public void onClick(android.view.View view) {
            int Index_String = -1;
            for (int i = 0; i < String_tune.length; i++) {
                if (view.getId() == String_tune[i].getId()) {
                    if (Forcage[i]) {

                        Forcage[i] = false;
                        String_tune[i].setBackgroundColor(Color.GRAY);
                    } else {
                        Forcage[i] = true;
                        String_tune[i].setBackgroundColor(Color.GREEN);
                    }
                    Log.d("CUST1", "Button Name: " + String_tune[i].getText() + "_s - Button Tune: " + view.getId());
                    Index_String = i;

                } else {
                    Forcage[i] = false;
                    String_tune[i].setBackgroundColor(Color.GRAY);

                }
            }


        }
    };
    private boolean FOCUSOLD = false;
    private boolean PAUSEOLD = false;
    private boolean FOCUS = false;
    private boolean PAUSE = false;
    private AudioRecord ar;
    private boolean EXIT = false;
    private double Errord = 0;

    public TunerFragment(VarGlob VarG) {
        // Required empty public constructor
        this.VarG = VarG;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param VarG Parameter 1.
     * @return A new instance of fragment TunerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TunerFragment newInstance(VarGlob VarG) {
        TunerFragment fragment = new TunerFragment(VarG);
        // Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, String.valueOf(UIG));
        //      fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            //    mParam1 = getArguments().getString(ARG_PARAM1);
            //     mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Graph_frag = new RealtimeUpdates();
        Graph_frag.RealtimeUpdatesInit(VarG);
        FragmentManager childFragMan = getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
        childFragTrans.add(R.id.FragGraphCont, Graph_frag);
        childFragTrans.commit();
        Task();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_tuner, container, false);
        super.onViewCreated(view, savedInstanceState);


        //
        ERRORTXT = view.findViewById(R.id.ERRORTXT);
        ERRORTXT.setTextSize((float) 20.0);
        ERRORTXT.setTextColor(Color.GRAY);
        ERRORTXT.setText("0.0");

        String_tune = new Button[VarG.String_guitar.length];
        Forcage = new boolean[VarG.String_guitar.length];
        String_tune[0] = view.findViewById(R.id.S1);
        String_tune[1] = view.findViewById(R.id.S2);
        String_tune[2] = view.findViewById(R.id.S3);
        String_tune[3] = view.findViewById(R.id.S4);
        String_tune[4] = view.findViewById(R.id.S5);
        String_tune[5] = view.findViewById(R.id.S6);

        for (int i = 0; i < String_tune.length; i++) {
            // String_tune[i].setEnabled(false);
            String_tune[i].setBackgroundColor(Color.GRAY);
            String_tune[i].setOnClickListener(btnListStringSet);
            String Value = VarG.String_guitar[i];
            String_tune[i].setText(Value);
        }

        Display = new Button[3];
        Display[0] = view.findViewById(R.id.TOOHIGH);
        Display[1] = view.findViewById(R.id.TUNEOK);
        Display[2] = view.findViewById(R.id.TOOLOW);

        for (int i = 0; i < Display.length; i++) {
            Display[i].setEnabled(false);
            Display[i].setBackgroundColor(Color.GRAY);

        }
        EXIT = false;


        Log.d("CUST1", "Start TunerFrag");


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


    }

    @Override
    public void onResume() {
        super.onResume();
        PAUSE = false;
        Log.d("CUST1", "resume: " + PAUSE);

    }

    @Override
    public void onPause() {
        PAUSE = true;
        Log.d("CUST1", "pause: " + PAUSE);
        super.onPause();
    }

    private void Task() {
        run = new Run();
        run.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public boolean isfocused() {
        boolean foc = false;

        View fragmentRootView = this.getView();
        if (fragmentRootView != null && fragmentRootView.getGlobalVisibleRect(new Rect())) {
            foc = true;
        }
        return foc;
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

    class Run extends AsyncTask<Void, Void, String> {
        private final int[] index_corde = new int[2];
        Filtre fil_e = new Filtre(VarG.fc_e, 1.0 / VarG.resolution);
        boolean finish = false;
        int ColorBase = Color.GRAY;
        boolean[] tt = new boolean[VarG.String_guitar.length];
        int in = -1;
        private double Last_freq = 0;
        private int CountSame = 0;
        private double error_f = 0;

        protected void onPreExecute() {
            Log.d("CUST1", "onPreExecute  TunerFrag");

            finish = false;
        }

        public boolean finish_task() {

            return finish;
        }


        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        protected String doInBackground(Void... voids) {

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

            long Pos_error_g = -1;

            int Countfreq_ecart_tot = 0;
            boolean testForcage = false;
            int count_nb_ok = 0;

            Calculation Calculation = new Calculation(VarG);

            while (!EXIT) {


                FOCUS = isfocused();
                //     AudioFormat format = AudioFormat.ENCODING_PCM_16BIT, VarG.F_acq, 16, 1, 2, VarG.F_acq, false);
                if (FOCUS) {
                    Pos_error_g++;


                    if (FOCUS != FOCUSOLD) {
                        Log.d("CUST1", "FOCUS Tuner: " + FOCUS);
                        Calculation = new Calculation(VarG);
                        Pos_error_g = 0;
                        Log.d("CUST1", "Resolution: " + VarG.resolution);
                        startGraphUI();

                        ar.startRecording();

                        Log.d("CUST1", "Start Recording");

                        if (VarG.refresh_button_name) {
                            VarG.refresh_button_name = false;
                            Log.d("CUST1", "Tune refresh");

                            for (int i = 0; i < String_tune.length; i++) {
                                String_tune[i].setText(VarG.String_guitar[i]);
                            }
                        }
                    }

                    //Begin reading
                    ar.read(buf, 0, buf.length);
                    float[] samples = fft.decode(buf, format, false);

                    float[][] transformed = fft.transform(samples);
                    float[] realPart = transformed[0];
                    float[] imaginaryPart = transformed[1];
                    double[] magnitudes = fft.toMagnitudes(realPart, imaginaryPart);

                    //LISSAGE


                    if (VarG.NB_lissage > 0) {
                        magnitudes = Calculation.Lissage(magnitudes);
                    }

                    index_corde[0] = -1;
                    testForcage = false;
                    Countfreq_ecart_tot = 0;


                    if (!Calculation.AnalyseNoiseDataReady() && VarG.NB_analyse_Noise > 0) {
////ANALYSE NOISE
                        Calculation.AnalyseNoise(magnitudes);

                    } else {
////Code
                        if (VarG.NB_analyse_Noise > 0) {
                            magnitudes = Calculation.AnalyseNoiseData(magnitudes);
                        }
int indForce=-1;
                        for (int k1 = 0; k1 < Forcage.length; k1++) {
                            if (Forcage[k1]) {
                                testForcage = true;
                                indForce = k1;
                                break;
                            }
                        }
                        if (Calculation.FindMaxMag(magnitudes)) {
                                double[] temp = Calculation.FindFreq(indForce);
                                Last_freq = temp[0];
                                index_corde[0] = (int) temp[1];
                               // CountSame = (int) temp[2];

                        }

                    }



                    if ((index_corde[0] == index_corde[1]) && index_corde[0] >= 0 &&  Last_freq>0.0) {

                        CountSame++;
                    } else {
                        //fil_e.new_value(0.0);
                        // fil_e.new_value(0.0);
                        CountSame = 0;
                    }
                    //System.out.println("--------------" + CountSame + "-----MaxIndex: " + index_corde[0]);
                    String mess = "--------------" + CountSame + "-----MaxIndex: " + index_corde[0];
                    //Log.d("CUST1", mess);

                        if (CountSame >= 2) {
                            Errord = Last_freq - VarG.freq_guitar[index_corde[0]];
                            //     Log.d("CUST1", "Index Corde: " + index_corde[0] + " - Error: " + Errord);

                            if (VarG.fc_e > 0) {

                                if (CountSame == 2) {
                                    fil_e.new_value(Errord);
                                    fil_e.new_value(Errord);
                                }

                                error_f = (float) fil_e.new_value(Errord);

                            } else {
                                error_f = Errord;
                                //     Log.d("CUST1", "Index Corde: " + index_corde[0] + " - Error: " + error_f);
                            }

                            if (testForcage) {

                                if (Math.abs(error_f) < VarG.Ecart_Acrodage * 2) {
                                    count_nb_ok++;
                                    if (count_nb_ok >= 5) {
                                        //ERRORTXT.setBackgroundColor(Color.GREEN);
                                        for (int k1 = 0; k1 < Forcage.length; k1++) {
                                            if (Forcage[k1]) {

                                                Forcage[k1] = false;
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    count_nb_ok = 0;
                                }
                            }else {

                                for (int j2 = 0; j2 < String_tune.length; j2++) {
                                    if (j2 == index_corde[0]) {
                                        String_tune[j2].setBackgroundColor(Color.GREEN);
                                    } else {
                                        String_tune[j2].setBackgroundColor(ColorBase);
                                    }
                                }
                            }
                            if (error_f > VarG.Ecart_Acrodage) {
                                ERRORTXT.setTextColor(Color.RED);
                                Display[0].setBackgroundColor(Color.RED);
                                Display[1].setBackgroundColor(ColorBase);
                                Display[2].setBackgroundColor(ColorBase);
                            } else if (error_f < -VarG.Ecart_Acrodage) {
                                ERRORTXT.setTextColor(Color.RED);
                                Display[0].setBackgroundColor(ColorBase);
                                Display[1].setBackgroundColor(ColorBase);
                                Display[2].setBackgroundColor(Color.RED);

                            } else {
                                ERRORTXT.setTextColor(Color.GREEN);
                                Display[0].setBackgroundColor(ColorBase);
                                Display[1].setBackgroundColor(Color.GREEN);
                                Display[2].setBackgroundColor(ColorBase);
                            }

                        } else {
                            ERRORTXT.setTextColor(ColorBase);
                            // Error.setBackground(ColorBase);
                            Display[1].setBackgroundColor(ColorBase);
                            for (int j2 = 0; j2 < String_tune.length; j2++) {

                               if( !Forcage[j2]) {
                                   String_tune[j2].setBackgroundColor(ColorBase);
                               }
                            }

                        }


                    VarG.main.runOnUiThread(new Runnable() {
                        public void run() {
                            // runs on UI thread

                            ERRORTXT.setText(VarG.df.format(error_f));


                        }
                    });

                    double errorDisplay = error_f;
                    if (errorDisplay > 5) {
                        errorDisplay = 5;

                    } else if (errorDisplay < -5) {
                        errorDisplay = -5;
                    }

                    Graph_frag.NewData(new double[]{errorDisplay, Pos_error_g + 30}, 0);
                    Graph_frag.NewData(new double[]{VarG.Ecart_Acrodage, Pos_error_g + 30}, 1);
                    Graph_frag.NewData(new double[]{-VarG.Ecart_Acrodage, Pos_error_g + 30}, 2);


                    index_corde[1] = index_corde[0];


                } else {

                    if (FOCUS != FOCUSOLD) {
                        ar.stop();
                        stopGraphUI();
                        // EXIT=true;

                        Log.d("CUST1", "Stop Record");
                        //Graph_frag.onStop();

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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

            //finish = true;

            //return null;
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("CUST1", "onPostExecute  TunerFrag");
            finish = true;

        }


    }


}