package uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Leavetracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.yellowrays.collegeinfo.classcubby.AppStatus;
import uk.co.yellowrays.collegeinfo.classcubby.MainActivity;
import uk.co.yellowrays.collegeinfo.classcubby.R;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;

/**
 * Created by Vino on 10/24/2017.
 */

public class Parents_Leavetracker_main extends android.support.v4.app.Fragment {

    public ListView mRecyclerView;
    View mainview,view;
    TextView emptycriteriamsg,leavepagename;
    Button newleavetrackerbutton;
    FloatingActionButton fab;
    Snackbar snackbar;
    ProgressDialog loginDialog;
    RelativeLayout parentsleavetrackerwholecontainer;
    parentsleavetrackerlistviewadapter adapter;

    Timer t;
    boolean timervalue = false;
    private JSONArray loginresult;
    int loadingstate = 0,oldposition=0,newclickevent=0;
    int loadingstatevalue = 0;
    String userid,singleuserimage,fromhourvalue,tohourvalue;
    private String newuserimage;
    String loogedinusertypeid;
    String singlestudentidtext;
    String count;

    String[] strstudentid,strstudentname,strclassname,strclassid,strschoolid,strschoolname,strleaveorderdate,strfromdate,strtodate,strfromhour,strtohour;
    String[] strtotaldays,strleavereason,strrejectedreason,strleavesubject,strleavestatus,strleaverequesteddate,strstudentimage;
    ArrayList<String> arrstudentid,arrstudentname,arrclassname,arrclassid,arrschoolid,arrschoolname,arrleaveorderdate,arrfromdate,arrtodate,arrfromhour,arrtohour;
    ArrayList<String> arrtotaldays,arrleavereason,arrrejectedreason,arrleavesubject,arrleavestatus,arrleaverequesteddate,arrstudentimage;
    String studentid,studentname ,classname,classid,schoolid,schoolname,leaveorderdate,fromdate,todate,fromhour,tohour;
    String totaldays,leavereason,rejectedreason,leavesubject,leavestatus,leaverequesteddate,studentimage;


    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;


    public Parents_Leavetracker_main() {
        // Required empty public constructor
    }

    public static Parents_Leavetracker_main newInstance(String param1) {
        Parents_Leavetracker_main fragment = new Parents_Leavetracker_main();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.parents_leavetracker_main_xml, null);

        parentsleavetrackerwholecontainer = (RelativeLayout)view.findViewById(R.id.parentsleavetrackerwholecontainer);
        mRecyclerView = (ListView) view.findViewById(R.id.mrecyclerView);
        emptycriteriamsg = (TextView)view.findViewById(R.id.emptycriteriamsg);
        leavepagename = (TextView)view.findViewById(R.id.leavepagename);
        newleavetrackerbutton = (Button)view.findViewById(R.id.newleavetrackerbutton);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        mainview = view.findViewById(R.id.parentsleavetrackerwholecontainer);

        fab.setVisibility(View.INVISIBLE);
        newleavetrackerbutton.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);

        Typeface normaltypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansBold.ttf");
        leavepagename.setTypeface(boldtypeface);
        emptycriteriamsg.setTypeface(normaltypeface);

        if (AppStatus.getInstance(getActivity().getApplicationContext()).isOnline()) {
            oldposition = 0;
            getLeavevalues();
        }else {
            Toast.makeText(getActivity(),"Please Connect to the Internet and Try Again",Toast.LENGTH_LONG).show();
        }

        t = new Timer();
        timervalue =true;
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                getleavesupdatestudentdetails();
            }

        },5000,5000);

        newleavetrackerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingstate = 1;
                leaveupdatestudentdetails();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setVisibility(View.INVISIBLE);
                Parents_Leavetracker_rowitems_click.setgenerateclicked(true);

                Parents_Leavetracker_submit newFragment = new Parents_Leavetracker_submit().newInstance("test");
                Bundle bundle = new Bundle();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.parentsleavetrackerwholecontainer, newFragment);
                transaction.addToBackStack("new");
                transaction.commit();
            }
        });


        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                singleuserimage = MainActivity.sharedPreferences.getString(loginconfig.key_leavesinglestudentnewuserimage, "");

                newuserimage = singleuserimage;

                newuserimage = newuserimage.replace("[","");
                newuserimage = newuserimage.replace("]","");
                newuserimage = newuserimage.replace("\"","");

                if(strfromhour[position].equals("Forenoon")){
                    fromhourvalue = "FN";
                }else{
                    fromhourvalue = "AN";
                }

                if(strtohour[position].equals("Forenoon")){
                    tohourvalue = "FN";
                }else{
                    tohourvalue = "AN";
                }

                Parents_Leavetracker_rowitems_click.setclicked(true);
                fab.setVisibility(View.INVISIBLE);

                Parents_Leavetracker_rowitems_click newFragment = new Parents_Leavetracker_rowitems_click().newInstance("test");
                Bundle bundle = new Bundle();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                bundle.putString("timesubmitted", strleaverequesteddate[position]);
                bundle.putString("userimage", strstudentimage[position]);
                bundle.putString("studentname", strstudentname[position]);
                bundle.putString("fromdate", strfromdate[position]+" "+fromhourvalue);
                bundle.putString("todate", strtodate[position]+" "+tohourvalue);
                bundle.putString("leavedetail", strleavereason[position]);
                bundle.putString("leavesubject", strleavesubject[position]);
                bundle.putString("rejectreason", strrejectedreason[position]);
                bundle.putString("status", strleavestatus[position]);
                newFragment.setArguments(bundle);
                transaction.replace(R.id.parentsleavetrackerwholecontainer, newFragment);
                transaction.addToBackStack("new");
                transaction.commit();

            }

        });


        return view;
    }


    private void leaveupdatestudentdetails() {
        deleteCache(getActivity().getApplicationContext());
        singlestudentidtext = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_parentsleave_update_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            newleavetrackerbutton.setVisibility(View.INVISIBLE);
                            newclickevent = 1;
                            loadingstate = 1;

                            loginDialog = new ProgressDialog(getActivity());
                            loginDialog.setIndeterminate(true);
                            loginDialog.setTitle("Please Wait");
                            loginDialog.setMessage("Loading Leave Tracker...");
                            loginDialog.setCancelable(false);
                            loginDialog.setCanceledOnTouchOutside(false);
                            loginDialog.show();

                            getLeavevalues();
                        } catch (Exception e) {
                            snackbar = Snackbar.make(mainview, "Unable to reach Server Please Try Again", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(mainview, "Unable Connect to Internet. Please Try Again", Snackbar.LENGTH_LONG);
                snackbar.show();
                volleyError.printStackTrace();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_leavesinglestudentid, singlestudentidtext);

                return params;
            }
        };
        RequestQueue studentlist = Volley.newRequestQueue(getActivity().getApplicationContext());
        studentlist.add(studentrequest1);
    }

    private void getleavesupdatestudentdetails() {
        deleteCache(getActivity().getApplicationContext());
        singlestudentidtext = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        StringRequest leavesupdaterequest1 = new StringRequest(Request.Method.POST, loginconfig.key_parentsleave_updatedetails_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            loginresult = j.getJSONArray("result");

                            if (loginresult.length() > 0) {
                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);
                                    count = json.optString("Count");
                                }


                                if(count.equals("0")){

                                }else{
                                    mRecyclerView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                        @Override
                                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                                        }

                                        @Override
                                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                            if(firstVisibleItem == 0 && listIsAtTop()){
                                                oldposition = 0;
                                                newleavetrackerbutton.setVisibility(View.INVISIBLE);
                                                leaveupdatestudentdetails();
                                            }else{
                                                newleavetrackerbutton.setVisibility(View.VISIBLE);
                                                newleavetrackerbutton.setText(count + " New / Updated Leave Request(s)");
                                            }
                                        }
                                    });


                                }

                            }

                        } catch (JSONException e) {
                            snackbar = Snackbar.make(mainview, "Unable to reach Server Please Try Again", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(mainview, "Unable Connect to Internet. Please Try Again", Snackbar.LENGTH_LONG);
                snackbar.show();
                volleyError.printStackTrace();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_leavesinglestudentid, singlestudentidtext);

                return params;
            }
        };
        RequestQueue leavesupdate = Volley.newRequestQueue(getActivity().getApplicationContext());
        leavesupdate.add(leavesupdaterequest1);
    }

    private boolean listIsAtTop()   {
        if(mRecyclerView.getChildCount() == 0) return true;
        return mRecyclerView.getChildAt(0).getTop() == 0;
    }

    private void getLeavevalues() {
        deleteCache(getActivity().getApplicationContext());
        singlestudentidtext = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        singlestudentidtext = singlestudentidtext.replace("[","");
        singlestudentidtext = singlestudentidtext.replace("]","");
        singlestudentidtext = singlestudentidtext.replace("\"","");

        StringRequest Leavevaluesrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_parentsleave_details_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            loginresult = j.getJSONArray("result");

                            if (loginresult.length() > 0) {

                                arrstudentid = new ArrayList<String>(loginresult.length());
                                arrstudentname = new ArrayList<String>(loginresult.length());
                                arrclassname = new ArrayList<String>(loginresult.length());
                                arrclassid = new ArrayList<String>(loginresult.length());
                                arrschoolid = new ArrayList<String>(loginresult.length());
                                arrschoolname = new ArrayList<String>(loginresult.length());
                                arrleaveorderdate = new ArrayList<String>(loginresult.length());
                                arrfromdate = new ArrayList<String>(loginresult.length());
                                arrtodate = new ArrayList<String>(loginresult.length());
                                arrfromhour = new ArrayList<String>(loginresult.length());
                                arrtohour = new ArrayList<String>(loginresult.length());
                                arrtotaldays = new ArrayList<String>(loginresult.length());
                                arrleavereason = new ArrayList<String>(loginresult.length());
                                arrrejectedreason = new ArrayList<String>(loginresult.length());
                                arrleavesubject = new ArrayList<String>(loginresult.length());
                                arrleavestatus = new ArrayList<String>(loginresult.length());
                                arrleaverequesteddate = new ArrayList<String>(loginresult.length());
                                arrstudentimage = new ArrayList<String>(loginresult.length());



                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);

                                    schoolid = json.optString("schoolid");
                                    schoolname = json.optString("schoolname");
                                    classid = json.optString("classid");
                                    classname = json.optString("classname");
                                    studentid = json.optString("studentid");
                                    studentname = json.optString("studentname");
                                    leaveorderdate = json.optString("leaveorderdate");
                                    fromdate = json.optString("fromdate");
                                    todate = json.optString("todate");
                                    fromhour = json.optString("fromhour");
                                    tohour = json.optString("tohour");
                                    totaldays = json.optString("totaldays");
                                    leavereason = json.optString("leavereason");
                                    rejectedreason = json.optString("rejectedreason");
                                    leavesubject = json.optString("leavesubject");
                                    leavestatus = json.optString("leavestatus");
                                    leaverequesteddate = json.optString("requesteddate");
                                    studentimage = json.optString("studentimage");

                                    arrschoolid.add(schoolid);
                                    arrschoolname.add(schoolname);
                                    arrclassid.add(classid);
                                    arrclassname.add(classname);
                                    arrstudentid.add(studentid);
                                    arrstudentname.add(studentname);
                                    arrleaveorderdate.add(leaveorderdate);
                                    arrfromdate.add(fromdate);
                                    arrtodate.add(todate);
                                    arrfromhour.add(fromhour);
                                    arrtohour.add(tohour);
                                    arrtotaldays.add(totaldays);
                                    arrleavereason.add(leavereason);
                                    arrrejectedreason.add(rejectedreason);
                                    arrleavesubject.add(leavesubject);
                                    arrleavestatus.add(leavestatus);
                                    arrleaverequesteddate.add(leaverequesteddate);
                                    arrstudentimage.add(studentimage);
                                }

                                sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                //creating editor to store values of shared preferences
                                editor = sharedPreferences.edit();

                                editor.putString(loginconfig.key_leave_schoolid, arrschoolid.toString());
                                editor.putString(loginconfig.key_leave_schoolname, arrschoolname.toString());
                                editor.putString(loginconfig.key_leave_classid, arrclassid.toString());
                                editor.putString(loginconfig.key_leave_classname, arrclassname.toString());
                                editor.putString(loginconfig.key_leave_studentid, arrstudentid.toString());
                                editor.putString(loginconfig.key_leave_studentname, arrstudentname.toString());
                                editor.putString(loginconfig.key_leave_leaveorderdate, arrleaveorderdate.toString());
                                editor.putString(loginconfig.key_leave_fromdate, arrfromdate.toString());
                                editor.putString(loginconfig.key_leave_todate, arrtodate.toString());
                                editor.putString(loginconfig.key_leave_fromhour, arrfromhour.toString());
                                editor.putString(loginconfig.key_leave_tohour, arrtohour.toString());
                                editor.putString(loginconfig.key_leave_totaldays, arrtotaldays.toString());
                                editor.putString(loginconfig.key_leave_leavereason, arrleavereason.toString());
                                editor.putString(loginconfig.key_leave_rejectedreason, arrrejectedreason.toString());
                                editor.putString(loginconfig.key_leave_leavesubject, arrleavesubject.toString());
                                editor.putString(loginconfig.key_leave_leavestatus, arrleavestatus.toString());
                                editor.putString(loginconfig.key_leave_leaverequesteddate, arrleaverequesteddate.toString());
                                editor.putString(loginconfig.key_leave_userimage, arrstudentimage.toString());
                                editor.apply();

                                schoolid = MainActivity.sharedPreferences.getString(loginconfig.key_leave_schoolid, "");
                                schoolname = MainActivity.sharedPreferences.getString(loginconfig.key_leave_schoolname, "");
                                classid = MainActivity.sharedPreferences.getString(loginconfig.key_leave_classid, "");
                                classname = MainActivity.sharedPreferences.getString(loginconfig.key_leave_classname, "");
                                studentid = MainActivity.sharedPreferences.getString(loginconfig.key_leave_studentid, "");
                                studentname = MainActivity.sharedPreferences.getString(loginconfig.key_leave_studentname, "");
                                leaveorderdate = MainActivity.sharedPreferences.getString(loginconfig.key_leave_leaveorderdate, "");
                                fromdate = MainActivity.sharedPreferences.getString(loginconfig.key_leave_fromdate, "");
                                todate = MainActivity.sharedPreferences.getString(loginconfig.key_leave_todate, "");
                                fromhour = MainActivity.sharedPreferences.getString(loginconfig.key_leave_fromhour, "");
                                tohour = MainActivity.sharedPreferences.getString(loginconfig.key_leave_tohour, "");
                                totaldays = MainActivity.sharedPreferences.getString(loginconfig.key_leave_totaldays, "");
                                leavereason = MainActivity.sharedPreferences.getString(loginconfig.key_leave_leavereason, "");
                                rejectedreason = MainActivity.sharedPreferences.getString(loginconfig.key_leave_rejectedreason, "");
                                leavesubject = MainActivity.sharedPreferences.getString(loginconfig.key_leave_leavesubject, "");
                                leavestatus = MainActivity.sharedPreferences.getString(loginconfig.key_leave_leavestatus, "");
                                leaverequesteddate = MainActivity.sharedPreferences.getString(loginconfig.key_leave_leaverequesteddate, "");
                                studentimage = MainActivity.sharedPreferences.getString(loginconfig.key_leave_userimage, "");


                                schoolid = schoolid.replace("[", "");
                                schoolid = schoolid.replace("]", "");
                                schoolid = schoolid.replace("\"", "");
                                schoolid = schoolid.replace(", ", ",");
                                strschoolid = schoolid.split(",");

                                schoolname = schoolname.replace("[", "");
                                schoolname = schoolname.replace("]", "");
                                schoolname = schoolname.replace("\"", "");
                                schoolname = schoolname.replace(", ", ",");
                                strschoolname = schoolname.split(",");

                                classid = classid.replace("[", "");
                                classid = classid.replace("]", "");
                                classid = classid.replace("\"", "");
                                classid = classid.replace(", ", ",");
                                strclassid = classid.split(",");


                                classname = classname.replace("[", "");
                                classname = classname.replace("]", "");
                                classname = classname.replace("\"", "");
                                classname = classname.replace(", ", ",");
                                strclassname = classname.split(",");


                                studentid = studentid.replace("[", "");
                                studentid = studentid.replace("]", "");
                                studentid = studentid.replace("\"", "");
                                studentid = studentid.replace(", ", ",");
                                strstudentid = studentid.split(",");

                                studentname = studentname.replace("[", "");
                                studentname = studentname.replace("]", "");
                                studentname = studentname.replace("\"", "");
                                studentname = studentname.replace(", ", ",");
                                strstudentname = studentname.split(",");

                                leaveorderdate = leaveorderdate.replace("[", "");
                                leaveorderdate = leaveorderdate.replace("]", "");
                                leaveorderdate = leaveorderdate.replace("\"", "");
                                leaveorderdate = leaveorderdate.replace(", ", ",");
                                strleaveorderdate = leaveorderdate.split(",");

                                fromdate = fromdate.replace("[", "");
                                fromdate = fromdate.replace("]", "");
                                fromdate = fromdate.replace("\"", "");
                                fromdate = fromdate.replace(", ", ",");
                                strfromdate = fromdate.split(",");

                                todate = todate.replace("[", "");
                                todate = todate.replace("]", "");
                                todate = todate.replace("\"", "");
                                todate = todate.replace(", ", ",");
                                strtodate = todate.split(",");

                                fromhour = fromhour.replace("[", "");
                                fromhour = fromhour.replace("]", "");
                                fromhour = fromhour.replace("\"", "");
                                fromhour = fromhour.replace(", ", ",");
                                strfromhour = fromhour.split(",");

                                tohour = tohour.replace("[", "");
                                tohour = tohour.replace("]", "");
                                tohour = tohour.replace("\"", "");
                                tohour = tohour.replace(", ", ",");
                                strtohour = tohour.split(",");

                                totaldays = totaldays.replace("[", "");
                                totaldays = totaldays.replace("]", "");
                                totaldays = totaldays.replace("\"", "");
                                totaldays = totaldays.replace(", ", ",");
                                strtotaldays = totaldays.split(",");

                                leavereason = leavereason.replace("[", "");
                                leavereason = leavereason.replace("]", "");
                                leavereason = leavereason.replace("\"", "");
                                leavereason = leavereason.replace(", ", ",");
                                strleavereason = leavereason.split(",");

                                rejectedreason = rejectedreason.replace("[", "");
                                rejectedreason = rejectedreason.replace("]", "");
                                rejectedreason = rejectedreason.replace("\"", "");
                                strrejectedreason = rejectedreason.split(",");

                                leavesubject = leavesubject.replace("[", "");
                                leavesubject = leavesubject.replace("]", "");
                                leavesubject = leavesubject.replace("\"", "");
                                leavesubject = leavesubject.replace(", ", ",");
                                strleavesubject = leavesubject.split(",");

                                leavestatus = leavestatus.replace("[", "");
                                leavestatus = leavestatus.replace("]", "");
                                leavestatus = leavestatus.replace("\"", "");
                                leavestatus = leavestatus.replace(", ", ",");
                                strleavestatus = leavestatus.split(",");

                                leaverequesteddate = leaverequesteddate.replace("[", "");
                                leaverequesteddate = leaverequesteddate.replace("]", "");
                                leaverequesteddate = leaverequesteddate.replace("\"", "");
                                leaverequesteddate = leaverequesteddate.replace(", ", ",");
                                strleaverequesteddate = leaverequesteddate.split(",");

                                studentimage = studentimage.replace("[", "");
                                studentimage = studentimage.replace("]", "");
                                studentimage = studentimage.replace("\"", "");
                                studentimage = studentimage.replace(", ", ",");
                                strstudentimage = studentimage.split(",");

                                emptycriteriamsg.setVisibility(View.INVISIBLE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                adapter = new parentsleavetrackerlistviewadapter(getActivity(),strschoolid,strschoolname,strclassid,strclassname,strstudentid,strstudentname,strleaveorderdate,strfromdate,strtodate,strfromhour,strtohour,strtotaldays,strleavereason,strrejectedreason,strleavesubject,strleavestatus,strleaverequesteddate);
                                mRecyclerView.setAdapter(adapter);

                                if(newclickevent==1){
                                    mRecyclerView.setSelection(0);
                                    mRecyclerView.smoothScrollToPosition(0);
                                    newclickevent = 0;
                                    adapter.notifyDataSetChanged();
                                }else {
                                    mRecyclerView.setSelection(oldposition);
                                    mRecyclerView.smoothScrollToPosition(oldposition);
                                    adapter.notifyDataSetChanged();
                                }


                                if(loadingstate==1){
                                    loadingstate = 0;
                                    loginDialog.dismiss();
                                    loadingstatevalue = 1;
                                }


                                mRecyclerView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                                                && (mRecyclerView.getLastVisiblePosition() - mRecyclerView.getHeaderViewsCount() -
                                                mRecyclerView.getFooterViewsCount()) >= (adapter.getCount() - 1)) {
                                            oldposition = adapter.getCount() - 1;
                                        }else if(mRecyclerView.getFirstVisiblePosition()==0){
                                            loadingstatevalue =1;
                                            oldposition = adapter.getCount() - 1;
                                        }else if(mRecyclerView.getLastVisiblePosition()==adapter.getCount()-1){
                                            loadingstatevalue =1;
                                        }else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                                                && (mRecyclerView.getLastVisiblePosition() - mRecyclerView.getHeaderViewsCount() -
                                                mRecyclerView.getFooterViewsCount()) <= (adapter.getCount()-1)) {
                                            loadingstatevalue = 1;
                                            oldposition = adapter.getCount() - 1;
                                        }else{
                                            loadingstatevalue = 0;
                                        }
                                    }

                                    @Override
                                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                                    }
                                });


                            }else{
                                emptycriteriamsg.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.INVISIBLE);
                                if(loadingstate==1){
                                    loadingstate = 0;
                                    loginDialog.dismiss();
                                }
                            }

                        } catch (JSONException e) {
                            snackbar = Snackbar.make(mainview, "Unable to reach Server Please Try Again", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(mainview, "Unable Connect to Internet. Please Try Again", Snackbar.LENGTH_LONG);
                snackbar.show();
                volleyError.printStackTrace();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_studentid, singlestudentidtext);

                return params;
            }
        };
        RequestQueue Leavevalues = Volley.newRequestQueue(getActivity().getApplicationContext());
        Leavevalues.add(Leavevaluesrequest1);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(timervalue == true) {
            t.cancel();
            t = null;
            timervalue = false;
        }

    }

}
