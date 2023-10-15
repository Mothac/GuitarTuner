package com.example.guitartuner.Utilities;

import static java.lang.Math.abs;

import android.util.Log;

import com.example.guitartuner.VarGlob;

import java.util.ArrayList;
import java.util.Collections;

public class Calculation {


    private ArrayList<double[]> LissMag = new ArrayList<double[]>();
    private ArrayList<double[]> AnalyseNoiseMag = new ArrayList<double[]>();
    private ArrayList<Double> freq_max = new ArrayList<Double>();
    private ArrayList<Double> maxMag = new ArrayList<Double>();
    private  ArrayList<Double> freq_sort = new ArrayList<Double>();
    private VarGlob VarG;

    double[] magnitudesMeanAnalyse;
    boolean DataReady = false;

    public Calculation(VarGlob VarG) {
        DataReady=false;
        LissMag = new ArrayList<double[]>();
        AnalyseNoiseMag = new ArrayList<double[]>();
        this.VarG = VarG;

    }

    public Calculation() {
        DataReady=false;
        LissMag = new ArrayList<double[]>();
        AnalyseNoiseMag = new ArrayList<double[]>();
    }

    //////////////-----------------------------------------------------------------------------------FIND Freq Listening

    public double[] FindFreqListen() {


        double Last_freq = 0;
        boolean[] bool0 = new boolean[freq_sort.size()];
        boolean[] bool05 = new boolean[freq_sort.size()];
        double div = 0;
        int Countfreq_ecart = 0;
        int Countfreq_ecart_tot = 0;
        double freq_ecart = 0;
       if (freq_sort.size()>1) {
            for (int k = 0; k < freq_sort.size(); k++) {
                if (freq_sort.get(k) > 0) {

                    div = freq_sort.get(k) / freq_max.get(0);

                    double ecart = div - Math.round(div);

                    if (abs(ecart) < VarG.Ecart_div) {
                        bool0[k] = true;
                        Countfreq_ecart++;
                        Countfreq_ecart_tot++;
                    } else if (abs(ecart + 0.5) < VarG.Ecart_div) {
                       Countfreq_ecart--;
                        Countfreq_ecart_tot++;
                        bool05[k] = true;
                    }

                }
            }

        if (Countfreq_ecart_tot > 1) {

                freq_ecart = freq_max.get(0);


            // Log.d("CUST1", "freq_ecart: " + freq_ecart + "Count: " + Countfreq_ecart);

            double[] Result_div = new double[freq_sort.size()];
            for (int k = 0; k < freq_sort.size(); k++) {
                if (freq_sort.get(k) > 0.0 && (bool0[k] || bool05[k])) {
if(bool0[k]) {
    div = freq_sort.get(k) / freq_ecart;
    Result_div[k] = freq_sort.get(k) / Math.round(div);
}else if( bool05[k]){
    div = freq_sort.get(k) / (freq_ecart/2.0);
    if(Math.round(div)>0.0) {
        Result_div[k] = 2.0 * (freq_sort.get(k) / Math.round(div));
    }else{
        Result_div[k] = 2.0 * freq_sort.get(k) /1.0;
    }
}
                }
            }

            int nb_s = 0;
          double tot = 0;
            for (int k = 0; k < Result_div.length; k++) {
                if (Result_div[k] > 0) {

                    nb_s++;
                    tot = tot + Result_div[k];
                }
            }



            double means = tot / nb_s;
            //System.out.println(" Nb: " + nb_s);

            Last_freq = means;


            //Log.d("CUST1", "Last Freq: " + Last_freq);

        }else{

            Last_freq= freq_max.get(0);

        }
    }else{
            if(freq_max.size()>0) {
                Last_freq = freq_max.get(0);

            }else{
                Last_freq=0.0;

            }
    }
        System.out.println(" Freq: " + Last_freq);
        return new double[]{Last_freq, Countfreq_ecart_tot};
    }

    //////////////-----------------------------------------------------------------------------------FIND Define Freq


    public double[] FindFreq(int indforce) {

        double Last_freq = -1;
        double[] temp = FindFreqListen();
        Last_freq = temp[0];
        int maxCount = 0;
        int index_corde = -1;

        if (Last_freq > 0.0) {

       boolean[] div2=new boolean[VarG.freq_ecart_guit.length];
        int[] Count_divOK = new int[VarG.freq_ecart_guit.length];
        double[] Rest = new double[VarG.freq_ecart_guit.length];
        boolean[][] BoolTab = new boolean[VarG.freq_ecart_guit.length][freq_sort.size()];


        for (int q = VarG.freq_ecart_guit.length - 1; q >= 0; q--) {

            if (indforce > -1) {

                q = indforce;
            }

            boolean too_low = false;

            double div;
            double reste = 0;
            boolean first_pass = false;
            boolean too_high = false;
            boolean too_high_half = false;
            boolean too_low_half = false;
            //for (int k = 0; k < freq_sort.size(); k++) {




                if (Last_freq > VarG.freq_ecart_guit[q] * (1 + VarG.Ecart_div)) {
                    too_high_half = true;
                }
                if (Last_freq > VarG.freq_guitar[q] * (1 + VarG.Ecart_div)) {
                    too_high = true;
                }
                if (Last_freq < (VarG.freq_ecart_guit[q]) * (1 - VarG.Ecart_div)) {
                    too_low_half = true;
                }
                if (Last_freq < (VarG.freq_guitar[q]) * (1 - VarG.Ecart_div)) {
                    too_low = true;
                }


                if ((!too_high && !too_low) || (!too_high_half && !too_low_half)) {
                    if ((!too_high_half && !too_low_half)) {
                        div2[q] = true;
                    }
                    div = Last_freq / VarG.freq_guitar[q];
                    if ((first_pass) || div < (VarG.Max_mult + VarG.Ecart_div)) {

                        reste = div - Math.round(div);

                        if (abs(reste) < VarG.Ecart_div) {
                            BoolTab[q][0] = true;
                            first_pass = true;
                            Count_divOK[q]++;
                            Rest[q] = Rest[q] + abs(reste);
                            // System.out.println("Div OK: "+div+" Corde:"+VarG.String_guitar[q]+" CordeF:"+VarG.freq_ecart_guit[q] + " Freq: "+freq_sort.get(k));
                            String mess = "Div OK: " + div + " Corde:" + VarG.String_guitar[q] + " CordeF:" + VarG.freq_ecart_guit[q] + " Freq: " + Last_freq;
                            //  Log.d("CUST1", mess);
                        }
                    }

                }

                //  }
                if (Count_divOK[q] > 0) {


                    Rest[q] = Rest[q] / Count_divOK[q];
                }
                if (indforce > -1) {
                    break;
                }

            }
            if (indforce == -1) {
                int maxIndex1 = 0;

                for (int i = Count_divOK.length - 1; i >= 0; i--) {
                    if ((Count_divOK[i] > Count_divOK[maxIndex1] || (Count_divOK[i] == Count_divOK[maxIndex1] && Rest[i] <= Rest[maxIndex1])) && Count_divOK[i] >= VarG.Validation_number) {
                        maxIndex1 = i;
                        index_corde = i;
                        maxCount = Count_divOK[i];
                    }
                }
            }else{
                index_corde = indforce;
            }

            if (index_corde > -1) {
                if (div2[index_corde]) {
                    double div = Last_freq / VarG.freq_guitar[index_corde];
                    Last_freq = Last_freq / Math.round(div);
                }
            }
        }





        return new double[]{Last_freq, index_corde, maxCount};
    }

    //////////////-----------------------------------------------------------------------------------FIND MAX MAG

    public boolean FindMaxMag(double[] magnitudes) {

        ArrayList<Integer> maxIndex = new ArrayList<Integer>();
        maxMag = new ArrayList<Double>();
        maxIndex.add(0);
        maxMag.add(0.0);
        freq_max = new ArrayList<Double>();
        freq_sort = new ArrayList<Double>();

        for (int i = VarG.min_sample_analyse; i < VarG.max_sample_analyse; i++) {

        if (magnitudes[i] > VarG.min_value && magnitudes[i] > maxMag.get(0)/10.0) {

                boolean ok = true;
                for (int l = 1; l <= VarG.Test_before_after; l++) {

                    int indmin=i-l;
                    if(indmin<0){
                        indmin=0;
                    }

                    int indplus=i+l;
                    if(indplus>magnitudes.length){
                        indplus=magnitudes.length-1;
                    }

                    if (magnitudes[i] < magnitudes[indplus] || magnitudes[i] < magnitudes[indmin]) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    for (int k = 0; k < maxIndex.size(); k++) {

                        if (magnitudes[i] > maxMag.get(k)) {

                            maxIndex.add(k, i);
                            maxMag.add(k, magnitudes[i]);
                            //  System.out.println("k: "+k+" i: "+i+" Mag: "+magnitudes[i])  ;
                            String mess = "k: " + k + " i: " + i + " Mag: " + magnitudes[i];
                            //Log.d("CUST1", mess);
                            break;
                        }


                    }
                }

            }
        }

        maxIndex.remove(maxIndex.size() - 1);
        maxMag.remove(maxMag.size() - 1);
        for (int jj = 0; jj < maxIndex.size(); jj++) {
            if(maxMag.get(jj)<maxMag.get(0)/10.0){

                maxMag.remove(jj);
                maxIndex.remove(jj);
            }
        }


        if (maxIndex.size() > 0 && maxMag.get(0)>VarG.min_for_max_mag) {
            //FoundMax=true;
            // double[] freq_max= new double[maxIndex.length];

            for (int k = 0; k < maxIndex.size(); k++) {
                freq_max.add(maxIndex.get(k) * VarG.resolution);
                String mess = k + " - Freq = " + freq_max.get(k)+" - Mag max= " + maxMag.get(k);
               // Log.d("CUST1", mess);
            }




            freq_sort = (ArrayList<Double>)freq_max.clone();
            Collections.sort(freq_sort);


            // double[] freq_sort= freq_max;

            System.out.println();
            for (int i = 0; i < freq_sort.size(); i++) {
               // System.out.println(freq_sort.get(i));
            }
        }else{
            freq_sort = new ArrayList<Double>();
        }
        boolean FoundMax = false;
        if(maxMag.size()>0) {
            if (maxMag.get(0) > VarG.min_for_max_mag) {
                FoundMax = true;
            }
        }
        return FoundMax;
    }

    //////////////-----------------------------------------------------------------------------------ANALYSE NOISE
    public double[] AnalyseNoiseData(double[] magnitudes) {

        if (VarG.NB_analyse_Noise > 0) {
            for (int i = 0; i < magnitudes.length; i++) {
                magnitudes[i] = magnitudes[i] - magnitudesMeanAnalyse[i];

                if (magnitudes[i] < 0.0) {
                    magnitudes[i] = 0.0;
                }

            }
        }
        return magnitudes;
    }

    public boolean AnalyseNoiseDataReady() {
        return DataReady;
    }

    public void AnalyseNoiseDataReset() {
        AnalyseNoiseMag = new ArrayList<double[]>();
    }

    public boolean AnalyseNoise(double[] magnitudes) {

        AnalyseNoiseMag.add(magnitudes);

        if (AnalyseNoiseMag.size() > VarG.NB_analyse_Noise) {

            AnalyseNoiseMag.remove(0);
        }


        double[] magnitudes_Mean = new double[magnitudes.length];


        if (AnalyseNoiseMag.size() == VarG.NB_analyse_Noise - 1) {
            System.out.println("Start");

            for (int j = 0; j < AnalyseNoiseMag.size(); j++) {
                for (int i = 0; i < magnitudes.length; i++) {
                    magnitudes_Mean[i] = magnitudes_Mean[i] + AnalyseNoiseMag.get(j)[i];
                }
            }

            for (int i = 0; i < magnitudes_Mean.length; i++) {
                magnitudes_Mean[i] = VarG.Ratio_mean * (magnitudes_Mean[i] / VarG.NB_analyse_Noise);
            }
double summ=0;
            for (int i = 0; i < magnitudes_Mean.length; i++) {
                summ= summ+magnitudes_Mean[i];
            }
            summ=summ/magnitudes_Mean.length;
            double max_mean = arrayMax(magnitudes_Mean, VarG.min_sample_analyse, VarG.max_sample_analyse);

            magnitudesMeanAnalyse = magnitudes_Mean;
            VarG.min_for_max_mag = summ * VarG.Mult_min_for_max;
            VarG.min_value = summ * VarG.Mult_min;

            System.out.println("Max_Mean= " + summ + " - Min_Max= " + VarG.min_for_max_mag + " - Min_Val= " + VarG.min_value);
            Log.d("CUST1", "Max_Mean= " + summ + " - Min_Max= " + VarG.min_for_max_mag + " - Min_Val= " + VarG.min_value);

            DataReady = true;
        } else {
            DataReady = false;
        }
        return DataReady;

    }

    public double arrayMax(double[] arr, int minA, int maxA) {
        double max = Double.NEGATIVE_INFINITY;

        for (int i = minA; i < maxA; i++) {
            max = Math.max(max, arr[i]);
        }
        return max;
    }

    //////////////-----------------------------------------------------------------------------------LISSAGE
    public double[] Lissage(double[] magnitudes) {


        LissMag.add(magnitudes);

        if (LissMag.size() > VarG.NB_lissage) {

            LissMag.remove(0);
        }


        double[] magnitudes_mean_liss = new double[magnitudes.length];
        for (int j = 0; j < LissMag.size(); j++) {
            for (int i = 0; i < magnitudes_mean_liss.length; i++) {


                magnitudes_mean_liss[i] = magnitudes_mean_liss[i] + LissMag.get(j)[i];
            }
        }
        for (int i = 0; i < magnitudes.length; i++) {
            magnitudes[i] = magnitudes_mean_liss[i] / (double) LissMag.size();
        }
        return magnitudes;
    }


    public String CloseNote(double FreqCal) {
String ANSW="NULL";
        int octave=-10;
        for (int i = 0; i <  VarG.freqoctave.length-1; i++) {
                  if(FreqCal>VarG.freqoctave[i] && FreqCal<VarG.freqoctave[i+1]) {

                octave=i;
                break;
            }

        }
        if(octave>-10) {

            for (int i = 0; i < VarG.Note_F.Note.length - 1; i++) {


                double freqNote = VarG.Note_F.NOTE_Otave(VarG.Note_F.Note[i], octave);
                double freqNote2 = VarG.Note_F.NOTE_Otave(VarG.Note_F.Note[i + 1], octave);

                if (FreqCal > freqNote && FreqCal < freqNote2) {

                    double diff1 = FreqCal - freqNote;
                    double diff2 =FreqCal- freqNote2 ;

                    if (abs(diff1) <= abs(diff2)) {

                        ANSW = VarG.Note_F.Note[i] + octave + " - Diff: " + VarG.df.format(diff1);

                    } else {
                        ANSW = VarG.Note_F.Note[i + 1] + octave + " - Diff: " + VarG.df.format(diff2);
                    }

                }


            }

        }else{
            ANSW="NOT FOUND";
        }


        return ANSW;
    }




}
