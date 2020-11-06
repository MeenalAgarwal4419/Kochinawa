package meenal.agarwal.kochinawa;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GroupChat_Fragment extends Fragment
{
private DatabaseReference groupRef;
    private  View groupFragmentView;
   private ListView Listv;
   private ArrayAdapter<String> array_adapter;
   private ArrayList<String>List_of_group=new ArrayList<>();
    public GroupChat_Fragment()
    {

    }
 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         groupFragmentView=inflater.inflate(R.layout.fragment_group_chat_, container, false);
     groupRef= FirebaseDatabase.getInstance().getReference().child("Groups");
InitializeFields();

        RetriveGroupNames();
        Listv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
     {
         String currentGroupName =parent.getItemAtPosition(position).toString();
         Intent groupchat=new Intent(getContext(),new_activity.class);
         groupchat.putExtra("groupName",currentGroupName);
         startActivity(groupchat);

         }
        });
              return groupFragmentView;
    }

    private void RetriveGroupNames()
    {
        groupRef.addValueEventListener(new ValueEventListener() {// value event listener are used to retrieve the data
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Set<String> sety=new HashSet<>();
                Iterator iter=dataSnapshot.getChildren().iterator();
while(iter.hasNext())
{
    sety.add(((DataSnapshot)iter.next()).getKey());
}
List_of_group.clear();
List_of_group.addAll(sety);
array_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void InitializeFields() {
    Listv=(ListView) groupFragmentView.findViewById(R.id.List_view);
    array_adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,List_of_group);
    Listv.setAdapter(array_adapter);
    }
}
