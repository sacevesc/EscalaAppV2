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
    TextView name, descrption, host, members, podiums;
    private DatabaseReference userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);

        draweeView = findViewById(R.id.gym_profile_picture);
        name = findViewById(R.id.gym_name);
        descrption = findViewById(R.id.gym_description);
        host = findViewById(R.id.gym_number_host);
        members = findViewById(R.id.gym_members_number);
        podiums = findViewById(R.id.gym_podiums_number);


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
                gym.setThumb(dataSnapshot.child("thumb").getValue().toString());
                gym.setMembers(dataSnapshot.child("members").getValue().toString());
                gym.setPodiums(dataSnapshot.child("podiums").getValue().toString());
                gym.setHost(dataSnapshot.child("host").getValue().toString());

                name.setText(gym.getName());
                descrption.setText(gym.getDescription());
                members.setText(gym.getMembers());
                host.setText(gym.getHost());
                podiums.setText(gym.getPodiums());

                Uri imageUri = Uri.parse(gym.getThumb());
                draweeView.setImageURI(imageUri);
                imageUri = Uri.parse(gym.getImage());
                draweeView.setImageURI(imageUri);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
