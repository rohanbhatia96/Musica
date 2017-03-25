package bhatia.rohan.musica;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

/**
 * Created by Rohan on 28-Nov-16.
 */

public class SecondFragment extends Fragment {

    TextView tv;
    Button left,right,play,record,add;
    File[] fileList;
    MediaPlayer mediaPlayer;
    AudioRecorder audioRecorder;
    String path;
    File filePath;
    int pos;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_second, container, false);
        setHasOptionsMenu(true);

        path = Environment.getExternalStorageDirectory().getAbsolutePath();
        filePath= new File(path+"/Musica/YourRecordings");
        fileList = filePath.listFiles();
        pos=-1;

        tv = (TextView) v.findViewById(R.id.textView);
        if(fileList.length>0)
        {
            pos=0;
            String name = fileList[pos].getName().toString();
            tv.setText(name);
        }
        left= (Button) v.findViewById(R.id.btnLeft);
        right= (Button) v.findViewById(R.id.btnRight);
        record= (Button) v.findViewById(R.id.btnRecord);
        play= (Button) v.findViewById(R.id.btnPlay);
        add = (Button) v.findViewById(R.id.btnEffects);

        left.setOnClickListener(new butClick());
        right.setOnClickListener(new butClick());
        record.setOnClickListener(new butClick());
        play.setOnClickListener(new butClick());
        add.setOnClickListener(new butClick());


        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser)
        {
            pos=-1;
            if(fileList.length>0)
            {
                pos=0;
                String name = fileList[pos].getName().toString();
                tv.setText(name);
            }

        }
    }


    public static SecondFragment newInstance() {

        SecondFragment f = new SecondFragment();
        return f;
    }

    private class butClick implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            String name;
            switch(view.getId())
            {
                case R.id.btnRight:
                    if( pos<(fileList.length-1) )
                        pos++;

                    else if(pos == (fileList.length-1))
                        pos=0;

                    name = fileList[pos].getName().toString();
                    tv.setText(name);
                    break;

                case R.id.btnLeft:
                    if( pos>0 )
                        pos--;

                    else if(pos == 0)
                        pos=(fileList.length-1);

                    name = fileList[pos].getName().toString();
                    tv.setText(name);
                    break;

                case R.id.btnPlay:
                    mediaPlayer = new MediaPlayer();
                    String path1 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String ext1 = "/Musica/YourRecordings/" + tv.getText();
                    try
                    {
                        mediaPlayer.setDataSource(path1 + ext1);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    //play.setText("Stop");
                    break;

                case R.id.btnRecord:
                    if(record.getText().equals("Record"))
                    {
                        audioRecorder = new AudioRecorder();
                        audioRecorder.startRecording();
                        record.setText("Stop");
                    }
                    else
                    {
                        audioRecorder.stopRecording();
                        record.setText("Record");
                        fileList = filePath.listFiles();
                    }
                    break;

                case R.id.btnEffects:
                    Intent intent = new Intent(getContext(),EffectsActivity.class);
                    Bundle b = new Bundle();
                    b.putString("name",tv.getText().toString());
                    intent.putExtras(b);
                    startActivity(intent);
                    break;



            }

        }
    }

}
