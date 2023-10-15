package com.example.guitartuner.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.guitartuner.VarGlob;

public class SaveData {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    VarGlob VarG;
    private final Context context;

    public SaveData(Context context) {
        this.context = context;


    }

    public void SavePref(String Name, String Value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(Name, Value);
        editor.apply();
    }

    public String LoadPref(String Name, String Default) {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        String Val = pref.getString(Name, Default);
        return Val;
    }
}
