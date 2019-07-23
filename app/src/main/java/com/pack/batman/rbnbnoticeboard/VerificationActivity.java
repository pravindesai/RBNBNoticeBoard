package com.pack.batman.rbnbnoticeboard;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificationActivity extends AppCompatActivity {
    TextView textView;
    Button cntBtn;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressDialog pd;

    public void initGui()
    {
        textView=findViewById(R.id.textView);
        cntBtn=findViewById(R.id.cntBtn);
        pd = new ProgressDialog(VerificationActivity.this); pd.setMessage("loading");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        initGui();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        checkVerified();

        cntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                user.reload();
                checkVerified();
            }
        });
    }

    public void checkVerified()
    {
        if(user.isEmailVerified())
        {
            startActivity(new Intent(VerificationActivity.this,ProfileActivity.class));
            finish();
            pd.dismiss();
        }else
        {
            textView.setText(" "+user.getEmail()+"\n Click on Verification link from  " +
                                    "noreply@rbnbnoticeboard in your inbox"+"\nand press continue");
            pd.dismiss();
        }
    }

}
