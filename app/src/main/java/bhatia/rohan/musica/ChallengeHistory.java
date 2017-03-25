package bhatia.rohan.musica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChallengeHistory extends AppCompatActivity {

    Firebase ref;
    FirebaseAuth auth;
    ArrayList<Challenge> challenges;
    ValueEventListener val;
    String userID;
    int count;
    ArrayList<String> challengeIDs;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_history);
        challenges = new ArrayList<Challenge>();
        challengeIDs = new ArrayList<String>();
        auth = FirebaseAuth.getInstance();
        count = 0;
        lv = (ListView) findViewById(R.id.HistoryListView);
        userID = auth.getCurrentUser().getUid();
        ref = new Firebase("https://musica-cb289.firebaseio.com/");
        val = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                    for(DataSnapshot child : dataSnapshot.getChildren())
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
                                    if(child2.child("Status_A").getValue().equals("Pending"));
                                    else
                                    {
                                        challenges.add(child2.getValue(Challenge.class));
                                    }

                                    count++;
                                }
                            }
                        }
                    }


                //song,email,score,score,status
                SongList songList = new SongList();
                String[] s = songList.getArr();
                String[] song_list = new String[challenges.size()];
                String[] em = new String[challenges.size()];
                double[] yscore = new double[challenges.size()];
                double[] oscore = new double[challenges.size()];
                String[] status = new String[challenges.size()];
                for(int i=0;i<challenges.size();i++)
                {

                    song_list[i]=s[challenges.get(i).Song];
                    if(userID.equals(challenges.get(i).Challenger_A))
                    {
                        em[i] = dataSnapshot.child("Users").child(challenges.get(i).Challenger_B).child("Email").getValue().toString();
                        yscore[i] = challenges.get(i).Score_A;
                        oscore[i] = challenges.get(i).Score_B;
                        status[i] = challenges.get(i).Status_A;
                    }
                    else {

                        em[i] = dataSnapshot.child("Users").child(challenges.get(i).Challenger_A).child("Email").getValue().toString();
                        yscore[i] = challenges.get(i).Score_B;
                        oscore[i] = challenges.get(i).Score_A;
                        status[i] = challenges.get(i).Status_B;
                    }
                }
                HistoryCustomList adapter = new HistoryCustomList(getBaseContext(),song_list,em,status,yscore,oscore);
                lv.setAdapter(adapter);




            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });





    }
}
