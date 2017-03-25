package bhatia.rohan.musica;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Rohan on 27-Nov-16.
 */

public class ChallengeCustomList extends ArrayAdapter<String> {

    private final Context context;
    private final String[] songs;
    private final String[] email;

    public ChallengeCustomList(Context context, String[] songs, String[] email) {
        super(context,R.layout.challenge_list_single);
        this.context = context;
        this.songs = songs;
        this.email = email;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.challenge_list_single,parent,false);
        }

        TextView songTitle = (TextView) convertView.findViewById(R.id.song_textview);
        TextView emailTitle = (TextView) convertView.findViewById(R.id.email_textview);
        songTitle.setText(songs[position]);
        songTitle.setTypeface(null, Typeface.BOLD);
        emailTitle.setText(email[position]);

        return convertView;
    }

    @Override
    public int getCount() {
        return songs.length;
    }
}
