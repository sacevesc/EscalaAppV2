package mx.iteso.escalaapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.beans.Climber;
import mx.iteso.escalaapp.results.ActivityResults;


public class ActivityClimber extends AppCompatActivity {

    SimpleDraweeView draweeView;
    TextView firstname, lastname, descrption, city, state, gym;

    Button results;
    private DatabaseReference userDatabase;
    // private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_climber);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.app_name));


        firstname = findViewById(R.id.climber_firstname);
        lastname = findViewById(R.id.climber_lastname);
        city = findViewById(R.id.climber_city_name);
        state = findViewById(R.id.climber_state_name);
        descrption = findViewById(R.id.climber_description);
        gym = findViewById(R.id.climber_gym_name);
        draweeView = (SimpleDraweeView) findViewById(R.id.climber_profile_picture);

        results = findViewById(R.id.climber_results_button);
        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityClimber.this, ActivityResults.class);
                startActivity(intent);
            }
        });

        String climber_id = getIntent().getStringExtra("climber_id");
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers").child(climber_id);
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Climber climber = new Climber();
                climber.setFirstname(dataSnapshot.child("firstname").getValue().toString());
                climber.setLastname(dataSnapshot.child("lastname").getValue().toString());
                climber.setGym(dataSnapshot.child("gym").getValue().toString());
                climber.setState(dataSnapshot.child("state").getValue().toString());
                climber.setCity(dataSnapshot.child("city").getValue().toString());
                climber.setDescription(dataSnapshot.child("description").getValue().toString());
                climber.setImage(dataSnapshot.child("image").getValue().toString());
                climber.setThumb(dataSnapshot.child("thumb").getValue().toString());

                firstname.setText(climber.getFirstname());
                lastname.setText(climber.getLastname());
                gym.setText(climber.getGym());
                state.setText(climber.getState());
                city.setText(climber.getCity());
                descrption.setText(climber.getDescription());

                Uri imageUri = Uri.parse(climber.getThumb());
                draweeView.setImageURI(imageUri);
                imageUri = Uri.parse(climber.getImage());
                draweeView.setImageURI(imageUri);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
