package bhatia.rohan.musica;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;
    private FirebaseAuth auth;
    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mName = (EditText) findViewById(R.id.name);
        auth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://musica-cb289.firebaseio.com/");
    }
    public void createDataBase(String name)
    {
        User user = new User(name,mEmail.getText().toString());
        String userID = auth.getCurrentUser().getUid();
        ref.child("Users").child(userID).setValue(user);
        ref.child("UserChallenges").child(userID);
    }

    public void doSignup(View view) {
        final String email, password, name;
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();
        name = mName.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(email.contains("gmail"));
        else if(email.contains("ymail"));
        else if(email.contains("yahoo"));
        else if(email.contains("hotmail"));
        else if (email.contains("in.com"));
        else if (email.contains("rediff"));
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Invalid Email Address!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(email.contains(".co"));
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Invalid Email Address!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(),
                    "Password too short, enter minimum 6 characters!",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(bhatia.rohan.musica.SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this,
                                    "Sign Up Successful!",
                                    Toast.LENGTH_SHORT).show();

                            createDataBase(name);


                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this,
                                    "Authentication Failed. " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }




}
