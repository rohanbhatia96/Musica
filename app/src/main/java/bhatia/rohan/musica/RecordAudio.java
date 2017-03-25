package bhatia.rohan.musica;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import FFT.RealDoubleFFT;

/**
 * Created by Rohan on 26-Nov-16.
 */

public class RecordAudio extends AsyncTask<Void, double[], Boolean> {

    //Frequency graph variables
    Bitmap bmp1,bmp2,bmp3;
    int cnt=0;
    double sco;
    int x;

    //FFT varaiables
    RealDoubleFFT transformer;
    RealDoubleFFT transformer2;
    int blockSize;// = 256;

    //File writing variables
    FileInputStream fis;


    //Audio Recording variables
    int frequency = 20500;
    int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    AudioRecord audioRecord;
    int num;


    private Boolean started = false;
    private Boolean CANCELLED_FLAG = false;

    FileOutputStream fos;

    public RecordAudio(int n){
        num=n;
        blockSize = 512;
        transformer = new RealDoubleFFT(blockSize);
        transformer2 = new RealDoubleFFT(blockSize);
        bmp1 = Bitmap.createBitmap(1000,500,Bitmap.Config.ARGB_8888);
        bmp2 = Bitmap.createBitmap(1000,500,Bitmap.Config.ARGB_8888);
        bmp3 = Bitmap.createBitmap(1000,500,Bitmap.Config.ARGB_8888);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Musica/song"+(num+1)+"vocals.bin";
        File file= new File(path);
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Musica/yourRec.pcm";
        file= new File(path);
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        sco=0;
        x=0;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        int bufferSize = AudioRecord.getMinBufferSize(frequency,
                channelConfiguration, audioEncoding);
        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.DEFAULT, frequency,
                channelConfiguration, audioEncoding, bufferSize);
        int bufferReadResult;
        short[] buffer = new short[blockSize];
        double[] toTransform = new double[blockSize];
        double[] toTransform2 = new double[blockSize];

        byte[] buf2 = new byte[blockSize*2];


        try {
            audioRecord.startRecording();

        } catch (IllegalStateException e) {
            Log.e("Recording failed", e.toString());

        }

        while (started) {

            if (isCancelled() || (CANCELLED_FLAG == true)) {

                started = false;
                //publishProgress(cancelledResult);
                Log.d("doInBackground", "Cancelling the RecordTask");
                break;
            }

            else
            {
                bufferReadResult = audioRecord.read(buffer, 0, blockSize);

                for(int i=0;i<blockSize;i++)
                {
                    buf2[2*i]= (byte) (buffer[i]);
                    buf2[(2*i)+1]= (byte) ((buffer[i] >> 8) & 0xff);
                }
                try {
                    fos.write(buf2);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                byte[] buf = new byte[blockSize*2];
                short[] temp = new short[blockSize];
                try {
                    fis.read(buf);

                    for (int i = 0; i <buf.length/2 ; i++)
                        temp[i] = ( (short)( ( buf[i*2] & 0xff )|( buf[i*2 + 1] << 8 ) ) );

                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < blockSize && i < bufferReadResult; i++)
                {
                    toTransform[i] = (double) buffer[i] / 32768.0; // signed 16 bit
                    toTransform2[i] = (double) temp[i] / 32768.0;
                }

                transformer.ft(toTransform);
                transformer2.ft(toTransform2);

                publishProgress(toTransform,toTransform2);
                x++;

            }


        }
        return true;
    }


    @Override
    protected void onProgressUpdate(double[]... values) {
        super.onProgressUpdate(values);


        Canvas canvas1 = null;

        if(cnt<10)
            canvas1 = new Canvas(bmp1);
        else
            canvas1 = new Canvas(bmp2);

        cnt++;


        //canvas1.drawColor(Color.BLACK);
        //canvas2.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(Color.GREEN);
        for(int i=0;i<values[0].length;i++)
        {
            float x = ((float) i*(6000/512));
            float y=  (float) values[0][i]*5f;

            canvas1.drawLine(x,250,x,250-y,paint);

        }

        sco+=score(values[0],values[1]);
    }


    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        try {
            audioRecord.stop();
            audioRecord.release();

        } catch (IllegalStateException e) {
            Log.e("Stop failed", e.toString());

        }

    }


    public Bitmap retBMP3()
    {
        cnt=0;
        Bitmap temp;
        temp=bmp1;
        Canvas canvas = new Canvas(bmp1);
        canvas.drawColor(Color.BLACK);
        return temp;
    }

    public Bitmap retBMP2()
    {
        cnt=0;
        Bitmap temp;
        temp=bmp2;
        Canvas canvas = new Canvas(bmp2);
        canvas.drawColor(Color.BLACK);
        return temp;
    }


    private double score(double[] x,double[] y)
    {
        double sum=0;
        for(int i=0;i<blockSize;i++)
        {
            sum+=( (x[i]-y[i])*(x[i]-y[i]) );
        }

        sum/=blockSize;
        sum=Math.sqrt(sum);

        return sum;

    }

    public double retScore()
    {
        return sco/x;
    }


    public void setStarted(Boolean t)
    {
        started = t;
    }

    public void setCANCELLED_FLAG(Boolean t)
    {

        CANCELLED_FLAG = t;
    }

}

