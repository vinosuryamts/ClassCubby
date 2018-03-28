package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Assignments.Teacher_Assignments_Main_Activity;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Attendance.Teacher_Attendance_Main_Activity;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Dashboard.Teachers_Dashboard;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Events.Events_Main_Activity;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.LeaveTracker.Teacher_LeaveTracker_Main_Activity;

/**
 * Created by Vino on 9/26/2017.
 */

public class teacherattendancetabpageadapter extends FragmentPagerAdapter {

    int tabCount;

    //Constructor to the class
    public teacherattendancetabpageadapter(FragmentManager fm, int tabCount) {
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
                result = new Teachers_Dashboard().newInstance("Teachers_Dashboard");
                break;
            case 1:
                result = new Teacher_Attendance_Main_Activity();
                break;
            case 2:
                result = new Teacher_Assignments_Main_Activity().newInstance("Assignments");
                break;
            case 3:
                result = new Teacher_LeaveTracker_Main_Activity().newInstance("Leave Tracker");
                break;
            case 4:
                result = new Events_Main_Activity().newInstance("Events");
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
