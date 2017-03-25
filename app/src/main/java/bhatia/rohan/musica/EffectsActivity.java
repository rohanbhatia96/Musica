package bhatia.rohan.musica;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import FFT.RealDoubleFFT;

public class EffectsActivity extends AppCompatActivity {

    String name;
    Button pitch,playback,reverb;
    EditText et1,et2;
    String[] pitchFactor;
    String[] playbackFactor;
    Button playold,playnew,playnew2,playnew3;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effects);

        Intent obj=getIntent();
        Bundle abc=obj.getExtras();
        name=abc.getString("name");
        pitchFactor = new String[]{"-3","-2","-1","1","2","3"};
        playbackFactor = new String[]{"-100","-60","-30","+30","+60","+100"};
        et1 = (EditText) findViewById(R.id.editText1);
        et2 = (EditText) findViewById(R.id.editText2);
        pitch = (Button) findViewById(R.id.btnPitch);
        pitch.setOnClickListener(new pitchButtonClick());
        playback = (Button) findViewById(R.id.btnPlayback);
        playback.setOnClickListener(new playbackButtonClick());
        reverb = (Button) findViewById(R.id.btnReverb);
        reverb.setOnClickListener(new reverbButtonClick());
        playold = (Button) findViewById(R.id.btnStart);
        playnew = (Button) findViewById(R.id.btnPlayNew);
        playnew2 = (Button) findViewById(R.id.btnPlayNew2);
        playnew3 = (Button) findViewById(R.id.btnPlayNew3);

        playold.setOnClickListener(new playButtonClicked());
        playnew.setOnClickListener(new playButtonClicked());
        playnew2.setOnClickListener(new playButtonClicked());
        playnew3.setOnClickListener(new playButtonClicked());

        playnew.setEnabled(false);
        playnew2.setEnabled(false);
        playnew3.setEnabled(false);

    }


    private class playButtonClicked implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {

            switch(view.getId())
            {
                case R.id.btnStart:
                    mediaPlayer = new MediaPlayer();
                    String path1 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String ext1 = "/Musica/YourRecordings/" + name;
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


                case R.id.btnPlayNew:
                    mediaPlayer = new MediaPlayer();
                    String path2 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String ext2 = "/Musica/YourRecordings/" ;
                    try
                    {
                        mediaPlayer.setDataSource(path2+ext2+"newPitch"+name);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    //play.setText("Stop");
                    break;

                case R.id.btnPlayNew2:
                    mediaPlayer = new MediaPlayer();
                    String path3 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String ext3 = "/Musica/YourRecordings/" ;
                    try
                    {
                        mediaPlayer.setDataSource(path3+ext3+"newPlayback"+name);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    //play.setText("Stop");
                    break;

                case R.id.btnPlayNew3:
                    mediaPlayer = new MediaPlayer();
                    String path4 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String ext4 = "/Musica/YourRecordings/" ;
                    try
                    {
                        mediaPlayer.setDataSource(path4+ext4+"newReverb"+name);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    //play.setText("Stop");
                    break;


            }

        }
    }

    private class reverbButtonClick implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {



            try {

                String path1 = Environment.getExternalStorageDirectory().getAbsolutePath();
                String ext1 = "/Musica/YourRecordings/";
                File srcFile = new File(path1+ext1+name);
                FileInputStream in = new FileInputStream(srcFile);
                FileOutputStream output =  new FileOutputStream(path1+ext1+"newReverb"+name);


                int bufsize=2400;
                int shortsize=bufsize/2;
                byte[] bufNew;
                short[] shortArr = new short[shortsize];
                short[] shortArrNew = new short[shortsize];
                double[] toTransform = new double[shortsize];
                double[] toTransformNew = new double[shortsize];
                double[] previous = new double[shortsize];
                RealDoubleFFT transformer = new RealDoubleFFT(shortsize);


                for(int i=0;i<shortsize;i++)
                    previous[i]=0;

                byte[] buf = new byte[50];
                in.read(buf);
                output.write(buf);

                buf = new byte[bufsize];


                while(in.available()>0)
                {
                    in.read(buf);
                    for (int i = 0; i < buf.length / 2; i++)
                        shortArr[i] = ((short) ((buf[i * 2] & 0xff) | (buf[i * 2 + 1] << 8)));


                    for (int i = 0; i < shortsize; i++)
                        toTransform[i] = (double) shortArr[i] / 32768.0; // signed 16 bit

                    transformer.ft(toTransform);

                    for (int i = 0; i < shortsize; i++)
                        toTransformNew[i] =0;

                    for (int i = 0; i < shortsize; i++)
                        toTransformNew[i] =toTransform[i]+previous[i];

                    for (int i = 0; i < shortsize; i++)
                        previous[i]=toTransform[i];

                    transformer.bt(toTransformNew);
                    for (int i = 0; i < shortsize; i++)
                        toTransformNew[i] = toTransformNew[i] / shortsize;


                    for (int i = 0; i < shortsize; i++)
                        shortArrNew[i] = (short) (toTransformNew[i] * 32768.0); // signed 16 bit

                    int shortArrSize = shortArrNew.length;
                    bufNew = new byte[shortArrSize * 2];
                    for (int i = 0; i < shortArrSize; i++) {
                        bufNew[i * 2] = (byte) (shortArrNew[i] & 0x00FF);
                        bufNew[(i * 2) + 1] = (byte) (shortArrNew[i] >> 8);
                        shortArrNew[i] = 0;
                    }


                    output.write(bufNew);

                }

                in.close();
                output.close();
                playnew3.setEnabled(true);

            } catch (Exception e) {
                System.err.println(e);
            }

        }
    }


    private class playbackButtonClick implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            AlertDialog.Builder build = new AlertDialog.Builder(EffectsActivity.this);
            build.setTitle("Select a factor");
            build.setItems(playbackFactor, new playbackChanger());
            build.show();
            playnew2.setEnabled(true);

        }
    }

    private class playbackChanger implements DialogInterface.OnClickListener
    {

        @Override
        public void onClick(DialogInterface dialogInterface, int j) {

            try {

                int start = Integer.parseInt( et1.getText().toString() );
                int end =   Integer.parseInt( et2.getText().toString() );
                String path1 = Environment.getExternalStorageDirectory().getAbsolutePath();
                String ext1 = "/Musica/YourRecordings/";
                File srcFile = new File(path1+ext1+name);
                FileInputStream in = new FileInputStream(srcFile);
                FileOutputStream output =  new FileOutputStream(path1+ext1+"newPlayback"+name);
                Double num = Double.parseDouble(playbackFactor[j]);

                int bufsize=2;

                byte[] buf = new byte[50];
                in.read(buf);
                buf = new byte[bufsize];
                int fincount=0;
                while(in.available()>0) {
                    in.read(buf);
                    fincount++;

                }


                int start1 = (fincount/100)*start;
                int end1 =  (fincount/100)*end;

                in = new FileInputStream(srcFile);
                buf = new byte[50];
                in.read(buf);
                long totalDataLen,totalAudioLen;

                if(num<0)
                    totalAudioLen = in.getChannel().size()*2;
                else
                    totalAudioLen = in.getChannel().size();//-(long)( in.getChannel().size()*kk*num );
                totalDataLen = totalAudioLen + 36;

                buf[4] = (byte) (totalDataLen & 0xff);
                buf[5] = (byte) ((totalDataLen >> 8) & 0xff);
                buf[6] = (byte) ((totalDataLen >> 16) & 0xff);
                buf[7] = (byte) ((totalDataLen >> 24) & 0xff);

                buf[40] = (byte) (totalAudioLen & 0xff);
                buf[41] = (byte) ((totalAudioLen >> 8) & 0xff);
                buf[42] = (byte) ((totalAudioLen >> 16) & 0xff);
                buf[43] = (byte) ((totalAudioLen >> 24) & 0xff);

                output.write(buf);

                buf = new byte[bufsize];

                for(int i=0;i<start1;i++)
                {
                    in.read(buf);
                    output.write(buf);
                }

                int count=0;

                if(num<0)
                    for(int i=start1;i<end1;i++)
                    {
                        in.read(buf);
                        output.write(buf);

                        if(num==-30)
                        {
                            if(count<3)
                                output.write(buf);
                            if(count>11)
                                count=0;
                        }

                        if(num==-60)
                        {
                            if(count<6)
                                output.write(buf);
                            if(count>16)
                                count=0;

                        }

                        if(num==-100)
                        {

                            if(count%2==0)
                                output.write(buf);

                        }
                        count++;

                    }


                else
                    for(int i=start1;i<end1;i++)
                    {
                        in.read(buf);

                        if(num==30)
                        {
                            if(count<8)
                                output.write(buf);

                            if(count==10)
                                count=0;
                        }

                        if(num==60)
                        {
                            if(count<13)
                                output.write(buf);
                            if(count==20)
                                count=0;

                        }

                        if(num==100)
                        {

                            if(count%2==0)
                                output.write(buf);

                        }
                        count++;

                    }

                while(in.available()>0)
                {
                    in.read(buf);
                    output.write(buf);
                }

                in.close();
                output.close();

            } catch (Exception e) {
                System.err.println(e);
            }

        }
    }

    private class pitchButtonClick implements View.OnClickListener
    {


        @Override
        public void onClick(View view) {
            AlertDialog.Builder build = new AlertDialog.Builder(EffectsActivity.this);
            build.setTitle("Select a factor");
            build.setItems(pitchFactor, new pitchChange());
            build.show();
            playnew.setEnabled(true);
        }
    }

    private class pitchChange implements DialogInterface.OnClickListener
    {

        @Override
        public void onClick(DialogInterface dialogInterface, int j) {



            try {

                String path1 = Environment.getExternalStorageDirectory().getAbsolutePath();
                String ext1 = "/Musica/YourRecordings/";
                File srcFile = new File(path1+ext1+name);
                FileInputStream in = new FileInputStream(srcFile);
                FileOutputStream output =  new FileOutputStream(path1+ext1+"newPitch"+name);


                int bufsize=3600;
                int shortsize=bufsize/2;
                byte[] bufNew;
                short[] shortArr = new short[shortsize];
                short[] shortArrNew = new short[shortsize];
                double[] toTransform = new double[shortsize];
                double[] toTransformNew = new double[shortsize];
                RealDoubleFFT transformer = new RealDoubleFFT(shortsize);

                byte[] buf = new byte[50];
                in.read(buf);
                long totalDataLen,totalAudioLen;

                totalAudioLen = in.getChannel().size();
                totalDataLen = totalAudioLen + 36;

                buf[4] = (byte) (totalDataLen & 0xff);
                buf[5] = (byte) ((totalDataLen >> 8) & 0xff);
                buf[6] = (byte) ((totalDataLen >> 16) & 0xff);
                buf[7] = (byte) ((totalDataLen >> 24) & 0xff);

                buf[40] = (byte) (totalAudioLen & 0xff);
                buf[41] = (byte) ((totalAudioLen >> 8) & 0xff);
                buf[42] = (byte) ((totalAudioLen >> 16) & 0xff);
                buf[43] = (byte) ((totalAudioLen >> 24) & 0xff);

                output.write(buf);

                buf = new byte[bufsize];

                int count=0;
                int x,y;
                x=y=0;
                while(in.available()>0) {
                    in.read(buf);
                    for (int i = 0; i < buf.length / 2; i++)
                        shortArr[i] = ((short) ((buf[i * 2] & 0xff) | (buf[i * 2 + 1] << 8)));


                    for (int i = 0; i < shortsize; i++)
                        toTransform[i] = (double) shortArr[i] / 32768.0; // signed 16 bit

                    transformer.ft(toTransform);

                    for (int i = 0; i < shortsize; i++)
                        toTransformNew[i] =0;

                    int temp = Integer.parseInt( pitchFactor[j] );
                    if(temp < 0)
                        //negative
                        for (int i = temp*(-10); i < shortsize; i++)
                            toTransformNew[i] = toTransform[i-(temp*-10)];

                    if(temp>0)
                        //positive
                        for (int i = 0; i < shortsize-(temp*10); i++)
                            toTransformNew[i] = toTransform[i+(temp*10)];

                    transformer.bt(toTransformNew);
                    for (int i = 0; i < shortsize; i++)
                        toTransformNew[i] = toTransformNew[i] / shortsize;


                    for (int i = 0; i < shortsize; i++)
                        shortArrNew[i] = (short) (toTransformNew[i] * 32768.0); // signed 16 bit

                    int shortArrSize = shortArrNew.length;
                    bufNew = new byte[shortArrSize * 2];
                    for (int i = 0; i < shortArrSize; i++) {
                        bufNew[i * 2] = (byte) (shortArrNew[i] & 0x00FF);
                        bufNew[(i * 2) + 1] = (byte) (shortArrNew[i] >> 8);
                        shortArrNew[i] = 0;
                    }


                    //if(count%2==0)

                    //for(int i=0;i<2;i++)

                    output.write(bufNew);


                    //count++;
                }

                in.close();
                output.close();

            } catch (Exception e) {
                System.err.println(e);
            }

        }
    }



}
