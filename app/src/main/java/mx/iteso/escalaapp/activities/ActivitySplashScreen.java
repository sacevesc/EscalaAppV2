package mx.iteso.escalaapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.beans.User;

public class ActivitySplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);//FRESCO SE INICILIZA

        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();//firebase

    }


    @Override
    public void onStart() {//firebase
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (currentUser == null) {
                    Intent intent = new Intent(ActivitySplashScreen.this, ActivityLogin.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(ActivitySplashScreen.this, ActivityMain.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000);


    }

    public User loadUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("mx.iteso.USER_PREFERENCES", MODE_PRIVATE);
        User user = new User();
        user.setName(sharedPreferences.getString("NAME", null));
        user.setPassword(sharedPreferences.getString("PWD", null));
        user.setIslogged(sharedPreferences.getBoolean("LOGGED", false));
        return user;
    }
}
