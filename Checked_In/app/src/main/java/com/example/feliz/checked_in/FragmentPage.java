package com.example.feliz.checked_in;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Feliz on 2017/09/14.
 */

public class FragmentPage extends FragmentPagerAdapter {

    public FragmentPage(FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
           //     return new TodayFeeds();
            case 1:
             //   return new UpComingFeeds();
            case 2:
               // return new PastFeeds();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
