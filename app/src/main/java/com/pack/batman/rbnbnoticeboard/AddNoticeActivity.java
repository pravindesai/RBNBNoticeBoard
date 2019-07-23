package com.pack.batman.rbnbnoticeboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class AddNoticeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    EditText titleEt,noticeEt;
    Button publishBtn,cancelBtn;
    String supername,superdept,Currentuser,usermail,dept,date,noticeTitle,noticetext;
    ProgressDialog pd;
    public void initGui()
    {
        titleEt=findViewById(R.id.titleEt);
        noticeEt=findViewById(R.id.noticeEt);
        publishBtn=findViewById(R.id.publishBtn);
        cancelBtn=findViewById(R.id.cancelBtn);
        pd = new ProgressDialog(AddNoticeActivity.this); pd.setMessage("loading");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);
        initGui();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
               getDetails();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddNoticeActivity.this,ProfileActivity.class));
                finish();
            }
        });
    }


    public void getDetails()
    {
        DatabaseReference Dref=FirebaseDatabase.getInstance().getReference("Users");
        Query query=Dref.orderByChild("email").equalTo(user.getEmail());


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                        for (DataSnapshot Ds:dataSnapshot.getChildren())
                        {
                            UserDetails uDetails=Ds.getValue(UserDetails.class);
                            supername=uDetails.getName();
                            superdept=uDetails.getDept();
                            uploadNotice();
                        }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void uploadNotice()
    {
        Currentuser=supername;
        dept=superdept;
        usermail=user.getEmail();
        date=DateFormat.getDateInstance().format(new Date());
        noticeTitle=titleEt.getText().toString();
        noticetext=noticeEt.getText().toString();
        if(noticeTitle.isEmpty()||noticetext.isEmpty())
        {
            Toast.makeText(AddNoticeActivity.this, "Fields can not be empty", Toast.LENGTH_SHORT).show();
           pd.dismiss();
            return;
        }

        DatabaseReference Dref=FirebaseDatabase.getInstance().getReference("Notice");
        String id=Dref.push().getKey();
        Notice notice=new Notice(id,Currentuser,usermail,dept,date,noticeTitle,noticetext);
        Dref.child(id).setValue(notice);

        Toast.makeText(this, "Notice Uploaded..", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddNoticeActivity.this,ProfileActivity.class));
        finish();
        pd.dismiss();
    }


}
