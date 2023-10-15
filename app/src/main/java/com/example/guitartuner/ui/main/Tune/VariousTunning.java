package com.example.guitartuner.ui.main.Tune;

public class VariousTunning {


    public String Name = "Standard";
    public String[] Tune = new String[]{"E2", "A2", "D3", "G3", "B3", "E4"};


    public VariousTunning(String Namei, String[] Tunei) {

        Name = Namei;
        Tune = Tunei;
    }

    public VariousTunning() {


    }

    public String tostringTune() {
        String ret = null;

        for (int i = 0; i < Tune.length; i++) {
            if (i == 0) {
                ret = Tune[i] + ", ";
            } else if (i == Tune.length - 1) {
                ret = ret + Tune[i];

            } else {
                ret = ret + Tune[i] + ", ";
            }

        }
        return ret;
    }

}
