package mx.iteso.escalaapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mx.iteso.escalaapp.beans.Climber;

public class ActivityProfile extends AppCompatActivity {

    ImageView edit_profile;
    SimpleDraweeView draweeView;
    TextView firstname, lastname, descrption, city, state, gym;

    Button results;
    DatabaseReference userDatabase;
    private FirebaseUser currentUser;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getResources().getString(R.string.app_name));

            edit_profile = findViewById(R.id.climber_profile_edit);
            edit_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editIntent = new Intent(ActivityProfile.this, ActivityEditProfile.class);
                    startActivity(editIntent);

                }
            });

            firstname = findViewById(R.id.climber_firstname);
            lastname = findViewById(R.id.climber_lastname);
            city = findViewById(R.id.climber_city_name);
            state = findViewById(R.id.climber_state_name);
            descrption = findViewById(R.id.climber_description);
            gym = findViewById(R.id.climber_gym_name);
            results = findViewById(R.id.climber_results_button);
            draweeView = (SimpleDraweeView) findViewById(R.id.climber_profile_picture);


            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String currentUid = currentUser.getUid();
            userDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers").child(currentUid);
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
                    climber.setPhoto(dataSnapshot.child("image").getValue().toString());

                    firstname.setText(climber.getFirstname());
                    lastname.setText(climber.getLastname());
                    gym.setText(climber.getGym());
                    state.setText(climber.getState());
                    city.setText(climber.getCity());
                    descrption.setText(climber.getDescription());

                    Uri imageUri = Uri.parse(climber.getPhoto());
                    draweeView.setImageURI(imageUri);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

