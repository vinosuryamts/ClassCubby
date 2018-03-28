package uk.co.yellowrays.collegeinfo.classcubby;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Notifications.MultiClickPreventer;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Notifications.NotificationsMainAdapter;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Notifications.Notifications_View;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.userinformation.Parents_Userinformation_View;
import uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.userinformation.Teachers_Userinformation_View;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.previousloginconfig;

/**
 * Created by vinos on 1/28/2017.
 */

public class ChangePassword_View extends AppCompatActivity {

    Snackbar snackbar;
    View mainview;
    private Toolbar toolbar;

    private Button submit;
    private EditText password,retypepassword;
    private ProgressDialog loginDialog;
    private String loggeduserid;
    Timer t;
    boolean timervalue;

    RelativeLayout notificationCount1;
    TextView counttext;
    TextView btnNotifCount,projecttitle;
    String countvaluetext;
    ImageView profileimage;
    int loadingstatevalue;
    private JSONArray loginresult;
    String userid;

    private String loggedinuserimage,loogedinusertypeid;
    private RequestQueue requestQueue1 = null;
    private RequestQueue requestQueue3 = null;

    private NotificationsMainAdapter adapter;
    int eventcount=0,oldposition=0,loadingstate=0,newclickevent=0;

    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;

    private String blockCharacterSet = "`~!@#$%^&*()_-+=|\\}{]['\";:?/>.<,";
    private InputFilter filterfortext = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword_xml);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        profileimage = (ImageView) findViewById(R.id.profileimage);
        projecttitle = (TextView)findViewById(R.id.projecttitle);
        submit = (Button) findViewById(R.id.submit);
        password = (EditText) findViewById(R.id.newpassword);
        retypepassword = (EditText) findViewById(R.id.retypenewpassword);
        mainview = findViewById(R.id.parentsuserinformationwholecontainer);

        previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);
        loggedinuserimage = previoussharedPreferences.getString(previousloginconfig.key_userimage, "");

        Glide.with(ChangePassword_View.this).load(loggedinuserimage).listener(new RequestListener<String, GlideDrawable>() {
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

        password.setFilters(new InputFilter[] { filterfortext });
        retypepassword.setFilters(new InputFilter[] { filterfortext });

        t = new Timer();
        timervalue =true;
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                getnotificationcount();
            }

        },5000,5000);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()){
                    return;
                }

                loginDialog = new ProgressDialog(ChangePassword_View.this);
                loginDialog.setIndeterminate(true);
                loginDialog.setTitle("Please Wait");
                loginDialog.setMessage("Processing your Change Password Request...");
                loginDialog.setCancelable(false);
                loginDialog.setCanceledOnTouchOutside(false);
                loginDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                passwordchangerequest();
                            }
                        }, 100);
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(timervalue == true) {
            t.cancel();
            t = null;
            timervalue = false;
        }

    }

    @Override
    protected void onDestroy(){
        if(loadingstate==1){
            loadingstate = 0;
            mainview.setEnabled(true);
            loginDialog.dismiss();
            if(timervalue==true){
                t.cancel();
                t = null;
                timervalue = false;
            }
        }else{
            mainview.setEnabled(true);
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

    private void passwordchangerequest() {
        loggeduserid = MainActivity.sharedPreferences.getString(loginconfig.key_userid, "");
        final String edittextpassword = password.getText().toString().trim();
        final String edittextretypepassword = retypepassword.getText().toString().trim();

        if (!(edittextretypepassword.equals("")) && !(edittextpassword.equals("")) && edittextpassword.equals(edittextretypepassword))
        {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, loginconfig.key_passwordupdate_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            JSONObject j = null;
                            try {

                                loginDialog.dismiss();
                                snackbar = Snackbar.make(mainview, "Success", Snackbar.LENGTH_LONG);
                                snackbar.show();

                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {

                                                sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                                                final String nusertypeid = sharedPreferences.getString(loginconfig.key_usertypeid, "");

                                                if (nusertypeid.equals("1")) {
                                                    Intent m = new Intent(ChangePassword_View.this, Teachers_Userinformation_View.class);
                                                    startActivity(m);
                                                    finish();
                                                }else if (nusertypeid.equals("2")) {
                                                    Intent m = new Intent(ChangePassword_View.this, Parents_Userinformation_View.class);
                                                    startActivity(m);
                                                    finish();
                                                }

                                            }
                                        }, 1000);

                            } catch (Exception e) {
                                loginDialog.dismiss();
                                snackbar = Snackbar.make(mainview, "Kindly check with administrator for this error", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    loginDialog.dismiss();
                    snackbar = Snackbar.make(mainview, "Kindly check with administrator for this error", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.clear();

                    params.put(loginconfig.key_loggeduserid, loggeduserid);
                    params.put(loginconfig.key_password, edittextpassword);

                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(ChangePassword_View.this);
            requestQueue.add(stringRequest);

        }else{
            loginDialog.dismiss();
            snackbar = Snackbar.make(mainview, "Password's doesnot match", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    private boolean validate() {
        boolean valid = true;

        String passwordvalue = password.getText().toString().trim();
        String retypepasswordvalue = retypepassword.getText().toString().trim();

        if (retypepasswordvalue.isEmpty()) {

            retypepassword.setError("Kindly enter Re-Type Password and Try Again");
            valid = false;
        } else {
            retypepassword.setError(null);
        }

        if (passwordvalue.isEmpty()) {

            password.setError("Kindly enter Password and Try Again");
            valid = false;
        } else {
            password.setError(null);
        }

        if ( passwordvalue.isEmpty() || passwordvalue.length() < 4 || password.length() > 10) {
            password.setError("Pasword should be between 4 to 10 Alphanumeric Characters");
            valid = false;
        } else {
            password.setError(null);
        }

        if ( retypepasswordvalue.length() < 4 || retypepassword.length() > 10) {
            retypepassword.setError("Pasword should be between 4 to 10 Alphanumeric Characters");
            valid = false;
        } else {
            retypepassword.setError(null);
        }

        return valid;

    }


}
