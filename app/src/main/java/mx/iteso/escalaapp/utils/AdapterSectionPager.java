package mx.iteso.escalaapp.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import mx.iteso.escalaapp.FragmentCompetitions.FragmentCompetitionComingUp;
import mx.iteso.escalaapp.FragmentCompetitions.FragmentCompetitionEnded;
import mx.iteso.escalaapp.FragmentCompetitions.FragmentCompetitionLive;


public class AdapterSectionPager extends FragmentPagerAdapter {

    private String fragments[] = {"Ended", "Live", "Coming Up"};
    private FragmentCompetitionEnded fragmentCompetitionEnded;
    private FragmentCompetitionComingUp fragmentCompetitionComingUp;
    private FragmentCompetitionLive fragmentCompetitionLive;

    public AdapterSectionPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (fragmentCompetitionEnded == null)
                    fragmentCompetitionEnded = new FragmentCompetitionEnded();
                return fragmentCompetitionEnded;

            case 1:
                if (fragmentCompetitionLive == null)
                    fragmentCompetitionLive = new FragmentCompetitionLive();
                return fragmentCompetitionLive;

            case 2:
                if (fragmentCompetitionComingUp == null)
                    fragmentCompetitionComingUp = new FragmentCompetitionComingUp();
                return fragmentCompetitionComingUp;
            default:
                return new FragmentCompetitionLive();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments[position];
    }

}
