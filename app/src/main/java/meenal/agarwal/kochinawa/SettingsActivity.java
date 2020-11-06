package meenal.agarwal.kochinawa;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
private Button UpdateAccountSettings;
private String currentUserID;
private FirebaseAuth ath;
private EditText userName,userStatus;
private DatabaseReference rootRef;
private StorageReference UserProfImage;
private ProgressBar loadingBar;
private CircleImageView userProfileImage;
private  static  final int Galler=1;
    private DialogFragment LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        InitializeVariables();
        userName.setVisibility(View.INVISIBLE);
        ath = FirebaseAuth.getInstance();
        currentUserID = ath.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        UserProfImage= FirebaseStorage.getInstance().getReference().child("Profile Images");
        UpdateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
   UpdateSettings();

            }
        });

        RetriveUserName();
        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
Intent galleryInent=new Intent();
galleryInent.setAction(Intent.ACTION_GET_CONTENT);
galleryInent.setType("image/*");
startActivityForResult(galleryInent,Galler);
            }
        });
    }
private void InitializeVariables()
    {
loadingBar=new ProgressBar(this);
        UpdateAccountSettings=(Button)findViewById(R.id.SettingsButton);
        userStatus=(EditText)findViewById(R.id.prof_status);
       userProfileImage=(CircleImageView)findViewById(R.id.profile_image);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Galler && resultCode==RESULT_OK && data!=null)
        {
            Uri ImageUri=data.getData();
            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
if(resultCode==RESULT_OK)
{

    final Uri resultUri = result.getUri();
    final StorageReference filePath = UserProfImage.child(currentUserID + ".jpg");
    filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    final String downloadUrl = uri.toString();
                    rootRef.child("Users").child(currentUserID).child("image").setValue(downloadUrl)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SettingsActivity.this, "Profile image stored to firebase database successfully.", Toast.LENGTH_SHORT).show();
                                        LoadingBar.dismiss();
                                    } else {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(SettingsActivity.this, "Error Occurred..." + message, Toast.LENGTH_SHORT).show();
                                        LoadingBar.dismiss();
                                    }
                                }
                            });
                }
            });
        }
    });
}

else
{
    Toast toast= Toast.makeText(SettingsActivity.this,"try again :(",Toast.LENGTH_SHORT);
    toast.show();
}
        }
    }
    private void UpdateSettings()
    {
String SetUserName=userName.getText().toString();
        String SetUserStatus=userStatus.getText().toString();
if(TextUtils.isEmpty(SetUserName))
{
    Toast toast= Toast.makeText(SettingsActivity.this,"Please write your username",Toast.LENGTH_SHORT);
    toast.show();
}
        if(TextUtils.isEmpty(SetUserStatus))
        {
            Toast toast= Toast.makeText(SettingsActivity.this,"Please write your status",Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            HashMap<String,String> profileMap=new HashMap<>();
            profileMap.put("UID",currentUserID);
            profileMap.put("name", SetUserName);
            profileMap.put("status",SetUserStatus);
            rootRef.child("Users").child(currentUserID).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast toast= Toast.makeText(SettingsActivity.this,"Your profile has been updates successfully XDXD",Toast.LENGTH_SHORT);
                        toast.show();
                        SendUserToMainActivity();
                    }
                    else
                    {String message=task.getException().toString();
                        Toast toast= Toast.makeText(SettingsActivity.this,"An error has occurred :("+message,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }
    }

    private void SendUserToMainActivity()
    {
        Intent MainIntent = new Intent(SettingsActivity .this, MainActivity.class);
        MainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainIntent);
        finish();
    }
    private void RetriveUserName()
    {
        rootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists( ) && (dataSnapshot.hasChild("name") )&& (dataSnapshot.hasChild("image")))
                {
                    String retrieveUserName=dataSnapshot.child("name").getValue().toString();
                    String retrieveUserStatus=dataSnapshot.child("status").getValue().toString();
                    String retrieveUserProfileImage=dataSnapshot.child("image").getValue(String.class);
                     userName.setText(retrieveUserName);
                    userStatus.setText(retrieveUserStatus);
                    Picasso.get().load(retrieveUserProfileImage).into(userProfileImage);
                }
                else if(dataSnapshot.exists() && (dataSnapshot.hasChild("name") ) && !(dataSnapshot.hasChild("image")))
                {
                    String retrieveUserName=dataSnapshot.child("name").getValue().toString();
                    String retrieveUserStatus=dataSnapshot.child("status").getValue().toString();

                    userName.setText(retrieveUserName);
                    userStatus.setText(retrieveUserStatus);
                }
                else
                {
                    userName.setVisibility(View.VISIBLE);
                    Toast toast= Toast.makeText(SettingsActivity.this,"Please update your profile",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
