package uk.co.yellowrays.collegeinfo.classcubby.WardenAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Dashboard.Parents_Dashboard;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Events.Events_Main_Activity;
import uk.co.yellowrays.collegeinfo.classcubby.WardenAdapter.OutgoingRequests.outgoingrequestclickitems.Warden_Outgoingpermission_Main_Activity;

/**
 * Created by Vino on 9/26/2017.
 */

public class wardentabpageadapter extends FragmentPagerAdapter {

    int tabCount;

    //Constructor to the class
    public wardentabpageadapter(FragmentManager fm, int tabCount) {
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
                result = new Parents_Dashboard().newInstance("Wardens_Dashboard");
                break;
            case 1:
                result = new Events_Main_Activity().newInstance("Events");
                break;
            case 2:
                result = new Warden_Outgoingpermission_Main_Activity().newInstance("Outgoing Permissions");
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
