package com.example.feliz.checked_in;

/**
 * Created by Thulani Maphanga on 2017/05/13.
 */

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
  * Created by hp1 on 21-01-2015.
  */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

      CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
      int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {

        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

        }

    public Fragment getItem(int position) {

        if(position == 0)// if the position is 0 we are returning the First tab
        {
            tab1 tab1 = new tab1();
            return tab1;
            }
        else// As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            tab2 tab2 = new tab2();
            return tab2;
        }
    }

    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }


    public int getCount() {
        return NumbOfTabs;
    }
}