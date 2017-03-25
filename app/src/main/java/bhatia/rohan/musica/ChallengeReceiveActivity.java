package bhatia.rohan.musica;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChallengeReceiveActivity extends AppCompatActivity {

    Firebase ref;
    FirebaseAuth auth;
    ArrayList<String> emailIDs;
    ArrayList<String> challengeIDs;
    ArrayList<Integer> songIDs;
    DataSnapshot dataSnapshotG;
    String userID;
    ListView listView;
    String email;
    int count;
    String[] arr;
    ValueEventListener val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_receive);

        SongList songList = new SongList();
        arr = songList.getArr();

        listView = (ListView)findViewById(R.id.ChallengeListView);
        ref = new Firebase("https://musica-cb289.firebaseio.com/");
        auth = FirebaseAuth.getInstance();
        emailIDs = new ArrayList<String>();
        challengeIDs = new ArrayList<String>();
        songIDs = new ArrayList<Integer>();
        count = 0;
        userID = auth.getCurrentUser().getUid();
        val = ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshotG = dataSnapshot;

                if(dataSnapshot==null)
                    Log.e("manik1","not received");
                else
                    Log.e("manik1","received");

                if(dataSnapshotG==null)
                    Log.e("manik2","not received");
                else
                    Log.e("manik2","received");

                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    if(child.getKey().equals("UserChallenges"))
                    {
                        for(DataSnapshot child2 : child.getChildren())
                        {
                            if(child2.getKey().equals(userID))
                            {
                                for(DataSnapshot child3 : child2.getChildren())
                                    challengeIDs.add(child3.getKey().toString());
                                Log.e("manik",challengeIDs.size()+"");
                            }
                        }
                    }
                }
                while(count<challengeIDs.size())
                    for(DataSnapshot child : dataSnapshotG.getChildren())
                    {
                        //Log.e("manikwhile","in while");
                        if(child.getKey().equals("Challenges"))
                        {
                            for (DataSnapshot child2 : child.getChildren())
                            {
                                Log.e("manikman",child2.getKey());
                                Log.e("manikyooo",challengeIDs.get(count));
                                if(child2.getKey().equals(challengeIDs.get(count)))
                                {
                                    Log.e("manikyo1","pahunch gaye");
                                    Log.e("manikyo2","pahunch gaye "+child2.child("Challenger_A").getValue());
                                    if(child2.child("Challenger_A").getValue().equals(userID))
                                    {
                                        challengeIDs.remove(count);
                                        Log.e("manikyo3","pahunch gaye galti se");
                                    }
                                    else if(child2.child("Status_A").getValue().equals("Pending"))
                                    {
                                        Log.e("manikyo4", "pahunch gaye ye sahi hai");
                                        emailIDs.add(child2.child("Challenger_A").getValue().toString());
                                        songIDs.add(child2.child("Song").getValue(Integer.class));
                                        count++;
                                        Log.e("manikcount", count + "");
                                    }
                                    else
                                    {
                                        challengeIDs.remove(count);

                                    }
                                }
                            }
                        }
                    }
                for(int i=0;i<emailIDs.size();i++)
                {
                    emailIDs.set(i,dataSnapshotG.child("Users").child(emailIDs.get(i)).child("Email").getValue().toString());
                }

                Log.e("manik3",songIDs.size()+"");
                Log.e("manik3-1",emailIDs.size()+"");
                if(songIDs.size()!=0)
                {
                    String[] songs = new String[songIDs.size()];
                    String[] emails = new String[songIDs.size()];
                    for (int i = 0; i < songIDs.size(); i++)
                    {
                        songs[i] = arr[songIDs.get(i)];
                        emails[i] = emailIDs.get(i);
                    }

                    Log.e("manik5","yoooo"+songs[0]+emails[0]);
                    ChallengeCustomList adapter = new ChallengeCustomList(getBaseContext(),songs,emails);
                    listView.setAdapter(adapter);

                }
                ref.removeEventListener(val);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        listView.setOnItemClickListener(new challegeItemClicked());
    }


    public class challegeItemClicked implements AdapterView.OnItemClickListener
    {
        int temp;
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            AlertDialog.Builder build = new AlertDialog.Builder(ChallengeReceiveActivity.this);
            build.setTitle("Accept Challenge?");
            build.setMessage("Song is "+arr[songIDs.get(i)]);
            temp=i;
            build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Bundle b = new Bundle();
                    b.putInt("choice",2);
                    b.putInt("pos",songIDs.get(temp));
                    b.putString("email",emailIDs.get(temp));
                    b.putString("ChallengeID",challengeIDs.get(temp));
                    Log.e("maniklog",songIDs.get(temp)+" "+emailIDs.get(temp)+" "+challengeIDs.get(temp));
                    Intent intent = new Intent(ChallengeReceiveActivity.this,KaraokeActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

            build.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //tv.setText("No Clicked");
                }
            });
            build.show();
        }
    }


}
