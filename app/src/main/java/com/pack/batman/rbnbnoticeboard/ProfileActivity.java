package com.pack.batman.rbnbnoticeboard;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;

    RecyclerView NoticeListView;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration itemDecoration;
    ArrayList date,name,title,text;
    ImageButton imgButton;
    ProgressDialog pd;

    String dept = "";
    String studentName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgButton=(ImageButton)findViewById(R.id.imgButton);
        date=new ArrayList();
        name=new ArrayList();
        title=new ArrayList();
        text=new ArrayList();
        pd = new ProgressDialog(ProfileActivity.this); pd.setMessage("loading"); pd.show();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        startService(new Intent(this,NotifyUserForNotice.class));

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,AddNoticeActivity.class));
            }
        });

        setCurUser();
        pd.dismiss();
     }

     public void setCurUser()
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
                         String name=uDetails.getName();
                         String Dept=uDetails.getDept();
                         dept = Dept;
                         studentName = name;
                         ShowNotice(name,Dept);
                     }
                 }
             }
             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 }
         });
     }

     public void ShowNotice(String username,String Department)
     {
         DatabaseReference Ref=FirebaseDatabase.getInstance().getReference("Notice");
         Query query=Ref.orderByChild("dept").equalTo(Department);
         query.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(dataSnapshot.exists())
                 {
                     for (DataSnapshot Ds:dataSnapshot.getChildren())
                     {
                         Notice notice=Ds.getValue(Notice.class);

                         date.add(0,notice.getDate());
                         name.add(0,notice.getUser());
                         title.add(0,notice.getNoticeTitle());
                         text.add(0,notice.getNoticetext());

                         NoticeListView=findViewById(R.id.NoticeListView);
                         NoticeListView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
                         NoticeListView.setAdapter(new ProgrammingAdapter(
                                 date,name,title,text
                         ));

                         layoutManager=new LinearLayoutManager(ProfileActivity.this);
                         itemDecoration=new DividerItemDecoration(ProfileActivity.this,((LinearLayoutManager) layoutManager).getOrientation());
                         NoticeListView.addItemDecoration(itemDecoration);
                     }
                 }
                 pd.dismiss();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();
             }
         });

     }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.Add:
                startActivity(new Intent(ProfileActivity.this,AddNoticeActivity.class));
                break;
            case R.id.Refresh:
                pd.show();
                ShowNotice(studentName, dept);

                break;

            case R.id.About:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("This app provides you Daily Weekly " +
                        "and Monthly notices and updates of your specific " +
                        "department, Hence saves your time and paper.")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, id1) -> {
                            dialog.dismiss();
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.Logout:
                mAuth.signOut();
                stopService(new Intent(this,NotifyUserForNotice.class));
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                finish();
                break;
        }
        return true;
    }

}
