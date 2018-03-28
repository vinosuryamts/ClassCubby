package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Notifications;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import uk.co.yellowrays.collegeinfo.classcubby.Teacher_Dashboard_Activity;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.userinformation.Parents_Userinformation_View;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.userinformation.Teachers_Userinformation_View;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.previousloginconfig;

/**
 * Created by vinos on 1/28/2017.
 */

public class Notifications_View extends AppCompatActivity {

    Snackbar snackbar;
    View mainview;
    private Toolbar toolbar;
    private ListView notificationlistview;
    TextView emptycriteriamsg,projecttitle,attendancepagename;
    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;
    private JSONArray loginresult;
    private NotificationsMainAdapter adapter;
    ProgressDialog loginDialog;

    int notificount = 0;
    int eventcount=0,oldposition=0,loadingstate=0,newclickevent=0;
    String userid;
    String notificationmsg,notificationtype,postedbyname,postedbyimage,notificationpostedtime,notificationreaddate;
    ArrayList<String> arrnotificationid,arrnotificationmsg,arrnotificationtype,arrpostedbyname,arrpostedbyimage,arrnotificationpostedtime,arrnotificationreaddate;
    String[]  strnotificationid,strnotificationmsg,strnotificationtype,strpostedbyname,strpostedbyimage,strnotificationpostedtime,strnotificationreaddate;
    String notificationid;

    RelativeLayout notificationCount1;
    TextView counttext;
    TextView btnNotifCount;
    String countvaluetext;
    ImageView profileimage;
    int loadingstatevalue;
    Timer t;
    boolean timervalue;
    private String loggedinuserimage,loogedinusertypeid;
    private RequestQueue requestQueue1 = null;
    private RequestQueue requestQueue3 = null;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_main_xml);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        notificationlistview = (ListView) findViewById(R.id.notificationslistview);
        emptycriteriamsg = (TextView) findViewById(R.id.emptycriteriamsg);
        profileimage = (ImageView) findViewById(R.id.profileimage);
        projecttitle = (TextView)findViewById(R.id.projecttitle);
        attendancepagename = (TextView)findViewById(R.id.attendancepagename);

        mainview = findViewById(R.id.parentsdashboardwholecontainer);
        mainview.setEnabled(true);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/appfont.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSansBold.ttf");
        projecttitle.setTypeface(typeface);
        attendancepagename.setTypeface(boldtypeface);

        previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);
        loggedinuserimage = previoussharedPreferences.getString(previousloginconfig.key_userimage, "");

        Glide.with(Notifications_View.this).load(loggedinuserimage).listener(new RequestListener<String, GlideDrawable>() {
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

        Typeface fontawesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome.ttf");


        loadingstatevalue =1;

        t = new Timer();
        timervalue =true;
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                getnotificationcount();
            }

        },5000,5000);


        if (AppStatus.getInstance(Notifications_View.this).isOnline()) {
            getnotificationsinformation();
        }else {
            snackbar = Snackbar.make(mainview,"Please Connect to the Internet and Try Again",Snackbar.LENGTH_LONG);
            snackbar.show();
        }

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
                    Intent m = new Intent(Notifications_View.this, Parents_Userinformation_View.class);
                    startActivity(m);
                    finish();
                }else if (nusertypeid.equals("2")){
                    if(timervalue==true){
                        t.cancel();
                        t = null;
                        timervalue = false;
                    }
                    Intent m = new Intent(Notifications_View.this, Teachers_Userinformation_View.class);
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



    @Override
    public void onBackPressed() {

        if(loadingstate==1){

        }else{
            //super.onBackPressed();
            if(timervalue == true) {
                t.cancel();
                t = null;
                timervalue = false;
            }

            Intent m = new Intent(Notifications_View.this, Teacher_Dashboard_Activity.class);
            startActivity(m);
            finish();
        }

    }

    private void getnotificationsinformation() {
        deleteCache(Notifications_View.this);
        userid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        userid = userid.replace("[", "");
        userid = userid.replace("]", "");
        userid = userid.replace("\"", "");

        loginDialog = new ProgressDialog(Notifications_View.this);
        loginDialog.setIndeterminate(true);
        loginDialog.setTitle("Please Wait");
        loginDialog.setMessage("Loading Notifications...");
        loginDialog.setCancelable(false);
        loginDialog.setCanceledOnTouchOutside(false);
        loginDialog.show();
        loadingstate = 1;
        mainview.setEnabled(false);

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_user_notificationlist_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            loginresult = j.getJSONArray("result");

                            if (loginresult.length() > 0) {

                                arrnotificationid = new ArrayList<String>(loginresult.length());
                                arrnotificationmsg = new ArrayList<String>(loginresult.length());
                                arrnotificationtype = new ArrayList<String>(loginresult.length());
                                arrpostedbyname = new ArrayList<String>(loginresult.length());
                                arrpostedbyimage = new ArrayList<String>(loginresult.length());
                                arrnotificationpostedtime = new ArrayList<String>(loginresult.length());
                                arrnotificationreaddate = new ArrayList<String>(loginresult.length());


                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);

                                    notificationid = json.optString("notificationid");
                                    notificationmsg = json.optString("notificationmsg");
                                    notificationtype = json.optString("notificationtype");
                                    postedbyname = json.optString("postedbyname");
                                    postedbyimage = json.optString("postedbyimage");
                                    notificationpostedtime = json.optString("notificationpostedtime");
                                    notificationreaddate = json.optString("notificationreaddate");

                                    arrnotificationid.add(notificationid);
                                    arrnotificationmsg.add(notificationmsg);
                                    arrnotificationtype.add(notificationtype);
                                    arrpostedbyname.add(postedbyname);
                                    arrpostedbyimage.add(postedbyimage);
                                    arrnotificationpostedtime.add(notificationpostedtime);
                                    arrnotificationreaddate.add(notificationreaddate);
                                }


                                sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                //creating editor to store values of shared preferences
                                editor = sharedPreferences.edit();

                                editor.putString(loginconfig.key_notification_id, arrnotificationid.toString());
                                editor.putString(loginconfig.key_notification_notificationmsg, arrnotificationmsg.toString());
                                editor.putString(loginconfig.key_notification_notificationtype, arrnotificationtype.toString());
                                editor.putString(loginconfig.key_notification_postedbyname, arrpostedbyname.toString());
                                editor.putString(loginconfig.key_notification_postedbyimage, arrpostedbyimage.toString());
                                editor.putString(loginconfig.key_notification_notificationpostedtime, arrnotificationpostedtime.toString());
                                editor.putString(loginconfig.key_notification_notificationreaddate, arrnotificationreaddate.toString());
                                editor.apply();



                                notificationid = MainActivity.sharedPreferences.getString(loginconfig.key_notification_id, "");
                                notificationmsg = MainActivity.sharedPreferences.getString(loginconfig.key_notification_notificationmsg, "");
                                notificationtype = MainActivity.sharedPreferences.getString(loginconfig.key_notification_notificationtype, "");
                                postedbyname = MainActivity.sharedPreferences.getString(loginconfig.key_notification_postedbyname, "");
                                postedbyimage = MainActivity.sharedPreferences.getString(loginconfig.key_notification_postedbyimage, "");
                                notificationpostedtime = MainActivity.sharedPreferences.getString(loginconfig.key_notification_notificationpostedtime, "");
                                notificationreaddate = MainActivity.sharedPreferences.getString(loginconfig.key_notification_notificationreaddate, "");


                                notificationid = notificationid.replace("[", "");
                                notificationid = notificationid.replace("]", "");
                                notificationid = notificationid.replace("\"", "");
                                strnotificationid = notificationid.split(",");

                                notificationmsg = notificationmsg.replace("[", "");
                                notificationmsg = notificationmsg.replace("]", "");
                                notificationmsg = notificationmsg.replace("\"", "");
                                strnotificationmsg = notificationmsg.split(",");

                                notificationtype = notificationtype.replace("[", "");
                                notificationtype = notificationtype.replace("]", "");
                                notificationtype = notificationtype.replace("\"", "");
                                strnotificationtype = notificationtype.split(",");

                                postedbyname = postedbyname.replace("[", "");
                                postedbyname = postedbyname.replace("]", "");
                                postedbyname = postedbyname.replace("\"", "");
                                strpostedbyname = postedbyname.split(",");

                                postedbyimage = postedbyimage.replace("[", "");
                                postedbyimage = postedbyimage.replace("]", "");
                                postedbyimage = postedbyimage.replace("\"", "");
                                postedbyimage = postedbyimage.replace("\\", "");
                                strpostedbyimage = postedbyimage.split(",");

                                notificationpostedtime = notificationpostedtime.replace("[", "");
                                notificationpostedtime = notificationpostedtime.replace("]", "");
                                notificationpostedtime = notificationpostedtime.replace("\"", "");
                                strnotificationpostedtime = notificationpostedtime.split(",");

                                notificationreaddate = notificationreaddate.replace("[", "");
                                notificationreaddate = notificationreaddate.replace("]", "");
                                notificationreaddate = notificationreaddate.replace("\"", "");
                                strnotificationreaddate = notificationreaddate.split(",");

                                notificationlistview.setVisibility(View.VISIBLE);
                                emptycriteriamsg.setVisibility(View.INVISIBLE);

                                eventcount = strnotificationid.length;
                                mainview.setEnabled(true);

                                adapter = new NotificationsMainAdapter(Notifications_View.this,strnotificationid,strnotificationmsg,strnotificationtype,strpostedbyname,strpostedbyimage,strnotificationpostedtime,strnotificationreaddate,t,timervalue);
                                notificationlistview.setAdapter(adapter);
                                notificationlistview.setScrollingCacheEnabled(false);

                                if(newclickevent==1){
                                    notificationlistview.setSelection(0);
                                    notificationlistview.smoothScrollToPosition(0);
                                    newclickevent = 0;
                                    adapter.notifyDataSetChanged();
                                    requestQueue1 = null;
                                }else {
                                    notificationlistview.setSelection(oldposition);
                                    notificationlistview.smoothScrollToPosition(oldposition);
                                    adapter.notifyDataSetChanged();
                                    requestQueue1 = null;
                                }

                                if(loadingstate==1){
                                    loadingstate = 0;
                                    mainview.setEnabled(true);
                                    loginDialog.dismiss();
                                }

                                notificationlistview.setOnScrollListener(new AbsListView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                                                && (notificationlistview.getLastVisiblePosition() - notificationlistview.getHeaderViewsCount() -
                                                notificationlistview.getFooterViewsCount()) >= (adapter.getCount() - 1)) {

                                            oldposition = notificationlistview.getFirstVisiblePosition();

                                            if(notificationlistview.getFirstVisiblePosition() == 0){
                                                newclickevent = 1;
                                            }else{
                                                newclickevent = 0;
                                            }

                                            if(eventcount>=notificount+10){

                                                notificount = notificount+10;

                                                handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loadingstate = 1;
                                                        getnotificationsinformation();
                                                    }
                                                }, 50);
                                            }else{
                                                eventcount = oldposition;
                                            }

                                        }
                                    }

                                    @Override
                                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                                    }
                                });



                            }else{
                                if(loadingstate==1){
                                    loadingstate = 0;
                                    mainview.setEnabled(true);
                                    loginDialog.dismiss();
                                }
                                notificationlistview.setVisibility(View.INVISIBLE);
                                emptycriteriamsg.setVisibility(View.VISIBLE);
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

                params.put(loginconfig.key_userid, userid);
                params.put(loginconfig.key_notification_count, String.valueOf(notificount));

                return params;
            }
        };
        if (requestQueue1 == null) {
            requestQueue1 = Volley.newRequestQueue(getBaseContext());
            requestQueue1.add(studentrequest1);
        }

    }



    @Override
    protected void onDestroy(){
        if(loadingstate==1){
            loadingstate = 0;
            //mainview.setEnabled(true);
            loginDialog.dismiss();
            if(timervalue==true){
                t.cancel();
                t = null;
                timervalue = false;
            }
        }else{
            //mainview.setEnabled(true);
            if(timervalue==true){
                t.cancel();
                t = null;
                timervalue = false;
            }
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item1 = menu.findItem(R.id.action_notifications);
        MenuItemCompat.setActionView(item1, R.layout.notification_update_count_layout);
        notificationCount1 = (RelativeLayout) MenuItemCompat.getActionView(item1);

        View count = menu.findItem(R.id.action_notifications).getActionView();
        btnNotifCount = (TextView) count.findViewById(R.id.button1);
        counttext = (TextView) count.findViewById(R.id.counttext);
        Typeface fontawesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome.ttf");
        btnNotifCount.setTypeface(fontawesome);
        btnNotifCount.setClickable(true);
        counttext.setClickable(true);
        final RelativeLayout relativeLayout = (RelativeLayout) count.findViewById(R.id.badge_layout1);


        getnotificationcount();

        if(loadingstatevalue == 1 ){

        }else{
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(loadingstate==1){

                    }else{
                        MultiClickPreventer.preventMultiClick(v);
                        btnNotifCount.setClickable(false);
                        counttext.setClickable(false);
                        counttext.setVisibility(View.INVISIBLE);
                        Intent i = new Intent(getBaseContext(),Notifications_View.class);
                        startActivity(i);
                        finish();
                    }

                }
            });

            btnNotifCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(loadingstate==1){

                    }else{
                        MultiClickPreventer.preventMultiClick(v);
                        btnNotifCount.setClickable(false);
                        counttext.setClickable(false);
                        counttext.setVisibility(View.INVISIBLE);
                        Intent i = new Intent(getBaseContext(),Notifications_View.class);
                        startActivity(i);
                        finish();
                    }
                }
            });

            counttext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(loadingstate==1){

                    }else{
                        MultiClickPreventer.preventMultiClick(v);
                        btnNotifCount.setClickable(false);
                        counttext.setClickable(false);
                        counttext.setVisibility(View.INVISIBLE);
                        Intent i = new Intent(getBaseContext(),Notifications_View.class);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }


        return super.onCreateOptionsMenu(menu);
    }


    private void getnotificationcount() {
        deleteCache(Notifications_View.this);
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

                params.put(loginconfig.key_userid, userid);

                return params;
            }
        };


        if (requestQueue3 == null) {
            requestQueue3 = Volley.newRequestQueue(getBaseContext());
            requestQueue3.add(studentrequest1);
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
}
