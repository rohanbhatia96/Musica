package bhatia.rohan.musica;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = (ViewPager)findViewById(R.id.viewPager);
        pager.setAdapter(new myPagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);

    }

    private class myPagerAdapter extends FragmentPagerAdapter
    {

        public myPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {

                case 0: return FirstFragment.newInstance();
                case 1: return SecondFragment.newInstance();
                case 2: return ThirdFragment.newInstance();
                default: return FirstFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public  void changeFrag()
    {
        pager.setCurrentItem(0);
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
