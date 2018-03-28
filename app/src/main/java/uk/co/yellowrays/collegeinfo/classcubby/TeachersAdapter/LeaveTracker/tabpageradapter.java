package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.LeaveTracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by user1 on 18-11-2016.
 */

public class tabpageradapter extends FragmentPagerAdapter {


    int tabCount;

    //Constructor to the class
    public tabpageradapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs

        final Fragment result;

        switch (position) {
            case 0:
                result = new Pending_Leaveletter_tab().newInstance("Pending");
                break;
            case 1:
                result = new Accepted_Leaveletter_tab().newInstance("Accepted");
                break;
            case 2:
                result = new Rejected_Leaveletter_tab().newInstance("Rejected");
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

}
