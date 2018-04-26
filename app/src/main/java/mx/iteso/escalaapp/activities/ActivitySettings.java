package mx.iteso.escalaapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.settings.FragmentGymSettings;

public class ActivitySettings extends AppCompatActivity {

    Button generalSettings, gymSettings, otherSettings;
    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        generalSettings = findViewById(R.id.general_settings);
        gymSettings = findViewById(R.id.gym_settings);
        otherSettings = findViewById(R.id.other_settings);

    }


    public void settingsHandler(View target) {
        generalSettings.setAlpha(.5f);
        gymSettings.setAlpha(.5f);
        otherSettings.setAlpha(.5f);
        setFragment(new FragmentGymSettings());
        Fragment activeFragment = null;

        if (target.getId() == R.id.general_settings) {
            activeFragment = new FragmentGymSettings();
            generalSettings.setAlpha(1);
        } else if (target.getId() == R.id.gym_settings) {
            activeFragment = new FragmentGymSettings();
            gymSettings.setAlpha(1);
        } else if (target.getId() == R.id.other_settings) {
            activeFragment = new FragmentGymSettings();
            otherSettings.setAlpha(1);
        }
        setFragment(activeFragment);

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragments_settings, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
