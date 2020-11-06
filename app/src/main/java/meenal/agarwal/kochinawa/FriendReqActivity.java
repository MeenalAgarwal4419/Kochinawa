package meenal.agarwal.kochinawa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FriendReqActivity extends AppCompatActivity {
private Toolbar mtoolBar;
private RecyclerView recyclerList;
private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_req);
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerList=(RecyclerView)findViewById(R.id.find_friends_recyclerList);
        recyclerList.setLayoutManager(new LinearLayoutManager(this));
        mtoolBar=(Toolbar)findViewById(R.id.FriendToolBar);
        setSupportActionBar(mtoolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find the ship wanna ride");
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerOptions<Contacts>options=new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(userRef,Contacts.class).build();
        FirebaseRecyclerAdapter<Contacts,FindFriendViewHolder> adapter=new FirebaseRecyclerAdapter<Contacts, FindFriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, int position, @NonNull Contacts model) {

            }

            @NonNull
            @Override
            public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display,parent,false);
              FindFriendViewHolder viewHolder=new FindFriendViewHolder(view);
         return viewHolder;
            }
        };
    }
    public static class FindFriendViewHolder extends RecyclerView.ViewHolder
    {
TextView userName, userStatus;
        public FindFriendViewHolder(@NonNull View itemView)
        {
            super(itemView);
            userName=itemView.findViewById(R.id.user_name);
            userStatus=itemView.findViewById(R.id.user_status);
        }
    }


}
