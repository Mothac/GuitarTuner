package com.example.guitartuner.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.guitartuner.R;
import com.example.guitartuner.VarGlob;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    VarGlob VarG;
    int totalTabs;

    public SectionsPagerAdapter(Context c, FragmentManager fm, VarGlob VarG) {
        super(fm);
        mContext = c;
        this.totalTabs = TAB_TITLES.length;
        this.VarG = VarG;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:

                TunerFragment Tuner = new TunerFragment(VarG);

                return Tuner;
            case 1:
                SettingsFragment Settings = new SettingsFragment(VarG);

                return Settings;
            case 2:
                FreqFragment Frequency = new FreqFragment(VarG);

                return Frequency;

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //   VarG.FOCUS=position;
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}