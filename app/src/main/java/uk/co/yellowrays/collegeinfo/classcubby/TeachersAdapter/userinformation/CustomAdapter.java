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
import java.util.List;
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

        private List<UserInformationList> infolist;
        private BroadcastReceiver mRegistrationBroadcastReceiver;

        public CustomAdapter(Context mainActivity, String[]dataSet, int[] dataSetTypes, List<UserInformationList> userinfolist) {
            context = mainActivity;
            mDataSet = dataSet;
            mDataSetTypes = dataSetTypes;
            infolist = userinfolist;
        }

        @Override
        public int getItemCount() {
            return mDataSet.length;
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

                case 2:
                    View v3 = inflater.inflate(R.layout.parents_userinformation_contact_cardview_xml, null);
                    viewHolder = new ContactsViewHolder(v3);
                    break;

            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            switch (viewHolder.getItemViewType()) {
                case 0:
                    PrimaryViewHolder vh1 = (PrimaryViewHolder) viewHolder;
                    configurePrimaryViewHolder(vh1, position);
                    break;

                case 1:
                    ValuesViewHolder vh = (ValuesViewHolder) viewHolder;
                    configureValuesViewHolder(vh, position);
                    break;

                case 2:
                    ContactsViewHolder vh2 = (ContactsViewHolder) viewHolder;
                    configurecontactsViewHolder(vh2, position);
                    break;
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

        }


        private void configurePrimaryViewHolder(final PrimaryViewHolder vh1, int position) {

            Glide.with(context).load(infolist.get(0).getuserimage()).listener(new RequestListener<String, GlideDrawable>() {
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

            vh1.primaryusername.setText(infolist.get(0).getfirstname()+" "+infolist.get(position).getlastname());
            vh1.address.setText(infolist.get(0).getaddressline1()+" "+infolist.get(position).getaddressline2()+" "+ infolist.get(position).getcityname());

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

        }



        private void configureValuesViewHolder(ValuesViewHolder vh, int position) {

            vh.firstname.setText(infolist.get(0).getfirstname());
            vh.lastname.setText(infolist.get(0).getlastname());
            vh.gender.setText(infolist.get(0).getgender());
            vh.Addressline1.setText(infolist.get(0).getaddressline1());

            vh.Addressline2.setText(infolist.get(0).getaddressline2());
            vh.city.setText(infolist.get(0).getcityname());
            vh.pincode.setText(infolist.get(0).getpincode());
            vh.occupation.setText(infolist.get(0).getoccupation());

        }




        public class ContactsViewHolder extends RecyclerView.ViewHolder {
            TextView phonenumbertext,altphonenumbertext,emailphonenumbertext;

            public ContactsViewHolder(View v) {
                super(v);
                this.phonenumbertext = (TextView) v.findViewById(R.id.phonenumbertext);
                this.altphonenumbertext = (TextView) v.findViewById(R.id.altphonenumbertext);
                this.emailphonenumbertext = (TextView) v.findViewById(R.id.emailphonenumbertext);
            }

        }


        private void configurecontactsViewHolder(ContactsViewHolder vh2, int position) {

            vh2.phonenumbertext.setText(infolist.get(0).getcontactnumber());
            vh2.altphonenumbertext.setText(infolist.get(0).getaltcontactnumber());
            vh2.emailphonenumbertext.setText(infolist.get(0).getemailid());

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
