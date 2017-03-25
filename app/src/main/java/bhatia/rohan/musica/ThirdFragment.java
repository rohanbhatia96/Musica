package bhatia.rohan.musica;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;



/**
 * Created by Rohan on 27-Nov-16.
 */

public class ThirdFragment extends Fragment {


    AppCompatActivity activity;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_third, container, false);

        activity = (AppCompatActivity) getActivity();
        Button b1,b2,b3;
        b1 = (Button) v.findViewById(R.id.buttonSend);
        b2 = (Button) v.findViewById(R.id.buttonRece);
        b3 = (Button) v.findViewById(R.id.buttonHis);

        b1.setOnClickListener(new butClick());
        b2.setOnClickListener(new butClick());
        b3.setOnClickListener(new butClick());

        return v;
    }

    public class butClick implements View.OnClickListener
    {


        @Override
        public void onClick(View view) {

            Intent intent;
            switch(view.getId())
            {

                case R.id.buttonSend:
                    intent = new Intent(activity.getApplicationContext(),ChallengeSentActivity.class);
                    startActivity(intent);
                    break;

                case R.id.buttonRece:
                    intent = new Intent(activity.getApplicationContext(),ChallengeReceiveActivity.class);
                    startActivity(intent);
                    break;

                case R.id.buttonHis:
                    intent = new Intent(activity.getApplicationContext(),ChallengeHistory.class);
                    startActivity(intent);
                    break;

            }

        }
    }



    public static ThirdFragment newInstance() {

        ThirdFragment f = new ThirdFragment();
        return f;
    }
}
