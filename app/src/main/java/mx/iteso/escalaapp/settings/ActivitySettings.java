package mx.iteso.escalaapp.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.activities.ActivitySplashScreen;
import mx.iteso.escalaapp.beans.Climber;

public class ActivitySettings extends AppCompatActivity {

    Button generalSettings, gymSettings, otherSettings;
    LinearLayout logout;
    TextView firstname, lastname, gym;
    SimpleDraweeView draweeView;
    private DatabaseReference userDatabase;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        generalSettings = findViewById(R.id.general_settings);
        gymSettings = findViewById(R.id.gym_settings);
        otherSettings = findViewById(R.id.other_settings);
        firstname = findViewById(R.id.settings_climber_firstname);
        lastname = findViewById(R.id.settings_climber_lastname);
        gym = findViewById(R.id.settings_climber_gym);
        draweeView = findViewById(R.id.settings_profile_picture);
        logout = findViewById(R.id.log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ActivitySettings.this, ActivitySplashScreen.class);
                startActivity(intent);
            }
        });

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
                climber.setImage(dataSnapshot.child("image").getValue().toString());
                climber.setThumb(dataSnapshot.child("thumb").getValue().toString());

                firstname.setText(climber.getFirstname());
                lastname.setText(climber.getLastname());
                gym.setText(climber.getGym());

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


    public void settingsHandler(View target) {
        generalSettings.setAlpha(.5f);
        gymSettings.setAlpha(.5f);
        otherSettings.setAlpha(.5f);
        setFragment(new FragmentGymSettings());
        Fragment activeFragment = new FragmentGymSettings();

        if (target.getId() == R.id.general_settings) {
            activeFragment = new FragmentGymSettings();
            generalSettings.setAlpha(1);
        } else if (target.getId() == R.id.gym_settings) {
            if (activeFragment == null)
                activeFragment = new FragmentGymSettings();
            gymSettings.setAlpha(1);
        } else if (target.getId() == R.id.other_settings) {
            if (activeFragment == null)
                activeFragment = new FragmentGymSettings();
            otherSettings.setAlpha(1);
        }
        setFragment(activeFragment);

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragments_settings, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
