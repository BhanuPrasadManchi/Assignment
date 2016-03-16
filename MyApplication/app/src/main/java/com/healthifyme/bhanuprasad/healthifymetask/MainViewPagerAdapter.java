package com.healthifyme.bhanuprasad.healthifymetask;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bhanuprasad on 3/15/2016.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<SlotsObject> slotsObjects = new ArrayList<>();

    public MainViewPagerAdapter(FragmentManager fm, ArrayList<SlotsObject> slotsObjects) {
        super(fm);
        this.slotsObjects = slotsObjects;
    }



    @Override
    public Fragment getItem(int position) {
        return ExpandableFragment.newInstance(slotsObjects.get(position));
    }

    /**
     * This method may be called by the ViewPager to obtain a title string
     * to describe the specified page. This method may return null
     * indicating no title for this page. The default implementation returns
     * null.
     *
     * @param position The position of the title requested
     * @return A title for the requested page
     */
    @Override
    public CharSequence getPageTitle(int position) {
        String string = null; //
        try {
            string = findDay((slotsObjects.get(position)).date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return string;
    }

    @Override
    public int getCount() {
        return slotsObjects.size();
    }


    public String findDay(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(dateString);
        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date);//Thursday
        String stringMonth = (String) android.text.format.DateFormat.format("MMM", date); //Jun
        String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
        String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
        String day = (String) android.text.format.DateFormat.format("dd", date); //20
        return day+"\n"+dayOfTheWeek;
    }
}
