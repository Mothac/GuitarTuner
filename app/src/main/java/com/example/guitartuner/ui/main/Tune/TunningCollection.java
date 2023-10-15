package com.example.guitartuner.ui.main.Tune;

import java.util.ArrayList;

public class TunningCollection {


    public ArrayList<VariousTunning> CollectionTuning;

    public TunningCollection() {
        CollectionTuning = new ArrayList<VariousTunning>();
        CollectionTuning.add(new VariousTunning("Standard", new String[]{"E2", "A2", "D3", "G3", "B3", "E4"}));

//Drop
        CollectionTuning.add(new VariousTunning("Drop D", new String[]{"D2", "A2", "D3", "G3", "B3", "E4"}));

        CollectionTuning.add(new VariousTunning("Double Drop D", new String[]{"D2", "A2", "D3", "G3", "B3", "D4"}));

        CollectionTuning.add(new VariousTunning("Drop C", new String[]{"C2", "G2", "C3", "F3", "A3", "D4"}));

        CollectionTuning.add(new VariousTunning("Drop A", new String[]{"A1", "E2", "A2", "D3", "F#3", "B3"}));

//Open
        CollectionTuning.add(new VariousTunning("Open D", new String[]{"D2", "A2", "D3", "F#3", "A3", "D3"}));

        CollectionTuning.add(new VariousTunning("Open C", new String[]{"C2", "G2", "C3", "G3", "C4", "E4"}));

        CollectionTuning.add(new VariousTunning("Open E", new String[]{"E2", "B2", "E3", "G#3", "B3", "E4"}));

        CollectionTuning.add(new VariousTunning("Open A", new String[]{"E2", "A2", "C#3", "E3", "A3", "E4"}));

//Various
        CollectionTuning.add(new VariousTunning("C G D G B D", new String[]{"C2", "G2", "D3", "G3", "B3", "D4"}));

        CollectionTuning.add(new VariousTunning("C G D G B E", new String[]{"C2", "G2", "D3", "G3", "B3", "E4"}));

        CollectionTuning.add(new VariousTunning("D A D E A D", new String[]{"D2", "A2", "D3", "E3", "A3", "D4"}));

        CollectionTuning.add(new VariousTunning("D G D G A D", new String[]{"D2", "G2", "D3", "G3", "A3", "D4"}));
    }

}
