package mx.iteso.escalaapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
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
import java.io.File;
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
    DatabaseReference userDatabase;
    private FirebaseAuth mAuth;//firebase auth
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
                climber.setImage(dataSnapshot.child("image").getValue().toString());

                firstname.setText(climber.getFirstname());
                lastname.setText(climber.getLastname());
                gym.setText(climber.getGym());
                state.setText(climber.getState());
                city.setText(climber.getCity());
                descrption.setText(climber.getDescription());

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
                updateAccount("firstname", firstname);
                updateAccount("lastname", lastname);
                updateAccount("city", city);
                updateAccount("gym", gym);
                updateAccount("state", state);
                updateAccount("description", descrption);

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

            final File thumb_file = new File(image_uri.getPath());
            // Bitmap thumb_bitmap = new Compressor(ActivityEditProfile.this).setMaxWidth(200).setMaxHeight(200).setQuality(75).compressToBitmap(thumb_file);
            Bitmap thumb_bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(thumb_file.getPath()), 200, 200);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            assert thumb_bitmap != null;
            //thumb_bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

            StorageReference filepath = mStorageRef.child("profile_images").child(userUid + ".jpg");
            final StorageReference thumb_filePath = mStorageRef.child("profile_images").child("thumbs").child(userUid + ".jpg");

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

                                UploadTask uploadTask = thumb_filePath.putBytes(thumb_byte);
                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                        String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                                        if (thumb_task.isSuccessful()) {

                                            Map updateHashmap = new HashMap();
                                            updateHashmap.put("image", downloadUrl);
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
                                        } else {
                                            Log.d("Image", "uploadThumbnail:FAILURE");
                                            progressDialog.dismiss();

                                        }

                                    }
                                });

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
