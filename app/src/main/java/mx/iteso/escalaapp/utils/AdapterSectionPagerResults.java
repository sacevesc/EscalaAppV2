package mx.iteso.escalaapp.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mx.iteso.escalaapp.results.FragmentFinal;
import mx.iteso.escalaapp.results.FragmentQualifications;
import mx.iteso.escalaapp.results.FragmentSemifinal;
import mx.iteso.escalaapp.results.FragmentSuperfinal;


public class AdapterSectionPagerResults extends FragmentPagerAdapter {

    private String actualCategory = "Principiantes";
    private String compKey = "";
    private String fragments[] = {"Qualifications", "SemiFinal", "Final", "SuperFinal"};
    int rondas = 0;
    private FragmentQualifications fragmentQualifications;
    private FragmentSemifinal fragmentSemifinal;
    private FragmentFinal fragmentFinal;
    private FragmentSuperfinal fragmentSuperFinal;
    private String twofragments[] = {"Qualifications", "Final"};

    public AdapterSectionPagerResults(FragmentManager fm, String compKey) {
        super(fm);
        setCompKey(compKey);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Competitions").child(compKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("logs", "onDataChange ADAPTER : " + dataSnapshot.child("no_rounds").getValue().toString());
                rondas = Integer.parseInt(dataSnapshot.child("no_rounds").getValue().toString());
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
        if (getCount() == 1) {
            switch (position) {
                case 0:
                    if (fragmentFinal == null)
                        fragmentFinal = new FragmentFinal();
                    return fragmentFinal;
                default:
                    return new FragmentFinal();
            }

        } else if (getCount() == 2) {
            switch (position) {
                case 0:
                    if (fragmentQualifications == null)
                        fragmentQualifications = new FragmentQualifications();
                    return fragmentQualifications;

                case 1:
                    if (fragmentFinal == null)
                        fragmentFinal = new FragmentFinal();
                    return fragmentFinal;

                default:
                    return new FragmentQualifications();
            }

        } else {

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
    }

    @Override
    public int getCount() {
        return rondas;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (getCount() == 1) {
            return "Finals";
        } else if (getCount() == 2) {
            return twofragments[position];
        } else {
            return fragments[position];

        }
    }

}
