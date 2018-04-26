package mx.iteso.escalaapp.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.beans.Competition;

public class ActivityCompetition extends AppCompatActivity {

    SimpleDraweeView draweeView;
    TextView name, descrption, day, month, year, gym;
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

        String comp_id = getIntent().getStringExtra("comp_id");
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Competitions").child(comp_id);
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Competition comp = new Competition();
                comp.setName(dataSnapshot.child("name").getValue().toString());
                comp.setDay(dataSnapshot.child("day").getValue().toString());
                comp.setMonth(dataSnapshot.child("month").getValue().toString());
                comp.setYear(dataSnapshot.child("year").getValue().toString());
                comp.setOwner(dataSnapshot.child("owner").getValue().toString());
                comp.setDescription(dataSnapshot.child("description").getValue().toString());
                comp.setImage(dataSnapshot.child("image").getValue().toString());
                comp.setParticipants(dataSnapshot.child("participants").getValue().toString());
                comp.setGym(dataSnapshot.child("gym").getValue().toString());


                name.setText(comp.getName());
                descrption.setText(comp.getDescription());
                day.setText(comp.getDay());
                month.setText(comp.getMonth());
                year.setText(comp.getYear());
                gym.setText(comp.getGym());

                Uri imageUri = Uri.parse(comp.getImage());
                draweeView.setImageURI(imageUri);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
