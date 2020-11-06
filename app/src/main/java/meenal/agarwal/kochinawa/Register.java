package meenal.agarwal.kochinawa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
   private Button RegisterButton1;
    private EditText UserEmail11, UserPassword11;
private FirebaseAuth ath;
private DatabaseReference rootRef;
    private TextView AlreadyHaveAccountLink1;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
      ath=FirebaseAuth.getInstance();
      rootRef= FirebaseDatabase.getInstance().getReference();
       InitializeVariables();
        AlreadyHaveAccountLink1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterActivity();
            }
        });
        RegisterButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount()
    {
        String email= UserEmail11.getText().toString();
        String Password= UserPassword11.getText().toString();
    if(TextUtils.isEmpty((email)))
    {
       Toast toast= Toast.makeText(getApplicationContext(),"You haven't entered your MailID yet",Toast.LENGTH_SHORT);
   toast.show();
    }
        if(TextUtils.isEmpty((Password)))
        {
            Toast toast1= Toast.makeText(getApplicationContext(),"You haven't entered your Password yet",Toast.LENGTH_SHORT);
            toast1.show();
        }
else
        {
            loadingBar.setTitle("Creating new Account");
            loadingBar.setMessage("Please wait,while we are creating new account for you");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            ath.createUserWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    { String currUserID= ath.getCurrentUser().getUid();
                    rootRef.child("Users").child(currUserID).setValue("");

                        loadingBar.dismiss();
                        Toast toast1= Toast.makeText(Register.this,"Account created successfully!",Toast.LENGTH_SHORT);
                        toast1.show();
                        SendUserToMainActivity();
                    }
                    else
                    { loadingBar.dismiss();
                        String message=task.getException().toString();
                        Toast toast1= Toast.makeText(Register.this,"Error occurred...Please try again."+message,Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                }
            });
        }

    }

    private void SendUserToRegisterActivity()
    {
        Intent LoginIntent = new Intent(Register.this, Login.class);
        startActivity(LoginIntent);
    }
    private void SendUserToMainActivity()
    {
        Intent MainIntent = new Intent(Register.this, MainActivity.class);
        MainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainIntent);
        finish();
    }


    private void InitializeVariables() {

        RegisterButton1 = (Button) findViewById(R.id.RegisterButton);
        UserEmail11 = (EditText) findViewById(R.id.Register_eMail);
        AlreadyHaveAccountLink1 = (TextView) findViewById(R.id.AlredyHaveLoginAccount);
        UserPassword11 = (EditText) findViewById(R.id.RegisterPassword);
loadingBar=new ProgressDialog(this);
    }


}