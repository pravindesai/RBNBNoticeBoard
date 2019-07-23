package com.pack.batman.rbnbnoticeboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    EditText emailEt,passwordEt,phoneEt,rollEt,nameEt;
    Button signUpBtn;
    TextView logIn;
    Spinner classSpinner;
    String className,Email,password,Dept,roll,name,phone;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference Dref=FirebaseDatabase.getInstance().getReference("Users");
    ProgressDialog pd;
    void initGui()
    {
        emailEt=findViewById(R.id.emailEt);
        passwordEt=findViewById(R.id.passwordEt);
        logIn=findViewById(R.id.logIn);
        signUpBtn=findViewById(R.id.signUpBtn);
        phoneEt=findViewById(R.id.phoneEt);
        rollEt=findViewById(R.id.rollEt);
        nameEt=findViewById(R.id.nameEt);
        classSpinner=findViewById(R.id.classSpinner);
        pd = new ProgressDialog(SignupActivity.this); pd.setMessage("loading");

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initGui();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,MainActivity.class));
                finish();
            }
        });


        final String[] Department={"11th Sci.","12th Sci.","11th Arts","12th Arts",
                                    "BCS","MCS","BA","MA","Bsc","Msc"};
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, Department);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*Toast.makeText(SignupActivity.this, "pos:"+i+"\n"+
                        Department[i], Toast.LENGTH_SHORT).show();*/
                        className=Department[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                createUser();
            }
        });
    }


    public void createUser()
    {
         Email=emailEt.getText().toString().trim();
         password=passwordEt.getText().toString().trim();
         Dept=className;
         name=nameEt.getText().toString().trim();
         phone=phoneEt.getText().toString().trim();
         roll=rollEt.getText().toString().trim();

        if(Email.isEmpty()||password.isEmpty()||Dept.isEmpty()||name.isEmpty()||phone.isEmpty()||roll.isEmpty())
        {
            Toast.makeText(SignupActivity.this, "invalid details", Toast.LENGTH_SHORT).show();
            emailEt.setError("Check your email and password name !");
            emailEt.requestFocus();
            pd.dismiss();
            return;
        }
        if(phone.length()<10||phone.length()>12)
        {
            phoneEt.setError("wrong mobile number");
            phoneEt.requestFocus();
            pd.dismiss();
            return;
        }

        mAuth.createUserWithEmailAndPassword(Email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        user=mAuth.getCurrentUser();
                        if(user!=null)
                        {
                            String id=Dref.push().getKey();
                            UserDetails userdetail=new UserDetails(id,name,phone,Email,className,roll);
                            Dref.child(id).setValue(userdetail);
                            sendVerificationMail();
                            startActivity(new Intent(SignupActivity.this,VerificationActivity.class));
                            finish();
                            pd.dismiss();
                        }
                        else
                        {
                        emailEt.setError("Check your email and password name !");
                        emailEt.requestFocus();
                        pd.dismiss();
                        Toast.makeText(SignupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        return;
                        }
                    }
                });
    }


    public void sendVerificationMail()
    {
        if(user!=null)
        {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(SignupActivity.this, "Verificaction mail sent", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
