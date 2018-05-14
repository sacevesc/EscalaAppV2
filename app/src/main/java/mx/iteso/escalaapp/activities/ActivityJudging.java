package mx.iteso.escalaapp.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.beans.Climber;

public class ActivityJudging extends AppCompatActivity {
    int triesCounter = 0, bonusCounter = 0, time = 300, last;
    boolean top = false;
    public TextView topV, triesV, bonusV, timerV, resultsV, climber;
    public String compKey = "", currentRound = "", currentBoulder = "", currentClimber = "Result1", cboulder, calculateRanking;
    private Spinner boulderSpinner, roundSpinner, competitorSpinner;
    private FirebaseUser currentUser;
    private DatabaseReference judgeDatabase, resultsDatabase;
    //    private Button addClimber;
//    private ArrayDeque<Climber> competitors;
    private ArrayList<String> climberIDs;
    private ArrayList<Climber> registeredClimbers;
    boolean started = false;
    ImageView pause;
    boolean paused = true;
    CountDownTimer timer;
    boolean timerStarted = false;
    Climber judged;

    public void initData() {
        this.triesCounter = 0;
        this.bonusCounter = 0;
        this.time = 300;
        this.top = false;
        this.paused = true;
        this.started = false;

        pause.setVisibility(View.INVISIBLE);
        timerV.setText("");
        triesV.setText(String.valueOf(triesCounter));
        topV.setText(String.valueOf(triesCounter));
        bonusV.setText(String.valueOf(triesCounter));
        resultsV.setText("t0-b0");
        // PONER EL RELOJ EN 5:00 OTRA VEZ
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judging);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        topV = findViewById(R.id.activity_judging_topCounter);
        bonusV = findViewById(R.id.activity_judging_bonusCounter);
        triesV = findViewById(R.id.activity_judging_triesCounter);
        timerV = findViewById(R.id.activity_judging_timer);
        resultsV = findViewById(R.id.activity_judging_results);
        resultsV.setText("t0-b0");
        boulderSpinner = findViewById(R.id.activity_judging_boulderSpinner);
//        addClimber = findViewById(R.id.add_climber);
        pause = findViewById(R.id.pause);
//        competitors = new ArrayDeque<Climber>();
        climberIDs = new ArrayList<String>();
        registeredClimbers = new ArrayList<Climber>();
        compKey = getIntent().getStringExtra("comp_id");
        competitorSpinner = findViewById(R.id.activity_judging_competitorSpinner);


//        climber = findViewById(R.id.activity_judging_climber);

        Query gymsDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers");
        gymsDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                int selection = competitorSpinner.getSelectedItemPosition();
                ArrayList<Climber> competitors = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Climber climber = postSnapshot.getValue(Climber.class);
                    climber.setKey(postSnapshot.getKey());
                    competitors.add(climber);
                }
                ArrayAdapter<Climber> adapter = new ArrayAdapter<Climber>(getApplicationContext(), android.R.layout.simple_spinner_item, competitors);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                competitorSpinner.setAdapter(adapter);
                competitorSpinner.setSelection(selection);
            }

            public void onCancelled(DatabaseError databaseError) {
                Log.w("Gymslist", "loadPost:onCancelled", databaseError.toException());
            }
        });

//        climber.setText(currentClimber);
        DatabaseReference climbersDatabase = FirebaseDatabase.getInstance().getReference().child("Competitions").child(compKey).child("climbers");
        climbersDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> lclimberIDs = new ArrayList<String>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    lclimberIDs.add(postSnapshot.getKey());
                }
                climberIDs = new ArrayList<String>(lclimberIDs);
                climberIDs.addAll(lclimberIDs);
            }

            public void onCancelled(DatabaseError databaseError) {
                Log.w("Climberlist", "loadPost:onCancelled", databaseError.toException());
            }
        });

        Log.d("CLIMBERS", "" + climberIDs.size());
        Log.d("COMPETITION", "" + compKey);

        for (String id : climberIDs) {
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
        for (Climber c : registeredClimbers) {
            climberArray[i] = c.getFirstname() + " " + c.getLastname();
            i++;
        }
//        addClimber.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityJudging.this);
//                builder.setTitle("Add a competitor");
//                builder.setItems(climberArray, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        String climberName = climberArray[which].toString();
//                    }
//                });
//                builder.create();
//                builder.show();
//            }
//        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!top) {
                    if (!paused) {
                        paused = true;
                        pause.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_24dp));
                    } else {
                        paused = false;
                        pause.setImageDrawable(getDrawable(R.drawable.ic_pause_black_24dp));
                    }
                }
            }
        });
        competitorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                updateKeys("currentClimber", competitorSpinner.getSelectedItem().toString());
//                climber.setText(currentClimber);
                judged = (Climber) competitorSpinner.getSelectedItem();
                currentClimber = judged.toString();
                initData();
                resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child(judged.getCategory()).child(currentClimber).child("firstname");
                resultsDatabase.setValue(judged.getFirstname());
                resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child(judged.getCategory()).child(currentClimber).child("lastname");
                resultsDatabase.setValue(judged.getLastname());
                resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child(judged.getCategory()).child(currentClimber).child("ranking");
                resultsDatabase.setValue("");
                resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child(judged.getCategory()).child(currentClimber).child("boulders").child(currentBoulder).child("number");
                resultsDatabase.setValue(getCurrentBoulder().charAt(getCurrentBoulder().length() - 1) + "");
                resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child(judged.getCategory()).child(currentClimber).child("boulders").child(currentBoulder).child("top");
//                resultsDatabase.setValue("0");
                resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child(judged.getCategory()).child(currentClimber).child("boulders").child(currentBoulder).child("bonus");
//                resultsDatabase.setValue("0");

                Query scoreDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child(judged.getCategory()).child(currentClimber).child("boulders");
                scoreDatabase.addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int topN, bonusN, triesTop, triesBonus;
                        topN = bonusN = triesTop = triesBonus = 0;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                if (postSnapshot.child("top").getValue() != null && postSnapshot.child("bonus").getValue() != null) {
                                    triesTop += Integer.parseInt(postSnapshot.child("top").getValue().toString());
                                    triesBonus += Integer.parseInt(postSnapshot.child("bonus").getValue().toString());
                                    if (Integer.parseInt(postSnapshot.child("top").getValue().toString()) > 0)
                                        topN++;
                                    if (Integer.parseInt(postSnapshot.child("bonus").getValue().toString()) > 0)
                                        bonusN++;

                            }
                        }

                        resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child(judged.getCategory()).child(currentClimber).child("sum");
                        resultsDatabase.setValue(topN + "t" + triesTop + " " + bonusN + "b" + triesBonus);
                        calculateRanking = String.valueOf(topN) + String.valueOf(bonusN) + String.valueOf(triesTop) + String.valueOf(triesBonus);
                        Log.d("rank", "calculate" + calculateRanking);

                        Log.d("rank", "onDataChange: " + judged.getCategory().toLowerCase());
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Competitions").child(compKey).child(currentRound.toLowerCase());
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                long bestResult = Long.parseLong(dataSnapshot.getValue().toString().substring(0, 1)) * 1111;
                                long calculong = Math.abs(Long.parseLong(calculateRanking) - bestResult);
                                DatabaseReference rank = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child(judged.getCategory()).child(currentClimber).child("ranking");
                                rank.setValue(String.valueOf(calculong));
                                Log.d("rank", "rank change" + calculong);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        boulderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateKeys("currentBoulder", boulderSpinner.getSelectedItem().toString());
                setCurrentBoulder(boulderSpinner.getSelectedItem().toString());
                initData();
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
                setCurrentRound(roundSpinner.getSelectedItem().toString());
                initData();
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
        this.currentBoulder = "boulder" + currentBoulder;
        cboulder = currentBoulder;
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

    public void startTimer() {
        pause.setVisibility(View.VISIBLE);
        timer = new CountDownTimer(999999999, 1000) {
            public void onTick(long millisUntilFinished) {
                if (!paused) {
                    if (!top && time > 0) {
                        timerV.setText(formatTime(time));
                        time--;
                        if (time == 0) {
                            timerV.setText(R.string.time_up);
                            paused = true;
                            pause.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            public void onFinish() {
                timerV.setText(R.string.time_up);
            }

        }.start();
    }

    public String formatTime(int totalSecs) {
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    public void addTry(View v) {
        if (!top) {
            if (!started) {
                if (!timerStarted) {
                    startTimer();
                    timerStarted = true;
                }
                started = true;
                paused = false;
                pause.setImageDrawable(getDrawable(R.drawable.ic_pause_black_24dp));
                pause.setVisibility(View.VISIBLE);
            }
            if (!paused) {
                last = 0;
                triesCounter++;
                triesV.setText(String.valueOf(triesCounter));
            }
        }
    }

    public void setTop(View v) {
        if (!top && !paused) {
            last = 1;
            top = true;
            topV.setText(String.valueOf(triesCounter));
            resultsV.setText("t" + (top ? triesCounter : "0") + "-b" + bonusCounter);

            resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child(judged.getCategory()).child(currentClimber).child("boulders").child(currentBoulder).child("top");
            resultsDatabase.setValue(topV.getText().toString());
        }
    }

    public void setBonus(View v) {
        if (!top && !paused) {
            last = 2;
            bonusCounter = triesCounter;
            bonusV.setText(String.valueOf(bonusCounter));
            resultsV.setText("t" + (top ? triesCounter : "0") + "-b" + bonusCounter);
            resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child(judged.getCategory()).child(currentClimber).child("boulders").child(currentBoulder).child("bonus");
            resultsDatabase.setValue(bonusV.getText().toString());
        }
    }

    public void undo(View v) {
        switch (last) {
            case 0:
                if (triesCounter > 0) {
                    triesCounter--;
                    triesV.setText(String.valueOf(triesCounter));
                }
                break;
            case 1:
                top = false;
                topV.setText("0");
                resultsV.setText("t" + (top ? triesCounter : "0") + "-b" + bonusCounter);
                resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child(judged.getCategory()).child(currentClimber).child("boulders").child(currentBoulder).child("top");
                resultsDatabase.setValue(topV.getText().toString());

                break;
            case 2:
                bonusCounter = 0;
                bonusV.setText("0");
                resultsV.setText("t0-b0");
                resultsDatabase = FirebaseDatabase.getInstance().getReference().child("Results").child(currentRound).child(compKey).child(judged.getCategory()).child(currentClimber).child("boulders").child(currentBoulder).child("bonus");
                resultsDatabase.setValue(bonusV.getText().toString());
                break;
        }

    }
}