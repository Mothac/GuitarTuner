package com.example.guitartuner.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.guitartuner.R;
import com.example.guitartuner.VarGlob;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private static final int FOCUSN = 1;
    // private VarGlob VarG=new VarGlob();
    private final VarGlob VarG;
    Button[] String_set;
    TextView[] FreqString;
    RadioGroup Tune;
    RadioGroup Octave;
    int sizeMess = 20;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //   private static final String ARG_PARAM1 = "param1";
    // private static final String ARG_PARAM2 = "param2";
    View.OnClickListener btnListStringSet = new View.OnClickListener() {
        @Override
        public void onClick(android.view.View view) {
            int Index_String = -1;
            for (int i = 0; i < String_set.length; i++) {
                if (view.getId() == String_set[i].getId()) {
                    Log.d("CUST1", "Button Name: " + String_set[i].getText() + "_s - Button Tune: " + view.getId());
                    Index_String = i;
                    break;
                }
            }


            Context context = view.getContext();
            LayoutInflater li = LayoutInflater.from(view.getContext());
            View promptsView = li.inflate(R.layout.prompttune, null);

            Tune = (RadioGroup) promptsView.findViewById(R.id.Tune);
            Octave = (RadioGroup) promptsView.findViewById(R.id.Octave);

            RadioGroup Test = new RadioGroup(VarG.main);


            // Create RadioButton programmatically
            RadioButton radioButton1 = new RadioButton(VarG.main);
            radioButton1.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            );
            radioButton1.setText("No");

            Test.addView(radioButton1, 0);


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);


            // set dialog message
            int finalIndex_String = Index_String;
            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    String OctaveS;
                                    String TuneS;
                                    TuneS = GetRadioString(Tune, promptsView);


                                    OctaveS = GetRadioString(Octave, promptsView);

                                    if (!TuneS.equals("null") && !OctaveS.equals("null")) {
                                        TuneS = TuneS.substring(0, TuneS.indexOf("/") - 1);
                                        String TuneOctave = TuneS + OctaveS;

                                        Log.d("CUST1", "Tune Selected: " + TuneOctave);


                                        if (VarG.Note_change(finalIndex_String, TuneOctave)) {
                                            String_set[finalIndex_String].setText(TuneOctave);
                                            VarG.savedata.SavePref(VarG.SaveData[finalIndex_String], TuneOctave);
                                            FreqString[finalIndex_String].setText(VarG.df.format(VarG.freq_guitar[finalIndex_String]));
                                        } else {
                                            String mess = "Tune or Octave not ok";
                                            GetMessage(context, mess);
                                            Log.d("CUST1", mess);
                                        }
                                    } else {

                                        String mess = "Tune or Octave not selected";
                                        GetMessage(context, mess);
                                        Log.d("CUST1", mess);
                                    }


                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    Log.d("CUST1", "No changes");

                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();


        }
    };
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment(VarGlob VarG) {
        // Required empty public constructor
        this.VarG = VarG;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param VarG Parameter 1.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(VarGlob VarG) {
        SettingsFragment fragment = new SettingsFragment(VarG);
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //  fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //   mParam1 = getArguments().getString(ARG_PARAM1);
            //  mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        super.onViewCreated(view, savedInstanceState);
        String_set = new Button[VarG.String_guitar.length];
        String_set[0] = (Button) view.findViewById(R.id.S1);
        String_set[1] = (Button) view.findViewById(R.id.S2);
        String_set[2] = (Button) view.findViewById(R.id.S3);
        String_set[3] = (Button) view.findViewById(R.id.S4);
        String_set[4] = (Button) view.findViewById(R.id.S5);
        String_set[5] = (Button) view.findViewById(R.id.S6);



        FreqString = new TextView[VarG.String_guitar.length];
        FreqString[0] = (TextView) view.findViewById(R.id.S1F);
        FreqString[1] = (TextView) view.findViewById(R.id.S2F);
        FreqString[2] = (TextView) view.findViewById(R.id.S3F);
        FreqString[3] = (TextView) view.findViewById(R.id.S4F);
        FreqString[4] = (TextView) view.findViewById(R.id.S5F);
        FreqString[5] = (TextView) view.findViewById(R.id.S6F);

        for (int i = 0; i < String_set.length; i++) {
            String_set[i].setOnClickListener(btnListStringSet);
            String Value = VarG.String_guitar[i];
            String_set[i].setText(Value);
            FreqString[i].setText(VarG.df.format(VarG.freq_guitar[i]));
        }


        Button FREQ;
        FREQ = (Button) view.findViewById(R.id.FREQ);


        String Value = String.valueOf(VarG.Frequency_ref);
        FREQ.setText(Value + " Hz");
        FREQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.promptfreqref, null);


                final TextView userInput = (TextView) promptsView
                        .findViewById(R.id.INPUT);
                userInput.setText("Reference frequency (440Hz / 432Hz)");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        TextView FREQREF = (EditText) promptsView
                                                .findViewById(R.id.FREQREF);

                                        String val = FREQREF.getText().toString();
                                        if (VarG.isNumeric(val)) {
                                            double vald = Double.valueOf(val);
                                            if (vald > 0.0) {
                                                VarG.init(vald);
                                                for (int i = 0; i < FreqString.length; i++) {

                                                    FreqString[i].setText(VarG.df.format(VarG.freq_guitar[i]));
                                                }

                                                FREQ.setText(val + " Hz");
                                                VarG.savedata.SavePref(VarG.SaveData[6], val);
                                                Log.d("CUST1", "Freq Ref Changed " + val);
                                            } else {
                                                String mess = "Value must be bigger than 0";
                                                GetMessage(context, mess);
                                                Log.d("CUST1", mess);
                                            }
                                        } else {
                                            String mess = "Freq Ref ,numerical value only";
                                            GetMessage(context, mess);
                                            Log.d("CUST1", mess);
                                        }

                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }
        });

        String messs = String.valueOf(VarG.NB_analyse_Noise);
        Button NBANALYSE;
        NBANALYSE = (Button) view.findViewById(R.id.NBANALYSE);
        NBANALYSE.setText(messs);
        NBANALYSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.promptfreqref, null);


                final TextView userInput = (TextView) promptsView
                        .findViewById(R.id.INPUT);
                userInput.setText("Number of analyse cycle:");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        TextView FREQREF = (EditText) promptsView
                                                .findViewById(R.id.FREQREF);

                                        String val = FREQREF.getText().toString();
                                        if (VarG.isNumeric(val)) {
                                            double vald = Double.valueOf(val);
                                            if (vald >= 0.0) {
                                                VarG.NB_analyse_Noise = (int) vald;
                                                NBANALYSE.setText(val);
                                                VarG.savedata.SavePref(VarG.SaveData[7], val);

                                                if(vald==0.0){
                                                    VarG.min_for_max_mag =  VarG.Finalmin_for_max_mag;// magnitude min for max
                                                    VarG.min_value =  VarG.Finalmin_value;// magnitude min for max

                                                }
                                                Log.d("CUST1", "NB ANALYSE Changed " + val);
                                            } else {
                                                String mess = "Value must be bigger than or equal to 0";
                                                GetMessage(context, mess);
                                                Log.d("CUST1", mess);
                                            }
                                        } else {
                                            String mess = "NB ANALYSE ,numerical value only";
                                            GetMessage(context, mess);
                                            Log.d("CUST1", mess);
                                        }

                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }
        });

        messs = String.valueOf(VarG.NB_lissage);
        Button NBSMOOTH;
        NBSMOOTH = (Button) view.findViewById(R.id.NBSMOOTH);
        NBSMOOTH.setText(messs);
        NBSMOOTH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.promptfreqref, null);


                final TextView userInput = (TextView) promptsView
                        .findViewById(R.id.INPUT);
                userInput.setText("Number of Smoothing:");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        TextView FREQREF = (EditText) promptsView
                                                .findViewById(R.id.FREQREF);

                                        String val = FREQREF.getText().toString();
                                        if (VarG.isNumeric(val)) {
                                            double vald = Double.valueOf(val);
                                            if (vald >= 0.0) {
                                                VarG.NB_lissage = (int) vald;
                                                NBSMOOTH.setText(val);
                                                VarG.savedata.SavePref(VarG.SaveData[8], val);
                                                Log.d("CUST1", "NB SMOOTH Changed " + val);
                                            } else {
                                                String mess = "Value must be bigger than or equal to 0";
                                                GetMessage(context, mess);
                                                Log.d("CUST1", mess);
                                            }
                                        } else {
                                            String mess = "NB SMOOTH ,numerical value only";
                                            GetMessage(context, mess);
                                            Log.d("CUST1", mess);
                                        }

                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }
        });



        //  messs=String.valueOf(VarG.Tunningcollection.CollectionTuning.get(0).Name);

        Button Tunning = (Button) view.findViewById(R.id.Tunning);

        //Tunning.setText(messs);

        Tunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Context context = view.getContext();
                LayoutInflater li = LayoutInflater.from(view.getContext());
                View promptsView = li.inflate(R.layout.fragtunning, null);


                RadioGroup RadioTunning = (RadioGroup) promptsView.findViewById(R.id.RadioTunning);


                for (int i = 0; i < VarG.Tunningcollection.CollectionTuning.size(); i++) {

                    // Create RadioButton programmatically
                    RadioButton radioButton1 = new RadioButton(context);
                    radioButton1.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    );
                    radioButton1.setText(VarG.Tunningcollection.CollectionTuning.get(i).Name + " / " + VarG.Tunningcollection.CollectionTuning.get(i).tostringTune());
                    radioButton1.setId(i);
                    RadioTunning.addView(radioButton1, i);
                }

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);


                // set dialog message

                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        int id1 = RadioTunning.getCheckedRadioButtonId();
                                        boolean OK1 = false;
                                        if (id1 >= 0) {
                                            OK1 = VarG.AllNote_change(VarG.Tunningcollection.CollectionTuning.get(id1).Tune);
                                            if (OK1) {
                                                for (int i = 0; i < VarG.String_guitar.length; i++) {
                                                    String Note = VarG.Tunningcollection.CollectionTuning.get(id1).Tune[i];
                                                    VarG.savedata.SavePref(VarG.SaveData[i], String.valueOf(Note));

                                                    String_set[i].setText(Note);
                                                    FreqString[i].setText(VarG.df.format(VarG.freq_guitar[i]));
                                                }
                                                Tunning.setText(VarG.Tunningcollection.CollectionTuning.get(id1).Name);
                                            } else {
                                                String mess = "Error";
                                                GetMessage(context, mess);
                                                Log.d("CUST1", mess);
                                            }
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        Log.d("CUST1", "No changes");

                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }
        });

        Log.d("CUST1", "Start SettingFrag");


        return view;
    }

    private void GetMessage(Context context, String message) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt_message, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        final TextView userInput = (TextView) promptsView
                .findViewById(R.id.INPUT);
        userInput.setTextSize(sizeMess);
        userInput.setText(message);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    private String GetRadioString(RadioGroup RG, View view) {
        int selectedId = RG.getCheckedRadioButtonId();
        String ret = "null";
        if (selectedId != -1) {
            // find the radiobutton by returned id
            RadioButton radioButton = (RadioButton) view.findViewById(selectedId);
            ret = (String) radioButton.getText();
        }
        return ret;
    }

}