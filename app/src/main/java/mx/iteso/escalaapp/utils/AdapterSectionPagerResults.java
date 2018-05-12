package mx.iteso.escalaapp.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import mx.iteso.escalaapp.results.FragmentFinal;
import mx.iteso.escalaapp.results.FragmentQualifications;
import mx.iteso.escalaapp.results.FragmentSemifinal;
import mx.iteso.escalaapp.results.FragmentSuperfinal;


public class AdapterSectionPagerResults extends FragmentPagerAdapter {

    private String actualCategory = "Principiantes";
    private String compKey = "";
    private String fragments[] = {"Qualifications", "SemiFinal", "Final", "SuperFinal"};
    private FragmentQualifications fragmentQualifications;
    private FragmentSemifinal fragmentSemifinal;
    private FragmentFinal fragmentFinal;
    private FragmentSuperfinal fragmentSuperFinal;

    public AdapterSectionPagerResults(FragmentManager fm) {
        super(fm);

    }

    public String getActualCategory() {
        return actualCategory;
    }

    public void setActualCategory(String actualCategory) {
        this.actualCategory = actualCategory;
        Log.e("logs", " MSPR setCategory: " + actualCategory, null);

    }

    public String getCompKey() {
        return compKey;
    }

    public void setCompKey(String compKey) {
        this.compKey = compKey;
        Log.e("logs", " MSPR setCompKey: " + compKey, null);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (fragmentQualifications == null)
                    fragmentQualifications = new FragmentQualifications();
                return fragmentQualifications;

            case 1:
                if (fragmentSemifinal == null)
                    fragmentSemifinal = new FragmentSemifinal();
                return fragmentSemifinal;

            case 2:
                if (fragmentFinal == null)
                    fragmentFinal = new FragmentFinal();
                return fragmentFinal;

            case 3:
                if (fragmentSuperFinal == null)
                    fragmentSuperFinal = new FragmentSuperfinal();
                return fragmentSuperFinal;
            default:
                return new FragmentQualifications();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments[position];
    }

}
