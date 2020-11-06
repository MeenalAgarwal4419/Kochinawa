package meenal.agarwal.kochinawa;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class new_activity extends AppCompatActivity {
private Toolbar mtoolbar;
private ImageButton sendMessageButton;
private EditText groupMessages;
private TextView disPlayTextMessage;
private ScrollView scv;
private String currentGroupName,currentUserID,currentUserName,currentDt,currentT;
private FirebaseAuth mth;
private DatabaseReference userRef,GroupNameRef,GroupMessageKeyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activity);

        mth=FirebaseAuth.getInstance();
        currentUserID=mth.getCurrentUser().getUid();

        currentGroupName= getIntent().getExtras().get("groupName").toString();
        Toast toast1= Toast.makeText(new_activity.this,currentGroupName,Toast.LENGTH_SHORT);
        toast1.show();

        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef=FirebaseDatabase.getInstance().getReference().child("Groups").child((currentGroupName));

        InitializeField();
GetUserInfo();
sendMessageButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        SendMessageInfoToDatabase();
        groupMessages.setText("");
        scv.fullScroll(ScrollView.FOCUS_DOWN);
    }
});
    }


    @Override
    protected void onStart() {
        super.onStart();
        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
             if(dataSnapshot.exists())
             {
                 DisplayMessages(dataSnapshot);
             }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }
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
    }


    private void InitializeField()
    {
       mtoolbar=(Toolbar)findViewById(R.id.group_chat_bar);
setSupportActionBar(mtoolbar);
getSupportActionBar().setTitle(currentGroupName);
sendMessageButton=(ImageButton)findViewById(R.id.send_message_button);
        disPlayTextMessage=(TextView)findViewById(R.id.group_chat);

        groupMessages=(EditText)findViewById(R.id.groupMessage);
      scv=(ScrollView)findViewById(R.id.my_scroll_view);

    }
    private void GetUserInfo()
    {
userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
     if(dataSnapshot.exists())
     {
         currentUserName=dataSnapshot.child("name").getValue().toString();
     }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
    }
    private void SendMessageInfoToDatabase()
    {
        String message=groupMessages.getText().toString();
      String messageKey=GroupNameRef.push().getKey();
        if(TextUtils.isEmpty(message))
        {
            Toast toast= Toast.makeText(getApplicationContext(),"You forgot to type :-/",Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
           Calendar ccdate =Calendar.getInstance();
            SimpleDateFormat currentDateFormat=new SimpleDateFormat("MMM dd,yyyy");
            currentDt=currentDateFormat.format(ccdate.getTime());
            Calendar cctime =Calendar.getInstance();
            SimpleDateFormat currentTimeFormat=new SimpleDateFormat("hh:mm a");
            currentT=currentTimeFormat.format(cctime.getTime());
            HashMap<String,Object> groupMessageKey=new HashMap<>();
GroupNameRef.updateChildren(groupMessageKey);
GroupMessageKeyRef=GroupNameRef.child(messageKey);
            HashMap<String,Object> MessageInfo=new HashMap<>();
            MessageInfo.put("name",currentUserName);
            MessageInfo.put("message",message);
            MessageInfo.put("date",currentDt);
            MessageInfo.put("time",currentT);
GroupMessageKeyRef.updateChildren(MessageInfo);

        }
    }
    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator=dataSnapshot.getChildren().iterator();
        while(iterator.hasNext())
        {
            String chatDate=(String)((DataSnapshot)iterator.next()).getValue();
            String chatMessage=(String)((DataSnapshot)iterator.next()).getValue();
            String chatName=(String)((DataSnapshot)iterator.next()).getValue();
            String chatTime=(String)((DataSnapshot)iterator.next()).getValue();
       disPlayTextMessage.append(chatName+":\n"+chatMessage+"\n" + chatTime+"\n" +chatDate+"\n\n\n");
            scv.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

}
