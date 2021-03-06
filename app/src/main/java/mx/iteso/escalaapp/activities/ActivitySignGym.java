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
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mx.iteso.escalaapp.R;

public class ActivitySignGym extends AppCompatActivity {
    EditText name, address, descrption, phone;
    AutoCompleteTextView city, state;
    private static final int GALLERY_PICK = 1;
    Button done, image_btn;
    SimpleDraweeView draweeView;


    //private FirebaseAuth mAuth;//firebase auth
    private ProgressDialog progressDialog;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser currentUser;
    private StorageReference mStorageRef;
    private Uri image_gym_id;
    private byte[] datas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_gym);

        //firebase auth
        //  mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        name = findViewById(R.id.sign_gym_name);
        image_btn = findViewById(R.id.sign_gym_image_button);
        draweeView = findViewById(R.id.sign_gym_image);
        phone = findViewById(R.id.sign_gym_phone);


        city = findViewById(R.id.sign_gym_city_autocomplete);
        String[] cities = getResources().getStringArray(R.array.ciudades);
        ArrayAdapter<String> adapterCities = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
        city.setAdapter(adapterCities);

        state = findViewById(R.id.sign_gym_state_autocomplete);
        String[] states = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapterStates = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, states);
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
            image_gym_id = image_uri;
            String userUid = currentUser.getUid();
            draweeView.setImageURI(image_uri);

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
            datas = baos.toByteArray();
        }
    }


    private void createGym() {

        final String gymId = firebaseDatabase.child("Gyms").push().getKey();
        String uid = currentUser.getUid();
        HashMap<String, String> gymMap = new HashMap<>();
        gymMap.put("name", name.getText().toString().toUpperCase());
        gymMap.put("phone", phone.getText().toString());
        gymMap.put("city", city.getText().toString().toUpperCase());
        gymMap.put("state", state.getText().toString().toUpperCase());
        gymMap.put("address", address.getText().toString());
        gymMap.put("description", descrption.getText().toString());
        gymMap.put("image", getString(R.string.default_image_gym));
        gymMap.put("thumb", "default");
        gymMap.put("podiums", "0");
        gymMap.put("members", "0");
        gymMap.put("host", "0");
        gymMap.put("owner", uid);


        firebaseDatabase.child("Gyms").child(gymId).setValue(gymMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    progressDialog.dismiss();
                    Log.d("GYM", "createGym:success");
                    if (image_gym_id != null)
                        updateImageGym(gymId);

                    Intent intent = new Intent(ActivitySignGym.this, ActivityGym.class);
                    intent.putExtra("gym_id", gymId);
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

        Map addGymClimber = new HashMap();
        addGymClimber.put("owner", "true");
        addGymClimber.put("gymOwner", gymId);
        firebaseDatabase.child("Climbers").child(uid).updateChildren(addGymClimber).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    progressDialog.dismiss();
                    Log.d("ClimberCreationGym", "owner:success");
                } else {
                    // If sign in fails, display a message to the user.
                    progressDialog.hide();
                    Log.w("GYM", "CREATEgym:failure", task.getException());
                    Toast.makeText(ActivitySignGym.this, "Owner failed.",
                            Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }
            }

        });


    }


    public void updateImageGym(final String gymId) {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Gyms").child(gymId);
        final StorageReference filepath = mStorageRef.child("gym_images").child(gymId + ".jpg");
        UploadTask uploadTask = filepath.putFile(image_gym_id);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    final String downloadUrl = task.getResult().toString();

                    Map updateHashmap = new HashMap();
                    updateHashmap.put("image", downloadUrl);

                    firebaseDatabase.updateChildren(updateHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
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

        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Gyms").child(gymId);
        final StorageReference thumb_filePath = mStorageRef.child("gym_images").child("thumbs").child(gymId + ".jpg");

        uploadTask = thumb_filePath.putBytes(datas);
        Task<Uri> urlTask2 = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return thumb_filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> thumb_task) {
                String thumb_downloadUrl = thumb_task.getResult().toString();

                if (thumb_task.isSuccessful()) {
                    Map updateHashmap = new HashMap();
                    updateHashmap.put("thumb", thumb_downloadUrl);

                    firebaseDatabase.updateChildren(updateHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public boolean checkDataUser() {
        if (name.getText().toString().isEmpty())
            Toast.makeText(ActivitySignGym.this, "Falta el nombre", Toast.LENGTH_SHORT).show();
      /*  else if (city.getText().toString().isEmpty())
            Toast.makeText(ActivitySignGym.this, "Falta la ciudad", Toast.LENGTH_SHORT).show();
        else if (state.getText().toString().isEmpty())
            Toast.makeText(ActivitySignGym.this, "Falta el estado", Toast.LENGTH_SHORT).show();
        else if (descrption.getText().toString().length() > 100)
            Toast.makeText(ActivitySignGym.this, "Descripción max(100)", Toast.LENGTH_SHORT).show();
        else if (address.getText().toString().length() < 2)
            Toast.makeText(ActivitySignGym.this, "Direccion no valido", Toast.LENGTH_SHORT).show();
       */
        else return true;
        return false;
    }


}
