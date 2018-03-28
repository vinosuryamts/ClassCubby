package uk.co.yellowrays.collegeinfo.classcubby.WardenAdapter.OutgoingRequests.outgoingrequestclickitems;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import uk.co.yellowrays.collegeinfo.classcubby.WardenAdapter.OutgoingRequests.Accepted_Leaveletter_tab;
import uk.co.yellowrays.collegeinfo.classcubby.WardenAdapter.OutgoingRequests.Pending_Leaveletter_tab;
import uk.co.yellowrays.collegeinfo.classcubby.WardenAdapter.OutgoingRequests.Rejected_Leaveletter_tab;


/**
 * Created by Vino on 9/26/2017.
 */

public class outgoingrequesttabpageadapter extends FragmentPagerAdapter {

    int tabCount;

    //Constructor to the class
    public outgoingrequesttabpageadapter(FragmentManager fm, int tabCount) {
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
