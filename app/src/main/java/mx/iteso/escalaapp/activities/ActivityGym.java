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
import mx.iteso.escalaapp.beans.Gym;

public class ActivityGym extends AppCompatActivity {

    SimpleDraweeView draweeView;
    TextView name, descrption;
    private DatabaseReference userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);

        draweeView = findViewById(R.id.gym_profile_picture);
        name = findViewById(R.id.gym_name);
        descrption = findViewById(R.id.gym_description);


        String gym_id = getIntent().getStringExtra("gym_id");
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Gyms").child(gym_id);
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Gym gym = new Gym();
                gym.setName(dataSnapshot.child("name").getValue().toString());
                gym.setAddress(dataSnapshot.child("address").getValue().toString());
                gym.setCity(dataSnapshot.child("city").getValue().toString());
                gym.setState(dataSnapshot.child("state").getValue().toString());
                gym.setOwner(dataSnapshot.child("owner").getValue().toString());
                gym.setDescription(dataSnapshot.child("description").getValue().toString());
                gym.setImage(dataSnapshot.child("image").getValue().toString());

                name.setText(gym.getName());
                descrption.setText(gym.getDescription());

                Uri imageUri = Uri.parse(gym.getImage());
                draweeView.setImageURI(imageUri);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
