package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Assignments;

import android.animation.Animator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
import uk.co.yellowrays.collegeinfo.classcubby.MainActivity;
import uk.co.yellowrays.collegeinfo.classcubby.R;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;
import uk.co.yellowrays.collegeinfo.classcubby.customtabadapter.DatePickerFragment;

/**
 * Created by Vino on 10/4/2017.
 */

public class Teacher_Assignments_Main_Activity extends android.support.v4.app.Fragment{

    View view;
    RelativeLayout attendancepagemaincontainer,innerfilterlayout;
    TextView assignmentpagename,empty,newrecenttext,datetext;
    GridView assignmentgridview;
    ImageView calendarimage;
    Spinner classspinner,hourspinner;
    Button generate,submit;
    FloatingActionButton filter;
    Snackbar snackbar;
    RecentAssignmentListViewAdapter recentadapter;

    private JSONArray loginresult;
    private Timer t;
    boolean timervalue = false;
    private boolean isgenerateclicked = false;
    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor;

    ArrayList<String> datelist,assignmentidlist,assignmenttitlelist,assignmentdesclist,assignmenttargetlist,
            assignmentsubjectlist,assignmentattachmentlist,assignmentfilenamelist,departmentidlist,departmentnamelist,
            sectionidlist,sectionnamelist;

    ArrayList<String> datelist1,assignmentidlist1,assignmenttitlelist1,assignmentdesclist1,assignmenttargetlist1,
            assignmentsubjectlist1,assignmentattachmentlist1,assignmentfilenamelist1,departmentidlist1,departmentnamelist1,
            sectionidlis1t,sectionnamelist1;
    int previouslistcountvalue=0,currentlistcountvalue=0;
    private boolean iscountstarted = false;
    private boolean filterisOpen = false;
    boolean value,generatevalue;

    public ArrayList<String> classlist;
    public ArrayList<String> hourlist;
    public ArrayAdapter<String> hourdataAdapter,classdataAdapter;
    public static String[] classnewlist;
    public static String[] hournewlist;
    public static String[] prgmNameList1;
    public static String[] prgmImages;
    String classname,classsection,schoolid,datevalue,hourlistspinner,classlistspinner,assignmentgeneratevalue;

    private FragmentActivity myContext;

    private int pDay,pMonth,pYear;
    static final int DATE_DIALOG_ID = 0;




    public Teacher_Assignments_Main_Activity() {
        // Required empty public constructor
    }

    public static Teacher_Assignments_Main_Activity newInstance(String param1) {
        Teacher_Assignments_Main_Activity fragment = new Teacher_Assignments_Main_Activity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.teacher_assignments_main_xml, null);

        attendancepagemaincontainer = (RelativeLayout)view.findViewById(R.id.attendancepagemaincontainer);
        assignmentpagename = (TextView) view.findViewById(R.id.assignmentpagename);
        assignmentgridview = (GridView) view.findViewById(R.id.assignmentgridview);
        empty = (TextView) view.findViewById(R.id.empty);
        newrecenttext = (TextView) view.findViewById(R.id.newrecenttext);
        innerfilterlayout = (RelativeLayout)view.findViewById(R.id.innerfilterlayout);
        calendarimage = (ImageView) view.findViewById(R.id.calendarimage);
        classspinner = (Spinner) view.findViewById(R.id.classspinner);
        hourspinner = (Spinner) view.findViewById(R.id.hourspinner);
        datetext = (TextView) view.findViewById(R.id.datetext);
        generate = (Button) view.findViewById(R.id.generatestudentslist);
        submit = (Button) view.findViewById(R.id.submitstudentslist);
        filter = (FloatingActionButton)view.findViewById(R.id.filter);

        filter.setVisibility(View.INVISIBLE);
        filter.setVisibility(View.VISIBLE);
        submit.setVisibility(View.INVISIBLE);
        empty.setVisibility(View.INVISIBLE);
        newrecenttext.setVisibility(View.INVISIBLE);

        Typeface normaltypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansBold.ttf");
        assignmentpagename.setTypeface(boldtypeface);
        datetext.setTypeface(normaltypeface);
        generate.setTypeface(boldtypeface);
        submit.setTypeface(boldtypeface);
        newrecenttext.setTypeface(boldtypeface);

        assignmentpagename.setText("Recent Assignments");

        t = new Timer();

        timervalue =true;
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if(isgenerateclicked==false) {
                    getrecentassignmentcount();
                }
            }

        },30000,30000);

        getrecentassignment();

        newrecenttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previouslistcountvalue = currentlistcountvalue;
                currentlistcountvalue = 0;
                getrecentassignment();
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
                        assignmentgridview.setEnabled(true);

                        if(value==true) {
                            submit.setVisibility(View.VISIBLE);
                            submit.setClickable(true);
                        }else if(value==false){
                            submit.setVisibility(View.INVISIBLE);
                            submit.setClickable(false);
                        }

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

                        RecentAssignmentListViewAdapter.setFilterisOpen(false);

                        filter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                        filter.setImageResource(R.drawable.filterwhite);
                    }

                    snackbar = Snackbar.make(view,"Selected date doesn't have an hour mapped. Kindly select a working day as per your routine and proceed. ",Snackbar.LENGTH_LONG);
                    snackbar.show();

                }else{

                    if(value==true){
                        RecentAssignmentListViewAdapter.setclicked(false);
                        RecentAssignmentListViewAdapter.setgenerateclicked(true);
                    }

                    if(filterisOpen==true){

                        assignmentgridview.setEnabled(true);

                        if(value==true) {
                            submit.setVisibility(View.VISIBLE);
                            submit.setClickable(true);
                        }else if(value==false){
                            submit.setVisibility(View.INVISIBLE);
                            submit.setClickable(false);
                        }

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

                        RecentAssignmentListViewAdapter.setFilterisOpen(false);

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

                    StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_generateassignments_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    JSONObject j = null;
                                    try {
                                        j = new JSONObject(s);
                                        loginresult = j.getJSONArray("result");

                                        datelist1 = new ArrayList<String>(loginresult.length());
                                        assignmentidlist1 = new ArrayList<String>(loginresult.length());
                                        assignmenttitlelist1 = new ArrayList<String>(loginresult.length());
                                        assignmentdesclist1 = new ArrayList<String>(loginresult.length());
                                        assignmenttargetlist1 = new ArrayList<String>(loginresult.length());
                                        assignmentsubjectlist1 = new ArrayList<String>(loginresult.length());
                                        assignmentattachmentlist1 = new ArrayList<String>(loginresult.length());
                                        assignmentfilenamelist1 = new ArrayList<String>(loginresult.length());

                                        if (loginresult.length() > 0) {
                                            for (int i = 0; i < loginresult.length(); i++) {
                                                JSONObject json = loginresult.getJSONObject(i);
                                                String date = json.optString("date");
                                                String assignmentid = json.optString("Assignmentid");
                                                String assignmenttitle = json.optString("AssignmentTitle");
                                                String assignmentdesc = json.optString("AssignmentDesc");
                                                String assignmenttarget = json.optString("AssignmentTarget");
                                                String assignmentsubject = json.optString("AssignmentSubject");
                                                String assignmentattachment = json.optString("AssignmentAttachment");
                                                String assignmentfilename = json.optString("AssignmentFileName");
                                                String value = json.optString("value");

                                                datelist1.add(date);
                                                assignmentidlist1.add(assignmentid);
                                                assignmenttitlelist1.add(assignmenttitle);
                                                assignmentdesclist1.add(assignmentdesc);
                                                assignmenttargetlist1.add(assignmenttarget);
                                                assignmentsubjectlist1.add(assignmentsubject);
                                                assignmentattachmentlist1.add(assignmentattachment);
                                                assignmentfilenamelist1.add(assignmentfilename);
                                                if(value.equals("0")){
                                                    assignmentgeneratevalue = "New";
                                                }else{
                                                    assignmentgeneratevalue = "Existing";
                                                }

                                            }

                                            //submit.setClickable(true);
                                            RecentAssignmentListViewAdapter.setNewgenerateclicled(true);
                                            RecentAssignmentListViewAdapter.setgenerateclicked(true);
                                            RecentAssignmentListViewAdapter.setclicked(true);

                                            assignmentgridview.setVisibility(View.INVISIBLE);
                                            empty.setVisibility(View.INVISIBLE);

                                            filter.setVisibility(View.INVISIBLE);

                                            boolean isitemclicked = true;

                                            Teacher_Assignments_New_Activity newFragment = new Teacher_Assignments_New_Activity().newInstance("test");
                                            Bundle bundle = new Bundle();
                                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                            bundle.putString("classname",classname);
                                            bundle.putString("sectionname",classsection);
                                            bundle.putString("hourname",hourlistspinner);
                                            bundle.putString("assignmentdate",datevalue);
                                            bundle.putString("assignmenttargetdate",assignmenttargetlist1.get(0));
                                            bundle.putString("serverpathvalue",assignmentattachmentlist1.get(0));
                                            bundle.putString("assignmenttitle",assignmenttitlelist1.get(0));
                                            bundle.putString("assignmentdescription",assignmentdesclist1.get(0));
                                            bundle.putString("assignmentfilename",assignmentfilenamelist1.get(0));
                                            bundle.putString("assignmenttypevalue",assignmentgeneratevalue);
                                            bundle.putBoolean("baseitemclicked",isitemclicked);
                                            newFragment.setArguments(bundle);
                                            transaction.replace(R.id.attendancepagemaincontainer, newFragment);
                                            transaction.addToBackStack("new");
                                            transaction.commit();


                                        } else {
                                            submit.setClickable(true);
                                            RecentAssignmentListViewAdapter.setNewgenerateclicled(true);
                                            RecentAssignmentListViewAdapter.setgenerateclicked(true);
                                            RecentAssignmentListViewAdapter.setclicked(true);

                                            assignmentgridview.setVisibility(View.INVISIBLE);
                                            empty.setVisibility(View.INVISIBLE);

                                            filter.setVisibility(View.INVISIBLE);
                                            myContext = (FragmentActivity)getActivity().getApplicationContext();


                                            boolean isitemclicked = true;

                                            Teacher_Assignments_New_Activity newFragment = new Teacher_Assignments_New_Activity().newInstance("test");
                                            Bundle bundle = new Bundle();
                                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                            bundle.putString("classname",classname);
                                            bundle.putString("sectionname",classsection);
                                            bundle.putString("hourname",hourlistspinner);
                                            bundle.putString("assignmentdate",datevalue);
                                            bundle.putString("assignmenttargetdate","");
                                            bundle.putString("serverpathvalue","");
                                            bundle.putString("assignmenttitle","");
                                            bundle.putString("assignmentdescription","");
                                            bundle.putString("assignmentfilename","");
                                            bundle.putString("assignmenttypevalue","0");
                                            bundle.putBoolean("baseitemclicked",isitemclicked);
                                            newFragment.setArguments(bundle);
                                            transaction.replace(R.id.attendancepagemaincontainer, newFragment);
                                            transaction.addToBackStack("new");
                                            transaction.commit();
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

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                value = RecentAssignmentListViewAdapter.getclicked();
                generatevalue = RecentAssignmentListViewAdapter.getgenerateclick();

                if (AppStatus.getInstance(getContext()).isOnline()) {

                    if (!filterisOpen) {

                        assignmentgridview.setEnabled(false);

                        RecentAssignmentListViewAdapter.setFilterisOpen(true);

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

                        RecentAssignmentListViewAdapter.setFilterisOpen(false);

                        assignmentgridview.setEnabled(true);

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

        return view;
    }


    private void getrecentassignmentcount() {

        deleteCache(getActivity().getApplicationContext());
        sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        final String userid = sharedPreferences.getString(loginconfig.key_userid, "");


        StringRequest newrecentassignment = new StringRequest(Request.Method.POST, loginconfig.key_recentassignments_url,

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

                            datelist = new ArrayList<String>(loginresult.length());
                            assignmentidlist = new ArrayList<String>(loginresult.length());
                            assignmenttitlelist = new ArrayList<String>(loginresult.length());
                            assignmentdesclist = new ArrayList<String>(loginresult.length());
                            assignmenttargetlist = new ArrayList<String>(loginresult.length());
                            assignmentsubjectlist = new ArrayList<String>(loginresult.length());
                            assignmentattachmentlist = new ArrayList<String>(loginresult.length());
                            assignmentfilenamelist = new ArrayList<String>(loginresult.length());

                            if(loginresult.length()>0)
                            {
                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);
                                    String date = json.optString("date");
                                    String assignmentid = json.optString("Assignmentid");
                                    String assignmenttitle = json.optString("AssignmentTitle");
                                    String assignmentdesc = json.optString("AssignmentDesc");
                                    String assignmenttarget = json.optString("AssignmentTarget");
                                    String assignmentsubject = json.optString("AssignmentSubject");
                                    String assignmentattachment = json.optString("AssignmentAttachment");
                                    String assignmentfilename = json.optString("AssignmentFileName");

                                    datelist.add(date);
                                    assignmentidlist.add(assignmentid);
                                    assignmenttitlelist.add(assignmenttitle);
                                    assignmentdesclist.add(assignmentdesc);
                                    assignmenttargetlist.add(assignmenttarget);
                                    assignmentsubjectlist.add(assignmentsubject);
                                    assignmentattachmentlist.add(assignmentattachment);
                                    assignmentfilenamelist.add(assignmentfilename);

                                    if(currentlistcountvalue!=0 && iscountstarted == false){
                                        currentlistcountvalue = 0;
                                        currentlistcountvalue = currentlistcountvalue+1;
                                        iscountstarted = true;
                                    }else{
                                        iscountstarted = true;
                                        currentlistcountvalue = currentlistcountvalue+1;
                                    }

                                }

                                assignmentgridview.setVisibility(View.INVISIBLE);
                                assignmentgridview.setVisibility(View.VISIBLE);
                                empty.setVisibility(View.INVISIBLE);

                                iscountstarted = false;

                                if(previouslistcountvalue == currentlistcountvalue){

                                }else {
                                    assignmentgridview.setOnScrollListener(new AbsListView.OnScrollListener() {
                                        @Override
                                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                                        }

                                        @Override
                                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                            if (firstVisibleItem == 0 && gridlistIsAtTop()) {

                                                boolean filtervalueopen = RecentAssignmentListViewAdapter.getisfileropen();

                                                if (filtervalueopen == true) {
                                                    assignmentgridview.setEnabled(false);
                                                } else {
                                                    if (currentlistcountvalue > previouslistcountvalue) {
                                                        previouslistcountvalue = currentlistcountvalue;
                                                        newrecenttext.setVisibility(View.INVISIBLE);
                                                        getrecentassignment();
                                                    } else if (currentlistcountvalue == previouslistcountvalue) {
                                                        previouslistcountvalue = currentlistcountvalue;
                                                        newrecenttext.setVisibility(View.INVISIBLE);
                                                    } else if (previouslistcountvalue > currentlistcountvalue) {
                                                        previouslistcountvalue = currentlistcountvalue;
                                                        newrecenttext.setVisibility(View.INVISIBLE);
                                                        getrecentassignment();
                                                    }
                                                }

                                            } else {
                                                boolean filtervalueopen = RecentAssignmentListViewAdapter.getisfileropen();

                                                if (filtervalueopen == true) {
                                                    assignmentgridview.setEnabled(false);
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
                                    });

                                }

                            }else{
                                empty.setVisibility(View.INVISIBLE);
                                assignmentgridview.setVisibility(View.INVISIBLE);
                                empty.setVisibility(View.VISIBLE);
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
        RequestQueue recassreq = Volley.newRequestQueue(getActivity().getApplicationContext());
        recassreq.add(newrecentassignment);



    }



    private void getrecentassignment() {

        deleteCache(getActivity().getApplicationContext());
        sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        final String userid = sharedPreferences.getString(loginconfig.key_userid, "");


        StringRequest newrecentassignment = new StringRequest(Request.Method.POST, loginconfig.key_recentassignments_url,

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

                            datelist = new ArrayList<String>(loginresult.length());
                            assignmentidlist = new ArrayList<String>(loginresult.length());
                            assignmenttitlelist = new ArrayList<String>(loginresult.length());
                            assignmentdesclist = new ArrayList<String>(loginresult.length());
                            assignmenttargetlist = new ArrayList<String>(loginresult.length());
                            assignmentsubjectlist = new ArrayList<String>(loginresult.length());
                            assignmentattachmentlist = new ArrayList<String>(loginresult.length());
                            assignmentfilenamelist = new ArrayList<String>(loginresult.length());
                            departmentidlist = new ArrayList<String>(loginresult.length());
                            departmentnamelist = new ArrayList<String>(loginresult.length());
                            sectionidlist = new ArrayList<String>(loginresult.length());
                            sectionnamelist = new ArrayList<String>(loginresult.length());

                            if(loginresult.length()>0)
                            {
                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);
                                    String date = json.optString("date");
                                    String assignmentid = json.optString("Assignmentid");
                                    String assignmenttitle = json.optString("AssignmentTitle");
                                    String assignmentdesc = json.optString("AssignmentDesc");
                                    String assignmenttarget = json.optString("AssignmentTarget");
                                    String assignmentsubject = json.optString("AssignmentSubject");
                                    String assignmentattachment = json.optString("AssignmentAttachment");
                                    String assignmentfilename = json.optString("AssignmentFileName");
                                    String departmentid = json.optString("Departmentid");
                                    String departmentname = json.optString("Departmentname");
                                    String sectionid = json.optString("Sectionid");
                                    String sectionname = json.optString("Sectionname");

                                    datelist.add(date);
                                    assignmentidlist.add(assignmentid);
                                    assignmenttitlelist.add(assignmenttitle);
                                    assignmentdesclist.add(assignmentdesc);
                                    assignmenttargetlist.add(assignmenttarget);
                                    assignmentsubjectlist.add(assignmentsubject);
                                    assignmentattachmentlist.add(assignmentattachment);
                                    assignmentfilenamelist.add(assignmentfilename);
                                    departmentidlist.add(departmentid);
                                    departmentnamelist.add(departmentname);
                                    sectionidlist.add(sectionid);
                                    sectionnamelist.add(sectionname);

                                    if(currentlistcountvalue!=0 && iscountstarted == false){
                                        currentlistcountvalue = 0;
                                        currentlistcountvalue = currentlistcountvalue+1;
                                        iscountstarted = true;
                                    }else{
                                        iscountstarted = true;
                                        currentlistcountvalue = currentlistcountvalue+1;
                                    }

                                }

                                assignmentgridview.setVisibility(View.VISIBLE);
                                empty.setVisibility(View.INVISIBLE);

                                recentadapter = new RecentAssignmentListViewAdapter(getActivity(),datelist,assignmentidlist,assignmenttitlelist,assignmentdesclist,assignmenttargetlist,assignmentsubjectlist,assignmentattachmentlist,departmentidlist,departmentnamelist,sectionidlist,sectionnamelist,assignmentgridview,submit,attendancepagemaincontainer,innerfilterlayout,filter,assignmentpagename,assignmentfilenamelist);
                                assignmentgridview.setAdapter(recentadapter);

                                previouslistcountvalue = currentlistcountvalue;
                                iscountstarted = false;

                            }else{
                                assignmentgridview.setVisibility(View.INVISIBLE);
                                empty.setVisibility(View.VISIBLE);
                                empty.setText("No Assignments have been taken assigned. Click on filter icon below and start assigning assignments.");
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
        RequestQueue recassreq = Volley.newRequestQueue(getActivity().getApplicationContext());
        recassreq.add(newrecentassignment);

    }


    private void gethourlist() {

        deleteCache(getContext());

        if (classspinner != null && classspinner.getSelectedItem() !=null) {

            sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
            final String classschoolid = sharedPreferences.getString(loginconfig.key_school_id, "");
            final String userid = sharedPreferences.getString(loginconfig.key_userid, "");
            final String school = classschoolid;
            final String initialvalue = classspinner.getSelectedItem().toString().trim();
            final String initialdate = datetext.getText().toString().trim();


            StringRequest newhourstringrequest = new StringRequest(Request.Method.POST, loginconfig.key_Subjectdetails_url,

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

                                editor.putString(loginconfig.key_subjectname_name, loginresult.toString());
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
                    params.put(loginconfig.key_userid, userid);
                    params.put(loginconfig.key_spinnerclassname, initialvalue);
                    params.put(loginconfig.key_spinnerselecteddate, initialdate);
                    return params;
                }
            };
            RequestQueue hourreq = Volley.newRequestQueue(getActivity().getApplicationContext());
            hourreq.add(newhourstringrequest);
        }

    }

    private void sethourspinner() {

        final String hourarray = MainActivity.sharedPreferences.getString(loginconfig.key_subjectname_name, "sectionname");
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

                        String dashboardtype = json.optString("subjectname");

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(timervalue == true) {
            t.cancel();
            t = null;
            timervalue = false;
        }

        RecentAssignmentListViewAdapter.setclicked(false);

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

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            datetext.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
            gethourlist();
        }
    };

    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

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

    private boolean gridlistIsAtTop()   {
        if(assignmentgridview.getChildCount() == 0) return true;
        return assignmentgridview.getChildAt(0).getTop() == 0;
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
