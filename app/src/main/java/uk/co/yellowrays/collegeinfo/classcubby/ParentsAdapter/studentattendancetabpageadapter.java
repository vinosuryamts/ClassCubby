package uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Assignments.Parents_Assignment_Main_Activity;
import uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Attendance.Parents_Attendance_Main;
import uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Dashboard.Parents_Dashboard;
import uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Leavetracker.Parents_Leavetracker_main;
import uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Outgoingpermission.Parents_Outgoing_main;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Events.Events_Main_Activity;

/**
 * Created by Vino on 9/26/2017.
 */

public class studentattendancetabpageadapter extends FragmentPagerAdapter {

    int tabCount;
    private boolean _hasLoadedOnce= false;

    //Constructor to the class
    public studentattendancetabpageadapter(FragmentManager fm, int tabCount) {
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
                result = new Parents_Dashboard().newInstance("Parents_Dashboard");
                _hasLoadedOnce = true;
                break;
            case 1:
                result = new Parents_Attendance_Main();
                break;
            case 2:
                result = new Parents_Assignment_Main_Activity().newInstance("Parents_Assignments");
                break;
            case 3:
                result = new Parents_Leavetracker_main().newInstance("Parents_Leave");
                break;
            case 4:
                result = new Events_Main_Activity().newInstance("Events");
                break;
            case 5:
                result = new Parents_Outgoing_main().newInstance("Outgoing");
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
