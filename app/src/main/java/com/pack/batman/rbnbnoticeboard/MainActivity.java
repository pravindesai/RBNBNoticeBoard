package com.pack.batman.rbnbnoticeboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    EditText emailEt,passwordEt;
    Button loginBtn;
    TextView signUp;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressDialog pd;
    void initGui()
    {
        emailEt=findViewById(R.id.emailEt);
        passwordEt=findViewById(R.id.passwordEt);
        signUp=findViewById(R.id.signUp);
        loginBtn=findViewById(R.id.loginBtn);
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("loading");

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGui();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        checkSignin();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignupActivity.class));
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                signIn();
            }
        });
    }

    public void signIn()
    {
        String email=emailEt.getText().toString().trim();
        String password=passwordEt.getText().toString().trim();
        if(email.isEmpty()||password.isEmpty())
        {
            emailEt.setError("Check your email and password !");
            emailEt.requestFocus();
            pd.dismiss();
            return;
        }


        mAuth.signInWithEmailAndPassword(email,password)
             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                user=FirebaseAuth.getInstance().getCurrentUser();
                startActivity(new Intent(MainActivity.this,VerificationActivity.class));
                finish();
                pd.dismiss();
                }
                else
                {
                    pd.dismiss();
                    emailEt.setError("Check your email and password !");
                    emailEt.requestFocus();
                    Toast.makeText(MainActivity.this, "wrong id and password", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }


    public void checkSignin()
    {
        user=mAuth.getCurrentUser();
        if(user!=null)
        {
            startActivity(new Intent(MainActivity.this,VerificationActivity.class));
            finish();
        }
    }
}
