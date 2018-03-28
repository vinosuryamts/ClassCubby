package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Events;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
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

public class Events_Main_Activity extends android.support.v4.app.Fragment {

    public ListView eventslistview;
    View mainview,view;
    TextView dashboardpagename,emptycriteriamsg;
    Button neweventsbutton;


    ProgressDialog loginDialog;
    Snackbar snackbar;

    Timer t;
    boolean timervalue;
    String loogedinusertypeid,userid;
    int eventposition = 0;
    int eventcount=0,oldposition=0,loadingstate=0,newclickevent=0;
    ArrayList<String> arrschoolname,arreventid,arruserimages,arreventimages,arreventdescription,arreventpostedby,arreventTitle,arreventDate,arreventpostedtime,arreventtype;
    String[] strschoolname,streventid,struserimages,streventimages,streventdescription,streventpostedby,streventTitle,streventDate,streventpostedtime,streventtype;
    String schoolname,eventid,userimages,eventimages,eventdescription,eventpostedby,eventTitle,eventDate,eventpostedtime,eventtype;
    String studentid,studentname ,classname,classid,userimage;
    String[] strstudentid ,strstudentname,strclassname,strclassid,struserimage;
    String assignmentschoolname,assignmentschoolid,assignmentclassid,assignmentclassname,assignmentstudentid,assignmentstudentname,assignmentcount,assignmentuserimage;
    ArrayList<String> arrassignmentschoolname,arrassignmentschoolid,arrassignmentclassid,arrassignmentclassname,arrassignmentstudentid,arrassignmentstudentname,arrassignmentcount,arrassignmentuserimage;
    String leaveschoolname,leaveschoolid,leavesclassid,leavesclassname,leavestudentid,leavestudentname,leavescount,leavesuserimage;
    ArrayList<String> arrleaveschoolname,arrleaveschoolid,arrleavesclassid,arrleavesclassname,arrleavestudentid,arrleavestudentname,arrleavescount,arrleavesuserimage;
    String attendanceschoolname,attendanceschoolid,attendanceclassid,attendanceclassname,attendancestudentid,attendancestudentname,attendancepercent,attendanceuserimage;
    ArrayList<String> arrattendanceschoolname,arrattendanceschoolid,arrattendanceclassid,arrattendanceclassname,arrattendancestudentid,arrattendancestudentname,arrattendancepercent,arrattendanceuserimage;
    String notificationid,countvalue;
    ArrayList<String> newnotifications,assignmentstudentslist;
    String countvaluetext;
    int backcount = -1;
    TextView counttext;

    private JSONArray loginresult;
    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;


    public Events_Main_Activity() {
        // Required empty public constructor
    }

    public static Events_Main_Activity newInstance(String param1) {
        Events_Main_Activity fragment = new Events_Main_Activity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.events_main_xml, null);

        mainview = view.findViewById(R.id.teachersdashboardcontainer);
        dashboardpagename = (TextView) view.findViewById(R.id.dashboardpagename);
        eventslistview = (ListView)view.findViewById(R.id.eventslistview);
        emptycriteriamsg = (TextView)view.findViewById(R.id.emptycriteriamsg);
        neweventsbutton = (Button)view.findViewById(R.id.neweventsbutton);

        Typeface normaltypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSansBold.ttf");
        dashboardpagename.setTypeface(boldtypeface);
        neweventsbutton.setTypeface(boldtypeface);
        emptycriteriamsg.setTypeface(normaltypeface);

        emptycriteriamsg.setVisibility(View.INVISIBLE);
        neweventsbutton.setVisibility(View.INVISIBLE);
        eventslistview.setVisibility(View.VISIBLE);

        oldposition = 0;

        if (AppStatus.getInstance(getActivity().getApplicationContext()).isOnline()) {
            getevents();
        }else {
            Toast.makeText(getActivity(),"Please Connect to the Internet and Try Again",Toast.LENGTH_LONG).show();
        }

        t = new Timer();
        timervalue =true;
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                geteventsupdatecount();
            }

        },5000,5000);

        neweventsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatenewstories();
            }
        });

        return view;
    }

    private void geteventsupdatecount() {
        //deleteCache(getActivity().getApplicationContext());
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        userid = sharedPreferences.getString(loginconfig.key_userid, "");

        StringRequest teachereventcount = new StringRequest(Request.Method.POST, loginconfig.key_parentseventsupdatecheck_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            loginresult = j.getJSONArray("result");

                            newnotifications = new ArrayList<String>(loginresult.length());

                            if (loginresult.length() > 0) {
                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);

                                    notificationid = json.optString("notificationid");
                                    countvalue = json.optString("countvalue");

                                    newnotifications.add(notificationid);
                                }

                                if(countvalue.equals("0")){

                                }else{

                                    eventslistview.setOnScrollListener(new AbsListView.OnScrollListener() {
                                        @Override
                                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                                        }

                                        @Override
                                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                            if(firstVisibleItem == 0 && listIsAtTop()){
                                                oldposition = 0;
                                                neweventsbutton.setVisibility(View.INVISIBLE);
                                                getevents();
                                                updatenewstories();
                                            }else{
                                                neweventsbutton.setVisibility(View.VISIBLE);
                                                neweventsbutton.setText("+ "+countvalue+" new events");
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

                params.put(loginconfig.key_loggeduserid, userid);

                return params;
            }
        };
        RequestQueue eventcountvolley = Volley.newRequestQueue(getActivity().getApplicationContext());
        eventcountvolley.add(teachereventcount);
    }

    private boolean listIsAtTop()   {
        if(eventslistview.getChildCount() == 0) return true;
        return eventslistview.getChildAt(0).getTop() == 0;
    }

    private void getevents() {
        //deleteCache(getActivity().getApplicationContext());
        userid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_parentsevents_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            loginresult = j.getJSONArray("result");

                            loginDialog = new ProgressDialog(getActivity());
                            loginDialog.setIndeterminate(true);
                            loginDialog.setTitle("Please Wait");
                            loginDialog.setMessage("Loading Events...");
                            loginDialog.show();
                            loginDialog.setCancelable(false);
                            loginDialog.setCanceledOnTouchOutside(false);
                            loadingstate = 1;

                            arrschoolname = new ArrayList<>(loginresult.length());
                            arreventid = new ArrayList<>(loginresult.length());
                            arruserimages = new ArrayList<>(loginresult.length());
                            arreventimages = new ArrayList<>(loginresult.length());
                            arreventdescription = new ArrayList<>(loginresult.length());
                            arreventpostedby = new ArrayList<>(loginresult.length());
                            arreventTitle = new ArrayList<>(loginresult.length());
                            arreventDate = new ArrayList<>(loginresult.length());
                            arreventpostedtime = new ArrayList<>(loginresult.length());
                            arreventtype = new ArrayList<>(loginresult.length());

                            if (loginresult.length() > 0) {
                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);

                                    String schoolname = json.optString("schoolname");
                                    String eventid = json.optString("eventid");
                                    String userimages = json.optString("userimages");
                                    String eventimages = json.optString("eventimages");
                                    String eventdescription = json.optString("eventdescription");
                                    String eventpostedby = json.optString("eventpostedby");
                                    String eventTitle = json.optString("eventTitle");
                                    String eventDate = json.optString("eventDate");
                                    String eventpostedtime = json.optString("eventpostedtime");
                                    String eventtype = json.optString("eventtype");

                                    arrschoolname.add(schoolname);
                                    arreventid.add(eventid);
                                    arruserimages.add(userimages);
                                    arreventimages.add(eventimages);
                                    arreventdescription.add(eventdescription);
                                    arreventpostedby.add(eventpostedby);
                                    arreventTitle.add(eventTitle);
                                    arreventDate.add(eventDate);
                                    arreventpostedtime.add(eventpostedtime);
                                    arreventtype.add(eventtype);
                                }

                                sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                //creating editor to store values of shared preferences
                                editor = sharedPreferences.edit();

                                editor.putString(loginconfig.key_eventschoolname, arrschoolname.toString());
                                editor.putString(loginconfig.key_eventid, arreventid.toString());
                                editor.putString(loginconfig.key_eventuserimages, arruserimages.toString());
                                editor.putString(loginconfig.key_eventimages, arreventimages.toString());
                                editor.putString(loginconfig.key_eventdescription, arreventdescription.toString());
                                editor.putString(loginconfig.key_eventpostedby, arreventpostedby.toString());
                                editor.putString(loginconfig.key_eventTitle, arreventTitle.toString());
                                editor.putString(loginconfig.key_eventDate, arreventDate.toString());
                                editor.putString(loginconfig.key_eventpostedtime, arreventpostedtime.toString());
                                editor.putString(loginconfig.key_eventtype, arreventtype.toString());
                                editor.apply();

                                schoolname = MainActivity.sharedPreferences.getString(loginconfig.key_eventschoolname, "");
                                eventid = MainActivity.sharedPreferences.getString(loginconfig.key_eventid, "");
                                userimages = MainActivity.sharedPreferences.getString(loginconfig.key_eventuserimages, "");
                                eventimages = MainActivity.sharedPreferences.getString(loginconfig.key_eventimages, "");
                                eventdescription = MainActivity.sharedPreferences.getString(loginconfig.key_eventdescription, "");
                                eventpostedby = MainActivity.sharedPreferences.getString(loginconfig.key_eventpostedby, "");
                                eventTitle = MainActivity.sharedPreferences.getString(loginconfig.key_eventTitle, "");
                                eventDate = MainActivity.sharedPreferences.getString(loginconfig.key_eventDate, "");
                                eventpostedtime = MainActivity.sharedPreferences.getString(loginconfig.key_eventpostedtime, "");
                                eventtype = MainActivity.sharedPreferences.getString(loginconfig.key_eventtype, "");

                                schoolname = schoolname.replace("[", "");
                                schoolname = schoolname.replace("]", "");
                                schoolname = schoolname.replace("\"", "");
                                schoolname = schoolname.replace(", ", ",");
                                strschoolname = schoolname.split(",");

                                eventid = eventid.replace("[", "");
                                eventid = eventid.replace("]", "");
                                eventid = eventid.replace("\"", "");
                                eventid = eventid.replace(", ", ",");
                                streventid = eventid.split(",");

                                userimages = userimages.replace("[", "");
                                userimages = userimages.replace("]", "");
                                userimages = userimages.replace("\"", "");
                                userimages = userimages.replace("\\", "");
                                userimages = userimages.replace(", ", ",");
                                struserimages = userimages.split(",");

                                eventimages = eventimages.replace("[", "");
                                eventimages = eventimages.replace("]", "");
                                eventimages = eventimages.replace("\"", "");
                                eventimages = eventimages.replace("\\", "");
                                eventimages = eventimages.replace(", ", ",");
                                streventimages = eventimages.split(",");

                                eventdescription = eventdescription.replace("[", "");
                                eventdescription = eventdescription.replace("]", "");
                                eventdescription = eventdescription.replace("\"", "");
                                eventdescription = eventdescription.replace(", ", ",");
                                streventdescription = eventdescription.split("@@@");

                                eventpostedby = eventpostedby.replace("[", "");
                                eventpostedby = eventpostedby.replace("]", "");
                                eventpostedby = eventpostedby.replace("\"", "");
                                eventpostedby = eventpostedby.replace(", ", ",");
                                eventpostedby = eventpostedby.replaceFirst(" ", "");
                                streventpostedby = eventpostedby.split(",");

                                eventTitle = eventTitle.replace("[", "");
                                eventTitle = eventTitle.replace("]", "");
                                eventTitle = eventTitle.replace("\"", "");
                                eventTitle = eventTitle.replace(", ", ",");
                                streventTitle = eventTitle.split("@@@");

                                eventDate = eventDate.replace("[", "");
                                eventDate = eventDate.replace("]", "");
                                eventDate = eventDate.replace("\"", "");
                                eventDate = eventDate.replace(", ", ",");
                                streventDate = eventDate.split(",");

                                eventpostedtime = eventpostedtime.replace("[", "");
                                eventpostedtime = eventpostedtime.replace("]", "");
                                eventpostedtime = eventpostedtime.replace("\"", "");
                                eventpostedtime = eventpostedtime.replace(", ", ",");
                                streventpostedtime = eventpostedtime.split(",");

                                eventtype = eventtype.replace("[", "");
                                eventtype = eventtype.replace("]", "");
                                eventtype = eventtype.replace("\"", "");
                                eventtype = eventtype.replace(", ", ",");
                                streventtype = eventtype.split(",");

                                eventcount = streventid.length;

                                final ParentsEventsMainAdapter adapter = new ParentsEventsMainAdapter(getActivity(), strschoolname, streventid, struserimages, streventimages, streventdescription, streventpostedby, streventTitle, streventDate, streventpostedtime, streventtype);
                                eventslistview.setAdapter(adapter);
                                if (newclickevent == 1) {
                                    eventslistview.setSelection(0);
                                    eventslistview.smoothScrollToPosition(0);
                                    newclickevent = 0;
                                    adapter.notifyDataSetChanged();
                                } else {
                                    eventslistview.setSelection(oldposition);
                                    eventslistview.smoothScrollToPosition(oldposition);
                                    adapter.notifyDataSetChanged();
                                }

                                if (loadingstate == 1) {
                                    loadingstate = 0;
                                    loginDialog.dismiss();
                                }

                                eventslistview.setOnScrollListener(new AbsListView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                                                && (eventslistview.getLastVisiblePosition() - eventslistview.getHeaderViewsCount() -
                                                eventslistview.getFooterViewsCount()) >= (adapter.getCount() - 1)) {

                                            oldposition = adapter.getCount() - 1;

                                            if (eventcount >= eventposition + 10) {

                                                eventposition = eventposition + 10;

                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        getevents();
                                                    }
                                                }, 1000);
                                            } else {
                                                eventcount = oldposition;
                                            }

                                        }
                                    }

                                    @Override
                                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                                    }
                                });

                            } else {
                                if (loadingstate == 1) {
                                    loadingstate = 0;
                                    loginDialog.dismiss();
                                }
                                emptycriteriamsg.setVisibility(View.VISIBLE);
                                eventslistview.setVisibility(View.INVISIBLE);
                            }


                        } catch (JSONException e) {
                            if (loadingstate == 1) {
                                loadingstate = 0;
                                loginDialog.dismiss();
                            }
                            snackbar = Snackbar.make(mainview, "Unable to reach Server Please Try Again", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (loadingstate == 1) {
                    loadingstate = 0;
                    loginDialog.dismiss();
                }
                Toast.makeText(getActivity(), "Unable Connect to Internet. Please Try Again", Toast.LENGTH_LONG).show();
                volleyError.printStackTrace();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_userid, userid);
                params.put(loginconfig.key_eventendposition, String.valueOf(eventposition));
                params.put(loginconfig.key_eventsid, "");
                params.put(loginconfig.key_eventcountvalue, String.valueOf(eventcount));

                return params;
            }
        };
        RequestQueue studentlist = Volley.newRequestQueue(getActivity());
        studentlist.add(studentrequest1);
    }

    private void updatenewstories() {
        //deleteCache(getActivity().getApplicationContext());
        userid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        String notificationvalue = newnotifications.toString();

        notificationvalue = notificationvalue.replace("[", "");
        notificationvalue = notificationvalue.replace("]", "");
        notificationvalue = notificationvalue.replace(" ", "");

        final String notificationids = notificationvalue;

        StringRequest studentrequest3 = new StringRequest(Request.Method.POST, loginconfig.key_parentseventsupdate_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            neweventsbutton.setVisibility(View.INVISIBLE);
                            newclickevent = 1;
                            getevents();
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

                params.put(loginconfig.key_loggeduserid, userid);
                params.put(loginconfig.key_newevent, notificationids);

                return params;
            }
        };
        RequestQueue studentlist3 = Volley.newRequestQueue(getActivity());
        studentlist3.add(studentrequest3);
    }


    @Override
    public void onDestroyView() {

        if(loadingstate==1){
            loadingstate = 0;
            loginDialog.dismiss();
            if(timervalue==true){
                t.cancel();
                t = null;
                timervalue = false;
            }
        }else{
            if(timervalue==true){
                t.cancel();
                t = null;
                timervalue = false;
            }
        }
        super.onDestroyView();
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
