package mx.iteso.escalaapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mx.iteso.escalaapp.R;

public class ActivityLogin extends AppCompatActivity {
    EditText name, password;
    Button login, signin;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.activity_login_name);
        password = findViewById(R.id.activity_login_password);
        login=findViewById(R.id.activity_login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().isEmpty() || !password.getText().toString().isEmpty()) {
                    //saveUser();
                    progressDialog.setTitle("Logging User");
                    progressDialog.setMessage("Please wait while your are logging");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    loginUser(name.getText().toString(), password.getText().toString());

                }
            }
        });
        signin = findViewById(R.id.activity_login_signin_button);
        signin.setPaintFlags(signin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        SpannableString content = new SpannableString("Sign In");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        signin.setText(content);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivitySignIn.class);
                startActivity(intent);
            }
        });

    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            Log.d("Auth", "loginWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            user.reload();
                            if(user.isEmailVerified()) {
                                Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else{
                               showDialog(user);
                            }

                        } else {
                            // If sign in fails, display a message to the user.

                            progressDialog.hide();
                            Log.w("Auth", "signIngWithEmail:failure", task.getException());
                            Toast.makeText(ActivityLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    public void showDialog(final FirebaseUser user){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityLogin.this);
        alertDialog.setMessage((CharSequence) "Verify your email");
        alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setPositiveButton("SEND EMAIL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user.sendEmailVerification();
            }
        });
        alertDialog.create();
        alertDialog.show();
    }

}
