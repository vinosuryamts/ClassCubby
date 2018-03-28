package uk.co.yellowrays.collegeinfo.classcubby;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.yellowrays.collegeinfo.classcubby.FontManager.CustomTabLayout;
import uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Leavetracker.Parents_Leavetracker_rowitems_click;
import uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.Outgoingpermission.Parents_Outgoing_rowitems_click;
import uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.parentattendancetabpageadapter;
import uk.co.yellowrays.collegeinfo.classcubby.ParentsAdapter.studentattendancetabpageadapter;
import uk.co.yellowrays.collegeinfo.classcubby.Server.GCMRegistrationIntentService;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Assignments.RecentAssignmentListViewAdapter;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Attendance.RecentAttendanceListViewAdapter;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.LeaveTracker.Teacher_LeaveTracker_Main_Activity;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Notifications.Notifications_View;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.fragments.BackStackFragment;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Attendance.Teacher_Attendance_Main_Activity;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.teacherattendancetabpageadapter;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.userinformation.Parents_Userinformation_View;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.userinformation.Teachers_Userinformation_View;
import uk.co.yellowrays.collegeinfo.classcubby.WardenAdapter.OutgoingRequests.outgoingrequestclickitems.Warden_Outgoingpermission_Main_Activity;
import uk.co.yellowrays.collegeinfo.classcubby.WardenAdapter.wardentabpageadapter;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.previousloginconfig;
import uk.co.yellowrays.collegeinfo.classcubby.customtabadapter.CustomViewPager;

public class Teacher_Dashboard_Activity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private Toolbar toolbar;
    private TextView projecttitle,messageimage,counttext;
    private ImageView profileimage;
    private RelativeLayout mainpageoverallcontainer;
    private View view;
    Snackbar snackbar;
    private Timer t;
    boolean timervalue;
    private CustomTabLayout tabLayout;
    private CustomViewPager viewPager;
    private String loggedinuserimage,loogedinusertypeid;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    int check = 0;
    String loginid;
    private Teacher_Attendance_Main_Activity teacherattendance;
    public static Activity myActivity;
    boolean value,generateclick;
    private JSONArray loginresult;

    private RequestQueue requestQueue3 = null;
    String userid;
    String countvaluetext;
    int backcount=0;
    final Handler handler = new Handler();
    boolean selected=false;

    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_dashboard_xml);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);

        previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);
        loogedinusertypeid = previoussharedPreferences.getString(previousloginconfig.key_usertypeid, "");

        projecttitle = (TextView)findViewById(R.id.projecttitle);
        profileimage = (ImageView) findViewById(R.id.profileimage);
        messageimage = (TextView) findViewById(R.id.messageimage);
        counttext = (TextView) findViewById(R.id.counttext);
        mainpageoverallcontainer = (RelativeLayout) findViewById(R.id.mainpageoverallcontainer);
        view = (RelativeLayout) findViewById(R.id.mainpageoverallcontainer);

        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        /*setupViewPager(viewPager);
        viewPager.setPagingEnabled(false);
        viewPager.beginFakeDrag();*/

        tabLayout = (CustomTabLayout) findViewById(R.id.tabs);
        /*tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();*/


        counttext.setVisibility(View.INVISIBLE);

        t = new Timer();
        timervalue =true;
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                getnotificationcount();
            }

        },5000,5000);

        getnotificationcount();

        if(loogedinusertypeid.equals("2")){
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.newsfeeds)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.attendance)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.assignments)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.leavetracker)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.friends)));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            teacherattendancetabpageadapter adapter = new teacherattendancetabpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);
            viewPager.beginFakeDrag();
        }else if(loogedinusertypeid.equals("3")){
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.newsfeeds)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.attendance)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.assignments)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.leavetracker)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.friends)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.outgoing)));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            parentattendancetabpageadapter adapter = new parentattendancetabpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);
            viewPager.beginFakeDrag();
        }else if(loogedinusertypeid.equals("4")){
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.newsfeeds)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.attendance)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.assignments)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.leavetracker)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.friends)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.outgoing)));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            studentattendancetabpageadapter adapter = new studentattendancetabpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);
            viewPager.beginFakeDrag();
        }else if(loogedinusertypeid.equals("9")||loogedinusertypeid.equals("10")){
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.newsfeeds)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.friends)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.outgoing)));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            wardentabpageadapter adapter = new wardentabpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);
            viewPager.beginFakeDrag();
        }

        final Intent intent = getIntent();
        if (intent.hasExtra("TabNumber")) {
            String tab = intent.getExtras().getString("TabNumber");
            Log.e("TabNumberFriendList",tab);
            final int value = Integer.parseInt(tab);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(selected==false) {
                        tabLayout.getTabAt(value).select();
                        selected=true;
                    }else{
                        handler.removeCallbacks(null);
                    }
                }
            }, 100);
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check type of intent filter
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Registration success

                    InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
                    try {
                        instanceID.deleteInstanceID();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String token = intent.getStringExtra("token");

                    sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                    previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);

                    //creating editor to store values of shared preferences
                    editor = sharedPreferences.edit();
                    previouseditor = previoussharedPreferences.edit();

                    editor.putString(loginconfig.key_gcmtokenid, token);
                    editor.apply();

                    ongcmregistrationsuccess();
                    //Toast.makeText(getApplicationContext(), "GCM token:" + token, Toast.LENGTH_LONG).show();
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    //Registration error
                    Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();
                } else {
                    //Tobe define
                }
            }
        };

        //Check status of Google play service in device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(ConnectionResult.SUCCESS != resultCode) {
            //Check type of error
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                //So notification
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            //Start service
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }


        //Adding adapter to pager

        viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                viewPager.getCurrentItem();
                return true;
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupTabIcons();

        tabLayout.setOnTabSelectedListener(Teacher_Dashboard_Activity.this);

        previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);
        loggedinuserimage = previoussharedPreferences.getString(previousloginconfig.key_userimage, "");

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/appfont.ttf");
        Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSansBold.ttf");
        Typeface fontawesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome.ttf");
        projecttitle.setTypeface(typeface);
        messageimage.setTypeface(fontawesome);
        //profileimage.setTypeface(fontawesome);
        messageimage.setTextColor(getResources().getColor(R.color.textcolor));
        //profileimage.setTextColor(getResources().getColor(R.color.textcolor));


        Glide.with(Teacher_Dashboard_Activity.this).load(loggedinuserimage).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                Log.i("GLIDE", "onException :", e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                Log.i("GLIDE", "onResourceReady");
                profileimage.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(profileimage);


        messageimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timervalue==true){
                    t.cancel();
                    t = null;
                    timervalue = false;
                }
                Intent i = new Intent(Teacher_Dashboard_Activity.this, Notifications_View.class);
                startActivity(i);
                finish();
            }
        });


        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                final String nusertypeid = sharedPreferences.getString(loginconfig.key_usertypeid, "");


                if (nusertypeid.equals("3")) {
                    if(timervalue==true){
                        t.cancel();
                        t = null;
                        timervalue = false;
                    }
                    Intent m = new Intent(Teacher_Dashboard_Activity.this, Parents_Userinformation_View.class);
                    startActivity(m);
                    finish();
                }else if (nusertypeid.equals("4")){
                    if(timervalue==true){
                        t.cancel();
                        t = null;
                        timervalue = false;
                    }
                    Intent m = new Intent(Teacher_Dashboard_Activity.this, Parents_Userinformation_View.class);
                    startActivity(m);
                    finish();
                }else if (nusertypeid.equals("9")||loogedinusertypeid.equals("10")){
                    if(timervalue==true){
                        t.cancel();
                        t = null;
                        timervalue = false;
                    }
                    Intent m = new Intent(Teacher_Dashboard_Activity.this, Parents_Userinformation_View.class);
                    startActivity(m);
                    finish();
                }else if (nusertypeid.equals("2")){
                    if(timervalue==true){
                        t.cancel();
                        t = null;
                        timervalue = false;
                    }
                    Intent m = new Intent(Teacher_Dashboard_Activity.this, Teachers_Userinformation_View.class);
                    startActivity(m);
                    finish();
                }


                /*sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                //creating editor to store values of shared preferences
                editor = sharedPreferences.edit();

                // Clearing all data from Shared Preferences
                editor.clear();

                //Saving the sharedpreferences
                editor.commit();

                LocalBroadcastManager.getInstance(getBaseContext()).unregisterReceiver(mRegistrationBroadcastReceiver);

                sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);

                //creating editor to store values of shared preferences
                editor = sharedPreferences.edit();
                previouseditor = previoussharedPreferences.edit();

                editor.putString(loginconfig.key_gcmtokenid, "");
                editor.apply();

                InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
                try {
                    instanceID.deleteInstanceID();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ongcmregistrationsuccess();

                Intent i = new Intent(Teacher_Dashboard_Activity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();*/

            }
        });


    }

    private void getnotificationcount() {
        userid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_user_notificationcount_url,
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

                                    countvaluetext = json.optString("countvalue");
                                }

                                if(countvaluetext.equals("0")){
                                    counttext.setVisibility(View.INVISIBLE);
                                }else {
                                    counttext.setVisibility(View.VISIBLE);
                                    counttext.setText(countvaluetext);
                                }

                            }else{
                                counttext.setVisibility(View.INVISIBLE);
                            }

                        } catch (JSONException e) {
                            snackbar = Snackbar.make(view, "Unable to reach Server Please Try Again", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(view, "Unable Connect to Internet. Please Try Again", Snackbar.LENGTH_LONG);
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

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(studentrequest1);

    }

    public void ongcmregistrationsuccess() {
        deleteCache(this);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);
        final String gcmtokenid = sharedPreferences.getString(loginconfig.key_gcmtokenid, "");
        loginid = sharedPreferences.getString(loginconfig.key_userid, "");
        if(loginid.equals("")){
            loginid = previoussharedPreferences.getString(loginconfig.key_userid, "");
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginconfig.key_gcmtokenupdate_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            loginresult = j.optJSONArray("result");

                            //snackbar = Snackbar.make(view,"GCM Registration Success",Snackbar.LENGTH_LONG);
                            //snackbar.show();

                        } catch (JSONException e) {
                            snackbar = Snackbar.make(view,"Unable to recieve data from server due to Technical issues.",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(view,"Unable to Connect with server due to Technical Issues. Kindly Contact Administrator for more details.",Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_userid, loginid);
                params.put(loginconfig.key_gcmtokenid, gcmtokenid);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void setupTabIcons() {

        if(loogedinusertypeid.equals("2")){

            tabLayout.setTabMode(TabLayout.MODE_FIXED);

            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabOne.setText(getResources().getString(R.string.newsfeeds));
            tabOne.setTextColor(getResources().getColor(R.color.textcolor));
            tabOne.setTextSize(24);
            tabLayout.getTabAt(0).setCustomView(tabOne);

            TextView tabtwo = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabtwo.setText(getResources().getString(R.string.attendance));
            tabtwo.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabtwo.setTextSize(18);
            tabLayout.getTabAt(1).setCustomView(tabtwo);


            TextView tabthree = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabthree.setText(getResources().getString(R.string.assignments));
            tabthree.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabthree.setTextSize(18);
            tabLayout.getTabAt(2).setCustomView(tabthree);


            TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabfour.setText(getResources().getString(R.string.leavetracker));
            tabfour.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabfour.setTextSize(18);
            tabLayout.getTabAt(3).setCustomView(tabfour);

            TextView tabfive = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabfive.setText(getResources().getString(R.string.friends));
            tabfive.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabfive.setTextSize(18);
            tabLayout.getTabAt(4).setCustomView(tabfive);

        }else if(loogedinusertypeid.equals("3")){
            tabLayout.setTabMode(TabLayout.MODE_FIXED);

            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabOne.setText(getResources().getString(R.string.newsfeeds));
            tabOne.setTextColor(getResources().getColor(R.color.textcolor));
            tabOne.setTextSize(24);
            tabLayout.getTabAt(0).setCustomView(tabOne);

            TextView tabtwo = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabtwo.setText(getResources().getString(R.string.attendance));
            tabtwo.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabtwo.setTextSize(18);
            tabLayout.getTabAt(1).setCustomView(tabtwo);


            TextView tabthree = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabthree.setText(getResources().getString(R.string.assignments));
            tabthree.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabthree.setTextSize(18);
            tabLayout.getTabAt(2).setCustomView(tabthree);


            TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabfour.setText(getResources().getString(R.string.leavetracker));
            tabfour.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabfour.setTextSize(18);
            tabLayout.getTabAt(3).setCustomView(tabfour);

            TextView tabfive = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabfive.setText(getResources().getString(R.string.friends));
            tabfive.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabfive.setTextSize(18);
            tabLayout.getTabAt(4).setCustomView(tabfive);

            TextView tabsix = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabsix.setText(getResources().getString(R.string.outgoing));
            tabsix.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabsix.setTextSize(18);
            tabLayout.getTabAt(5).setCustomView(tabsix);

        }else if(loogedinusertypeid.equals("4")){
            tabLayout.setTabMode(TabLayout.MODE_FIXED);

            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabOne.setText(getResources().getString(R.string.newsfeeds));
            tabOne.setTextColor(getResources().getColor(R.color.textcolor));
            tabOne.setTextSize(24);
            tabLayout.getTabAt(0).setCustomView(tabOne);

            TextView tabtwo = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabtwo.setText(getResources().getString(R.string.attendance));
            tabtwo.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabtwo.setTextSize(18);
            tabLayout.getTabAt(1).setCustomView(tabtwo);


            TextView tabthree = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabthree.setText(getResources().getString(R.string.assignments));
            tabthree.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabthree.setTextSize(18);
            tabLayout.getTabAt(2).setCustomView(tabthree);


            TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabfour.setText(getResources().getString(R.string.leavetracker));
            tabfour.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabfour.setTextSize(18);
            tabLayout.getTabAt(3).setCustomView(tabfour);

            TextView tabfive = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabfive.setText(getResources().getString(R.string.friends));
            tabfive.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabfive.setTextSize(18);
            tabLayout.getTabAt(4).setCustomView(tabfive);

            TextView tabsix = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabsix.setText(getResources().getString(R.string.outgoing));
            tabsix.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabsix.setTextSize(18);
            tabLayout.getTabAt(5).setCustomView(tabsix);
        }else if(loogedinusertypeid.equals("9")||loogedinusertypeid.equals("10")){
            tabLayout.setTabMode(TabLayout.MODE_FIXED);

            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabOne.setText(getResources().getString(R.string.newsfeeds));
            tabOne.setTextColor(getResources().getColor(R.color.textcolor));
            tabOne.setTextSize(24);
            tabLayout.getTabAt(0).setCustomView(tabOne);

            TextView tabfive = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabfive.setText(getResources().getString(R.string.friends));
            tabfive.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabfive.setTextSize(18);
            tabLayout.getTabAt(1).setCustomView(tabfive);

            TextView tabsix = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomtext, null);
            tabsix.setText(getResources().getString(R.string.outgoing));
            tabsix.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
            tabsix.setTextSize(18);
            tabLayout.getTabAt(2).setCustomView(tabsix);
        }


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
    protected void onResume() {
        super.onResume();

        Log.w("Parents_Dashboard", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("Parents_Dashboard", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();

        check = position;

        tabLayout.getTabAt(position).setCustomView(null);

        tabLayout.setTabTextColors(getResources().getColor(R.color.lightdimtextcolor),getResources().getColor(R.color.textcolor));

        viewPager.setCurrentItem(tab.getPosition());

        if(loogedinusertypeid.equals("2")){

            if(position==0){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.newsfeeds));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==1){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.attendance));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==2){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.assignments));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==3){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.leavetracker));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==4){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.friends));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }


        }else if(loogedinusertypeid.equals("3")){
            if(position==0){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.newsfeeds));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==1){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.attendance));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==2){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.assignments));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==3){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.leavetracker));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==4){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.friends));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==5){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.outgoing));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }
        }else if(loogedinusertypeid.equals("4")){
            if(position==0){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.newsfeeds));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==1){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.attendance));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==2){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.assignments));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==3){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.leavetracker));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==4){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.friends));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==5){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.outgoing));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }
        }else if(loogedinusertypeid.equals("9")||loogedinusertypeid.equals("10")){
            if(position==0){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.newsfeeds));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==1){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.friends));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }else if(position==2){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.outgoing));
                tabOne.setTextColor(getResources().getColor(R.color.textcolor));
                tabOne.setTextSize(24);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        tabLayout.getTabAt(position).setCustomView(null);

        if(loogedinusertypeid.equals("2")){

            if(position==0){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.newsfeeds));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==1){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.attendance));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==2){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.assignments));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==3){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.leavetracker));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==4){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.friends));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }


        }else if(loogedinusertypeid.equals("3")){
            if(position==0){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.newsfeeds));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==1){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.attendance));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==2){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.assignments));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==3){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.leavetracker));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==4){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.friends));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==5){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.outgoing));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }
        }else if(loogedinusertypeid.equals("4")){
            if(position==0){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.newsfeeds));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==1){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.attendance));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==2){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.assignments));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==3){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.leavetracker));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==4){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.friends));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==5){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.outgoing));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }
        }else if(loogedinusertypeid.equals("9")||loogedinusertypeid.equals("10")){
            if(position==0){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.newsfeeds));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==1){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.friends));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }

            if(position==2){
                TextView tabOne = (TextView) LayoutInflater.from(Teacher_Dashboard_Activity.this).inflate(R.layout.tabcustomtext, null);
                tabOne.setText(getResources().getString(R.string.outgoing));
                tabOne.setTextColor(getResources().getColor(R.color.lightdimtextcolor));
                tabOne.setTextSize(18);
                tabLayout.getTabAt(position).setCustomView(tabOne);
            }
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void onDestroy() {
        if(timervalue==true){
            t.cancel();
            t = null;
            timervalue = false;
        }
        super.onDestroy();
    }


    @Override
    public void onBackPressed()
    {
        if(!BackStackFragment.handleBackPressed(getSupportFragmentManager())){

            previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);
            loogedinusertypeid = previoussharedPreferences.getString(previousloginconfig.key_usertypeid, "");

            if(loogedinusertypeid.equals("2")){
                if(tabLayout.getSelectedTabPosition()==0){
                    // This is for Teacher Parents_Dashboard page back navigation functionality
                    if(backcount==0){
                        snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        backcount=1;
                    }else {
                        backcount = 0;
                        super.onBackPressed();
                        finish();
                    }
                }else if(tabLayout.getSelectedTabPosition()==1) {
                    // This is for Teacher Attendance page back navigation functionality
                    value = RecentAttendanceListViewAdapter.getclicked();
                    generateclick = RecentAttendanceListViewAdapter.newgenerateclicked();

                    if(value==true || generateclick==true) {
                        if (loogedinusertypeid.equals("2")) {
                            viewPager.removeAllViews();
                            viewPager.setAdapter(null);
                            teacherattendancetabpageadapter adapter = new teacherattendancetabpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());
                            viewPager.setAdapter(adapter);
                            viewPager.setCurrentItem(1);
                            viewPager.beginFakeDrag();
                            RecentAttendanceListViewAdapter.setclicked(false);
                            backcount=0;
                        }
                    }else{
                        if(backcount==0){
                            snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            backcount=1;
                        }else {
                            backcount=0;
                            super.onBackPressed();
                            RecentAttendanceListViewAdapter.setclicked(false);
                            RecentAttendanceListViewAdapter.setNewgenerateclicled(false);
                        }

                    }

                    /*if(value==true){
                        Teacher_Attendance_Main_Activity innerFragment = new Teacher_Attendance_Main_Activity ().newInstance("Teacher Attendance");
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(R.id.attendancepagemaincontainer, innerFragment);
                        fragmentTransaction.addToBackStack("new");
                        fragmentTransaction.commit();
                    }else{
                        super.onBackPressed();
                    }*/
                }else if(tabLayout.getSelectedTabPosition()==2) {

                    value = RecentAssignmentListViewAdapter.getclicked();
                    generateclick = RecentAssignmentListViewAdapter.newgenerateclicked();

                    if (value == true || generateclick == true) {
                        if (loogedinusertypeid.equals("2")) {
                            viewPager.removeAllViews();
                            viewPager.setAdapter(null);
                            teacherattendancetabpageadapter adapter = new teacherattendancetabpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());
                            viewPager.setAdapter(adapter);
                            viewPager.setCurrentItem(2);
                            viewPager.beginFakeDrag();
                            RecentAssignmentListViewAdapter.setclicked(false);
                            RecentAssignmentListViewAdapter.setgenerateclicked(false);
                            backcount=0;
                        }
                    } else {
                        if(backcount==0){
                            snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            backcount=1;
                        }else {
                            backcount=0;
                            super.onBackPressed();
                            RecentAssignmentListViewAdapter.setclicked(false);
                            RecentAssignmentListViewAdapter.setNewgenerateclicled(false);
                            finish();
                        }

                    }

                }else if(tabLayout.getSelectedTabPosition()==3) {
                    value = Teacher_LeaveTracker_Main_Activity.getclicked();
                    generateclick = Teacher_LeaveTracker_Main_Activity.getitembacked();

                    if(generateclick == true && value == false){
                            Teacher_LeaveTracker_Main_Activity.setitembacked(false);
                            teacherattendancetabpageadapter adapter = new teacherattendancetabpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());
                            viewPager.setAdapter(adapter);
                            viewPager.setCurrentItem(3);
                            viewPager.beginFakeDrag();
                            RecentAssignmentListViewAdapter.setclicked(false);
                            backcount=0;
                        }else if(value==true){
                                Teacher_LeaveTracker_Main_Activity.setclicked(false);
                                viewPager.removeAllViews();
                                viewPager.setAdapter(null);
                                teacherattendancetabpageadapter adapter = new teacherattendancetabpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());
                                viewPager.setAdapter(adapter);
                                viewPager.setCurrentItem(3);
                                viewPager.beginFakeDrag();
                                RecentAssignmentListViewAdapter.setclicked(false);
                                backcount=0;
                            }else {
                                if(backcount==0){
                                    snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    backcount=1;
                                }else {
                                    super.onBackPressed();
                                    finish();
                                }
                            }
                    }else if(tabLayout.getSelectedTabPosition()==4) {
                        if(backcount==0){
                            snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            backcount=1;
                        }else {
                            backcount = 0;
                            super.onBackPressed();
                            finish();
                        }
                    }
                }else if(loogedinusertypeid.equals("3")) {
                    if (tabLayout.getSelectedTabPosition() == 0) {
                        // This is for Teacher Parents_Dashboard page back navigation functionality
                        if (backcount == 0) {
                            snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            backcount = 1;
                        } else {
                            backcount = 0;
                            super.onBackPressed();
                            finish();
                        }
                    } else if (tabLayout.getSelectedTabPosition() == 1) {
                        // This is for Teacher Attendance page back navigation functionality
                        if (backcount == 0) {
                            snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            backcount = 1;
                        } else {
                            backcount = 0;
                            super.onBackPressed();
                            finish();
                        }
                    }else if (tabLayout.getSelectedTabPosition() == 2) {
                        // This is for Teacher Attendance page back navigation functionality
                        if (backcount == 0) {
                            snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            backcount = 1;
                        } else {
                            backcount = 0;
                            super.onBackPressed();
                            finish();
                        }
                    }else if (tabLayout.getSelectedTabPosition() == 3) {
                        // This is for Teacher Attendance page back navigation functionality
                        value = Parents_Leavetracker_rowitems_click.getclicked();
                        generateclick = Parents_Leavetracker_rowitems_click.getgenerateclicked();

                        if (value == true || generateclick == true) {
                            viewPager.removeAllViews();
                            viewPager.setAdapter(null);
                            parentattendancetabpageadapter adapter = new parentattendancetabpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());
                            viewPager.setAdapter(adapter);
                            viewPager.setCurrentItem(3);
                            viewPager.beginFakeDrag();
                            Parents_Leavetracker_rowitems_click.setclicked(false);
                            Parents_Leavetracker_rowitems_click.setgenerateclicked(false);
                            backcount=0;
                        } else {
                            if(backcount==0){
                                snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                backcount=1;
                            }else {
                                backcount=0;
                                super.onBackPressed();
                                Parents_Leavetracker_rowitems_click.setclicked(false);
                                Parents_Leavetracker_rowitems_click.setgenerateclicked(false);
                                finish();
                            }

                        }

                    }else if (tabLayout.getSelectedTabPosition() == 4) {
                        // This is for Teacher Attendance page back navigation functionality
                        if (backcount == 0) {
                            snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            backcount = 1;
                        } else {
                            backcount = 0;
                            super.onBackPressed();
                            finish();
                        }
                    }else if (tabLayout.getSelectedTabPosition() == 5) {
                        // This is for Teacher Attendance page back navigation functionality
                        value = Parents_Outgoing_rowitems_click.getclicked();
                        generateclick = Parents_Outgoing_rowitems_click.getgenerateclicked();

                        if (value == true || generateclick == true) {
                            viewPager.removeAllViews();
                            viewPager.setAdapter(null);
                            parentattendancetabpageadapter adapter = new parentattendancetabpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());
                            viewPager.setAdapter(adapter);
                            viewPager.setCurrentItem(5);
                            viewPager.beginFakeDrag();
                            Parents_Outgoing_rowitems_click.setclicked(false);
                            Parents_Outgoing_rowitems_click.setgenerateclicked(false);
                            backcount=0;
                        } else {
                            if(backcount==0){
                                snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                backcount=1;
                            }else {
                                backcount=0;
                                super.onBackPressed();
                                Parents_Outgoing_rowitems_click.setclicked(false);
                                Parents_Outgoing_rowitems_click.setgenerateclicked(false);
                                finish();
                            }

                        }

                    }
                }else if(loogedinusertypeid.equals("4")) {
                    if (tabLayout.getSelectedTabPosition() == 0) {
                        // This is for Teacher Parents_Dashboard page back navigation functionality
                        if (backcount == 0) {
                            snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            backcount = 1;
                        } else {
                            backcount = 0;
                            super.onBackPressed();
                            finish();
                        }
                    } else if (tabLayout.getSelectedTabPosition() == 1) {
                        // This is for Teacher Attendance page back navigation functionality
                        if (backcount == 0) {
                            snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            backcount = 1;
                        } else {
                            backcount = 0;
                            super.onBackPressed();
                            finish();
                        }
                    }else if (tabLayout.getSelectedTabPosition() == 2) {
                        // This is for Teacher Attendance page back navigation functionality
                        if (backcount == 0) {
                            snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            backcount = 1;
                        } else {
                            backcount = 0;
                            super.onBackPressed();
                            finish();
                        }
                    }else if (tabLayout.getSelectedTabPosition() == 3) {
                        // This is for Teacher Attendance page back navigation functionality
                        value = Parents_Leavetracker_rowitems_click.getclicked();
                        generateclick = Parents_Leavetracker_rowitems_click.getgenerateclicked();

                        if (value == true || generateclick == true) {
                                viewPager.removeAllViews();
                                viewPager.setAdapter(null);
                                parentattendancetabpageadapter adapter = new parentattendancetabpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());
                                viewPager.setAdapter(adapter);
                                viewPager.setCurrentItem(3);
                                viewPager.beginFakeDrag();
                                Parents_Leavetracker_rowitems_click.setclicked(false);
                                Parents_Leavetracker_rowitems_click.setgenerateclicked(false);
                                backcount=0;
                        } else {
                            if(backcount==0){
                                snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                backcount=1;
                            }else {
                                backcount=0;
                                super.onBackPressed();
                                Parents_Leavetracker_rowitems_click.setclicked(false);
                                Parents_Leavetracker_rowitems_click.setgenerateclicked(false);
                                finish();
                            }

                        }

                    }else if (tabLayout.getSelectedTabPosition() == 4) {
                        // This is for Teacher Attendance page back navigation functionality
                        if (backcount == 0) {
                            snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            backcount = 1;
                        } else {
                            backcount = 0;
                            super.onBackPressed();
                            finish();
                        }
                    }else if (tabLayout.getSelectedTabPosition() == 5) {
                        // This is for Teacher Attendance page back navigation functionality
                        value = Parents_Outgoing_rowitems_click.getclicked();
                        generateclick = Parents_Outgoing_rowitems_click.getgenerateclicked();

                        if (value == true || generateclick == true) {
                            viewPager.removeAllViews();
                            viewPager.setAdapter(null);
                            studentattendancetabpageadapter adapter = new studentattendancetabpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());
                            viewPager.setAdapter(adapter);
                            viewPager.setCurrentItem(5);
                            viewPager.beginFakeDrag();
                            Parents_Outgoing_rowitems_click.setclicked(false);
                            Parents_Outgoing_rowitems_click.setgenerateclicked(false);
                            backcount=0;
                        } else {
                            if(backcount==0){
                                snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                backcount=1;
                            }else {
                                backcount=0;
                                super.onBackPressed();
                                Parents_Outgoing_rowitems_click.setclicked(false);
                                Parents_Outgoing_rowitems_click.setgenerateclicked(false);
                                finish();
                            }

                        }

                    }

                }else if(loogedinusertypeid.equals("9")||loogedinusertypeid.equals("10")){
                if(tabLayout.getSelectedTabPosition()==0){
                    // This is for Teacher Parents_Dashboard page back navigation functionality
                    if(backcount==0){
                        snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        backcount=1;
                    }else {
                        backcount = 0;
                        super.onBackPressed();
                        finish();
                    }
                }else if(tabLayout.getSelectedTabPosition()==1) {
                    if(backcount==0){
                        snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        backcount=1;
                    }else {
                        backcount = 0;
                        super.onBackPressed();
                        finish();
                    }
                }else if(tabLayout.getSelectedTabPosition()==2) {
                    value = Warden_Outgoingpermission_Main_Activity.getclicked();
                    generateclick = Warden_Outgoingpermission_Main_Activity.getitembacked();

                    if(generateclick == true && value == false){
                        Teacher_LeaveTracker_Main_Activity.setitembacked(false);
                        teacherattendancetabpageadapter adapter = new teacherattendancetabpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());
                        viewPager.setAdapter(adapter);
                        viewPager.setCurrentItem(2);
                        viewPager.beginFakeDrag();
                        Warden_Outgoingpermission_Main_Activity.setclicked(false);
                        backcount=0;
                    }else if(value==true){
                        Warden_Outgoingpermission_Main_Activity.setclicked(false);
                        viewPager.removeAllViews();
                        viewPager.setAdapter(null);
                        teacherattendancetabpageadapter adapter = new teacherattendancetabpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());
                        viewPager.setAdapter(adapter);
                        viewPager.setCurrentItem(3);
                        viewPager.beginFakeDrag();
                        Warden_Outgoingpermission_Main_Activity.setclicked(false);
                        backcount=0;
                    }else {
                        if(backcount==0){
                            snackbar = Snackbar.make(view, "Press back again to exit", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            backcount=1;
                        }else {
                            super.onBackPressed();
                            finish();
                        }
                    }
                }

            }

        }
    }

}


