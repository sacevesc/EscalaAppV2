package mx.iteso.escalaapp.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import mx.iteso.escalaapp.R;

public class ActivityJudging extends AppCompatActivity {
    int triesCounter = 0, bonusCounter = 0, time = 300, last;
    boolean top = false;
    TextView topV, triesV, bonusV, timerV, resultsV;
    public String compKey = "", currentRound = "", currentBoulder = "";
    Spinner boulderSpinner, roundSpinner;
    private FirebaseUser currentUser;
    private DatabaseReference judgeDatabase, resultsDatabase;

    public String getCompKey() {
        return compKey;
    }

    public void setCompKey(String compKey) {
        this.compKey = compKey;
        Log.w("logs", "cat-jg " + compKey, null);
    }

    public String getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(String currentRound) {
        this.currentRound = currentRound;
        Log.w("logs", "round-jg " + currentRound, null);
    }

    public String getCurrentBoulder() {
        return currentBoulder;
    }

    public void setCurrentBoulder(String currentBoulder) {
        this.currentBoulder = currentBoulder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judging);
        topV = findViewById(R.id.activity_judging_topCounter);
        bonusV = findViewById(R.id.activity_judging_bonusCounter);
        triesV = findViewById(R.id.activity_judging_triesCounter);
        timerV = findViewById(R.id.activity_judging_timer);
        resultsV = findViewById(R.id.activity_judging_results);
        resultsV.setText("t0-b0");
        boulderSpinner = findViewById(R.id.activity_judging_boulderSpinner);
        boulderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateKeys("currentBoulder", boulderSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        roundSpinner = findViewById(R.id.activity_judging_roundSpinner);
        roundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateKeys("currentRound", roundSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        compKey = getIntent().getStringExtra("comp_id");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = currentUser.getUid();
        judgeDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers").child(currentUid);
        judgeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setCurrentRound(dataSnapshot.child("currentRound").getValue().toString());
                setCurrentBoulder(dataSnapshot.child("currentBoulder").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void updateKeys(final String toUpdate, final String value) {
        Map currentKeys = new HashMap();
        currentKeys.put(toUpdate, value);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = currentUser.getUid();
        judgeDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers").child(currentUid);
        judgeDatabase.updateChildren(currentKeys).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("logs", "updates " + value + " " + getCompKey());
                }
            }
        });
    }

    public void startTimer(){
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                if(!top) {
                    timerV.setText(formatTime(time));
                    time--;
                }
            }

            public void onFinish() {
                timerV.setText("try again");
            }

        }.start();
    }

    public String formatTime(int totalSecs){
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void addTry(View v){
        if(!top) {
            if (triesCounter == 0)
                startTimer();
            last = 0;
            triesCounter++;
            triesV.setText(String.valueOf(triesCounter));
        }
    }

    public void setTop(View v){
        if(!top) {
            last = 1;
            top = true;
            topV.setText(String.valueOf(triesCounter));
            resultsV.setText("t" + (top ? triesCounter : "0") + "-b" + bonusCounter);

            resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child("Principiantes").child("Result1").child("boulders").child(currentBoulder).child("top");
            resultsDatabase.setValue(topV.getText().toString());
        }
    }

    public void setBonus(View v){
        if(!top) {
            last = 2;
            bonusCounter = triesCounter;
            bonusV.setText(String.valueOf(bonusCounter));
            resultsV.setText("t" + (top ? triesCounter : "0") + "-b" + bonusCounter);
            resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child("Principiantes").child("Result1").child("boulders").child(currentBoulder).child("bonus");
            resultsDatabase.setValue(bonusV.getText().toString());
        }
    }
    public void undo(View v){
        switch(last){
            case 0:
                triesCounter --;
                triesV.setText(String.valueOf(triesCounter));
                break;
            case 1:
                top = false;
                topV.setText("");
                resultsV.setText("t" + (top ? triesCounter : "0") + "-b" + bonusCounter);
                resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child("Principiantes").child("Result1").child("boulders").child(currentBoulder).child("top");
                resultsDatabase.setValue(topV.getText().toString());

                break;
            case 2:
                bonusCounter = 0;
                bonusV.setText("");
                resultsV.setText("t0-b0");
                resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child("Principiantes").child("Result1").child("boulders").child(currentBoulder).child("bonus");
                resultsDatabase.setValue(bonusV.getText().toString());
                break;
        }

    }
}
