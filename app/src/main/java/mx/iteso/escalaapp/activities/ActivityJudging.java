package mx.iteso.escalaapp.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.beans.Climber;
import mx.iteso.escalaapp.fragmentclimber.AdapterClimber;

public class ActivityJudging extends AppCompatActivity {
    int triesCounter = 0, bonusCounter = 0, time = 300, last;
    boolean top = false;
    public TextView topV, triesV, bonusV, timerV, resultsV;
    public String compKey = "", currentRound = "", currentBoulder = "";
    private Spinner boulderSpinner, roundSpinner;
    private FirebaseUser currentUser;
    private DatabaseReference judgeDatabase, resultsDatabase;
    private Button addClimber;
    private ArrayDeque<Climber> competitors;
    private ArrayList<String> climberIDs;
    private ArrayList<Climber> registeredClimbers;
    boolean started = false;

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
        addClimber = findViewById(R.id.add_climber);
        competitors = new ArrayDeque<Climber>();
        climberIDs = new ArrayList<String>();
        registeredClimbers = new ArrayList<Climber>();
        compKey = getIntent().getStringExtra("comp_id");


        Query climbersDatabase = FirebaseDatabase.getInstance().getReference().child("Competitions").child(compKey).child("climbers");
        climbersDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    climberIDs.add(postSnapshot.getKey());
                }
            }
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Climberlist", "loadPost:onCancelled", databaseError.toException());
            }
        });

        Log.d("CLIMBERS", ""+ climberIDs.size());
        Log.d("COMPETITION", ""+ compKey);

        for(String id : climberIDs){
            climbersDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers").child(id);
            climbersDatabase.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    registeredClimbers.add(dataSnapshot.getValue(Climber.class));
                }
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("Climberlist", "loadPost:onCancelled", databaseError.toException());
                }
            });
        }

        final CharSequence climberArray[] = new CharSequence[registeredClimbers.size()];
        int i = 0;
        for(Climber c : registeredClimbers){
            climberArray[i] = c.getFirstname() + " " + c.getLastname();
            i++;
        }
        addClimber.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityJudging.this);
                builder.setTitle("Add a competitor");
                builder.setItems(climberArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String climberName = climberArray[which].toString();
                    }
                });
                builder.create();
                builder.show();
            }
        });
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

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = currentUser.getUid();
        judgeDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers").child(currentUid);
//        judgeDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                setCurrentRound(dataSnapshot.child("currentRound").getValue().toString());
//                setCurrentBoulder(dataSnapshot.child("currentBoulder").getValue().toString());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

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
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    public void addTry(View v){
        if(!top) {
            if (!started) {
                startTimer();
                started = true;
            }
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
                if(triesCounter > 0) {
                    triesCounter--;
                    triesV.setText(String.valueOf(triesCounter));
                }
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
