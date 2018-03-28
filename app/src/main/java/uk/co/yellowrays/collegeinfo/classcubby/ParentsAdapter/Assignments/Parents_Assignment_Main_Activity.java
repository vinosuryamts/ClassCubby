package uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Assignments;

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

public class Parents_Assignment_Main_Activity extends android.support.v4.app.Fragment {

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
    ArrayList<String> arrassignmentdate,arrassignmentid,arrassignmenttitle,arrassignmentdesc,arrassignmenttarget,arrassignmentsubject,arrassignmentattachment,arrassignmentfilename,arreventpostedtime,arreventtype;
    String[] strassignmentdate,strassignmentid,strassignmenttitle,strassignmentdesc,strassignmenttarget,strassignmentsubject,strassignmentattachment,strassignmentfilename,streventpostedtime,streventtype;
    String assignmentdate,assignmentid,assignmenttitle,assignmentdesc,assignmenttarget,assignmentsubject,assignmentattachment,assignmentfilename,eventpostedtime,eventtype;
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


    public Parents_Assignment_Main_Activity() {
        // Required empty public constructor
    }

    public static Parents_Assignment_Main_Activity newInstance(String param1) {
        Parents_Assignment_Main_Activity fragment = new Parents_Assignment_Main_Activity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.parents_assignments_main_xml, null);

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
            getassignments();
        }else {
            Toast.makeText(getActivity(),"Please Connect to the Internet and Try Again",Toast.LENGTH_LONG).show();
        }

        t = new Timer();
        timervalue =true;
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                getassignmentsupdatecount();
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

    private void getassignmentsupdatecount() {
        deleteCache(getActivity().getApplicationContext());
        userid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        StringRequest assignmentcountcheckqueue = new StringRequest(Request.Method.POST, loginconfig.key_parents_assignments_check_url,
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
                                                getassignments();
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
        RequestQueue assignmentcountcheck = Volley.newRequestQueue(getActivity());
        assignmentcountcheck.add(assignmentcountcheckqueue);
    }

    private boolean listIsAtTop()   {
        if(eventslistview.getChildCount() == 0) return true;
        return eventslistview.getChildAt(0).getTop() == 0;
    }

    private void updatenewstories() {
        deleteCache(getActivity().getApplicationContext());
        userid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        String notificationvalue = newnotifications.toString();

        notificationvalue = notificationvalue.replace("[", "");
        notificationvalue = notificationvalue.replace("]", "");
        notificationvalue = notificationvalue.replace(" ", "");

        final String notificationids = notificationvalue;

        StringRequest studentrequest3 = new StringRequest(Request.Method.POST, loginconfig.key_parents_assignments_check_update_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            neweventsbutton.setVisibility(View.INVISIBLE);
                            newclickevent = 1;
                            getassignments();
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

    private void getassignments() {
        //deleteCache(getActivity().getApplicationContext());
        userid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_parents_assignments_main_url,
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
                            loginDialog.setMessage("Loading Assignments...");
                            loginDialog.show();
                            loginDialog.setCancelable(false);
                            loginDialog.setCanceledOnTouchOutside(false);
                            loadingstate = 1;

                            arrassignmentdate = new ArrayList<>(loginresult.length());
                            arrassignmentid = new ArrayList<>(loginresult.length());
                            arrassignmenttitle = new ArrayList<>(loginresult.length());
                            arrassignmentdesc = new ArrayList<>(loginresult.length());
                            arrassignmenttarget = new ArrayList<>(loginresult.length());
                            arrassignmentsubject = new ArrayList<>(loginresult.length());
                            arrassignmentattachment = new ArrayList<>(loginresult.length());
                            arrassignmentfilename = new ArrayList<>(loginresult.length());

                            if (loginresult.length() > 0) {
                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);

                                    String assignmentdate = json.optString("date");
                                    String assignmentid = json.optString("Assignmentid");
                                    String assignmenttitle = json.optString("AssignmentTitle");
                                    String assignmentdesc = json.optString("AssignmentDesc");
                                    String assignmenttarget = json.optString("AssignmentTarget");
                                    String assignmentsubject = json.optString("AssignmentSubject");
                                    String assignmentattachment = json.optString("AssignmentAttachment");
                                    String assignmentfilename = json.optString("AssignmentFileName");

                                    arrassignmentdate.add(assignmentdate);
                                    arrassignmentid.add(assignmentid);
                                    arrassignmenttitle.add(assignmenttitle);
                                    arrassignmentdesc.add(assignmentdesc);
                                    arrassignmenttarget.add(assignmenttarget);
                                    arrassignmentsubject.add(assignmentsubject);
                                    arrassignmentattachment.add(assignmentattachment);
                                    arrassignmentfilename.add(assignmentfilename);
                                }

                                sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                //creating editor to store values of shared preferences
                                editor = sharedPreferences.edit();

                                editor.putString(loginconfig.key_assignment_date, arrassignmentdate.toString());
                                editor.putString(loginconfig.key_assignment_id, arrassignmentid.toString());
                                editor.putString(loginconfig.key_assignment_title, arrassignmenttitle.toString());
                                editor.putString(loginconfig.key_assignment_desc, arrassignmentdesc.toString());
                                editor.putString(loginconfig.key_assignment_target, arrassignmenttarget.toString());
                                editor.putString(loginconfig.key_assignment_subject, arrassignmentsubject.toString());
                                editor.putString(loginconfig.key_assignment_attachment, arrassignmentattachment.toString());
                                editor.putString(loginconfig.key_assignment_filename, arrassignmentfilename.toString());
                                editor.apply();

                                assignmentdate = MainActivity.sharedPreferences.getString(loginconfig.key_assignment_date, "");
                                assignmentid = MainActivity.sharedPreferences.getString(loginconfig.key_assignment_id, "");
                                assignmenttitle = MainActivity.sharedPreferences.getString(loginconfig.key_assignment_title, "");
                                assignmentdesc = MainActivity.sharedPreferences.getString(loginconfig.key_assignment_desc, "");
                                assignmenttarget = MainActivity.sharedPreferences.getString(loginconfig.key_assignment_target, "");
                                assignmentsubject = MainActivity.sharedPreferences.getString(loginconfig.key_assignment_subject, "");
                                assignmentattachment = MainActivity.sharedPreferences.getString(loginconfig.key_assignment_attachment, "");
                                assignmentfilename = MainActivity.sharedPreferences.getString(loginconfig.key_assignment_filename, "");

                                assignmentdate = assignmentdate.replace("[", "");
                                assignmentdate = assignmentdate.replace("]", "");
                                assignmentdate = assignmentdate.replace("\"", "");
                                assignmentdate = assignmentdate.replace(", ", ",");
                                strassignmentdate = assignmentdate.split(",");

                                assignmentid = assignmentid.replace("[", "");
                                assignmentid = assignmentid.replace("]", "");
                                assignmentid = assignmentid.replace("\"", "");
                                assignmentid = assignmentid.replace(", ", ",");
                                strassignmentid = assignmentid.split(",");

                                assignmenttitle = assignmenttitle.replace("[", "");
                                assignmenttitle = assignmenttitle.replace("]", "");
                                assignmenttitle = assignmenttitle.replace("\"", "");
                                assignmenttitle = assignmenttitle.replace("\\", "");
                                assignmenttitle = assignmenttitle.replace(", ", ",");
                                strassignmenttitle = assignmenttitle.split(",");

                                assignmentdesc = assignmentdesc.replace("[", "");
                                assignmentdesc = assignmentdesc.replace("]", "");
                                assignmentdesc = assignmentdesc.replace("\"", "");
                                assignmentdesc = assignmentdesc.replace("\\", "");
                                assignmentdesc = assignmentdesc.replace(", ", ",");
                                strassignmentdesc = assignmentdesc.split(",");

                                assignmenttarget = assignmenttarget.replace("[", "");
                                assignmenttarget = assignmenttarget.replace("]", "");
                                assignmenttarget = assignmenttarget.replace("\"", "");
                                assignmenttarget = assignmenttarget.replace(", ", ",");
                                strassignmenttarget = assignmenttarget.split(",");

                                assignmentsubject = assignmentsubject.replace("[", "");
                                assignmentsubject = assignmentsubject.replace("]", "");
                                assignmentsubject = assignmentsubject.replace("\"", "");
                                assignmentsubject = assignmentsubject.replace(", ", ",");
                                assignmentsubject = assignmentsubject.replaceFirst(" ", "");
                                strassignmentsubject = assignmentsubject.split(",");

                                assignmentattachment = assignmentattachment.replace("[", "");
                                assignmentattachment = assignmentattachment.replace("]", "");
                                assignmentattachment = assignmentattachment.replace("\"", "");
                                strassignmentattachment = assignmentattachment.split(",");

                                assignmentfilename = assignmentfilename.replace("[", "");
                                assignmentfilename = assignmentfilename.replace("]", "");
                                assignmentfilename = assignmentfilename.replace("\"", "");
                                assignmentfilename = assignmentfilename.replace(", ", ",");
                                strassignmentfilename = assignmentfilename.split(",");

                                eventcount = strassignmentid.length;

                                final ParentsAssignmentsMainAdapter adapter = new ParentsAssignmentsMainAdapter(getActivity(), strassignmentdate, strassignmentid, strassignmenttitle, strassignmentdesc, strassignmenttarget, strassignmentsubject, strassignmentattachment, strassignmentfilename);
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
                                                        getassignments();
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

                params.put(loginconfig.key_userid, userid);
                params.put(loginconfig.key_eventendposition, String.valueOf(eventposition));

                return params;
            }
        };
        RequestQueue studentlist = Volley.newRequestQueue(getActivity());
        studentlist.add(studentrequest1);
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
