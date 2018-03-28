package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.userinformation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import com.google.android.gms.iid.InstanceID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import uk.co.yellowrays.collegeinfo.classcubby.AppStatus;
import uk.co.yellowrays.collegeinfo.classcubby.ChangePassword_View;
import uk.co.yellowrays.collegeinfo.classcubby.LoginActivity;
import uk.co.yellowrays.collegeinfo.classcubby.MainActivity;
import uk.co.yellowrays.collegeinfo.classcubby.R;
import uk.co.yellowrays.collegeinfo.classcubby.configfiles.loginconfig;

/**
 * Created by vinos on 12/30/2016.
 */


public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private String[] mDataSet;
    private int[] mDataSetTypes;
    Context context;
    String address,username,loguserid,salutation,firstname,lastname,gender,addressline1text,addressline2,userimage,cityname,pincode,contactnumber,altcontactnumber,emailid,occupation;

    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;

    private Button submit;
    private EditText password,retypepassword;
    private PopupWindow pwindow;
    private ProgressDialog loginDialog;
    private String loggeduserid;

    public static final int primary = 0;
    public static final int Values = 1;
    public static final int Contact = 2;

    private JSONArray loginresult;
    String loginid;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public CustomAdapter(Context mainActivity, String[]dataSet, int[] dataSetTypes) {
        context = mainActivity;
        mDataSet = dataSet;
        mDataSetTypes = dataSetTypes;
    }

    @Override
    public int getItemCount() {
        return this.mDataSet.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0: (position ==1 ? 1:2);
        //return mDataSetTypes[position];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());

        switch (viewType) {
            case 0:
                View v1 = inflater.inflate(R.layout.parents_userinformation_main_cardview_xml, null);
                viewHolder = new PrimaryViewHolder(v1);
                break;

            case 1:
                View v2 = inflater.inflate(R.layout.parents_userinformation_primaryinfo_cardview_xml, null);
                viewHolder = new ValuesViewHolder(v2);
                break;

            default:
                View v = inflater.inflate(R.layout.parents_userinformation_contact_cardview_xml, null);
                viewHolder = new ContactsViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case 0:
                PrimaryViewHolder vh1 = (PrimaryViewHolder) viewHolder;
                configureViewHolder1(vh1, position);
                break;

            case 1:
                ValuesViewHolder vh = (ValuesViewHolder) viewHolder;
                configureDefaultViewHolder(vh, position);
                break;

            default:
                ContactsViewHolder vh2 = (ContactsViewHolder) viewHolder;
                configurecontactsViewHolder(vh2, position);
                break;
        }
    }


    public class ValuesViewHolder extends RecyclerView.ViewHolder {
        TextView firstname,lastname,gender,Addressline1,Addressline2,city,pincode,occupation;

        public ValuesViewHolder(View v) {
            super(v);
            this.firstname = (TextView) v.findViewById(R.id.firstname);
            this.lastname = (TextView) v.findViewById(R.id.lastname);
            this.gender = (TextView) v.findViewById(R.id.gender);
            this.Addressline1 = (TextView) v.findViewById(R.id.Addressline1);
            this.Addressline2 = (TextView) v.findViewById(R.id.Addressline2);
            this.city = (TextView) v.findViewById(R.id.city);
            this.pincode = (TextView) v.findViewById(R.id.pincode);
            this.occupation = (TextView) v.findViewById(R.id.occupation);
        }

        public TextView getfirstname() {
            return firstname;
        }

        public void setfirstname(TextView label1) {
            this.firstname = label1;
        }

        public TextView getlastname() {
            return lastname;
        }

        public void setlastname(TextView label1) {
            this.lastname = label1;
        }

        public TextView getgender() {
            return gender;
        }

        public void setgender(TextView label1) {
            this.gender = label1;
        }

        public TextView getAddressline1() {
            return Addressline1;
        }

        public void setAddressline1(String label1) {
            this.Addressline1.setText(label1);
        }

        public TextView getAddressline2() {
            return Addressline2;
        }

        public void setAddressline2(TextView label1) {
            this.Addressline2 = label1;
        }

        public TextView getcity() {
            return city;
        }

        public void setcity(TextView label1) {
            this.city = label1;
        }

        public TextView getpincode() {
            return pincode;
        }

        public void setpincode(TextView label1) {
            this.pincode = label1;
        }

        public TextView getoccupation() {
            return occupation;
        }

        public void setoccupation(TextView label1) {
            this.occupation = label1;
        }

    }



    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView phonenumbertext,altphonenumbertext,emailphonenumbertext;

        public ContactsViewHolder(View v) {
            super(v);
            this.phonenumbertext = (TextView) v.findViewById(R.id.phonenumbertext);
            this.altphonenumbertext = (TextView) v.findViewById(R.id.altphonenumbertext);
            this.emailphonenumbertext = (TextView) v.findViewById(R.id.emailphonenumbertext);
        }

        public TextView getphonenumbertext() {
            return phonenumbertext;
        }

        public void setphonenumbertext(TextView label1) {
            this.phonenumbertext = label1;
        }

        public TextView getaltphonenumbertext() {
            return altphonenumbertext;
        }

        public void setaltphonenumbertext(TextView label1) {
            this.altphonenumbertext = label1;
        }

        public TextView getemailphonenumbertext() {
            return emailphonenumbertext;
        }

        public void setemailphonenumbertext(TextView label1) {
            this.emailphonenumbertext = label1;
        }

    }

    public class PrimaryViewHolder extends RecyclerView.ViewHolder {
        ImageView userimagevalue,edituserimagevalue;
        TextView primaryusername,address;
        RelativeLayout changepasswordoverallcontainer,logoutoverallcontainer;

        public PrimaryViewHolder(View v) {
            super(v);
            this.userimagevalue = (ImageView) v.findViewById(R.id.userimagevalue);
            /*this.edituserimagevalue = (ImageView) v.findViewById(R.id.edituserimagevalue);*/
            this.primaryusername = (TextView) v.findViewById(R.id.username);
            this.address = (TextView) v.findViewById(R.id.address);
            this.changepasswordoverallcontainer = (RelativeLayout) v.findViewById(R.id.changepasswordoverallcontainer);
            this.logoutoverallcontainer = (RelativeLayout) v.findViewById(R.id.logoutoverallcontainer);
        }

        public ImageView getuserimagevalue() {
            return userimagevalue;
        }

        public void setuserimagevalue(ImageView label1) {
            this.userimagevalue = label1;
        }

        public TextView getaddress() {
            return address;
        }

        public void setaddress(TextView label1) {
            this.address = label1;
        }

        public TextView getprimaryusername() {
            return primaryusername;
        }

        public void setprimaryusername(TextView label1) {
            this.primaryusername = label1;
        }
    }

    private void configureDefaultViewHolder(ValuesViewHolder vh, int position) {

        final ValuesViewHolder holder = vh;

        String newvalues1 = mDataSet[position];
        newvalues1 = newvalues1.replace("\\r", "~`");
        newvalues1 = newvalues1.replace("\\n", "~~");
        newvalues1 = newvalues1.replace("\\'", "``");
        newvalues1 = newvalues1.replace("[\"", "[");
        newvalues1 = newvalues1.replace("\"]", "]");
        newvalues1 = newvalues1.replace("\\", "");
        newvalues1 = newvalues1.replace("\\", "");
        newvalues1 = newvalues1.replace("~`", "\\r");
        newvalues1 = newvalues1.replace("~~", "\\n");
        newvalues1 = newvalues1.replace(", ", ",");
        newvalues1 = newvalues1.replace("```", ",");

        try {

            JSONObject j = new JSONObject(newvalues1);

            //JSONArray jsonArr = j.getJSONArray("");

            JSONArray jsonArr = j.getJSONArray("valueslist");

            if(jsonArr.length()>0) {
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject json = jsonArr.getJSONObject(i);
                    loguserid = json.optString("userid");
                    salutation = json.optString("salutation");
                    firstname = json.optString("firstname");
                    lastname = json.optString("lastname");
                    gender = json.optString("gender");
                    addressline1text = json.optString("addressline1");
                    addressline2 = json.optString("addressline2");
                    cityname = json.optString("cityname");
                    pincode = json.optString("pincode");
                    occupation = json.optString("occupation");
                }

            }

            vh.firstname.setText(firstname);
            vh.lastname.setText(lastname);
            vh.gender.setText(gender);
            vh.Addressline1.setText(addressline1text);

            vh.Addressline2.setText(addressline2);
            vh.city.setText(cityname);
            vh.pincode.setText(pincode);
            vh.occupation.setText(occupation);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void configureViewHolder1(final PrimaryViewHolder vh1, int position) {
        String newvalues = mDataSet[position];
        newvalues = newvalues.replace("\\r", "~`");
        newvalues = newvalues.replace("\\n", "~~");
        newvalues = newvalues.replace("\\'", "``");
        newvalues = newvalues.replace("[\"", "[");
        newvalues = newvalues.replace("\"]", "]");
        newvalues = newvalues.replace("\\", "");
        newvalues = newvalues.replace("\\", "");
        newvalues = newvalues.replace("~`", "\\r");
        newvalues = newvalues.replace("~~", "\\n");
        newvalues = newvalues.replace(", ", ",");
        newvalues = newvalues.replace("```", ",");

        try {

            JSONObject j = new JSONObject(newvalues);

            JSONArray jsonArr = j.getJSONArray("valueslist");

            if(jsonArr.length()>0) {
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject json = jsonArr.getJSONObject(i);
                    userimage = json.optString("userimage");
                    username = json.optString("username");
                    address = json.optString("address");
                }


                Glide.with(context).load(userimage).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                        Log.i("GLIDE", "onException :", e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                        Log.i("GLIDE", "onResourceReady");
                        vh1.userimagevalue.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(vh1.userimagevalue);

                vh1.primaryusername.setText(username);
                vh1.address.setText(address);

                vh1.logoutoverallcontainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                        //creating editor to store values of shared preferences
                        editor = sharedPreferences.edit();

                        // Clearing all data from Shared Preferences
                        editor.clear();

                        //Saving the sharedpreferences
                        editor.commit();

                        LocalBroadcastManager.getInstance(context).unregisterReceiver(mRegistrationBroadcastReceiver);

                        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                        previoussharedPreferences = context.getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);

                        //creating editor to store values of shared preferences
                        editor = sharedPreferences.edit();
                        previouseditor = previoussharedPreferences.edit();

                        editor.putString(loginconfig.key_gcmtokenid, "");
                        editor.apply();

                        InstanceID instanceID = InstanceID.getInstance(context);
                        try {
                            instanceID.deleteInstanceID();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ongcmregistrationsuccess();

                        Intent i = new Intent(context, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        ((Activity)context).finish();

                    }
                });

                vh1.changepasswordoverallcontainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppStatus.getInstance(context).isOnline()) {
                            Intent i = new Intent(context, ChangePassword_View.class);
                            context.startActivity(i);
                        }
                    }
                });

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void configurecontactsViewHolder(ContactsViewHolder vh2, int position) {
        String newvalues2 = mDataSet[position];
        newvalues2 = newvalues2.replace("\\r", "~`");
        newvalues2 = newvalues2.replace("\\n", "~~");
        newvalues2 = newvalues2.replace("\\'", "``");
        newvalues2 = newvalues2.replace("[\"", "[");
        newvalues2 = newvalues2.replace("\"]", "]");
        newvalues2 = newvalues2.replace("\\", "");
        newvalues2 = newvalues2.replace("\\", "");
        newvalues2 = newvalues2.replace("~`", "\\r");
        newvalues2 = newvalues2.replace("~~", "\\n");
        newvalues2 = newvalues2.replace(", ", ",");
        newvalues2 = newvalues2.replace("```", ",");

        try {

            JSONObject j = new JSONObject(newvalues2);

            //JSONArray jsonArr = j.getJSONArray("");

            JSONArray jsonArr = j.getJSONArray("valueslist");

            if(jsonArr.length()>0) {
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject json = jsonArr.getJSONObject(i);
                    contactnumber = json.optString("contactnumber");
                    altcontactnumber = json.optString("altcontactnumber");
                    emailid = json.optString("emailid");
                }

                vh2.phonenumbertext.setText(contactnumber);
                vh2.altphonenumbertext.setText(altcontactnumber);
                vh2.emailphonenumbertext.setText(emailid);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ongcmregistrationsuccess() {
        deleteCache(context);

        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        previoussharedPreferences = context.getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);
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

                params.put(loginconfig.key_userid, loginid);
                params.put(loginconfig.key_gcmtokenid, gcmtokenid);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

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
