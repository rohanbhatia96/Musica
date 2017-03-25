package bhatia.rohan.musica;

/**
 * Created by Rohan on 26-Nov-16.
 */

public class Challenge {
    public String Challenger_A;
    public String Challenger_B;
    public Integer Song;
    public double Score_A;
    public double Score_B;
    public String Status_A;
    public String Status_B;

    public Challenge()
    {

    }
    public Challenge(String A,String B,Integer S,double SA)
    {
        Challenger_A = A;
        Challenger_B = B;
        Song = S;
        Score_A = SA;
        Score_B = 0.0;
        Status_A = "Pending";
        Status_B = "Pending";
    }
    public void rejectChallenge()
    {
        Status_B = "Rejected";
        Status_A = "Rejected";
    }
    public void completeChallenge(double SB)
    {
        Score_B = SB;
        if(Score_A>Score_B)
        {
            Status_A = "Won";
            Status_B = "Lost";
        }
        else if(Score_A<Score_B)
        {
            Status_A = "Lost";
            Status_B = "Won";
        }
        else
        {
            Status_A = "Draw";
            Status_B = "Draw";
        }
    }
}

