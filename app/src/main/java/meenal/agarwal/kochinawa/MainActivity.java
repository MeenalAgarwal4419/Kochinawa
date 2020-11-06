package meenal.agarwal.kochinawa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{
private Toolbar mToolBar;
    private FirebaseAuth ath;
private ViewPager vPager;
private TabLayout tLayout;
private DatabaseReference rootRef;
private Tab_fragment tf;
private FirebaseUser current_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ath=FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        current_user=ath.getCurrentUser();
        mToolBar=(Toolbar) findViewById(R.id.main_page_toolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Kochinawa!");
vPager=(ViewPager) findViewById(R.id.ViewLayout);
tf=new Tab_fragment(getSupportFragmentManager());
vPager.setAdapter(tf);
tLayout=(TabLayout) findViewById(R.id.tab_layout);
tLayout.setupWithViewPager(vPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.Logout)
        {
            ath.signOut();
            SendUserToLoginActivity();
        }

        if (item.getItemId() == R.id.ST)
        {
            SendUserToSettingsActivity();
        }
        if (item.getItemId() == R.id.SM)
        {

        }
        if (item.getItemId() == R.id.Find_contact)
        {
            SendUserFriendRequestActivity();
        }
        if (item.getItemId() == R.id.YNG)
        {
RequestNewGroup();
        }
        return true;

    }

    private void RequestNewGroup()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter your group name:");
        final EditText groupNmaeField=new EditText(MainActivity.this);
        groupNmaeField.setHint("Sort Karlo apna apna XD");
        builder.setView(groupNmaeField);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           String groupName=groupNmaeField.getText().toString();
           if(TextUtils.isEmpty(groupName))
           {
               Toast toast= Toast.makeText(MainActivity.this,"You haven't written your group's name",Toast.LENGTH_SHORT);
               toast.show();
           }
           else
           {
CreateNewGroup(groupName);
           }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }

        });
        builder.show();
    }

    private void CreateNewGroup(final String groupName)
    {
        rootRef.child("Groups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
if(task.isSuccessful())
{
    Toast toast= Toast.makeText(MainActivity.this,groupName+" is created successfully! XD",Toast.LENGTH_SHORT);
    toast.show();
}
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(current_user==null)
        {
            SendUserToLoginActivity();
        }
        else {
            VerifyUserExistence();
        }
    }

    private void VerifyUserExistence()
    {
        String currUserId=ath.getCurrentUser().getUid();
rootRef.child("Users").child((currUserId)).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if((dataSnapshot.child(("name")).exists()))
        {
            Toast toast= Toast.makeText(MainActivity.this,"Welcome",Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
           SendUserToSettingsActivity();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
    }

    private void SendUserToLoginActivity()
    {
        Intent LoginIntent=new Intent(MainActivity.this,Login.class);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity( LoginIntent);
        
        finish();

    }
    private void SendUserFriendRequestActivity()
    {
        Intent FriendIntent=new Intent(MainActivity.this,FriendReqActivity.class);
        startActivity( FriendIntent);

        finish();

    }
    private void SendUserToSettingsActivity()
    {
        Intent SettingsIntent=new Intent(MainActivity.this,SettingsActivity.class);
        SettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SettingsIntent);
        finish();
    }
}

