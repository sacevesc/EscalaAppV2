package mx.iteso.escalaapp;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mx.iteso.escalaapp.beans.Climber;

public class ActivityEditProfile extends AppCompatActivity {
    EditText firstname, lastname, email, password, descrption;
    AutoCompleteTextView city, state, gym;
    Button done;
    DatabaseReference userDatabase;
    private FirebaseAuth mAuth;//firebase auth
    private ProgressDialog progressDialog;
    private FirebaseUser curretnUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        firstname = findViewById(R.id.sigin_firstname);
        lastname = findViewById(R.id.sigin_lastname);
        //password = findViewById(R.id.sigin_password);
        //email = findViewById(R.id.signin_email);

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

        //firebase
        mAuth = FirebaseAuth.getInstance();
        curretnUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = curretnUser.getUid();
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

                firstname.setText(climber.getFirstname());
                lastname.setText(climber.getLastname());
                gym.setText(climber.getGym());
                state.setText(climber.getState());
                city.setText(climber.getCity());
                descrption.setText(climber.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        done = findViewById(R.id.signin_done_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(ActivityEditProfile.this);
                progressDialog.setTitle("Saving changes");
                progressDialog.setMessage("Please wait while we save your changes");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                updateAccount("firstname", firstname);
                updateAccount("lastname", lastname);
                updateAccount("city", city);
                updateAccount("gym", gym);
                updateAccount("state", state);
                updateAccount("description", descrption);

            }
        });


    }

    private void updateAccount(final String update, TextView textView) {
        userDatabase.child(update).setValue(textView.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Auth", "updateUser" + update + ":success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                    Intent intent = new Intent(ActivityEditProfile.this, ActivityProfile.class);
                    startActivity(intent);
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Auth", "updateUser" + update + ":failure", task.getException());
                    Toast.makeText(ActivityEditProfile.this, "Update failed.",
                            Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }

            }
        });
    }

  /*  public boolean checkDataUser() {
        if (firstname.getText().toString().isEmpty())
            Toast.makeText(ActivityEditProfile.this, "Falta el nombre", Toast.LENGTH_SHORT).show();
        else if (lastname.getText().toString().isEmpty())
            Toast.makeText(ActivityEditProfile.this, "Falta el apellido", Toast.LENGTH_SHORT).show();
        else if (email.getText().toString().isEmpty())
            Toast.makeText(ActivityEditProfile.this, "Falta el mail", Toast.LENGTH_SHORT).show();
        else if (password.getText().toString().isEmpty()) {
            if (password.getText().equals(firstname.getText().toString()) || password.getText().length() < 5) {
                Toast.makeText(ActivityEditProfile.this, "La contraseña no es valida", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(ActivityEditProfile.this, "Falta la contraseña", Toast.LENGTH_SHORT).show();
        }else if (city.getText().toString().isEmpty())
            Toast.makeText(ActivityEditProfile.this, "Falta la ciudad", Toast.LENGTH_SHORT).show();
        else if (state.getText().toString().isEmpty())
            Toast.makeText(ActivityEditProfile.this, "Falta el estado", Toast.LENGTH_SHORT).show();
        else if (descrption.getText().toString().length() > 100)
            Toast.makeText(ActivityEditProfile.this, "Descripción max(100)", Toast.LENGTH_SHORT).show();
        else if (gym.getText().toString().length() < 2)
            Toast.makeText(ActivityEditProfile.this, "Muro no valido", Toast.LENGTH_SHORT).show();
        else return true;
        return false;
    }
*/

}
