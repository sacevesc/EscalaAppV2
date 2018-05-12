package mx.iteso.escalaapp.results;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.settings.ActivitySettings;
import mx.iteso.escalaapp.utils.AdapterSectionPagerResults;

public class ActivityResults extends AppCompatActivity {
    private RecyclerView.LayoutManager mLayoutManager;
    public String compKey = "";

    private RecyclerView climbersList;
    private DatabaseReference userDatabase;

    String actualCategory = "Principiantes";
    private FirebaseUser currentUser;
    private ViewPager mViewPager;
    private AdapterSectionPagerResults mSectionsPagerAdapterResults;
    private Spinner categorySpineer;

    public String getCompKey() {
        return compKey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        compKey = getIntent().getStringExtra("comp_id");

        categorySpineer = findViewById(R.id.activity_results_spinner);
        categorySpineer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateKeys(categorySpineer.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        mSectionsPagerAdapterResults = new AdapterSectionPagerResults(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapterResults);


        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        });
    }

    public void updateKeys(final String category) {
        Map currentKeys = new HashMap();
        currentKeys.put("currentCompKey", getCompKey());
        currentKeys.put("currentCategory", category);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = currentUser.getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers").child(currentUid);
        userDatabase.updateChildren(currentKeys).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("logs", "updates " + category + " " + getCompKey());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(ActivityResults.this, ActivitySettings.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
