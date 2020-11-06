package meenal.agarwal.kochinawa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private ImageView im;
    private FirebaseAuth ath;
    private ProgressDialog loadingBar;
    private Button LoginButton1,PhoneLoginButton1;
    private EditText UserEmail1,UserPassword1;
            private TextView NeedNewAccountLink1,ForgetPassword1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ath=FirebaseAuth.getInstance();
       InitializeVariables();
        NeedNewAccountLink1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterActivity();
            }
        });
        LoginButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });
    PhoneLoginButton1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent phoneLoginIntent=new Intent(Login.this,PhoneLogin.class);
            startActivity(phoneLoginIntent);
        }
    });
    }

    private void AllowUserToLogin()
    {
        String email= UserEmail1.getText().toString();
        String Password= UserPassword1.getText().toString();
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
        else {
            loadingBar.setTitle("Logging into your Account");
            loadingBar.setMessage("Please wait,while we are logging you in...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
ath.signInWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task)
    {//on complete listsner are used to check whether the task is successfull or not?
        if(task.isSuccessful())
        {
            loadingBar.dismiss();
            Toast toast1= Toast.makeText(Login.this,"Logged in successfully!",Toast.LENGTH_SHORT);
            toast1.show();
            SendUserToMainActivity();
        }
        else
        { loadingBar.dismiss();
            String message=task.getException().toString();
            Toast toast1= Toast.makeText(Login.this,"Error occurred...Please try again."+message,Toast.LENGTH_SHORT);
            toast1.show();
        }
    }
});
        }

    }

    private void InitializeVariables()
    {
        LoginButton1=(Button)findViewById(R.id.LoginButton);
        PhoneLoginButton1=(Button)findViewById(R.id.PhoneButton);
        UserEmail1=(EditText)findViewById(R.id.Login_eMail);
        im=(ImageView)findViewById(R.id.Login_Image);
        UserPassword1=(EditText)findViewById(R.id.Password);
        NeedNewAccountLink1=(TextView)findViewById(R.id.NewAccount);
        ForgetPassword1=(TextView)findViewById(R.id.ForgotPassword);
        loadingBar=new ProgressDialog(this);

    }



    private void SendUserToMainActivity()
    {
        Intent MainIntent = new Intent(Login .this, MainActivity.class);
        MainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainIntent);
        finish();
    }
    private void SendUserToRegisterActivity()
    {
        Intent RegisterIntent=new Intent(Login.this,Register.class);
        startActivity(RegisterIntent);
    }

}
