package mx.iteso.escalaapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.beans.Competition;

public class ActivityCompetition extends AppCompatActivity {

    SimpleDraweeView draweeView;
    TextView name, descrption, day, month, year, gym;
    String entrants;
    Button register, results, judge;

    private DatabaseReference userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);

        draweeView = findViewById(R.id.comp_picture);
        name = findViewById(R.id.comp_name);
        descrption = findViewById(R.id.comp_description);
        day = findViewById(R.id.comp_day);
        month = findViewById(R.id.comp_month);
        year = findViewById(R.id.comp_year);
        gym = findViewById(R.id.comp_gym_name);

        register = findViewById(R.id.comp_register_button);

        results = findViewById(R.id.comp_results_button);
        judge = findViewById(R.id.comp_judge_button);


        final String comp_id = getIntent().getStringExtra("comp_id");
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Competitions").child(comp_id);
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Competition comp = new Competition();
                comp.setName(Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString());
                comp.setDay(Objects.requireNonNull(dataSnapshot.child("day").getValue()).toString());
                comp.setMonth(Objects.requireNonNull(dataSnapshot.child("month").getValue()).toString());
                comp.setYear(Objects.requireNonNull(dataSnapshot.child("year").getValue()).toString());
                comp.setOwner(Objects.requireNonNull(dataSnapshot.child("owner").getValue()).toString());
                comp.setDescription(Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString());
                comp.setImage(Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString());
                comp.setParticipants(Objects.requireNonNull(dataSnapshot.child("participants").getValue()).toString());
                comp.setGym(Objects.requireNonNull(dataSnapshot.child("gym").getValue()).toString());
                name.setText(comp.getName());
                descrption.setText(comp.getDescription());
                day.setText(comp.getDay());
                month.setText(comp.getMonth());
                year.setText(comp.getYear());
                gym.setText(comp.getGym());
                Uri imageUri = Uri.parse(comp.getImage());
                draweeView.setImageURI(imageUri);
                entrants = comp.getParticipants();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCompetition.this, ActivityResults.class);
                startActivity(intent);
            }
        });
        judge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCompetition.this, ActivityJudging.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int participants = Integer.parseInt(entrants);
                participants++;

                userDatabase.child("participants").setValue(((String.valueOf(participants)))).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            register.setVisibility(View.INVISIBLE);
                            Log.d("Comp", "register:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Comp", "CREATEgym:failure", task.getException());
                            Toast.makeText(ActivityCompetition.this, "register failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
            }
        });
    }
}