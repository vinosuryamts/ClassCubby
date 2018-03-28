package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Attendance;

import android.animation.Animator;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.yellowrays.collegeinfo.classcubby.AppStatus;
import uk.co.yellowrays.collegeinfo.classcubby.FontManager.CustomTabLayout;
import uk.co.yellowrays.collegeinfo.classcubby.MainActivity;
import uk.co.yellowrays.collegeinfo.classcubby.R;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.previousloginconfig;
import uk.co.yellowrays.collegeinfo.classcubby.customtabadapter.CustomViewPager;
import uk.co.yellowrays.collegeinfo.classcubby.customtabadapter.DatePickerFragment;

/**
 * Created by Vino on 9/26/2017.
 */

public class Teacher_Attendance_Main_Activity extends android.support.v4.app.Fragment {

    View view;
    CustomViewPager viewpager;
    CustomTabLayout tabLayout;
    TextView attendancepagename,datetext,empty,newrecenttext;
    ListView attendancelistview;
    GridView attendancegridview;
    Snackbar snackbar;
    FloatingActionButton filter;
    Button generate,submit;
    ImageView calendarimage;
    Spinner classspinner,hourspinner;
    RelativeLayout attendancepagemaincontainer,innerfilterlayout;
    RecentAttendanceListViewAdapter recentadapter;


    private int pDay,pMonth,pYear;
    private int previouslistcountvalue=0,currentlistcountvalue=0;
    private JSONArray loginresult;
    Timer t;

    boolean timervalue = false;
    boolean value,generatevalue;

    String classname,classsection,schoolid,datevalue,hourlistspinner,classlistspinner;
    String loogedinusertypeid;
    String attendancetype;
    String attendanceclass,attendancesection,attendancehour,attendancedate;
    String loginuser;

    ArrayList<String> id ,username;
    ArrayList<String> schoolidlist,schoolnamelist,classidlist,classnamelist,sectionidlist,houridlist,hournamelist,absentcountlist,presentcountlist,ondutycountlist,takendatelist;
    ArrayList<String> attendancevaluesfromadapter;
    public ArrayAdapter<String> hourdataAdapter,classdataAdapter;
    public ArrayList<String> ImageList;
    public ArrayList<String> namelist,studentidlist,rollnumberlist,attendancevaluelist;
    public ArrayList<String> classlist;
    public ArrayList<String> hourlist;

    private boolean filterisOpen = false;
    private boolean iscountstarted = false;
    private boolean isgenerateclicked = false;
    private boolean newgenerateclicled = false;
    private static boolean previousattelist = false;

    static final int DATE_DIALOG_ID = 0;
    public static String[] classnewlist;
    public static String[] hournewlist;
    public static String[] prgmNameList1;
    public static String[] prgmImages;
    public static int[] attendancevalues={R.drawable.presenticon,R.drawable.excusedabsenticon,R.drawable.absenticon,R.drawable.ondutyicon};
    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;



    public Teacher_Attendance_Main_Activity() {
        // Required empty public constructor
    }

    public static Teacher_Attendance_Main_Activity newInstance(String param1) {
        Teacher_Attendance_Main_Activity fragment = new Teacher_Attendance_Main_Activity ();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.teacher_attendance_main_xml, null);

        attendancepagename = (TextView)view.findViewById(R.id.attendancepagename);
        attendancelistview = (ListView)view.findViewById(R.id.attendancelistview);
        attendancegridview = (GridView) view.findViewById(R.id.attendancegridview);
        filter = (FloatingActionButton)view.findViewById(R.id.filter);
        generate = (Button) view.findViewById(R.id.generatestudentslist);
        submit = (Button) view.findViewById(R.id.submitstudentslist);
        datetext = (TextView) view.findViewById(R.id.datetext);
        empty = (TextView) view.findViewById(R.id.empty);
        newrecenttext = (TextView) view.findViewById(R.id.newrecenttext);
        calendarimage = (ImageView) view.findViewById(R.id.calendarimage);
        classspinner = (Spinner) view.findViewById(R.id.classspinner);
        hourspinner = (Spinner) view.findViewById(R.id.hourspinner);
        attendancepagemaincontainer = (RelativeLayout) view.findViewById(R.id.attendancepagemaincontainer);
        innerfilterlayout = (RelativeLayout) view.findViewById(R.id.innerfilterlayout);

        filter.setVisibility(View.INVISIBLE);
        filter.setVisibility(View.VISIBLE);
        submit.setVisibility(View.INVISIBLE);
        empty.setVisibility(View.INVISIBLE);
        newrecenttext.setVisibility(View.INVISIBLE);
        attendancegridview.setVisibility(View.INVISIBLE);

        Typeface normaltypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansBold.ttf");
        attendancepagename.setTypeface(boldtypeface);
        datetext.setTypeface(normaltypeface);
        generate.setTypeface(boldtypeface);
        submit.setTypeface(boldtypeface);
        newrecenttext.setTypeface(boldtypeface);

        attendancepagename.setText("Recent Attendance");

        t = new Timer();

        timervalue =true;
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if(isgenerateclicked==false) {
                    getrecentattendancecount();
                }
            }

        },30000,30000);

        getrecentattendance();

        newrecenttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previouslistcountvalue = currentlistcountvalue;
                currentlistcountvalue = 0;
                getrecentattendance();
            }
        });

        RecentAttendanceListViewAdapter.setgenerateclicked(false);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                value = RecentAttendanceListViewAdapter.getclicked();
                generatevalue = RecentAttendanceListViewAdapter.getgenerateclick();

                if (AppStatus.getInstance(getContext()).isOnline()) {

                    if (!filterisOpen) {

                        attendancelistview.setEnabled(false);
                        attendancegridview.setEnabled(false);

                        RecentAttendanceListViewAdapter.setFilterisOpen(true);

                        if(value==true || generatevalue==true){
                            submit.setVisibility(View.INVISIBLE);
                            submit.setClickable(false);
                        }

                        sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                        //creating editor to store values of shared preferences
                        editor = sharedPreferences.edit();

                        editor.putBoolean(loginconfig.key_attendancefilteropen, true);
                        editor.apply();

                        int x = attendancepagemaincontainer.getRight();
                        int y = attendancepagemaincontainer.getBottom();

                        int startRadius = 0;
                        int endRadius = (int) Math.hypot(attendancepagemaincontainer.getWidth(), attendancepagemaincontainer.getHeight());

                        Animator anim = ViewAnimationUtils.createCircularReveal(innerfilterlayout, x, y, startRadius, endRadius);

                        innerfilterlayout.setVisibility(View.VISIBLE);
                        anim.start();

                        filterisOpen = true;

                        filter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                        filter.setImageResource(R.drawable.closepopup);

                        final String classarray = MainActivity.sharedPreferences.getString(loginconfig.key_class_name, "classnamesection");
                        try {
                            loginresult = new JSONArray(classarray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (classarray != null) {
                            try {
                                classlist = new ArrayList<String>(loginresult.length());

                                for (int i = 0; i < loginresult.length(); i++)
                                    try {
                                        JSONObject json = loginresult.getJSONObject(i);

                                        String dashboardtype = json.optString("classname");

                                        classlist.add(dashboardtype);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                classnewlist = new String[classlist.size()];
                                classnewlist = (String[]) classlist.toArray(classnewlist);

                                classdataAdapter = new ArrayAdapter<String>(getContext(),
                                        R.layout.spinner_item, classnewlist);
                                classdataAdapter.setDropDownViewResource(R.layout.spinner_item);
                                classspinner.setAdapter(classdataAdapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                        calendarimage.setEnabled(true);
                        calendarimage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDatePicker();
                            }
                        });

                        final Calendar cal = Calendar.getInstance();
                        pYear = cal.get(Calendar.YEAR);
                        pMonth = cal.get(Calendar.MONTH);
                        pDay = cal.get(Calendar.DAY_OF_MONTH);
                        updateDisplay();

                        gethourlist();


                    }else{

                        RecentAttendanceListViewAdapter.setFilterisOpen(false);

                        attendancelistview.setEnabled(true);
                        attendancegridview.setEnabled(true);

                        if(value==true|| generatevalue==true) {
                            submit.setVisibility(View.VISIBLE);
                            submit.setClickable(true);
                        }else if(value==false){
                            submit.setVisibility(View.INVISIBLE);
                            submit.setClickable(false);
                        }

                        sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                        //creating editor to store values of shared preferences
                        editor = sharedPreferences.edit();

                        editor.putBoolean(loginconfig.key_attendancefilteropen, false);
                        editor.apply();

                        int x = attendancepagemaincontainer.getRight();
                        int y = attendancepagemaincontainer.getBottom();

                        int startRadius = Math.max(attendancepagemaincontainer.getWidth(), attendancepagemaincontainer.getHeight());
                        int endRadius = 0;

                        Animator anim = ViewAnimationUtils.createCircularReveal(innerfilterlayout, x, y, startRadius, endRadius);
                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                innerfilterlayout.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        anim.start();

                        filterisOpen = false;

                        filter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                        filter.setImageResource(R.drawable.filterwhite);

                    }


                }else {
                    snackbar = Snackbar.make(view,"Please Connect to the Internet and Try Again",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            }
        });


        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(classspinner.getSelectedItem() != null) {
                    classlistspinner = classspinner.getSelectedItem().toString().trim();
                }else{
                    classlistspinner = "";
                }

                if(hourspinner.getSelectedItem() != null) {
                    hourlistspinner = hourspinner.getSelectedItem().toString().trim();
                }else{
                    hourlistspinner = "";
                }

                datevalue = datetext.getText().toString();

                if(classlistspinner.equals("") || hourlistspinner.equals("") || hourlistspinner.equals("")){

                    if(filterisOpen==true){
                        attendancelistview.setEnabled(true);
                        attendancegridview.setEnabled(true);

                        if(value==true) {
                            submit.setVisibility(View.VISIBLE);
                            submit.setClickable(true);
                        }else if(value==false){
                            submit.setVisibility(View.INVISIBLE);
                            submit.setClickable(false);
                        }

                        sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                        //creating editor to store values of shared preferences
                        editor = sharedPreferences.edit();

                        editor.putBoolean(loginconfig.key_attendancefilteropen, false);
                        editor.apply();

                        int x = attendancepagemaincontainer.getRight();
                        int y = attendancepagemaincontainer.getBottom();

                        int startRadius = Math.max(attendancepagemaincontainer.getWidth(), attendancepagemaincontainer.getHeight());
                        int endRadius = 0;

                        Animator anim = ViewAnimationUtils.createCircularReveal(innerfilterlayout, x, y, startRadius, endRadius);
                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                innerfilterlayout.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        anim.start();

                        filterisOpen = false;

                        RecentAttendanceListViewAdapter.setFilterisOpen(false);

                        filter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                        filter.setImageResource(R.drawable.filterwhite);
                    }

                    snackbar = Snackbar.make(view,"Selected date doesn't have an hour mapped. Kindly select a working day as per your routine and proceed. ",Snackbar.LENGTH_LONG);
                    snackbar.show();

                }else{


                    if(value==true){
                        RecentAttendanceListViewAdapter.setclicked(false);
                        RecentAttendanceListViewAdapter.setgenerateclicked(true);
                    }

                    if(filterisOpen==true){
                        attendancelistview.setEnabled(true);
                        attendancegridview.setEnabled(true);

                        if(value==true) {
                            submit.setVisibility(View.VISIBLE);
                            submit.setClickable(true);
                        }else if(value==false){
                            submit.setVisibility(View.INVISIBLE);
                            submit.setClickable(false);
                        }

                        sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                        //creating editor to store values of shared preferences
                        editor = sharedPreferences.edit();

                        editor.putBoolean(loginconfig.key_attendancefilteropen, false);
                        editor.apply();

                        int x = attendancepagemaincontainer.getRight();
                        int y = attendancepagemaincontainer.getBottom();

                        int startRadius = Math.max(attendancepagemaincontainer.getWidth(), attendancepagemaincontainer.getHeight());
                        int endRadius = 0;

                        Animator anim = ViewAnimationUtils.createCircularReveal(innerfilterlayout, x, y, startRadius, endRadius);
                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                innerfilterlayout.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        anim.start();

                        filterisOpen = false;

                        RecentAttendanceListViewAdapter.setFilterisOpen(false);

                        filter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                        filter.setImageResource(R.drawable.filterwhite);
                    }


                    classlistspinner = classspinner.getSelectedItem().toString().trim();
                    hourlistspinner = hourspinner.getSelectedItem().toString().trim();
                    datevalue = datetext.getText().toString();
                    schoolid = MainActivity.sharedPreferences.getString(loginconfig.key_school_id, "");


                    String delimiters = "-";
                    String[] wholeclassname = classlistspinner.split(delimiters);

                    classname = wholeclassname[0].trim();
                    classsection = wholeclassname[1].trim();

                    sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                    //creating editor to store values of shared preferences
                    editor = sharedPreferences.edit();

                    editor.putString(loginconfig.key_class, classname);
                    editor.putString(loginconfig.key_section, classsection);
                    editor.putString(loginconfig.key_attendancedate, datevalue);
                    editor.apply();

                    StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_studentdetails_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    JSONObject j = null;
                                    try {
                                        j = new JSONObject(s);
                                        loginresult = j.getJSONArray("result");

                                        namelist = new ArrayList<String>(loginresult.length());
                                        studentidlist = new ArrayList<String>(loginresult.length());
                                        ImageList = new ArrayList<String>(loginresult.length());
                                        rollnumberlist = new ArrayList<String>(loginresult.length());
                                        attendancevaluelist = new ArrayList<String>(loginresult.length());

                                        if (loginresult.length() > 0) {
                                            for (int i = 0; i < loginresult.length(); i++) {
                                                JSONObject json = loginresult.getJSONObject(i);
                                                String studentid = json.optString("userid");
                                                String studentname = json.optString("username");
                                                String studentimage = json.optString("userimage");
                                                String rollnumber = json.optString("rollnumber");
                                                String attendancevalue = json.optString("attendancevalue");
                                                attendancetype = json.optString("value");

                                                studentidlist.add(studentid);
                                                namelist.add(studentname);
                                                ImageList.add(studentimage);
                                                rollnumberlist.add(rollnumber);

                                                if (attendancetype.equals("1")) {
                                                    attendancevaluelist.add(attendancevalue);
                                                } else {
                                                    if (attendancevalue.equals("Excused Absent")) {
                                                        attendancevaluelist.add("Excused Absent");
                                                    } else {
                                                        attendancevaluelist.add("Present");
                                                    }
                                                }
                                            }


                                            empty.setVisibility(View.INVISIBLE);
                                            attendancelistview.setVisibility(View.VISIBLE);
                                            attendancegridview.setVisibility(View.VISIBLE);
                                            submit.setVisibility(View.VISIBLE);

                                            prgmNameList1 = new String[namelist.size()];
                                            prgmNameList1 = (String[]) namelist.toArray(prgmNameList1);

                                            prgmImages = new String[ImageList.size()];
                                            prgmImages = (String[]) ImageList.toArray(prgmImages);

                                            submit.setClickable(true);

                                            attendancepagename.setText("Attendance for Class " + classlistspinner);

                                            attendancegridview.setVisibility(View.VISIBLE);
                                            AttendancetrackerListviewadapter listviewadapter = new AttendancetrackerListviewadapter(getActivity().getApplicationContext(), studentidlist, namelist, ImageList, rollnumberlist, attendancevaluelist, attendancevalues);
                                            attendancegridview.setAdapter(listviewadapter);
                                            attendancegridview.setNumColumns(2);

                                            RecentAttendanceListViewAdapter.setNewgenerateclicled(true);
                                            RecentAttendanceListViewAdapter.setgenerateclicked(true);
                                        } else {

                                            empty.setVisibility(View.VISIBLE);
                                            attendancelistview.setVisibility(View.INVISIBLE);
                                            attendancegridview.setVisibility(View.INVISIBLE);
                                            submit.setVisibility(View.INVISIBLE);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            volleyError.printStackTrace();
                        }
                    })

                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.clear();

                            params.put(loginconfig.key_school_id, schoolid);
                            params.put(loginconfig.key_class, classname);
                            params.put(loginconfig.key_section, classsection);
                            params.put(loginconfig.key_hour_name, hourlistspinner);
                            params.put(loginconfig.key_attendancedate, datevalue);

                            return params;
                        }
                    };
                    RequestQueue studentlist = Volley.newRequestQueue(getActivity().getApplicationContext());
                    studentlist.add(studentrequest1);

                }

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                value = RecentAttendanceListViewAdapter.getclicked();
                generatevalue = RecentAttendanceListViewAdapter.getgenerateclick();

                if(value==false && generatevalue ==true){
                    previousattelist = false;
                }else if(value==true || generatevalue ==true){
                    attendancetype = RecentAttendanceListViewAdapter.getattendancetype();
                    previousattelist = true;
                }else{
                    previousattelist = false;
                }

                submit.setClickable(false);

                if(attendancetype.equals("1"))
                {

                    loginuser = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");
                    schoolid = MainActivity.sharedPreferences.getString(loginconfig.key_school_id, "");

                    if(previousattelist==true){
                        id = RecentAttendanceListViewAdapter.getIdlist();
                        studentidlist = RecentAttendanceListViewAdapter.getIdlist();
                        namelist = RecentAttendanceListViewAdapter.getUsernamelist();
                        username = RecentAttendanceListViewAdapter.getUsernamelist();
                        attendanceclass = RecentAttendanceListViewAdapter.getClassnamevalue();
                        attendancesection = RecentAttendanceListViewAdapter.getClasssectionvalue();
                        attendancehour = RecentAttendanceListViewAdapter.getClasshourvalue();
                        attendancedate = RecentAttendanceListViewAdapter.getClassdate();
                        ImageList = RecentAttendanceListViewAdapter.getUsermageList();
                        rollnumberlist = RecentAttendanceListViewAdapter.getUserrollnumberlist();
                        attendancevaluelist = AttendancetrackerListviewadapter.getAttendancelistvalue();
                    }else {
                        id = studentidlist;
                        username = namelist;
                        attendanceclass = classname;
                        attendancesection = classsection;
                        attendancehour = hourlistspinner;
                        attendancedate = datevalue;
                    }

                    AttendancetrackerListviewadapter adapter = new AttendancetrackerListviewadapter(getActivity().getApplicationContext(), studentidlist, namelist, ImageList,rollnumberlist,attendancevaluelist,attendancevalues);
                    attendancevaluesfromadapter = adapter.getattendanceValues();

                    JSONObject jObject = new JSONObject();

                    try
                    {
                        JSONArray jArry=new JSONArray();
                        for (int i=0;i<attendancevaluesfromadapter.size();i++)
                        {
                            JSONObject jObjd=new JSONObject();
                            jObjd.put("value", attendancevaluesfromadapter.get(i));
                            jObjd.put("createdby", loginuser);
                            jObjd.put("id", id.get(i));
                            jObjd.put("username", username.get(i));
                            jObjd.put("attendanceclass", attendanceclass);
                            jObjd.put("attendancesection", attendancesection);
                            jObjd.put("schoolid", schoolid);
                            jObjd.put("attendancehour", attendancehour);
                            jObjd.put("attendancedate", attendancedate);
                            jArry.put(jObjd);
                        }
                        jObject.put("StudentList", jArry);


                    }
                    catch(JSONException ex)
                    {
                        ex.printStackTrace();
                    }

                    if (AppStatus.getInstance(getActivity().getApplicationContext()).isOnline()) {
                        existingupload(jObject);
                    }else {
                        snackbar = Snackbar.make(view,"Please Connect to the Internet and Try Again",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                }else
                {
                    String loginuser = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");
                    String schoolid = MainActivity.sharedPreferences.getString(loginconfig.key_school_id, "");
                    ArrayList<String> id = studentidlist;
                    ArrayList<String> username = namelist;
                    String attendanceclass = classname;
                    String attendancesection = classsection;
                    String attendancehour = hourlistspinner;
                    String attendancedate = datevalue;

                    AttendancetrackerListviewadapter adapter = new AttendancetrackerListviewadapter(getActivity().getApplicationContext(), studentidlist, namelist, ImageList,rollnumberlist,attendancevaluelist,attendancevalues);
                    attendancevaluesfromadapter = adapter.getattendanceValues();

                    JSONObject jObject = new JSONObject();

                    try
                    {
                        JSONArray jArry=new JSONArray();
                        for (int i=0;i<attendancevaluesfromadapter.size();i++)
                        {
                            JSONObject jObjd=new JSONObject();
                            jObjd.put("value", attendancevaluesfromadapter.get(i));
                            jObjd.put("createdby", loginuser);
                            jObjd.put("id", id.get(i));
                            jObjd.put("username", username.get(i));
                            jObjd.put("attendanceclass", attendanceclass);
                            jObjd.put("attendancesection", attendancesection);
                            jObjd.put("schoolid", schoolid);
                            jObjd.put("attendancehour", attendancehour);
                            jObjd.put("attendancedate", attendancedate);
                            jArry.put(jObjd);
                        }
                        jObject.put("StudentList", jArry);


                    }
                    catch(JSONException ex)
                    {
                        ex.printStackTrace();
                    }

                    if (AppStatus.getInstance(getActivity().getApplicationContext()).isOnline()) {
                        newupload(jObject);
                    }else {
                        snackbar = Snackbar.make(view,"Please Connect to the Internet and Try Again",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                }

            }
        });

        return view;
    }

    private void getrecentattendancecount() {
        deleteCache(getActivity().getApplicationContext());

        sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        final String userid = sharedPreferences.getString(loginconfig.key_userid, "");

        StringRequest newrecentattendance = new StringRequest(Request.Method.POST, loginconfig.key_recentattendance_url,

                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        JSONObject j =null;
                        try
                        {
                            j = new JSONObject(response);
                            loginresult = j.optJSONArray("result");

                            schoolidlist = new ArrayList<String>(loginresult.length());
                            schoolnamelist = new ArrayList<String>(loginresult.length());
                            classidlist = new ArrayList<String>(loginresult.length());
                            classnamelist = new ArrayList<String>(loginresult.length());
                            sectionidlist = new ArrayList<String>(loginresult.length());
                            houridlist = new ArrayList<String>(loginresult.length());
                            hournamelist = new ArrayList<String>(loginresult.length());
                            absentcountlist = new ArrayList<String>(loginresult.length());
                            presentcountlist = new ArrayList<String>(loginresult.length());
                            ondutycountlist = new ArrayList<String>(loginresult.length());
                            takendatelist = new ArrayList<String>(loginresult.length());

                            if(loginresult.length()>0)
                            {
                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);
                                    String schoolid = json.optString("schoolid");
                                    String schoolname = json.optString("schoolname");
                                    String classid = json.optString("classid");
                                    String classname = json.optString("classname");
                                    String sectionid = json.optString("sectionid");
                                    String hourid = json.optString("hourid");
                                    String hourname = json.optString("hourname");
                                    String absentcount = json.optString("absentcount");
                                    String presentcount = json.optString("presentcount");
                                    String ondutycount = json.optString("ondutycount");
                                    String takendate = json.optString("takendate");

                                    schoolidlist.add(schoolid);
                                    schoolnamelist.add(schoolname);
                                    classidlist.add(classid);
                                    classnamelist.add(classname);
                                    sectionidlist.add(sectionid);
                                    houridlist.add(hourid);
                                    hournamelist.add(hourname);
                                    absentcountlist.add(absentcount);
                                    presentcountlist.add(presentcount);
                                    ondutycountlist.add(ondutycount);
                                    takendatelist.add(takendate);

                                    if(currentlistcountvalue!=0 && iscountstarted == false){
                                        currentlistcountvalue = 0;
                                        currentlistcountvalue = currentlistcountvalue+1;
                                        iscountstarted = true;
                                    }else{
                                        iscountstarted = true;
                                        currentlistcountvalue = currentlistcountvalue+1;
                                    }

                                }

                                attendancelistview.setVisibility(View.VISIBLE);
                                attendancegridview.setVisibility(View.VISIBLE);
                                empty.setVisibility(View.INVISIBLE);

                                iscountstarted = false;

                                if(previouslistcountvalue == currentlistcountvalue){

                                }else{
                                    attendancelistview.setOnScrollListener(new AbsListView.OnScrollListener() {
                                        @Override
                                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                                        }

                                        @Override
                                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                            if(firstVisibleItem == 0 && listIsAtTop()){


                                                boolean filtervalueopen = RecentAttendanceListViewAdapter.getisfileropen();
                                                generatevalue = RecentAttendanceListViewAdapter.getgenerateclick();

                                                if(generatevalue == true){

                                                }else {
                                                    if (filtervalueopen == true) {
                                                        attendancelistview.setEnabled(false);
                                                    } else {
                                                        if (currentlistcountvalue > previouslistcountvalue) {
                                                            previouslistcountvalue = currentlistcountvalue;
                                                            newrecenttext.setVisibility(View.INVISIBLE);
                                                            getrecentattendance();
                                                        } else if (currentlistcountvalue == previouslistcountvalue) {
                                                            previouslistcountvalue = currentlistcountvalue;
                                                            newrecenttext.setVisibility(View.INVISIBLE);
                                                        } else if (previouslistcountvalue > currentlistcountvalue) {
                                                            previouslistcountvalue = currentlistcountvalue;
                                                            newrecenttext.setVisibility(View.INVISIBLE);
                                                            getrecentattendance();
                                                        }
                                                    }
                                                }

                                            }else{
                                                boolean filtervalueopen = RecentAttendanceListViewAdapter.getisfileropen();
                                                generatevalue = RecentAttendanceListViewAdapter.getgenerateclick();

                                                if(generatevalue == true){

                                                }else {
                                                    if (filtervalueopen == true) {
                                                        attendancelistview.setEnabled(false);
                                                    } else {
                                                        if (currentlistcountvalue > previouslistcountvalue) {
                                                            int countvalue = currentlistcountvalue - previouslistcountvalue;
                                                            newrecenttext.setVisibility(View.VISIBLE);
                                                            newrecenttext.setText("+ " + countvalue + " new recent attendance");
                                                        } else if (currentlistcountvalue == previouslistcountvalue) {
                                                        } else if (previouslistcountvalue > currentlistcountvalue) {
                                                            int countvalue = previouslistcountvalue - currentlistcountvalue;
                                                            newrecenttext.setVisibility(View.VISIBLE);
                                                            newrecenttext.setText(" " + countvalue + " recent attendance has been modified");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    });

                                    attendancegridview.setOnScrollListener(new AbsListView.OnScrollListener() {
                                        @Override
                                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                                        }

                                        @Override
                                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                            if(firstVisibleItem == 0 && gridlistIsAtTop()){

                                                boolean filtervalueopen = RecentAttendanceListViewAdapter.getisfileropen();
                                                generatevalue = RecentAttendanceListViewAdapter.getgenerateclick();

                                                if(generatevalue == true){

                                                }else {
                                                    if (filtervalueopen == true) {
                                                        attendancegridview.setEnabled(false);
                                                    } else {
                                                        if (currentlistcountvalue > previouslistcountvalue) {
                                                            previouslistcountvalue = currentlistcountvalue;
                                                            newrecenttext.setVisibility(View.INVISIBLE);
                                                            getrecentattendance();
                                                        } else if (currentlistcountvalue == previouslistcountvalue) {
                                                            previouslistcountvalue = currentlistcountvalue;
                                                            newrecenttext.setVisibility(View.INVISIBLE);
                                                        } else if (previouslistcountvalue > currentlistcountvalue) {
                                                            previouslistcountvalue = currentlistcountvalue;
                                                            newrecenttext.setVisibility(View.INVISIBLE);
                                                            getrecentattendance();
                                                        }
                                                    }
                                                }

                                            }else{
                                                boolean filtervalueopen = RecentAttendanceListViewAdapter.getisfileropen();
                                                generatevalue = RecentAttendanceListViewAdapter.getgenerateclick();

                                                if(generatevalue == true){

                                                }else {
                                                    if (filtervalueopen == true) {
                                                        attendancegridview.setEnabled(false);
                                                    } else {
                                                        if (currentlistcountvalue > previouslistcountvalue) {
                                                            int countvalue = currentlistcountvalue - previouslistcountvalue;
                                                            newrecenttext.setVisibility(View.VISIBLE);
                                                            newrecenttext.setText("+ " + countvalue + " new recent attendance");
                                                        } else if (currentlistcountvalue == previouslistcountvalue) {
                                                        } else if (previouslistcountvalue > currentlistcountvalue) {
                                                            int countvalue = previouslistcountvalue - currentlistcountvalue;
                                                            newrecenttext.setVisibility(View.VISIBLE);
                                                            newrecenttext.setText(" " + countvalue + " recent attendance has been modified");
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    });
                                }

                            }else{
                                generatevalue = RecentAttendanceListViewAdapter.getgenerateclick();

                                if(generatevalue == true){

                                }else {
                                    attendancelistview.setVisibility(View.INVISIBLE);
                                    attendancegridview.setVisibility(View.INVISIBLE);
                                    empty.setVisibility(View.VISIBLE);
                                }
                            }

                        } catch (JSONException e) {
                            if (timervalue == true) {
                                t.cancel();
                                t = null;
                                timervalue = false;
                            }
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        if (timervalue == true) {
                            t.cancel();
                            t = null;
                            timervalue = false;
                        }
                        snackbar = Snackbar.make(view,"Connection Response Error",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.clear();
                params.put(loginconfig.key_userid,userid);
                return params;
            }
        };
        RequestQueue recattreq = Volley.newRequestQueue(getActivity().getApplicationContext());
        recattreq.add(newrecentattendance);
    }


    private boolean listIsAtTop()   {
        if(attendancelistview.getChildCount() == 0) return true;
        return attendancelistview.getChildAt(0).getTop() == 0;
    }

    private boolean gridlistIsAtTop()   {
        if(attendancegridview.getChildCount() == 0) return true;
        return attendancegridview.getChildAt(0).getTop() == 0;
    }


    private void getrecentattendance() {
        deleteCache(getActivity().getApplicationContext());

        sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        final String userid = sharedPreferences.getString(loginconfig.key_userid, "");
        final String collegeid = sharedPreferences.getString(loginconfig.key_school_id, "");

        StringRequest newrecentattendance = new StringRequest(Request.Method.POST, loginconfig.key_recentattendance_url,

                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        JSONObject j =null;
                        try
                        {
                            j = new JSONObject(response);
                            loginresult = j.optJSONArray("result");

                            schoolidlist = new ArrayList<String>(loginresult.length());
                            schoolnamelist = new ArrayList<String>(loginresult.length());
                            classidlist = new ArrayList<String>(loginresult.length());
                            classnamelist = new ArrayList<String>(loginresult.length());
                            sectionidlist = new ArrayList<String>(loginresult.length());
                            houridlist = new ArrayList<String>(loginresult.length());
                            hournamelist = new ArrayList<String>(loginresult.length());
                            absentcountlist = new ArrayList<String>(loginresult.length());
                            presentcountlist = new ArrayList<String>(loginresult.length());
                            ondutycountlist = new ArrayList<String>(loginresult.length());
                            takendatelist = new ArrayList<String>(loginresult.length());

                            if(loginresult.length()>0)
                            {
                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);
                                    String schoolid = json.optString("schoolid");
                                    String schoolname = json.optString("schoolname");
                                    String classid = json.optString("classid");
                                    String classname = json.optString("classname");
                                    String sectionid = json.optString("sectionid");
                                    String hourid = json.optString("hourid");
                                    String hourname = json.optString("hourname");
                                    String absentcount = json.optString("absentcount");
                                    String presentcount = json.optString("presentcount");
                                    String ondutycount = json.optString("ondutycount");
                                    String takendate = json.optString("takendate");

                                    schoolidlist.add(schoolid);
                                    schoolnamelist.add(schoolname);
                                    classidlist.add(classid);
                                    classnamelist.add(classname);
                                    sectionidlist.add(sectionid);
                                    houridlist.add(hourid);
                                    hournamelist.add(hourname);
                                    absentcountlist.add(absentcount);
                                    presentcountlist.add(presentcount);
                                    ondutycountlist.add(ondutycount);
                                    takendatelist.add(takendate);

                                    if(currentlistcountvalue!=0 && iscountstarted == false){
                                        currentlistcountvalue = 0;
                                        currentlistcountvalue = currentlistcountvalue+1;
                                        iscountstarted = true;
                                    }else{
                                        iscountstarted = true;
                                        currentlistcountvalue = currentlistcountvalue+1;
                                    }

                                }

                                attendancelistview.setVisibility(View.VISIBLE);
                                attendancegridview.setVisibility(View.VISIBLE);
                                empty.setVisibility(View.INVISIBLE);


                                recentadapter = new RecentAttendanceListViewAdapter(getActivity(),classidlist,classnamelist,houridlist,hournamelist,absentcountlist,presentcountlist,ondutycountlist,takendatelist,attendancelistview,empty,submit,collegeid,attendancepagename,view,attendancepagemaincontainer,filter,innerfilterlayout,attendancegridview);
                                attendancegridview.setAdapter(recentadapter);

                                previouslistcountvalue = currentlistcountvalue;
                                iscountstarted = false;

                            }else{
                                attendancelistview.setVisibility(View.INVISIBLE);
                                attendancegridview.setVisibility(View.INVISIBLE);
                                empty.setVisibility(View.VISIBLE);
                                empty.setText("No attendance have been taken recently. Click on filter icon below and start taking attendance.");
                            }

                        } catch (JSONException e) {
                            if (timervalue == true) {
                                t.cancel();
                                t = null;
                                timervalue = false;
                            }
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        if (timervalue == true) {
                            t.cancel();
                            t = null;
                            timervalue = false;
                        }
                        snackbar = Snackbar.make(view,"Connection Response Error",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.clear();
                params.put(loginconfig.key_userid,userid);
                return params;
            }
        };
        RequestQueue recattreq = Volley.newRequestQueue(getActivity().getApplicationContext());
        recattreq.add(newrecentattendance);

    }


    private void gethourlist() {

        deleteCache(getContext());

        if (classspinner !=null && classspinner.getSelectedItem() != null) {

            sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
            final String classschoolid = sharedPreferences.getString(loginconfig.key_school_id, "");
            final String userid = sharedPreferences.getString(loginconfig.key_userid, "");
            final String school = classschoolid;
            final String initialvalue = classspinner.getSelectedItem().toString().trim();
            final String initialdate = datetext.getText().toString().trim();


            StringRequest newhourstringrequest = new StringRequest(Request.Method.POST, loginconfig.key_hourdetails_url,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject j = null;
                            try {
                                j = new JSONObject(response);
                                loginresult = j.optJSONArray("result");

                                sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                //creating editor to store values of shared preferences
                                editor = sharedPreferences.edit();

                                editor.putString(loginconfig.key_hour_name, loginresult.toString());
                                editor.apply();

                                sethourspinner();

                            } catch (JSONException e) {
                                if (timervalue == true) {
                                    t.cancel();
                                    t = null;
                                    timervalue = false;
                                }
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (timervalue == true) {
                                t.cancel();
                                t = null;
                                timervalue = false;
                            }
                            snackbar = Snackbar.make(view, "Connection Response Error", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.clear();
                    params.put(loginconfig.key_school_id, school);
                    params.put(loginconfig.key_spinnerclassname, initialvalue);
                    params.put(loginconfig.key_spinnerselecteddate, initialdate);
                    params.put(loginconfig.key_userid, userid);
                    return params;
                }
            };
            RequestQueue hourreq = Volley.newRequestQueue(getActivity().getApplicationContext());
            hourreq.add(newhourstringrequest);
        }

    }

    public boolean getfilteropenedvalue(){
        return filterisOpen;
    }

    private void sethourspinner() {

        final String hourarray = MainActivity.sharedPreferences.getString(loginconfig.key_hour_name, "hourname");
        try {
            loginresult = new JSONArray(hourarray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (hourarray != null) {
            try {
                hourlist = new ArrayList<String>(loginresult.length());

                for (int i = 0; i < loginresult.length(); i++)
                    try {
                        JSONObject json = loginresult.getJSONObject(i);

                        String dashboardtype = json.optString("hourname");

                        hourlist.add(dashboardtype);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                hournewlist = new String[hourlist.size()];
                hournewlist = (String[]) hourlist.toArray(hournewlist);

                hourdataAdapter = new ArrayAdapter<String>(getContext(),
                        R.layout.spinner_item, hournewlist);
                hourdataAdapter.setDropDownViewResource(R.layout.spinner_item);
                hourspinner.setAdapter(hourdataAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    private void existingupload(final JSONObject jObject) {

        deleteCache(getActivity().getApplicationContext());

        final String existinguploadvalue = jObject.toString();

        value = RecentAttendanceListViewAdapter.getclicked();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginconfig.key_studentexistingupload_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            attendancelistview.setVisibility(View.VISIBLE);
                            attendancegridview.setVisibility(View.VISIBLE);
                            empty.setVisibility(View.INVISIBLE);
                            submit.setVisibility(View.INVISIBLE);

                            RecentAttendanceListViewAdapter.setgenerateclicked(false);

                            snackbar = Snackbar.make(view,"Attendance Edited Successfully",Snackbar.LENGTH_LONG);
                            snackbar.show();

                            previoussharedPreferences = getActivity().getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);
                            loogedinusertypeid = previoussharedPreferences.getString(previousloginconfig.key_usertypeid, "");

                            sendnexistingnotifications(jObject);
                            getrecentattendance();

                        } catch (Exception e) {
                            snackbar = Snackbar.make(view,"Kindly check with administrator for this error",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(view,"Unable to contact webservice",Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_existingupload, existinguploadvalue);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);

    }


    private void sendnexistingnotifications(JSONObject jObject) {

        deleteCache(getActivity().getApplicationContext());

        final String existinguploadvalue = jObject.toString();

        value = RecentAttendanceListViewAdapter.getclicked();

        StringRequest existingnotireq = new StringRequest(Request.Method.POST, loginconfig.key_attexistinguploadnotification_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {

                            snackbar = Snackbar.make(view,"Notifications sent Successfully",Snackbar.LENGTH_LONG);
                            snackbar.show();

                        } catch (Exception e) {
                            snackbar = Snackbar.make(view,"Kindly check with administrator for this error",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_existingupload, existinguploadvalue);

                return params;
            }

        };
        RequestQueue notifirequest = Volley.newRequestQueue(getActivity().getApplicationContext());
        notifirequest.add(existingnotireq);

    }

    private void sendnewnotifications(JSONObject jObject) {

        deleteCache(getActivity().getApplicationContext());

        final String existinguploadvalue = jObject.toString();

        value = RecentAttendanceListViewAdapter.getclicked();

        StringRequest existingnotireq = new StringRequest(Request.Method.POST, loginconfig.key_attnewuploadnotification_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {

                            snackbar = Snackbar.make(view,"Notifications sent Successfully",Snackbar.LENGTH_LONG);
                            snackbar.show();

                        } catch (Exception e) {
                            snackbar = Snackbar.make(view,"Kindly check with administrator for this error",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_existingupload, existinguploadvalue);

                return params;
            }

        };
        RequestQueue notifirequest = Volley.newRequestQueue(getActivity().getApplicationContext());
        notifirequest.add(existingnotireq);

    }

    private void newupload(final JSONObject jObject) {

        deleteCache(getActivity().getApplicationContext());

        final String existinguploadvalue = jObject.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginconfig.key_studentattendancenewupload_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            attendancelistview.setVisibility(View.VISIBLE);
                            attendancegridview.setVisibility(View.VISIBLE);
                            empty.setVisibility(View.INVISIBLE);
                            submit.setVisibility(View.INVISIBLE);

                            RecentAttendanceListViewAdapter.setgenerateclicked(false);
                            snackbar = Snackbar.make(view,"Attendance Created Successfully",Snackbar.LENGTH_LONG);
                            snackbar.show();

                            sendnewnotifications(jObject);
                            getrecentattendance();

                        } catch (Exception e) {
                            snackbar = Snackbar.make(view,"Kindly check with administrator for this error",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(view,"Unable to contact webservice",Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_existingupload, existinguploadvalue);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    OnDateSetListener ondate = new OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            datetext.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
            gethourlist();
        }
    };

    private OnDateSetListener pDateSetListener =
            new OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;
                    updateDisplay();
                }
            };

    /** Updates the date in the TextView */
    private void updateDisplay() {
        datetext.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pDay).append("-")
                        .append(pMonth + 1).append("-")
                        .append(pYear).append(" "));
        gethourlist();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(timervalue == true) {
            t.cancel();
            t = null;
            timervalue = false;
        }

        RecentAttendanceListViewAdapter.setclicked(false);

    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
