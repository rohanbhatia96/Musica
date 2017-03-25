package bhatia.rohan.musica;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Rohan on 28-Nov-16.
 */

public class HistoryCustomList extends ArrayAdapter<String> {

    private final Context context;
    private final String[] songs;
    private final String[] email;
    private final String[] status;
    private final double[] yscore;
    private final double[] oscore;

    public HistoryCustomList(Context context, String[] songs, String[] email,String[] status,double[] yscore, double[] oscore) {
        super(context,R.layout.history_list_single);
        this.context = context;
        this.songs = songs;
        this.email = email;
        this.status = status;
        this.yscore=yscore;
        this.oscore=oscore;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.history_list_single,parent,false);
        }

        TextView songTitle = (TextView) convertView.findViewById(R.id.his_song_tv);
        TextView emailTitle = (TextView) convertView.findViewById(R.id.his_email_tv);
        TextView statusTitle = (TextView) convertView.findViewById(R.id.status_tv);
        TextView yscoreTitle = (TextView) convertView.findViewById(R.id.your_Score_tv);
        TextView oscoreTitle = (TextView) convertView.findViewById(R.id.opp_score_tv);

        songTitle.setText("Song Name: "+songs[position]);
        statusTitle.setText("Status: "+status[position]);
        yscoreTitle.setText("Your Score: "+yscore[position]);
        oscoreTitle.setText("Opponent Score: "+oscore[position]);
        emailTitle.setText("Opponent: "+email[position]);
        emailTitle.setTypeface(null, Typeface.BOLD);
        return convertView;
    }

    @Override
    public int getCount() {
        return songs.length;
    }
}

