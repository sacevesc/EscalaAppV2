package mx.iteso.escalaapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.beans.Climber;

public class ActivityEditProfile extends AppCompatActivity {
    private static final int GALLERY_PICK = 1;
    EditText firstname, lastname, email, password, descrption;
    AutoCompleteTextView city, state, gym;
    Button done, image_btn;
    SimpleDraweeView draweeView;
    Spinner categorySpinner;
    byte[] datas;

    DatabaseReference userDatabase;
    private ProgressDialog progressDialog;
    private FirebaseUser curretnUser;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        firstname = findViewById(R.id.sigin_firstname);
        lastname = findViewById(R.id.sigin_lastname);
        image_btn = findViewById(R.id.signin_image_button);
        draweeView = findViewById(R.id.signin_image);
        categorySpinner = findViewById(R.id.signin_category_spinner);
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
        //image
        mStorageRef = FirebaseStorage.getInstance().getReference();


        //data user
        curretnUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUid = curretnUser.getUid();
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
                climber.setCategory(dataSnapshot.child("category").getValue().toString());

                firstname.setText(climber.getFirstname());
                lastname.setText(climber.getLastname());
                gym.setText(climber.getGym());
                state.setText(climber.getState());
                city.setText(climber.getCity());
                descrption.setText(climber.getDescription());

                for (int i = 0; i < categorySpinner.getCount(); i++) {
                    if (climber.getCategory().contentEquals(categorySpinner.getItemAtPosition(i).toString())) {
                        categorySpinner.setSelection(i);
                    }
                }

                Uri imageUri = Uri.parse(climber.getImage());
                draweeView.setImageURI(imageUri);

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


                Map editClimber = new HashMap();
                editClimber.put("firstname", firstname.getText().toString());
                editClimber.put("lastname", lastname.getText().toString());
                editClimber.put("city", city.getText().toString());
                editClimber.put("gym", gym.getText().toString());
                editClimber.put("state", state.getText().toString());
                editClimber.put("description", descrption.getText().toString());
                editClimber.put("category", categorySpinner.getSelectedItem().toString());

                userDatabase.updateChildren(editClimber).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            Log.d("ClimberEditProfile", "edit:success");
                            Intent intent = new Intent(ActivityEditProfile.this, ActivityProfile.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.hide();
                            Log.w("GYM", "CREATEgym:failure", task.getException());
                            Toast.makeText(ActivityEditProfile.this, "Edit failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });

                finish();


            }
        });

        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            final Uri image_uri = data.getData();
            String userUid = curretnUser.getUid();

            StorageReference filepath = mStorageRef.child("profile_images").child(userUid + ".jpg");

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Loading Image");
            progressDialog.setMessage("Please wait while your image is being saved");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            filepath.putFile(image_uri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                final String downloadUrl = task.getResult().getDownloadUrl().toString();
                                draweeView.setImageURI(image_uri);
                                Map updateHashmap = new HashMap();
                                updateHashmap.put("image", downloadUrl);
                                userDatabase.updateChildren(updateHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Log.d("Image", "uploadImage:success");
                                        }
                                    }
                                });
                            } else {
                                Log.d("Image", "uploadImage:FAILURE");
                                progressDialog.dismiss();
                            }
                        }
                    });

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
            datas = baos.toByteArray();
            final StorageReference thumb_filePath = mStorageRef.child("profile_images").child("thumbs").child(userUid + ".jpg");

            UploadTask uploadTask = thumb_filePath.putBytes(datas);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                    if (thumb_task.isSuccessful()) {
                        Map updateHashmap = new HashMap();
                        updateHashmap.put("thumb", thumb_downloadUrl);

                        userDatabase.updateChildren(updateHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Log.d("Image", "uploadImage:success");
                                }
                            }
                        });
                        progressDialog.dismiss();
                        Log.d("Image", "uploadImage:success");

                    } else {
                        Log.d("Image", "uploadImage:FAILURE");
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }


    private void updateAccount(final String update, TextView textView) {
        userDatabase.child(update).setValue(textView.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Auth", "updateUser" + update + ":success");
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Auth", "updateUser" + update + ":failure", task.getException());
                    Toast.makeText(ActivityEditProfile.this, "Update failed.",
                            Toast.LENGTH_SHORT).show();
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
