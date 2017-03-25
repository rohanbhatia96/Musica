package bhatia.rohan.musica;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Rohan on 26-Nov-16.
 */

public class FirstFragment extends Fragment {
    ListView list;
    String[] song;
    String[] artist;
    Integer[] img;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_first, container, false);
        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("Musica 2.0");
        //activity.getSupportActionBar().hide();

        list=(ListView)v.findViewById(R.id.listView);
        song=new String[]{"Hai Junoon","Wonderwall"};
        artist=new String[]{"KK","Oasis"};
        img=new Integer[]{R.raw.song1,R.raw.song2};

        CustomList adapter = new CustomList(v.getContext(),song,artist,img);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new itemClicked());

        return v;
    }

    public static FirstFragment newInstance() {

        FirstFragment f = new FirstFragment();
        return f;
    }

    public class itemClicked implements AdapterView.OnItemClickListener
    {


        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            // MainActivity.changeFrag();
            AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
            build.setTitle(song[i]);
            build.setMessage("Ready to sing?");
            final int x11=i;
            build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent = new Intent(getActivity(),KaraokeActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("choice",0);
                    b.putInt("pos",x11);
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

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getMenuInflater().inflate(R.menu.options,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    */
}

