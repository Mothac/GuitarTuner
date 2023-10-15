package com.example.guitartuner.FTT;

public class Filtre {

    private double a1 = 0;
    private double b1 = 0;

    private double a0 = 0;
    private double b0 = 0;
    private double fc = 0;
    private double wc = 0;
    private double tauc = 0;
    private final double[] Output = {0, 0};
    private final double[] Input = {0, 0};

    public Filtre() {

    }

    public Filtre(double fc, double dt) {
        this.fc = fc;
        this.wc = 2 * Math.PI * this.fc;
        this.tauc = 1 / this.wc;
        this.a0 = dt;
        this.a1 = dt;
        this.b0 = (dt + 2 * tauc);
        this.b1 = (dt - 2 * tauc);
    }

    public double new_value(double Value_new) {
        Input[0] = Value_new;
        Output[0] = (1 / b0) * (a0 * Input[0] + a1 * Input[1] - b1 * Output[1]);
        Output[1] = Output[0];
        Input[1] = Input[0];
        // Y(x) = (1 / b0) * (a0 * U(x) + a1 * U(x - 1) - b1 * Y(x - 1));
        return Output[0];
    }

}
