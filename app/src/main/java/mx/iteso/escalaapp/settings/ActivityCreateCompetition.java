package mx.iteso.escalaapp.settings;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.iteso.escalaapp.R;
import mx.iteso.escalaapp.activities.ActivityCompetition;
import mx.iteso.escalaapp.beans.Climber;

public class ActivityCreateCompetition extends AppCompatActivity {

    private static final int GALLERY_PICK = 1;
    EditText compName, description, cat1, cat2, cat3, cat4, cat5;
    Spinner numberRounds, numberCat, qualifications, semifinals, finals, superfinals, day, month, year, judgeSpinner;
    LinearLayout qual, semi, fina, superf, ca1, ca2, ca3, ca4, ca5;
    SimpleDraweeView draweeView;
    Button done, image_btn, addJudge;
    Uri image_comp_id;
    private ProgressDialog progressDialog;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser currentUser;
    private StorageReference mStorageRef;
    private byte[] datas;
    ArrayList<Climber> judges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_competition);

        progressDialog = new ProgressDialog(this);

        //referencia para obtener los datos de una instancia
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        //storage donde estan las imagenes
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        judgeSpinner = findViewById(R.id.activity_judgeSpinner);
        addJudge = findViewById(R.id.add_judge);
        judges = new ArrayList<>();

        //edittext
        compName = findViewById(R.id.newcomp_name);
        description = findViewById(R.id.newcomp_description);
        image_btn = findViewById(R.id.newcomp_image_btn);
        draweeView = findViewById(R.id.newcomp_image);
        //category names
        cat1 = findViewById(R.id.newcomp_category_one);
        cat2 = findViewById(R.id.newcomp_category_two);
        cat3 = findViewById(R.id.newcomp_category_three);
        cat4 = findViewById(R.id.newcomp_category_four);
        cat5 = findViewById(R.id.newcomp_category_five);
        // numero de categorias
        numberCat = findViewById(R.id.newcomp_no_categorys);
        numberCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onCategoriesSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        numberRounds = findViewById(R.id.newcomp_no_rounds_spinner);
        numberRounds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onRoundSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //activar o desactivar linearlayouts de rondas
        qual = findViewById(R.id.linear_qualifications);
        semi = findViewById(R.id.linear_semifinals);
        fina = findViewById(R.id.linear_finals);
        superf = findViewById(R.id.linear_superfinals);
        ca1 = findViewById(R.id.linear_category_one);
        ca2 = findViewById(R.id.linear_category_two);
        ca3 = findViewById(R.id.linear_category_three);
        ca4 = findViewById(R.id.linear_category_four);
        ca5 = findViewById(R.id.linear_category_five);

        qualifications = findViewById(R.id.newcomp_boulder_per_round_qualifications);
        semifinals = findViewById(R.id.newcomp_boulder_per_round_semifinal);
        finals = findViewById(R.id.newcomp_boulder_per_round_finals);
        superfinals = findViewById(R.id.newcomp_boulder_per_round_superfinals);

        day = findViewById(R.id.newcomp_day);
        day.setMinimumHeight(5);
        month = findViewById(R.id.newcomp_month);
        year = findViewById(R.id.newcomp_year);

        done = findViewById(R.id.newcomp_done_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkDataUser()) {
                    //saveUser();        progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Creating competition");
                    progressDialog.setMessage("Please wait while the competition is being created");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    createCompetition();
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

        Query climbersDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers");
        climbersDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                int selection = judgeSpinner.getSelectedItemPosition();
                ArrayList<Climber> climberJudge = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Climber climber = postSnapshot.getValue(Climber.class);
                    climber.setKey(postSnapshot.getKey());
                    climberJudge.add(climber);
                }
                ArrayAdapter<Climber> adapter = new ArrayAdapter<Climber>(getApplicationContext(), android.R.layout.simple_spinner_item, climberJudge);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                judgeSpinner.setAdapter(adapter);
                judgeSpinner.setSelection(selection);
            }

            public void onCancelled(DatabaseError databaseError) {
                Log.w("Gymslist", "loadPost:onCancelled", databaseError.toException());
            }
        });

        addJudge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judges.add((Climber) judgeSpinner.getSelectedItem());
            }
        });


    }


    public void onRoundSelected(int position) {
        switch (position) {
            case 0:
                qual.setVisibility(View.VISIBLE);
                semi.setVisibility(View.GONE);
                fina.setVisibility(View.GONE);
                superf.setVisibility(View.GONE);
                break;
            case 1:
                qual.setVisibility(View.VISIBLE);
                semi.setVisibility(View.GONE);
                fina.setVisibility(View.VISIBLE);
                superf.setVisibility(View.GONE);
                break;
            case 2:
                qual.setVisibility(View.VISIBLE);
                semi.setVisibility(View.VISIBLE);
                fina.setVisibility(View.VISIBLE);
                superf.setVisibility(View.GONE);
                break;
            default:
                qual.setVisibility(View.VISIBLE);
                semi.setVisibility(View.VISIBLE);
                fina.setVisibility(View.VISIBLE);
                superf.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void onCategoriesSelected(int position) {
        switch (position) {
            case 0:
                ca1.setVisibility(View.VISIBLE);
                ca2.setVisibility(View.GONE);
                ca3.setVisibility(View.GONE);
                ca4.setVisibility(View.GONE);
                ca5.setVisibility(View.GONE);
                break;
            case 1:
                ca1.setVisibility(View.VISIBLE);
                ca2.setVisibility(View.VISIBLE);
                ca3.setVisibility(View.GONE);
                ca4.setVisibility(View.GONE);
                ca5.setVisibility(View.GONE);
                break;
            case 2:
                ca1.setVisibility(View.VISIBLE);
                ca2.setVisibility(View.VISIBLE);
                ca3.setVisibility(View.VISIBLE);
                ca4.setVisibility(View.GONE);
                ca5.setVisibility(View.GONE);
                break;
            case 3:
                ca1.setVisibility(View.VISIBLE);
                ca2.setVisibility(View.VISIBLE);
                ca3.setVisibility(View.VISIBLE);
                ca4.setVisibility(View.VISIBLE);
                ca5.setVisibility(View.GONE);
                break;
            default:
                ca1.setVisibility(View.VISIBLE);
                ca2.setVisibility(View.VISIBLE);
                ca3.setVisibility(View.VISIBLE);
                ca4.setVisibility(View.VISIBLE);
                ca5.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            final Uri image_uri = data.getData();
            image_comp_id = image_uri;
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

    private void createCompetition() {

        final String competitionId = firebaseDatabase.child("Competitions").push().getKey();
        String uid = currentUser.getUid();
        HashMap<String, String> compMap = new HashMap<>();
        compMap.put("name", compName.getText().toString().toUpperCase());
        compMap.put("day", day.getSelectedItem().toString());
        compMap.put("month", month.getSelectedItem().toString());
        compMap.put("year", year.getSelectedItem().toString());
        compMap.put("gym", "default");
        compMap.put("owner", uid);
        compMap.put("qualifications", qualifications.getSelectedItem().toString());
        compMap.put("semifinals", semifinals.getSelectedItem().toString());
        compMap.put("finals", finals.getSelectedItem().toString());
        compMap.put("superfinals", qualifications.getSelectedItem().toString());
        compMap.put("no_rounds", String.valueOf(numberRounds.getSelectedItemPosition() + 1));
        compMap.put("no_categories", String.valueOf(numberCat.getSelectedItemPosition() + 1));
        compMap.put("description", description.getText().toString());
        compMap.put("image", getString(R.string.default_image_comp));
        compMap.put("thumb", "default");
        compMap.put("participants", "0");
        compMap.put("date", year.getSelectedItem().toString() + (((month.getSelectedItemPosition() + 1) < 10)? "0" + (month.getSelectedItemPosition() + 1) : (month.getSelectedItemPosition() + 1))  + day.getSelectedItem().toString());

        DatabaseReference climbersDatabase = FirebaseDatabase.getInstance().getReference().child("Climbers").child(((Climber)judgeSpinner.getSelectedItem()).getKey()).child("judge").child(competitionId);
        climbersDatabase.setValue("true");


        firebaseDatabase.child("Competitions").child(competitionId).setValue(compMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    progressDialog.dismiss();
                    Log.d("COMP", "createCOMP:success");
                    if (image_comp_id != null)
                        updateImageComp(competitionId);
                    Intent intent = new Intent(ActivityCreateCompetition.this, ActivityCompetition.class);
                    intent.putExtra("comp_id", competitionId);
                    startActivity(intent);
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    progressDialog.hide();
                    Log.w("COMP", "createCOMP:failure", task.getException());
                    Toast.makeText(ActivityCreateCompetition.this, "Competition creation failed.", Toast.LENGTH_SHORT).show();
                }
            }

            ;
        });
    }


    public void updateImageComp(final String compId) {
        StorageReference filepath = mStorageRef.child("competition_images").child(compId + ".jpg");
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Competitions").child(compId);

        filepath.putFile(image_comp_id).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    final String downloadUrl = task.getResult().getDownloadUrl().toString();

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


        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Competitions").child(compId);
        final StorageReference thumb_filePath = mStorageRef.child("competition_images").child("thumbs").child(compId + ".jpg");

        UploadTask uploadTask = thumb_filePath.putBytes(datas);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

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
        if (compName.getText().toString().isEmpty())
            Toast.makeText(ActivityCreateCompetition.this, "Falta el nombre", Toast.LENGTH_SHORT).show();
        else if (description.getText().toString().length() > 100)
            Toast.makeText(ActivityCreateCompetition.this, "Descripción max(100)", Toast.LENGTH_SHORT).show();
        else return true;
        return false;
    }

}
