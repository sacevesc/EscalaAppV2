package mx.iteso.escalaapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import mx.iteso.escalaapp.R;

public class ActivitySignIn extends AppCompatActivity {
    EditText firstname, lastname, email, password, descrption;
    AutoCompleteTextView city, state, gym;
    Button done, facebook_signin, image_btn;


    private FirebaseAuth mAuth;//firebase auth
    private ProgressDialog progressDialog;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //firebase auth
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);


        firstname = findViewById(R.id.sigin_firstname);
        lastname = findViewById(R.id.sigin_lastname);
        password = findViewById(R.id.sigin_password);
        email = findViewById(R.id.signin_email);
        image_btn = findViewById(R.id.signin_image_button);


        city = findViewById(R.id.sigin_city_autocomplete);
        String[] cities = getResources().getStringArray(R.array.ciudades);
        ArrayAdapter<String> adapterCities = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        city.setAdapter(adapterCities);

        state = findViewById(R.id.sigin_state_autocomplete);
        String[] states = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapterStates = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, states);
        state.setAdapter(adapterStates);

        descrption = findViewById(R.id.signin_description);
        gym = findViewById(R.id.sigin_gym);
        String[] gyms = getResources().getStringArray(R.array.muros);
        ArrayAdapter<String> adapterGyms = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gyms);
        gym.setAdapter(adapterGyms);


        done = findViewById(R.id.signin_done_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkDataUser()) {
                    //saveUser();        progressDialog = new ProgressDialog(this);

                    progressDialog.setTitle("Regstering User");
                    progressDialog.setMessage("Please wait while your account is being created");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    createAccount(email.getText().toString(), password.getText().toString());
                }
            }
        });
        facebook_signin = findViewById(R.id.activity_login_facebook_button);
        facebook_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivitySignIn.this, "Iniciar sesion mediante Facebook", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ActivitySignIn.this, ActivityMain.class);

            }
        });

    }


    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentUser.getUid();
                            firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers").child(uid);
                            HashMap<String, String> climbersMap = new HashMap<>();
                            climbersMap.put("firstname", firstname.getText().toString().toUpperCase());
                            climbersMap.put("lastname", lastname.getText().toString().toUpperCase());
                            climbersMap.put("city", city.getText().toString().toUpperCase());
                            climbersMap.put("state", state.getText().toString().toUpperCase());
                            climbersMap.put("gym", gym.getText().toString());
                            climbersMap.put("description", descrption.getText().toString());
                            climbersMap.put("image", getString(R.string.default_image_icon));
                            climbersMap.put("thumb", "default");
                            climbersMap.put("owner", "false");


                            firebaseDatabase.setValue(climbersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        progressDialog.dismiss();
                                        Log.d("Auth", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        //updateUI(user);
                                        Intent intent = new Intent(ActivitySignIn.this, ActivityMain.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.hide();
                            Log.w("Auth", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(ActivitySignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public boolean checkDataUser() {
        if (firstname.getText().toString().isEmpty())
            Toast.makeText(ActivitySignIn.this, "Falta el nombre", Toast.LENGTH_SHORT).show();
        else if (lastname.getText().toString().isEmpty())
            Toast.makeText(ActivitySignIn.this, "Falta el apellido", Toast.LENGTH_SHORT).show();
        else if (email.getText().toString().isEmpty())
            Toast.makeText(ActivitySignIn.this, "Falta el mail", Toast.LENGTH_SHORT).show();
        else if (password.getText().toString().isEmpty()) {
            if (password.getText().equals(firstname.getText().toString()) || password.getText().length() < 5) {
                Toast.makeText(ActivitySignIn.this, "La contraseña no es valida", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(ActivitySignIn.this, "Falta la contraseña", Toast.LENGTH_SHORT).show();
        } else if (city.getText().toString().isEmpty())
            Toast.makeText(ActivitySignIn.this, "Falta la ciudad", Toast.LENGTH_SHORT).show();
        else if (state.getText().toString().isEmpty())
            Toast.makeText(ActivitySignIn.this, "Falta el estado", Toast.LENGTH_SHORT).show();
        else if (descrption.getText().toString().length() > 100)
            Toast.makeText(ActivitySignIn.this, "Descripción max(100)", Toast.LENGTH_SHORT).show();
        else if (gym.getText().toString().length() < 2)
            Toast.makeText(ActivitySignIn.this, "Muro no valido", Toast.LENGTH_SHORT).show();
        else return true;
        return false;
    }

    public void saveUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("mx.iteso.USER_PREFRENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NAME", email.getText().toString());
        editor.putString("PWD", password.getText().toString());
        editor.putBoolean("LOGGED", true);
        editor.apply();
    }


}
