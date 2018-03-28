package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.userinformation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.yellowrays.collegeinfo.classcubby.AppStatus;
import uk.co.yellowrays.collegeinfo.classcubby.MainActivity;
import uk.co.yellowrays.collegeinfo.classcubby.R;
import uk.co.yellowrays.collegeinfo.classcubby.Teacher_Dashboard_Activity;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Notifications.MultiClickPreventer;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Notifications.NotificationsMainAdapter;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Notifications.Notifications_View;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.previousloginconfig;

/**
 * Created by Vino on 10/24/2017.
 */

public class Parents_Userinformation_View extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    View mainview,view;
    private RecyclerView.LayoutManager mLayoutManager;
    Snackbar snackbar;
    private CustomAdapter mAdapter;
    Toolbar toolbar;
    String dob,salutation,firstname,lastname,gender,addressline1,addressline2,userimage,cityname,pincode,contactnumber,altcontactnumber,emailid,occupation;
    ArrayList<String> arrdob,arrsalutation,arrfirstname,arrlastname,arrgender,arraddressline1,arraddressline2,arruserimage,arrcityname,arrpincode,arrcontactnumber,arraltcontactnumber,arremailid,arroccupation;

    String[] dataSet,newvalues;
    int[] dataSetTypes;
    org.json.simple.JSONObject mainObj,jo;
    JSONArray ja;
    Timer t;
    boolean timervalue;

    int notificount = 0;
    int eventcount=0,oldposition=0,loadingstate=0,newclickevent=0;
    String userid;
    String notificationmsg,notificationtype,postedbyname,postedbyimage,notificationpostedtime,notificationreaddate;
    ArrayList<String> arrnotificationid,arrnotificationmsg,arrnotificationtype,arrpostedbyname,arrpostedbyimage,arrnotificationpostedtime,arrnotificationreaddate;
    String[]  strnotificationid,strnotificationmsg,strnotificationtype,strpostedbyname,strpostedbyimage,strnotificationpostedtime,strnotificationreaddate;
    String notificationid;

    RelativeLayout notificationCount1;
    TextView counttext;
    TextView btnNotifCount,projecttitle;
    String countvaluetext;
    ImageView profileimage;
    int loadingstatevalue;

    List<UserInformationList> arraylist = new ArrayList<UserInformationList>();

    private String loggedinuserimage,loogedinusertypeid;
    private RequestQueue requestQueue1 = null;
    private RequestQueue requestQueue3 = null;

    private JSONArray loginresult;
    private NotificationsMainAdapter adapter;
    ProgressDialog loginDialog;

    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;


    public Parents_Userinformation_View() {
        // Required empty public constructor
    }

    public static Parents_Userinformation_View newInstance(String param1) {
        Parents_Userinformation_View fragment = new Parents_Userinformation_View();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parents_userinformation_main_xml);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        profileimage = (ImageView) findViewById(R.id.profileimage);
        projecttitle = (TextView)findViewById(R.id.projecttitle);
        mainview = findViewById(R.id.teachersuserinformationcontainer);

        previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);
        loggedinuserimage = previoussharedPreferences.getString(previousloginconfig.key_userimage, "");

        Glide.with(Parents_Userinformation_View.this).load(loggedinuserimage).listener(new RequestListener<String, GlideDrawable>() {
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

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/appfont.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSansBold.ttf");
        projecttitle.setTypeface(typeface);

        mLayoutManager = new LinearLayoutManager(Parents_Userinformation_View.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        t = new Timer();
        timervalue =true;
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                getnotificationcount();
            }

        },5000,5000);

        if (AppStatus.getInstance(Parents_Userinformation_View.this).isOnline()) {
            getuserinformation();
        }else {
            snackbar = Snackbar.make(mainview,"Please Connect to the Internet and Try Again",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


    private void getuserinformation() {
        deleteCache(Parents_Userinformation_View.this);
        userid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");

        userid = userid.replace("[", "");
        userid = userid.replace("]", "");
        userid = userid.replace("\"", "");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_parents_userinformation_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            loginresult = j.getJSONArray("result");

                            if (loginresult.length() > 0) {

                                arrsalutation = new ArrayList<String>(loginresult.length());
                                arrfirstname = new ArrayList<String>(loginresult.length());
                                arrlastname = new ArrayList<String>(loginresult.length());
                                arrgender = new ArrayList<String>(loginresult.length());
                                arraddressline1 = new ArrayList<String>(loginresult.length());
                                arraddressline2 = new ArrayList<String>(loginresult.length());
                                arruserimage = new ArrayList<String>(loginresult.length());
                                arrcityname = new ArrayList<String>(loginresult.length());
                                arrpincode = new ArrayList<String>(loginresult.length());
                                arrcontactnumber = new ArrayList<String>(loginresult.length());
                                arraltcontactnumber = new ArrayList<String>(loginresult.length());
                                arremailid = new ArrayList<String>(loginresult.length());
                                arroccupation = new ArrayList<String>(loginresult.length());


                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);

                                    salutation = json.optString("salutation");
                                    firstname = json.optString("firstname");
                                    lastname = json.optString("lastname");
                                    gender = json.optString("gender");
                                    addressline1 = json.optString("addressline1");
                                    addressline2 = json.optString("addressline2");
                                    userimage = json.optString("userimage");
                                    cityname = json.optString("cityname");
                                    pincode = json.optString("pincode");
                                    contactnumber = json.optString("contactnumber");
                                    altcontactnumber = json.optString("altcontactnumber");
                                    emailid = json.optString("emailid");
                                    occupation = json.optString("occupation");

                                    arrsalutation.add(salutation);
                                    arrfirstname.add(firstname);
                                    arrlastname.add(lastname);
                                    arrgender.add(gender);
                                    arraddressline1.add(addressline1);
                                    arraddressline2.add(addressline2);
                                    arruserimage.add(userimage);
                                    arrcityname.add(cityname);
                                    arrpincode.add(pincode);
                                    arrcontactnumber.add(contactnumber);
                                    arraltcontactnumber.add(altcontactnumber);
                                    arremailid.add(emailid);
                                    arroccupation.add(occupation);
                                }


                                sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                //creating editor to store values of shared preferences
                                editor = sharedPreferences.edit();

                                editor.putString(loginconfig.key_user_salutation, arrsalutation.toString());
                                editor.putString(loginconfig.key_user_firstname, arrfirstname.toString());
                                editor.putString(loginconfig.key_user_lastname, arrlastname.toString());
                                editor.putString(loginconfig.key_user_gender, arrgender.toString());
                                editor.putString(loginconfig.key_user_addressline1, arraddressline1.toString());
                                editor.putString(loginconfig.key_user_addressline2, arraddressline2.toString());
                                editor.putString(loginconfig.key_user_userimage, arruserimage.toString());
                                editor.putString(loginconfig.key_user_cityname, arrcityname.toString());
                                editor.putString(loginconfig.key_user_pincode, arrpincode.toString());
                                editor.putString(loginconfig.key_user_contactnumber, arrcontactnumber.toString());
                                editor.putString(loginconfig.key_user_altcontactnumber, arraltcontactnumber.toString());
                                editor.putString(loginconfig.key_user_emailid, arremailid.toString());
                                editor.putString(loginconfig.key_user_occupation, arroccupation.toString());
                                editor.apply();



                                salutation = MainActivity.sharedPreferences.getString(loginconfig.key_user_salutation, "");
                                firstname = MainActivity.sharedPreferences.getString(loginconfig.key_user_firstname, "");
                                lastname = MainActivity.sharedPreferences.getString(loginconfig.key_user_lastname, "");
                                gender = MainActivity.sharedPreferences.getString(loginconfig.key_user_gender, "");
                                addressline1 = MainActivity.sharedPreferences.getString(loginconfig.key_user_addressline1, "");
                                addressline2 = MainActivity.sharedPreferences.getString(loginconfig.key_user_addressline2, "");
                                userimage = MainActivity.sharedPreferences.getString(loginconfig.key_user_userimage, "");
                                cityname = MainActivity.sharedPreferences.getString(loginconfig.key_user_cityname, "");
                                pincode = MainActivity.sharedPreferences.getString(loginconfig.key_user_pincode, "");
                                contactnumber = MainActivity.sharedPreferences.getString(loginconfig.key_user_contactnumber, "");
                                altcontactnumber = MainActivity.sharedPreferences.getString(loginconfig.key_user_altcontactnumber, "");
                                emailid = MainActivity.sharedPreferences.getString(loginconfig.key_user_emailid, "");
                                occupation = MainActivity.sharedPreferences.getString(loginconfig.key_user_occupation, "");


                                salutation = salutation.replace("[", "");
                                salutation = salutation.replace("]", "");
                                salutation = salutation.replace("\"", "");

                                firstname = firstname.replace("[", "");
                                firstname = firstname.replace("]", "");
                                firstname = firstname.replace("\"", "");

                                lastname = lastname.replace("[", "");
                                lastname = lastname.replace("]", "");
                                lastname = lastname.replace("\"", "");

                                gender = gender.replace("[", "");
                                gender = gender.replace("]", "");
                                gender = gender.replace("\"", "");

                                addressline1 = addressline1.replace("[", "");
                                addressline1 = addressline1.replace("]", "");
                                addressline1 = addressline1.replace("\"", "");
                                addressline1 = addressline1.replace(",","```");

                                addressline2 = addressline2.replace("[", "");
                                addressline2 = addressline2.replace("]", "");
                                addressline2 = addressline2.replace("\"", "");
                                addressline2 = addressline2.replace(",","```");

                                userimage = userimage.replace("[", "");
                                userimage = userimage.replace("]", "");
                                userimage = userimage.replace("\"", "");

                                cityname = cityname.replace("[", "");
                                cityname = cityname.replace("]", "");
                                cityname = cityname.replace("\"", "");

                                pincode = pincode.replace("[", "");
                                pincode = pincode.replace("]", "");
                                pincode = pincode.replace("\"", "");

                                contactnumber = contactnumber.replace("[", "");
                                contactnumber = contactnumber.replace("]", "");
                                contactnumber = contactnumber.replace("\"", "");

                                altcontactnumber = altcontactnumber.replace("[", "");
                                altcontactnumber = altcontactnumber.replace("]", "");
                                altcontactnumber = altcontactnumber.replace("\"", "");

                                emailid = emailid.replace("[", "");
                                emailid = emailid.replace("]", "");
                                emailid = emailid.replace("\"", "");

                                occupation = occupation.replace("[", "");
                                occupation = occupation.replace("]", "");
                                occupation = occupation.replace("\"", "");


                                String[] values = {"0","1","2"};

                                newvalues = new String[values.length];

                                int k=0;
                                jo = new org.json.simple.JSONObject();
                                ja = new JSONArray();
                                mainObj = new org.json.simple.JSONObject();
                                jo.put("userimage", userimage);
                                jo.put("username", firstname+" "+lastname);
                                jo.put("address", addressline1 +" "+ addressline2 +" | "+ cityname);
                                ja.put(jo);
                                mainObj.put("valueslist", ja);
                                newvalues[k]=mainObj.toString();


                                int i =1;
                                jo = new org.json.simple.JSONObject();
                                ja = new JSONArray();
                                mainObj = new org.json.simple.JSONObject();
                                jo.put("salutation", salutation);
                                jo.put("firstname", firstname);
                                jo.put("lastname", lastname);
                                jo.put("gender", gender);
                                jo.put("addressline1", addressline1);
                                jo.put("addressline2", addressline2);
                                jo.put("cityname", cityname);
                                jo.put("pincode", pincode);
                                jo.put("occupation", occupation);
                                ja.put(jo);
                                mainObj.put("valueslist", ja);
                                newvalues[i]=mainObj.toString();

                                int r =2;
                                jo = new org.json.simple.JSONObject();
                                ja = new JSONArray();
                                mainObj = new org.json.simple.JSONObject();
                                jo.put("contactnumber", contactnumber);
                                jo.put("altcontactnumber", altcontactnumber);
                                jo.put("emailid", emailid);
                                ja.put(jo);
                                mainObj.put("valueslist", ja);
                                newvalues[r]=mainObj.toString();


                                dataSet = new String[newvalues.length];
                                dataSetTypes = new int[newvalues.length];

                                for(int t=0;t<newvalues.length;t++){
                                    if(t==0){
                                        dataSet[t]=newvalues[t];
                                        dataSetTypes[t]=0;
                                    }else if(t==1){
                                        dataSet[t]=newvalues[t];
                                        dataSetTypes[t]=1;
                                    }else{
                                        dataSet[t]=newvalues[t];
                                        dataSetTypes[t]=2;
                                    }
                                }


                                for (int y = 0; y < arrfirstname.size(); y++)
                                {
                                    UserInformationList wp = new UserInformationList(arrsalutation.get(y),arrfirstname.get(y),
                                            arrlastname.get(y),arrgender.get(y),arraddressline1.get(y),arraddressline2.get(y),
                                            arruserimage.get(y),arrcityname.get(y),arrpincode.get(y),arrcontactnumber.get(y),
                                            arraltcontactnumber.get(y),arremailid.get(y),arroccupation.get(y),"");
                                    // Binds all strings into an array
                                    arraylist.add(wp);
                                }

                                mAdapter = new CustomAdapter(Parents_Userinformation_View.this,dataSet,dataSetTypes,arraylist);
                                mRecyclerView.setAdapter(mAdapter);

                            }else{
                                snackbar = Snackbar.make(mainview,"Unable to display User Information, Please Try again Later.",Snackbar.LENGTH_LONG);
                                snackbar.show();
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
        RequestQueue studentlist = Volley.newRequestQueue(Parents_Userinformation_View.this);
        studentlist.add(studentrequest1);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(timervalue == true) {
            t.cancel();
            t = null;
            timervalue = false;
        }
        Intent m = new Intent(Parents_Userinformation_View.this, Teacher_Dashboard_Activity.class);
        startActivity(m);
        finish();
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
                        Intent i = new Intent(Parents_Userinformation_View.this,Notifications_View.class);
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
                        Intent i = new Intent(Parents_Userinformation_View.this,Notifications_View.class);
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
                        Intent i = new Intent(Parents_Userinformation_View.this,Notifications_View.class);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }


        return super.onCreateOptionsMenu(menu);
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
