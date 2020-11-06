package meenal.agarwal.kochinawa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLogin extends AppCompatActivity {
private Button verify;
    private Button verification_code;
    private EditText phoneNumber;
    private EditText code;
    private ProgressDialog loading;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
       mAuth =FirebaseAuth.getInstance();
        verification_code=(Button)findViewById(R.id.verifyButton);
        phoneNumber=(EditText) findViewById(R.id.phone_number);
        verify=(Button)findViewById(R.id.verifies_Button);
        loading=new ProgressDialog(this);
        code=(EditText)findViewById(R.id.phone_number_verification);
        verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           String phoneNum=phoneNumber.getText().toString();
           if(TextUtils.isEmpty(phoneNum))
           {
               Toast toast1= Toast.makeText(PhoneLogin.this,"Phone number not entered :/",Toast.LENGTH_SHORT);
               toast1.show();
           }
           else
           {loading.setTitle("Phone Verification");
           loading.setMessage("Please wait for your verification code");
           loading.setCanceledOnTouchOutside(false);
           loading.show();
               PhoneAuthProvider.getInstance().verifyPhoneNumber(
                       phoneNum,        // Phone number to verify
                       60,                 // Timeout duration
                       TimeUnit.SECONDS,   // Unit of timeout
                       PhoneLogin.this,               // Activity (for callback binding)
                       callbacks);        // OnVerificationStateChangedCallbacks
           }
            }

        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verification_code.setVisibility(View.INVISIBLE);
                phoneNumber.setVisibility(View.INVISIBLE);

                String verficationCode=code.getText().toString();
                if(TextUtils.isEmpty(verficationCode))
                {
                    Toast toast1= Toast.makeText(PhoneLogin.this,"Please enter the code first :)",Toast.LENGTH_SHORT);
                    toast1.show();
                }

else
                {
                    loading.setTitle("Code Verification");
                    loading.setMessage("Please wait while we are verifying your account");
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verficationCode);
                    signInWithPhoneAuthCredential(credential);
                }


            }
        });
       callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {loading.dismiss();
                Toast toast1= Toast.makeText(PhoneLogin.this,"You have entered wrong info:/",Toast.LENGTH_SHORT);
                toast1.show();
                verification_code.setVisibility(View.VISIBLE);
                phoneNumber.setVisibility(View.VISIBLE);
                verify.setVisibility(View.INVISIBLE);
                code.setVisibility(View.INVISIBLE);
            }
           @Override
           public void onCodeSent(@NonNull String verificationId,
                                  @NonNull PhoneAuthProvider.ForceResendingToken token) {


               // Save verification ID and resending token so we can use them later
               mVerificationId = verificationId;
               mResendToken = token;
               loading.dismiss();
               Toast toast1= Toast.makeText(PhoneLogin.this,"Please check your inbox. Verification code has been sent :)",Toast.LENGTH_SHORT);
               toast1.show();
               verification_code.setVisibility(View.INVISIBLE);
               phoneNumber.setVisibility(View.INVISIBLE);
               verify.setVisibility(View.VISIBLE);
               code.setVisibility(View.VISIBLE);

           }
        };
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
loading.dismiss();
                            Toast toast1= Toast.makeText(PhoneLogin.this,"Congratulations you are logged in successfully XDXD",Toast.LENGTH_SHORT);
                            toast1.show();
                            SendUserToMainActivity();
                        } else
                            {
String message=task.getException().toString();
                                Toast toast1= Toast.makeText(PhoneLogin.this,"Error :("+message,Toast.LENGTH_SHORT);
                                toast1.show();
                        }
                    }
                });
    }

    private void SendUserToMainActivity()
    {
        Intent RegisterIntent=new Intent(PhoneLogin.this,MainActivity.class);
        startActivity(RegisterIntent);
        finish();
    }
}
