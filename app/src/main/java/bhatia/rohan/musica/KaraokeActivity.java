package bhatia.rohan.musica;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class KaraokeActivity extends AppCompatActivity {

    int choice;
    int num;
    Bundle abc;
    String song_name;

    //Graphic variables
    ImageView backgroundOne, backgroundTwo, backgroundThree, backgroundFour;
    String lyric;
    BufferedReader br;

    //Recording variables
    RecordAudio recordAudio;
    MediaPlayer mediaPlayer;
    boolean start;
    File[] fileList;
    File filePath;

    //Firebase Variables
    DataSnapshot dataSnapshotG;
    String key;
    String userID;
    Firebase ref;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karaoke);
        String path, ext;
        int Rid;
        Intent obj = getIntent();
        abc = obj.getExtras();
        num = abc.getInt("pos");
        choice = abc.getInt("choice");
        ref = new Firebase("https://musica-cb289.firebaseio.com/");
        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        Firebase.setAndroidContext(this);

        switch (num) {
            case 0:
                song_name = "Hai Junoon";
                ext = "/Musica/hai_junoon.mp3";
                Rid = R.raw.hai_junoon_lyrics;
                break;

            case 1:
                song_name = "Wonderwall";
                ext = "/Musica/wonderwall.mp3";
                Rid = R.raw.wonderwall_lyrics;
                break;

            default:
                ext = "/Musica/hai_junoon.mp3";
                Rid = R.raw.hai_junoon_lyrics;
                break;

        }


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshotG = dataSnapshot;
                int flag;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    flag = 0;

                    if (child.getKey().equals("Users")) {
                        for (DataSnapshot child2 : child.getChildren()) {
                            for (DataSnapshot child3 : child2.getChildren()) {
                                if (child3.getKey().equals("Email")) {
                                    if (child3.getValue().equals(abc.getString("email"))) {
                                        key = child2.getKey();
                                        flag = 1;
                                        break;
                                    }
                                }
                            }
                            if (flag == 1)
                                break;
                        }
                    }
                    if (flag == 1)
                        break;


                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }


        });


        br = null;
        mediaPlayer = new MediaPlayer();
        path = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            mediaPlayer.setDataSource(path + ext);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        start = true;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        InputStream fis = getResources().openRawResource(Rid);
        br = new BufferedReader(new InputStreamReader(fis));
        try {

            if (br.ready())
                lyric = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Button b1 = (Button) findViewById(R.id.butStop);

        recordAudio = new RecordAudio(num);
        recordAudio.setStarted(true);
        recordAudio.setCANCELLED_FLAG(false);
        recordAudio.execute();

        b1.setOnClickListener(new butClick());


        backgroundOne = (ImageView) findViewById(R.id.background_one);
        backgroundTwo = (ImageView) findViewById(R.id.background_two);
        backgroundThree = (ImageView) findViewById(R.id.background_three);
        backgroundFour = (ImageView) findViewById(R.id.background_four);


        getLyrics();
        getBMP();


        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(3000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();

                //final float translationX = width * 2* progress;
                final float translationX = width * progress;
                backgroundOne.setTranslationX(-translationX);
                backgroundTwo.setTranslationX(-(translationX - width));
                //backgroundThree.setTranslationX(- (translationX - 2*width) );
            }
        });
        animator.start();


    }

    class butClick implements View.OnClickListener {


        @Override
        public void onClick(View view) {

            recordAudio.setStarted(false);
            recordAudio.setCANCELLED_FLAG(true);
            recordAudio.cancel(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mediaPlayer.stop();
            start = false;

            int score = (int) (recordAudio.retScore() * 30);
            score = 100 - score;
            if (score < 0)
                score = 5;

            FileConverter fc = new FileConverter();
            String path;
            path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Musica/yourRec.pcm";
            File file1= new File(path);
            path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Musica/YourRecordings";
            filePath= new File(path);
            fileList = filePath.listFiles();
            int count = fileList.length;
            path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Musica/YourRecordings/yourRec"+(count+1)+".wav";
            File file2= new File(path);
            try {
                fc.rawToWave(file1,file2);
            } catch (IOException e) {
                e.printStackTrace();
            }

            file1.delete();

            AlertDialog.Builder build = new AlertDialog.Builder(KaraokeActivity.this);
            build.setTitle("Congratulations");
            build.setMessage("Your score is: " + score);
            build.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            build.show();
            if(choice!=0)
                UpdateDatabase(score);



        }
    }

    void getBMP() {
        //bmp=recordAudio.retBMP3();
        //backgroundThree.setIm0ageBitmap(bmp);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp;
                bmp = recordAudio.retBMP3();
                backgroundOne.setImageBitmap(bmp);
                bmp = recordAudio.retBMP2();
                backgroundTwo.setImageBitmap(bmp);
                //bmp=recordAudio.retBMP3();
                //backgroundThree.setImageBitmap(bmp);
                getBMP();
            }

        }, 3000);

    }


    void getLyrics() {

        Bitmap pallet = Bitmap.createBitmap(400, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(pallet);
        canvas.drawColor(Color.BLUE);
        Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setStrokeWidth(0.3f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(32);

        if (lyric.length() < 20)
            canvas.drawText(lyric, 200, 100, paint);

        else {

            String sub1, sub2;
            int mid = lyric.length() / 2;
            sub1 = lyric.substring(0, mid);
            sub2 = lyric.substring(mid, lyric.length());
            canvas.drawText(sub1, 200, 85, paint);
            canvas.drawText(sub2, 200, 115, paint);
        }
        backgroundFour.setImageBitmap(pallet);

        try {
            if (br.ready())
                lyric = br.readLine();

            else
                return;

        } catch (IOException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (start)
                    getLyrics();

            }
        }, 5000);
    }

    private void UpdateDatabase(double score) {
        if (dataSnapshotG != null)
            Log.e("manik", "received");
        else
            Log.e("manik", "not received");
        if (choice == 1) {
            Toast.makeText(getApplicationContext(), "challenge sent", Toast.LENGTH_LONG).show();
            String k;
            Challenge challenge = new Challenge(userID, key, num, score);
            k = ref.child("UserChallenges").child(userID).push().getKey();
            ref.child("UserChallenges").child(userID).child(k).setValue(key);
            ref.child("UserChallenges").child(key).child(k).setValue(userID);
            ref.child("Challenges").child(k).setValue(challenge);
        } else if (choice == 2) {
            double scoreA=0;
            Log.e("manikyo222","12345"+abc.getString("ChallengeID"));
            String challengeID = abc.getString("ChallengeID");
            for (DataSnapshot child : dataSnapshotG.getChildren()) {
                if (child.getKey().equals("Challenges")) {
                    for (DataSnapshot child2 : child.getChildren()) {
                        if (child2.getKey().equals(challengeID)) {
                            Challenge challenge = child2.getValue(Challenge.class);
                            challenge.completeChallenge(score);
                            ref.child("Challenges").child(challengeID).setValue(challenge);
                            scoreA = challenge.Score_A;
                            Log.e("manikyoyo",scoreA+"");
                        }
                    }
                }
            }
            AlertDialog.Builder build = new AlertDialog.Builder(KaraokeActivity.this);
            if(score>scoreA)
                build.setTitle("Congratulations You Won!");
            else if(scoreA>score)
                build.setTitle("Alas You Lost!");
            else
                build.setTitle("Its A Draw!");
            build.setMessage("Your score is: " + score + "\nOpponent's score is: " + scoreA);
            build.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            build.show();

        }


    }
}
