package com.example.guitartuner.ui.main.Tune;

public class NOTE {

    final String REF_note = "A";
    final int REF_Ocatave = 4;
    final double FreqBase = 440.0;//Base ref= A4 = 440HZ
    public final String[] Note = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    final double Const_a = Math.pow(2.0, (1.0 / Note.length));

    private double Frequency_trick = 1.0;


    //final String[] Note_t={"C2","C#2","D2","D#2","E2","F2","F#2","G2","G#2","A2","A#2","B2"};

    //Octave 1
    //final double[] Freq_Note= {32.703125, 34.6478, 36.7081, 38.8909, 41.2034, 43.6535, 46.25, 49, 51.9131, 55.0000, 58.2705, 61.7354 };
    //Octave 0
    //final double[] Freq_Note0= new double[Freq_Note.length];


    public NOTE(double freq_base) {//Base ref= A4 = 440HZ

        Frequency_trick = freq_base / FreqBase;


    }


    public NOTE() {
        Frequency_trick = 1.0;


    }


    public double NOTE_Otave(String Note_in, int Octave) {
        int indexNote = IndexNote(Note_in);

        if (indexNote == -1) {
            return -1.0;
        } else {

            return NOTE_Otace_cal(Note_in, Octave);

        }
    }

    public double NOTE_Otave(String Note_in) {
        String NoteT;
        int Octave;
        if (Note_in.length() > 2) {
            NoteT = Note_in.substring(0, 2);
            Octave = Integer.valueOf(Note_in.substring(2, 3));
        } else {
            NoteT = Note_in.substring(0, 1);
            Octave = Integer.valueOf(Note_in.substring(1, 2));

        }

        int indexNote = IndexNote(NoteT);

        if (indexNote == -1) {
            return -1.0;
        } else {

            return NOTE_Otace_cal(NoteT, Octave);

        }
    }


    public double NOTE_Otace_cal(String Note_in, int Octave) {
        double FreqCal = 0;

        int diff_ocatve = Octave - REF_Ocatave;

        int indexNote = IndexNote(Note_in);
        int indexREF = IndexNote(REF_note);

        int diff_note = indexNote - indexREF;


        int diffNoteTot = diff_note + (diff_ocatve * Note.length);


        FreqCal = Frequency_trick * FreqBase * Math.pow(Const_a, diffNoteTot);

     //   Log.d("CUST1", "Tune Cal :" + Note_in + Octave);

        return FreqCal;

    }

    public int IndexNote2(String Note_in) {
        String NoteT = null;
        int Octave;
        boolean bypass = false;
        if (Note_in.length() == 3) {
            NoteT = Note_in.substring(0, 2);
            if (isNumeric(Note_in.substring(2, 3))) {
                Octave = Integer.valueOf(Note_in.substring(2, 3));
            } else {
                bypass = true;
            }
        } else if (Note_in.length() == 2) {
            NoteT = Note_in.substring(0, 1);
            if (isNumeric(Note_in.substring(1, 2))) {
                Octave = Integer.valueOf(Note_in.substring(1, 2));
            } else {
                bypass = true;
            }

        } else {
            bypass = true;

        }

        int indexNote = -1;
        if (!bypass) {
            for (int i = 0; i < Note.length; i++) {
                if (Note[i].equals(NoteT)) {
                    indexNote = i;
                    break;
                }
            }
        }
        return indexNote;
    }


    public int IndexNote(String Note_in) {
        int indexNote = -1;
        for (int i = 0; i < Note.length; i++) {
            if (Note[i].equals(Note_in)) {
                indexNote = i;
                break;
            }
        }
        return indexNote;
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
