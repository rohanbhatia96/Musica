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
 * Created by Rohan on 26-Nov-16.
 */

public class CustomList extends ArrayAdapter<String> {

    private final Context context;
    private final String[] songs;
    private final String[] artist;
    private final Integer[] imgID;

    public CustomList(Context context, String[] songs, String[] artist, Integer[] imgID) {
        super(context,R.layout.list_single);
        this.context = context;
        this.songs = songs;
        this.artist = artist;
        this.imgID = imgID;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.list_single,parent,false);
        }

        TextView songTitle = (TextView) convertView.findViewById(R.id.song_name);
        TextView artistTitle = (TextView) convertView.findViewById(R.id.art_name);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.album_art);
        songTitle.setText(songs[position]);
        songTitle.setTypeface(null, Typeface.BOLD);
        artistTitle.setText(artist[position]);
        imageView.setImageResource(imgID[position]);

        return convertView;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

