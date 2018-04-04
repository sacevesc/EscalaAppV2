package mx.iteso.escalaapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragment(new FragmentClimbers());

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment activeFragment;
                        switch (item.getItemId()) {
                            case R.id.action_gyms:
                                activeFragment = new FragmentGyms();
                                setFragment(activeFragment);
                                break;

                            case R.id.action_climbers:
                                activeFragment = new FragmentClimbers();
                                setFragment(activeFragment);
                                break;
                        }
                        return true;
                    }
                });

    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.frame_main_fragment, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }




}
