package com.example.guitartuner;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.guitartuner.ui.main.Tune.NOTE;
import com.example.guitartuner.ui.main.Tune.TunningCollection;
import com.example.guitartuner.Utilities.SaveData;

import java.text.DecimalFormat;

public class VarGlob {

    final public int F_acq = 44100;
    final public int sampless = 16 * 1024;
    final public double Ratio_mean = 1.0;
    final public double Mult_min_for_max = 3.0;
    final public double Mult_min = 1.2;
    public int NB_lissage = 2;

    ///////Else
    final public double Ecart_div = 0.1;
    final public int Validation_number = 1;
    final public double Ecart_Acrodage = 0.75;

    //Set After analyse noise
    public int NB_analyse_Noise = 0;
    final public double Finalmin_for_max_mag = 2250.0;// magnitude min for max
    final public double Finalmin_value = 400.0;// magnitude min for max
    public double min_for_max_mag = Finalmin_for_max_mag;// magnitude min for max
    public double min_value = Finalmin_value;// magnitude min for max


//settings
    final public double Max_mult = 10.0;//Max multiple accepted
    final public int Nb_freq_affich = 1;// sample maxx for max mag.
    final public double max_freq_analyse = 2000.0;
    final public double Limit_low_freq = 35.0;
    final public int freq_beforeAfter = 22;//Freq to check for max
    //filtre
    final public double fc_e = 0.25;// freq de coupure filtre error
    final public double fc_s = 1500.0;// freq de coupure filtre audio brut
    final public double resolution =2.0* (F_acq*1.0) / (sampless*1.0 );
    final public int max_sample_analyse = (int) (max_freq_analyse / resolution);
    final public int min_sample_analyse = (int) (Limit_low_freq / resolution);
    final public int Test_before_after = (int) (freq_beforeAfter / resolution);
    ///OLD
    final double tol_ecart = 10.0;//+/-
    final double tol_freq = 15.0;//+/-
    public SaveData savedata;
    /////Echange Android
    public AppCompatActivity main;
    public Context mainCont;
    public String[] SaveData = new String[]{"S1", "S2", "S3", "S4", "S5", "S6", "FreqRef","NBANALYSE","NBSMOOTH"};
    public boolean refresh_button_name = false;
    public DecimalFormat df = new DecimalFormat("#.##");


    public double Frequency_ref = 440.0; // 432 HZ trick=0.982
    public NOTE Note_F;
//Tone Sound=new Tone((float)F_acq);
    public TunningCollection Tunningcollection = new TunningCollection();


//final String[] String_guitar= {"C2","C#2","D2","D#2","E2","F2","F#2","G2","G#2","A2","A#2","B2"};
    //standard
    public String[] String_guitar = Tunningcollection.CollectionTuning.get(0).Tune.clone();
    final public double[] freq_guitar = new double[String_guitar.length];
    final public double[] freq_ecart_guit = new double[String_guitar.length];

   public double[] freqoctave =new double[20];



    public VarGlob() {
        init(Frequency_ref);
    }


    public VarGlob(double freqRef) {

        init(freqRef);
    }

    public void Setmain(AppCompatActivity mainc) {
        main = mainc;
        savedata = new SaveData(main);

        String Value = savedata.LoadPref(SaveData[6], String.valueOf(Frequency_ref));
        Frequency_ref = Double.valueOf(Value);

        Value = savedata.LoadPref(SaveData[7], String.valueOf(NB_analyse_Noise));
        NB_analyse_Noise = Integer.valueOf(Value);

        Value = savedata.LoadPref(SaveData[8], String.valueOf(NB_lissage));
        NB_lissage = Integer.valueOf(Value);


        for (int i = 0; i < String_guitar.length; i++) {

            String Value1 = savedata.LoadPref(SaveData[i], String.valueOf(String_guitar[i]));
            String_guitar[i] = Value1;
        }

        init(Frequency_ref);


    }

    public void init(double freqRef) {

        Frequency_ref = freqRef;
        Note_F = new NOTE(freqRef);
        for (int l = 0; l < String_guitar.length; l++) {
            freq_guitar[l] = Note_F.NOTE_Otave(String_guitar[l]);
            Log.d("CUST1", "Note :" + String_guitar[l] + " - Freq :" + df.format(freq_guitar[l]));
            System.out.println("Note :" + String_guitar[l] + " - Freq :" + df.format(freq_guitar[l]));
        }

        for (int l = 0; l < String_guitar.length; l++) {
            freq_ecart_guit[l] = freq_guitar[l] * 2.0;
        }

        for (int i = 0; i < freqoctave.length; i++) {
            freqoctave[i]=Note_F.NOTE_Otave(Note_F.Note[0], i);
        }


    }

    public boolean AllNote_change(String[] note) {
        boolean testOK = false;
        for (int i = 0; i < note.length; i++) {
            refresh_button_name = true;

            if (Note_F.IndexNote2(note[i]) != -1) {
                String_guitar[i] = note[i];

                testOK = true;
            } else {
                testOK = false;
                break;
            }

        }

        if (testOK) {
            init(Frequency_ref);
        }
        return testOK;
    }

    public boolean Note_change(int String, String note) {

        refresh_button_name = true;
        if (Note_F.IndexNote2(note) != -1) {
            String_guitar[String] = note;
            init(Frequency_ref);
            return true;
        }
        return false;
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }




}
