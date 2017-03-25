package bhatia.rohan.musica;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChallengeSentActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Firebase refAll;
    Firebase ref;
    Firebase refMe;
    String userID;
    ListView list;
    int count;
    String challengeID;
    ArrayList<String> mUserEmails;
    DataSnapshot dataSnapshotG;

    String[] song_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_sent);

        SongList songList = new SongList();
        song_list = songList.getArr();


        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        refAll = new Firebase("https://musica-cb289.firebaseio.com/");
        ref = new Firebase("https://musica-cb289.firebaseio.com/Users");
        refMe = new Firebase("https://musica-cb289.firebaseio.com/Users/" + userID);
        list = (ListView)findViewById(R.id.listView);
        challengeID = "";
        count = 0;
        mUserEmails = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUserEmails);
        list.setAdapter(arrayAdapter);
        ChildEventListener listner = refAll.addChildEventListener(new ChildEventListener() {
            int i = 0;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dataSnapshotG = dataSnapshot;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    for (DataSnapshot child2 : child.getChildren()) {
                        if (child2.getKey().equals("Email")) {
                            if (child.getKey().equals(userID))
                                break;
                            mUserEmails.add(child2.getValue().toString());
                            arrayAdapter.notifyDataSetChanged();

                        }
                    }


                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    /*dataSnapshotG = dataSnapshot;
                    mUserEmails = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        for (DataSnapshot child2 : child.getChildren()) {
                            if (child2.getKey().equals("Email")) {
                                if (child.getKey().equals(userID))
                                    break;
                                if (child.child("Challenger").getValue().equals("none")) {
                                    mUserEmails.add(child2.getValue().toString());
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                    arrayAdapter.notifyDataSetChanged();

                */
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String email = adapterView.getItemAtPosition(i).toString();

                AlertDialog.Builder build = new AlertDialog.Builder(ChallengeSentActivity.this);
                build.setTitle("Select a Song");
                build.setItems(song_list,new songChoose(email));
                build.show();

            }
        });


    }


    public class songChoose implements DialogInterface.OnClickListener
    {

        String email;

        public songChoose(String k)
        {
            email = k;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            Bundle b = new Bundle();
            b.putInt("choice",1);
            b.putInt("pos",i);
            b.putString("email",email);
            Intent intent = new Intent(ChallengeSentActivity.this,KaraokeActivity.class);
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    public int getCount(String k) {
        count = 0;
        ChildEventListener listner2 = ref.child(k).child("Challenge_Count").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                count = dataSnapshot.getValue(int.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return count;
    }

    public String getChallengeID(String k) {
        challengeID = "";
        refAll.child("UserChallenges").child(k).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                    if (child.getValue().equals("")) {
                        challengeID = child.getKey();
                        break;
                    }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return challengeID;
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
