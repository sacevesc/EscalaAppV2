package mx.iteso.escalaapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import mx.iteso.escalaapp.R;

public class ActivitySignGym extends AppCompatActivity {
    EditText name, eslogan, address, descrption;
    AutoCompleteTextView city, state;
    Button done, image_btn;


    //private FirebaseAuth mAuth;//firebase auth
    private ProgressDialog progressDialog;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_gym);

        //firebase auth
        //  mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();


        name = findViewById(R.id.sign_gym_name);
        eslogan = findViewById(R.id.sign_gym_eslogan);
        image_btn = findViewById(R.id.sign_gym_image_button);


        city = findViewById(R.id.sign_gym_city_autocomplete);
        String[] cities = getResources().getStringArray(R.array.ciudades);
        ArrayAdapter<String> adapterCities = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        city.setAdapter(adapterCities);

        state = findViewById(R.id.sign_gym_state_autocomplete);
        String[] states = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapterStates = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, states);
        state.setAdapter(adapterStates);

        address = findViewById(R.id.sign_gym_address);
        descrption = findViewById(R.id.sign_gym_description);


        done = findViewById(R.id.sign_gym_done_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkDataUser()) {
                    //saveUser();        progressDialog = new ProgressDialog(this);

                    progressDialog.setTitle("Regstering Gym");
                    progressDialog.setMessage("Please wait while your gym is being created");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    createGym();
                }
            }
        });

    }


    private void createGym() {

        final String gymId = firebaseDatabase.child("Gyms").push().getKey();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        HashMap<String, String> gymMap = new HashMap<>();
        gymMap.put("name", name.getText().toString().toUpperCase());
        gymMap.put("eslogan", eslogan.getText().toString().toUpperCase());
        gymMap.put("city", city.getText().toString().toUpperCase());
        gymMap.put("state", state.getText().toString().toUpperCase());
        gymMap.put("address", address.getText().toString());
        gymMap.put("description", descrption.getText().toString());
        gymMap.put("image", getString(R.string.default_image_icon));
        gymMap.put("thumb", "default");
        gymMap.put("owner", uid);


        firebaseDatabase.child("Gyms").child(gymId).setValue(gymMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    progressDialog.dismiss();
                    Log.d("GYM", "createGym:success");
                    //FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                    Intent intent = new Intent(ActivitySignGym.this, ActivityMain.class);
                    startActivity(intent);
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    progressDialog.hide();
                    Log.w("GYM", "CREATEgym:failure", task.getException());
                    Toast.makeText(ActivitySignGym.this, "GYM failed.",
                            Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }
            }
        });
    }


    public boolean checkDataUser() {
        if (name.getText().toString().isEmpty())
            Toast.makeText(ActivitySignGym.this, "Falta el nombre", Toast.LENGTH_SHORT).show();
        else if (city.getText().toString().isEmpty())
            Toast.makeText(ActivitySignGym.this, "Falta la ciudad", Toast.LENGTH_SHORT).show();
        else if (state.getText().toString().isEmpty())
            Toast.makeText(ActivitySignGym.this, "Falta el estado", Toast.LENGTH_SHORT).show();
        else if (descrption.getText().toString().length() > 100)
            Toast.makeText(ActivitySignGym.this, "Descripción max(100)", Toast.LENGTH_SHORT).show();
        else if (address.getText().toString().length() < 2)
            Toast.makeText(ActivitySignGym.this, "Direccion no valido", Toast.LENGTH_SHORT).show();
        else return true;
        return false;
    }


}
