package com.pack.batman.rbnbnoticeboard;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotifyUserForNotice extends Service {
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference DrefUser;
    DatabaseReference DrefNotice;
    String userDept,userName;

    //Constructor
    public NotifyUserForNotice() {

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        DrefUser=FirebaseDatabase.getInstance().getReference("Users");
        DrefNotice=FirebaseDatabase.getInstance().getReference("Users");

        FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("email").equalTo(user.getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            for(DataSnapshot snapshot:dataSnapshot.getChildren())
                            {
                                UserDetails userDetails=snapshot.getValue(UserDetails.class);
                                userName=userDetails.getName();
                                userDept=userDetails.getDept();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            FirebaseDatabase.getInstance().getReference("Notice")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.exists())
                            {
                                Notice notice=dataSnapshot.getValue(Notice.class);
                                if(notice.getDept().equals(userDept)){
                                    makeNotification();
                                }
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        return START_STICKY;
    }

    private void makeNotification() {
            Notification.Builder notification = new Notification.Builder(this);
            notification.setAutoCancel(true);
            notification.setSmallIcon(R.mipmap.ic_icon2);
            notification.setTicker("RBNB");
            notification.setWhen(System.currentTimeMillis());
            notification.setContentTitle("RBNB NoticeBoard");
            notification.setContentText("Hey "+userName+" new Notice for "+userDept+"");

            Intent intent=new Intent(this,MainActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingIntent);

            NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0,notification.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
