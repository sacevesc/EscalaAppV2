package mx.iteso.escalaapp.results;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;

import mx.iteso.escalaapp.R;

public class ActivityResults extends AppCompatActivity {
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference climbersDatabase;
    private RecyclerView climbersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLayoutManager = new LinearLayoutManager(ActivityResults.this);
        climbersList.setHasFixedSize(true);
        climbersList.setLayoutManager(mLayoutManager);
    }


}
