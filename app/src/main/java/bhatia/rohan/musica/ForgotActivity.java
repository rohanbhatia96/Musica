package bhatia.rohan.musica;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {

    private EditText mEmail;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        mEmail = (EditText) findViewById(R.id.email);
        auth = FirebaseAuth.getInstance();
    }

    public void doReset(View view) {
        String email;
        email = mEmail.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(),"Enter email!",Toast.LENGTH_SHORT).show();
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
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful())
                            Toast.makeText(getApplicationContext(),"Reset Unsuccessful. "
                                    + task.getException(),Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(),"Reset Successful!"
                                    , Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
