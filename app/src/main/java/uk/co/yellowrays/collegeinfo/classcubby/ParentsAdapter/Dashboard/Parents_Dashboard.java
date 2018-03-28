package uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Dashboard;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import uk.co.yellowrays.collegeinfo.classcubby.AppStatus;
import uk.co.yellowrays.collegeinfo.classcubby.MainActivity;
import uk.co.yellowrays.collegeinfo.classcubby.R;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Dashboard.Dashboardgridviewapdapter;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.previousloginconfig;

/**
 * Created by Vino on 10/24/2017.
 */

public class Parents_Dashboard extends android.support.v4.app.Fragment {

    public GridView gv;
    View mainview,view;
    TextView dashboardpagename;
    Snackbar snackbar;
    String loogedinusertypeid;
    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;


    public Parents_Dashboard() {
        // Required empty public constructor
    }

    public static Parents_Dashboard newInstance(String param1) {
        Parents_Dashboard fragment = new Parents_Dashboard();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.teachers_dashboard_main_xml, null);

        gv = (GridView) view.findViewById(R.id.dashboardgridview);
        mainview = view.findViewById(R.id.teachersdashboardcontainer);

        dashboardpagename = (TextView) view.findViewById(R.id.dashboardpagename);
        Typeface normaltypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansBold.ttf");
        dashboardpagename.setTypeface(boldtypeface);

        previoussharedPreferences = getActivity().getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);
        loogedinusertypeid = previoussharedPreferences.getString(previousloginconfig.key_usertypeid, "");


        if (AppStatus.getInstance(getActivity().getApplicationContext()).isOnline()) {
            if(loogedinusertypeid.equals("2")){
                String[] prgmNameList = {"Attendance","Assignments","Leave Tracker","Events"};
                String[] prgmImages = {"http://www.gamcngl.com/Webservice/Envy/images/teacherattendance.jpg","http://www.gamcngl.com/Webservice/Envy/images/assignment.jpg","http://www.gamcngl.com/Webservice/Envy/images/leave.jpg","http://www.gamcngl.com/Webservice/Envy/images/events.png"};
                gv.setAdapter(new Dashboardgridviewapdapter(getActivity(), prgmNameList, prgmImages));
            }else if(loogedinusertypeid.equals("3")){
                String[] prgmNameList = {"Attendance","Assignments","Leave Tracker","Events","Hostel outgoing Permisison"};
                String[] prgmImages = {"http://www.gamcngl.com/Webservice/Envy/images/teacherattendance.jpg","http://www.gamcngl.com/Webservice/Envy/images/assignment.jpg","http://www.gamcngl.com/Webservice/Envy/images/leave.jpg","http://www.gamcngl.com/Webservice/Envy/images/events.png","http://www.gamcngl.com/Webservice/Envy/images/vacation.png"};
                gv.setAdapter(new Dashboardgridviewapdapter(getActivity(), prgmNameList, prgmImages));
            }else if(loogedinusertypeid.equals("4")){
                String[] prgmNameList = {"Attendance","Assignments","Leave Tracker","Events","Hostel outgoing Permisison"};
                String[] prgmImages = {"http://www.gamcngl.com/Webservice/Envy/images/teacherattendance.jpg","http://www.gamcngl.com/Webservice/Envy/images/assignment.jpg","http://www.gamcngl.com/Webservice/Envy/images/leave.jpg","http://www.gamcngl.com/Webservice/Envy/images/events.png","http://www.gamcngl.com/Webservice/Envy/images/vacation.png"};
                gv.setAdapter(new Dashboardgridviewapdapter(getActivity(), prgmNameList, prgmImages));
            }else if(loogedinusertypeid.equals("9")||loogedinusertypeid.equals("10")){
                String[] prgmNameList = {"Events","Hostel outgoing Permisison"};
                String[] prgmImages = {"http://www.gamcngl.com/Webservice/Envy/images/events.png","http://www.gamcngl.com/Webservice/Envy/images/vacation.png"};
                gv.setAdapter(new Dashboardgridviewapdapter(getActivity(), prgmNameList, prgmImages));
            }
        }else{
            snackbar = Snackbar.make(mainview,"Please Connect to the Internet and Try Again",Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        return view;
    }

}
