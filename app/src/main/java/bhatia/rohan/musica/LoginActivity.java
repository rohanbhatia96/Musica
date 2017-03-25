package bhatia.rohan.musica;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private FirebaseAuth auth;
    Firebase ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);

    }

    public void gotoSignup(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    public void doForgotPassword(View view) {
        startActivity(new Intent(LoginActivity.this,ForgotActivity.class));
    }

    public void doSignin(View view) {
        String email, password;
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Incorrect password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.contains("gmail")) ;
        else if (email.contains("ymail")) ;
        else if (email.contains("yahoo")) ;
        else if (email.contains("hotmail")) ;
        else if (email.contains("in.com")) ;
        else if (email.contains("rediff")) ;
        else {
            Toast.makeText(getApplicationContext(),
                    "Invalid Email Address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.contains(".co")) ;
        else {
            Toast.makeText(getApplicationContext(),
                    "Invalid Email Address!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful())
                                    Toast.makeText(getApplicationContext(), "Sign In Failed. "
                                            + task.getException(), Toast.LENGTH_LONG).show();
                                else {
                                    Toast.makeText(getApplicationContext(),"Signed In.",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                }
                            }
                        });

    }
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
