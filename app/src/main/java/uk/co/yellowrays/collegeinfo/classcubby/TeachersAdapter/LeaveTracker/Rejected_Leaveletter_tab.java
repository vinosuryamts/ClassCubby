package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.LeaveTracker;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.yellowrays.collegeinfo.classcubby.AppStatus;
import uk.co.yellowrays.collegeinfo.classcubby.MainActivity;
import uk.co.yellowrays.collegeinfo.classcubby.R;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.LeaveTracker.adapter.rejectrowcontentadapter;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.LeaveTracker.leavetrackerclickitems.Teachers_LeaveTracker_Rejected_Click_Items;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;

/**
 * Created by user1 on 18-11-2016.
 */

public class Rejected_Leaveletter_tab extends Fragment {

    SwipeRefreshLayout refreshLayout;
    ListView pendinglistview;
    TextView emptypendingleaves;
    Button pendingcount;
    String userid;
    private JSONArray loginresult,countloginresult;
    int count = 0,newcount=0;
    Snackbar snackbar;
    View view,sanckbarview;
    Timer t;
    boolean timervalue = false;
    ProgressDialog loadingdialog;
    PopupWindow pwindow;
    ImageView popupstudentimage;
    EditText popuprejectreason;
    Button accpet,reject,close;
    TextView popuptimesubmitted,popupstudentname,popupfromdate,popuptodate,popupleavedetail;
    FrameLayout leavemainlayout;

    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    String transactionid,studentname,classname,classsection,leaverequesteddate,fromdate,todate,totalleavedayscount,leaverequest,leavestatusname,userimage,rejectreason;
    String[] transactionidvalue,studentnamevalue,classnamevalue,classsectionvalue,timesubmittedvalue,fromdatevalue,todatevalue,totalleavedayscountvalue,leaverequestdetailsvalue,leavestatusnamevalue,userimagevalue,rejectreasonvalue;

    ArrayList<String> arrtransactionidvalue,arrstudentnamevalue,arrclassnamevalue,arrclasssectionvalue,arrtimesubmittedvalue,arrfromdatevalue,arrtodatevalue,arrtotalleavedayscountvalue,arrleaverequestdetailsvalue,arrleavestatusnamevalue,arruserimagevalue,arrrejectreasonvalue;

    public static Rejected_Leaveletter_tab newInstance(String param1) {
        Rejected_Leaveletter_tab fragment = new Rejected_Leaveletter_tab();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.teachers_leavetracker_reject_main_xml, container, false);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        pendinglistview = (ListView) view.findViewById(R.id.rejectlistview);
        pendingcount = (Button) view.findViewById(R.id.rejectcount);
        emptypendingleaves = (TextView)view.findViewById(R.id.emptyrejectleaves);
        leavemainlayout = (FrameLayout) view.findViewById(R.id.leavemainlayout);

        sanckbarview = view.findViewById(R.id.teachersleavetrackercontainer);

        pendingcount.setVisibility(View.INVISIBLE);
        emptypendingleaves.setVisibility(View.INVISIBLE);

        Typeface normaltypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSansRegular.ttf");
        Typeface semiboldtypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSansBold.ttf");
        emptypendingleaves.setTypeface(normaltypeface);

        if (loadingdialog != null && loadingdialog.isShowing()) {
            loadingdialog.setCancelable(true);
            loadingdialog.dismiss();
        }

        t = new Timer();
        timervalue =true;
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                rejectedlistcount();
                if (loadingdialog != null && loadingdialog.isShowing()) {
                    loadingdialog.setCancelable(true);
                    loadingdialog.dismiss();
                }
            }

        },5000,5000);

        if (AppStatus.getInstance(getActivity()).isOnline()) {
            rejectedleaveslist();
        }else {
            snackbar = Snackbar.make(sanckbarview,"Please Connect to the Internet and Try Again",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rejectedleaveslist();
            }

        });

        pendinglistview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem == 0 && listIsAtTop()){
                    if(newcount>0){
                        rejectedleaveslist();
                        rejectedlistcount();
                        newcount = 0;
                    }
                    refreshLayout.setEnabled(true);
                }else{
                    refreshLayout.setEnabled(false);
                }
            }
        });


        pendinglistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                Teacher_LeaveTracker_Main_Activity.setclicked(true);
                if(timervalue == true) {
                    t.cancel();
                    t = null;
                    timervalue = false;
                }

                Teachers_LeaveTracker_Rejected_Click_Items newFragment = new Teachers_LeaveTracker_Rejected_Click_Items().newInstance("test");
                Bundle bundle = new Bundle();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                bundle.putString("imagevalue",userimagevalue[position]);
                bundle.putString("timesubmitted", timesubmittedvalue[position]);
                bundle.putString("studentname", studentnamevalue[position]);
                bundle.putString("fromdate", fromdatevalue[position]);
                bundle.putString("todate", todatevalue[position]);
                bundle.putString("leavedetail", leaverequestdetailsvalue[position]);
                bundle.putString("transactionid", transactionidvalue[position]);
                bundle.putString("rejectreason", rejectreasonvalue[position]);
                newFragment.setArguments(bundle);
                transaction.replace(R.id.teachersleavetrackercontainer, newFragment);
                transaction.addToBackStack("new");
                transaction.commit();

            }
        });


        pendingcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectedleaveslist();
            }
        });
        return view;
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


    private boolean listIsAtTop()   {
        if(pendinglistview.getChildCount() == 0) return true;
        return pendinglistview.getChildAt(0).getTop() == 0;
    }

    private void rejectedleaveslist() {

        userid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        /*loadingdialog = new ProgressDialog(getActivity());
        loadingdialog.setIndeterminate(true);
        loadingdialog.setMessage("Loading Please Wait!");
        loadingdialog.setCancelable(true);
        loadingdialog.setCanceledOnTouchOutside(true);
        loadingdialog.show();*/

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_leavettrackerrejectedvalues_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            loginresult = j.getJSONArray("result");

                            arrtransactionidvalue = new ArrayList<String>(loginresult.length());
                            arrstudentnamevalue = new ArrayList<String>(loginresult.length());
                            arrclassnamevalue = new ArrayList<String>(loginresult.length());
                            arrclasssectionvalue = new ArrayList<String>(loginresult.length());
                            arrtimesubmittedvalue = new ArrayList<String>(loginresult.length());
                            arrfromdatevalue = new ArrayList<String>(loginresult.length());
                            arrtodatevalue = new ArrayList<String>(loginresult.length());
                            arrtotalleavedayscountvalue = new ArrayList<String>(loginresult.length());
                            arrleaverequestdetailsvalue = new ArrayList<String>(loginresult.length());
                            arrleavestatusnamevalue = new ArrayList<String>(loginresult.length());
                            arruserimagevalue = new ArrayList<String>(loginresult.length());
                            arrrejectreasonvalue = new ArrayList<String>(loginresult.length());

                            if (loginresult.length() > 0) {
                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);
                                    String transactionid = json.optString("transactionid");
                                    String studentname = json.optString("studentname");
                                    String classname = json.optString("classname");
                                    String classsection = json.optString("classsection");
                                    String leaverequesteddate = json.optString("leaverequesteddate");
                                    String fromdate = json.optString("fromdate");
                                    String todate = json.optString("todate");
                                    String totalleavedayscount = json.optString("totalleavedayscount");
                                    String leaverequest = json.optString("leaverequest");
                                    String leavestatusname = json.optString("leavestatusname");
                                    String userimage = json.optString("userimage");
                                    String rejectreason = json.optString("leaverejectreason");

                                    arrtransactionidvalue.add(transactionid);
                                    arrstudentnamevalue.add(studentname);
                                    arrclassnamevalue.add(classname);
                                    arrclasssectionvalue.add(classsection);
                                    arrtimesubmittedvalue.add(leaverequesteddate);
                                    arrfromdatevalue.add(fromdate);
                                    arrtodatevalue.add(todate);
                                    arrtotalleavedayscountvalue.add(totalleavedayscount);
                                    arrleaverequestdetailsvalue.add(leaverequest);
                                    arrleavestatusnamevalue.add(leavestatusname);
                                    arruserimagevalue.add(userimage);
                                    arrrejectreasonvalue.add(rejectreason);
                                }

                                sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                //creating editor to store values of shared preferences
                                editor = sharedPreferences.edit();

                                editor.putString(loginconfig.key_pending_transactionid, arrtransactionidvalue.toString());
                                editor.putString(loginconfig.key_pending_studentname, arrstudentnamevalue.toString());
                                editor.putString(loginconfig.key_pending_classname, arrclassnamevalue.toString());
                                editor.putString(loginconfig.key_pending_classsection, arrclasssectionvalue.toString());
                                editor.putString(loginconfig.key_pending_leaverequesteddate, arrtimesubmittedvalue.toString());
                                editor.putString(loginconfig.key_pending_fromdate, arrfromdatevalue.toString());
                                editor.putString(loginconfig.key_pending_todate, arrtodatevalue.toString());
                                editor.putString(loginconfig.key_pending_totalleavedayscount, arrtotalleavedayscountvalue.toString());
                                editor.putString(loginconfig.key_pending_leaverequest, arrleaverequestdetailsvalue.toString());
                                editor.putString(loginconfig.key_pending_leavestatusname, arrleavestatusnamevalue.toString());
                                editor.putString(loginconfig.key_pending_userimage, arruserimagevalue.toString());
                                editor.putString(loginconfig.key_pending_rejectreason, arrrejectreasonvalue.toString());
                                editor.apply();

                                transactionid = MainActivity.sharedPreferences.getString(loginconfig.key_pending_transactionid, "");
                                studentname = MainActivity.sharedPreferences.getString(loginconfig.key_pending_studentname, "");
                                classname = MainActivity.sharedPreferences.getString(loginconfig.key_pending_classname, "");
                                classsection = MainActivity.sharedPreferences.getString(loginconfig.key_pending_classsection, "");
                                leaverequesteddate = MainActivity.sharedPreferences.getString(loginconfig.key_pending_leaverequesteddate, "");
                                fromdate = MainActivity.sharedPreferences.getString(loginconfig.key_pending_fromdate, "");
                                todate = MainActivity.sharedPreferences.getString(loginconfig.key_pending_todate, "");
                                totalleavedayscount = MainActivity.sharedPreferences.getString(loginconfig.key_pending_totalleavedayscount, "");
                                leaverequest = MainActivity.sharedPreferences.getString(loginconfig.key_pending_leaverequest, "");
                                leavestatusname = MainActivity.sharedPreferences.getString(loginconfig.key_pending_leavestatusname, "");
                                userimage = MainActivity.sharedPreferences.getString(loginconfig.key_pending_userimage, "");
                                rejectreason = MainActivity.sharedPreferences.getString(loginconfig.key_pending_rejectreason, "");


                                transactionid = transactionid.replace("[","");
                                transactionid = transactionid.replace("[","");
                                transactionid = transactionid.replace(", ",",");
                                transactionid = transactionid.replaceFirst(" ","");
                                transactionidvalue = transactionid.split(",");

                                studentname = studentname.replace("[","");
                                studentname = studentname.replace("]","");
                                studentname = studentname.replace(", ",",");
                                studentname = studentname.replaceFirst(" ","");
                                studentnamevalue = studentname.split(",");

                                classname = classname.replace("[","");
                                classname = classname.replace("]","");
                                classname = classname.replace(", ",",");
                                classname = classname.replaceFirst(" ","");
                                classnamevalue = classname.split(",");

                                classsection = classsection.replace("[","");
                                classsection = classsection.replace("]","");
                                classsection = classsection.replace(", ",",");
                                classsection = classsection.replaceFirst(" ","");
                                classsectionvalue = classsection.split(",");

                                leaverequesteddate = leaverequesteddate.replace("[","");
                                leaverequesteddate = leaverequesteddate.replace("]","");
                                leaverequesteddate = leaverequesteddate.replace(", ",",");
                                timesubmittedvalue = leaverequesteddate.split(",");

                                fromdate = fromdate.replace("[","");
                                fromdate = fromdate.replace("]","");
                                fromdate = fromdate.replace(", ",",");
                                fromdatevalue = fromdate.split(",");

                                todate = todate.replace("[","");
                                todate = todate.replace("]","");
                                todate = todate.replace(", ",",");
                                todatevalue = todate.split(",");

                                totalleavedayscount = totalleavedayscount.replace("[","");
                                totalleavedayscount = totalleavedayscount.replace("]","");
                                totalleavedayscount = totalleavedayscount.replace(", ",",");
                                totalleavedayscount = totalleavedayscount.replaceFirst(" ","");
                                totalleavedayscountvalue = totalleavedayscount.split(",");

                                leaverequest = leaverequest.replace("[","");
                                leaverequest = leaverequest.replace("]","");
                                leaverequest = leaverequest.replace(", ",",");
                                leaverequestdetailsvalue= leaverequest.split(",");

                                leavestatusname = leavestatusname.replace("[","");
                                leavestatusname = leavestatusname.replace("]","");
                                leavestatusname = leavestatusname.replace(", ",",");
                                leavestatusname = leavestatusname.replaceFirst(" ","");
                                leavestatusnamevalue = leavestatusname.split(",");

                                userimage = userimage.replace("[","");
                                userimage = userimage.replace("]","");
                                userimage = userimage.replace(", ",",");
                                userimage = userimage.replaceFirst(" ","");
                                userimagevalue = userimage.split(",");

                                rejectreason = rejectreason.replace("[","");
                                rejectreason = rejectreason.replace("]","");
                                rejectreasonvalue = rejectreason.split(",");

                                if (loadingdialog != null && loadingdialog.isShowing()) {
                                    loadingdialog.setCancelable(true);
                                    loadingdialog.dismiss();
                                }
                                pendinglistview.setVisibility(View.VISIBLE);
                                emptypendingleaves.setVisibility(View.INVISIBLE);
                                pendinglistview.setAdapter(new rejectrowcontentadapter(getActivity(),transactionidvalue,studentnamevalue,classnamevalue,classsectionvalue,timesubmittedvalue,fromdatevalue,todatevalue,totalleavedayscountvalue,leaverequestdetailsvalue,leavestatusnamevalue,userimagevalue,rejectreasonvalue));
                                refreshLayout.setRefreshing(false);
                            }else {
                                if (loadingdialog != null && loadingdialog.isShowing()) {
                                    loadingdialog.setCancelable(true);
                                    loadingdialog.dismiss();
                                }
                                emptypendingleaves.setVisibility(View.VISIBLE);
                            }


                        } catch (JSONException e) {
                            if (loadingdialog != null && loadingdialog.isShowing()) {
                                loadingdialog.setCancelable(true);
                                loadingdialog.dismiss();
                            }
                            //snackbar = Snackbar.make(view,"You are Not a Mentor of any Class, Kindly Contact School Admin to assign You as a mentor",Snackbar.LENGTH_LONG);
                            //snackbar.show();
                            if(timervalue == true) {
                                t.cancel();
                                t = null;
                                timervalue = false;
                            }
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (loadingdialog != null && loadingdialog.isShowing()) {
                    loadingdialog.setCancelable(true);
                    loadingdialog.dismiss();
                }
                snackbar = Snackbar.make(view,"Unable to Connect to Internet. Please Try Again",Snackbar.LENGTH_LONG);
                snackbar.show();
                if(timervalue == true) {
                    t.cancel();
                    t = null;
                    timervalue = false;
                }
                volleyError.printStackTrace();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_userid, userid);

                return params;
            }
        };
        RequestQueue studentlist = Volley.newRequestQueue(getActivity());
        studentlist.add(studentrequest1);
    }


    private void rejectedlistcount(){

        userid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_leavettrackerrejectedvalues_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            countloginresult = j.getJSONArray("result");

                            count = countloginresult.length();

                            if(count>loginresult.length()){
                                pendingcount.setVisibility(View.VISIBLE);
                                emptypendingleaves.setVisibility(View.INVISIBLE);
                                newcount = count-loginresult.length();
                                pendingcount.setText("+"+newcount+"  New Rejected Leave Requests");
                                if(loginresult.length()==0){
                                    rejectedleaveslist();
                                }
                            }else if(count==loginresult.length()){
                                pendingcount.setVisibility(View.INVISIBLE);
                                if(loginresult.length()==0){
                                    emptypendingleaves.setVisibility(View.VISIBLE);
                                }
                            }else if(count<loginresult.length()){
                                pendingcount.setVisibility(View.INVISIBLE);
                                if(count==0){
                                    emptypendingleaves.setVisibility(View.VISIBLE);
                                    pendinglistview.setVisibility(View.INVISIBLE);
                                }
                            }

                            refreshLayout.setRefreshing(false);

                        } catch (JSONException e) {
                            snackbar = Snackbar.make(view,"Unable to reach Server Please Try Again",Snackbar.LENGTH_LONG);
                            snackbar.show();
                            if(timervalue == true) {
                                t.cancel();
                                t = null;
                                timervalue = false;
                            }
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(view,"Unable to Connect to Internet. Please Try Again",Snackbar.LENGTH_LONG);
                snackbar.show();
                if(timervalue == true) {
                    t.cancel();
                    t = null;
                    timervalue = false;
                }
                volleyError.printStackTrace();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_userid, userid);

                return params;
            }
        };
        RequestQueue studentlist = Volley.newRequestQueue(getActivity());
        studentlist.add(studentrequest1);

    }

}

